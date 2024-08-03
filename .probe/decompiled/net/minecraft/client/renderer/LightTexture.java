package net.minecraft.client.renderer;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.dimension.DimensionType;
import org.joml.Vector3f;

public class LightTexture implements AutoCloseable {

    public static final int FULL_BRIGHT = 15728880;

    public static final int FULL_SKY = 15728640;

    public static final int FULL_BLOCK = 240;

    private final DynamicTexture lightTexture;

    private final NativeImage lightPixels;

    private final ResourceLocation lightTextureLocation;

    private boolean updateLightTexture;

    private float blockLightRedFlicker;

    private final GameRenderer renderer;

    private final Minecraft minecraft;

    public LightTexture(GameRenderer gameRenderer0, Minecraft minecraft1) {
        this.renderer = gameRenderer0;
        this.minecraft = minecraft1;
        this.lightTexture = new DynamicTexture(16, 16, false);
        this.lightTextureLocation = this.minecraft.getTextureManager().register("light_map", this.lightTexture);
        this.lightPixels = this.lightTexture.getPixels();
        for (int $$2 = 0; $$2 < 16; $$2++) {
            for (int $$3 = 0; $$3 < 16; $$3++) {
                this.lightPixels.setPixelRGBA($$3, $$2, -1);
            }
        }
        this.lightTexture.upload();
    }

    public void close() {
        this.lightTexture.close();
    }

    public void tick() {
        this.blockLightRedFlicker = this.blockLightRedFlicker + (float) ((Math.random() - Math.random()) * Math.random() * Math.random() * 0.1);
        this.blockLightRedFlicker *= 0.9F;
        this.updateLightTexture = true;
    }

    public void turnOffLightLayer() {
        RenderSystem.setShaderTexture(2, 0);
    }

    public void turnOnLightLayer() {
        RenderSystem.setShaderTexture(2, this.lightTextureLocation);
        this.minecraft.getTextureManager().bindForSetup(this.lightTextureLocation);
        RenderSystem.texParameter(3553, 10241, 9729);
        RenderSystem.texParameter(3553, 10240, 9729);
    }

    private float getDarknessGamma(float float0) {
        if (this.minecraft.player.m_21023_(MobEffects.DARKNESS)) {
            MobEffectInstance $$1 = this.minecraft.player.m_21124_(MobEffects.DARKNESS);
            if ($$1 != null && $$1.getFactorData().isPresent()) {
                return ((MobEffectInstance.FactorData) $$1.getFactorData().get()).getFactor(this.minecraft.player, float0);
            }
        }
        return 0.0F;
    }

    private float calculateDarknessScale(LivingEntity livingEntity0, float float1, float float2) {
        float $$3 = 0.45F * float1;
        return Math.max(0.0F, Mth.cos(((float) livingEntity0.f_19797_ - float2) * (float) Math.PI * 0.025F) * $$3);
    }

