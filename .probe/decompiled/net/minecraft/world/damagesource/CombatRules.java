package net.minecraft.world.damagesource;

import net.minecraft.util.Mth;

public class CombatRules {

    public static final float MAX_ARMOR = 20.0F;

    public static final float ARMOR_PROTECTION_DIVIDER = 25.0F;

    public static final float BASE_ARMOR_TOUGHNESS = 2.0F;

    public static final float MIN_ARMOR_RATIO = 0.2F;

    private static final int NUM_ARMOR_ITEMS = 4;

    public static float getDamageAfterAbsorb(float float0, float float1, float float2) {
        float $$3 = 2.0F + float2 / 4.0F;
        float $$4 = Mth.clamp(float1 - float0 / $$3, float1 * 0.2F, 20.0F);
        return float0 * (1.0F - $$4 / 25.0F);
    }

    public static float getDamageAfterMagicAbsorb(float float0, float float1) {
        float $$2 = Mth.clamp(float1, 0.0F, 20.0F);
        return float0 * (1.0F - $$2 / 25.0F);
    }
}