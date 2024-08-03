package net.minecraft.client.gui.screens;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.renderer.GpuWarnlistManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class VideoSettingsScreen extends OptionsSubScreen {

    private static final Component FABULOUS = Component.translatable("options.graphics.fabulous").withStyle(ChatFormatting.ITALIC);

    private static final Component WARNING_MESSAGE = Component.translatable("options.graphics.warning.message", FABULOUS, FABULOUS);

    private static final Component WARNING_TITLE = Component.translatable("options.graphics.warning.title").withStyle(ChatFormatting.RED);

    private static final Component BUTTON_ACCEPT = Component.translatable("options.graphics.warning.accept");

    private static final Component BUTTON_CANCEL = Component.translatable("options.graphics.warning.cancel");

    private OptionsList list;

    private final GpuWarnlistManager gpuWarnlistManager;

    private final int oldMipmaps;

    private static OptionInstance<?>[] options(Options options0) {
        return new OptionInstance[] { options0.graphicsMode(), options0.renderDistance(), options0.prioritizeChunkUpdates(), options0.simulationDistance(), options0.ambientOcclusion(), options0.framerateLimit(), options0.enableVsync(), options0.bobView(), options0.guiScale(), options0.attackIndicator(), options0.gamma(), options0.cloudStatus(), options0.fullscreen(), options0.particles(), options0.mipmapLevels(), options0.entityShadows(), options0.screenEffectScale(), options0.entityDistanceScaling(), options0.fovEffectScale(), options0.showAutosaveIndicator(), options0.glintSpeed(), options0.glintStrength() };
    }

    public VideoSettingsScreen(Screen screen0, Options options1) {
        super(screen0, options1, Component.translatable("options.videoTitle"));
        this.gpuWarnlistManager = screen0.minecraft.getGpuWarnlistManager();
        this.gpuWarnlistManager.resetWarnings();
        if (options1.graphicsMode().get() == GraphicsStatus.FABULOUS) {
            this.gpuWarnlistManager.dismissWarning();
        }
        this.oldMipmaps = options1.mipmapLevels().get();
    }

    @Override
    protected void init() {
        this.list = new OptionsList(this.f_96541_, this.f_96543_, this.f_96544_, 32, this.f_96544_ - 32, 25);
        int $$0 = -1;
        Window $$1 = this.f_96541_.getWindow();
        Monitor $$2 = $$1.findBestMonitor();
        int $$3;
        if ($$2 == null) {
            $$3 = -1;
        } else {
            Optional<VideoMode> $$4 = $$1.getPreferredFullscreenVideoMode();
            $$3 = (Integer) $$4.map($$2::m_84946_).orElse(-1);
        }
        OptionInstance<Integer> $$6 = new OptionInstance<>("options.fullscreen.resolution", OptionInstance.noTooltip(), (p_232806_, p_232807_) -> {
            if ($$2 == null) {
                return Component.translatable("options.fullscreen.unavailable");
            } else {
                return p_232807_ == -1 ? Options.genericValueLabel(p_232806_, Component.translatable("options.fullscreen.current")) : Options.genericValueLabel(p_232806_, Component.literal($$2.getMode(p_232807_).toString()));
            }
        }, new OptionInstance.IntRange(-1, $$2 != null ? $$2.getModeCount() - 1 : -1), $$3, p_232803_ -> {
            if ($$2 != null) {
                $$1.setPreferredFullscreenVideoMode(p_232803_ == -1 ? Optional.empty() : Optional.of($$2.getMode(p_232803_)));
            }
        });
        this.list.addBig($$6);
        this.list.addBig(this.f_96282_.biomeBlendRadius());
        this.list.addSmall(options(this.f_96282_));
        this.m_7787_(this.list);
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280842_ -> {
            this.f_96541_.options.save();
            $$1.changeFullscreenVideoMode();
            this.f_96541_.setScreen(this.f_96281_);
        }).bounds(this.f_96543_ / 2 - 100, this.f_96544_ - 27, 200, 20).build());
    }

    @Override
    public void removed() {
        if (this.f_96282_.mipmapLevels().get() != this.oldMipmaps) {
            this.f_96541_.updateMaxMipLevel(this.f_96282_.mipmapLevels().get());
            this.f_96541_.delayTextureReload();
        }
        super.removed();
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        int $$3 = this.f_96282_.guiScale().get();
        if (super.m_6375_(double0, double1, int2)) {
            if (this.f_96282_.guiScale().get() != $$3) {
                this.f_96541_.resizeDisplay();
            }
            if (this.gpuWarnlistManager.isShowingWarning()) {
                List<Component> $$4 = Lists.newArrayList(new Component[] { WARNING_MESSAGE, CommonComponents.NEW_LINE });
                String $$5 = this.gpuWarnlistManager.getRendererWarnings();
                if ($$5 != null) {
                    $$4.add(CommonComponents.NEW_LINE);
                    $$4.add(Component.translatable("options.graphics.warning.renderer", $$5).withStyle(ChatFormatting.GRAY));
                }
                String $$6 = this.gpuWarnlistManager.getVendorWarnings();
                if ($$6 != null) {
                    $$4.add(CommonComponents.NEW_LINE);
                    $$4.add(Component.translatable("options.graphics.warning.vendor", $$6).withStyle(ChatFormatting.GRAY));
                }
                String $$7 = this.gpuWarnlistManager.getVersionWarnings();
                if ($$7 != null) {
                    $$4.add(CommonComponents.NEW_LINE);
                    $$4.add(Component.translatable("options.graphics.warning.version", $$7).withStyle(ChatFormatting.GRAY));
                }
                this.f_96541_.setScreen(new PopupScreen(WARNING_TITLE, $$4, ImmutableList.of(new PopupScreen.ButtonOption(BUTTON_ACCEPT, p_280839_ -> {
                    this.f_96282_.graphicsMode().set(GraphicsStatus.FABULOUS);
                    Minecraft.getInstance().levelRenderer.allChanged();
                    this.gpuWarnlistManager.dismissWarning();
                    this.f_96541_.setScreen(this);
                }), new PopupScreen.ButtonOption(BUTTON_CANCEL, p_280840_ -> {
                    this.gpuWarnlistManager.dismissWarningAndSkipFabulous();
                    this.f_96541_.setScreen(this);
                }))));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double double0, double double1, double double2) {
        if (Screen.hasControlDown()) {
            OptionInstance<Integer> $$3 = this.f_96282_.guiScale();
            int $$4 = $$3.get() + (int) Math.signum(double2);
            if ($$4 != 0) {
                $$3.set($$4);
                if ($$3.get() == $$4) {
                    this.f_96541_.resizeDisplay();
                    return true;
                }
            }
            return false;
        } else {
            return super.m_6050_(double0, double1, double2);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280419_(guiGraphics0, this.list, int1, int2, float3);
    }
}