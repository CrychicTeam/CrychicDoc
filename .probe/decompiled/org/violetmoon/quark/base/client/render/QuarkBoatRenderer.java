package org.violetmoon.quark.base.client.render;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Map;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.violetmoon.quark.base.client.handler.ModelHandler;
import org.violetmoon.quark.base.handler.WoodSetHandler;
import org.violetmoon.quark.base.item.boat.IQuarkBoat;

public class QuarkBoatRenderer extends EntityRenderer<Boat> {

    private final Map<String, QuarkBoatRenderer.BoatModelTuple> boatResources;

    public QuarkBoatRenderer(EntityRendererProvider.Context context, boolean chest) {
        super(context);
        this.f_114477_ = 0.8F;
        this.boatResources = computeBoatResources(chest, context);
    }

    private static Map<String, QuarkBoatRenderer.BoatModelTuple> computeBoatResources(boolean chest, EntityRendererProvider.Context context) {
        return (Map<String, QuarkBoatRenderer.BoatModelTuple>) WoodSetHandler.boatTypes().collect(ImmutableMap.toImmutableMap(Functions.identity(), name -> {
            String folder = chest ? "chest_boat" : "boat";
            ResourceLocation texture = new ResourceLocation("quark", "textures/model/entity/" + folder + "/" + name + ".png");
            BoatModel model = (BoatModel) (chest ? new ChestBoatModel(context.bakeLayer(ModelHandler.quark_boat_chest)) : new BoatModel(context.bakeLayer(ModelHandler.quark_boat)));
            return new QuarkBoatRenderer.BoatModelTuple(texture, model);
        }));
    }

    public void render(Boat boat, float yaw, float partialTicks, PoseStack matrix, @NotNull MultiBufferSource buffer, int light) {
        matrix.pushPose();
        matrix.translate(0.0, 0.375, 0.0);
        matrix.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        float wiggleAngle = (float) boat.getHurtTime() - partialTicks;
        float wiggleMagnitude = boat.getDamage() - partialTicks;
        if (wiggleMagnitude < 0.0F) {
            wiggleMagnitude = 0.0F;
        }
        if (wiggleAngle > 0.0F) {
            matrix.mulPose(Axis.XP.rotationDegrees(Mth.sin(wiggleAngle) * wiggleAngle * wiggleMagnitude / 10.0F * (float) boat.getHurtDir()));
        }
        float f2 = boat.getBubbleAngle(partialTicks);
        if (!Mth.equal(f2, 0.0F)) {
            matrix.mulPose(new Quaternionf(1.0F, 0.0F, 1.0F, boat.getBubbleAngle(partialTicks) * (float) (Math.PI / 180.0)));
        }
        QuarkBoatRenderer.BoatModelTuple tuple = this.getModelWithLocation(boat);
        ResourceLocation loc = tuple.resloc();
        BoatModel model = tuple.model();
        matrix.scale(-1.0F, -1.0F, 1.0F);
        matrix.mulPose(Axis.YP.rotationDegrees(90.0F));
        model.setupAnim(boat, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = buffer.getBuffer(model.m_103119_(loc));
        model.m_7695_(matrix, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!boat.isUnderWater()) {
            VertexConsumer waterMask = buffer.getBuffer(RenderType.waterMask());
            model.waterPatch().render(matrix, waterMask, light, OverlayTexture.NO_OVERLAY);
        }
        matrix.popPose();
        super.render(boat, yaw, partialTicks, matrix, buffer, light);
    }

    @Deprecated
    @NotNull
    public ResourceLocation getTextureLocation(@NotNull Boat boat) {
        return this.getModelWithLocation(boat).resloc();
    }

    public QuarkBoatRenderer.BoatModelTuple getModelWithLocation(Boat boat) {
        return (QuarkBoatRenderer.BoatModelTuple) this.boatResources.get(((IQuarkBoat) boat).getQuarkBoatTypeObj().name());
    }

    private static record BoatModelTuple(ResourceLocation resloc, BoatModel model) {
    }
}