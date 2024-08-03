package se.mickelus.tetra.module.improvement;

import com.google.gson.JsonObject;
import java.util.Arrays;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraftforge.fml.ModList;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.data.ImprovementData;

@ParametersAreNonnullByDefault
public class DestabilizationEffect {

    private static DestabilizationEffect[] effects = new DestabilizationEffect[0];

    public String destabilizationKey = "";

    public int minLevel = 0;

    public int maxLevel = 0;

    public String requiredMod;

    public float probability = 0.0F;

    public int instabilityLimit = 1;

    public String improvementKey;

    public static void init() {
        DataManager.instance.destabilizationData.onReload(() -> effects = (DestabilizationEffect[]) DataManager.instance.destabilizationData.getData().values().stream().flatMap(Arrays::stream).filter(effect -> effect.requiredMod == null || ModList.get().isLoaded(effect.requiredMod)).toArray(DestabilizationEffect[]::new));
    }

    public static String[] getKeys() {
        return (String[]) Arrays.stream(effects).map(effect -> effect.destabilizationKey).toArray(String[]::new);
    }

    public static DestabilizationEffect[] getEffectsForImprovement(int instability, ImprovementData[] improvements) {
        return (DestabilizationEffect[]) Arrays.stream(effects).filter(effect -> effect.instabilityLimit <= instability).filter(effect -> effect.improvementKey == null || Arrays.stream(improvements).anyMatch(improvement -> improvement.key.equals(effect.improvementKey))).toArray(DestabilizationEffect[]::new);
    }

    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        result.addProperty("destabilizationKey", this.destabilizationKey);
        if (this.minLevel != 0) {
            result.addProperty("minLevel", this.minLevel);
        }
        if (this.minLevel != 0) {
            result.addProperty("minLevel", this.minLevel);
        }
        if (this.maxLevel != 0) {
            result.addProperty("maxLevel", this.maxLevel);
        }
        if (this.requiredMod != null) {
            result.addProperty("requiredMod", this.requiredMod);
        }
        if (this.probability != 0.0F) {
            result.addProperty("probability", this.probability);
        }
        if (this.instabilityLimit != 1) {
            result.addProperty("instabilityLimit", this.instabilityLimit);
        }
        if (this.improvementKey != null) {
            result.addProperty("improvementKey", this.improvementKey);
        }
        return result;
    }
}