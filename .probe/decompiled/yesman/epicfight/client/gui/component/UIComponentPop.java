package yesman.epicfight.client.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.config.ClientConfig;
import yesman.epicfight.config.OptionHandler;

@OnlyIn(Dist.CLIENT)
public class UIComponentPop<T extends UIComponent> extends Screen implements ContainerEventHandler {

    protected final T parentWidget;

    protected int width;

    protected int height;

    public int x;

    public int y;

    private boolean enable;

    public UIComponentPop(int width, int height, T parentWidget) {
        super(Component.literal(""));
        this.width = width;
        this.height = height;
        this.parentWidget = parentWidget;
        this.init();
    }

    @Override
    public void init() {
        this.m_169413_();
        this.m_142416_(createButton(this.x + 10, this.y - 2, 11, 8, button -> {
            this.parentWidget.verticalBasis.setValue(ClientConfig.VerticalBasis.TOP);
            this.parentWidget.yCoord.setValue((Integer) ClientConfig.VerticalBasis.TOP.saveCoordGetter.apply(this.parentWidget.parentScreen.f_96544_, this.y));
        }));
        this.m_142416_(createButton(this.x - 2, this.y + 11, 11, 7, button -> {
            this.parentWidget.horizontalBasis.setValue(ClientConfig.HorizontalBasis.LEFT);
            this.parentWidget.xCoord.setValue((Integer) ClientConfig.HorizontalBasis.LEFT.saveCoordGetter.apply(this.parentWidget.parentScreen.f_96543_, this.x));
        }));
        this.m_142416_(createButton(this.x + 22, this.y + 11, 11, 7, button -> {
            this.parentWidget.horizontalBasis.setValue(ClientConfig.HorizontalBasis.RIGHT);
            this.parentWidget.xCoord.setValue((Integer) ClientConfig.HorizontalBasis.RIGHT.saveCoordGetter.apply(this.parentWidget.parentScreen.f_96543_, this.x));
        }));
        this.m_142416_(createButton(this.x + 10, this.y + 24, 11, 8, button -> {
            this.parentWidget.verticalBasis.setValue(ClientConfig.VerticalBasis.BOTTOM);
            this.parentWidget.yCoord.setValue((Integer) ClientConfig.VerticalBasis.BOTTOM.saveCoordGetter.apply(this.parentWidget.parentScreen.f_96544_, this.y));
        }));
        this.m_142416_(createButton(this.x + 10, this.y + 11, 11, 7, button -> {
            this.parentWidget.verticalBasis.setValue(ClientConfig.VerticalBasis.CENTER);
            this.parentWidget.horizontalBasis.setValue(ClientConfig.HorizontalBasis.CENTER);
            this.parentWidget.xCoord.setValue((Integer) ClientConfig.HorizontalBasis.CENTER.saveCoordGetter.apply(this.parentWidget.parentScreen.f_96543_, this.x));
            this.parentWidget.yCoord.setValue((Integer) ClientConfig.VerticalBasis.CENTER.saveCoordGetter.apply(this.parentWidget.parentScreen.f_96544_, this.y));
        }));
    }

    public static Button createButton(int x, int y, int width, int height, Button.OnPress onpress) {
        return Button.builder(Component.literal(""), onpress).bounds(x, y, width, height).build();
    }

    public void openPop() {
        this.enable = true;
        this.init();
    }

    public void closePop() {
        this.enable = false;
    }

    protected boolean isHoverd(double x, double y) {
        return this.enable && x >= (double) this.x && y >= (double) this.y && x < (double) (this.x + this.width) && y < (double) (this.y + this.height);
    }

    public boolean isOpen() {
        return this.enable;
    }

