package com.mna.entities.renderers.boss;

import com.mna.entities.boss.CouncilWarden;
import com.mna.entities.models.boss.CouncilWardenModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import com.mna.items.ItemInit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.util.RenderUtils;

public class CouncilWardenRenderer extends MAGeckoRenderer<CouncilWarden> {

    private static final ItemStack sword = new ItemStack(ItemInit.ASTRO_BLADE.get());

    private final Minecraft mc = Minecraft.getInstance();

    public CouncilWardenRenderer(EntityRendererProvider.Context context) {
        super(context, new CouncilWardenModel());
    }

    public void renderRecursively(PoseStack stack, CouncilWarden animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("right_grip_point")) {
            if (animatable.getWeaponState() == CouncilWarden.WeaponState.SWORD_IN_HAND) {
                stack.pushPose();
                RenderUtils.translateMatrixToBone(stack, bone);
                RenderUtils.translateToPivotPoint(stack, bone);
                RenderUtils.rotateMatrixAroundBone(stack, bone);
                stack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                stack.mulPose(Axis.YP.rotationDegrees(90.0F));
                RenderUtils.scaleMatrixForBone(stack, bone);
                Minecraft.getInstance().getItemRenderer().renderStatic(sword, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, packedLight, packedOverlay, stack, bufferSource, this.mc.level, 0);
                stack.popPose();
                bufferSource.getBuffer(renderType);
            }
        } else if (bone.getName().equals("back_anchor") && animatable.getWeaponState() == CouncilWarden.WeaponState.SWORD_ON_BACK) {
            stack.pushPose();
            stack.translate(0.4, 1.5, 0.25);
            stack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
            stack.mulPose(Axis.YP.rotationDegrees(90.0F));
            stack.mulPose(Axis.XP.rotationDegrees(170.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(sword, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, packedLight, packedOverlay, stack, bufferSource, this.mc.level, 0);
            stack.popPose();
            bufferSource.getBuffer(renderType);
        }
        super.renderRecursively(stack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    static {
        sword.getOrCreateTag().putBoolean("as_warden", true);
    }
}