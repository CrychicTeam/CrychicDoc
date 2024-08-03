package se.mickelus.tetra.blocks.forged.hammer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
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
public class HammerBaseRenderer implements BlockEntityRenderer<HammerBaseBlockEntity> {

    public static final Material material = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("tetra", "block/forged_hammer/base_sheet"));

    public static ModelLayerLocation layer = new ModelLayerLocation(new ResourceLocation("tetra", "hammer_base"), "main");

    private final ModelPart unpowered;

    private final ModelPart powered;

    private final ModelPart[] modulesA;

    private final ModelPart[] modulesB;

    private final ModelPart cellAunpowered;

    private final ModelPart cellBunpowered;

    private final ModelPart cellApowered;

    private final ModelPart cellBpowered;

    public HammerBaseRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelpart = context.bakeLayer(layer);
        this.unpowered = modelpart.getChild("unpowered");
        this.powered = modelpart.getChild("powered");
        HammerEffect[] effects = HammerEffect.values();
        this.modulesA = new ModelPart[effects.length];
        this.modulesB = new ModelPart[effects.length];
        for (int i = 0; i < effects.length; i++) {
            this.modulesA[i] = modelpart.getChild("moduleA" + i);
            this.modulesB[i] = modelpart.getChild("moduleB" + i);
        }
        this.cellAunpowered = modelpart.getChild("cellAunpowered");
        this.cellBunpowered = modelpart.getChild("cellBunpowered");
        this.cellApowered = modelpart.getChild("cellApowered");
        this.cellBpowered = modelpart.getChild("cellBpowered");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition parts = mesh.getRoot();
        parts.addOrReplaceChild("unpowered", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
        parts.addOrReplaceChild("powered", CubeListBuilder.create().texOffs(64, 0).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
        HammerEffect[] effects = HammerEffect.values();
        for (int i = 0; i < effects.length; i++) {
            parts.addOrReplaceChild("moduleA" + i, CubeListBuilder.create().texOffs(i * 16, 32).addBox(0.0F, 0.0F, -16.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 2), 0.0F));
            parts.addOrReplaceChild("moduleB" + i, CubeListBuilder.create().texOffs(i * 16, 32).addBox(-16.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 2), 0.0F));
        }
        parts.addOrReplaceChild("cellAunpowered", CubeListBuilder.create().texOffs(48, 0).addBox(5.5F, -19.0F, 5.5F, 5.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) (-Math.PI / 2), 0.0F, 0.0F));
        parts.addOrReplaceChild("cellApowered", CubeListBuilder.create().texOffs(48, 8).addBox(5.5F, -19.0F, 5.5F, 5.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) (-Math.PI / 2), 0.0F, 0.0F));
        parts.addOrReplaceChild("cellBunpowered", CubeListBuilder.create().texOffs(48, 0).addBox(5.5F, -3.0F, -10.5F, 5.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        parts.addOrReplaceChild("cellBpowered", CubeListBuilder.create().texOffs(48, 8).addBox(5.5F, -3.0F, -10.5F, 5.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 64);
    }

    public void render(HammerBaseBlockEntity tile, float v, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (tile.m_58898_()) {
            matrixStack.pushPose();
            matrixStack.translate(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            matrixStack.mulPose(Axis.YP.rotationDegrees(tile.getFacing().toYRot()));
            matrixStack.translate(-0.5F, -0.5F, -0.5F);
            VertexConsumer vertexBuilder = material.buffer(buffer, RenderType::m_110452_);
            if (tile.isFunctional()) {
                this.powered.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
            } else {
                this.unpowered.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
            }
            if (tile.hasCellInSlot(0)) {
                if (tile.getCellFuel(0) > 0) {
                    this.cellApowered.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
                } else {
                    this.cellAunpowered.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
                }
            }
            if (tile.hasCellInSlot(1)) {
                if (tile.getCellFuel(1) > 0) {
                    this.cellBpowered.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
                } else {
                    this.cellBunpowered.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
                }
            }
            if (tile.getEffect(true) != null) {
                this.modulesA[tile.getEffect(true).ordinal()].render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
            }
            if (tile.getEffect(false) != null) {
                this.modulesB[tile.getEffect(false).ordinal()].render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
            }
            matrixStack.popPose();
        }
    }
}