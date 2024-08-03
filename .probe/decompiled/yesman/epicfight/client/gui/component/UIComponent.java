package yesman.epicfight.client.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.utils.math.Vec2i;
import yesman.epicfight.client.gui.screen.UISetupScreen;
import yesman.epicfight.config.ClientConfig;
import yesman.epicfight.config.OptionHandler;

@OnlyIn(Dist.CLIENT)
public class UIComponent extends Button {

    protected final UISetupScreen parentScreen;

    protected final ResourceLocation texture;

    protected int texU;

    protected int texV;

    protected int texW;

    protected int texH;

    protected int resolutionDivW;

    protected int resolutionDivH;

    protected int draggingTime;

    protected float r;

    protected float g;

    protected float b;

    private double pressX;

    private double pressY;

    public final OptionHandler<Integer> xCoord;

    public final OptionHandler<Integer> yCoord;

    public final OptionHandler<ClientConfig.HorizontalBasis> horizontalBasis;

    public final OptionHandler<ClientConfig.VerticalBasis> verticalBasis;

    public UIComponentPop<?> popupScreen;

    public UIComponent(int x, int y, OptionHandler<Integer> xCoord, OptionHandler<Integer> yCoord, OptionHandler<ClientConfig.HorizontalBasis> horizontalBasis, OptionHandler<ClientConfig.VerticalBasis> verticalBasis, int width, int height, int texU, int texV, int texW, int texH, int resolutionDivW, int resolutionDivH, int r, int g, int b, UISetupScreen parentScreen, ResourceLocation texture) {
        super(x, y, width, height, Component.literal(""), button -> {
        }, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.texU = texU;
        this.texV = texV;
        this.texW = texW;
        this.texH = texH;
        this.resolutionDivW = resolutionDivW;
        this.resolutionDivH = resolutionDivH;
        this.r = (float) r / 255.0F;
        this.g = (float) g / 255.0F;
        this.b = (float) b / 255.0F;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.horizontalBasis = horizontalBasis;
        this.verticalBasis = verticalBasis;
        this.parentScreen = parentScreen;
        this.popupScreen = new UIComponentPop<>(30, 30, this);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.f_93623_ && this.f_93624_) {
            if (this.m_7972_(button)) {
                this.draggingTime = 0;
                if (this.m_93680_(mouseX, mouseY)) {
                    this.parentScreen.beginToDrag(this);
                    this.pressX = mouseX - (double) this.m_252754_();
                    this.pressY = mouseY - (double) this.m_252907_();
                    this.m_7435_(Minecraft.getInstance().getSoundManager());
                    if (!this.popupScreen.isHoverd((double) this.m_252754_(), (double) this.m_252907_())) {
                        this.popupScreen.closePop();
                    }
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    protected void onDrag(double x, double y, double dx, double dy) {
        if (this.parentScreen.isDraggingComponent(this)) {
            this.m_252865_((int) (x - this.pressX));
            this.m_253211_((int) (y - this.pressY));
            this.draggingTime++;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.m_7972_(button)) {
            this.onRelease(mouseX, mouseY);
            this.parentScreen.endDragging();
            int xCoord = (Integer) this.horizontalBasis.getValue().saveCoordGetter.apply(this.parentScreen.f_96543_, this.m_252754_());
            int yCoord = (Integer) this.verticalBasis.getValue().saveCoordGetter.apply(this.parentScreen.f_96544_, this.m_252907_());
            this.xCoord.setValue(xCoord);
            this.yCoord.setValue(yCoord);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRelease(double x, double y) {
        if (!this.popupScreen.isOpen() && this.draggingTime < 2) {
            if (x + (double) this.popupScreen.width > (double) this.parentScreen.f_96543_) {
                this.popupScreen.x = (int) x - this.popupScreen.width;
            } else {
                this.popupScreen.x = (int) x;
            }
            if (y + (double) this.popupScreen.height > (double) this.parentScreen.f_96544_) {
                this.popupScreen.y = (int) y - this.popupScreen.height;
            } else {
                this.popupScreen.y = (int) y;
            }
            this.popupScreen.openPop();
        }
    }

    public void drawOutline(GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();
        float screenX = (float) (this.m_252754_() - 1);
        float screenXEnd = (float) (this.m_252754_() + this.f_93618_ + 1);
        float screenY = (float) (this.m_252907_() - 1);
        float screenYEnd = (float) (this.m_252907_() + this.f_93619_ + 1);
        RenderSystem.disableCull();
        RenderSystem.lineWidth(2.0F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::m_172757_);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
        bufferbuilder.m_252986_(poseStack.last().pose(), screenX, screenY, 0.0F).color(69, 166, 244, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.m_252986_(poseStack.last().pose(), screenXEnd, screenY, 0.0F).color(69, 166, 244, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.m_252986_(poseStack.last().pose(), screenXEnd, screenY, 0.0F).color(69, 166, 244, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        bufferbuilder.m_252986_(poseStack.last().pose(), screenXEnd, screenYEnd, 0.0F).color(69, 166, 244, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        bufferbuilder.m_252986_(poseStack.last().pose(), screenXEnd, screenYEnd, 0.0F).color(69, 166, 244, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.m_252986_(poseStack.last().pose(), screenX, screenYEnd, 0.0F).color(69, 166, 244, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.m_252986_(poseStack.last().pose(), screenX, screenYEnd, 0.0F).color(69, 166, 244, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.m_252986_(poseStack.last().pose(), screenX, screenY, 0.0F).color(69, 166, 244, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        if (this.horizontalBasis.getValue() == ClientConfig.HorizontalBasis.CENTER) {
            bufferbuilder.m_252986_(poseStack.last().pose(), screenX + (screenXEnd - screenX) / 2.0F, screenY + (screenYEnd - screenY) / 2.0F, 0.0F).color(69, 166, 244, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.m_252986_(poseStack.last().pose(), (float) (this.parentScreen.f_96543_ / 2), screenY + (screenYEnd - screenY) / 2.0F, 0.0F).color(69, 166, 244, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        } else if (this.horizontalBasis.getValue() == ClientConfig.HorizontalBasis.LEFT) {
            bufferbuilder.m_252986_(poseStack.last().pose(), screenX, screenY, 0.0F).color(69, 166, 244, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.m_252986_(poseStack.last().pose(), 0.0F, screenY, 0.0F).color(69, 166, 244, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        } else if (this.horizontalBasis.getValue() == ClientConfig.HorizontalBasis.RIGHT) {
            bufferbuilder.m_252986_(poseStack.last().pose(), screenX, screenY, 0.0F).color(69, 166, 244, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.m_252986_(poseStack.last().pose(), (float) this.parentScreen.f_96543_, screenY, 0.0F).color(69, 166, 244, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        }
        if (this.verticalBasis.getValue() == ClientConfig.VerticalBasis.CENTER) {
            bufferbuilder.m_252986_(poseStack.last().pose(), screenX + (screenXEnd - screenX) / 2.0F, screenY + (screenYEnd - screenY) / 2.0F, 0.0F).color(69, 166, 244, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.m_252986_(poseStack.last().pose(), screenX + (screenXEnd - screenX) / 2.0F, (float) (this.parentScreen.f_96544_ / 2), 0.0F).color(69, 166, 244, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        } else if (this.verticalBasis.getValue() == ClientConfig.VerticalBasis.TOP) {
            bufferbuilder.m_252986_(poseStack.last().pose(), screenX, screenY, 0.0F).color(69, 166, 244, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.m_252986_(poseStack.last().pose(), screenX, 0.0F, 0.0F).color(69, 166, 244, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        } else if (this.verticalBasis.getValue() == ClientConfig.VerticalBasis.BOTTOM) {
            bufferbuilder.m_252986_(poseStack.last().pose(), screenX, screenY, 0.0F).color(69, 166, 244, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.m_252986_(poseStack.last().pose(), screenX, (float) this.parentScreen.f_96544_, 0.0F).color(69, 166, 244, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        }
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(this.r, this.g, this.b, this.f_93625_);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        guiGraphics.blit(this.texture, this.m_252754_(), this.m_252907_(), this.f_93618_, this.f_93619_, (float) this.texU, (float) this.texV, this.texW, this.texH, this.resolutionDivW, this.resolutionDivH);
        if (this.m_198029_() || this.popupScreen.isOpen()) {
            this.drawOutline(guiGraphics);
        }
        if (this.popupScreen.isOpen()) {
            this.popupScreen.render(guiGraphics, x, y, partialTicks);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class PassiveUIComponent extends UIComponent {

        public final OptionHandler<ClientConfig.AlignDirection> alignDirection;

        protected final ResourceLocation texture2;

        public PassiveUIComponent(int x, int y, OptionHandler<Integer> xCoord, OptionHandler<Integer> yCoord, OptionHandler<ClientConfig.HorizontalBasis> horizontalBasis, OptionHandler<ClientConfig.VerticalBasis> verticalBasis, OptionHandler<ClientConfig.AlignDirection> alignDirection, int width, int height, int texU, int texV, int texW, int texH, int resolutionDivW, int resolutionDivH, int r, int g, int b, UISetupScreen parentScreen, ResourceLocation texture, ResourceLocation texture2) {
            super(x, y, xCoord, yCoord, horizontalBasis, verticalBasis, width, height, texU, texV, texW, texH, resolutionDivW, resolutionDivH, r, g, b, parentScreen, texture);
            this.popupScreen = new UIComponentPop.PassivesUIComponentPop(30, 44, this);
            this.alignDirection = alignDirection;
            this.texture2 = texture2;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
            Vec2i startPos = this.alignDirection.getValue().startCoordGetter.get(this.m_252754_(), this.m_252907_(), this.f_93618_, this.f_93619_, 2, this.horizontalBasis.getValue(), this.verticalBasis.getValue());
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(this.r, this.g, this.b, this.f_93625_);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            guiGraphics.blit(this.texture, startPos.x, startPos.y, this.f_93618_, this.f_93619_, (float) this.texU, (float) this.texV, this.texW, this.texH, this.resolutionDivW, this.resolutionDivH);
            if (this.m_198029_() || this.popupScreen.isOpen()) {
                this.drawOutline(guiGraphics);
            }
            if (this.popupScreen.isOpen()) {
                this.popupScreen.render(guiGraphics, x, y, partialTicks);
            }
            Vec2i nextPos = this.alignDirection.getValue().nextPositionGetter.getNext(this.horizontalBasis.getValue(), this.verticalBasis.getValue(), startPos, this.f_93618_, this.f_93619_);
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(this.r, this.g, this.b, this.f_93625_);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            guiGraphics.blit(this.texture2, nextPos.x, nextPos.y, this.f_93618_, this.f_93619_, (float) this.texU, (float) this.texV, this.texW, this.texH, this.resolutionDivW, this.resolutionDivH);
        }
    }
}