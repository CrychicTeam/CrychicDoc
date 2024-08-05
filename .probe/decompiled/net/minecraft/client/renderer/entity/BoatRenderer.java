package net.minecraft.client.renderer.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ChestRaftModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.RaftModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import org.joml.Quaternionf;

public class BoatRenderer extends EntityRenderer<Boat> {

    private final Map<Boat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public BoatRenderer(EntityRendererProvider.Context entityRendererProviderContext0, boolean boolean1) {
        super(entityRendererProviderContext0);
        this.f_114477_ = 0.8F;
        this.boatResources = (Map<Boat.Type, Pair<ResourceLocation, ListModel<Boat>>>) Stream.of(Boat.Type.values()).collect(ImmutableMap.toImmutableMap(p_173938_ -> p_173938_, p_247941_ -> Pair.of(new ResourceLocation(getTextureLocation(p_247941_, boolean1)), this.createBoatModel(entityRendererProviderContext0, p_247941_, boolean1))));
    }

    private ListModel<Boat> createBoatModel(EntityRendererProvider.Context entityRendererProviderContext0, Boat.Type boatType1, boolean boolean2) {
        ModelLayerLocation $$3 = boolean2 ? ModelLayers.createChestBoatModelName(boatType1) : ModelLayers.createBoatModelName(boatType1);
        ModelPart $$4 = entityRendererProviderContext0.bakeLayer($$3);
        if (boatType1 == Boat.Type.BAMBOO) {
            return (ListModel<Boat>) (boolean2 ? new ChestRaftModel($$4) : new RaftModel($$4));
        } else {
            return (ListModel<Boat>) (boolean2 ? new ChestBoatModel($$4) : new BoatModel($$4));
        }
    }

    private static String getTextureLocation(Boat.Type boatType0, boolean boolean1) {
        return boolean1 ? "textures/entity/chest_boat/" + boatType0.getName() + ".png" : "textures/entity/boat/" + boatType0.getName() + ".png";
    }

    public void render(Boat boat0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        poseStack3.translate(0.0F, 0.375F, 0.0F);
        poseStack3.mulPose(Axis.YP.rotationDegrees(180.0F - float1));
        float $$6 = (float) boat0.getHurtTime() - float2;
        float $$7 = boat0.getDamage() - float2;
        if ($$7 < 0.0F) {
            $$7 = 0.0F;
        }
        if ($$6 > 0.0F) {
            poseStack3.mulPose(Axis.XP.rotationDegrees(Mth.sin($$6) * $$6 * $$7 / 10.0F * (float) boat0.getHurtDir()));
        }
        float $$8 = boat0.getBubbleAngle(float2);
        if (!Mth.equal($$8, 0.0F)) {
            poseStack3.mulPose(new Quaternionf().setAngleAxis(boat0.getBubbleAngle(float2) * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));
        }
        Pair<ResourceLocation, ListModel<Boat>> $$9 = (Pair<ResourceLocation, ListModel<Boat>>) this.boatResources.get(boat0.getVariant());
        ResourceLocation $$10 = (ResourceLocation) $$9.getFirst();
        ListModel<Boat> $$11 = (ListModel<Boat>) $$9.getSecond();
        poseStack3.scale(-1.0F, -1.0F, 1.0F);
        poseStack3.mulPose(Axis.YP.rotationDegrees(90.0F));
        $$11.m_6973_(boat0, float2, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer $$12 = multiBufferSource4.getBuffer($$11.m_103119_($$10));
        $$11.renderToBuffer(poseStack3, $$12, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!boat0.isUnderWater()) {
            VertexConsumer $$13 = multiBufferSource4.getBuffer(RenderType.waterMask());
            if ($$11 instanceof WaterPatchModel $$14) {
                $$14.waterPatch().render(poseStack3, $$13, int5, OverlayTexture.NO_OVERLAY);
            }
        }
        poseStack3.popPose();
        super.render(boat0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(Boat boat0) {
        return (ResourceLocation) ((Pair) this.boatResources.get(boat0.getVariant())).getFirst();
    }
}