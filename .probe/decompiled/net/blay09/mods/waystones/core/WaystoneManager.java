package net.blay09.mods.waystones.core;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public class WaystoneManager extends SavedData {

    private static final String DATA_NAME = "waystones";

    private static final String TAG_WAYSTONES = "Waystones";

    private static final WaystoneManager clientStorageCopy = new WaystoneManager();

    private final Map<UUID, IWaystone> waystones = new HashMap();

    public void addWaystone(IWaystone waystone) {
        this.waystones.put(waystone.getWaystoneUid(), waystone);
        this.m_77762_();
    }

    public void updateWaystone(IWaystone waystone) {
        Waystone mutableWaystone = (Waystone) this.waystones.getOrDefault(waystone.getWaystoneUid(), waystone);
        mutableWaystone.setName(waystone.getName());
        mutableWaystone.setGlobal(waystone.isGlobal());
        this.waystones.put(waystone.getWaystoneUid(), mutableWaystone);
        this.m_77762_();
    }

    public void removeWaystone(IWaystone waystone) {
        this.waystones.remove(waystone.getWaystoneUid());
        this.m_77762_();
    }

    public Optional<IWaystone> getWaystoneAt(BlockGetter world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof WaystoneBlockEntityBase ? Optional.of(((WaystoneBlockEntityBase) blockEntity).getWaystone()) : Optional.empty();
    }

    public Optional<IWaystone> getWaystoneById(UUID waystoneUid) {
        return Optional.ofNullable((IWaystone) this.waystones.get(waystoneUid));
    }

    public Optional<IWaystone> findWaystoneByName(String name) {
        return this.waystones.values().stream().filter(it -> it.getName().equals(name)).findFirst();
    }

    public Stream<IWaystone> getWaystonesByType(ResourceLocation type) {
        return this.waystones.values().stream().filter(it -> it.getWaystoneType().equals(type)).sorted(Comparator.comparing(IWaystone::getName));
    }

    public List<IWaystone> getGlobalWaystones() {
        return (List<IWaystone>) this.waystones.values().stream().filter(IWaystone::isGlobal).collect(Collectors.toList());
    }

    public static WaystoneManager read(CompoundTag tagCompound) {
        WaystoneManager waystoneManager = new WaystoneManager();
        for (Tag tag : tagCompound.getList("Waystones", 10)) {
            CompoundTag compound = (CompoundTag) tag;
            IWaystone waystone = Waystone.read(compound);
            waystoneManager.waystones.put(waystone.getWaystoneUid(), waystone);
        }
        return waystoneManager;
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound) {
        ListTag tagList = new ListTag();
        for (IWaystone waystone : this.waystones.values()) {
            tagList.add(Waystone.write(waystone, new CompoundTag()));
        }
        tagCompound.put("Waystones", tagList);
        return tagCompound;
    }

    public static WaystoneManager get(@Nullable MinecraftServer server) {
        if (server != null) {
            ServerLevel overworld = server.getLevel(Level.OVERWORLD);
            return ((ServerLevel) Objects.requireNonNull(overworld)).getDataStorage().computeIfAbsent(WaystoneManager::read, WaystoneManager::new, "waystones");
        } else {
            return clientStorageCopy;
        }
    }
}