package dev.latvian.mods.kubejs.player;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

@Info("Invoked when a player opens a chest.\n\nSame as `PlayerEvents.inventoryOpened`, but only for chests.\n")
public class ChestEventJS extends InventoryEventJS {

    public ChestEventJS(Player player, AbstractContainerMenu menu) {
        super(player, menu);
    }

    @Info("Gets the chest inventory.")
    public Container getInventory() {
        return ((ChestMenu) this.getInventoryContainer()).getContainer();
    }

    @Info("Gets the chest block.")
    @Nullable
    public BlockContainerJS getBlock() {
        return this.getInventory() instanceof BlockEntity be ? this.getLevel().kjs$getBlock(be) : null;
    }
}