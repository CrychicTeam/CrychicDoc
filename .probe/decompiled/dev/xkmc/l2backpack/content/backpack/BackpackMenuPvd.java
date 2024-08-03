package dev.xkmc.l2backpack.content.backpack;

import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

public final class BackpackMenuPvd implements MenuProvider {

    private final ServerPlayer player;

    private final PlayerSlot<?> slot;

    private final ItemStack stack;

    private final BaseBagItem bag;

    public BackpackMenuPvd(ServerPlayer player, PlayerSlot<?> slot, BaseBagItem item, ItemStack stack) {
        this.player = player;
        this.slot = slot;
        this.stack = stack;
        this.bag = item;
    }

    @Override
    public Component getDisplayName() {
        return this.stack.getHoverName();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        CompoundTag tag = this.stack.getOrCreateTag();
        UUID uuid = tag.getUUID("container_id");
        return new BackpackMenu(id, inventory, this.slot, uuid, this.bag.getRows(this.stack), this.getDisplayName());
    }

    public void writeBuffer(FriendlyByteBuf buf) {
        CompoundTag tag = this.stack.getOrCreateTag();
        UUID id = tag.getUUID("container_id");
        this.slot.write(buf);
        buf.writeUUID(id);
        buf.writeInt(this.bag.getRows(this.stack));
    }

    public void open() {
        CompoundTag tag = this.stack.getOrCreateTag();
        this.bag.checkInit(this.stack);
        NetworkHooks.openScreen(this.player, this, this::writeBuffer);
    }
}