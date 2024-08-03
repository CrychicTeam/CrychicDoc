package net.mehvahdjukaar.amendments.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.dragon.DragonHeadModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ SkullBlockRenderer.class })
public class SkullBlockRendererMixin {

    @Unique
    private static final ResourceLocation DRAGON_EYES = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");

    @Inject(method = { "renderSkull" }, at = { @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/model/SkullModelBase;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V") })
    private static void amendments$addDragonEyes(Direction direction, float yRot, float mouthAnimation, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SkullModelBase model, RenderType renderType, CallbackInfo ci) {
        if (model instanceof DragonHeadModel) {
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(DRAGON_EYES));
            poseStack.pushPose();
            model.m_7695_(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }

    @WrapOperation(method = { "getRenderType" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entityCutoutNoCullZOffset(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;") })
    private static RenderType amendments$overrideRenderType(ResourceLocation location, Operation<RenderType> original, SkullBlock.Type skullType, @Nullable GameProfile gameProfile) {
        return skullType == SkullBlock.Types.DRAGON ? RenderType.entityCutoutNoCull(location) : (RenderType) original.call(new Object[] { location });
    }
}