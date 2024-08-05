package dev.xkmc.l2backpack.content.remote.worldchest;

import dev.xkmc.l2backpack.content.remote.common.StorageContainer;
import dev.xkmc.l2backpack.content.remote.common.WorldStorage;
import dev.xkmc.l2backpack.init.advancement.BackpackTriggers;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

public record WorldChestMenuPvd(ServerPlayer player, ItemStack stack, WorldChestItem item) implements MenuProvider {

    @Override
    public Component getDisplayName() {
        return this.stack.getHoverName();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        StorageContainer container = (StorageContainer) this.getContainer((ServerLevel) player.m_9236_()).get();
        if (!container.id.equals(player.m_20148_())) {
            BackpackTriggers.SHARE.trigger((ServerPlayer) player);
        }
        return new WorldChestContainer(id, inventory, container.container, container, null);
    }

    public Optional<StorageContainer> getContainer(ServerLevel level) {
        CompoundTag tag = this.stack.getOrCreateTag();
        UUID id = tag.getUUID("owner_id");
        long pwd = tag.getLong("password");
        long seed = 0L;
        ResourceLocation loot = null;
        if (tag.contains("loot")) {
            loot = new ResourceLocation(tag.getString("loot"));
            tag.remove("loot");
            seed = tag.getLong("seed");
            tag.remove("seed");
        }
        return WorldStorage.get(level).getOrCreateStorage(id, this.item.color.getId(), pwd, this.player, loot, seed);
    }

    public void open() {
        this.item.refresh(this.stack, this.player);
        if (!this.player.m_9236_().isClientSide() && !this.getContainer((ServerLevel) this.player.m_9236_()).isEmpty()) {
            NetworkHooks.openScreen(this.player, this);
        }
    }
}