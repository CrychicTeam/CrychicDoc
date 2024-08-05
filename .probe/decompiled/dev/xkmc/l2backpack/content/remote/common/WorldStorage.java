package dev.xkmc.l2backpack.content.remote.common;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class WorldStorage {

    public static Capability<WorldStorage> CAPABILITY = CapabilityManager.get(new CapabilityToken<WorldStorage>() {
    });

    public final ServerLevel level;

    @SerialField
    private final HashMap<String, CompoundTag> storage = new HashMap();

    private final HashMap<UUID, StorageContainer[]> cache = new HashMap();

    @SerialField
    final HashMap<String, HashMap<Item, Integer>> drawer = new HashMap();

    private final HashMap<String, HashMap<Item, DrawerAccess>> drawer_cache = new HashMap();

    public static WorldStorage get(ServerLevel level) {
        return (WorldStorage) level.getServer().overworld().getCapability(CAPABILITY).resolve().get();
    }

    public WorldStorage(ServerLevel level) {
        this.level = level;
    }

    public Optional<StorageContainer> getOrCreateStorage(UUID id, int color, long password, @Nullable ServerPlayer player, @Nullable ResourceLocation loot, long seed) {
        if (this.cache.containsKey(id)) {
            StorageContainer storage = ((StorageContainer[]) this.cache.get(id))[color];
            if (storage != null) {
                if (storage.password == password) {
                    return Optional.of(storage);
                }
                return Optional.empty();
            }
        }
        CompoundTag col = this.getColor(id, color, password);
        if (col.getLong("password") != password) {
            return Optional.empty();
        } else {
            StorageContainer storage = new StorageContainer(id, color, col);
            if (loot != null) {
                LootTable loottable = this.level.getServer().getLootData().m_278676_(loot);
                LootParams.Builder builder = new LootParams.Builder(this.level);
                if (player != null) {
                    builder.withLuck(player.m_36336_()).withParameter(LootContextParams.THIS_ENTITY, player);
                }
                loottable.fill(storage.container, builder.create(LootContextParamSets.EMPTY), seed);
            }
            this.putStorage(id, color, storage);
            return Optional.of(storage);
        }
    }

    public Optional<StorageContainer> getStorageWithoutPassword(UUID id, int color) {
        if (this.cache.containsKey(id)) {
            StorageContainer storage = ((StorageContainer[]) this.cache.get(id))[color];
            if (storage != null) {
                return Optional.of(storage);
            }
        }
        Optional<CompoundTag> colOptional = this.getColorWithoutPassword(id, color);
        if (colOptional.isEmpty()) {
            return Optional.empty();
        } else {
            StorageContainer storage = new StorageContainer(id, color, (CompoundTag) colOptional.get());
            this.putStorage(id, color, storage);
            return Optional.of(storage);
        }
    }

    public StorageContainer changePassword(UUID id, int color, long password) {
        this.cache.remove(id);
        CompoundTag col = this.getColor(id, color, password);
        col.putLong("password", password);
        StorageContainer storage = new StorageContainer(id, color, col);
        this.putStorage(id, color, storage);
        return storage;
    }

    private void putStorage(UUID id, int color, StorageContainer storage) {
        StorageContainer[] arr;
        if (this.cache.containsKey(id)) {
            arr = (StorageContainer[]) this.cache.get(id);
        } else {
            this.cache.put(id, arr = new StorageContainer[16]);
        }
        arr[color] = storage;
    }

    private CompoundTag getColor(UUID id, int color, long password) {
        String sid = id.toString();
        CompoundTag ans;
        if (!this.storage.containsKey(sid)) {
            this.storage.put(sid, ans = new CompoundTag());
            ans.putUUID("owner_id", id);
        } else {
            ans = (CompoundTag) this.storage.get(sid);
        }
        CompoundTag col;
        if (ans.contains("color_" + color)) {
            col = ans.getCompound("color_" + color);
        } else {
            col = new CompoundTag();
            col.putLong("password", password);
            ans.put("color_" + color, col);
        }
        return col;
    }

    private Optional<CompoundTag> getColorWithoutPassword(UUID id, int color) {
        String sid = id.toString();
        if (!this.storage.containsKey(sid)) {
            return Optional.empty();
        } else {
            CompoundTag ans = (CompoundTag) this.storage.get(sid);
            if (ans.contains("color_" + color)) {
                CompoundTag col = ans.getCompound("color_" + color);
                return Optional.of(col);
            } else {
                return Optional.empty();
            }
        }
    }

    public void init() {
    }

    public DrawerAccess getOrCreateDrawer(UUID id, Item item) {
        return (DrawerAccess) ((HashMap) this.drawer_cache.computeIfAbsent(id.toString(), e -> new HashMap())).computeIfAbsent(item, i -> new DrawerAccess(this, id, item));
    }
}