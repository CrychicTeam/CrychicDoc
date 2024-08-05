package noobanidus.mods.lootr.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.server.ServerLifecycleHooks;
import noobanidus.mods.lootr.api.LootFiller;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;

public class ChestData extends SavedData {

    private String key;

    private BlockPos pos;

    private ResourceKey<Level> dimension;

    private UUID uuid;

    private Map<UUID, SpecialChestInventory> inventories = new HashMap();

    private NonNullList<ItemStack> reference;

    private boolean custom;

    private boolean entity;

    private int size = -1;

    protected ChestData(String key) {
        this.key = key;
    }

    public static String ID(UUID id) {
        String idString = id.toString();
        return "lootr/" + idString.charAt(0) + "/" + idString.substring(0, 2) + "/" + idString;
    }

    public static Supplier<ChestData> ref_id(ResourceKey<Level> dimension, BlockPos pos, UUID id, NonNullList<ItemStack> base) {
        if (id == null) {
            throw new IllegalArgumentException("Can't create ChestData for custom container in dimension '" + dimension + "' at '" + pos + "' with a null id.");
        } else {
            return () -> {
                ChestData data = new ChestData(ID(id));
                data.pos = pos;
                data.dimension = dimension;
                data.uuid = id;
                data.reference = base;
                data.custom = true;
                data.entity = false;
                if (data.reference == null) {
                    throw new IllegalArgumentException("Inventory reference cannot be null.");
                } else {
                    return data;
                }
            };
        }
    }

