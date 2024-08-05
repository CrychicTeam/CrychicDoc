package dev.xkmc.l2hostility.compat.gateway;

import dev.xkmc.l2hostility.content.config.SpecialConfigCondition;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.resources.ResourceLocation;

@SerialClass
public class GatewayCondition extends SpecialConfigCondition<WaveData> {

    @SerialField
    public int minWave;

    @SerialField
    public int maxWave;

    @SerialField
    public int maxCount;

    @SerialField
    public double chance;

    public static GatewayCondition of(ResourceLocation id, int minWave, int count, double chance) {
        return of(id, minWave, -1, count, chance);
    }

    public static GatewayCondition of(ResourceLocation id, int minWave, int maxWave, int count, double chance) {
        GatewayCondition ans = new GatewayCondition();
        ans.id = id;
        ans.minWave = minWave;
        ans.maxWave = maxWave;
        ans.maxCount = count;
        ans.chance = chance;
        return ans;
    }

    public GatewayCondition() {
        super(WaveData.class);
    }

    public boolean test(WaveData wave) {
        int index = wave.id.wave();
        if (index >= this.minWave && (this.maxWave < 0 || index <= this.maxWave)) {
            int val = (Integer) wave.appliedCount.getOrDefault(this, 0);
            if (val >= this.maxCount) {
                return false;
            } else {
                wave.appliedCount.put(this, val + 1);
                return true;
            }
        } else {
            return false;
        }
    }
}