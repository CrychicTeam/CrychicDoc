package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.particle.LightningBoltData;
import com.github.alexthe666.iceandfire.client.particle.LightningRender;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RenderLightningDragon extends RenderDragonBase {

    private final LightningRender lightningRender = new LightningRender();

    public RenderLightningDragon(EntityRendererProvider.Context context, AdvancedEntityModel model, int dragonType) {
        super(context, model, dragonType);
    }

    public boolean shouldRender(@NotNull EntityDragonBase livingEntityIn, @NotNull Frustum camera, double camX, double camY, double camZ) {
        if (super.m_5523_(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            EntityLightningDragon lightningDragon = (EntityLightningDragon) livingEntityIn;
            if (lightningDragon.hasLightningTarget()) {
                Vec3 Vector3d1 = lightningDragon.getHeadPosition();
                Vec3 Vector3d = new Vec3((double) lightningDragon.getLightningTargetX(), (double) lightningDragon.getLightningTargetY(), (double) lightningDragon.getLightningTargetZ());
                return camera.isVisible(new AABB(Vector3d1.x, Vector3d1.y, Vector3d1.z, Vector3d.x, Vector3d.y, Vector3d.z));
            } else {
                return false;
            }
        }
    }

    public void render(@NotNull EntityDragonBase entityIn, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        super.m_7392_(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        EntityLightningDragon lightningDragon = (EntityLightningDragon) entityIn;
        matrixStackIn.pushPose();
        if (lightningDragon.hasLightningTarget()) {
            double dist = (double) Minecraft.getInstance().player.m_20270_(lightningDragon);
            if (dist <= (double) Math.max(256.0F, (float) Minecraft.getInstance().options.renderDistance().get().intValue() * 16.0F)) {
                Vec3 Vector3d1 = lightningDragon.getHeadPosition();
                Vec3 Vector3d = new Vec3((double) lightningDragon.getLightningTargetX(), (double) lightningDragon.getLightningTargetY(), (double) lightningDragon.getLightningTargetZ());
                float energyScale = 0.4F * lightningDragon.m_6134_();
                LightningBoltData bolt = new LightningBoltData(LightningBoltData.BoltRenderInfo.ELECTRICITY, Vector3d1, Vector3d, 15).size(0.05F * getBoundedScale(energyScale, 0.5F, 2.0F)).lifespan(4).spawn(LightningBoltData.SpawnFunction.NO_DELAY);
                this.lightningRender.update(null, bolt, partialTicks);
                matrixStackIn.translate(-lightningDragon.m_20185_(), -lightningDragon.m_20186_(), -lightningDragon.m_20189_());
                this.lightningRender.render(partialTicks, matrixStackIn, bufferIn);
            }
        }
        matrixStackIn.popPose();
    }

    private static float getBoundedScale(float scale, float min, float max) {
        return min + scale * (max - min);
    }
}