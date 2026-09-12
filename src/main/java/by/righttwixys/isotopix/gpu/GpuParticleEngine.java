package by.righttwixys.isotopix.gpu;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.config.IsotopixConfig;
import by.righttwixys.isotopix.init.ModBlocks;
import by.righttwixys.isotopix.init.ModSounds;
import by.righttwixys.isotopix.item.DosimeterItem;
import by.righttwixys.isotopix.network.PlayerParticleHitPayload;
import by.righttwixys.isotopix.network.RadiationParticleBurstPayload;
import by.righttwixys.isotopix.radiation.RadiationSourceRecord;
import by.righttwixys.isotopix.radiation.RadiationSpectra;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@EventBusSubscriber(modid = Isotopix.MODID, value = Dist.CLIENT)
public class GpuParticleEngine {

    public static int MAX_PARTICLES = 524288;
    private static final int PARTICLE_STRIDE_BYTES = 64;

    private static final int MAX_SOURCES = 256;
    private static final int SOURCE_STRIDE_BYTES = 80;

    private static final int VOXEL_DIM = 48;
    private static final int TOTAL_VOXELS = VOXEL_DIM * VOXEL_DIM * VOXEL_DIM;
    private static final ByteBuffer VOXEL_BUFFER = BufferUtils.createByteBuffer(TOTAL_VOXELS);
    private static BlockPos lastVoxelOrigin = BlockPos.ZERO;
    private static int voxelUpdateCooldown = 0;

    private static boolean initialized = false;
    private static boolean supported = false;

    private static byte visualMode = 0;

    private static int computeProgram = 0;
    private static int renderProgram = 0;
    private static int ssboBuffer = 0;
    private static int hitSsboBuffer = 0;
    private static int voxelSsboBuffer = 0;
    private static int spawnCounterBuffer = 0;
    private static int sourcesSsboBuffer = 0;
    private static int vaoId = 0;
    private static int vboAttribId = 0;

    private static int writeCursor = 0;
    private static long lastFrameNano = 0;
    private static float spawnRemainder = 0.0f;

    private static final List<RadiationSourceRecord> activeSources = new ArrayList<>();
    private static float totalSourcesWeight = 0.0f;

    private static final float[] VIEW_ARRAY = new float[16];
    private static final float[] PROJECTION_ARRAY = new float[16];

    public static void setVisualMode(byte mode) {
        visualMode = mode;
    }

    public static boolean isDebugRaysEnabled() {
        return visualMode != 0;
    }

    public static void updateActiveSources(List<RadiationSourceRecord> sources) {
        synchronized (activeSources) {
            activeSources.clear();
            float sum = 0.0f;
            int count = Math.min(sources.size(), MAX_SOURCES);
            for (int i = 0; i < count; i++) {
                RadiationSourceRecord s = sources.get(i);
                sum += s.totalEmissionWeight();
                activeSources.add(s);
            }
            totalSourcesWeight = sum;
        }
    }

