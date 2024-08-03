package net.minecraft.client.renderer;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

public abstract class DimensionSpecialEffects {

    private static final Object2ObjectMap<ResourceLocation, DimensionSpecialEffects> EFFECTS = Util.make(new Object2ObjectArrayMap(), p_108881_ -> {
        DimensionSpecialEffects.OverworldEffects $$1 = new DimensionSpecialEffects.OverworldEffects();
        p_108881_.defaultReturnValue($$1);
        p_108881_.put(BuiltinDimensionTypes.OVERWORLD_EFFECTS, $$1);
        p_108881_.put(BuiltinDimensionTypes.NETHER_EFFECTS, new DimensionSpecialEffects.NetherEffects());
        p_108881_.put(BuiltinDimensionTypes.END_EFFECTS, new DimensionSpecialEffects.EndEffects());
    });

    private final float[] sunriseCol = new float[4];

    private final float cloudLevel;

    private final boolean hasGround;

    private final DimensionSpecialEffects.SkyType skyType;

    private final boolean forceBrightLightmap;

    private final boolean constantAmbientLight;

    public DimensionSpecialEffects(float float0, boolean boolean1, DimensionSpecialEffects.SkyType dimensionSpecialEffectsSkyType2, boolean boolean3, boolean boolean4) {
        this.cloudLevel = float0;
        this.hasGround = boolean1;
        this.skyType = dimensionSpecialEffectsSkyType2;
        this.forceBrightLightmap = boolean3;
        this.constantAmbientLight = boolean4;
    }

    public static DimensionSpecialEffects forType(DimensionType dimensionType0) {
        return (DimensionSpecialEffects) EFFECTS.get(dimensionType0.effectsLocation());
    }

    @Nullable
    public float[] getSunriseColor(float float0, float float1) {
        float $$2 = 0.4F;
        float $$3 = Mth.cos(float0 * (float) (Math.PI * 2)) - 0.0F;
        float $$4 = -0.0F;
        if ($$3 >= -0.4F && $$3 <= 0.4F) {
            float $$5 = ($$3 - -0.0F) / 0.4F * 0.5F + 0.5F;
            float $$6 = 1.0F - (1.0F - Mth.sin($$5 * (float) Math.PI)) * 0.99F;
            $$6 *= $$6;
            this.sunriseCol[0] = $$5 * 0.3F + 0.7F;
            this.sunriseCol[1] = $$5 * $$5 * 0.7F + 0.2F;
            this.sunriseCol[2] = $$5 * $$5 * 0.0F + 0.2F;
            this.sunriseCol[3] = $$6;
            return this.sunriseCol;
        } else {
            return null;
        }
    }

    public float getCloudHeight() {
        return this.cloudLevel;
    }

    public boolean hasGround() {
        return this.hasGround;
    }

    public abstract Vec3 getBrightnessDependentFogColor(Vec3 var1, float var2);

    public abstract boolean isFoggyAt(int var1, int var2);

    public DimensionSpecialEffects.SkyType skyType() {
        return this.skyType;
    }

    public boolean forceBrightLightmap() {
        return this.forceBrightLightmap;
    }

    public boolean constantAmbientLight() {
        return this.constantAmbientLight;
    }

    public static class EndEffects extends DimensionSpecialEffects {

        public EndEffects() {
            super(Float.NaN, false, DimensionSpecialEffects.SkyType.END, true, false);
        }

        @Override
        public Vec3 getBrightnessDependentFogColor(Vec3 vec0, float float1) {
            return vec0.scale(0.15F);
        }

        @Override
        public boolean isFoggyAt(int int0, int int1) {
            return false;
        }

        @Nullable
        @Override
        public float[] getSunriseColor(float float0, float float1) {
            return null;
        }
    }

    public static class NetherEffects extends DimensionSpecialEffects {

        public NetherEffects() {
            super(Float.NaN, true, DimensionSpecialEffects.SkyType.NONE, false, true);
        }

        @Override
        public Vec3 getBrightnessDependentFogColor(Vec3 vec0, float float1) {
            return vec0;
        }

        @Override
        public boolean isFoggyAt(int int0, int int1) {
            return true;
        }
    }

    public static class OverworldEffects extends DimensionSpecialEffects {

        public static final int CLOUD_LEVEL = 192;

        public OverworldEffects() {
            super(192.0F, true, DimensionSpecialEffects.SkyType.NORMAL, false, false);
        }

        @Override
        public Vec3 getBrightnessDependentFogColor(Vec3 vec0, float float1) {
            return vec0.multiply((double) (float1 * 0.94F + 0.06F), (double) (float1 * 0.94F + 0.06F), (double) (float1 * 0.91F + 0.09F));
        }

        @Override
        public boolean isFoggyAt(int int0, int int1) {
            return false;
        }
    }

    public static enum SkyType {

        NONE, NORMAL, END
    }
}