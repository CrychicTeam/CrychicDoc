package com.rekindled.embers.fluidtypes;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Consumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

public class SteamFluidType extends EmbersFluidType {

    public SteamFluidType(FluidType.Properties properties, EmbersFluidType.FluidInfo info) {
        super(properties, info);
    }

    @Override
    public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity) {
        entity.hurt(entity.m_9236_().damageSources().inFire(), 1.0F);
        entity.m_9236_().getProfiler().push("jump");
        if (entity.jumping) {
            boolean flag = entity.m_20069_();
            if (flag && !entity.m_20096_()) {
                entity.jumpInFluid(ForgeMod.WATER_TYPE.get());
            } else if (entity.m_20077_() && !entity.m_20096_()) {
                entity.jumpInFluid(ForgeMod.LAVA_TYPE.get());
            } else if ((entity.m_20096_() || flag) && entity.noJumpDelay == 0) {
                jumpFromGround(entity);
                entity.noJumpDelay = 10;
            }
        } else {
            entity.noJumpDelay = 0;
        }
        entity.m_9236_().getProfiler().pop();
        float f2 = 0.6F;
        float f3 = entity.m_20096_() ? f2 * 0.91F : 0.91F;
        Vec3 vec35 = entity.handleRelativeFrictionAndCalculateMovement(movementVector, f2);
        double d2 = vec35.y;
        if (entity.hasEffect(MobEffects.LEVITATION)) {
            d2 += (0.05 * (double) (entity.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec35.y) * 0.2;
            entity.m_183634_();
        } else if (entity.m_9236_().isClientSide() && !entity.m_9236_().m_46805_(entity.m_20183_())) {
            if (entity.m_20186_() > (double) entity.m_9236_().m_141937_()) {
                d2 = -0.1;
            } else {
                d2 = 0.0;
            }
        } else if (!entity.m_20068_()) {
            d2 -= gravity;
        }
        if (entity.shouldDiscardFriction()) {
            vec35 = new Vec3(vec35.x, d2, vec35.z);
        } else {
            vec35 = new Vec3(vec35.x * (double) f3, d2 * 0.98F, vec35.z * (double) f3);
        }
        entity.m_20256_(vec35);
        return true;
    }

    static void jumpFromGround(LivingEntity entity) {
        Vec3 vec3 = entity.m_20184_();
        entity.m_20334_(vec3.x, (double) (0.42F + entity.getJumpBoostPower()), vec3.z);
        if (entity.m_20142_()) {
            float f = entity.m_146908_() * (float) (Math.PI / 180.0);
            entity.m_20256_(entity.m_20184_().add((double) (-Mth.sin(f) * 0.2F), 0.0, (double) (Mth.cos(f) * 0.2F)));
        }
        entity.f_19812_ = true;
        ForgeHooks.onLivingJump(entity);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture() {
                return SteamFluidType.this.TEXTURE_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return SteamFluidType.this.TEXTURE_FLOW;
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return SteamFluidType.this.FOG_COLOR;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(SteamFluidType.this.fogStart);
                float[] color = RenderSystem.getShaderFogColor();
                RenderSystem.setShaderFogColor(color[0], color[1], color[2], 0.7F);
                RenderSystem.setShaderFogEnd(SteamFluidType.this.fogEnd);
            }
        });
    }
}