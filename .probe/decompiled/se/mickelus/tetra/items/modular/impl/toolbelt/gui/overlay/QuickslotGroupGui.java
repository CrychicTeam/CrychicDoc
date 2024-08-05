package se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay;

import java.util.Arrays;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuickslotInventory;

@ParametersAreNonnullByDefault
public class QuickslotGroupGui extends GuiElement {

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    private QuickslotItemGui[] slots = new QuickslotItemGui[0];

    public QuickslotGroupGui(int x, int y) {
        super(x, y, 200, 0);
        this.isVisible = false;
        this.opacity = 0.0F;
        this.showAnimation = new KeyframeAnimation(200, this).applyTo(new Applier.TranslateX((float) (this.getX() + 10)), new Applier.Opacity(1.0F)).onStop(isFinished -> {
            if (isFinished) {
                Arrays.stream(this.slots).filter(Objects::nonNull).forEach(item -> item.setVisible(true));
            }
        });
        this.hideAnimation = new KeyframeAnimation(100, this).applyTo(new Applier.TranslateX((float) this.getX()), new Applier.Opacity(0.0F)).onStop(isFinished -> {
            if (isFinished) {
                this.isVisible = false;
            }
        });
    }

    public void setInventory(QuickslotInventory inventory) {
        this.clearChildren();
        int numSlots = inventory.m_6643_();
        this.slots = new QuickslotItemGui[numSlots];
        this.addChild(new GuiTexture(0, numSlots * -20 / 2 - 9, 22, 7, 0, 28, GuiTextures.toolbelt));
        this.addChild(new GuiTexture(0, numSlots * 20 / 2 + 2, 22, 7, 0, 35, GuiTextures.toolbelt));
        this.addChild(new GuiRect(0, numSlots * -20 / 2 - 2, 22, numSlots * 20 + 4, -872415232));
        for (int i = 0; i < numSlots; i++) {
            ItemStack itemStack = inventory.m_8020_(i);
            if (!itemStack.isEmpty()) {
                this.slots[i] = new QuickslotItemGui(-35, numSlots * -20 / 2 + i * 20, itemStack, i);
                this.addChild(this.slots[i]);
            }
        }
    }

    public void clear() {
        this.clearChildren();
    }

    @Override
    protected void onShow() {
        this.hideAnimation.stop();
        if (this.slots.length > 0) {
            this.showAnimation.start();
        }
    }

    @Override
    protected boolean onHide() {
        this.showAnimation.stop();
        this.hideAnimation.start();
        Arrays.stream(this.slots).filter(Objects::nonNull).forEach(item -> item.setVisible(false));
        return false;
    }

    public int getFocus() {
        return (Integer) Arrays.stream(this.slots).filter(Objects::nonNull).filter(GuiElement::hasFocus).map(QuickslotItemGui::getSlot).findFirst().orElse(-1);
    }

    public InteractionHand getHand() {
        return (InteractionHand) Arrays.stream(this.slots).filter(Objects::nonNull).filter(GuiElement::hasFocus).map(QuickslotItemGui::getHand).findFirst().orElse(null);
    }
}