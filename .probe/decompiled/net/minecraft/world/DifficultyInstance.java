package net.minecraft.world;

import javax.annotation.concurrent.Immutable;
import net.minecraft.util.Mth;

@Immutable
public class DifficultyInstance {

    private static final float DIFFICULTY_TIME_GLOBAL_OFFSET = -72000.0F;

    private static final float MAX_DIFFICULTY_TIME_GLOBAL = 1440000.0F;

    private static final float MAX_DIFFICULTY_TIME_LOCAL = 3600000.0F;

    private final Difficulty base;

    private final float effectiveDifficulty;

    public DifficultyInstance(Difficulty difficulty0, long long1, long long2, float float3) {
        this.base = difficulty0;
        this.effectiveDifficulty = this.calculateDifficulty(difficulty0, long1, long2, float3);
    }

    public Difficulty getDifficulty() {
        return this.base;
    }

    public float getEffectiveDifficulty() {
        return this.effectiveDifficulty;
    }

    public boolean isHard() {
        return this.effectiveDifficulty >= (float) Difficulty.HARD.ordinal();
    }

    public boolean isHarderThan(float float0) {
        return this.effectiveDifficulty > float0;
    }

    public float getSpecialMultiplier() {
        if (this.effectiveDifficulty < 2.0F) {
            return 0.0F;
        } else {
            return this.effectiveDifficulty > 4.0F ? 1.0F : (this.effectiveDifficulty - 2.0F) / 2.0F;
        }
    }

    private float calculateDifficulty(Difficulty difficulty0, long long1, long long2, float float3) {
        if (difficulty0 == Difficulty.PEACEFUL) {
            return 0.0F;
        } else {
            boolean $$4 = difficulty0 == Difficulty.HARD;
            float $$5 = 0.75F;
            float $$6 = Mth.clamp(((float) long1 + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
            $$5 += $$6;
            float $$7 = 0.0F;
            $$7 += Mth.clamp((float) long2 / 3600000.0F, 0.0F, 1.0F) * ($$4 ? 1.0F : 0.75F);
            $$7 += Mth.clamp(float3 * 0.25F, 0.0F, $$6);
            if (difficulty0 == Difficulty.EASY) {
                $$7 *= 0.5F;
            }
            $$5 += $$7;
            return (float) difficulty0.getId() * $$5;
        }
    }
}