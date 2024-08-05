package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ConduitRenderer implements BlockEntityRenderer<ConduitBlockEntity> {

    public static final Material SHELL_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/base"));

    public static final Material ACTIVE_SHELL_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/cage"));

    public static final Material WIND_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/wind"));

    public static final Material VERTICAL_WIND_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/wind_vertical"));

    public static final Material OPEN_EYE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/open_eye"));

    public static final Material CLOSED_EYE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/closed_eye"));

    private final ModelPart eye;

    private final ModelPart wind;

    private final ModelPart shell;

    private final ModelPart cage;

    private final BlockEntityRenderDispatcher renderer;

    public ConduitRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        this.renderer = blockEntityRendererProviderContext0.getBlockEntityRenderDispatcher();
        this.eye = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.CONDUIT_EYE);
        this.wind = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.CONDUIT_WIND);
        this.shell = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.CONDUIT_SHELL);
        this.cage = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.CONDUIT_CAGE);
    }

    public static LayerDefinition createEyeLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.ZERO);
        return LayerDefinition.create($$0, 16, 16);
    }

    public static LayerDefinition createWindLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("wind", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 32);
    }

    public static LayerDefinition createShellLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 32, 16);
    }

    public static LayerDefinition createCageLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 32, 16);
    }

    public void render(ConduitBlockEntity conduitBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        float $$6 = (float) conduitBlockEntity0.tickCount + float1;
        if (!conduitBlockEntity0.isActive()) {
            float $$7 = conduitBlockEntity0.getActiveRotation(0.0F);
            VertexConsumer $$8 = SHELL_TEXTURE.buffer(multiBufferSource3, RenderType::m_110446_);
            poseStack2.pushPose();
            poseStack2.translate(0.5F, 0.5F, 0.5F);
            poseStack2.mulPose(new Quaternionf().rotationY($$7 * (float) (Math.PI / 180.0)));
            this.shell.render(poseStack2, $$8, int4, int5);
            poseStack2.popPose();
        } else {
            float $$9 = conduitBlockEntity0.getActiveRotation(float1) * (180.0F / (float) Math.PI);
            float $$10 = Mth.sin($$6 * 0.1F) / 2.0F + 0.5F;
            $$10 = $$10 * $$10 + $$10;
            poseStack2.pushPose();
            poseStack2.translate(0.5F, 0.3F + $$10 * 0.2F, 0.5F);
            Vector3f $$11 = new Vector3f(0.5F, 1.0F, 0.5F).normalize();
            poseStack2.mulPose(new Quaternionf().rotationAxis($$9 * (float) (Math.PI / 180.0), $$11));
            this.cage.render(poseStack2, ACTIVE_SHELL_TEXTURE.buffer(multiBufferSource3, RenderType::m_110458_), int4, int5);
            poseStack2.popPose();
            int $$12 = conduitBlockEntity0.tickCount / 66 % 3;
            poseStack2.pushPose();
            poseStack2.translate(0.5F, 0.5F, 0.5F);
            if ($$12 == 1) {
                poseStack2.mulPose(new Quaternionf().rotationX((float) (Math.PI / 2)));
            } else if ($$12 == 2) {
                poseStack2.mulPose(new Quaternionf().rotationZ((float) (Math.PI / 2)));
            }
            VertexConsumer $$13 = ($$12 == 1 ? VERTICAL_WIND_TEXTURE : WIND_TEXTURE).buffer(multiBufferSource3, RenderType::m_110458_);
            this.wind.render(poseStack2, $$13, int4, int5);
            poseStack2.popPose();
            poseStack2.pushPose();
            poseStack2.translate(0.5F, 0.5F, 0.5F);
            poseStack2.scale(0.875F, 0.875F, 0.875F);
            poseStack2.mulPose(new Quaternionf().rotationXYZ((float) Math.PI, 0.0F, (float) Math.PI));
            this.wind.render(poseStack2, $$13, int4, int5);
            poseStack2.popPose();
            Camera $$14 = this.renderer.camera;
            poseStack2.pushPose();
            poseStack2.translate(0.5F, 0.3F + $$10 * 0.2F, 0.5F);
            poseStack2.scale(0.5F, 0.5F, 0.5F);
            float $$15 = -$$14.getYRot();
            poseStack2.mulPose(new Quaternionf().rotationYXZ($$15 * (float) (Math.PI / 180.0), $$14.getXRot() * (float) (Math.PI / 180.0), (float) Math.PI));
            float $$16 = 1.3333334F;
            poseStack2.scale(1.3333334F, 1.3333334F, 1.3333334F);
            this.eye.render(poseStack2, (conduitBlockEntity0.isHunting() ? OPEN_EYE_TEXTURE : CLOSED_EYE_TEXTURE).buffer(multiBufferSource3, RenderType::m_110458_), int4, int5);
            poseStack2.popPose();
        }
    }
}