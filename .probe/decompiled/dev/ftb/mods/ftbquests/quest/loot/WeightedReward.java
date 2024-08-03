package dev.ftb.mods.ftbquests.quest.loot;

import dev.ftb.mods.ftbquests.quest.reward.Reward;

public class WeightedReward implements Comparable<WeightedReward> {

    private final Reward reward;

    private float weight;

    public WeightedReward(Reward reward, float weight) {
        this.reward = reward;
        this.weight = Math.max(weight, 0.0F);
    }

    public Reward getReward() {
        return this.reward;
    }

    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public static String chanceString(float weight, float totalWeight, boolean empty) {
        if (totalWeight <= 0.0F) {
            return "??%";
        } else if (weight <= 0.0F) {
            return empty ? "0%" : "100%";
        } else if (weight >= totalWeight) {
            return "100%";
        } else {
            int chance = (int) (weight * 100.0F / totalWeight);
            float chanced = weight * 100.0F / totalWeight;
            if ((float) chance != chanced) {
                return chanced < 0.01F ? "<0.01%" : String.format("%.2f%%", chanced);
            } else {
                return chance + "%";
            }
        }
    }

    public static String chanceString(float weight, float totalWeight) {
        return chanceString(weight, totalWeight, false);
    }

    public int compareTo(WeightedReward o) {
        return Float.compare(this.weight, o.weight);
    }
}