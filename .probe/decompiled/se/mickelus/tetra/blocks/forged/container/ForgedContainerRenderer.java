package se.mickelus.tetra.blocks.forged.container;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class ForgedContainerRenderer implements BlockEntityRenderer<ForgedContainerBlockEntity> {

    public static final Material material = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("tetra", "block/forged_container/forged_container"));

    private static final float openDuration = 300.0F;

    public static ModelLayerLocation layer = new ModelLayerLocation(new ResourceLocation("tetra", "forged_container"), "main");

    public ModelPart lid;

    public ModelPart base;

    public ModelPart[] locks;

    public ForgedContainerRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelpart = context.bakeLayer(layer);
        this.lid = modelpart.getChild("lid");
        this.locks = new ModelPart[4];
        for (int i = 0; i < 4; i++) {
            this.locks[i] = modelpart.getChild("locks" + i);
        }
        this.base = modelpart.getChild("base");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition parts = mesh.getRoot();
        parts.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 4.0F, 1.0F, 30.0F, 3.0F, 14.0F), PartPose.ZERO);
        for (int i = 0; i < 4; i++) {
            parts.addOrReplaceChild("locks" + i, CubeListBuilder.create().texOffs(0, 0).addBox((float) (6 + i * 6), 6.0F, 0.97F, 2.0F, 3.0F, 1.0F), PartPose.ZERO);
        }
        parts.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 17).addBox(1.0F, 7.0F, 1.0F, 30.0F, 9.0F, 14.0F), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 64);
    }

    public void render(ForgedContainerBlockEntity tile, float partialTicks, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int combinedLight, int combinedOverlay) {
        if (!tile.isFlipped()) {
            if (tile.m_58898_()) {
                matrixStack.pushPose();
                matrixStack.translate(0.5F, 0.5F, 0.5F);
                matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                matrixStack.mulPose(Axis.YP.rotationDegrees(tile.getFacing().toYRot()));
                matrixStack.translate(-0.5F, -0.5F, -0.5F);
                VertexConsumer vertexBuilder = material.buffer(renderTypeBuffer, RenderType::m_110446_);
                this.renderLid(tile, partialTicks, matrixStack, vertexBuilder, combinedLight, combinedOverlay);
                this.renderLocks(tile, partialTicks, matrixStack, vertexBuilder, combinedLight, combinedOverlay);
                this.base.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
                matrixStack.popPose();
            }
        }
    }

    private void renderLid(ForgedContainerBlockEntity tile, float partialTicks, PoseStack matrixStack, VertexConsumer vertexBuilder, int combinedLight, int combinedOverlay) {
        if (tile.isOpen()) {
            float progress = Math.min(1.0F, (float) (System.currentTimeMillis() - tile.openTime) / 300.0F);
            this.lid.yRot = progress * 0.1F * (float) (Math.PI / 2);
            matrixStack.translate(0.0F, 0.0F, 0.3F * progress);
            this.lid.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
            matrixStack.translate(0.0F, 0.0F, -0.3F * progress);
        } else {
            this.lid.yRot = 0.0F;
            this.lid.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
        }
    }

    private void renderLocks(ForgedContainerBlockEntity tile, float partialTicks, PoseStack matrixStack, VertexConsumer vertexBuilder, int combinedLight, int combinedOverlay) {
        Boolean[] locked = tile.isLocked();
        for (int i = 0; i < this.locks.length; i++) {
            if (locked[i]) {
                this.locks[i].render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
            }
        }
    }
}