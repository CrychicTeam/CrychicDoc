package se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay;

import java.util.Arrays;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.PotionsInventory;

@ParametersAreNonnullByDefault
public class PotionGroupGui extends GuiElement {

    GuiString focusSlot;

    PotionsInventory inventory;

    private PotionItemGui[] slots = new PotionItemGui[0];

    public PotionGroupGui(int x, int y) {
        super(x, y, 0, 0);
        this.focusSlot = new GuiString(0, -15, "");
        this.focusSlot.setAttachmentPoint(GuiAttachment.topCenter);
        this.focusSlot.setAttachmentAnchor(GuiAttachment.topCenter);
    }

    public void setInventory(PotionsInventory inventory) {
        this.clearChildren();
        this.inventory = inventory;
        int numSlots = inventory.m_6643_();
        this.slots = new PotionItemGui[numSlots];
        this.focusSlot.setString("");
        this.addChild(this.focusSlot);
        this.width = 66;
        if (numSlots > 5) {
            this.height = 44;
        } else if (numSlots > 3) {
            this.height = 33;
        } else {
            this.width = numSlots * 22;
            this.height = 22;
        }
        for (int i = 0; i < numSlots; i++) {
            ItemStack itemStack = inventory.m_8020_(i);
            if (!itemStack.isEmpty()) {
                if (i > 6) {
                    this.slots[i] = new PotionItemGui(22, 22, itemStack, i, true);
                } else if (i > 4) {
                    this.slots[i] = new PotionItemGui((i - 5) * 22 + 11, -11, itemStack, i, true);
                } else if (i > 2) {
                    this.slots[i] = new PotionItemGui((i - 3) * 22 + 11, 11, itemStack, i, true);
                } else {
                    this.slots[i] = new PotionItemGui(i * 22, 0, itemStack, i, true);
                }
                this.addChild(this.slots[i]);
            }
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
        this.focusSlot.setVisible(visible);
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        int focus = this.getFocus();
        if (focus != -1) {
            this.focusSlot.setString(this.inventory.m_8020_(focus).getHoverName().getString());
        } else {
            this.focusSlot.setString("");
        }
    }

    public int getFocus() {
        for (int i = 0; i < this.slots.length; i++) {
            PotionItemGui element = this.slots[i];
            if (element != null && element.hasFocus()) {
                return element.getSlot();
            }
        }
        return -1;
    }

    public InteractionHand getHand() {
        return null;
    }
}