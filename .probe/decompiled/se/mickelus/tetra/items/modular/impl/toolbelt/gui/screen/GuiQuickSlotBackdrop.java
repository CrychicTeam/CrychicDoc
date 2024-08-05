package se.mickelus.tetra.items.modular.impl.toolbelt.gui.screen;

import java.util.Collection;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.impl.toolbelt.SlotType;

@ParametersAreNonnullByDefault
public class GuiQuickSlotBackdrop extends GuiElement {

    public GuiQuickSlotBackdrop(int x, int y, int numSlots, Collection<Collection<ItemEffect>> inventoryEffects) {
        super(x, y, numSlots * 17 - 9, 28);
        this.setAttachmentPoint(GuiAttachment.bottomCenter);
        this.setAttachmentAnchor(GuiAttachment.topCenter);
        this.addChild(new GuiRect(0, 3, this.width, 22, -16777216));
        this.addChild(new GuiRect(0, 4, this.width, 20, 8355711));
        this.addChild(new GuiRect(0, 5, this.width, 18, -16777216));
        GuiTexture leftCap = new GuiTexture(0, 0, 16, 28, GuiTextures.toolbelt);
        leftCap.setAttachmentPoint(GuiAttachment.topRight);
        this.addChild(leftCap);
        GuiTexture rightCap = new GuiTexture(0, 0, 16, 28, 16, 0, GuiTextures.toolbelt);
        rightCap.setAttachmentPoint(GuiAttachment.topLeft);
        rightCap.setAttachmentAnchor(GuiAttachment.topRight);
        this.addChild(rightCap);
        GuiSlotEffect.getEffectsForInventory(SlotType.quick, inventoryEffects).forEach(this::addChild);
    }
}