    public void updateLightTexture(float float0) {
        if (this.updateLightTexture) {
            this.updateLightTexture = false;
            this.minecraft.getProfiler().push("lightTex");
            ClientLevel $$1 = this.minecraft.level;
            if ($$1 != null) {
                float $$2 = $$1.getSkyDarken(1.0F);
                float $$3;
                if ($$1.getSkyFlashTime() > 0) {
                    $$3 = 1.0F;
                } else {
                    $$3 = $$2 * 0.95F + 0.05F;
                }
                float $$5 = this.minecraft.options.darknessEffectScale().get().floatValue();
                float $$6 = this.getDarknessGamma(float0) * $$5;
                float $$7 = this.calculateDarknessScale(this.minecraft.player, $$6, float0) * $$5;
                float $$8 = this.minecraft.player.getWaterVision();
                float $$9;
                if (this.minecraft.player.m_21023_(MobEffects.NIGHT_VISION)) {
                    $$9 = GameRenderer.getNightVisionScale(this.minecraft.player, float0);
                } else if ($$8 > 0.0F && this.minecraft.player.m_21023_(MobEffects.CONDUIT_POWER)) {
                    $$9 = $$8;
                } else {
                    $$9 = 0.0F;
                }
                Vector3f $$12 = new Vector3f($$2, $$2, 1.0F).lerp(new Vector3f(1.0F, 1.0F, 1.0F), 0.35F);
                float $$13 = this.blockLightRedFlicker + 1.5F;
                Vector3f $$14 = new Vector3f();
                for (int $$15 = 0; $$15 < 16; $$15++) {
                    for (int $$16 = 0; $$16 < 16; $$16++) {
                        float $$17 = getBrightness($$1.m_6042_(), $$15) * $$3;
                        float $$18 = getBrightness($$1.m_6042_(), $$16) * $$13;
                        float $$20 = $$18 * (($$18 * 0.6F + 0.4F) * 0.6F + 0.4F);
                        float $$21 = $$18 * ($$18 * $$18 * 0.6F + 0.4F);
                        $$14.set($$18, $$20, $$21);
                        boolean $$22 = $$1.effects().forceBrightLightmap();
                        if ($$22) {
                            $$14.lerp(new Vector3f(0.99F, 1.12F, 1.0F), 0.25F);
                            clampColor($$14);
                        } else {
                            Vector3f $$23 = new Vector3f($$12).mul($$17);
                            $$14.add($$23);
                            $$14.lerp(new Vector3f(0.75F, 0.75F, 0.75F), 0.04F);
                            if (this.renderer.getDarkenWorldAmount(float0) > 0.0F) {
                                float $$24 = this.renderer.getDarkenWorldAmount(float0);
                                Vector3f $$25 = new Vector3f($$14).mul(0.7F, 0.6F, 0.6F);
                                $$14.lerp($$25, $$24);
                            }
                        }
                        if ($$9 > 0.0F) {
                            float $$26 = Math.max($$14.x(), Math.max($$14.y(), $$14.z()));
                            if ($$26 < 1.0F) {
                                float $$27 = 1.0F / $$26;
                                Vector3f $$28 = new Vector3f($$14).mul($$27);
                                $$14.lerp($$28, $$9);
                            }
                        }
                        if (!$$22) {
                            if ($$7 > 0.0F) {
                                $$14.add(-$$7, -$$7, -$$7);
                            }
                            clampColor($$14);
                        }
                        float $$29 = this.minecraft.options.gamma().get().floatValue();
                        Vector3f $$30 = new Vector3f(this.notGamma($$14.x), this.notGamma($$14.y), this.notGamma($$14.z));
                        $$14.lerp($$30, Math.max(0.0F, $$29 - $$6));
                        $$14.lerp(new Vector3f(0.75F, 0.75F, 0.75F), 0.04F);
                        clampColor($$14);
                        $$14.mul(255.0F);
                        int $$31 = 255;
                        int $$32 = (int) $$14.x();
                        int $$33 = (int) $$14.y();
                        int $$34 = (int) $$14.z();
                        this.lightPixels.setPixelRGBA($$16, $$15, 0xFF000000 | $$34 << 16 | $$33 << 8 | $$32);
                    }
                }
                this.lightTexture.upload();
                this.minecraft.getProfiler().pop();
            }
        }
    }

    private static void clampColor(Vector3f vectorF0) {
        vectorF0.set(Mth.clamp(vectorF0.x, 0.0F, 1.0F), Mth.clamp(vectorF0.y, 0.0F, 1.0F), Mth.clamp(vectorF0.z, 0.0F, 1.0F));
    }

    private float notGamma(float float0) {
        float $$1 = 1.0F - float0;
        return 1.0F - $$1 * $$1 * $$1 * $$1;
    }

    public static float getBrightness(DimensionType dimensionType0, int int1) {
        float $$2 = (float) int1 / 15.0F;
        float $$3 = $$2 / (4.0F - 3.0F * $$2);
        return Mth.lerp(dimensionType0.ambientLight(), $$3, 1.0F);
    }

    public static int pack(int int0, int int1) {
        return int0 << 4 | int1 << 20;
    }

    public static int block(int int0) {
        return int0 >> 4 & 65535;
    }

    public static int sky(int int0) {
        return int0 >> 20 & 65535;
    }
}