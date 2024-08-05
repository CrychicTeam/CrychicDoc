package se.mickelus.tetra.blocks.workbench.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class GuiInventoryInfo extends GuiElement {

    private final Inventory inventory;

    private final GuiInventoryHighlight[] highlights;

    private ItemStack targetStack;

    private String slot;

    private UpgradeSchematic schematic;

    public GuiInventoryInfo(int x, int y, Player player) {
        super(x, y, 224, 72);
        this.inventory = player.getInventory();
        this.highlights = new GuiInventoryHighlight[36];
        for (int xo = 0; xo < 9; xo++) {
            for (int yo = 0; yo < 3; yo++) {
                GuiInventoryHighlight highlight = new GuiInventoryHighlight(xo * 17, yo * 17 + 2, xo + yo);
                highlight.setVisible(false);
                this.addChild(highlight);
                this.highlights[9 + yo * 9 + xo] = highlight;
            }
        }
        for (int xo = 0; xo < 9; xo++) {
            GuiInventoryHighlight highlight = new GuiInventoryHighlight(xo * 17, 57, 4 + xo);
            highlight.setVisible(false);
            this.addChild(highlight);
            this.highlights[xo] = highlight;
        }
    }

    public void update() {
        if (this.schematic != null && this.targetStack != null) {
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 3; y++) {
                    int stackIndex = y * 9 + x + 9;
                    boolean shouldHighlight = false;
                    for (int materialIndex = 0; materialIndex < this.schematic.getNumMaterialSlots(); materialIndex++) {
                        if (this.schematic.acceptsMaterial(this.targetStack, this.slot, materialIndex, this.inventory.getItem(stackIndex))) {
                            shouldHighlight = true;
                            break;
                        }
                    }
                    this.highlights[stackIndex].setVisible(shouldHighlight);
                }
            }
            for (int stackIndex = 0; stackIndex < 9; stackIndex++) {
                boolean shouldHighlight = false;
                for (int materialIndexx = 0; materialIndexx < this.schematic.getNumMaterialSlots(); materialIndexx++) {
                    if (this.schematic.acceptsMaterial(this.targetStack, this.slot, materialIndexx, this.inventory.getItem(stackIndex))) {
                        shouldHighlight = true;
                        break;
                    }
                }
                this.highlights[stackIndex].setVisible(shouldHighlight);
            }
        } else {
            for (GuiInventoryHighlight highlight : this.highlights) {
                highlight.setVisible(false);
            }
        }
    }

    public void update(UpgradeSchematic schematic, String slot, ItemStack targetStack) {
        this.schematic = schematic;
        this.slot = slot;
        this.targetStack = targetStack;
        this.update();
    }
}