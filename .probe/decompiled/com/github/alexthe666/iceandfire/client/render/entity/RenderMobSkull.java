package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.client.model.ModelCockatrice;
import com.github.alexthe666.iceandfire.client.model.ModelCyclops;
import com.github.alexthe666.iceandfire.client.model.ModelHippogryph;
import com.github.alexthe666.iceandfire.client.model.ModelHydraHead;
import com.github.alexthe666.iceandfire.client.model.ModelStymphalianBird;
import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.entity.EntityMobSkull;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RenderMobSkull extends EntityRenderer<EntityMobSkull> {

    private static final Map<String, ResourceLocation> SKULL_TEXTURE_CACHE = Maps.newHashMap();

    private final ModelHippogryph hippogryphModel = new ModelHippogryph();

    private final ModelCyclops cyclopsModel = new ModelCyclops();

    private final ModelCockatrice cockatriceModel = new ModelCockatrice();

    private final ModelStymphalianBird stymphalianBirdModel = new ModelStymphalianBird();

    private final ModelTroll trollModel = new ModelTroll();

    private final ModelAmphithere amphithereModel = new ModelAmphithere();

    private final ModelHydraHead hydraModel;

    private final TabulaModel seaSerpentModel;

    public RenderMobSkull(EntityRendererProvider.Context context, AdvancedEntityModel seaSerpentModel) {
        super(context);
        this.seaSerpentModel = (TabulaModel) seaSerpentModel;
        this.hydraModel = new ModelHydraHead(0);
    }

    private static void setRotationAngles(BasicModelPart cube, float rotX, float rotY, float rotZ) {
        cube.rotateAngleX = rotX;
        cube.rotateAngleY = rotY;
        cube.rotateAngleZ = rotZ;
    }

    public void render(@NotNull EntityMobSkull entity, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-180.0F));
        matrixStackIn.mulPose(Axis.YN.rotationDegrees(180.0F - entity.getYaw()));
        float f = 0.0625F;
        float size = 1.0F;
        matrixStackIn.scale(size, size, size);
        matrixStackIn.translate(0.0F, entity.isOnWall() ? -0.24F : -0.12F, 0.5F);
        this.renderForEnum(entity.getSkullType(), entity.isOnWall(), matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
    }

    private void renderForEnum(EnumSkullType skull, boolean onWall, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(this.getSkullTexture(skull)));
        switch(skull) {
            case HIPPOGRYPH:
                matrixStackIn.translate(0.0F, -0.0F, -0.2F);
                matrixStackIn.scale(1.2F, 1.2F, 1.2F);
                this.hippogryphModel.resetToDefaultPose();
                setRotationAngles(this.hippogryphModel.Head, onWall ? (float) Math.toRadians(50.0) : (float) Math.toRadians(-5.0), 0.0F, 0.0F);
                this.hippogryphModel.Head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                break;
            case CYCLOPS:
                matrixStackIn.translate(0.0F, 1.8F, -0.5F);
                matrixStackIn.scale(2.25F, 2.25F, 2.25F);
                this.cyclopsModel.resetToDefaultPose();
                setRotationAngles(this.cyclopsModel.Head, onWall ? (float) Math.toRadians(50.0) : 0.0F, 0.0F, 0.0F);
                this.cyclopsModel.Head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                break;
            case COCKATRICE:
                if (onWall) {
                    matrixStackIn.translate(0.0F, 0.0F, 0.35F);
                }
                this.cockatriceModel.resetToDefaultPose();
                setRotationAngles(this.cockatriceModel.head, onWall ? (float) Math.toRadians(50.0) : 0.0F, 0.0F, 0.0F);
                this.cockatriceModel.head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                break;
            case STYMPHALIAN:
                if (!onWall) {
                    matrixStackIn.translate(0.0F, 0.0F, -0.35F);
                }
                this.stymphalianBirdModel.resetToDefaultPose();
                setRotationAngles(this.stymphalianBirdModel.HeadBase, onWall ? (float) Math.toRadians(50.0) : 0.0F, 0.0F, 0.0F);
                this.stymphalianBirdModel.HeadBase.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                break;
            case TROLL:
                matrixStackIn.translate(0.0F, 1.0F, -0.35F);
                if (onWall) {
                    matrixStackIn.translate(0.0F, 0.0F, 0.35F);
                }
                this.trollModel.resetToDefaultPose();
                setRotationAngles(this.trollModel.head, onWall ? (float) Math.toRadians(50.0) : (float) Math.toRadians(-20.0), 0.0F, 0.0F);
                this.trollModel.head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                break;
            case AMPHITHERE:
                matrixStackIn.translate(0.0F, -0.2F, 0.7F);
                matrixStackIn.scale(2.0F, 2.0F, 2.0F);
                this.amphithereModel.resetToDefaultPose();
                setRotationAngles(this.amphithereModel.Head, onWall ? (float) Math.toRadians(50.0) : 0.0F, 0.0F, 0.0F);
                this.amphithereModel.Head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                break;
            case SEASERPENT:
                matrixStackIn.translate(0.0F, -0.35F, 0.8F);
                matrixStackIn.scale(2.5F, 2.5F, 2.5F);
                this.seaSerpentModel.resetToDefaultPose();
                setRotationAngles(this.seaSerpentModel.getCube("Head"), onWall ? (float) Math.toRadians(50.0) : 0.0F, 0.0F, 0.0F);
                this.seaSerpentModel.getCube("Head").render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                break;
            case HYDRA:
                matrixStackIn.translate(0.0F, -0.2F, -0.1F);
                matrixStackIn.scale(2.0F, 2.0F, 2.0F);
                this.hydraModel.resetToDefaultPose();
                setRotationAngles(this.hydraModel.Head1, onWall ? (float) Math.toRadians(50.0) : 0.0F, 0.0F, 0.0F);
                this.hydraModel.Head1.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @NotNull
    public ResourceLocation getTextureLocation(EntityMobSkull entity) {
        return this.getSkullTexture(entity.getSkullType());
    }

    public ResourceLocation getSkullTexture(EnumSkullType skull) {
        String s = "iceandfire:textures/models/skulls/skull_" + skull.name().toLowerCase(Locale.ROOT) + ".png";
        ResourceLocation resourcelocation = (ResourceLocation) SKULL_TEXTURE_CACHE.get(s);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s);
            SKULL_TEXTURE_CACHE.put(s, resourcelocation);
        }
        return resourcelocation;
    }
}