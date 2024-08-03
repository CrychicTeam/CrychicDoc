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
public class PotionBackdropGui extends GuiElement {

    public PotionBackdropGui(int x, int y, int numSlots, Collection<Collection<ItemEffect>> inventoryEffects) {
        super(x, y, numSlots * 17 - 9, 28);
        this.setAttachmentPoint(GuiAttachment.bottomCenter);
        this.setAttachmentAnchor(GuiAttachment.topCenter);
        this.addChild(new GuiRect(0, 3, this.width, 22, -16777216));
        this.addChild(new GuiRect(0, 4, this.width, 1, 4210752));
        this.addChild(new GuiRect(0, 23, this.width, 1, 8355711));
        GuiTexture leftCap = new GuiTexture(0, 0, 16, 28, 64, 0, GuiTextures.toolbelt);
        leftCap.setAttachmentPoint(GuiAttachment.topRight);
        this.addChild(leftCap);
        GuiTexture rightCap = new GuiTexture(0, 0, 16, 28, 80, 0, GuiTextures.toolbelt);
        rightCap.setAttachmentPoint(GuiAttachment.topLeft);
        rightCap.setAttachmentAnchor(GuiAttachment.topRight);
        this.addChild(rightCap);
        GuiSlotEffect.getEffectsForInventory(SlotType.potion, inventoryEffects).forEach(this::addChild);
    }
}