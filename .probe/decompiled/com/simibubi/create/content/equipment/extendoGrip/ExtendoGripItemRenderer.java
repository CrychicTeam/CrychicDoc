package com.simibubi.create.content.equipment.extendoGrip;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ExtendoGripItemRenderer extends CustomRenderedItemModelRenderer {

    protected static final PartialModel COG = new PartialModel(Create.asResource("item/extendo_grip/cog"));

    protected static final PartialModel THIN_SHORT = new PartialModel(Create.asResource("item/extendo_grip/thin_short"));

    protected static final PartialModel WIDE_SHORT = new PartialModel(Create.asResource("item/extendo_grip/wide_short"));

    protected static final PartialModel THIN_LONG = new PartialModel(Create.asResource("item/extendo_grip/thin_long"));

    protected static final PartialModel WIDE_LONG = new PartialModel(Create.asResource("item/extendo_grip/wide_long"));

    private static final Vec3 ROTATION_OFFSET = new Vec3(0.0, 0.5, 0.5);

    private static final Vec3 COG_ROTATION_OFFSET = new Vec3(0.0, 0.0625, 0.0);

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        TransformStack stacker = TransformStack.cast(ms);
        float animation = 0.25F;
        boolean leftHand = transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        boolean rightHand = transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
        if (leftHand || rightHand) {
            animation = Mth.lerp(AnimationTickHolder.getPartialTicks(), ExtendoGripRenderHandler.lastMainHandAnimation, ExtendoGripRenderHandler.mainHandAnimation);
        }
        animation = animation * animation * animation;
        float extensionAngle = Mth.lerp(animation, 24.0F, 156.0F);
        float halfAngle = extensionAngle / 2.0F;
        float oppositeAngle = 180.0F - extensionAngle;
        renderer.renderSolid(model.getOriginalModel(), light);
        ms.pushPose();
        ms.translate(0.0F, 0.0625F, -0.4375F);
        ms.scale(1.0F, 1.0F, 1.0F + animation);
        ms.pushPose();
        ((TransformStack) stacker.rotateX((double) (-halfAngle))).translate(ROTATION_OFFSET);
        renderer.renderSolid(THIN_SHORT.get(), light);
        stacker.translateBack(ROTATION_OFFSET);
        ms.translate(0.0F, 0.34375F, 0.0F);
        ((TransformStack) stacker.rotateX((double) (-oppositeAngle))).translate(ROTATION_OFFSET);
        renderer.renderSolid(WIDE_LONG.get(), light);
        stacker.translateBack(ROTATION_OFFSET);
        ms.translate(0.0F, 0.6875F, 0.0F);
        ((TransformStack) stacker.rotateX((double) oppositeAngle)).translate(ROTATION_OFFSET);
        ms.translate(0.0F, 0.03125F, 0.0F);
        renderer.renderSolid(THIN_SHORT.get(), light);
        stacker.translateBack(ROTATION_OFFSET);
        ms.popPose();
        ms.pushPose();
        ((TransformStack) stacker.rotateX((double) (-180.0F + halfAngle))).translate(ROTATION_OFFSET);
        renderer.renderSolid(WIDE_SHORT.get(), light);
        stacker.translateBack(ROTATION_OFFSET);
        ms.translate(0.0F, 0.34375F, 0.0F);
        ((TransformStack) stacker.rotateX((double) oppositeAngle)).translate(ROTATION_OFFSET);
        renderer.renderSolid(THIN_LONG.get(), light);
        stacker.translateBack(ROTATION_OFFSET);
        ms.translate(0.0F, 0.6875F, 0.0F);
        ((TransformStack) stacker.rotateX((double) (-oppositeAngle))).translate(ROTATION_OFFSET);
        ms.translate(0.0F, 0.03125F, 0.0F);
        renderer.renderSolid(WIDE_SHORT.get(), light);
        stacker.translateBack(ROTATION_OFFSET);
        ms.translate(0.0F, 0.34375F, 0.0F);
        ((TransformStack) stacker.rotateX((double) (180.0F - halfAngle))).rotateY(180.0);
        ms.translate(0.0F, 0.0F, -0.25F);
        ms.scale(1.0F, 1.0F, 1.0F / (1.0F + animation));
        renderer.renderSolid(!leftHand && !rightHand ? AllPartialModels.DEPLOYER_HAND_POINTING.get() : ExtendoGripRenderHandler.pose.get(), light);
        ms.popPose();
        ms.popPose();
        ms.pushPose();
        float angle = AnimationTickHolder.getRenderTime() * -2.0F;
        if (leftHand || rightHand) {
            angle += 360.0F * animation;
        }
        angle %= 360.0F;
        ((TransformStack) ((TransformStack) stacker.translate(COG_ROTATION_OFFSET)).rotateZ((double) angle)).translateBack(COG_ROTATION_OFFSET);
        renderer.renderSolid(COG.get(), light);
        ms.popPose();
    }
}