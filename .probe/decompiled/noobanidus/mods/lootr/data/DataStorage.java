package noobanidus.mods.lootr.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.server.ServerLifecycleHooks;
import noobanidus.mods.lootr.api.LootFiller;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;

public class DataStorage {

    public static final String ID_OLD = "Lootr-AdvancementData";

    public static final String SCORED_OLD = "Lootr-ScoreData";

    public static final String DECAY_OLD = "Lootr-DecayData";

    public static final String REFRESH_OLD = "Lootr-RefreshData";

    public static final String ID = "lootr/Lootr-AdvancementData";

    public static final String SCORED = "lootr/Lootr-ScoreData";

    public static final String DECAY = "lootr/Lootr-DecayData";

    public static final String REFRESH = "lootr/Lootr-RefreshData";

    @Nullable
    public static DimensionDataStorage getDataStorage() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            LootrAPI.LOG.error("MinecraftServer is null at this stage; Lootr cannot fetch data storage.");
            return null;
        } else {
            ServerLevel overworld = server.overworld();
            if (overworld == null) {
                LootrAPI.LOG.error("The Overworld is null at this stage; Lootr cannot fetch data storage.");
                return null;
            } else {
                return overworld.getDataStorage();
            }
        }
    }

    public static boolean isAwarded(UUID player, UUID tileId) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot determine if advancement has been awarded.");
            return false;
        } else {
            AdvancementData data = manager.computeIfAbsent(AdvancementData::load, AdvancementData::new, "lootr/Lootr-AdvancementData");
            return data.contains(player, tileId);
        }
    }

    public static void award(UUID player, UUID tileId) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot award advancement.");
        } else {
            AdvancementData data = manager.computeIfAbsent(AdvancementData::load, AdvancementData::new, "lootr/Lootr-AdvancementData");
            data.add(player, tileId);
            data.m_77762_();
        }
    }

    public static boolean isScored(UUID player, UUID tileId) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot determine if block entity has been scored.");
            return false;
        } else {
            AdvancementData data = manager.computeIfAbsent(AdvancementData::load, AdvancementData::new, "lootr/Lootr-ScoreData");
            return data.contains(player, tileId);
        }
    }

    public static void score(UUID player, UUID tileId) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot score block entities at this time.");
        } else {
            AdvancementData data = manager.computeIfAbsent(AdvancementData::load, AdvancementData::new, "lootr/Lootr-ScoreData");
            data.add(player, tileId);
            data.m_77762_();
        }
    }

    public static int getDecayValue(UUID id) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot determine the decay value for " + id.toString() + ".");
            return -1;
        } else {
            TickingData data = manager.computeIfAbsent(TickingData::load, TickingData::new, "lootr/Lootr-DecayData");
            return data.getValue(id);
        }
    }

    public static boolean isDecayed(UUID id) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot determine the decay value for " + id.toString() + ".");
            return false;
        } else {
            TickingData data = manager.computeIfAbsent(TickingData::load, TickingData::new, "lootr/Lootr-DecayData");
            return data.isComplete(id);
        }
    }

    public static void setDecaying(UUID id, int decay) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot set the decay value for " + id.toString() + ".");
        } else {
            TickingData data = manager.computeIfAbsent(TickingData::load, TickingData::new, "lootr/Lootr-DecayData");
            data.setValue(id, decay);
            data.m_77762_();
        }
    }

    public static void removeDecayed(UUID id) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr remove the decay value for " + id.toString() + ".");
        } else {
            TickingData data = manager.computeIfAbsent(TickingData::load, TickingData::new, "lootr/Lootr-DecayData");
            if (data.remove(id) != -1) {
                data.m_77762_();
            }
        }
    }

    public static void doDecay() {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot iterate and tick decay.");
        } else {
            TickingData data = manager.computeIfAbsent(TickingData::load, TickingData::new, "lootr/Lootr-DecayData");
            if (data.tick()) {
                data.m_77762_();
            }
        }
    }

    public static int getRefreshValue(UUID id) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot determine the refresh value for " + id.toString() + ".");
            return -1;
        } else {
            TickingData data = manager.computeIfAbsent(TickingData::load, TickingData::new, "lootr/Lootr-RefreshData");
            return data.getValue(id);
        }
    }

    public static boolean isRefreshed(UUID id) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot determine the refresh value for " + id.toString() + ".");
            return false;
        } else {
            TickingData data = manager.computeIfAbsent(TickingData::load, TickingData::new, "lootr/Lootr-RefreshData");
            return data.isComplete(id);
        }
    }

    public static void setRefreshing(UUID id, int decay) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot set the refresh value for " + id.toString() + ".");
        } else {
            TickingData data = manager.computeIfAbsent(TickingData::load, TickingData::new, "lootr/Lootr-RefreshData");
            data.setValue(id, decay);
            data.m_77762_();
        }
    }

    public static void removeRefreshed(UUID id) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr remove the refresh value for " + id.toString() + ".");
        } else {
            TickingData data = manager.computeIfAbsent(TickingData::load, TickingData::new, "lootr/Lootr-RefreshData");
            if (data.remove(id) != -1) {
                data.m_77762_();
            }
        }
    }

    public static void doRefresh() {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot iterate and tick refresh.");
        } else {
            TickingData data = manager.computeIfAbsent(TickingData::load, TickingData::new, "lootr/Lootr-RefreshData");
            if (data.tick()) {
                data.m_77762_();
            }
        }
    }

    @Deprecated(since = "1.20.0", forRemoval = true)
    public static ChestData getInstanceUuid(ServerLevel world, BlockPos pos, UUID id) {
        return getContainerData(world, pos, id);
    }

    public static ChestData getContainerData(ServerLevel world, BlockPos pos, UUID id) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot fetch chest data for " + world.m_46472_() + " at " + pos.toString() + " with ID " + id.toString() + " and cannot continue.");
            return null;
        } else {
            BlockEntity be = world.m_7702_(pos);
            if (be == null) {
                LootrAPI.LOG.error("The block entity with id '" + id.toString() + "' in '" + world.m_46472_() + "' at '" + pos + "' is null.");
            }
            int size;
            if (be instanceof Container bce) {
                size = bce.getContainerSize();
            } else {
                LootrAPI.LOG.error("We have no heuristic to determine the size of '" + id.toString() + "' in '" + world.m_46472_() + "' at '" + pos + "'. Defaulting to 27.");
                size = 27;
            }
            return ChestData.unwrap(manager.computeIfAbsent(ChestData.loadWrapper(id, world.m_46472_(), pos), ChestData.id(world.m_46472_(), pos, id), ChestData.ID(id)), id, world.m_46472_(), pos, size);
        }
    }

    @Deprecated(since = "1.20.0", forRemoval = true)
    public static ChestData getInstance(ServerLevel world, BlockPos pos, UUID id) {
        return getEntityData(world, pos, id);
    }

    public static ChestData getEntityData(ServerLevel world, BlockPos pos, UUID id) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot fetch chest data for " + world.m_46472_() + " at " + pos.toString() + " with ID " + id.toString() + " and cannot continue.");
            return null;
        } else {
            Entity entity = world.getEntity(id);
            if (entity == null) {
                LootrAPI.LOG.error("The entity with id '" + id + "' in '" + world.m_46472_() + "' at '" + pos + "' is null.");
            }
            int size;
            if (entity instanceof Container container) {
                size = container.getContainerSize();
            } else {
                LootrAPI.LOG.error("We have no heuristic to determine the size of entity '" + id + "' in '" + world.m_46472_() + "' at '" + pos + "'. Defaulting to 27.");
                size = 27;
            }
            return ChestData.unwrap(manager.computeIfAbsent(ChestData.loadWrapper(id, world.m_46472_(), pos), ChestData.entity(world.m_46472_(), pos, id), ChestData.ID(id)), id, world.m_46472_(), pos, size);
        }
    }

    @Deprecated(since = "1.20.0", forRemoval = true)
    public static ChestData getInstanceInventory(ServerLevel world, BlockPos pos, UUID id, NonNullList<ItemStack> base) {
        return getReferenceContainerData(world, pos, id, base);
    }

    public static ChestData getReferenceContainerData(ServerLevel world, BlockPos pos, UUID id, NonNullList<ItemStack> base) {
        DimensionDataStorage manager = getDataStorage();
        if (manager == null) {
            LootrAPI.LOG.error("DataStorage is null at this stage; Lootr cannot fetch chest data for " + world.m_46472_() + " at " + pos.toString() + " with ID " + id.toString() + " and cannot continue.");
            return null;
        } else {
            return ChestData.unwrap(manager.computeIfAbsent(ChestData.loadWrapper(id, world.m_46472_(), pos), ChestData.ref_id(world.m_46472_(), pos, id, base), ChestData.ID(id)), id, world.m_46472_(), pos, base.size());
        }
    }

    @Nullable
    public static SpecialChestInventory getInventory(Level level, UUID uuid, BlockPos pos, ServerPlayer player, IntSupplier sizeSupplier, Supplier<Component> displaySupplier, LootFiller filler, Supplier<ResourceLocation> tableSupplier, LongSupplier seedSupplier) {
        if (!level.isClientSide() && level instanceof ServerLevel) {
            ChestData data = getContainerData((ServerLevel) level, pos, uuid);
            if (data == null) {
                return null;
            } else {
                SpecialChestInventory inventory = data.getInventory(player);
                if (inventory == null) {
                    inventory = data.createInventory(player, filler, sizeSupplier, displaySupplier, tableSupplier, seedSupplier);
                }
                return inventory;
            }
        } else {
            return null;
        }
    }

    @Nullable
    public static SpecialChestInventory getInventory(Level level, UUID uuid, BlockPos pos, ServerPlayer player, BaseContainerBlockEntity blockEntity, LootFiller filler, Supplier<ResourceLocation> tableSupplier, LongSupplier seedSupplier) {
        if (!level.isClientSide() && level instanceof ServerLevel) {
            ChestData data = getContainerData((ServerLevel) level, pos, uuid);
            if (data == null) {
                return null;
            } else {
                SpecialChestInventory inventory = data.getInventory(player);
                if (inventory == null) {
                    inventory = data.createInventory(player, filler, blockEntity, tableSupplier, seedSupplier);
                }
                return inventory;
            }
        } else {
            return null;
        }
    }

    @Nullable
    public static SpecialChestInventory getInventory(Level world, UUID uuid, BlockPos pos, ServerPlayer player, RandomizableContainerBlockEntity tile, LootFiller filler) {
        if (!world.isClientSide && world instanceof ServerLevel) {
            ChestData data = getContainerData((ServerLevel) world, pos, uuid);
            if (data == null) {
                return null;
            } else {
                SpecialChestInventory inventory = data.getInventory(player);
                if (inventory == null) {
                    inventory = data.createInventory(player, filler, tile);
                }
                return inventory;
            }
        } else {
            return null;
        }
    }

    @Nullable
    public static SpecialChestInventory getInventory(Level world, UUID uuid, NonNullList<ItemStack> base, ServerPlayer player, BlockPos pos, RandomizableContainerBlockEntity tile) {
        if (!world.isClientSide && world instanceof ServerLevel) {
            ChestData data = getReferenceContainerData((ServerLevel) world, pos, uuid, base);
            if (data == null) {
                return null;
            } else {
                SpecialChestInventory inventory = data.getInventory(player);
                if (inventory == null) {
                    inventory = data.createInventory(player, data.customInventory(), tile);
                }
                return inventory;
            }
        } else {
            return null;
        }
    }

    public static boolean clearInventories(UUID uuid) {
        DimensionDataStorage data = getDataStorage();
        if (data == null) {
            return false;
        } else {
            ServerLevel world = ServerLifecycleHooks.getCurrentServer().overworld();
            if (world == null) {
                LootrAPI.LOG.error("Overworld is null while attempting to clear inventories for '" + uuid.toString() + "'; Lootr cannot clear inventories.");
                return false;
            } else {
                Path dataPath = world.getServer().getWorldPath(new LevelResource("data")).resolve("lootr");
                List<String> ids = new ArrayList();
                try {
                    Stream<Path> paths = Files.walk(dataPath);
                    try {
                        paths.forEach(o -> {
                            if (Files.isRegularFile(o, new LinkOption[0])) {
                                String fileName = o.getFileName().toString();
                                if (fileName.startsWith("Lootr-")) {
                                    return;
                                }
                                ids.add("lootr/" + fileName.charAt(0) + "/" + fileName.substring(0, 2) + "/" + fileName.replace(".dat", ""));
                            }
                        });
                    } catch (Throwable var10) {
                        if (paths != null) {
                            try {
                                paths.close();
                            } catch (Throwable var9) {
                                var10.addSuppressed(var9);
                            }
                        }
                        throw var10;
                    }
                    if (paths != null) {
                        paths.close();
                    }
                } catch (IOException var11) {
                    return false;
                }
                int cleared = 0;
                for (String id : ids) {
                    ChestData chestData = data.get(ChestData::load, id);
                    if (chestData != null && chestData.clearInventory(uuid)) {
                        cleared++;
                        chestData.m_77762_();
                    }
                }
                LootrAPI.LOG.info("Cleared " + cleared + " inventories for play UUID " + uuid.toString());
                return cleared != 0;
            }
        }
    }

    @Nullable
    public static SpecialChestInventory getInventory(Level world, LootrChestMinecartEntity cart, ServerPlayer player, LootFiller filler) {
        if (!world.isClientSide && world instanceof ServerLevel) {
            ChestData data = getEntityData((ServerLevel) world, cart.m_20183_(), cart.m_20148_());
            if (data == null) {
                return null;
            } else {
                SpecialChestInventory inventory = data.getInventory(player);
                if (inventory == null) {
                    inventory = data.createInventory(player, filler, null);
                }
                return inventory;
            }
        } else {
            return null;
        }
    }

    public static void refreshInventory(Level level, BlockPos pos, UUID uuid, ServerPlayer player) {
        if (!level.isClientSide() && level instanceof ServerLevel) {
            ChestData data = getContainerData((ServerLevel) level, pos, uuid);
            if (data != null) {
                data.clear();
                data.m_77762_();
            }
        }
    }

    public static void refreshInventory(Level world, BlockPos pos, UUID uuid, NonNullList<ItemStack> base, ServerPlayer player) {
        if (!world.isClientSide() && world instanceof ServerLevel) {
            ChestData data = getReferenceContainerData((ServerLevel) world, pos, uuid, base);
            if (data != null) {
                data.clear();
                data.m_77762_();
            }
        }
    }

    public static void refreshInventory(Level world, LootrChestMinecartEntity cart, ServerPlayer player) {
        if (!world.isClientSide() && world instanceof ServerLevel) {
            ChestData data = getEntityData((ServerLevel) world, cart.m_20183_(), cart.m_20148_());
            if (data != null) {
                data.clear();
                data.m_77762_();
            }
        }
    }
}