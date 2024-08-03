package com.simibubi.create.content.equipment.zapper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public abstract class ShootableGadgetRenderHandler {

    protected float leftHandAnimation;

    protected float rightHandAnimation;

    protected float lastLeftHandAnimation;

    protected float lastRightHandAnimation;

    protected boolean dontReequipLeft;

    protected boolean dontReequipRight;

    public void tick() {
        this.lastLeftHandAnimation = this.leftHandAnimation;
        this.lastRightHandAnimation = this.rightHandAnimation;
        this.leftHandAnimation = this.leftHandAnimation * this.animationDecay();
        this.rightHandAnimation = this.rightHandAnimation * this.animationDecay();
    }

    public float getAnimation(boolean rightHand, float partialTicks) {
        return Mth.lerp(partialTicks, rightHand ? this.lastRightHandAnimation : this.lastLeftHandAnimation, rightHand ? this.rightHandAnimation : this.leftHandAnimation);
    }

    protected float animationDecay() {
        return 0.8F;
    }

    public void shoot(InteractionHand hand, Vec3 location) {
        LocalPlayer player = Minecraft.getInstance().player;
        boolean rightHand = hand == InteractionHand.MAIN_HAND ^ player.m_5737_() == HumanoidArm.LEFT;
        if (rightHand) {
            this.rightHandAnimation = 0.2F;
            this.dontReequipRight = false;
        } else {
            this.leftHandAnimation = 0.2F;
            this.dontReequipLeft = false;
        }
        this.playSound(hand, location);
    }

    protected abstract void playSound(InteractionHand var1, Vec3 var2);

    protected abstract boolean appliesTo(ItemStack var1);

    protected abstract void transformTool(PoseStack var1, float var2, float var3, float var4, float var5);

    protected abstract void transformHand(PoseStack var1, float var2, float var3, float var4, float var5);

    public void registerListeners(IEventBus bus) {
        bus.addListener(this::onRenderPlayerHand);
    }

    protected void onRenderPlayerHand(RenderHandEvent event) {
        ItemStack heldItem = event.getItemStack();
        if (this.appliesTo(heldItem)) {
            Minecraft mc = Minecraft.getInstance();
            AbstractClientPlayer player = mc.player;
            PlayerRenderer playerrenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().<AbstractClientPlayer>getRenderer(player);
            ItemInHandRenderer firstPersonRenderer = mc.getEntityRenderDispatcher().getItemInHandRenderer();
            PoseStack ms = event.getPoseStack();
            MultiBufferSource buffer = event.getMultiBufferSource();
            int light = event.getPackedLight();
            float pt = event.getPartialTick();
            boolean rightHand = event.getHand() == InteractionHand.MAIN_HAND ^ mc.player.m_5737_() == HumanoidArm.LEFT;
            float recoil = rightHand ? Mth.lerp(pt, this.lastRightHandAnimation, this.rightHandAnimation) : Mth.lerp(pt, this.lastLeftHandAnimation, this.leftHandAnimation);
            float equipProgress = event.getEquipProgress();
            if (rightHand && (this.rightHandAnimation > 0.01F || this.dontReequipRight)) {
                equipProgress = 0.0F;
            }
            if (!rightHand && (this.leftHandAnimation > 0.01F || this.dontReequipLeft)) {
                equipProgress = 0.0F;
            }
            ms.pushPose();
            RenderSystem.setShaderTexture(0, player.getSkinTextureLocation());
            float flip = rightHand ? 1.0F : -1.0F;
            float f1 = Mth.sqrt(event.getSwingProgress());
            float f2 = -0.3F * Mth.sin(f1 * (float) Math.PI);
            float f3 = 0.4F * Mth.sin(f1 * (float) (Math.PI * 2));
            float f4 = -0.4F * Mth.sin(event.getSwingProgress() * (float) Math.PI);
            float f5 = Mth.sin(event.getSwingProgress() * event.getSwingProgress() * (float) Math.PI);
            float f6 = Mth.sin(f1 * (float) Math.PI);
            ms.translate(flip * (f2 + 0.64F - 0.1F), f3 + -0.4F + equipProgress * -0.6F, f4 + -0.72F + 0.3F + recoil);
            ms.mulPose(Axis.YP.rotationDegrees(flip * 75.0F));
            ms.mulPose(Axis.YP.rotationDegrees(flip * f6 * 70.0F));
            ms.mulPose(Axis.ZP.rotationDegrees(flip * f5 * -20.0F));
            ms.translate(flip * -1.0F, 3.6F, 3.5F);
            ms.mulPose(Axis.ZP.rotationDegrees(flip * 120.0F));
            ms.mulPose(Axis.XP.rotationDegrees(200.0F));
            ms.mulPose(Axis.YP.rotationDegrees(flip * -135.0F));
            ms.translate(flip * 5.6F, 0.0F, 0.0F);
            ms.mulPose(Axis.YP.rotationDegrees(flip * 40.0F));
            this.transformHand(ms, flip, equipProgress, recoil, pt);
            if (rightHand) {
                playerrenderer.renderRightHand(ms, buffer, light, player);
            } else {
                playerrenderer.renderLeftHand(ms, buffer, light, player);
            }
            ms.popPose();
            ms.pushPose();
            ms.translate(flip * (f2 + 0.64F - 0.1F), f3 + -0.4F + equipProgress * -0.6F, f4 + -0.72F - 0.1F + recoil);
            ms.mulPose(Axis.YP.rotationDegrees(flip * f6 * 70.0F));
            ms.mulPose(Axis.ZP.rotationDegrees(flip * f5 * -20.0F));
            this.transformTool(ms, flip, equipProgress, recoil, pt);
            firstPersonRenderer.renderItem(mc.player, heldItem, rightHand ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !rightHand, ms, buffer, light);
            ms.popPose();
            event.setCanceled(true);
        }
    }

    public void dontAnimateItem(InteractionHand hand) {
        LocalPlayer player = Minecraft.getInstance().player;
        boolean rightHand = hand == InteractionHand.MAIN_HAND ^ player.m_5737_() == HumanoidArm.LEFT;
        this.dontReequipRight |= rightHand;
        this.dontReequipLeft |= !rightHand;
    }
}