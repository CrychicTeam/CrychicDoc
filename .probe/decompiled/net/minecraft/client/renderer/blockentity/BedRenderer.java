package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class BedRenderer implements BlockEntityRenderer<BedBlockEntity> {

    private final ModelPart headRoot;

    private final ModelPart footRoot;

    public BedRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        this.headRoot = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.BED_HEAD);
        this.footRoot = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.BED_FOOT);
    }

    public static LayerDefinition createHeadLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(50, 6).addBox(0.0F, 6.0F, 0.0F, 3.0F, 3.0F, 3.0F), PartPose.rotation((float) (Math.PI / 2), 0.0F, (float) (Math.PI / 2)));
        $$1.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(50, 18).addBox(-16.0F, 6.0F, 0.0F, 3.0F, 3.0F, 3.0F), PartPose.rotation((float) (Math.PI / 2), 0.0F, (float) Math.PI));
        return LayerDefinition.create($$0, 64, 64);
    }

    public static LayerDefinition createFootLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(50, 0).addBox(0.0F, 6.0F, -16.0F, 3.0F, 3.0F, 3.0F), PartPose.rotation((float) (Math.PI / 2), 0.0F, 0.0F));
        $$1.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(50, 12).addBox(-16.0F, 6.0F, -16.0F, 3.0F, 3.0F, 3.0F), PartPose.rotation((float) (Math.PI / 2), 0.0F, (float) (Math.PI * 3.0 / 2.0)));
        return LayerDefinition.create($$0, 64, 64);
    }

    public void render(BedBlockEntity bedBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        Material $$6 = Sheets.BED_TEXTURES[bedBlockEntity0.getColor().getId()];
        Level $$7 = bedBlockEntity0.m_58904_();
        if ($$7 != null) {
            BlockState $$8 = bedBlockEntity0.m_58900_();
            DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> $$9 = DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, BedBlock::m_49559_, BedBlock::m_49557_, ChestBlock.FACING, $$8, $$7, bedBlockEntity0.m_58899_(), (p_112202_, p_112203_) -> false);
            int $$10 = $$9.apply(new BrightnessCombiner<>()).get(int4);
            this.renderPiece(poseStack2, multiBufferSource3, $$8.m_61143_(BedBlock.PART) == BedPart.HEAD ? this.headRoot : this.footRoot, (Direction) $$8.m_61143_(BedBlock.f_54117_), $$6, $$10, int5, false);
        } else {
            this.renderPiece(poseStack2, multiBufferSource3, this.headRoot, Direction.SOUTH, $$6, int4, int5, false);
            this.renderPiece(poseStack2, multiBufferSource3, this.footRoot, Direction.SOUTH, $$6, int4, int5, true);
        }
    }

    private void renderPiece(PoseStack poseStack0, MultiBufferSource multiBufferSource1, ModelPart modelPart2, Direction direction3, Material material4, int int5, int int6, boolean boolean7) {
        poseStack0.pushPose();
        poseStack0.translate(0.0F, 0.5625F, boolean7 ? -1.0F : 0.0F);
        poseStack0.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack0.translate(0.5F, 0.5F, 0.5F);
        poseStack0.mulPose(Axis.ZP.rotationDegrees(180.0F + direction3.toYRot()));
        poseStack0.translate(-0.5F, -0.5F, -0.5F);
        VertexConsumer $$8 = material4.buffer(multiBufferSource1, RenderType::m_110446_);
        modelPart2.render(poseStack0, $$8, int5, int6);
        poseStack0.popPose();
    }
}