package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.layers.ShulkerHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ShulkerRenderer extends MobRenderer<Shulker, ShulkerModel<Shulker>> {

    private static final ResourceLocation DEFAULT_TEXTURE_LOCATION = new ResourceLocation("textures/" + Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION.texture().getPath() + ".png");

    private static final ResourceLocation[] TEXTURE_LOCATION = (ResourceLocation[]) Sheets.SHULKER_TEXTURE_LOCATION.stream().map(p_115919_ -> new ResourceLocation("textures/" + p_115919_.texture().getPath() + ".png")).toArray(ResourceLocation[]::new);

    public ShulkerRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new ShulkerModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.SHULKER)), 0.0F);
        this.m_115326_(new ShulkerHeadLayer(this));
    }

    public Vec3 getRenderOffset(Shulker shulker0, float float1) {
        return (Vec3) shulker0.getRenderPosition(float1).orElse(super.m_7860_(shulker0, float1));
    }

    public boolean shouldRender(Shulker shulker0, Frustum frustum1, double double2, double double3, double double4) {
        return super.shouldRender(shulker0, frustum1, double2, double3, double4) ? true : shulker0.getRenderPosition(0.0F).filter(p_174374_ -> {
            EntityType<?> $$3 = shulker0.m_6095_();
            float $$4 = $$3.getHeight() / 2.0F;
            float $$5 = $$3.getWidth() / 2.0F;
            Vec3 $$6 = Vec3.atBottomCenterOf(shulker0.m_20183_());
            return frustum1.isVisible(new AABB(p_174374_.x, p_174374_.y + (double) $$4, p_174374_.z, $$6.x, $$6.y + (double) $$4, $$6.z).inflate((double) $$5, (double) $$4, (double) $$5));
        }).isPresent();
    }

    public ResourceLocation getTextureLocation(Shulker shulker0) {
        return getTextureLocation(shulker0.getColor());
    }

    public static ResourceLocation getTextureLocation(@Nullable DyeColor dyeColor0) {
        return dyeColor0 == null ? DEFAULT_TEXTURE_LOCATION : TEXTURE_LOCATION[dyeColor0.getId()];
    }

    protected void setupRotations(Shulker shulker0, PoseStack poseStack1, float float2, float float3, float float4) {
        super.m_7523_(shulker0, poseStack1, float2, float3 + 180.0F, float4);
        poseStack1.translate(0.0, 0.5, 0.0);
        poseStack1.mulPose(shulker0.getAttachFace().getOpposite().getRotation());
        poseStack1.translate(0.0, -0.5, 0.0);
    }
}