package dev.xkmc.l2backpack.content.remote.common;

import dev.xkmc.l2backpack.content.drawer.BaseDrawerItem;
import dev.xkmc.l2backpack.content.remote.drawer.EnderDrawerBlockEntity;
import dev.xkmc.l2library.util.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class DrawerAccess {

    private final WorldStorage storage;

    private final UUID id;

    private final Item item;

    public final List<EnderDrawerBlockEntity> listener = new ArrayList();

    public static DrawerAccess of(Level level, ItemStack drawer) {
        UUID id = drawer.getOrCreateTag().getUUID("owner_id");
        Item item = BaseDrawerItem.getItem(drawer);
        return of(level, id, item);
    }

    public static DrawerAccess of(Level level, UUID id, Item item) {
        return WorldStorage.get((ServerLevel) level).getOrCreateDrawer(id, item);
    }

    DrawerAccess(WorldStorage storage, UUID id, Item item) {
        this.storage = storage;
        this.id = id;
        this.item = item;
    }

    private HashMap<Item, Integer> getMap() {
        return (HashMap<Item, Integer>) this.storage.drawer.computeIfAbsent(this.id.toString(), k -> new HashMap());
    }

    public int getCount() {
        return (Integer) this.getMap().computeIfAbsent(this.item, k -> 0);
    }

    public void setCount(int count) {
        this.getMap().put(this.item, count);
        this.listener.forEach(BlockEntity::m_6596_);
    }

    public Item item() {
        return this.item;
    }

    public Optional<ServerPlayer> getOwner() {
        return Proxy.getServer().map(e -> e.getPlayerList().getPlayer(this.id));
    }
}