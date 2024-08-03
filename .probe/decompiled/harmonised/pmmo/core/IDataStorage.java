package harmonised.pmmo.core;

import java.util.Map;
import java.util.UUID;

public interface IDataStorage {

    long getXpRaw(UUID var1, String var2);

    default boolean setXpDiff(UUID playerID, String skillName, long change) {
        return false;
    }

    void setXpRaw(UUID var1, String var2, long var3);

    Map<String, Long> getXpMap(UUID var1);

    void setXpMap(UUID var1, Map<String, Long> var2);

    int getPlayerSkillLevel(String var1, UUID var2);

    default void setPlayerSkillLevel(String skill, UUID player, int level) {
    }

    default boolean changePlayerSkillLevel(String skill, UUID playerID, int change) {
        return false;
    }

    int getLevelFromXP(long var1);

    IDataStorage get();

    default void computeLevelsForCache() {
    }

    long getBaseXpForLevel(int var1);
}