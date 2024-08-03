package com.mna.entities.renderers.faction;

import com.mna.entities.faction.WitchHunter;
import com.mna.entities.models.faction.WitchHunterModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import software.bernie.geckolib.cache.object.GeoBone;

public class WitchHunterRenderer extends MAGeckoRenderer<WitchHunter> {

    private static final ItemStack crossbow = new ItemStack(Items.CROSSBOW);

    private final Minecraft mc = Minecraft.getInstance();

    public WitchHunterRenderer(EntityRendererProvider.Context context) {
        super(context, new WitchHunterModel());
    }

    public void render(WitchHunter entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.mulPose(Axis.YP.rotationDegrees(180.0F));
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    public void renderRecursively(PoseStack poseStack, WitchHunter animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("right_hand")) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(15.0F));
            poseStack.translate(0.14, -0.13, 0.8);
            poseStack.scale(0.7F, 0.7F, 0.7F);
            this.mc.getItemRenderer().renderStatic(crossbow, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, packedOverlay, poseStack, bufferSource, this.mc.level, 0);
            poseStack.popPose();
            bufferSource.getBuffer(renderType);
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}