    public static Supplier<ChestData> id(ResourceKey<Level> dimension, BlockPos pos, UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't create ChestData for container in dimension '" + dimension + "' at '" + pos + "' with a null id.");
        } else {
            return () -> {
                ChestData data = new ChestData(ID(id));
                data.pos = pos;
                data.dimension = dimension;
                data.uuid = id;
                data.reference = null;
                data.custom = false;
                data.entity = false;
                return data;
            };
        }
    }

    public static Supplier<ChestData> entity(ResourceKey<Level> dimension, BlockPos pos, UUID entityId) {
        if (entityId == null) {
            throw new IllegalArgumentException("Can't create ChestData for minecart in dimension '" + dimension + "' at '" + pos + "' with a null entityId.");
        } else {
            return () -> {
                ChestData data = new ChestData(ID(entityId));
                data.pos = pos;
                data.dimension = dimension;
                data.uuid = entityId;
                data.entity = true;
                data.reference = null;
                data.custom = false;
                return data;
            };
        }
    }

    public static Function<CompoundTag, ChestData> loadWrapper(UUID id, ResourceKey<Level> dimension, BlockPos position) {
        return tag -> {
            ChestData result = load(tag);
            result.key = ID(id);
            result.dimension = dimension;
            result.pos = position;
            return result;
        };
    }

    public static ChestData unwrap(ChestData data, UUID id, ResourceKey<Level> dimension, BlockPos position, int size) {
        data.key = ID(id);
        data.dimension = dimension;
        data.pos = position;
        data.setSize(size);
        return data;
    }

    public static ChestData load(CompoundTag compound) {
        ChestData data = new ChestData(compound.getString("key"));
        data.inventories.clear();
        data.pos = null;
        data.dimension = null;
        if (compound.contains("position", 4)) {
            data.pos = BlockPos.of(compound.getLong("position"));
        } else if (compound.contains("position", 10)) {
            data.pos = NbtUtils.readBlockPos(compound.getCompound("position"));
        }
        if (compound.contains("dimension")) {
            data.dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(compound.getString("dimension")));
        }
        boolean foundNewUUID = false;
        if (compound.hasUUID("uuid")) {
            data.uuid = compound.getUUID("uuid");
            foundNewUUID = true;
        }
        boolean foundEntity = false;
        if (compound.hasUUID("entityId")) {
            if (data.uuid != null) {
                LootrAPI.LOG.error("Loaded an `entityId` from an already-migrated file: '" + data.key + "'");
            }
            data.uuid = compound.getUUID("entityId");
            data.entity = true;
            foundEntity = true;
        }
        if (compound.hasUUID("tileId")) {
            if (data.uuid != null) {
                if (foundEntity && !foundNewUUID) {
                    LootrAPI.LOG.error("Loaded a `tileId` from an unmigrated file that also has `entityId`: '" + data.key + "'");
                } else if (foundEntity) {
                    LootrAPI.LOG.error("Loaded a `tileId` from an already-migrated file that also had an `entityId`: '" + data.key + "'");
                } else if (foundNewUUID) {
                    LootrAPI.LOG.error("Loaded a `tileId` from an already-migrated file: '" + data.key + "'");
                }
            }
            data.uuid = compound.getUUID("tileId");
            data.entity = false;
        }
        if (compound.contains("custom")) {
            data.custom = compound.getBoolean("custom");
        }
        if (compound.contains("entity")) {
            data.entity = compound.getBoolean("entity");
        }
        if (compound.hasUUID("customId")) {
            LootrAPI.LOG.error("Loaded a `customId` from an old file when this field was never used. File was '" + data.key + "'");
            data.uuid = compound.getUUID("customId");
            data.entity = false;
            data.custom = true;
        }
        if (compound.contains("reference") && compound.contains("referenceSize")) {
            int size = compound.getInt("referenceSize");
            data.size = size;
            data.reference = NonNullList.withSize(data.size, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(compound.getCompound("reference"), data.reference);
        }
        if (compound.contains("size", 3)) {
            data.size = compound.getInt("size");
        } else if (!compound.contains("referenceSize")) {
            LootrAPI.LOG.error("Loaded a data file with no size: '" + data.key + "' located in dimension '" + data.dimension + "' at '" + data.pos + "'. Sizes will be guessed and updated in future. This message will only appear once; if it occurs multiple times for the same location, please report on GitHub.");
        }
        ListTag compounds = compound.getList("inventories", 10);
        if (data.size == -1) {
            int maxSlot = -1;
            for (int i = 0; i < compounds.size(); i++) {
                CompoundTag thisTag = compounds.getCompound(i);
                ListTag items = thisTag.getCompound("chest").getList("Items", 10);
                for (int j = 0; j < items.size(); j++) {
                    CompoundTag itemTag = items.getCompound(j);
                    int slot = itemTag.getByte("Slot") & 255;
                    if (slot > maxSlot) {
                        maxSlot = slot;
                    }
                }
            }
            data.size = maxSlot % 9 == 0 ? maxSlot : maxSlot + (9 - maxSlot % 9);
        }
        for (int i = 0; i < compounds.size(); i++) {
            CompoundTag thisTag = compounds.getCompound(i);
            CompoundTag items = thisTag.getCompound("chest");
            String name = thisTag.getString("name");
            UUID uuid = thisTag.getUUID("uuid");
            data.inventories.put(uuid, new SpecialChestInventory(data, items, name));
        }
        return data;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public String getKey() {
        return this.key;
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    public int getSize() {
        return this.size;
    }

    private void setSize(int size) {
        if (this.size != size) {
            if (size < this.size) {
                throw new IllegalArgumentException("Cannot resize inventory associated with '" + this.getKey() + "' in dimension '" + this.getDimension() + "' at location '" + this.getPos() + "' to a smaller size.");
            } else {
                this.size = size;
                for (SpecialChestInventory inventory : this.inventories.values()) {
                    inventory.resizeInventory(size);
                }
            }
        }
    }

    @Nullable
    public UUID getEntityId() {
        return this.entity ? this.uuid : null;
    }

    @Nullable
    public UUID getTileId() {
        return !this.entity ? this.uuid : null;
    }

    public boolean isEntity() {
        return this.entity;
    }

    public LootFiller customInventory() {
        return (player, inventory, table, seed) -> {
            for (int i = 0; i < this.reference.size(); i++) {
                inventory.setItem(i, this.reference.get(i).copy());
            }
        };
    }

    public boolean clearInventory(UUID uuid) {
        return this.inventories.remove(uuid) != null;
    }

    @Nullable
    public SpecialChestInventory getInventory(ServerPlayer player) {
        return (SpecialChestInventory) this.inventories.get(player.m_20148_());
    }

    public SpecialChestInventory createInventory(ServerPlayer player, LootFiller filler, IntSupplier sizeSupplier, Supplier<Component> displaySupplier, Supplier<ResourceLocation> tableSupplier, LongSupplier seedSupplier) {
        ServerLevel level = (ServerLevel) player.m_9236_();
        if (level.m_46472_() != this.dimension) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) {
                return null;
            }
            level = server.getLevel(this.dimension);
        }
        if (level == null) {
            return null;
        } else {
            NonNullList<ItemStack> items = NonNullList.withSize(sizeSupplier.getAsInt(), ItemStack.EMPTY);
            SpecialChestInventory result = new SpecialChestInventory(this, items, (Component) displaySupplier.get());
            filler.unpackLootTable(player, result, (ResourceLocation) tableSupplier.get(), seedSupplier.getAsLong());
            this.inventories.put(player.m_20148_(), result);
            this.m_77762_();
            return result;
        }
    }

    public SpecialChestInventory createInventory(ServerPlayer player, LootFiller filler, BaseContainerBlockEntity blockEntity, Supplier<ResourceLocation> tableSupplier, LongSupplier seedSupplier) {
        ServerLevel level = (ServerLevel) player.m_9236_();
        if (level.m_46472_() != this.dimension) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) {
                return null;
            }
            level = server.getLevel(this.dimension);
        }
        if (level == null) {
            return null;
        } else {
            NonNullList<ItemStack> items = NonNullList.withSize(blockEntity.m_6643_(), ItemStack.EMPTY);
            SpecialChestInventory result = new SpecialChestInventory(this, items, blockEntity.getDisplayName());
            filler.unpackLootTable(player, result, (ResourceLocation) tableSupplier.get(), seedSupplier.getAsLong());
            this.inventories.put(player.m_20148_(), result);
            this.m_77762_();
            return result;
        }
    }

    public SpecialChestInventory createInventory(ServerPlayer player, LootFiller filler, @Nullable RandomizableContainerBlockEntity tile) {
        ServerLevel world = (ServerLevel) player.m_9236_();
        long seed = -1L;
        SpecialChestInventory result;
        ResourceLocation lootTable;
        if (this.entity) {
            if (!(world.getEntity(this.uuid) instanceof LootrChestMinecartEntity cart)) {
                return null;
            }
            NonNullList items = NonNullList.withSize(cart.getContainerSize(), ItemStack.EMPTY);
            result = new SpecialChestInventory(this, items, cart.m_5446_());
            lootTable = cart.f_38204_;
        } else {
            if (tile == null) {
                return null;
            }
            lootTable = ((ILootBlockEntity) tile).getTable();
            NonNullList<ItemStack> items = NonNullList.withSize(tile.m_6643_(), ItemStack.EMPTY);
            result = new SpecialChestInventory(this, items, tile.m_5446_());
        }
        filler.unpackLootTable(player, result, lootTable, seed);
        this.inventories.put(player.m_20148_(), result);
        this.m_77762_();
        return result;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        if (this.key != null) {
            compound.putString("key", this.key);
        } else {
            LootrAPI.LOG.error("Attempted to save a data file with no key! How could this happen?" + this);
        }
        if (this.pos != null) {
            compound.put("position", NbtUtils.writeBlockPos(this.pos));
        } else {
            LootrAPI.LOG.error("Attempted to save a data file with no `position`: '" + this.key + "'");
        }
        if (this.dimension != null) {
            compound.putString("dimension", this.dimension.location().toString());
        } else {
            LootrAPI.LOG.error("Attempted to save a data file with no `dimension`: '" + this.key + "'");
        }
        if (this.uuid == null) {
            throw new IllegalStateException("Attempted to save a data file with no `uuid`: '" + this.key + "'. Located in dimension '" + this.dimension + "' at '" + this.pos + "'. This is an unrecoverable error.");
        } else {
            compound.putUUID("uuid", this.uuid);
            compound.putBoolean("custom", this.custom);
            compound.putBoolean("entity", this.entity);
            compound.putInt("size", this.size);
            if (this.reference != null) {
                compound.putInt("referenceSize", this.reference.size());
                compound.put("reference", ContainerHelper.saveAllItems(new CompoundTag(), this.reference, true));
            }
            ListTag compounds = new ListTag();
            for (Entry<UUID, SpecialChestInventory> entry : this.inventories.entrySet()) {
                CompoundTag thisTag = new CompoundTag();
                thisTag.putUUID("uuid", (UUID) entry.getKey());
                thisTag.put("chest", ((SpecialChestInventory) entry.getValue()).writeItems());
                thisTag.putString("name", ((SpecialChestInventory) entry.getValue()).writeName());
                compounds.add(thisTag);
            }
            compound.put("inventories", compounds);
            return compound;
        }
    }

    public void clear() {
        this.inventories.clear();
    }

    @Override
    public void save(File pFile) {
        if (this.m_77764_()) {
            pFile.getParentFile().mkdirs();
        }
        super.save(pFile);
    }
}