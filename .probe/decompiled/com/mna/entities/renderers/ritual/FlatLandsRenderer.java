package com.mna.entities.renderers.ritual;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.models.FixedBookModel;
import com.mna.entities.rituals.FlatLands;
import com.mna.tools.math.MathUtils;
import com.mna.tools.math.Vector3;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FlatLandsRenderer extends EntityRenderer<FlatLands> {

    private final Vector3 TARGET_POSITION = new Vector3(0.5, 1.25, 0.5);

    private final ResourceLocation bookTexture = RLoc.create("textures/block/artifice/sentry/sentry_earth.png");

    private final FixedBookModel modelBook;

    public FlatLandsRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.modelBook = new FixedBookModel(context.bakeLayer(FixedBookModel.LAYER_LOCATION));
    }

    public ResourceLocation getTextureLocation(FlatLands entity) {
        return null;
    }

    public void render(FlatLands pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float f = (float) pEntity.ticks + pPartialTick;
        float t = MathUtils.clamp01(((float) pEntity.ticksSinceTargetChange + pPartialTick) / 10.0F);
        pPoseStack.pushPose();
        pPoseStack.translate(0.0, (double) (0.1F + Mth.sin(f * 0.1F) * 0.01F), 0.0);
        float f1 = pEntity.nextPageAngle - pEntity.pageAngle;
        while (f1 >= (float) Math.PI) {
            f1 -= (float) (Math.PI * 2);
        }
        while (f1 < (float) -Math.PI) {
            f1 += (float) (Math.PI * 2);
        }
        float f2 = (float) Math.toDegrees((double) (pEntity.pageAngle + f1 * pPartialTick));
        float zpRotation = MathUtils.lerpf(90.0F, 0.0F, t);
        float ypRotation = MathUtils.lerpf(90.0F, -f2, t);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(zpRotation));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(ypRotation));
        float f3 = Mth.lerp(pPartialTick, pEntity.oFlip, pEntity.flip);
        float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = Mth.lerp(pPartialTick, pEntity.pageTurningSpeed, pEntity.nextPageTurningSpeed);
        this.modelBook.setupAnim(f, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);
        VertexConsumer ivertexbuilder = pBuffer.getBuffer(RenderType.entitySolid(this.bookTexture));
        this.modelBook.renderToBuffer(pPoseStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
    }
}