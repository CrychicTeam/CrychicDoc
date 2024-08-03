package noppes.npcs.client.gui.player.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class InventoryTabVanilla extends AbstractTab {

    public InventoryTabVanilla() {
        super(0, 0, 0, new ItemStack(Blocks.CRAFTING_TABLE));
    }

    @Override
    public void onTabClicked() {
        Minecraft mc = Minecraft.getInstance();
        mc.player.connection.send(new ServerboundContainerClosePacket(mc.player.f_36096_.containerId));
        InventoryScreen inventory = new InventoryScreen(mc.player);
        mc.setScreen(inventory);
    }

    @Override
    public boolean shouldAddToList() {
        return true;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }
}