    public static void resizeParticleBuffer(int newMaxParticles) {
        if (newMaxParticles <= 0 || newMaxParticles == MAX_PARTICLES) return;
        MAX_PARTICLES = newMaxParticles;

        if (supported && initialized && ssboBuffer != 0) {
            RenderSystem.recordRenderCall(() -> {
                GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, ssboBuffer);
                GL15C.glBufferData(GL43C.GL_SHADER_STORAGE_BUFFER, (long) MAX_PARTICLES * PARTICLE_STRIDE_BYTES, GL15C.GL_DYNAMIC_DRAW);
                GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, 0);
                writeCursor = 0;
                long vramMB = ((long) MAX_PARTICLES * PARTICLE_STRIDE_BYTES) / (1024L * 1024L);
                Isotopix.LOGGER.info("Resized GPU Particle buffer to {} particles ({} MB VRAM).",
                        MAX_PARTICLES, vramMB);
            });
        }
    }

    private static final String COMPUTE_SHADER_SRC = """
        #version 430 core
        layout(local_size_x = 256) in;

        struct Particle {
            vec4 pos_life;      // xyz: Pos, w: Life
            vec4 vel_maxlife;   // xyz: Vel, w: MaxLife
            vec4 props;         // x: Mass, y: Charge, z: Energy (MeV), w: Type (0: Alpha, 1: Beta, 2: Gamma, 3: Neutron, 5: Positron)
            vec4 color;         // rgb: Color, a: Radius / Dose
        };

        struct GpuSource {
            vec4 pos_emitterId;
            vec4 emissionRates;
            vec4 doses;
            vec4 energies;
            vec4 weights;
        };

        layout(std430, binding = 0) buffer ParticleBuffer {
            Particle particles[];
        };

        layout(std430, binding = 1) buffer HitBuffer {
            uint u_HitDoseMicroR;
            uint u_HitDosePicoR;
            uint u_HitCount;
            uint u_DominantType;
        };

        layout(std430, binding = 2) buffer VoxelGridBuffer {
            uint u_Voxels[];
        };

        layout(std430, binding = 3) buffer SpawnCounterBuffer {
            uint u_GpuSpawnCounter;
        };

        layout(std430, binding = 4) buffer SourcesBuffer {
            GpuSource u_Sources[];
        };

        uniform float u_DeltaTime;
        uniform int u_TotalParticles;

        uniform vec3 u_PlayerMin;
        uniform vec3 u_PlayerMax;
        uniform int u_HasPlayer;
        uniform float u_LocalPlayerId;

        uniform ivec3 u_VoxelOrigin;

        uniform uint u_NumSources;
        uniform float u_TotalSourcesWeight;
        uniform uint u_SpawnQuota;

        float rand(inout uint state) {
            state = state * 747796405u + 2891336453u;
            uint word = ((state >> ((state >> 28u) + 4u)) ^ state) * 277803737u;
            return float((word >> 22u) ^ word) / 4294967295.0;
        }

        uint getVoxelMaterial(ivec3 bPos) {
            ivec3 local = bPos - u_VoxelOrigin;
            if (local.x < 0 || local.x >= 48 || local.y < 0 || local.y >= 48 || local.z < 0 || local.z >= 48) {
                return 0u;
            }
            int idx = local.x + local.y * 48 + local.z * 2304;
            uint word = u_Voxels[idx >> 2];
            return (word >> ((idx & 3) * 8)) & 0xFFu;
        }

        vec3 deflectVector(vec3 v, float theta, float phi) {
            vec3 w = normalize(v);
            vec3 a = abs(w.x) > 0.1 ? vec3(0.0, 1.0, 0.0) : vec3(1.0, 0.0, 0.0);
            vec3 u = normalize(cross(w, a));
            vec3 vPerp = cross(w, u);
            return normalize(u * (sin(theta) * cos(phi)) + vPerp * (sin(theta) * sin(phi)) + w * cos(theta)) * length(v);
        }

        bool lineIntersectsAABB(vec3 p0, vec3 p1, vec3 bMin, vec3 bMax) {
            if (all(greaterThanEqual(p0, bMin)) && all(lessThanEqual(p0, bMax))) {
                return true;
            }
            vec3 d = p1 - p0;
            vec3 safeD = vec3(
                abs(d.x) < 1e-5 ? (d.x >= 0.0 ? 1e-5 : -1e-5) : d.x,
                abs(d.y) < 1e-5 ? (d.y >= 0.0 ? 1e-5 : -1e-5) : d.y,
                abs(d.z) < 1e-5 ? (d.z >= 0.0 ? 1e-5 : -1e-5) : d.z
            );
            vec3 invD = 1.0 / safeD;
            vec3 t0 = (bMin - p0) * invD;
            vec3 t1 = (bMax - p0) * invD;
            vec3 tNear = min(t0, t1);
            vec3 tFar  = max(t0, t1);
            float tEnter = max(max(tNear.x, tNear.y), tNear.z);
            float tExit  = min(min(tFar.x, tFar.y), tFar.z);
            return tEnter <= tExit && tExit >= 0.0 && tEnter <= 1.0;
        }

        void emitSecondary(vec3 pos, vec3 vel, float life, float mass, float charge, float energy, float pType, vec4 col) {
            uint slot = atomicAdd(u_GpuSpawnCounter, 1u) % uint(u_TotalParticles);
            Particle sec;
            sec.pos_life = vec4(pos, life);
            sec.vel_maxlife = vec4(vel, life);
            sec.props = vec4(mass, charge, energy, pType);
            sec.color = col;
            particles[slot] = sec;
        }

        void main() {
            uint id = gl_GlobalInvocationID.x;
            if (id >= uint(u_TotalParticles)) return;

            Particle p = particles[id];

            if (isnan(p.pos_life.w) || isinf(p.pos_life.w) || p.pos_life.w < 0.0 || p.pos_life.w > 100.0) {
                p.pos_life.w = 0.0;
            }

            uint rngState = id + uint(p.pos_life.w * 10000.0) + uint(u_DeltaTime * 1000000.0) + 1337u;


            if (p.pos_life.w <= 0.0) {
                if (u_NumSources > 0u && u_TotalSourcesWeight > 0.01 && atomicAdd(u_GpuSpawnCounter, 1u) < u_SpawnQuota) {
                    float rPick = rand(rngState) * u_TotalSourcesWeight;
                    uint srcIdx = 0u;
                    for (uint s = 0u; s < u_NumSources; s++) {
                        if (rPick <= u_Sources[s].weights.y || s == u_NumSources - 1u) {
                            srcIdx = s;
                            break;
                        }
                    }
                    GpuSource src = u_Sources[srcIdx];

                    float sumRates = src.emissionRates.x + src.emissionRates.y + src.emissionRates.z + src.emissionRates.w;
                    float rType = rand(rngState) * max(0.001, sumRates);
                    uint pType = 1u;
                    if (rType < src.emissionRates.x) pType = 0u;
                    else if (rType < src.emissionRates.x + src.emissionRates.y) pType = 1u;
                    else if (rType < src.emissionRates.x + src.emissionRates.y + src.emissionRates.z) pType = 2u;
                    else pType = 3u;

                    float baseSpeed = 35.0;
                    float lifetime = 3.20;
                    float mass = 0.00055;
                    float charge = -1.0;
                    vec3 col = vec3(0.05, 0.88, 1.0);
                    float nominalE = max(0.01, src.energies[pType]);
                    float sampledE = nominalE;

                    if (pType == 0u) {
                        mass = 4.0; charge = 2.0;
                        baseSpeed = 12.0; lifetime = 0.005;
                        col = vec3(1.0, 0.12, 0.12);
                        sampledE = max(0.5, nominalE + (rand(rngState) - 0.5) * 0.08);
                    } else if (pType == 1u) {
                        mass = 0.00055; charge = -1.0;
                        baseSpeed = 35.0; lifetime = 3.20;
                        col = vec3(0.05, 0.88, 1.0);
                        float xi1 = rand(rngState); float xi2 = rand(rngState); float xi3 = rand(rngState);
                        sampledE = nominalE * (xi1 + xi2 + xi3) * 0.3333;
                    } else if (pType == 2u) {
                        mass = 0.00001; charge = 0.0;
                        baseSpeed = 75.0; lifetime = 12.0;
                        col = vec3(0.85, 0.20, 1.0);
                        if (rand(rngState) < 0.30) sampledE = nominalE * (0.25 + rand(rngState) * 0.75);
                    } else if (pType == 3u) {
                        mass = 1.0; charge = 0.0;
                        baseSpeed = 55.0; lifetime = 12.0;
                        col = vec3(1.0, 0.92, 0.35);
                        float xiA = max(1e-6, rand(rngState));
                        sampledE = -1.025 * log(xiA) + (rand(rngState) * 0.85);
                    }

                    if (src.pos_emitterId.w >= 0.0 && abs(src.pos_emitterId.w - u_LocalPlayerId) < 0.1) {
                        mass = -mass;
                    }

                    float speedMod = sqrt(max(0.1, sampledE / nominalE));
                    float speed = baseSpeed * clamp(speedMod, 0.4, 2.0) * (0.92 + rand(rngState) * 0.16);

                    float uCos = rand(rngState) * 2.0 - 1.0;
                    float uPhi = rand(rngState) * 6.2831853;
                    float uR = sqrt(max(0.0, 1.0 - uCos * uCos));
                    vec3 dir = vec3(uR * cos(uPhi), uCos, uR * sin(uPhi));
                    vec3 vel = dir * speed;

                    vec3 spawnPos = src.pos_emitterId.xyz + (vec3(rand(rngState), rand(rngState), rand(rngState)) - 0.5) * 0.08;
                    float doseNanoR = max(0.0001, src.doses[pType] * (sampledE / nominalE));

                    p.pos_life = vec4(spawnPos, lifetime);
                    p.vel_maxlife = vec4(vel, lifetime);
                    p.props = vec4(mass, charge, sampledE, float(pType));
                    p.color = vec4(col, doseNanoR);
                    particles[id] = p;
                } else {
                    particles[id] = p;
                    return;
                }
            }

            vec3 myPos = p.pos_life.xyz;
            vec3 myVel = p.vel_maxlife.xyz;
            float rawMass = p.props.x;
            float myMass = max(0.0001, abs(rawMass));
            bool isFromOwnInventory = (rawMass < 0.0);
            float myCharge = p.props.y;
            float energy = p.props.z;
            uint pType = uint(p.props.w);
            float nanoDose = max(0.0001, p.color.a);

            vec3 force = vec3(0.0);

            if (pType == 0u) {
                float ageAlpha = p.vel_maxlife.w - p.pos_life.w;
                if (ageAlpha > 0.005) {
                    p.pos_life.w = 0.0;
                    particles[id] = p;
                    return;
                }
            }

            if (pType == 3u) {
                float lambdaNeutron = 0.001136;
                float decayProb = 1.0 - exp(-lambdaNeutron * 15.0 * u_DeltaTime);
                if (rand(rngState) < decayProb) {
                    p.pos_life.w = 0.0;
                    float eBeta = 0.782 * pow(rand(rngState), 1.33);
                    vec3 betaDir = normalize(vec3(rand(rngState)-0.5, rand(rngState)-0.5, rand(rngState)-0.5));
                    emitSecondary(myPos, betaDir * 35.0, 3.20, 0.00055, -1.0, eBeta, 1.0, vec4(0.05, 0.88, 1.0, 0.02));
                    particles[id] = p;
                    return;
                }
            }

            if (myCharge != 0.0) {
                vec3 bAxis = normalize(vec3(0.0, -0.35, -0.93));
                float gyroRate = (pType == 1u || pType == 5u) ? (myCharge * 2.8) : ((myCharge * 0.45) / myMass);
                float gyroAngle = gyroRate * u_DeltaTime;

                vec3 vRot = myVel * cos(gyroAngle) + cross(bAxis, myVel) * sin(gyroAngle) + bAxis * dot(bAxis, myVel) * (1.0 - cos(gyroAngle));
                myVel = vRot;
                force += vec3(0.0, -0.65 * myCharge, 0.0);
            }

            force = clamp(force, vec3(-25.0), vec3(25.0));
            float effMass = (pType == 1u || pType == 5u) ? 0.85 : myMass;
            myVel += (force / effMass) * u_DeltaTime;

            float dragCoeff = 0.025;
            if (pType == 0u) dragCoeff = 3.5;
            else if (pType == 1u || pType == 5u) dragCoeff = 0.18;

            myVel *= max(0.0, 1.0 - dragCoeff * u_DeltaTime);

            float speed = length(myVel);
            if (speed > 85.0) {
                myVel = (myVel / speed) * 85.0;
            }

            vec3 nextPos = myPos + myVel * u_DeltaTime;

            if (!isFromOwnInventory && u_HasPlayer == 1) {
                if (lineIntersectsAABB(myPos, nextPos, u_PlayerMin, u_PlayerMax)) {
                    p.pos_life.w = 0.0;
                    float safeDoseNano = max(0.0001, nanoDose);

                    float wR = 1.0;
                    if (pType == 0u) wR = 20.0;
                    else if (pType == 3u) {
                        wR = (energy > 0.1) ? 12.0 : 3.0;
                    }
                    safeDoseNano *= wR;

                    uint wholeMicro = uint(safeDoseNano * 0.001);
                    float remNano = safeDoseNano - float(wholeMicro) * 1000.0;
                    uint picoUnits = uint(clamp(remNano * 1000.0, 0.0, 1000000.0));

                    if (wholeMicro > 0u) atomicAdd(u_HitDoseMicroR, wholeMicro);
                    if (picoUnits > 0u) atomicAdd(u_HitDosePicoR, picoUnits);
                    atomicAdd(u_HitCount, 1u);
                    u_DominantType = pType;

                    particles[id] = p;
                    return;
                }
            }

            ivec3 bOld = ivec3(floor(myPos));
            ivec3 bNew = ivec3(floor(nextPos));
            uint curMat = getVoxelMaterial(bNew);

            vec3 exactFaceNormal = vec3(0.0);
            if (bNew.x != bOld.x) exactFaceNormal.x = float(bOld.x - bNew.x);
            else if (bNew.y != bOld.y) exactFaceNormal.y = float(bOld.y - bNew.y);
            else if (bNew.z != bOld.z) exactFaceNormal.z = float(bOld.z - bNew.z);
            if (dot(exactFaceNormal, exactFaceNormal) > 0.1) {
                exactFaceNormal = normalize(exactFaceNormal);
            } else {
                exactFaceNormal = -normalize(myVel + vec3(0.0001));
            }

            if (curMat > 0u) {
                float rho = 2.4;
                float zEff = 13.0;
                float hFrac = 0.0;

                if (curMat == 1u) { rho = 1.0; zEff = 7.4; hFrac = 1.0; }
                else if (curMat == 2u) { rho = 0.6; zEff = 6.5; hFrac = 0.8; }
                else if (curMat == 3u) { rho = 2.4; zEff = 13.0; hFrac = 0.0; }
                else if (curMat == 4u) { rho = 7.8; zEff = 26.0; hFrac = 0.0; }
                else if (curMat == 5u) { rho = 11.34; zEff = 82.0; hFrac = 0.0; }

                float stepDist = length(myVel) * u_DeltaTime;

                if (pType == 0u) {
                    float stoppingPower = (rho * 45.0) / max(0.15, energy);
                    energy -= stoppingPower * stepDist;
                    if (energy <= 0.10) {
                        p.pos_life.w = 0.0;
                        particles[id] = p;
                        return;
                    }
                } else if (pType == 1u || pType == 5u) {
                    energy -= rho * 1.8 * stepDist;
                    if (energy <= 0.05) {
                        if (pType == 5u) {
                            vec3 annDir = normalize(vec3(rand(rngState)-0.5, rand(rngState)-0.5, rand(rngState)-0.5));
                            emitSecondary(myPos, annDir * 75.0, 10.0, 0.00001, 0.0, 0.511, 2.0, vec4(0.85, 0.20, 1.0, 0.05));
                            emitSecondary(myPos, -annDir * 75.0, 10.0, 0.00001, 0.0, 0.511, 2.0, vec4(0.85, 0.20, 1.0, 0.05));
                        }
                        p.pos_life.w = 0.0;
                        particles[id] = p;
                        return;
                    }
                    float thetaMoliere = 0.35 * sqrt(rho * stepDist);
                    float phiMoliere = rand(rngState) * 6.2831853;
                    myVel = deflectVector(myVel, thetaMoliere, phiMoliere) * 0.70;
                } else if (pType == 2u) {
                    if (energy > 1.022 && zEff >= 26.0) {
                        float pairProb = (1.5e-4 * zEff * zEff) * (energy - 1.022);
                        if (rand(rngState) < pairProb * stepDist) {
                            p.pos_life.w = 0.0;
                            float kineticRem = energy - 1.022;
                            float eMinusE = kineticRem * (0.2 + rand(rngState) * 0.6);
                            float ePlusE = kineticRem - eMinusE;
                            vec3 pairDir = normalize(myVel);
                            emitSecondary(myPos, deflectVector(pairDir, 0.15, rand(rngState)*6.28) * 40.0, 3.20, 0.00055, -1.0, eMinusE, 1.0, vec4(0.05, 0.88, 1.0, 0.02));
                            emitSecondary(myPos, deflectVector(pairDir, 0.15, rand(rngState)*6.28 + 3.14) * 40.0, 3.20, 0.00055, 1.0, ePlusE, 5.0, vec4(0.20, 1.0, 0.40, 0.02));
                            particles[id] = p;
                            return;
                        }
                    }

                    float mu = rho * (0.045 + 0.0018 * zEff);
                    float mfp = mu * stepDist;
                    float buildupFactorB = 1.0 + 1.2 * mfp;
                    float interactProb = (1.0 - exp(-mu * stepDist * 1.8)) / buildupFactorB;

                    if (rand(rngState) < interactProb) {
                        float peRatio = clamp((zEff * zEff * zEff) / (zEff * zEff * zEff + 3000.0 * energy), 0.02, 0.95);
                        if (rand(rngState) < peRatio) {
                            p.pos_life.w = 0.0;
                            particles[id] = p;
                            return;
                        } else {
                            float cosTheta = 1.0 - 2.0 * pow(rand(rngState), 1.5);
                            float ePrime = energy / (1.0 + (energy / 0.511) * (1.0 - cosTheta));
                            energy = ePrime;
                            float phi = rand(rngState) * 6.2831853;
                            myVel = deflectVector(myVel, acos(clamp(cosTheta, -1.0, 1.0)), phi) * (ePrime / max(0.01, energy));
                        }
                    }
                } else if (pType == 3u) {
                    float sigma = (hFrac > 0.1) ? (rho * 0.18) : (rho * 0.035);
                    if (rand(rngState) < (1.0 - exp(-sigma * stepDist * 2.0))) {
                        if (hFrac > 0.1) {
                            float retain = 0.15 + rand(rngState) * 0.35;
                            myVel = deflectVector(myVel, rand(rngState) * 3.1415, rand(rngState) * 6.28) * retain;
                            energy *= retain;
                            if (energy < 0.0001 && rand(rngState) < 0.45) {
                                p.pos_life.w = 0.0;
                                vec3 capDir = normalize(vec3(rand(rngState)-0.5, rand(rngState)-0.5, rand(rngState)-0.5));
                                emitSecondary(myPos, capDir * 75.0, 10.0, 0.00001, 0.0, 7.5, 2.0, vec4(0.85, 0.20, 1.0, 0.15));
                                particles[id] = p;
                                return;
                            }
                        } else {
                            myPos += exactFaceNormal * 0.06;
                            myVel = reflect(myVel, exactFaceNormal) * 0.95;
                        }
                    }
                }
            }

            p.props.z = energy;
            myPos += myVel * u_DeltaTime;
            p.pos_life.w -= u_DeltaTime;
            p.pos_life.xyz = myPos;
            p.vel_maxlife.xyz = myVel;

            particles[id] = p;
        }
    """;

    private static final String VERTEX_SHADER_SRC = """
        #version 430 core

        struct Particle {
            vec4 pos_life;
            vec4 vel_maxlife;
            vec4 props;
            vec4 color;
        };

        layout(std430, binding = 0) buffer ParticleBuffer {
            Particle particles[];
        };

        layout(location = 0) in float a_VertexT;

        uniform mat4 u_ViewMatrix;
        uniform mat4 u_ProjectionMatrix;
        uniform vec3 u_CameraPos;

        out vec4 v_Color;

        void main() {
            int particleId = gl_InstanceID;
            Particle p = particles[particleId];

            if (p.pos_life.w <= 0.0 || p.vel_maxlife.w <= 0.0) {
                gl_Position = vec4(2.0, 2.0, 2.0, 1.0);
                v_Color = vec4(0.0);
                return;
            }

            vec3 relPos = p.pos_life.xyz - u_CameraPos;

            if (dot(relPos, relPos) > 65536.0) {
                gl_Position = vec4(2.0, 2.0, 2.0, 1.0);
                v_Color = vec4(0.0);
                return;
            }

            vec3 vel = p.vel_maxlife.xyz;
            float lifeFraction = clamp(p.pos_life.w / p.vel_maxlife.w, 0.0, 1.0);

            float speed = length(vel);
            float streakLength = clamp(speed * 0.035, 0.02, 0.85);
            vec3 dir = speed > 0.001 ? normalize(vel) : vec3(0.0, 1.0, 0.0);

            if (a_VertexT > 0.5) {
                relPos -= dir * streakLength;
            }

            vec4 viewPos = u_ViewMatrix * vec4(relPos, 1.0);
            vec4 clipPos = u_ProjectionMatrix * viewPos;

            if (clipPos.w <= 0.001 ||
                clipPos.x < -clipPos.w * 1.65 || clipPos.x > clipPos.w * 1.65 ||
                clipPos.y < -clipPos.w * 1.65 || clipPos.y > clipPos.w * 1.65 ||
                clipPos.z < -clipPos.w * 1.2 || clipPos.z > clipPos.w * 1.2) {
                gl_Position = vec4(2.0, 2.0, 2.0, 1.0);
                v_Color = vec4(0.0);
                return;
            }

            float alphaMod = a_VertexT < 0.5 ? 0.95 : 0.20;
            v_Color = vec4(p.color.rgb, lifeFraction * alphaMod);
            gl_Position = clipPos;
        }
    """;

    private static final String FRAGMENT_SHADER_SRC = """
        #version 430 core
        in vec4 v_Color;
        out vec4 fragColor;

        void main() {
            if (v_Color.a <= 0.01) discard;
            fragColor = v_Color;
        }
    """;

    public static void init() {
        if (initialized) return;
        initialized = true;

        GLCapabilities caps = GL.getCapabilities();
        supported = caps.OpenGL43;

        if (!supported) {
            Isotopix.LOGGER.warn("OpenGL 4.3 is not supported. GPU Particle Engine disabled.");
            return;
        }

        try {
            int cs = compileShader(GL43C.GL_COMPUTE_SHADER, COMPUTE_SHADER_SRC);
            computeProgram = GL20C.glCreateProgram();
            GL20C.glAttachShader(computeProgram, cs);
            GL20C.glLinkProgram(computeProgram);
            checkProgramLink(computeProgram);
            GL20C.glDeleteShader(cs);

            int vs = compileShader(GL20C.GL_VERTEX_SHADER, VERTEX_SHADER_SRC);
            int fs = compileShader(GL20C.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_SRC);
            renderProgram = GL20C.glCreateProgram();
            GL20C.glAttachShader(renderProgram, vs);
            GL20C.glAttachShader(renderProgram, fs);
            GL20C.glLinkProgram(renderProgram);
            checkProgramLink(renderProgram);
            GL20C.glDeleteShader(vs);
            GL20C.glDeleteShader(fs);

            int cfgParticles = 524288;
            try {
                if (IsotopixConfig.MAX_PARTICLES != null) {
                    cfgParticles = IsotopixConfig.MAX_PARTICLES.get();
                }
            } catch (Exception ignored) {}
            MAX_PARTICLES = cfgParticles;

            ssboBuffer = GL15C.glGenBuffers();
            GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, ssboBuffer);
            GL15C.glBufferData(GL43C.GL_SHADER_STORAGE_BUFFER, (long) MAX_PARTICLES * PARTICLE_STRIDE_BYTES, GL15C.GL_DYNAMIC_DRAW);

            hitSsboBuffer = GL15C.glGenBuffers();
            GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, hitSsboBuffer);
            ByteBuffer zeroHits = BufferUtils.createByteBuffer(16);
            GL15C.glBufferData(GL43C.GL_SHADER_STORAGE_BUFFER, zeroHits, GL15C.GL_DYNAMIC_DRAW);

            voxelSsboBuffer = GL15C.glGenBuffers();
            GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, voxelSsboBuffer);
            GL15C.glBufferData(GL43C.GL_SHADER_STORAGE_BUFFER, (long) TOTAL_VOXELS, GL15C.GL_DYNAMIC_DRAW);

            spawnCounterBuffer = GL15C.glGenBuffers();
            GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, spawnCounterBuffer);
            ByteBuffer zeroSpawn = BufferUtils.createByteBuffer(16);
            GL15C.glBufferData(GL43C.GL_SHADER_STORAGE_BUFFER, zeroSpawn, GL15C.GL_DYNAMIC_DRAW);

            sourcesSsboBuffer = GL15C.glGenBuffers();
            GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, sourcesSsboBuffer);
            GL15C.glBufferData(GL43C.GL_SHADER_STORAGE_BUFFER, (long) MAX_SOURCES * SOURCE_STRIDE_BYTES, GL15C.GL_DYNAMIC_DRAW);
            GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, 0);

            vaoId = GL30C.glGenVertexArrays();
            GL30C.glBindVertexArray(vaoId);

            vboAttribId = GL15C.glGenBuffers();
            FloatBuffer vboData = BufferUtils.createFloatBuffer(2);
            vboData.put(0.0f).put(1.0f);
            vboData.flip();

            GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, vboAttribId);
            GL15C.glBufferData(GL15C.GL_ARRAY_BUFFER, vboData, GL15C.GL_STATIC_DRAW);

            GL20C.glEnableVertexAttribArray(0);
            GL20C.glVertexAttribPointer(0, 1, GL11C.GL_FLOAT, false, 0, 0L);

            GL30C.glBindVertexArray(0);
            GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, 0);

            long vramMB = ((long) MAX_PARTICLES * PARTICLE_STRIDE_BYTES) / (1024L * 1024L);
            Isotopix.LOGGER.info("GPU Particle Engine initialized with {} particles ({} MB VRAM).",
                    MAX_PARTICLES, vramMB);
        } catch (Exception e) {
            Isotopix.LOGGER.error("Failed to initialize GPU Particle Engine: ", e);
            supported = false;
        }
    }

    public static boolean clientHasDosimeter(Player player) {
        if (player == null) return false;
        if (player.getMainHandItem().getItem() instanceof DosimeterItem ||
                player.getOffhandItem().getItem() instanceof DosimeterItem) {
            return true;
        }
        for (ItemStack item : player.getInventory().items) {
            if (item.getItem() instanceof DosimeterItem) {
                return true;
            }
        }
        return false;
    }

    private static void updateVoxelGrid(ClientLevel level, BlockPos playerPos) {
        BlockPos origin = playerPos.offset(-24, -16, -24);
        if (origin.equals(lastVoxelOrigin) && voxelUpdateCooldown-- > 0) {
            return;
        }
        voxelUpdateCooldown = 4;
        lastVoxelOrigin = origin;

        VOXEL_BUFFER.clear();
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();

        for (int z = 0; z < VOXEL_DIM; z++) {
            for (int y = 0; y < VOXEL_DIM; y++) {
                for (int x = 0; x < VOXEL_DIM; x++) {
                    mpos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    BlockState state = level.getBlockState(mpos);

                    if (state.isAir()) {
                        VOXEL_BUFFER.put((byte) 0);
                    } else if (state.is(Blocks.WATER) || state.getFluidState().is(FluidTags.WATER)) {
                        VOXEL_BUFFER.put((byte) 1);
                    } else if (state.getBlock().toString().contains("wood") || state.getBlock().toString().contains("planks") || state.getBlock().toString().contains("leaves")) {
                        VOXEL_BUFFER.put((byte) 2);
                    } else if (state.is(Blocks.IRON_BLOCK) || state.is(Blocks.ANVIL) || state.is(Blocks.IRON_BARS)) {
                        VOXEL_BUFFER.put((byte) 4);
                    } else if (state.is(Blocks.GOLD_BLOCK) || state.is(ModBlocks.LEAD_BLOCK.get()) || state.getBlock().toString().contains("lead")) {
                        VOXEL_BUFFER.put((byte) 5);
                    } else {
                        VOXEL_BUFFER.put((byte) 3);
                    }
                }
            }
        }

        VOXEL_BUFFER.flip();
        GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, voxelSsboBuffer);
        GL15C.glBufferSubData(GL43C.GL_SHADER_STORAGE_BUFFER, 0, VOXEL_BUFFER);
        GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, 0);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (!initialized) {
            init();
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES || !supported) {
            return;
        }

        long now = System.nanoTime();
        if (lastFrameNano == 0) lastFrameNano = now;
        float dt = (now - lastFrameNano) / 1_000_000_000.0f;
        lastFrameNano = now;
        dt = Math.min(0.08f, Math.max(0.001f, dt));

        Player localPlayer = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;

        if (localPlayer != null && level != null) {
            updateVoxelGrid(level, localPlayer.blockPosition());
        }

        int numSources;
        float totalWeight;
        ByteBuffer sourceUploadBuffer = BufferUtils.createByteBuffer(MAX_SOURCES * SOURCE_STRIDE_BYTES);

        synchronized (activeSources) {
            numSources = Math.min(activeSources.size(), MAX_SOURCES);
            totalWeight = totalSourcesWeight;
            float cumulative = 0.0f;

            for (int i = 0; i < numSources; i++) {
                RadiationSourceRecord s = activeSources.get(i);
                cumulative += s.totalEmissionWeight();

                sourceUploadBuffer.putFloat(s.posX()).putFloat(s.posY()).putFloat(s.posZ()).putFloat((float) s.emitterId());
                sourceUploadBuffer.putFloat(s.alphaRate()).putFloat(s.betaRate()).putFloat(s.gammaRate()).putFloat(s.neutronRate());
                sourceUploadBuffer.putFloat(s.alphaDoseNanoR()).putFloat(s.betaDoseNanoR()).putFloat(s.gammaDoseNanoR()).putFloat(s.neutronDoseNanoR());
                sourceUploadBuffer.putFloat(s.alphaEnergyMeV()).putFloat(s.betaEnergyMeV()).putFloat(s.gammaEnergyMeV()).putFloat(s.neutronEnergyMeV());
                sourceUploadBuffer.putFloat(s.totalEmissionWeight()).putFloat(cumulative).putFloat(3.0f).putFloat(0.0f);
            }
        }

        sourceUploadBuffer.flip();
        if (numSources > 0) {
            GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, sourcesSsboBuffer);
            GL15C.glBufferSubData(GL43C.GL_SHADER_STORAGE_BUFFER, 0, sourceUploadBuffer);
        }

        ByteBuffer zeroSpawn = BufferUtils.createByteBuffer(4);
        zeroSpawn.putInt(0).flip();
        GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, spawnCounterBuffer);
        GL15C.glBufferSubData(GL43C.GL_SHADER_STORAGE_BUFFER, 0, zeroSpawn);

        float spawnMultiplier;
        if (MAX_PARTICLES >= 16777216) {
            spawnMultiplier = 160.0f;
        } else if (MAX_PARTICLES >= 2097152) {
            spawnMultiplier = 80.0f;
        } else if (MAX_PARTICLES >= 524288) {
            spawnMultiplier = 45.0f;
        } else {
            spawnMultiplier = 25.0f;
        }

        float toSpawn = (totalWeight * spawnMultiplier) * dt + spawnRemainder;
        int spawnQuota = (int) toSpawn;
        spawnRemainder = toSpawn - spawnQuota;
        spawnQuota = Math.min(MAX_PARTICLES / 8, Math.max(0, spawnQuota));

        GL20C.glUseProgram(computeProgram);
        GL30C.glBindBufferBase(GL43C.GL_SHADER_STORAGE_BUFFER, 0, ssboBuffer);
        GL30C.glBindBufferBase(GL43C.GL_SHADER_STORAGE_BUFFER, 1, hitSsboBuffer);
        GL30C.glBindBufferBase(GL43C.GL_SHADER_STORAGE_BUFFER, 2, voxelSsboBuffer);
        GL30C.glBindBufferBase(GL43C.GL_SHADER_STORAGE_BUFFER, 3, spawnCounterBuffer);
        GL30C.glBindBufferBase(GL43C.GL_SHADER_STORAGE_BUFFER, 4, sourcesSsboBuffer);

        GL20C.glUniform1f(GL20C.glGetUniformLocation(computeProgram, "u_DeltaTime"), dt);
        GL20C.glUniform1i(GL20C.glGetUniformLocation(computeProgram, "u_TotalParticles"), MAX_PARTICLES);

        BlockPos vOrigin = lastVoxelOrigin;
        GL20C.glUniform3i(GL20C.glGetUniformLocation(computeProgram, "u_VoxelOrigin"), vOrigin.getX(), vOrigin.getY(), vOrigin.getZ());

        GL30C.glUniform1ui(GL20C.glGetUniformLocation(computeProgram, "u_NumSources"), numSources);
        GL20C.glUniform1f(GL20C.glGetUniformLocation(computeProgram, "u_TotalSourcesWeight"), totalWeight);
        GL30C.glUniform1ui(GL20C.glGetUniformLocation(computeProgram, "u_SpawnQuota"), spawnQuota);

        boolean isPlayerSpectator = (localPlayer != null && localPlayer.isSpectator());
        if (localPlayer != null && !isPlayerSpectator) {
            AABB aabb = localPlayer.getBoundingBox().inflate(0.06);
            GL20C.glUniform3f(GL20C.glGetUniformLocation(computeProgram, "u_PlayerMin"), (float) aabb.minX, (float) aabb.minY, (float) aabb.minZ);
            GL20C.glUniform3f(GL20C.glGetUniformLocation(computeProgram, "u_PlayerMax"), (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
            GL20C.glUniform1i(GL20C.glGetUniformLocation(computeProgram, "u_HasPlayer"), 1);
            GL20C.glUniform1f(GL20C.glGetUniformLocation(computeProgram, "u_LocalPlayerId"), (float) localPlayer.getId());
        } else {
            GL20C.glUniform1i(GL20C.glGetUniformLocation(computeProgram, "u_HasPlayer"), 0);
            GL20C.glUniform1f(GL20C.glGetUniformLocation(computeProgram, "u_LocalPlayerId"), -1.0f);
        }

        int workgroups = (MAX_PARTICLES + 255) / 256;
        GL43C.glDispatchCompute(workgroups, 1, 1);

        GL42C.glMemoryBarrier(GL43C.GL_SHADER_STORAGE_BARRIER_BIT | GL42C.GL_BUFFER_UPDATE_BARRIER_BIT);

        GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, hitSsboBuffer);
        ByteBuffer hitData = BufferUtils.createByteBuffer(16);
        GL15C.glGetBufferSubData(GL43C.GL_SHADER_STORAGE_BUFFER, 0, hitData);

        long doseMicroR = Integer.toUnsignedLong(hitData.getInt(0));
        long dosePicoR  = Integer.toUnsignedLong(hitData.getInt(4));
        int hitCount    = Math.max(0, hitData.getInt(8));
        int dominantType = hitData.getInt(12);

        ByteBuffer zeroHits = BufferUtils.createByteBuffer(16);
        GL15C.glBufferSubData(GL43C.GL_SHADER_STORAGE_BUFFER, 0, zeroHits);
        GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, 0);

        if (hitCount > 0 && localPlayer != null && !isPlayerSpectator) {
            if (clientHasDosimeter(localPlayer)) {
                int clicksToPlay = Math.min(8, hitCount);
                for (int c = 0; c < clicksToPlay; c++) {
                    float pitch = 0.90f + (float) (Math.random() * 0.22f);
                    Minecraft.getInstance().getSoundManager().play(
                            SimpleSoundInstance.forUI(ModSounds.GEIGER_CLICK.get(), pitch, 0.50f)
                    );
                }
            }

            double totalRoentgen = ((double) doseMicroR * 1.0e-6) + ((double) dosePicoR * 1.0e-12);
            float doseRoentgen = (float) Math.max(0.0, totalRoentgen);

            PacketDistributor.sendToServer(new PlayerParticleHitPayload(doseRoentgen, hitCount, (byte) dominantType));
        }

        if (localPlayer != null && clientHasDosimeter(localPlayer)) {
            synchronized (activeSources) {
                for (RadiationSourceRecord s : activeSources) {
                    if (s.emitterId() == localPlayer.getId() && s.totalEmissionWeight() > 0.01f) {
                        float clickProb = Math.min(1.0f, (s.totalEmissionWeight() * 0.04f) * dt);
                        if (Math.random() < clickProb) {
                            float pitch = 0.90f + (float) (Math.random() * 0.22f);
                            Minecraft.getInstance().getSoundManager().play(
                                    SimpleSoundInstance.forUI(ModSounds.GEIGER_CLICK.get(), pitch, 0.50f)
                            );
                        }
                        break;
                    }
                }
            }
        }

        if (isDebugRaysEnabled()) {
            Camera camera = event.getCamera();
            Vec3 camPos = camera.getPosition();

            Matrix4f trueViewMatrix = new Matrix4f()
                    .rotationX(camera.getXRot() * ((float) Math.PI / 180.0f))
                    .rotateY((camera.getYRot() + 180.0f) * ((float) Math.PI / 180.0f));
            Matrix4f projectionMatrix = event.getProjectionMatrix();

            trueViewMatrix.get(VIEW_ARRAY);
            projectionMatrix.get(PROJECTION_ARRAY);

            int prevVao = GL11C.glGetInteger(GL30C.GL_VERTEX_ARRAY_BINDING);

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            RenderSystem.depthMask(false);
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(GL11C.GL_LEQUAL);

            GL20C.glUseProgram(renderProgram);
            GL30C.glBindBufferBase(GL43C.GL_SHADER_STORAGE_BUFFER, 0, ssboBuffer);

            GL20C.glUniformMatrix4fv(GL20C.glGetUniformLocation(renderProgram, "u_ViewMatrix"), false, VIEW_ARRAY);
            GL20C.glUniformMatrix4fv(GL20C.glGetUniformLocation(renderProgram, "u_ProjectionMatrix"), false, PROJECTION_ARRAY);
            GL20C.glUniform3f(GL20C.glGetUniformLocation(renderProgram, "u_CameraPos"), (float) camPos.x, (float) camPos.y, (float) camPos.z);

            GL30C.glBindVertexArray(vaoId);
            GL31C.glDrawArraysInstanced(GL11C.GL_LINES, 0, 2, MAX_PARTICLES);

            GL20C.glUseProgram(0);
            GL30C.glBindBufferBase(GL43C.GL_SHADER_STORAGE_BUFFER, 0, 0);
            GL30C.glBindVertexArray(prevVao);

            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    }

    public static void handleBurstPacket(RadiationParticleBurstPayload payload) {
        if (!supported || payload.count() <= 0) return;

        Player localPlayer = Minecraft.getInstance().player;
        boolean isOwnInventory = (localPlayer != null && payload.emitterId() == localPlayer.getId());

        if (isOwnInventory && clientHasDosimeter(localPlayer)) {
            Random rnd = new Random();
            int clicks = 0;
            if (payload.count() <= 2) {
                if (rnd.nextFloat() < 0.20f) clicks = 1;
            } else if (payload.count() < 60) {
                clicks = 1 + rnd.nextInt(2);
            } else {
                clicks = Math.min(6, 2 + payload.count() / 350);
            }

            for (int c = 0; c < clicks; c++) {
                float pitch = 0.90f + rnd.nextFloat() * 0.22f;
                Minecraft.getInstance().getSoundManager().play(
                        SimpleSoundInstance.forUI(ModSounds.GEIGER_CLICK.get(), pitch, 0.50f)
                );
            }
        }

        int count = Math.min(payload.count(), MAX_PARTICLES);
        ByteBuffer burstBuffer = BufferUtils.createByteBuffer(count * PARTICLE_STRIDE_BYTES);

        float mass = 1.0f, charge = 0.0f;
        float r = 1.0f, g = 1.0f, b = 1.0f;
        float baseSpeed = 16.0f;
        float lifetime = 12.0f;

        switch (payload.particleType()) {
            case 0 -> {
                mass = 4.0f; charge = 2.0f;
                r = 1.0f; g = 0.12f; b = 0.12f;
                baseSpeed = 12.0f; lifetime = 0.005f;
            }
            case 1 -> {
                mass = 0.00055f; charge = -1.0f;
                r = 0.05f; g = 0.88f; b = 1.0f;
                baseSpeed = 35.0f; lifetime = 3.20f;
            }
            case 2 -> {
                mass = 0.00001f; charge = 0.0f;
                r = 0.85f; g = 0.20f; b = 1.0f;
                baseSpeed = 75.0f; lifetime = 12.0f;
            }
            case 3 -> {
                mass = 1.0f; charge = 0.0f;
                r = 1.0f; g = 0.92f; b = 0.35f;
                baseSpeed = 55.0f; lifetime = 12.0f;
            }
        }

        float encodedMass = isOwnInventory ? -mass : mass;
        float baseDoseNanoR = Math.max(0.0001f, payload.doseNanoRPerParticle());
        float nominalEnergy = Math.max(0.01f, payload.energyMeV());

        Random rnd = new Random();

        for (int i = 0; i < count; i++) {
            float ox = payload.x() + (rnd.nextFloat() - 0.5f) * 0.08f;
            float oy = payload.y() + (rnd.nextFloat() - 0.5f) * 0.08f;
            float oz = payload.z() + (rnd.nextFloat() - 0.5f) * 0.08f;

            float vx, vy, vz;
            if (payload.spread() >= 2.0f) {
                float u = rnd.nextFloat() * 2.0f - 1.0f;
                float phi = rnd.nextFloat() * 6.2831853f;
                float rCoord = (float) Math.sqrt(Math.max(0.0f, 1.0f - u * u));
                vx = rCoord * (float) Math.cos(phi);
                vy = u;
                vz = rCoord * (float) Math.sin(phi);
            } else {
                vx = payload.dirX() + (rnd.nextFloat() - 0.5f) * payload.spread();
                vy = payload.dirY() + (rnd.nextFloat() - 0.5f) * payload.spread();
                vz = payload.dirZ() + (rnd.nextFloat() - 0.5f) * payload.spread();
                float len = (float) Math.sqrt(vx * vx + vy * vy + vz * vz) + 0.0001f;
                vx /= len; vy /= len; vz /= len;
            }

            float sampledEnergy = RadiationSpectra.sampleParticleEnergy(rnd, payload.particleType(), nominalEnergy);
            float speedMod = (float) Math.sqrt(Math.max(0.1, sampledEnergy / nominalEnergy));
            float speed = baseSpeed * Math.min(2.0f, Math.max(0.4f, speedMod)) * (0.92f + rnd.nextFloat() * 0.16f);

            vx *= speed;
            vy *= speed;
            vz *= speed;

            float individualDose = baseDoseNanoR * (sampledEnergy / nominalEnergy);

            burstBuffer.putFloat(ox);
            burstBuffer.putFloat(oy);
            burstBuffer.putFloat(oz);
            burstBuffer.putFloat(lifetime);

            burstBuffer.putFloat(vx);
            burstBuffer.putFloat(vy);
            burstBuffer.putFloat(vz);
            burstBuffer.putFloat(lifetime);

            burstBuffer.putFloat(encodedMass);
            burstBuffer.putFloat(charge);
            burstBuffer.putFloat(sampledEnergy);
            burstBuffer.putFloat((float) payload.particleType());

            burstBuffer.putFloat(r);
            burstBuffer.putFloat(g);
            burstBuffer.putFloat(b);
            burstBuffer.putFloat(individualDose);
        }

        burstBuffer.flip();

        int start = writeCursor;
        writeCursor = (writeCursor + count) % MAX_PARTICLES;

        GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, ssboBuffer);
        if (start + count <= MAX_PARTICLES) {
            GL15C.glBufferSubData(GL43C.GL_SHADER_STORAGE_BUFFER, (long) start * PARTICLE_STRIDE_BYTES, burstBuffer);
        } else {
            int firstPart = MAX_PARTICLES - start;
            burstBuffer.limit(firstPart * PARTICLE_STRIDE_BYTES);
            GL15C.glBufferSubData(GL43C.GL_SHADER_STORAGE_BUFFER, (long) start * PARTICLE_STRIDE_BYTES, burstBuffer);

            burstBuffer.position(firstPart * PARTICLE_STRIDE_BYTES);
            burstBuffer.limit(count * PARTICLE_STRIDE_BYTES);
            GL15C.glBufferSubData(GL43C.GL_SHADER_STORAGE_BUFFER, 0, burstBuffer);
        }
        GL15C.glBindBuffer(GL43C.GL_SHADER_STORAGE_BUFFER, 0);
    }

    private static int compileShader(int type, String source) {
        int shader = GL20C.glCreateShader(type);
        GL20C.glShaderSource(shader, source);
        GL20C.glCompileShader(shader);
        if (GL20C.glGetShaderi(shader, GL20C.GL_COMPILE_STATUS) == GL11C.GL_FALSE) {
            String log = GL20C.glGetShaderInfoLog(shader);
            throw new RuntimeException("Shader compilation failed: " + log);
        }
        return shader;
    }

    private static void checkProgramLink(int program) {
        if (GL20C.glGetProgrami(program, GL20C.GL_LINK_STATUS) == GL11C.GL_FALSE) {
            String log = GL20C.glGetProgramInfoLog(program);
            throw new RuntimeException("Program linking failed: " + log);
        }
    }
}