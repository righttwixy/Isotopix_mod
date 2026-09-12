package by.righttwixys.isotopix.client.gui;

import by.righttwixys.isotopix.config.IsotopixConfig;
import by.righttwixys.isotopix.gpu.GpuParticleEngine;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class IsotopixConfigScreen extends Screen {
    private final Screen lastScreen;

    private static final int[] PRESETS = {
            8192,     // 512 KB
            16384,    // 1 MB
            32768,    // 2 MB
            65536,    // 4 MB
            131072,   // 8 MB
            262144,   // 16 MB
            524288,   // 32 MB
            1048576,  // 64 MB
            2097152,  // 128 MB
            4194304,  // 256 MB
            8388608,  // 512 MB
            16777216, // 1024 MB
            33554432  // 2048 MB
    };

    private int selectedParticles;
    private ParticleSlider slider;

    public IsotopixConfigScreen(Screen lastScreen) {
        super(Component.literal("Настройки Isotopix"));
        this.lastScreen = lastScreen;
        this.selectedParticles = IsotopixConfig.MAX_PARTICLES.get();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 4 + 20;

        double initialProgress = getProgressForParticles(this.selectedParticles);
        this.slider = new ParticleSlider(centerX - 150, startY, 300, 20, initialProgress);
        this.addRenderableWidget(this.slider);

        this.addRenderableWidget(Button.builder(
                Component.literal("Сбросить по умолчанию (512K / 32 МБ)"),
                btn -> {
                    this.selectedParticles = 524288;
                    this.slider.setValue(getProgressForParticles(524288));
                }
        ).bounds(centerX - 150, startY + 28, 300, 20).build());

        this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_DONE,
                btn -> {
                    IsotopixConfig.MAX_PARTICLES.set(this.selectedParticles);
                    IsotopixConfig.CLIENT_SPEC.save();
                    GpuParticleEngine.resizeParticleBuffer(this.selectedParticles);
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(this.lastScreen);
                    }
                }
        ).bounds(centerX - 155, this.height - 36, 150, 20).build());

        this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_CANCEL,
                btn -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(this.lastScreen);
                    }
                }
        ).bounds(centerX + 5, this.height - 36, 150, 20).build());
    }

    private double getProgressForParticles(int count) {
        for (int i = 0; i < PRESETS.length; i++) {
            if (PRESETS[i] >= count) {
                return (double) i / (PRESETS.length - 1);
            }
        }
        return 1.0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        int startY = this.height / 4 + 85;
        guiGraphics.drawCenteredString(this.font, "§7Каждая частица занимает ровно 64 байта в видеопамяти (VRAM).", this.width / 2, startY, 0xAAAAAA);
        guiGraphics.drawCenteredString(this.font, "§7Изменение применяется мгновенно без перезапуска игры.", this.width / 2, startY + 14, 0xAAAAAA);

        if (this.selectedParticles >= 8388608) {
            guiGraphics.drawCenteredString(this.font, "§cВнимание: Режимы 512 МБ – 2 ГБ требуют мощной дискретной видеокарты (4+ ГБ VRAM)!", this.width / 2, startY + 34, 0xFFAA00);
        }
    }

    private class ParticleSlider extends AbstractSliderButton {
        public ParticleSlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, Component.empty(), value);
            this.updateMessage();
        }

        public void setValue(double val) {
            this.value = Math.max(0.0, Math.min(1.0, val));
            this.applyValue();
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            int idx = (int) Math.round(this.value * (PRESETS.length - 1));
            idx = Math.max(0, Math.min(PRESETS.length - 1, idx));
            int particles = PRESETS[idx];
            long bytes = (long) particles * 64L;
            long vramMB = bytes / (1024L * 1024L);

            String formattedCount = String.format("%,d", particles).replace(',', ' ');
            String vramStr;
            if (particles < 16384) {
                vramStr = "512 КБ VRAM";
            } else if (vramMB >= 1024) {
                vramStr = (vramMB / 1024) + " ГБ VRAM";
            } else {
                vramStr = vramMB + " МБ VRAM";
            }

            String suffix = "";
            if (particles == 8192) {
                suffix = " §b[Минимум]§r";
            } else if (particles == 524288) {
                suffix = " §a[По умолчанию]§r";
            } else if (particles == 33554432) {
                suffix = " §c[Максимум 2 ГБ]§r";
            }

            this.setMessage(Component.literal("Лучи: " + formattedCount + " (" + vramStr + ")" + suffix));
        }

        @Override
        protected void applyValue() {
            int idx = (int) Math.round(this.value * (PRESETS.length - 1));
            idx = Math.max(0, Math.min(PRESETS.length - 1, idx));
            selectedParticles = PRESETS[idx];
        }
    }
}