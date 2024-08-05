package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class FogRenderer {

    private static final int WATER_FOG_DISTANCE = 96;

    private static final List<FogRenderer.MobEffectFogFunction> MOB_EFFECT_FOG = Lists.newArrayList(new FogRenderer.MobEffectFogFunction[] { new FogRenderer.BlindnessFogFunction(), new FogRenderer.DarknessFogFunction() });

    public static final float BIOME_FOG_TRANSITION_TIME = 5000.0F;

    private static float fogRed;

    private static float fogGreen;

    private static float fogBlue;

    private static int targetBiomeFog = -1;

    private static int previousBiomeFog = -1;

    private static long biomeChangedTime = -1L;

    public static void setupColor(Camera camera0, float float1, ClientLevel clientLevel2, int int3, float float4) {
        FogType $$5 = camera0.getFluidInCamera();
        Entity $$6 = camera0.getEntity();
        if ($$5 == FogType.WATER) {
            long $$7 = Util.getMillis();
            int $$8 = ((Biome) clientLevel2.m_204166_(BlockPos.containing(camera0.getPosition())).value()).getWaterFogColor();
            if (biomeChangedTime < 0L) {
                targetBiomeFog = $$8;
                previousBiomeFog = $$8;
                biomeChangedTime = $$7;
            }
            int $$9 = targetBiomeFog >> 16 & 0xFF;
            int $$10 = targetBiomeFog >> 8 & 0xFF;
            int $$11 = targetBiomeFog & 0xFF;
            int $$12 = previousBiomeFog >> 16 & 0xFF;
            int $$13 = previousBiomeFog >> 8 & 0xFF;
            int $$14 = previousBiomeFog & 0xFF;
            float $$15 = Mth.clamp((float) ($$7 - biomeChangedTime) / 5000.0F, 0.0F, 1.0F);
            float $$16 = Mth.lerp($$15, (float) $$12, (float) $$9);
            float $$17 = Mth.lerp($$15, (float) $$13, (float) $$10);
            float $$18 = Mth.lerp($$15, (float) $$14, (float) $$11);
            fogRed = $$16 / 255.0F;
            fogGreen = $$17 / 255.0F;
            fogBlue = $$18 / 255.0F;
            if (targetBiomeFog != $$8) {
                targetBiomeFog = $$8;
                previousBiomeFog = Mth.floor($$16) << 16 | Mth.floor($$17) << 8 | Mth.floor($$18);
                biomeChangedTime = $$7;
            }
        } else if ($$5 == FogType.LAVA) {
            fogRed = 0.6F;
            fogGreen = 0.1F;
            fogBlue = 0.0F;
            biomeChangedTime = -1L;
        } else if ($$5 == FogType.POWDER_SNOW) {
            fogRed = 0.623F;
            fogGreen = 0.734F;
            fogBlue = 0.785F;
            biomeChangedTime = -1L;
            RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);
        } else {
            float $$19 = 0.25F + 0.75F * (float) int3 / 32.0F;
            $$19 = 1.0F - (float) Math.pow((double) $$19, 0.25);
            Vec3 $$20 = clientLevel2.getSkyColor(camera0.getPosition(), float1);
            float $$21 = (float) $$20.x;
            float $$22 = (float) $$20.y;
            float $$23 = (float) $$20.z;
            float $$24 = Mth.clamp(Mth.cos(clientLevel2.m_46942_(float1) * (float) (Math.PI * 2)) * 2.0F + 0.5F, 0.0F, 1.0F);
            BiomeManager $$25 = clientLevel2.m_7062_();
            Vec3 $$26 = camera0.getPosition().subtract(2.0, 2.0, 2.0).scale(0.25);
            Vec3 $$27 = CubicSampler.gaussianSampleVec3($$26, (p_109033_, p_109034_, p_109035_) -> clientLevel2.effects().getBrightnessDependentFogColor(Vec3.fromRGB24($$25.getNoiseBiomeAtQuart(p_109033_, p_109034_, p_109035_).value().getFogColor()), $$24));
            fogRed = (float) $$27.x();
            fogGreen = (float) $$27.y();
            fogBlue = (float) $$27.z();
            if (int3 >= 4) {
                float $$28 = Mth.sin(clientLevel2.m_46490_(float1)) > 0.0F ? -1.0F : 1.0F;
                Vector3f $$29 = new Vector3f($$28, 0.0F, 0.0F);
                float $$30 = camera0.getLookVector().dot($$29);
                if ($$30 < 0.0F) {
                    $$30 = 0.0F;
                }
                if ($$30 > 0.0F) {
                    float[] $$31 = clientLevel2.effects().getSunriseColor(clientLevel2.m_46942_(float1), float1);
                    if ($$31 != null) {
                        $$30 *= $$31[3];
                        fogRed = fogRed * (1.0F - $$30) + $$31[0] * $$30;
                        fogGreen = fogGreen * (1.0F - $$30) + $$31[1] * $$30;
                        fogBlue = fogBlue * (1.0F - $$30) + $$31[2] * $$30;
                    }
                }
            }
            fogRed = fogRed + ($$21 - fogRed) * $$19;
            fogGreen = fogGreen + ($$22 - fogGreen) * $$19;
            fogBlue = fogBlue + ($$23 - fogBlue) * $$19;
            float $$32 = clientLevel2.m_46722_(float1);
            if ($$32 > 0.0F) {
                float $$33 = 1.0F - $$32 * 0.5F;
                float $$34 = 1.0F - $$32 * 0.4F;
                fogRed *= $$33;
                fogGreen *= $$33;
                fogBlue *= $$34;
            }
            float $$35 = clientLevel2.m_46661_(float1);
            if ($$35 > 0.0F) {
                float $$36 = 1.0F - $$35 * 0.5F;
                fogRed *= $$36;
                fogGreen *= $$36;
                fogBlue *= $$36;
            }
            biomeChangedTime = -1L;
        }
        float $$37 = ((float) camera0.getPosition().y - (float) clientLevel2.m_141937_()) * clientLevel2.getLevelData().getClearColorScale();
        FogRenderer.MobEffectFogFunction $$38 = getPriorityFogFunction($$6, float1);
        if ($$38 != null) {
            LivingEntity $$39 = (LivingEntity) $$6;
            $$37 = $$38.getModifiedVoidDarkness($$39, $$39.getEffect($$38.getMobEffect()), $$37, float1);
        }
        if ($$37 < 1.0F && $$5 != FogType.LAVA && $$5 != FogType.POWDER_SNOW) {
            if ($$37 < 0.0F) {
                $$37 = 0.0F;
            }
            $$37 *= $$37;
            fogRed *= $$37;
            fogGreen *= $$37;
            fogBlue *= $$37;
        }
        if (float4 > 0.0F) {
            fogRed = fogRed * (1.0F - float4) + fogRed * 0.7F * float4;
            fogGreen = fogGreen * (1.0F - float4) + fogGreen * 0.6F * float4;
            fogBlue = fogBlue * (1.0F - float4) + fogBlue * 0.6F * float4;
        }
        float $$40;
        if ($$5 == FogType.WATER) {
            if ($$6 instanceof LocalPlayer) {
                $$40 = ((LocalPlayer) $$6).getWaterVision();
            } else {
                $$40 = 1.0F;
            }
        } else {
            label86: {
                if ($$6 instanceof LivingEntity $$42 && $$42.hasEffect(MobEffects.NIGHT_VISION) && !$$42.hasEffect(MobEffects.DARKNESS)) {
                    $$40 = GameRenderer.getNightVisionScale($$42, float1);
                    break label86;
                }
                $$40 = 0.0F;
            }
        }
        if (fogRed != 0.0F && fogGreen != 0.0F && fogBlue != 0.0F) {
            float $$45 = Math.min(1.0F / fogRed, Math.min(1.0F / fogGreen, 1.0F / fogBlue));
            fogRed = fogRed * (1.0F - $$40) + fogRed * $$45 * $$40;
            fogGreen = fogGreen * (1.0F - $$40) + fogGreen * $$45 * $$40;
            fogBlue = fogBlue * (1.0F - $$40) + fogBlue * $$45 * $$40;
        }
        RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);
    }

    public static void setupNoFog() {
        RenderSystem.setShaderFogStart(Float.MAX_VALUE);
    }

    @Nullable
    private static FogRenderer.MobEffectFogFunction getPriorityFogFunction(Entity entity0, float float1) {
        return entity0 instanceof LivingEntity $$2 ? (FogRenderer.MobEffectFogFunction) MOB_EFFECT_FOG.stream().filter(p_234171_ -> p_234171_.isEnabled($$2, float1)).findFirst().orElse(null) : null;
    }

    public static void setupFog(Camera camera0, FogRenderer.FogMode fogRendererFogMode1, float float2, boolean boolean3, float float4) {
        FogType $$5 = camera0.getFluidInCamera();
        Entity $$6 = camera0.getEntity();
        FogRenderer.FogData $$7 = new FogRenderer.FogData(fogRendererFogMode1);
        FogRenderer.MobEffectFogFunction $$8 = getPriorityFogFunction($$6, float4);
        if ($$5 == FogType.LAVA) {
            if ($$6.isSpectator()) {
                $$7.start = -8.0F;
                $$7.end = float2 * 0.5F;
            } else if ($$6 instanceof LivingEntity && ((LivingEntity) $$6).hasEffect(MobEffects.FIRE_RESISTANCE)) {
                $$7.start = 0.0F;
                $$7.end = 3.0F;
            } else {
                $$7.start = 0.25F;
                $$7.end = 1.0F;
            }
        } else if ($$5 == FogType.POWDER_SNOW) {
            if ($$6.isSpectator()) {
                $$7.start = -8.0F;
                $$7.end = float2 * 0.5F;
            } else {
                $$7.start = 0.0F;
                $$7.end = 2.0F;
            }
        } else if ($$8 != null) {
            LivingEntity $$9 = (LivingEntity) $$6;
            MobEffectInstance $$10 = $$9.getEffect($$8.getMobEffect());
            if ($$10 != null) {
                $$8.setupFog($$7, $$9, $$10, float2, float4);
            }
        } else if ($$5 == FogType.WATER) {
            $$7.start = -8.0F;
            $$7.end = 96.0F;
            if ($$6 instanceof LocalPlayer $$11) {
                $$7.end = $$7.end * Math.max(0.25F, $$11.getWaterVision());
                Holder<Biome> $$12 = $$11.m_9236_().m_204166_($$11.m_20183_());
                if ($$12.is(BiomeTags.HAS_CLOSER_WATER_FOG)) {
                    $$7.end *= 0.85F;
                }
            }
            if ($$7.end > float2) {
                $$7.end = float2;
                $$7.shape = FogShape.CYLINDER;
            }
        } else if (boolean3) {
            $$7.start = float2 * 0.05F;
            $$7.end = Math.min(float2, 192.0F) * 0.5F;
        } else if (fogRendererFogMode1 == FogRenderer.FogMode.FOG_SKY) {
            $$7.start = 0.0F;
            $$7.end = float2;
            $$7.shape = FogShape.CYLINDER;
        } else {
            float $$13 = Mth.clamp(float2 / 10.0F, 4.0F, 64.0F);
            $$7.start = float2 - $$13;
            $$7.end = float2;
            $$7.shape = FogShape.CYLINDER;
        }
        RenderSystem.setShaderFogStart($$7.start);
        RenderSystem.setShaderFogEnd($$7.end);
        RenderSystem.setShaderFogShape($$7.shape);
    }

    public static void levelFogColor() {
        RenderSystem.setShaderFogColor(fogRed, fogGreen, fogBlue);
    }

    static class BlindnessFogFunction implements FogRenderer.MobEffectFogFunction {

        @Override
        public MobEffect getMobEffect() {
            return MobEffects.BLINDNESS;
        }

        @Override
        public void setupFog(FogRenderer.FogData fogRendererFogData0, LivingEntity livingEntity1, MobEffectInstance mobEffectInstance2, float float3, float float4) {
            float $$5 = mobEffectInstance2.isInfiniteDuration() ? 5.0F : Mth.lerp(Math.min(1.0F, (float) mobEffectInstance2.getDuration() / 20.0F), float3, 5.0F);
            if (fogRendererFogData0.mode == FogRenderer.FogMode.FOG_SKY) {
                fogRendererFogData0.start = 0.0F;
                fogRendererFogData0.end = $$5 * 0.8F;
            } else {
                fogRendererFogData0.start = $$5 * 0.25F;
                fogRendererFogData0.end = $$5;
            }
        }
    }

    static class DarknessFogFunction implements FogRenderer.MobEffectFogFunction {

        @Override
        public MobEffect getMobEffect() {
            return MobEffects.DARKNESS;
        }

        @Override
        public void setupFog(FogRenderer.FogData fogRendererFogData0, LivingEntity livingEntity1, MobEffectInstance mobEffectInstance2, float float3, float float4) {
            if (!mobEffectInstance2.getFactorData().isEmpty()) {
                float $$5 = Mth.lerp(((MobEffectInstance.FactorData) mobEffectInstance2.getFactorData().get()).getFactor(livingEntity1, float4), float3, 15.0F);
                fogRendererFogData0.start = fogRendererFogData0.mode == FogRenderer.FogMode.FOG_SKY ? 0.0F : $$5 * 0.75F;
                fogRendererFogData0.end = $$5;
            }
        }

        @Override
        public float getModifiedVoidDarkness(LivingEntity livingEntity0, MobEffectInstance mobEffectInstance1, float float2, float float3) {
            return mobEffectInstance1.getFactorData().isEmpty() ? 0.0F : 1.0F - ((MobEffectInstance.FactorData) mobEffectInstance1.getFactorData().get()).getFactor(livingEntity0, float3);
        }
    }

    static class FogData {

        public final FogRenderer.FogMode mode;

        public float start;

        public float end;

        public FogShape shape = FogShape.SPHERE;

        public FogData(FogRenderer.FogMode fogRendererFogMode0) {
            this.mode = fogRendererFogMode0;
        }
    }

    public static enum FogMode {

        FOG_SKY, FOG_TERRAIN
    }

    interface MobEffectFogFunction {

        MobEffect getMobEffect();

        void setupFog(FogRenderer.FogData var1, LivingEntity var2, MobEffectInstance var3, float var4, float var5);

        default boolean isEnabled(LivingEntity livingEntity0, float float1) {
            return livingEntity0.hasEffect(this.getMobEffect());
        }

        default float getModifiedVoidDarkness(LivingEntity livingEntity0, MobEffectInstance mobEffectInstance1, float float2, float float3) {
            MobEffectInstance $$4 = livingEntity0.getEffect(this.getMobEffect());
            if ($$4 != null) {
                if ($$4.endsWithin(19)) {
                    float2 = 1.0F - (float) $$4.getDuration() / 20.0F;
                } else {
                    float2 = 0.0F;
                }
            }
            return float2;
        }
    }
}