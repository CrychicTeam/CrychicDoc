package org.embeddedt.modernfix.common.mixin.perf.faster_item_rendering;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.render.FastItemRenderType;
import org.embeddedt.modernfix.render.RenderState;
import org.embeddedt.modernfix.render.SimpleItemModelView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { ItemRenderer.class }, priority = 600)
@ClientOnlyMixin
public abstract class ItemRendererMixin {

    private ItemDisplayContext transformType;

    private final SimpleItemModelView modelView = new SimpleItemModelView();

    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void markRenderingType(ItemStack itemStack, ItemDisplayContext transformType, boolean leftHand, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        this.transformType = transformType;
    }

    @ModifyArg(method = { "render" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"), index = 0)
    private BakedModel useSimpleWrappedItemModel(BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack matrixStack, VertexConsumer buffer, @Local(ordinal = 0) BakedModel originalModel) {
        if (originalModel != null && originalModel.getClass() != SimpleBakedModel.class) {
            return model;
        } else if (!RenderState.IS_RENDERING_LEVEL && !stack.isEmpty() && model.getClass() == SimpleBakedModel.class && this.transformType == ItemDisplayContext.GUI) {
            ItemTransform transform = model.getTransforms().gui;
            FastItemRenderType type;
            if (transform == ItemTransform.NO_TRANSFORM) {
                type = FastItemRenderType.SIMPLE_ITEM;
            } else {
                if (!(stack.getItem() instanceof BlockItem) || !this.isBlockTransforms(transform)) {
                    return model;
                }
                type = FastItemRenderType.SIMPLE_BLOCK;
            }
            this.modelView.setItem(model);
            this.modelView.setType(type);
            return this.modelView;
        } else {
            return model;
        }
    }

    private boolean isBlockTransforms(ItemTransform transform) {
        return transform.rotation.x() == 30.0F && transform.rotation.y() == 225.0F && transform.rotation.z() == 0.0F;
    }
}