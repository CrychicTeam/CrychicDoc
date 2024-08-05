package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import java.util.List;
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
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class BannerRenderer implements BlockEntityRenderer<BannerBlockEntity> {

    private static final int BANNER_WIDTH = 20;

    private static final int BANNER_HEIGHT = 40;

    private static final int MAX_PATTERNS = 16;

    public static final String FLAG = "flag";

    private static final String POLE = "pole";

    private static final String BAR = "bar";

    private final ModelPart flag;

    private final ModelPart pole;

    private final ModelPart bar;

    public BannerRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        ModelPart $$1 = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.BANNER);
        this.flag = $$1.getChild("flag");
        this.pole = $$1.getChild("pole");
        this.bar = $$1.getChild("bar");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("flag", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(44, 0).addBox(-1.0F, -30.0F, -1.0F, 2.0F, 42.0F, 2.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("bar", CubeListBuilder.create().texOffs(0, 42).addBox(-10.0F, -32.0F, -1.0F, 20.0F, 2.0F, 2.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 64);
    }

    public void render(BannerBlockEntity bannerBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        List<Pair<Holder<BannerPattern>, DyeColor>> $$6 = bannerBlockEntity0.getPatterns();
        float $$7 = 0.6666667F;
        boolean $$8 = bannerBlockEntity0.m_58904_() == null;
        poseStack2.pushPose();
        long $$9;
        if ($$8) {
            $$9 = 0L;
            poseStack2.translate(0.5F, 0.5F, 0.5F);
            this.pole.visible = true;
        } else {
            $$9 = bannerBlockEntity0.m_58904_().getGameTime();
            BlockState $$11 = bannerBlockEntity0.m_58900_();
            if ($$11.m_60734_() instanceof BannerBlock) {
                poseStack2.translate(0.5F, 0.5F, 0.5F);
                float $$12 = -RotationSegment.convertToDegrees((Integer) $$11.m_61143_(BannerBlock.ROTATION));
                poseStack2.mulPose(Axis.YP.rotationDegrees($$12));
                this.pole.visible = true;
            } else {
                poseStack2.translate(0.5F, -0.16666667F, 0.5F);
                float $$13 = -((Direction) $$11.m_61143_(WallBannerBlock.FACING)).toYRot();
                poseStack2.mulPose(Axis.YP.rotationDegrees($$13));
                poseStack2.translate(0.0F, -0.3125F, -0.4375F);
                this.pole.visible = false;
            }
        }
        poseStack2.pushPose();
        poseStack2.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer $$14 = ModelBakery.BANNER_BASE.buffer(multiBufferSource3, RenderType::m_110446_);
        this.pole.render(poseStack2, $$14, int4, int5);
        this.bar.render(poseStack2, $$14, int4, int5);
        BlockPos $$15 = bannerBlockEntity0.m_58899_();
        float $$16 = ((float) Math.floorMod((long) ($$15.m_123341_() * 7 + $$15.m_123342_() * 9 + $$15.m_123343_() * 13) + $$9, 100L) + float1) / 100.0F;
        this.flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * $$16)) * (float) Math.PI;
        this.flag.y = -32.0F;
        renderPatterns(poseStack2, multiBufferSource3, int4, int5, this.flag, ModelBakery.BANNER_BASE, true, $$6);
        poseStack2.popPose();
        poseStack2.popPose();
    }

    public static void renderPatterns(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, int int3, ModelPart modelPart4, Material material5, boolean boolean6, List<Pair<Holder<BannerPattern>, DyeColor>> listPairHolderBannerPatternDyeColor7) {
        renderPatterns(poseStack0, multiBufferSource1, int2, int3, modelPart4, material5, boolean6, listPairHolderBannerPatternDyeColor7, false);
    }

    public static void renderPatterns(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, int int3, ModelPart modelPart4, Material material5, boolean boolean6, List<Pair<Holder<BannerPattern>, DyeColor>> listPairHolderBannerPatternDyeColor7, boolean boolean8) {
        modelPart4.render(poseStack0, material5.buffer(multiBufferSource1, RenderType::m_110446_, boolean8), int2, int3);
        for (int $$9 = 0; $$9 < 17 && $$9 < listPairHolderBannerPatternDyeColor7.size(); $$9++) {
            Pair<Holder<BannerPattern>, DyeColor> $$10 = (Pair<Holder<BannerPattern>, DyeColor>) listPairHolderBannerPatternDyeColor7.get($$9);
            float[] $$11 = ((DyeColor) $$10.getSecond()).getTextureDiffuseColors();
            ((Holder) $$10.getFirst()).unwrapKey().map(p_234428_ -> boolean6 ? Sheets.getBannerMaterial(p_234428_) : Sheets.getShieldMaterial(p_234428_)).ifPresent(p_234425_ -> modelPart4.render(poseStack0, p_234425_.buffer(multiBufferSource1, RenderType::m_110482_), int2, int3, $$11[0], $$11[1], $$11[2], 1.0F));
        }
    }
}