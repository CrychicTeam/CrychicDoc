package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.world.gen.TypedFeature;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

public class IafWorldData extends SavedData {

    private static final String IDENTIFIER = "iceandfire_general";

    private static final Map<IafWorldData.FeatureType, List<Pair<String, BlockPos>>> LAST_GENERATED = new HashMap();

    public IafWorldData() {
    }

    public IafWorldData(CompoundTag tag) {
        this.load(tag);
    }

    public static IafWorldData get(Level world) {
        if (world instanceof ServerLevel) {
            ServerLevel overworld = world.getServer().getLevel(world.dimension());
            DimensionDataStorage storage = overworld.getDataStorage();
            IafWorldData data = storage.computeIfAbsent(IafWorldData::new, IafWorldData::new, "iceandfire_general");
            data.m_77762_();
            return data;
        } else {
            return null;
        }
    }

    public boolean check(TypedFeature feature, BlockPos position, String id) {
        return this.check(feature.getFeatureType(), position, id);
    }

    public boolean check(IafWorldData.FeatureType type, BlockPos position, String id) {
        List<Pair<String, BlockPos>> entries = (List<Pair<String, BlockPos>>) LAST_GENERATED.computeIfAbsent(type, key -> new ArrayList());
        boolean canGenerate = true;
        Pair<String, BlockPos> toRemove = null;
        for (Pair<String, BlockPos> entry : entries) {
            if (((String) entry.getFirst()).equals(id)) {
                toRemove = entry;
            }
            canGenerate = position.m_123331_((Vec3i) entry.getSecond()) > IafConfig.dangerousWorldGenSeparationLimit * IafConfig.dangerousWorldGenSeparationLimit;
        }
        if (toRemove != null) {
            entries.remove(toRemove);
        }
        entries.add(Pair.of(id, position));
        return canGenerate;
    }

    public IafWorldData load(CompoundTag tag) {
        IafWorldData.FeatureType[] types = IafWorldData.FeatureType.values();
        for (IafWorldData.FeatureType type : types) {
            ListTag list = tag.getList(type.toString(), 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entry = list.getCompound(i);
                String id = entry.getString("id");
                BlockPos position = NbtUtils.readBlockPos(entry.getCompound("position"));
                ((List) LAST_GENERATED.computeIfAbsent(type, key -> new ArrayList())).add(Pair.of(id, position));
            }
        }
        return this;
    }

    @NotNull
    @Override
    public CompoundTag save(@NotNull CompoundTag tag) {
        LAST_GENERATED.forEach((key, value) -> {
            ListTag listTag = new ListTag();
            value.forEach(entry -> {
                CompoundTag subTag = new CompoundTag();
                subTag.putString("id", (String) entry.getFirst());
                subTag.put("position", NbtUtils.writeBlockPos((BlockPos) entry.getSecond()));
                listTag.add(subTag);
            });
            tag.put(key.toString(), listTag);
        });
        return tag;
    }

    public static enum FeatureType {

        SURFACE, UNDERGROUND, OCEAN
    }
}