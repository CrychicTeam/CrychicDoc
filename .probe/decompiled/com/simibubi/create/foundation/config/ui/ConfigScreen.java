package com.simibubi.create.foundation.config.ui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.element.StencilElement;
import com.simibubi.create.foundation.utility.animation.Force;
import com.simibubi.create.foundation.utility.animation.PhysicalFloat;
import com.simibubi.create.infrastructure.gui.CreateMainMenuScreen;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.TriConsumer;

public abstract class ConfigScreen extends AbstractSimiScreen {

    public static final Map<String, TriConsumer<Screen, GuiGraphics, Float>> backgrounds = new HashMap();

    public static final PhysicalFloat cogSpin = PhysicalFloat.create().withLimit(10.0F).withDrag(0.3).addForce(new Force.Static(0.2F));

    public static final BlockState cogwheelState = (BlockState) AllBlocks.LARGE_COGWHEEL.getDefaultState().m_61124_(CogWheelBlock.AXIS, Direction.Axis.Y);

    public static String modID = null;

    protected final Screen parent;

    public ConfigScreen(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void tick() {
        super.tick();
        cogSpin.tick();
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(this, graphics));
    }

    @Override
    protected void renderWindowBackground(GuiGraphics graphics, int mouseX, int mouseY, final float partialTicks) {
        if (this.f_96541_ != null && this.f_96541_.level != null) {
            graphics.fill(0, 0, this.f_96543_, this.f_96544_, -1339544524);
        } else {
            this.renderMenuBackground(graphics, partialTicks);
        }
        (new StencilElement() {

            @Override
            protected void renderStencil(GuiGraphics graphics) {
                ConfigScreen.this.renderCog(graphics, partialTicks);
            }

            @Override
            protected void renderElement(GuiGraphics graphics) {
                graphics.fill(-200, -200, 200, 200, 1610612736);
            }
        }).<RenderElement>at((float) this.f_96543_ * 0.5F, (float) this.f_96544_ * 0.5F, 0.0F).render(graphics);
        super.renderWindowBackground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void prepareFrame() {
        UIRenderHelper.swapAndBlitColor(this.f_96541_.getMainRenderTarget(), UIRenderHelper.framebuffer);
        RenderSystem.clear(1280, Minecraft.ON_OSX);
    }

    @Override
    protected void endFrame() {
        UIRenderHelper.swapAndBlitColor(UIRenderHelper.framebuffer, this.f_96541_.getMainRenderTarget());
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        cogSpin.bump(3, -delta * 5.0);
        return super.m_6050_(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    public static String toHumanReadable(String key) {
        String s = key.replaceAll("_", " ");
        s = (String) Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(s)).map(StringUtils::capitalize).collect(Collectors.joining(" "));
        return StringUtils.normalizeSpace(s);
    }

    protected void renderMenuBackground(GuiGraphics graphics, float partialTicks) {
        TriConsumer<Screen, GuiGraphics, Float> customBackground = (TriConsumer<Screen, GuiGraphics, Float>) backgrounds.get(modID);
        if (customBackground != null) {
            customBackground.accept(this, graphics, partialTicks);
        } else {
            float elapsedPartials = this.f_96541_.getDeltaFrameTime();
            CreateMainMenuScreen.PANORAMA.render(elapsedPartials, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            graphics.blit(CreateMainMenuScreen.PANORAMA_OVERLAY_TEXTURES, 0, 0, this.f_96543_, this.f_96544_, 0.0F, 0.0F, 16, 128, 16, 128);
            graphics.fill(0, 0, this.f_96543_, this.f_96544_, -1876415436);
        }
    }

    protected void renderCog(GuiGraphics graphics, float partialTicks) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(-100.0F, 100.0F, -100.0F);
        ms.scale(200.0F, 200.0F, 1.0F);
        GuiGameElement.of(cogwheelState).rotateBlock(22.5, (double) cogSpin.getValue(partialTicks), 22.5).render(graphics);
        ms.popPose();
    }
}