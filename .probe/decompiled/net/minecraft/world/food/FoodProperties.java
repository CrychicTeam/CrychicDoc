package net.minecraft.world.food;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;

public class FoodProperties {

    private final int nutrition;

    private final float saturationModifier;

    private final boolean isMeat;

    private final boolean canAlwaysEat;

    private final boolean fastFood;

    private final List<Pair<MobEffectInstance, Float>> effects;

    FoodProperties(int int0, float float1, boolean boolean2, boolean boolean3, boolean boolean4, List<Pair<MobEffectInstance, Float>> listPairMobEffectInstanceFloat5) {
        this.nutrition = int0;
        this.saturationModifier = float1;
        this.isMeat = boolean2;
        this.canAlwaysEat = boolean3;
        this.fastFood = boolean4;
        this.effects = listPairMobEffectInstanceFloat5;
    }

    public int getNutrition() {
        return this.nutrition;
    }

    public float getSaturationModifier() {
        return this.saturationModifier;
    }

    public boolean isMeat() {
        return this.isMeat;
    }

    public boolean canAlwaysEat() {
        return this.canAlwaysEat;
    }

    public boolean isFastFood() {
        return this.fastFood;
    }

    public List<Pair<MobEffectInstance, Float>> getEffects() {
        return this.effects;
    }

    public static class Builder {

        private int nutrition;

        private float saturationModifier;

        private boolean isMeat;

        private boolean canAlwaysEat;

        private boolean fastFood;

        private final List<Pair<MobEffectInstance, Float>> effects = Lists.newArrayList();

        public FoodProperties.Builder nutrition(int int0) {
            this.nutrition = int0;
            return this;
        }

        public FoodProperties.Builder saturationMod(float float0) {
            this.saturationModifier = float0;
            return this;
        }

        public FoodProperties.Builder meat() {
            this.isMeat = true;
            return this;
        }

        public FoodProperties.Builder alwaysEat() {
            this.canAlwaysEat = true;
            return this;
        }

        public FoodProperties.Builder fast() {
            this.fastFood = true;
            return this;
        }

        public FoodProperties.Builder effect(MobEffectInstance mobEffectInstance0, float float1) {
            this.effects.add(Pair.of(mobEffectInstance0, float1));
            return this;
        }

        public FoodProperties build() {
            return new FoodProperties(this.nutrition, this.saturationModifier, this.isMeat, this.canAlwaysEat, this.fastFood, this.effects);
        }
    }
}