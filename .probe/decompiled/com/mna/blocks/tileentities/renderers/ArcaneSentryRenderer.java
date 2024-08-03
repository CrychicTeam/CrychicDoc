package com.mna.blocks.tileentities.renderers;

import com.mna.api.affinity.Affinity;
import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.ElementalSentryTile;
import com.mna.blocks.tileentities.models.FixedBookModel;
import com.mna.tools.math.MathUtils;
import com.mna.tools.math.Vector3;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;

public class ArcaneSentryRenderer implements BlockEntityRenderer<ElementalSentryTile> {

    public static final HashMap<Affinity, ResourceLocation> affinityMaterials = new HashMap();

    private final FixedBookModel modelBook;

    private final Minecraft mc;

    private final Vector3 TARGET_POSITION = new Vector3(0.5, 1.25, 0.5);

    private final Vector3 NO_TARGET_POSITION = new Vector3(0.5, 0.08, 0.7);

    public ArcaneSentryRenderer(BlockEntityRendererProvider.Context context) {
        this.modelBook = new FixedBookModel(context.bakeLayer(FixedBookModel.LAYER_LOCATION));
        this.mc = Minecraft.getInstance();
    }

    public void render(ElementalSentryTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float f = (float) tileEntityIn.ticks + partialTicks;
        Level world = tileEntityIn.m_58904_();
        BlockPos pos = tileEntityIn.m_58899_();
        BlockState state = tileEntityIn.m_58900_();
        float t = MathUtils.clamp01(((float) tileEntityIn.ticksSinceTargetChange + partialTicks) / 10.0F);
        matrixStackIn.pushPose();
        Vector3 vOffset = tileEntityIn.hasTarget() ? Vector3.lerp(this.NO_TARGET_POSITION, this.TARGET_POSITION, t) : Vector3.lerp(this.TARGET_POSITION, this.NO_TARGET_POSITION, t);
        matrixStackIn.translate(vOffset.x, vOffset.y, vOffset.z);
        matrixStackIn.translate(0.0, (double) (0.1F + Mth.sin(f * 0.1F) * 0.01F), 0.0);
        float f1 = tileEntityIn.nextPageAngle - tileEntityIn.pageAngle;
        while (f1 >= (float) Math.PI) {
            f1 -= (float) (Math.PI * 2);
        }
        while (f1 < (float) -Math.PI) {
            f1 += (float) (Math.PI * 2);
        }
        float f2 = (float) Math.toDegrees((double) (tileEntityIn.pageAngle + f1 * partialTicks));
        float zpRotation = tileEntityIn.hasTarget() ? MathUtils.lerpf(90.0F, 0.0F, t) : MathUtils.lerpf(0.0F, 90.0F, t);
        float ypRotation = tileEntityIn.hasTarget() ? MathUtils.lerpf(90.0F, -f2, t) : MathUtils.lerpf(-f2, 90.0F, t);
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(zpRotation));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(ypRotation));
        float f3 = Mth.lerp(partialTicks, tileEntityIn.oFlip, tileEntityIn.flip);
        float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = Mth.lerp(partialTicks, tileEntityIn.pageTurningSpeed, tileEntityIn.nextPageTurningSpeed);
        this.modelBook.setupAnim(f, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entitySolid((ResourceLocation) affinityMaterials.getOrDefault(tileEntityIn.getAffinity(), (ResourceLocation) affinityMaterials.get(Affinity.ARCANE))));
        this.modelBook.renderToBuffer(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 0.01F, 0.5F);
        if (tileEntityIn.hasTarget()) {
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
        }
        BakedModel model = this.mc.getModelManager().getModel(RunicAnvilRenderer.ring_large);
        ModelData data = model.getModelData(world, pos, state, ModelData.EMPTY);
        model.getRenderTypes(state, this.mc.level.f_46441_, data).forEach(rT -> {
            RenderType rType = RenderTypeHelper.getEntityRenderType(rT, false);
            VertexConsumer vertexBuilder = bufferIn.getBuffer(rType);
            for (BakedQuad quad : model.getQuads(null, null, this.mc.level.f_46441_, data, rType)) {
                vertexBuilder.putBulkData(matrixStackIn.last(), quad, 1.0F, 1.0F, 1.0F, 1.0F, combinedLightIn, combinedOverlayIn, true);
            }
        });
        matrixStackIn.popPose();
    }

    static {
        affinityMaterials.put(Affinity.ARCANE, RLoc.create("textures/block/artifice/sentry/sentry_arcane.png"));
        affinityMaterials.put(Affinity.ENDER, RLoc.create("textures/block/artifice/sentry/sentry_ender.png"));
        affinityMaterials.put(Affinity.EARTH, RLoc.create("textures/block/artifice/sentry/sentry_earth.png"));
        affinityMaterials.put(Affinity.FIRE, RLoc.create("textures/block/artifice/sentry/sentry_fire.png"));
        affinityMaterials.put(Affinity.WIND, RLoc.create("textures/block/artifice/sentry/sentry_wind.png"));
        affinityMaterials.put(Affinity.WATER, RLoc.create("textures/block/artifice/sentry/sentry_water.png"));
    }
}