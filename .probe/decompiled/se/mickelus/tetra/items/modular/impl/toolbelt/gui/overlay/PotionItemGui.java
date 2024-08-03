package se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class PotionItemGui extends GuiElement {

    private final ItemStack itemStack;

    private final int slot;

    private final Minecraft mc;

    private final KeyframeAnimation showAnimation;

    GuiTexture backdrop;

    public PotionItemGui(int x, int y, ItemStack itemStack, int slot, boolean animateUp) {
        super(x, y, 23, 23);
        this.setAttachmentPoint(GuiAttachment.middleLeft);
        this.setAttachmentAnchor(GuiAttachment.middleLeft);
        this.itemStack = itemStack;
        this.slot = slot;
        this.mc = Minecraft.getInstance();
        this.backdrop = new GuiTexture(0, 0, 23, 23, 32, 28, GuiTextures.toolbelt);
        this.addChild(this.backdrop);
        this.isVisible = false;
        this.showAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.TranslateY(animateUp ? (float) (y + 2) : (float) (y - 2), (float) y), new Applier.Opacity(0.0F, 1.0F)).withDelay((int) (Math.random() * 300.0));
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
        return true;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        if (this.opacity == 1.0F) {
            this.drawItemStack(graphics, this.itemStack, this.x + refX + 3, this.y + refY + 2);
        }
    }

    private void drawItemStack(GuiGraphics graphics, ItemStack itemStack, int x, int y) {
        PoseStack renderSystemStack = RenderSystem.getModelViewStack();
        renderSystemStack.pushPose();
        GlStateManager._enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        graphics.renderItem(itemStack, x, y);
        graphics.renderItemDecorations(this.mc.font, itemStack, x, y, "");
        GlStateManager._disableDepthTest();
        renderSystemStack.popPose();
    }

    public int getSlot() {
        return this.slot;
    }

    @Override
    protected void onFocus() {
        this.backdrop.setColor(16777164);
    }

    @Override
    protected void onBlur() {
        this.backdrop.setColor(16777215);
    }

    @Override
    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        mouseX -= refX + this.x;
        mouseY -= refY + this.y;
        boolean gainFocus = mouseX + mouseY >= 12;
        if (mouseX + mouseY > 34) {
            gainFocus = false;
        }
        if (mouseX - mouseY > 8) {
            gainFocus = false;
        }
        if (mouseY - mouseX > 12) {
            gainFocus = false;
        }
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