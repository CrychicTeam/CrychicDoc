package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class DifficultyLevel {

    @SerialField
    public int level;

    protected long experience;

    @SerialField
    public int extraLevel;

    public static DifficultyLevel merge(DifficultyLevel difficulty, int extraLevel) {
        DifficultyLevel ans = new DifficultyLevel();
        ans.level = difficulty.level;
        ans.experience = difficulty.experience;
        ans.extraLevel = difficulty.extraLevel + extraLevel;
        return ans;
    }

    public static int ofAny(LivingEntity entity) {
        if (entity instanceof Player player) {
            return PlayerDifficulty.HOLDER.get(player).getLevel().getLevel();
        } else {
            return MobTraitCap.HOLDER.isProper(entity) ? ((MobTraitCap) MobTraitCap.HOLDER.get(entity)).getLevel() : 0;
        }
    }

    public void grow(double growFactor, MobTraitCap cap) {
        if (this.level >= LHConfig.COMMON.maxPlayerLevel.get()) {
            this.level = LHConfig.COMMON.maxPlayerLevel.get();
            this.experience = 0L;
        } else {
            this.experience = this.experience + (long) ((int) (growFactor * (double) cap.getLevel() * (double) cap.getLevel()));
            for (int factor = LHConfig.COMMON.killsPerLevel.get(); this.experience >= (long) this.level * (long) this.level * (long) factor; this.level++) {
                this.experience = this.experience - (long) this.level * (long) this.level * (long) factor;
            }
            if (this.level >= LHConfig.COMMON.maxPlayerLevel.get()) {
                this.level = LHConfig.COMMON.maxPlayerLevel.get();
                this.experience = 0L;
            }
        }
    }

    public void decay() {
        double rate = LHConfig.COMMON.playerDeathDecay.get();
        if (rate < 1.0) {
            this.level = Math.max(0, this.level - Math.max(1, (int) Math.ceil((double) this.level * (1.0 - rate))));
        }
        this.experience = 0L;
    }

    public long getMaxExp() {
        int factor = LHConfig.COMMON.killsPerLevel.get();
        return Math.max(1L, (long) this.level * (long) this.level * (long) factor);
    }

    public int getLevel() {
        return Math.max(0, this.level + this.extraLevel);
    }

    public long getExp() {
        return this.experience;
    }

    public String getStr() {
        return this.extraLevel == 0 ? this.level + "" : (this.extraLevel > 0 ? this.level + "+" + this.extraLevel : "" + this.level + this.extraLevel);
    }
}