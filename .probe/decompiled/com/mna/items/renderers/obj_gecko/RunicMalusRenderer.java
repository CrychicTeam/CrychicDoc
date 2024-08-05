package com.mna.items.renderers.obj_gecko;

import com.mna.ManaAndArtifice;
import com.mna.api.tools.RLoc;
import com.mna.items.relic.ItemRunicMalus;
import com.mna.items.renderers.models.ModelRunicMalus;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class RunicMalusRenderer extends GeoItemRenderer<ItemRunicMalus> {

    public static final ResourceLocation hammer_model = RLoc.create("item/special/malus_hammer");

    protected Minecraft mc = Minecraft.getInstance();

    protected NonNullList<ItemStack> runes;

    public RunicMalusRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(new ModelRunicMalus());
    }

    public RenderType getRenderType(ItemRunicMalus animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.solid();
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext p_239207_2_, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int p_239207_6_) {
        this.runes = ItemRunicMalus.getRunes(itemStack);
        super.renderByItem(itemStack, p_239207_2_, matrixStack, bufferIn, combinedLightIn, p_239207_6_);
    }

    public void renderRecursively(PoseStack stack, ItemRunicMalus animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.mc != null) {
            stack.pushPose();
            RenderUtils.translateMatrixToBone(stack, bone);
            RenderUtils.translateToPivotPoint(stack, bone);
            RenderUtils.rotateMatrixAroundBone(stack, bone);
            RenderUtils.scaleMatrixForBone(stack, bone);
            if (!bone.isHidden()) {
                stack.pushPose();
                String var15 = bone.getName();
                switch(var15) {
                    case "ROOT":
                        ModelUtils.renderEntityModel(buffer, ManaAndArtifice.instance.proxy.getClientWorld(), hammer_model, stack, packedLight, OverlayTexture.NO_OVERLAY);
                        break;
                    case "RUNE_1":
                        if (this.runes.size() >= 1 && !this.runes.get(0).isEmpty()) {
                            stack.mulPose(Axis.YP.rotationDegrees(90.0F));
                            stack.scale(0.5F, 0.5F, 0.5F);
                            Minecraft.getInstance().getItemRenderer().renderStatic(this.runes.get(0), ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, this.mc.level, 0);
                        }
                        break;
                    case "RUNE_2":
                        if (this.runes.size() >= 1 && !this.runes.get(1).isEmpty()) {
                            stack.mulPose(Axis.YP.rotationDegrees(90.0F));
                            stack.scale(0.5F, 0.5F, 0.5F);
                            Minecraft.getInstance().getItemRenderer().renderStatic(this.runes.get(1), ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, this.mc.level, 0);
                        }
                        break;
                    case "RUNE_3":
                        if (this.runes.size() >= 2 && !this.runes.get(2).isEmpty()) {
                            stack.mulPose(Axis.YP.rotationDegrees(90.0F));
                            stack.scale(0.5F, 0.5F, 0.5F);
                            Minecraft.getInstance().getItemRenderer().renderStatic(this.runes.get(2), ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, this.mc.level, 0);
                        }
                        break;
                    case "RUNE_4":
                        if (this.runes.size() >= 3 && !this.runes.get(3).isEmpty()) {
                            stack.mulPose(Axis.YP.rotationDegrees(90.0F));
                            stack.scale(0.5F, 0.5F, 0.5F);
                            Minecraft.getInstance().getItemRenderer().renderStatic(this.runes.get(3), ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, this.mc.level, 0);
                        }
                        break;
                    case "RUNE_5":
                        if (this.runes.size() >= 4 && !this.runes.get(4).isEmpty()) {
                            stack.mulPose(Axis.YP.rotationDegrees(90.0F));
                            stack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
                            stack.scale(0.5F, 0.5F, 0.5F);
                            Minecraft.getInstance().getItemRenderer().renderStatic(this.runes.get(4), ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, this.mc.level, 0);
                        }
                        break;
                    case "RUNE_6":
                        if (this.runes.size() >= 5 && !this.runes.get(5).isEmpty()) {
                            stack.mulPose(Axis.YP.rotationDegrees(90.0F));
                            stack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
                            stack.scale(0.5F, 0.5F, 0.5F);
                            Minecraft.getInstance().getItemRenderer().renderStatic(this.runes.get(5), ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, this.mc.level, 0);
                        }
                }
                stack.popPose();
                for (GeoBone childBone : bone.getChildBones()) {
                    this.renderRecursively(stack, animatable, childBone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
                }
            }
            stack.popPose();
        }
    }
}