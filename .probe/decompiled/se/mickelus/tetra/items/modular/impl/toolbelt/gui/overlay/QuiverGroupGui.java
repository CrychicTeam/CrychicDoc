package se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay;

import java.util.Arrays;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuiverInventory;

@ParametersAreNonnullByDefault
public class QuiverGroupGui extends GuiElement {

    QuiverInventory inventory;

    private QuiverItemGui[] slots = new QuiverItemGui[0];

    public QuiverGroupGui(int x, int y) {
        super(x, y, 0, 0);
    }

    public void setInventory(QuiverInventory inventory) {
        this.clearChildren();
        this.inventory = inventory;
        ItemStack[] aggregatedStacks = inventory.getAggregatedStacks();
        this.slots = new QuiverItemGui[aggregatedStacks.length];
        this.width = aggregatedStacks.length * 13;
        this.height = aggregatedStacks.length * 13;
        for (int i = 0; i < aggregatedStacks.length; i++) {
            ItemStack itemStack = aggregatedStacks[i];
            this.slots[i] = new QuiverItemGui(-13 * i, -13 * i, itemStack, i);
            this.addChild(this.slots[i]);
        }
    }

    public void clear() {
        this.clearChildren();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            Arrays.stream(this.slots).filter(Objects::nonNull).forEach(item -> item.setVisible(true));
        } else {
            Arrays.stream(this.slots).filter(Objects::nonNull).forEach(item -> item.setVisible(false));
        }
    }

    public int getFocus() {
        for (int i = 0; i < this.slots.length; i++) {
            QuiverItemGui element = this.slots[i];
            if (element != null && element.hasFocus()) {
                ItemStack itemStack = element.getItemStack();
                return this.inventory.getFirstIndexForStack(itemStack);
            }
        }
        return -1;
    }

    public InteractionHand getHand() {
        return null;
    }
}