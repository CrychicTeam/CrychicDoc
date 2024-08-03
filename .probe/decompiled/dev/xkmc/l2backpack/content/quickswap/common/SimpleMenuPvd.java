package dev.xkmc.l2backpack.content.quickswap.common;

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

public final class SimpleMenuPvd implements MenuProvider {

    private final ServerPlayer player;

    private final PlayerSlot<?> slot;

    private final ItemStack stack;

    private final BaseBagItem bag;

    private final SimpleMenuPvd.BagMenuFactory factory;

    public SimpleMenuPvd(ServerPlayer player, PlayerSlot<?> slot, BaseBagItem item, ItemStack stack, SimpleMenuPvd.BagMenuFactory factory) {
        this.player = player;
        this.slot = slot;
        this.stack = stack;
        this.bag = item;
        this.factory = factory;
    }

    @Override
    public Component getDisplayName() {
        return this.stack.getHoverName();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        CompoundTag tag = this.stack.getOrCreateTag();
        UUID uuid = tag.getUUID("container_id");
        return this.factory.create(id, inventory, this.slot, uuid, this.getDisplayName());
    }

    public void writeBuffer(FriendlyByteBuf buf) {
        CompoundTag tag = this.stack.getOrCreateTag();
        UUID id = tag.getUUID("container_id");
        this.slot.write(buf);
        buf.writeUUID(id);
    }

    public void open() {
        this.bag.checkInit(this.stack);
        NetworkHooks.openScreen(this.player, this, this::writeBuffer);
    }

    public interface BagMenuFactory {

        AbstractContainerMenu create(int var1, Inventory var2, PlayerSlot<?> var3, UUID var4, Component var5);
    }
}