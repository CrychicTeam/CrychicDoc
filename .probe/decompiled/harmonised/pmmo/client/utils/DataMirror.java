package harmonised.pmmo.client.utils;

import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.config.codecs.SkillData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.IDataStorage;
import harmonised.pmmo.util.MsLoggy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.LogicalSide;

public class DataMirror implements IDataStorage {

    private Map<String, Long> mySkills = new HashMap();

    private Map<String, Long> otherSkills = new HashMap();

    private Map<String, Long> scheduledXp = new HashMap();

    private List<Long> levelCache = new ArrayList();

    public boolean me(UUID id) {
        return id == null || id.equals(Minecraft.getInstance().player.m_20148_());
    }

    public void setLevelCache(List<Long> cache) {
        this.levelCache = cache;
    }

    public long getScheduledXp(String skill) {
        return (Long) this.scheduledXp.getOrDefault(skill, 0L);
    }

    @Override
    public int getLevelFromXP(long xp) {
        for (int i = 0; i < this.levelCache.size(); i++) {
            if (i == Config.MAX_LEVEL.get()) {
                return i;
            }
            if ((Long) this.levelCache.get(i) > xp) {
                return Core.get(LogicalSide.CLIENT).getLevelProvider().process("", i);
            }
        }
        return Config.MAX_LEVEL.get();
    }

    private int getLevelFromXPwithoutLevelProvider(long xp) {
        for (int i = 0; i < this.levelCache.size(); i++) {
            if ((Long) this.levelCache.get(i) > xp) {
                return i;
            }
        }
        return Config.MAX_LEVEL.get();
    }

    public double getXpWithPercentToNextLevel(long rawXP) {
        int currentLevel = this.getLevelFromXPwithoutLevelProvider(rawXP);
        currentLevel = currentLevel >= this.levelCache.size() ? this.levelCache.size() - 1 : currentLevel;
        long currentXPThreshold = currentLevel - 1 >= 0 ? (Long) this.levelCache.get(currentLevel - 1) : 0L;
        long xpToNextLevel = (Long) this.levelCache.get(currentLevel) - currentXPThreshold;
        long progress = rawXP - currentXPThreshold;
        return (double) Core.get(LogicalSide.CLIENT).getLevelProvider().process("", currentLevel) + (double) progress / (double) xpToNextLevel;
    }

    @Override
    public long getXpRaw(UUID playerID, String skillName) {
        return this.me(playerID) ? (Long) this.mySkills.getOrDefault(skillName, 0L) : (Long) this.otherSkills.getOrDefault(skillName, 0L);
    }

    @Override
    public void setXpRaw(UUID playerID, String skillName, long value) {
        if (this.me(playerID)) {
            long oldValue = this.getXpRaw(playerID, skillName);
            if (value > oldValue) {
                this.scheduledXp.merge(skillName, value - oldValue, Long::sum);
            }
            this.mySkills.put(skillName, value);
            int newLevel = this.getLevelFromXP(value);
            int oldLevel = this.getLevelFromXP(oldValue);
            if (oldLevel < newLevel) {
                ClientUtils.sendLevelUpUnlocks(skillName, newLevel);
            }
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.XP, "Client Side Skill Map: " + MsLoggy.mapToString(this.mySkills));
        }
    }

    @Override
    public Map<String, Long> getXpMap(UUID playerID) {
        return this.me(playerID) ? this.mySkills : this.otherSkills;
    }

    @Override
    public void setXpMap(UUID playerID, Map<String, Long> map) {
        if (this.me(playerID)) {
            this.mySkills = map;
        } else {
            this.otherSkills = map;
        }
    }

    @Override
    public int getPlayerSkillLevel(String skill, UUID player) {
        int rawLevel = this.me(player) ? this.getLevelFromXP((Long) this.mySkills.getOrDefault(skill, 0L)) : this.getLevelFromXP((Long) this.otherSkills.getOrDefault(skill, 0L));
        rawLevel = Core.get(LogicalSide.CLIENT).getLevelProvider().process(skill, rawLevel);
        int skillMax = ((SkillData) SkillsConfig.SKILLS.get().getOrDefault(skill, SkillData.Builder.getDefault())).getMaxLevel();
        return Math.min(rawLevel, skillMax);
    }

    @Override
    public IDataStorage get() {
        return this;
    }

    @Override
    public long getBaseXpForLevel(int level) {
        return level > 0 && level - 1 < this.levelCache.size() ? (Long) this.levelCache.get(level - 1) : 0L;
    }
}