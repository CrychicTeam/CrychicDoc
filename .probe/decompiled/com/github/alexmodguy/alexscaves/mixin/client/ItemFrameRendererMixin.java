package com.github.alexmodguy.alexscaves.mixin.client;

import com.github.alexmodguy.alexscaves.client.render.misc.CaveMapRenderer;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.CaveMapItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ItemFrameRenderer.class })
public abstract class ItemFrameRendererMixin {

    private static final ModelResourceLocation MAP_FRAME_LOCATION = ModelResourceLocation.vanilla("item_frame", "map=true");

    private static final ModelResourceLocation GLOW_MAP_FRAME_LOCATION = ModelResourceLocation.vanilla("glow_item_frame", "map=true");

    @Shadow
    @Final
    private BlockRenderDispatcher blockRenderer;

    @Shadow
    protected abstract void renderNameTag(ItemFrame var1, Component var2, PoseStack var3, MultiBufferSource var4, int var5);

    @Shadow
    protected abstract boolean shouldShowName(ItemFrame var1);

    @Shadow
    public abstract Vec3 getRenderOffset(ItemFrame var1, float var2);

    @Inject(method = { "Lnet/minecraft/client/renderer/entity/ItemFrameRenderer;render(Lnet/minecraft/world/entity/decoration/ItemFrame;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V" }, remap = true, cancellable = true, at = { @At("HEAD") })
    private void ac_renderArmWithItem(ItemFrame entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        ItemStack itemstack = entity.getItem();
        if (itemstack.is(ACItemRegistry.CAVE_MAP.get()) && CaveMapItem.isFilled(itemstack)) {
            ci.cancel();
            RenderNameTagEvent renderNameTagEvent = new RenderNameTagEvent(entity, entity.m_5446_(), (ItemFrameRenderer) this, poseStack, bufferSource, packedLight, partialTicks);
            MinecraftForge.EVENT_BUS.post(renderNameTagEvent);
            if (renderNameTagEvent.getResult() != Result.DENY && (renderNameTagEvent.getResult() == Result.ALLOW || this.shouldShowName(entity))) {
                this.renderNameTag(entity, renderNameTagEvent.getContent(), poseStack, bufferSource, packedLight);
            }
            poseStack.pushPose();
            Direction direction = entity.m_6350_();
            Vec3 vec3 = this.getRenderOffset(entity, partialTicks);
            poseStack.translate(-vec3.x(), -vec3.y(), -vec3.z());
            poseStack.translate((double) direction.getStepX() * 0.46875, (double) direction.getStepY() * 0.46875, (double) direction.getStepZ() * 0.46875);
            poseStack.mulPose(Axis.XP.rotationDegrees(entity.m_146909_()));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entity.m_146908_()));
            if (!entity.m_20145_()) {
                ModelManager modelmanager = this.blockRenderer.getBlockModelShaper().getModelManager();
                ModelResourceLocation modelresourcelocation = entity.m_6095_() == EntityType.GLOW_ITEM_FRAME ? GLOW_MAP_FRAME_LOCATION : MAP_FRAME_LOCATION;
                poseStack.pushPose();
                poseStack.translate(-0.5F, -0.5F, -0.5F);
                this.blockRenderer.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), (BlockState) null, modelmanager.getModel(modelresourcelocation), 1.0F, 1.0F, 1.0F, packedLight, OverlayTexture.NO_OVERLAY);
                poseStack.popPose();
            }
            int j = entity.getRotation() % 4 * 2;
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) j * 360.0F / 8.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            float scale = 0.0078125F;
            poseStack.scale(scale, scale, scale);
            CaveMapRenderer.getMapFor(itemstack, false).render(poseStack, bufferSource, itemstack, true, packedLight);
            poseStack.popPose();
        }
    }
}