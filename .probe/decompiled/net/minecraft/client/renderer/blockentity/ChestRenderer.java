package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Calendar;
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
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class ChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {

    private static final String BOTTOM = "bottom";

    private static final String LID = "lid";

    private static final String LOCK = "lock";

    private final ModelPart lid;

    private final ModelPart bottom;

    private final ModelPart lock;

    private final ModelPart doubleLeftLid;

    private final ModelPart doubleLeftBottom;

    private final ModelPart doubleLeftLock;

    private final ModelPart doubleRightLid;

    private final ModelPart doubleRightBottom;

    private final ModelPart doubleRightLock;

    private boolean xmasTextures;

    public ChestRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        Calendar $$1 = Calendar.getInstance();
        if ($$1.get(2) + 1 == 12 && $$1.get(5) >= 24 && $$1.get(5) <= 26) {
            this.xmasTextures = true;
        }
        ModelPart $$2 = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.CHEST);
        this.bottom = $$2.getChild("bottom");
        this.lid = $$2.getChild("lid");
        this.lock = $$2.getChild("lock");
        ModelPart $$3 = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT);
        this.doubleLeftBottom = $$3.getChild("bottom");
        this.doubleLeftLid = $$3.getChild("lid");
        this.doubleLeftLock = $$3.getChild("lock");
        ModelPart $$4 = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT);
        this.doubleRightBottom = $$4.getChild("bottom");
        this.doubleRightLid = $$4.getChild("lid");
        this.doubleRightLock = $$4.getChild("lock");
    }

    public static LayerDefinition createSingleBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        $$1.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -2.0F, 14.0F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        return LayerDefinition.create($$0, 64, 64);
    }

    public static LayerDefinition createDoubleBodyRightLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        $$1.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(15.0F, -2.0F, 14.0F, 1.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        return LayerDefinition.create($$0, 64, 64);
    }

    public static LayerDefinition createDoubleBodyLeftLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(0.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        $$1.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.0F, 14.0F, 1.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        return LayerDefinition.create($$0, 64, 64);
    }

    @Override
    public void render(T t0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        Level $$6 = t0.getLevel();
        boolean $$7 = $$6 != null;
        BlockState $$8 = $$7 ? t0.getBlockState() : (BlockState) Blocks.CHEST.defaultBlockState().m_61124_(ChestBlock.FACING, Direction.SOUTH);
        ChestType $$9 = $$8.m_61138_(ChestBlock.TYPE) ? (ChestType) $$8.m_61143_(ChestBlock.TYPE) : ChestType.SINGLE;
        if ($$8.m_60734_() instanceof AbstractChestBlock<?> $$11) {
            boolean $$12 = $$9 != ChestType.SINGLE;
            poseStack2.pushPose();
            float $$13 = ((Direction) $$8.m_61143_(ChestBlock.FACING)).toYRot();
            poseStack2.translate(0.5F, 0.5F, 0.5F);
            poseStack2.mulPose(Axis.YP.rotationDegrees(-$$13));
            poseStack2.translate(-0.5F, -0.5F, -0.5F);
            DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> $$14;
            if ($$7) {
                $$14 = $$11.combine($$8, $$6, t0.getBlockPos(), true);
            } else {
                $$14 = DoubleBlockCombiner.Combiner::m_6502_;
            }
            float $$16 = $$14.apply(ChestBlock.opennessCombiner(t0)).get(float1);
            $$16 = 1.0F - $$16;
            $$16 = 1.0F - $$16 * $$16 * $$16;
            int $$17 = $$14.apply(new BrightnessCombiner<>()).applyAsInt(int4);
            Material $$18 = Sheets.chooseMaterial(t0, $$9, this.xmasTextures);
            VertexConsumer $$19 = $$18.buffer(multiBufferSource3, RenderType::m_110452_);
            if ($$12) {
                if ($$9 == ChestType.LEFT) {
                    this.render(poseStack2, $$19, this.doubleLeftLid, this.doubleLeftLock, this.doubleLeftBottom, $$16, $$17, int5);
                } else {
                    this.render(poseStack2, $$19, this.doubleRightLid, this.doubleRightLock, this.doubleRightBottom, $$16, $$17, int5);
                }
            } else {
                this.render(poseStack2, $$19, this.lid, this.lock, this.bottom, $$16, $$17, int5);
            }
            poseStack2.popPose();
        }
    }

    private void render(PoseStack poseStack0, VertexConsumer vertexConsumer1, ModelPart modelPart2, ModelPart modelPart3, ModelPart modelPart4, float float5, int int6, int int7) {
        modelPart2.xRot = -(float5 * (float) (Math.PI / 2));
        modelPart3.xRot = modelPart2.xRot;
        modelPart2.render(poseStack0, vertexConsumer1, int6, int7);
        modelPart3.render(poseStack0, vertexConsumer1, int6, int7);
        modelPart4.render(poseStack0, vertexConsumer1, int6, int7);
    }
}