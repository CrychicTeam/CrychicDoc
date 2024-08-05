package net.minecraft.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;

public class SignRenderer implements BlockEntityRenderer<SignBlockEntity> {

    private static final String STICK = "stick";

    private static final int BLACK_TEXT_OUTLINE_COLOR = -988212;

    private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);

    private static final float RENDER_SCALE = 0.6666667F;

    private static final Vec3 TEXT_OFFSET = new Vec3(0.0, 0.33333334F, 0.046666667F);

    private final Map<WoodType, SignRenderer.SignModel> signModels;

    private final Font font;

    public SignRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        this.signModels = (Map<WoodType, SignRenderer.SignModel>) WoodType.values().collect(ImmutableMap.toImmutableMap(p_173645_ -> p_173645_, p_173651_ -> new SignRenderer.SignModel(blockEntityRendererProviderContext0.bakeLayer(ModelLayers.createSignModelName(p_173651_)))));
        this.font = blockEntityRendererProviderContext0.getFont();
    }

    public void render(SignBlockEntity signBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        BlockState $$6 = signBlockEntity0.m_58900_();
        SignBlock $$7 = (SignBlock) $$6.m_60734_();
        WoodType $$8 = SignBlock.getWoodType($$7);
        SignRenderer.SignModel $$9 = (SignRenderer.SignModel) this.signModels.get($$8);
        $$9.stick.visible = $$6.m_60734_() instanceof StandingSignBlock;
        this.renderSignWithText(signBlockEntity0, poseStack2, multiBufferSource3, int4, int5, $$6, $$7, $$8, $$9);
    }

    public float getSignModelRenderScale() {
        return 0.6666667F;
    }

    public float getSignTextRenderScale() {
        return 0.6666667F;
    }

    void renderSignWithText(SignBlockEntity signBlockEntity0, PoseStack poseStack1, MultiBufferSource multiBufferSource2, int int3, int int4, BlockState blockState5, SignBlock signBlock6, WoodType woodType7, Model model8) {
        poseStack1.pushPose();
        this.translateSign(poseStack1, -signBlock6.getYRotationDegrees(blockState5), blockState5);
        this.renderSign(poseStack1, multiBufferSource2, int3, int4, woodType7, model8);
        this.renderSignText(signBlockEntity0.m_58899_(), signBlockEntity0.getFrontText(), poseStack1, multiBufferSource2, int3, signBlockEntity0.getTextLineHeight(), signBlockEntity0.getMaxTextLineWidth(), true);
        this.renderSignText(signBlockEntity0.m_58899_(), signBlockEntity0.getBackText(), poseStack1, multiBufferSource2, int3, signBlockEntity0.getTextLineHeight(), signBlockEntity0.getMaxTextLineWidth(), false);
        poseStack1.popPose();
    }

    void translateSign(PoseStack poseStack0, float float1, BlockState blockState2) {
        poseStack0.translate(0.5F, 0.75F * this.getSignModelRenderScale(), 0.5F);
        poseStack0.mulPose(Axis.YP.rotationDegrees(float1));
        if (!(blockState2.m_60734_() instanceof StandingSignBlock)) {
            poseStack0.translate(0.0F, -0.3125F, -0.4375F);
        }
    }

    void renderSign(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, int int3, WoodType woodType4, Model model5) {
        poseStack0.pushPose();
        float $$6 = this.getSignModelRenderScale();
        poseStack0.scale($$6, -$$6, -$$6);
        Material $$7 = this.getSignMaterial(woodType4);
        VertexConsumer $$8 = $$7.buffer(multiBufferSource1, model5::m_103119_);
        this.renderSignModel(poseStack0, int2, int3, model5, $$8);
        poseStack0.popPose();
    }

    void renderSignModel(PoseStack poseStack0, int int1, int int2, Model model3, VertexConsumer vertexConsumer4) {
        SignRenderer.SignModel $$5 = (SignRenderer.SignModel) model3;
        $$5.root.render(poseStack0, vertexConsumer4, int1, int2);
    }

    Material getSignMaterial(WoodType woodType0) {
        return Sheets.getSignMaterial(woodType0);
    }

    void renderSignText(BlockPos blockPos0, SignText signText1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5, int int6, boolean boolean7) {
        poseStack2.pushPose();
        this.translateSignText(poseStack2, boolean7, this.getTextOffset());
        int $$8 = getDarkColor(signText1);
        int $$9 = 4 * int5 / 2;
        FormattedCharSequence[] $$10 = signText1.getRenderMessages(Minecraft.getInstance().isTextFilteringEnabled(), p_277227_ -> {
            List<FormattedCharSequence> $$2 = this.font.split(p_277227_, int6);
            return $$2.isEmpty() ? FormattedCharSequence.EMPTY : (FormattedCharSequence) $$2.get(0);
        });
        int $$11;
        boolean $$12;
        int $$13;
        if (signText1.hasGlowingText()) {
            $$11 = signText1.getColor().getTextColor();
            $$12 = isOutlineVisible(blockPos0, $$11);
            $$13 = 15728880;
        } else {
            $$11 = $$8;
            $$12 = false;
            $$13 = int4;
        }
        for (int $$17 = 0; $$17 < 4; $$17++) {
            FormattedCharSequence $$18 = $$10[$$17];
            float $$19 = (float) (-this.font.width($$18) / 2);
            if ($$12) {
                this.font.drawInBatch8xOutline($$18, $$19, (float) ($$17 * int5 - $$9), $$11, $$8, poseStack2.last().pose(), multiBufferSource3, $$13);
            } else {
                this.font.drawInBatch($$18, $$19, (float) ($$17 * int5 - $$9), $$11, false, poseStack2.last().pose(), multiBufferSource3, Font.DisplayMode.POLYGON_OFFSET, 0, $$13);
            }
        }
        poseStack2.popPose();
    }

    private void translateSignText(PoseStack poseStack0, boolean boolean1, Vec3 vec2) {
        if (!boolean1) {
            poseStack0.mulPose(Axis.YP.rotationDegrees(180.0F));
        }
        float $$3 = 0.015625F * this.getSignTextRenderScale();
        poseStack0.translate(vec2.x, vec2.y, vec2.z);
        poseStack0.scale($$3, -$$3, $$3);
    }

    Vec3 getTextOffset() {
        return TEXT_OFFSET;
    }

    static boolean isOutlineVisible(BlockPos blockPos0, int int1) {
        if (int1 == DyeColor.BLACK.getTextColor()) {
            return true;
        } else {
            Minecraft $$2 = Minecraft.getInstance();
            LocalPlayer $$3 = $$2.player;
            if ($$3 != null && $$2.options.getCameraType().isFirstPerson() && $$3.m_150108_()) {
                return true;
            } else {
                Entity $$4 = $$2.getCameraEntity();
                return $$4 != null && $$4.distanceToSqr(Vec3.atCenterOf(blockPos0)) < (double) OUTLINE_RENDER_DISTANCE;
            }
        }
    }

    static int getDarkColor(SignText signText0) {
        int $$1 = signText0.getColor().getTextColor();
        if ($$1 == DyeColor.BLACK.getTextColor() && signText0.hasGlowingText()) {
            return -988212;
        } else {
            double $$2 = 0.4;
            int $$3 = (int) ((double) FastColor.ARGB32.red($$1) * 0.4);
            int $$4 = (int) ((double) FastColor.ARGB32.green($$1) * 0.4);
            int $$5 = (int) ((double) FastColor.ARGB32.blue($$1) * 0.4);
            return FastColor.ARGB32.color(0, $$3, $$4, $$5);
        }
    }

    public static SignRenderer.SignModel createSignModel(EntityModelSet entityModelSet0, WoodType woodType1) {
        return new SignRenderer.SignModel(entityModelSet0.bakeLayer(ModelLayers.createSignModelName(woodType1)));
    }

    public static LayerDefinition createSignLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("sign", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 32);
    }

    public static final class SignModel extends Model {

        public final ModelPart root;

        public final ModelPart stick;

        public SignModel(ModelPart modelPart0) {
            super(RenderType::m_110458_);
            this.root = modelPart0;
            this.stick = modelPart0.getChild("stick");
        }

        @Override
        public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
            this.root.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
        }
    }
}