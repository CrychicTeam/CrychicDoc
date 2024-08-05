package net.minecraft.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Map;
import net.minecraft.client.model.Model;
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
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;

public class HangingSignRenderer extends SignRenderer {

    private static final String PLANK = "plank";

    private static final String V_CHAINS = "vChains";

    private static final String NORMAL_CHAINS = "normalChains";

    private static final String CHAIN_L_1 = "chainL1";

    private static final String CHAIN_L_2 = "chainL2";

    private static final String CHAIN_R_1 = "chainR1";

    private static final String CHAIN_R_2 = "chainR2";

    private static final String BOARD = "board";

    private static final float MODEL_RENDER_SCALE = 1.0F;

    private static final float TEXT_RENDER_SCALE = 0.9F;

    private static final Vec3 TEXT_OFFSET = new Vec3(0.0, -0.32F, 0.073F);

    private final Map<WoodType, HangingSignRenderer.HangingSignModel> hangingSignModels;

    public HangingSignRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        super(blockEntityRendererProviderContext0);
        this.hangingSignModels = (Map<WoodType, HangingSignRenderer.HangingSignModel>) WoodType.values().collect(ImmutableMap.toImmutableMap(p_249901_ -> p_249901_, p_251956_ -> new HangingSignRenderer.HangingSignModel(blockEntityRendererProviderContext0.bakeLayer(ModelLayers.createHangingSignModelName(p_251956_)))));
    }

    @Override
    public float getSignModelRenderScale() {
        return 1.0F;
    }

    @Override
    public float getSignTextRenderScale() {
        return 0.9F;
    }

    @Override
    public void render(SignBlockEntity signBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        BlockState $$6 = signBlockEntity0.m_58900_();
        SignBlock $$7 = (SignBlock) $$6.m_60734_();
        WoodType $$8 = SignBlock.getWoodType($$7);
        HangingSignRenderer.HangingSignModel $$9 = (HangingSignRenderer.HangingSignModel) this.hangingSignModels.get($$8);
        $$9.evaluateVisibleParts($$6);
        this.m_278756_(signBlockEntity0, poseStack2, multiBufferSource3, int4, int5, $$6, $$7, $$8, $$9);
    }

    @Override
    void translateSign(PoseStack poseStack0, float float1, BlockState blockState2) {
        poseStack0.translate(0.5, 0.9375, 0.5);
        poseStack0.mulPose(Axis.YP.rotationDegrees(float1));
        poseStack0.translate(0.0F, -0.3125F, 0.0F);
    }

    @Override
    void renderSignModel(PoseStack poseStack0, int int1, int int2, Model model3, VertexConsumer vertexConsumer4) {
        HangingSignRenderer.HangingSignModel $$5 = (HangingSignRenderer.HangingSignModel) model3;
        $$5.root.render(poseStack0, vertexConsumer4, int1, int2);
    }

    @Override
    Material getSignMaterial(WoodType woodType0) {
        return Sheets.getHangingSignMaterial(woodType0);
    }

    @Override
    Vec3 getTextOffset() {
        return TEXT_OFFSET;
    }

    public static LayerDefinition createHangingSignLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("board", CubeListBuilder.create().texOffs(0, 12).addBox(-7.0F, 0.0F, -1.0F, 14.0F, 10.0F, 2.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("plank", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -6.0F, -2.0F, 16.0F, 2.0F, 4.0F), PartPose.ZERO);
        PartDefinition $$2 = $$1.addOrReplaceChild("normalChains", CubeListBuilder.create(), PartPose.ZERO);
        $$2.addOrReplaceChild("chainL1", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F), PartPose.offsetAndRotation(-5.0F, -6.0F, 0.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        $$2.addOrReplaceChild("chainL2", CubeListBuilder.create().texOffs(6, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F), PartPose.offsetAndRotation(-5.0F, -6.0F, 0.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        $$2.addOrReplaceChild("chainR1", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F), PartPose.offsetAndRotation(5.0F, -6.0F, 0.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        $$2.addOrReplaceChild("chainR2", CubeListBuilder.create().texOffs(6, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F), PartPose.offsetAndRotation(5.0F, -6.0F, 0.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        $$1.addOrReplaceChild("vChains", CubeListBuilder.create().texOffs(14, 6).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 6.0F, 0.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 32);
    }

    public static final class HangingSignModel extends Model {

        public final ModelPart root;

        public final ModelPart plank;

        public final ModelPart vChains;

        public final ModelPart normalChains;

        public HangingSignModel(ModelPart modelPart0) {
            super(RenderType::m_110458_);
            this.root = modelPart0;
            this.plank = modelPart0.getChild("plank");
            this.normalChains = modelPart0.getChild("normalChains");
            this.vChains = modelPart0.getChild("vChains");
        }

        public void evaluateVisibleParts(BlockState blockState0) {
            boolean $$1 = !(blockState0.m_60734_() instanceof CeilingHangingSignBlock);
            this.plank.visible = $$1;
            this.vChains.visible = false;
            this.normalChains.visible = true;
            if (!$$1) {
                boolean $$2 = (Boolean) blockState0.m_61143_(BlockStateProperties.ATTACHED);
                this.normalChains.visible = !$$2;
                this.vChains.visible = $$2;
            }
        }

        @Override
        public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
            this.root.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
        }
    }
}