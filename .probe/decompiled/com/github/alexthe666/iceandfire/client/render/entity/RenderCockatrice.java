package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.model.ModelCockatrice;
import com.github.alexthe666.iceandfire.client.model.ModelCockatriceChick;
import com.github.alexthe666.iceandfire.client.particle.CockatriceBeamRender;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RenderCockatrice extends MobRenderer<EntityCockatrice, AdvancedEntityModel<EntityCockatrice>> {

    public static final ResourceLocation TEXTURE_ROOSTER = new ResourceLocation("iceandfire:textures/models/cockatrice/cockatrice_0.png");

    public static final ResourceLocation TEXTURE_HEN = new ResourceLocation("iceandfire:textures/models/cockatrice/cockatrice_1.png");

    public static final ResourceLocation TEXTURE_ROOSTER_CHICK = new ResourceLocation("iceandfire:textures/models/cockatrice/cockatrice_0_chick.png");

    public static final ResourceLocation TEXTURE_HEN_CHICK = new ResourceLocation("iceandfire:textures/models/cockatrice/cockatrice_1_chick.png");

    public static final ModelCockatrice ADULT_MODEL = new ModelCockatrice();

    public static final ModelCockatriceChick BABY_MODEL = new ModelCockatriceChick();

    public RenderCockatrice(EntityRendererProvider.Context context) {
        super(context, new ModelCockatrice(), 0.6F);
    }

    private Vec3 getPosition(LivingEntity LivingEntityIn, double p_177110_2_, float p_177110_4_) {
        double d0 = LivingEntityIn.f_19790_ + (LivingEntityIn.m_20185_() - LivingEntityIn.f_19790_) * (double) p_177110_4_;
        double d1 = p_177110_2_ + LivingEntityIn.f_19791_ + (LivingEntityIn.m_20186_() - LivingEntityIn.f_19791_) * (double) p_177110_4_;
        double d2 = LivingEntityIn.f_19792_ + (LivingEntityIn.m_20189_() - LivingEntityIn.f_19792_) * (double) p_177110_4_;
        return new Vec3(d0, d1, d2);
    }

    public boolean shouldRender(@NotNull EntityCockatrice livingEntityIn, @NotNull Frustum camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            if (livingEntityIn.hasTargetedEntity()) {
                LivingEntity livingentity = livingEntityIn.getTargetedEntity();
                if (livingentity != null) {
                    Vec3 Vector3d = this.getPosition(livingentity, (double) livingentity.m_20206_() * 0.5, 1.0F);
                    Vec3 Vector3d1 = this.getPosition(livingEntityIn, (double) livingEntityIn.m_20192_(), 1.0F);
                    return camera.isVisible(new AABB(Vector3d1.x, Vector3d1.y, Vector3d1.z, Vector3d.x, Vector3d.y, Vector3d.z));
                }
            }
            return false;
        }
    }

    public void render(EntityCockatrice entityIn, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        if (entityIn.m_6162_()) {
            this.f_115290_ = BABY_MODEL;
        } else {
            this.f_115290_ = ADULT_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        LivingEntity livingentity = entityIn.getTargetedEntity();
        boolean blindness = entityIn.m_21023_(MobEffects.BLINDNESS) || livingentity != null && livingentity.hasEffect(MobEffects.BLINDNESS);
        if (!blindness && livingentity != null && EntityGorgon.isEntityLookingAt(entityIn, livingentity, 0.6F) && EntityGorgon.isEntityLookingAt(livingentity, entityIn, 0.6F) && livingentity != null) {
            CockatriceBeamRender.render(entityIn, livingentity, matrixStackIn, bufferIn, partialTicks);
        }
    }

    protected void scale(EntityCockatrice entity, @NotNull PoseStack matrixStackIn, float partialTickTime) {
        if (entity.m_6162_()) {
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        }
    }

    @NotNull
    public ResourceLocation getTextureLocation(EntityCockatrice cockatrice) {
        if (cockatrice.m_6162_()) {
            return cockatrice.isHen() ? TEXTURE_HEN_CHICK : TEXTURE_ROOSTER_CHICK;
        } else {
            return cockatrice.isHen() ? TEXTURE_HEN : TEXTURE_ROOSTER;
        }
    }
}