    @Override
    public boolean mouseClicked(double x, double y, int pressType) {
        if (!this.enable) {
            return false;
        } else {
            boolean clicked = false;
            for (GuiEventListener listener : this.m_6702_()) {
                clicked |= listener.mouseClicked(x, y, pressType);
            }
            return clicked;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.enable) {
            boolean popupOut = mouseX < this.x - 3 || mouseY < this.y - 3 || mouseX >= this.x + this.width + 3 || mouseY >= this.y + this.height + 3;
            boolean parentOut = mouseX < this.parentWidget.m_252754_() - 3 || mouseY < this.parentWidget.m_252907_() - 3 || mouseX >= this.parentWidget.m_252754_() + this.parentWidget.m_5711_() + 3 || mouseY >= this.parentWidget.m_252907_() + this.parentWidget.m_93694_() + 3;
            if (popupOut && parentOut) {
                this.enable = false;
            }
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 200.0F);
            this.renderPopup(guiGraphics, this.x, this.y, this.width, this.height);
            super.render(guiGraphics, mouseX, mouseY, partialTicks);
            poseStack.popPose();
        }
    }

    protected void renderPopup(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        RenderSystem.setShader(GameRenderer::m_172811_);
        int backgroundStart = -267386864;
        int backgroundEnd = -267386864;
        int boarderStart = 1347420415;
        int boarderEnd = 1344798847;
        guiGraphics.fillGradient(x - 3, y - 4, x + width + 3, y - 3, 400, backgroundStart, backgroundStart);
        guiGraphics.fillGradient(x - 3, y + height + 3, x + width + 3, y + height + 4, 400, backgroundEnd, backgroundEnd);
        guiGraphics.fillGradient(x - 3, y - 3, x + width + 3, y + height + 3, 400, backgroundStart, backgroundEnd);
        guiGraphics.fillGradient(x - 4, y - 3, x - 3, y + height + 3, 400, backgroundStart, backgroundEnd);
        guiGraphics.fillGradient(x + width + 3, y - 3, x + width + 4, y + height + 3, 400, backgroundStart, backgroundEnd);
        guiGraphics.fillGradient(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, 400, boarderStart, boarderEnd);
        guiGraphics.fillGradient(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, 400, boarderStart, boarderEnd);
        guiGraphics.fillGradient(x - 3, y - 3, x + width + 3, y - 3 + 1, 400, boarderStart, boarderStart);
        guiGraphics.fillGradient(x - 3, y + height + 2, x + width + 3, y + height + 3, 400, boarderEnd, boarderEnd);
        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public static class PassivesUIComponentPop extends UIComponentPop<UIComponent.PassiveUIComponent> {

        public PassivesUIComponentPop(int width, int height, UIComponent.PassiveUIComponent parentWidget) {
            super(width, height, parentWidget);
        }

        @Override
        protected void renderPopup(GuiGraphics guiGraphics, int x, int y, int width, int height) {
            super.renderPopup(guiGraphics, x, y + 14, width, height - 14);
        }

        @Override
        public void init() {
            super.init();
            for (GuiEventListener gui : this.m_6702_()) {
                if (gui instanceof AbstractWidget widget) {
                    widget.setY(widget.getY() + 14);
                }
            }
            this.m_142416_(new UIComponentPop.PassivesUIComponentPop.AlignButton(this.x - 3, this.y, 12, 10, this.parentWidget.horizontalBasis, this.parentWidget.verticalBasis, this.parentWidget.alignDirection, button -> {
                ClientConfig.AlignDirection newAlignDirection = ClientConfig.AlignDirection.values()[(this.parentWidget.alignDirection.getValue().ordinal() + 1) % ClientConfig.AlignDirection.values().length];
                this.parentWidget.alignDirection.setValue(newAlignDirection);
            }));
        }

        public static class AlignButton extends Button {

            private static final ResourceLocation BATTLE_ICONS = new ResourceLocation("epicfight", "textures/gui/battle_icons.png");

            private final OptionHandler<ClientConfig.HorizontalBasis> horBasis;

            private final OptionHandler<ClientConfig.VerticalBasis> verBasis;

            private final OptionHandler<ClientConfig.AlignDirection> alignDirection;

            public AlignButton(int x, int y, int width, int height, OptionHandler<ClientConfig.HorizontalBasis> horBasis, OptionHandler<ClientConfig.VerticalBasis> verBasis, OptionHandler<ClientConfig.AlignDirection> alignDirection, Button.OnPress onpress) {
                super(x, y, width, height, Component.literal(""), onpress, Button.DEFAULT_NARRATION);
                this.horBasis = horBasis;
                this.verBasis = verBasis;
                this.alignDirection = alignDirection;
            }

            @Override
            protected void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
                RenderSystem.setShader(GameRenderer::m_172817_);
                RenderSystem.setShaderTexture(0, BATTLE_ICONS);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableDepthTest();
                Vec2[] texCoords = new Vec2[4];
                float startX;
                float startY;
                float width;
                float height;
                if (this.f_93622_) {
                    startX = 0.5176471F;
                    startY = 0.0F;
                    width = 0.14117648F;
                    height = 0.14117648F;
                } else {
                    startX = 0.38039216F;
                    startY = 0.007843138F;
                    width = 0.12156863F;
                    height = 0.12156863F;
                }
                Vec2 uv0 = new Vec2(startX, startY);
                Vec2 uv1 = new Vec2(startX + width, startY);
                Vec2 uv2 = new Vec2(startX + width, startY + height);
                Vec2 uv3 = new Vec2(startX, startY + height);
                texCoords[0] = uv0;
                texCoords[1] = uv1;
                texCoords[2] = uv2;
                texCoords[3] = uv3;
                if (this.alignDirection.getValue() == ClientConfig.AlignDirection.HORIZONTAL) {
                    if (this.horBasis.getValue() == ClientConfig.HorizontalBasis.LEFT) {
                        texCoords[0] = uv1;
                        texCoords[1] = uv2;
                        texCoords[2] = uv3;
                        texCoords[3] = uv0;
                    } else {
                        texCoords[0] = uv3;
                        texCoords[1] = uv0;
                        texCoords[2] = uv1;
                        texCoords[3] = uv2;
                    }
                } else if (this.verBasis.getValue() == ClientConfig.VerticalBasis.BOTTOM) {
                    texCoords[0] = uv2;
                    texCoords[1] = uv3;
                    texCoords[2] = uv0;
                    texCoords[3] = uv1;
                }
                this.blitRotate(guiGraphics, texCoords);
            }

            public void blitRotate(GuiGraphics guiGraphics, Vec2[] texCoords) {
                PoseStack poseStack = guiGraphics.pose();
                RenderSystem.setShader(GameRenderer::m_172817_);
                BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                bufferbuilder.m_252986_(poseStack.last().pose(), (float) this.m_252754_(), (float) this.m_252907_(), (float) this.getBlitOffset()).uv(texCoords[0].x, texCoords[0].y).endVertex();
                bufferbuilder.m_252986_(poseStack.last().pose(), (float) (this.m_252754_() + this.f_93618_), (float) this.m_252907_(), (float) this.getBlitOffset()).uv(texCoords[1].x, texCoords[1].y).endVertex();
                bufferbuilder.m_252986_(poseStack.last().pose(), (float) (this.m_252754_() + this.f_93618_), (float) (this.m_252907_() + this.f_93619_), (float) this.getBlitOffset()).uv(texCoords[2].x, texCoords[2].y).endVertex();
                bufferbuilder.m_252986_(poseStack.last().pose(), (float) this.m_252754_(), (float) (this.m_252907_() + this.f_93619_), (float) this.getBlitOffset()).uv(texCoords[3].x, texCoords[3].y).endVertex();
                BufferUploader.drawWithShader(bufferbuilder.end());
            }

            public int getBlitOffset() {
                return 0;
            }
        }
    }
}