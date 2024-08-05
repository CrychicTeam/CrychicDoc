package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.ArmorStandModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.ArmorStand;

public class ArmorStandRenderer extends LivingEntityRenderer<ArmorStand, ArmorStandArmorModel> {

    public static final ResourceLocation DEFAULT_SKIN_LOCATION = new ResourceLocation("textures/entity/armorstand/wood.png");

    public ArmorStandRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new ArmorStandModel(entityRendererProviderContext0.bakeLayer(ModelLayers.ARMOR_STAND)), 0.0F);
        this.m_115326_(new HumanoidArmorLayer<>(this, new ArmorStandArmorModel(entityRendererProviderContext0.bakeLayer(ModelLayers.ARMOR_STAND_INNER_ARMOR)), new ArmorStandArmorModel(entityRendererProviderContext0.bakeLayer(ModelLayers.ARMOR_STAND_OUTER_ARMOR)), entityRendererProviderContext0.getModelManager()));
        this.m_115326_(new ItemInHandLayer<>(this, entityRendererProviderContext0.getItemInHandRenderer()));
        this.m_115326_(new ElytraLayer<>(this, entityRendererProviderContext0.getModelSet()));
        this.m_115326_(new CustomHeadLayer<>(this, entityRendererProviderContext0.getModelSet(), entityRendererProviderContext0.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(ArmorStand armorStand0) {
        return DEFAULT_SKIN_LOCATION;
    }

    protected void setupRotations(ArmorStand armorStand0, PoseStack poseStack1, float float2, float float3, float float4) {
        poseStack1.mulPose(Axis.YP.rotationDegrees(180.0F - float3));
        float $$5 = (float) (armorStand0.m_9236_().getGameTime() - armorStand0.lastHit) + float4;
        if ($$5 < 5.0F) {
            poseStack1.mulPose(Axis.YP.rotationDegrees(Mth.sin($$5 / 1.5F * (float) Math.PI) * 3.0F));
        }
    }

    protected boolean shouldShowName(ArmorStand armorStand0) {
        double $$1 = this.f_114476_.distanceToSqr(armorStand0);
        float $$2 = armorStand0.m_6047_() ? 32.0F : 64.0F;
        return $$1 >= (double) ($$2 * $$2) ? false : armorStand0.m_20151_();
    }

    @Nullable
    protected RenderType getRenderType(ArmorStand armorStand0, boolean boolean1, boolean boolean2, boolean boolean3) {
        if (!armorStand0.isMarker()) {
            return super.getRenderType(armorStand0, boolean1, boolean2, boolean3);
        } else {
            ResourceLocation $$4 = this.getTextureLocation(armorStand0);
            if (boolean2) {
                return RenderType.entityTranslucent($$4, false);
            } else {
                return boolean1 ? RenderType.entityCutoutNoCull($$4, false) : null;
            }
        }
    }
}