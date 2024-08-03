package net.minecraft.client.model;

public class ModelUtils {

    public static float rotlerpRad(float float0, float float1, float float2) {
        float $$3 = float1 - float0;
        while ($$3 < (float) -Math.PI) {
            $$3 += (float) (Math.PI * 2);
        }
        while ($$3 >= (float) Math.PI) {
            $$3 -= (float) (Math.PI * 2);
        }
        return float0 + float2 * $$3;
    }
}