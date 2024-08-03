package com.mna.api.faction;

import com.mna.api.entities.IFactionEnemy;
import java.util.HashMap;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class FactionDifficultyStats {

    private int haste_buff = -1;

    private int factionMobDespawnCount = 0;

    private HashMap<String, Integer> factionMobKilledByStats = new HashMap();

    private HashMap<String, Integer> factionMobResistanceBuffs = new HashMap();

    public void onFactionMobKilled(DamageSource source) {
        String msgId = source.getMsgId();
        if (msgId != null) {
            int newValue = (Integer) this.factionMobKilledByStats.getOrDefault(msgId, 0) + 1;
            if (newValue > 15) {
                newValue -= 15;
                int currentResistance = (Integer) this.factionMobResistanceBuffs.getOrDefault(msgId, -1);
                int newResistance = Mth.clamp(currentResistance + 1, 0, 4);
                this.factionMobResistanceBuffs.put(msgId, newResistance);
            }
            this.factionMobKilledByStats.put(msgId, newValue);
        }
    }

    public void onFactionMobDespawnDueToDistance() {
        this.factionMobDespawnCount++;
        if (this.factionMobDespawnCount > 15) {
            this.factionMobDespawnCount -= 15;
            this.haste_buff = Math.min(this.haste_buff + 1, 5);
        }
    }

    public void onFactionKilledPlayer() {
        for (String key : this.factionMobKilledByStats.keySet()) {
            this.factionMobKilledByStats.put(key, Math.max((Integer) this.factionMobKilledByStats.get(key) - 1, 0));
        }
        this.factionMobDespawnCount--;
    }

    public void adjustFactionEnemy(IFactionEnemy<?> factionMob) {
        if (this.haste_buff > -1) {
            ((LivingEntity) factionMob).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, this.haste_buff));
        }
        for (Entry<String, Integer> source : this.factionMobResistanceBuffs.entrySet()) {
            factionMob.setDamageResists((String) source.getKey(), (Integer) source.getValue());
        }
    }

    public CompoundTag writeToNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("haste_buff", this.haste_buff);
        nbt.putInt("factionMobDespawnCount", this.factionMobDespawnCount);
        ListTag list_kills = new ListTag();
        for (Entry<String, Integer> e : this.factionMobKilledByStats.entrySet()) {
            CompoundTag list_item = new CompoundTag();
            list_item.putString("key", (String) e.getKey());
            list_item.putInt("value", (Integer) e.getValue());
            list_kills.add(list_item);
        }
        nbt.put("kill_counts", list_kills);
        ListTag list_resist_buffs = new ListTag();
        for (Entry<String, Integer> e : this.factionMobResistanceBuffs.entrySet()) {
            CompoundTag list_item = new CompoundTag();
            list_item.putString("key", (String) e.getKey());
            list_item.putInt("value", (Integer) e.getValue());
            list_resist_buffs.add(list_item);
        }
        nbt.put("resist_buffs", list_resist_buffs);
        return nbt;
    }

    public void readFromNBT(CompoundTag nbt) {
        this.haste_buff = nbt.getInt("haste_buff");
        this.factionMobDespawnCount = nbt.getInt("factionMobDespawnCount");
        nbt.getList("kill_counts", 10).forEach(l -> this.factionMobKilledByStats.put(((CompoundTag) l).getString("key"), ((CompoundTag) l).getInt("value")));
        nbt.getList("resist_buffs", 10).forEach(l -> this.factionMobResistanceBuffs.put(((CompoundTag) l).getString("key"), ((CompoundTag) l).getInt("value")));
    }
}