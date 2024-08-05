package com.simibubi.create.content.equipment.extendoGrip;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({ Dist.CLIENT })
public class ExtendoGripRenderHandler {

    public static float mainHandAnimation;

    public static float lastMainHandAnimation;

    public static PartialModel pose = AllPartialModels.DEPLOYER_HAND_PUNCHING;

    public static void tick() {
        lastMainHandAnimation = mainHandAnimation;
        mainHandAnimation = mainHandAnimation * Mth.clamp(mainHandAnimation, 0.8F, 0.99F);
        pose = AllPartialModels.DEPLOYER_HAND_PUNCHING;
        if (AllItems.EXTENDO_GRIP.isIn(getRenderedOffHandStack())) {
            ItemStack main = getRenderedMainHandStack();
            if (!main.isEmpty()) {
                if (main.getItem() instanceof BlockItem) {
                    if (Minecraft.getInstance().getItemRenderer().getModel(main, null, null, 0).isGui3d()) {
                        pose = AllPartialModels.DEPLOYER_HAND_HOLDING;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerHand(RenderHandEvent event) {
        ItemStack heldItem = event.getItemStack();
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        boolean rightHand = event.getHand() == InteractionHand.MAIN_HAND ^ player.m_5737_() == HumanoidArm.LEFT;
        ItemStack offhandItem = getRenderedOffHandStack();
        boolean notInOffhand = !AllItems.EXTENDO_GRIP.isIn(offhandItem);
        if (!notInOffhand || AllItems.EXTENDO_GRIP.isIn(heldItem)) {
            PoseStack ms = event.getPoseStack();
            TransformStack msr = TransformStack.cast(ms);
            AbstractClientPlayer abstractclientplayerentity = mc.player;
            RenderSystem.setShaderTexture(0, abstractclientplayerentity.getSkinTextureLocation());
            float flip = rightHand ? 1.0F : -1.0F;
            float swingProgress = event.getSwingProgress();
            boolean blockItem = heldItem.getItem() instanceof BlockItem;
            float equipProgress = blockItem ? 0.0F : event.getEquipProgress() / 4.0F;
            ms.pushPose();
            if (event.getHand() == InteractionHand.MAIN_HAND) {
                if (1.0F - swingProgress > mainHandAnimation && swingProgress > 0.0F) {
                    mainHandAnimation = 0.95F;
                }
                float animation = Mth.lerp(AnimationTickHolder.getPartialTicks(), lastMainHandAnimation, mainHandAnimation);
                animation = animation * animation * animation;
                ms.translate(flip * 0.54F, -0.4F + equipProgress * -0.6F, -0.41999996F);
                ms.pushPose();
                msr.rotateY((double) (flip * 75.0F));
                ms.translate(flip * -1.0F, 3.6F, 3.5F);
                ((TransformStack) ((TransformStack) msr.rotateZ((double) (flip * 120.0F))).rotateX(200.0)).rotateY((double) (flip * -135.0F));
                ms.translate(flip * 5.6F, 0.0F, 0.0F);
                msr.rotateY((double) (flip * 40.0F));
                ms.translate(flip * 0.05F, -0.3F, -0.3F);
                PlayerRenderer playerrenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().<AbstractClientPlayer>getRenderer(player);
                if (rightHand) {
                    playerrenderer.renderRightHand(event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), player);
                } else {
                    playerrenderer.renderLeftHand(event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), player);
                }
                ms.popPose();
                ms.pushPose();
                ms.translate(flip * -0.1F, 0.0F, -0.3F);
                ItemInHandRenderer firstPersonRenderer = mc.getEntityRenderDispatcher().getItemInHandRenderer();
                ItemDisplayContext transform = rightHand ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
                firstPersonRenderer.renderItem(mc.player, notInOffhand ? heldItem : offhandItem, transform, !rightHand, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
                if (!notInOffhand) {
                    ForgeHooksClient.handleCameraTransforms(ms, mc.getItemRenderer().getModel(offhandItem, null, null, 0), transform, !rightHand);
                    ms.translate(flip * -0.05F, 0.15F, -1.2F);
                    ms.translate(0.0F, 0.0F, -animation * 2.25F);
                    if (blockItem && mc.getItemRenderer().getModel(heldItem, null, null, 0).isGui3d()) {
                        msr.rotateY((double) (flip * 45.0F));
                        ms.translate(flip * 0.15F, -0.15F, -0.05F);
                        ms.scale(1.25F, 1.25F, 1.25F);
                    }
                    firstPersonRenderer.renderItem(mc.player, heldItem, transform, !rightHand, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
                }
                ms.popPose();
            }
            ms.popPose();
            event.setCanceled(true);
        }
    }

    private static ItemStack getRenderedMainHandStack() {
        return Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().mainHandItem;
    }

    private static ItemStack getRenderedOffHandStack() {
        return Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().offHandItem;
    }
}