package com.rekindled.embers.fluidtypes;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Consumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

public class MoltenMetalFluidType extends EmbersFluidType {

    public MoltenMetalFluidType(FluidType.Properties properties, EmbersFluidType.FluidInfo info) {
        super(properties, info);
    }

    @Override
    public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity) {
        double d8 = entity.m_20186_();
        boolean flag = entity.m_20184_().y <= 0.0;
        entity.m_19920_(0.02F, movementVector);
        entity.m_6478_(MoverType.SELF, entity.m_20184_());
        if (entity.getFluidTypeHeight(this) <= entity.m_20204_()) {
            entity.m_20256_(entity.m_20184_().multiply(0.5, 0.8F, 0.5));
            Vec3 vec33 = entity.getFluidFallingAdjustedMovement(gravity, flag, entity.m_20184_());
            entity.m_20256_(vec33);
        } else {
            entity.m_20256_(entity.m_20184_().scale(0.5));
        }
        if (!entity.m_20068_()) {
            entity.m_20256_(entity.m_20184_().add(0.0, -gravity / 4.0, 0.0));
        }
        Vec3 vec34 = entity.m_20184_();
        if (entity.f_19862_ && entity.m_20229_(vec34.x, vec34.y + 0.6F - entity.m_20186_() + d8, vec34.z)) {
            entity.m_20334_(vec34.x, 0.3F, vec34.z);
        }
        entity.m_20093_();
        return true;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture() {
                return MoltenMetalFluidType.this.TEXTURE_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return MoltenMetalFluidType.this.TEXTURE_FLOW;
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return MoltenMetalFluidType.this.FOG_COLOR;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(MoltenMetalFluidType.this.fogStart);
                RenderSystem.setShaderFogEnd(MoltenMetalFluidType.this.fogEnd);
            }
        });
    }

    @Override
    public void setItemMovement(ItemEntity entity) {
        entity.m_20093_();
        Vec3 vec3 = entity.m_20184_();
        entity.m_20334_(vec3.x * 0.95F, vec3.y + (double) (vec3.y < 0.06F ? 5.0E-4F : 0.0F), vec3.z * 0.95F);
    }
}