package dev.xkmc.l2backpack.content.remote.worldchest;

import dev.xkmc.l2backpack.content.backpack.BackpackMenu;
import dev.xkmc.l2backpack.content.click.DrawerQuickInsert;
import dev.xkmc.l2backpack.content.remote.common.StorageContainer;
import dev.xkmc.l2backpack.init.registrate.BackpackMenus;
import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WorldChestContainer extends BaseContainerMenu<WorldChestContainer> implements DrawerQuickInsert {

    protected final Player player;

    @Nullable
    protected final StorageContainer storage;

    @Nullable
    private final WorldChestBlockEntity activeChest;

    public static WorldChestContainer fromNetwork(MenuType<WorldChestContainer> type, int windowId, Inventory inv) {
        return new WorldChestContainer(windowId, inv, new SimpleContainer(27), null, null);
    }

    public WorldChestContainer(int windowId, Inventory inventory, SimpleContainer cont, @Nullable StorageContainer storage, @Nullable WorldChestBlockEntity entity) {
        super((MenuType<?>) BackpackMenus.MT_WORLD_CHEST.get(), windowId, inventory, BackpackMenu.MANAGERS[2], menu -> cont, false);
        this.player = inventory.player;
        this.addSlot("grid", stack -> true);
        this.storage = storage;
        this.activeChest = entity;
    }

    public int getColor() {
        assert this.storage != null;
        return this.storage.color;
    }

    public UUID getOwner() {
        assert this.storage != null;
        return this.storage.id;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.activeChest != null && !this.activeChest.stillValid(player) ? false : this.storage == null || this.storage.isValid();
    }

    @Override
    public ItemStack quickMoveStack(Player pl, int id) {
        ItemStack stack = ((Slot) this.f_38839_.get(id)).getItem();
        if (this.quickMove(pl, this, stack, id)) {
            ((Slot) this.f_38839_.get(id)).setChanged();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean quickMove(Player pl, AbstractContainerMenu menu, ItemStack stack, int id) {
        int n = this.container.getContainerSize();
        boolean moved;
        if (id >= 36) {
            moved = DrawerQuickInsert.moveItemStackTo(pl, this, stack, 0, 36, true);
        } else {
            moved = DrawerQuickInsert.moveItemStackTo(pl, this, stack, 36, 36 + n, false);
        }
        return moved;
    }
}