package com.github.alexmodguy.alexscaves.server.level.storage;

import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.github.alexmodguy.alexscaves.server.level.map.CaveBiomeMapWorldWorker;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.common.WorldWorkerManager;

public class ACWorldData extends SavedData {

    private static final String IDENTIFIER = "alexscaves_world_data";

    private Map<UUID, Integer> deepOneReputations = new HashMap();

    private boolean primordialBossDefeatedOnce = false;

    private long firstPrimordialBossDefeatTimestamp = -1L;

    private Set<Integer> trackedLuxtructosaurusIds = new ObjectArraySet();

    private CaveBiomeMapWorldWorker lastMapWorker = null;

    private ACWorldData() {
    }

    public static ACWorldData get(Level world) {
        if (world instanceof ServerLevel) {
            ServerLevel overworld = world.getServer().getLevel(Level.OVERWORLD);
            DimensionDataStorage storage = overworld.getDataStorage();
            ACWorldData data = storage.computeIfAbsent(ACWorldData::load, ACWorldData::new, "alexscaves_world_data");
            if (data != null) {
                data.m_77762_();
            }
            return data;
        } else {
            return null;
        }
    }

    public static ACWorldData load(CompoundTag nbt) {
        ACWorldData data = new ACWorldData();
        if (nbt.contains("DeepOneReputations")) {
            ListTag listtag = nbt.getList("DeepOneReputations", 10);
            for (int i = 0; i < listtag.size(); i++) {
                CompoundTag innerTag = listtag.getCompound(i);
                data.deepOneReputations.put(innerTag.getUUID("UUID"), innerTag.getInt("Reputation"));
            }
        }
        data.primordialBossDefeatedOnce = nbt.getBoolean("PrimordialBossDefeatedOnce");
        data.firstPrimordialBossDefeatTimestamp = nbt.getLong("FirstPrimordialBossDefeatTimestamp");
        data.trackedLuxtructosaurusIds = (Set<Integer>) Arrays.stream(nbt.getIntArray("TrackedLuxtructosaurusIds")).boxed().collect(Collectors.toSet());
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        if (!this.deepOneReputations.isEmpty()) {
            ListTag listTag = new ListTag();
            for (Entry<UUID, Integer> reputations : this.deepOneReputations.entrySet()) {
                CompoundTag tag = new CompoundTag();
                tag.putUUID("UUID", (UUID) reputations.getKey());
                tag.putInt("Reputation", (Integer) reputations.getValue());
                listTag.add(tag);
            }
            compound.put("DeepOneReputations", listTag);
        }
        compound.putBoolean("PrimordialBossDefeatedOnce", this.primordialBossDefeatedOnce);
        compound.putLong("FirstPrimordialBossDefeatTimestamp", this.firstPrimordialBossDefeatTimestamp);
        compound.putIntArray("TrackedLuxtructosaurusIds", this.trackedLuxtructosaurusIds.stream().mapToInt(Integer::intValue).toArray());
        return compound;
    }

    public int getDeepOneReputation(@Nullable UUID uuid) {
        return uuid == null ? 0 : (Integer) this.deepOneReputations.getOrDefault(uuid, 0);
    }

    public void setDeepOneReputation(UUID uuid, int reputation) {
        this.deepOneReputations.put(uuid, Mth.clamp(reputation, -100, 100));
    }

    public boolean isPrimordialBossActive(Level level) {
        for (int i : this.trackedLuxtructosaurusIds) {
            if (level.getEntity(i) instanceof LuxtructosaurusEntity lux && lux.m_6084_() && lux.isLoadedInWorld()) {
                return true;
            }
        }
        return false;
    }

    public void trackPrimordialBoss(int id, boolean add) {
        if (add) {
            this.trackedLuxtructosaurusIds.add(id);
        } else {
            this.trackedLuxtructosaurusIds.remove(id);
        }
    }

    public boolean isPrimordialBossDefeatedOnce() {
        return this.primordialBossDefeatedOnce;
    }

    public void setPrimordialBossDefeatedOnce(boolean defeatedOnce) {
        this.primordialBossDefeatedOnce = defeatedOnce;
    }

    public long getFirstPrimordialBossDefeatTimestamp() {
        return this.firstPrimordialBossDefeatTimestamp;
    }

    public void setFirstPrimordialBossDefeatTimestamp(long time) {
        this.firstPrimordialBossDefeatTimestamp = time;
    }

    public void fillOutCaveMap(UUID uuid, ItemStack map, ServerLevel serverLevel, BlockPos center, Player player) {
        if (this.lastMapWorker != null) {
            this.lastMapWorker.onWorkComplete(this.lastMapWorker.getLastFoundBiome());
        }
        this.lastMapWorker = new CaveBiomeMapWorldWorker(map, serverLevel, center, player, uuid);
        WorldWorkerManager.addWorker(this.lastMapWorker);
    }

    public boolean isCaveMapTicking() {
        return this.lastMapWorker != null && this.lastMapWorker.hasWork();
    }
}