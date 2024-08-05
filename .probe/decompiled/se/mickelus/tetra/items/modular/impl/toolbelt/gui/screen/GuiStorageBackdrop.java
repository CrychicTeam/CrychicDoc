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
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.StorageInventory;

@ParametersAreNonnullByDefault
public class GuiStorageBackdrop extends GuiElement {

    public GuiStorageBackdrop(int x, int y, int numSlots, Collection<Collection<ItemEffect>> inventoryEffects) {
        super(x, y, 0, 0);
        this.setAttachmentPoint(GuiAttachment.bottomCenter);
        this.setAttachmentAnchor(GuiAttachment.topCenter);
        int maxCols = StorageInventory.getColumns(numSlots);
        int cols = Math.min(numSlots, maxCols);
        int rows = 1 + (numSlots - 1) / cols;
        this.setWidth(cols * 17 - 9);
        this.setHeight(rows * 17 + 11);
        this.addChild(new GuiRect(0, 3, this.width, this.height - 6, 0));
        this.addChild(new GuiRect(0, 4, this.width, 1, 4210752));
        this.addChild(new GuiRect(0, -4, this.width, 1, 8355711).setAttachment(GuiAttachment.bottomLeft));
        if (this.height > 28) {
            this.addChild(new GuiRect(-7, 14, this.width + 14, this.height - 28, 0));
            this.addChild(new GuiRect(-6, 14, 1, this.height - 28, 4210752));
            this.addChild(new GuiRect(6, 14, 1, this.height - 28, 4210752).setAttachment(GuiAttachment.topRight));
            for (int i = 0; i < cols - 1; i++) {
                for (int j = 0; j < rows - 1; j++) {
                    this.addChild(new GuiRect(i * 17 - 3 + 12, j * 17 + 22, 7, 1, 4210752));
                    this.addChild(new GuiRect(i * 17 + 12, j * 17 - 3 + 22, 1, 7, 4210752));
                    this.addChild(new GuiRect(i * 17 + 11, j * 17 + 21, 3, 3, 0));
                }
            }
        }
        this.addChild(new GuiTexture(0, 0, 16, 14, 32, 0, GuiTextures.toolbelt).setAttachmentPoint(GuiAttachment.topRight));
        this.addChild(new GuiTexture(0, 0, 16, 14, 32, 14, GuiTextures.toolbelt).setAttachmentPoint(GuiAttachment.bottomRight).setAttachmentAnchor(GuiAttachment.bottomLeft));
        this.addChild(new GuiTexture(0, 0, 16, 14, 48, 0, GuiTextures.toolbelt).setAttachmentPoint(GuiAttachment.topLeft).setAttachmentAnchor(GuiAttachment.topRight));
        this.addChild(new GuiTexture(0, 0, 16, 14, 48, 14, GuiTextures.toolbelt).setAttachmentPoint(GuiAttachment.bottomLeft).setAttachmentAnchor(GuiAttachment.bottomRight));
        GuiSlotEffect.getEffectsForInventory(SlotType.storage, inventoryEffects, cols).forEach(this::addChild);
    }
}