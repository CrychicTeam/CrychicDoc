package dev.xkmc.l2backpack.content.common;

import dev.xkmc.l2backpack.content.click.DrawerQuickInsert;
import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class BaseBagMenu<T extends BaseBagMenu<T>> extends BaseContainerMenu<T> implements DrawerQuickInsert {

    protected final Player player;

    public final PlayerSlot<?> item_slot;

    protected final UUID uuid;

    protected final IItemHandlerModifiable handler;

    private ItemStack stack_cache = ItemStack.EMPTY;

    public BaseBagMenu(MenuType<T> type, int windowId, Inventory inventory, SpriteManager manager, PlayerSlot<?> hand, UUID uuid, int row) {
        super(type, windowId, inventory, manager, menu -> new SimpleContainer(0), false);
        this.item_slot = hand;
        this.uuid = uuid;
        this.player = inventory.player;
        ItemStack stack = this.getStack();
        if (stack.getItem() instanceof BaseBagItem) {
            IItemHandler inv = (IItemHandler) stack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
            if (this.player instanceof ServerPlayer sp && inv instanceof BaseBagInvWrapper bag) {
                bag.attachEnv(sp, hand);
            }
            this.handler = (IItemHandlerModifiable) inv;
        } else {
            this.handler = new InvWrapper(new SimpleContainer(row * 9));
        }
        this.addSlot("grid");
        if (!this.player.m_9236_().isClientSide()) {
            BaseBagItem.checkLootGen(this.getStack(), this.player);
        }
    }

    protected void addSlot(String name) {
        this.sprite.get().getSlot(name, (x, y) -> new BagSlot(this.handler, this.added++, x, y), (x$0, x$1, x$2, x$3) -> this.addSlot(x$0, x$1, x$2, x$3));
    }

    @Override
    public boolean stillValid(Player player) {
        ItemStack oldStack = this.stack_cache;
        ItemStack newStack = this.getStackRaw();
        return !this.getStackRaw().isEmpty() && oldStack == newStack ? this.getStack().getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get() == this.handler : false;
    }

    public ItemStack getStack() {
        ItemStack stack = this.getStackRaw();
        return stack.isEmpty() ? this.stack_cache : stack;
    }

    private ItemStack getStackRaw() {
        ItemStack stack = this.item_slot.getItem(this.player);
        CompoundTag tag = stack.getTag();
        if (this.player.m_9236_().isClientSide()) {
            return stack;
        } else if (tag == null) {
            return ItemStack.EMPTY;
        } else if (!tag.contains("container_id")) {
            return ItemStack.EMPTY;
        } else if (!tag.getUUID("container_id").equals(this.uuid)) {
            return ItemStack.EMPTY;
        } else {
            this.stack_cache = stack;
            return stack;
        }
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
        int n = this.handler.getSlots();
        boolean moved;
        if (id >= 36) {
            moved = DrawerQuickInsert.moveItemStackTo(pl, this, stack, 0, 36, true);
        } else {
            moved = DrawerQuickInsert.moveItemStackTo(pl, this, stack, 36, 36 + n, false);
        }
        return moved;
    }
}