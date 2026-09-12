package by.righttwixys.isotopix.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class IsotopixConfig {
    public static final ModConfigSpec CLIENT_SPEC;
    public static final ModConfigSpec.IntValue MAX_PARTICLES;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("gpu_particles");

        MAX_PARTICLES = builder
                .comment("Максимальное количество одновременно симулируемых лучей радиации на GPU",
                        "8192 = 8K лучей (512 КБ VRAM, ультра-экономный)",
                        "524288 = 512K лучей (32 МБ VRAM, по умолчанию)",
                        "8388608 = 8M лучей (512 МБ VRAM)",
                        "33554432 = 32M лучей (2048 МБ / 2 ГБ VRAM, максимум)")
                .defineInRange("maxParticles", 524288, 8192, 33554432);

        builder.pop();
        CLIENT_SPEC = builder.build();
    }
}