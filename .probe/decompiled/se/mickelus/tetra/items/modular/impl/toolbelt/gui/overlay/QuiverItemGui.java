package se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class QuiverItemGui extends GuiElement {

    private final ItemStack itemStack;

    private final Minecraft mc;

    private final KeyframeAnimation showAnimation;

    private final GuiTexture backdrop;

    private Font fontRenderer;

    private GuiString count;

    private GuiString hoverLabel;

    public QuiverItemGui(int x, int y, ItemStack itemStack, int slot) {
        super(x, y, 23, 23);
        this.setAttachmentPoint(GuiAttachment.bottomRight);
        this.setAttachmentAnchor(GuiAttachment.bottomRight);
        this.itemStack = itemStack;
        this.mc = Minecraft.getInstance();
        if (itemStack != null) {
            this.fontRenderer = null;
        }
        if (this.fontRenderer == null) {
            this.fontRenderer = this.mc.font;
        }
        this.backdrop = new GuiTexture(0, 0, 23, 23, 32, 28, GuiTextures.toolbelt);
        this.addChild(this.backdrop);
        if (itemStack != null) {
            this.count = new GuiStringOutline(-3, 1, itemStack.getCount() + "");
            this.count.setAttachmentPoint(GuiAttachment.middleLeft);
            this.count.setAttachmentAnchor(GuiAttachment.middleRight);
            this.addChild(this.count);
            this.count.setVisible(false);
            this.hoverLabel = new GuiString(-5, 0, itemStack.getHoverName().getString());
            this.hoverLabel.setAttachmentPoint(GuiAttachment.middleRight);
            this.hoverLabel.setAttachmentAnchor(GuiAttachment.middleLeft);
            this.addChild(this.hoverLabel);
            this.hoverLabel.setVisible(false);
        }
        this.isVisible = false;
        this.showAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.TranslateX((float) (x - 2), (float) x), new Applier.TranslateY((float) (y + 2), (float) y), new Applier.Opacity(0.0F, 1.0F)).withDelay(slot * 80).onStop(finished -> this.count.setVisible(true));
    }

    @Override
    protected void onShow() {
        this.showAnimation.start();
    }

    @Override
    protected boolean onHide() {
        if (this.showAnimation.isActive()) {
            this.showAnimation.stop();
        }
        this.count.setVisible(false);
        this.hoverLabel.setVisible(false);
        return true;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        if (this.opacity == 1.0F) {
            this.drawItemStack(graphics, this.itemStack, this.x + refX + 3, this.y + refY + 3);
        }
    }

    private void drawItemStack(GuiGraphics graphics, ItemStack itemStack, int x, int y) {
        PoseStack renderSystemStack = RenderSystem.getModelViewStack();
        renderSystemStack.pushPose();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        graphics.renderItem(itemStack, x, y);
        graphics.renderItemDecorations(this.fontRenderer, itemStack, x, y, "");
        RenderSystem.disableDepthTest();
        renderSystemStack.popPose();
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    protected void onFocus() {
        this.backdrop.setColor(16777164);
        this.hoverLabel.setVisible(true);
    }

    @Override
    protected void onBlur() {
        this.backdrop.setColor(16777215);
        this.hoverLabel.setVisible(false);
    }

    @Override
    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        mouseX -= refX + this.x;
        mouseY -= refY + this.y;
        boolean gainFocus = mouseX + mouseY >= 12 && mouseX + mouseY <= 34 && mouseX - mouseY < 10 && mouseY - mouseX < 11;
        if (gainFocus != this.hasFocus) {
            this.hasFocus = gainFocus;
            if (this.hasFocus) {
                this.onFocus();
            } else {
                this.onBlur();
            }
        }
    }
}