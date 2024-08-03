package net.mehvahdjukaar.supplementaries.client.renderers.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.models.HatStandModel;
import net.mehvahdjukaar.supplementaries.common.entities.HatStandEntity;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class HatStandRenderer extends LivingEntityRenderer<HatStandEntity, HatStandModel> {

    public HatStandRenderer(EntityRendererProvider.Context context) {
        super(context, new HatStandModel(context.bakeLayer(ClientRegistry.HAT_STAND_MODEL)), 0.0F);
        ModelPart modelPart = context.bakeLayer(ClientRegistry.HAT_STAND_MODEL_ARMOR);
        this.m_115326_(new HumanoidArmorLayer<>(this, new HumanoidModel(modelPart), new HumanoidModel(modelPart), context.getModelManager()));
        this.m_115326_(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
    }

    protected void setupRotations(HatStandEntity entityLiving, PoseStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
        float f = (float) (entityLiving.m_9236_().getGameTime() - entityLiving.lastHit) + partialTicks;
        if (f < 5.0F) {
            matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f / 1.5F * (float) Math.PI) * 3.0F));
        }
    }

    public Vec3 getRenderOffset(HatStandEntity entity, float partialTicks) {
        return entity.isNoBasePlate() ? new Vec3(0.0, -0.0625, 0.0) : super.m_7860_(entity, partialTicks);
    }

    protected boolean shouldShowName(HatStandEntity entity) {
        double d = this.f_114476_.distanceToSqr(entity);
        float f = entity.m_6047_() ? 32.0F : 64.0F;
        return d < (double) (f * f) && entity.m_20151_();
    }

    public ResourceLocation getTextureLocation(HatStandEntity entity) {
        return ModTextures.HAT_STAND;
    }
}