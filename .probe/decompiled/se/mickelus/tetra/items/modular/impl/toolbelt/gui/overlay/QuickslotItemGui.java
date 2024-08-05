package se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiItem;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

@ParametersAreNonnullByDefault
public class QuickslotItemGui extends GuiElement {

    public static final int height = 20;

    private final ItemStack itemStack;

    private final int slot;

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation showLabel;

    private final KeyframeAnimation hideLabel;

    private final GuiItem guiItem;

    private final GuiString label;

    private final QuickslotDirectionGui hitLeft;

    private final QuickslotDirectionGui hitRight;

    public QuickslotItemGui(int x, int y, ItemStack itemStack, int slot) {
        super(x, y, 200, 20);
        this.itemStack = itemStack;
        this.slot = slot;
        this.guiItem = new GuiItem(38, 1);
        this.guiItem.setOpacity(0.0F);
        this.guiItem.setOpacityThreshold(0.2F);
        this.guiItem.setItem(itemStack);
        this.addChild(this.guiItem);
        this.label = new GuiStringOutline(61, 6, itemStack.getHoverName().getString());
        this.label.setColor(16777164);
        this.label.setOpacity(0.0F);
        this.addChild(this.label);
        this.hitLeft = new QuickslotDirectionGui(0, 0, 46, 20, false);
        this.addChild(this.hitLeft);
        this.hitRight = new QuickslotDirectionGui(46, 0, 151, 20, true);
        this.addChild(this.hitRight);
        this.isVisible = false;
        this.showAnimation = new KeyframeAnimation(80, this.guiItem).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateY(0.0F, 1.0F)).withDelay(slot * 80);
        this.showLabel = new KeyframeAnimation(100, this.label).applyTo(new Applier.TranslateX((float) (this.label.getX() + 1)), new Applier.Opacity(1.0F));
        this.hideLabel = new KeyframeAnimation(200, this.label).applyTo(new Applier.TranslateX((float) this.label.getX()), new Applier.Opacity(0.0F));
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
        this.opacity = 0.0F;
        return true;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }

    @Override
    protected void onFocus() {
        super.onFocus();
        this.hideLabel.stop();
        this.showLabel.start();
        this.hitLeft.animateIn();
        this.hitRight.animateIn();
    }

    @Override
    protected void onBlur() {
        super.onBlur();
        this.showLabel.stop();
        this.hideLabel.start();
        this.hitLeft.animateOut();
        this.hitRight.animateOut();
    }

    public int getSlot() {
        return this.slot;
    }

    public InteractionHand getHand() {
        return Minecraft.getInstance().player.m_5737_() == HumanoidArm.RIGHT == this.hitRight.hasFocus() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
}