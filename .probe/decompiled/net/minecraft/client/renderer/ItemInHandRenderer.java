package net.minecraft.client.renderer;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix4f;

public class ItemInHandRenderer {

    private static final RenderType MAP_BACKGROUND = RenderType.text(new ResourceLocation("textures/map/map_background.png"));

    private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType.text(new ResourceLocation("textures/map/map_background_checkerboard.png"));

    private static final float ITEM_SWING_X_POS_SCALE = -0.4F;

    private static final float ITEM_SWING_Y_POS_SCALE = 0.2F;

    private static final float ITEM_SWING_Z_POS_SCALE = -0.2F;

    private static final float ITEM_HEIGHT_SCALE = -0.6F;

    private static final float ITEM_POS_X = 0.56F;

    private static final float ITEM_POS_Y = -0.52F;

    private static final float ITEM_POS_Z = -0.72F;

    private static final float ITEM_PRESWING_ROT_Y = 45.0F;

    private static final float ITEM_SWING_X_ROT_AMOUNT = -80.0F;

    private static final float ITEM_SWING_Y_ROT_AMOUNT = -20.0F;

    private static final float ITEM_SWING_Z_ROT_AMOUNT = -20.0F;

    private static final float EAT_JIGGLE_X_ROT_AMOUNT = 10.0F;

    private static final float EAT_JIGGLE_Y_ROT_AMOUNT = 90.0F;

    private static final float EAT_JIGGLE_Z_ROT_AMOUNT = 30.0F;

    private static final float EAT_JIGGLE_X_POS_SCALE = 0.6F;

    private static final float EAT_JIGGLE_Y_POS_SCALE = -0.5F;

    private static final float EAT_JIGGLE_Z_POS_SCALE = 0.0F;

    private static final double EAT_JIGGLE_EXPONENT = 27.0;

    private static final float EAT_EXTRA_JIGGLE_CUTOFF = 0.8F;

    private static final float EAT_EXTRA_JIGGLE_SCALE = 0.1F;

    private static final float ARM_SWING_X_POS_SCALE = -0.3F;

    private static final float ARM_SWING_Y_POS_SCALE = 0.4F;

    private static final float ARM_SWING_Z_POS_SCALE = -0.4F;

    private static final float ARM_SWING_Y_ROT_AMOUNT = 70.0F;

    private static final float ARM_SWING_Z_ROT_AMOUNT = -20.0F;

    private static final float ARM_HEIGHT_SCALE = -0.6F;

    private static final float ARM_POS_SCALE = 0.8F;

    private static final float ARM_POS_X = 0.8F;

    private static final float ARM_POS_Y = -0.75F;

    private static final float ARM_POS_Z = -0.9F;

    private static final float ARM_PRESWING_ROT_Y = 45.0F;

    private static final float ARM_PREROTATION_X_OFFSET = -1.0F;

    private static final float ARM_PREROTATION_Y_OFFSET = 3.6F;

    private static final float ARM_PREROTATION_Z_OFFSET = 3.5F;

    private static final float ARM_POSTROTATION_X_OFFSET = 5.6F;

    private static final int ARM_ROT_X = 200;

    private static final int ARM_ROT_Y = -135;

    private static final int ARM_ROT_Z = 120;

    private static final float MAP_SWING_X_POS_SCALE = -0.4F;

    private static final float MAP_SWING_Z_POS_SCALE = -0.2F;

    private static final float MAP_HANDS_POS_X = 0.0F;

    private static final float MAP_HANDS_POS_Y = 0.04F;

    private static final float MAP_HANDS_POS_Z = -0.72F;

    private static final float MAP_HANDS_HEIGHT_SCALE = -1.2F;

    private static final float MAP_HANDS_TILT_SCALE = -0.5F;

    private static final float MAP_PLAYER_PITCH_SCALE = 45.0F;

    private static final float MAP_HANDS_Z_ROT_AMOUNT = -85.0F;

    private static final float MAPHAND_X_ROT_AMOUNT = 45.0F;

    private static final float MAPHAND_Y_ROT_AMOUNT = 92.0F;

    private static final float MAPHAND_Z_ROT_AMOUNT = -41.0F;

    private static final float MAP_HAND_X_POS = 0.3F;

    private static final float MAP_HAND_Y_POS = -1.1F;

    private static final float MAP_HAND_Z_POS = 0.45F;

    private static final float MAP_SWING_X_ROT_AMOUNT = 20.0F;

    private static final float MAP_PRE_ROT_SCALE = 0.38F;

    private static final float MAP_GLOBAL_X_POS = -0.5F;

    private static final float MAP_GLOBAL_Y_POS = -0.5F;

    private static final float MAP_GLOBAL_Z_POS = 0.0F;

    private static final float MAP_FINAL_SCALE = 0.0078125F;

    private static final int MAP_BORDER = 7;

    private static final int MAP_HEIGHT = 128;

    private static final int MAP_WIDTH = 128;

    private static final float BOW_CHARGE_X_POS_SCALE = 0.0F;

    private static final float BOW_CHARGE_Y_POS_SCALE = 0.0F;

    private static final float BOW_CHARGE_Z_POS_SCALE = 0.04F;

    private static final float BOW_CHARGE_SHAKE_X_SCALE = 0.0F;

    private static final float BOW_CHARGE_SHAKE_Y_SCALE = 0.004F;

    private static final float BOW_CHARGE_SHAKE_Z_SCALE = 0.0F;

    private static final float BOW_CHARGE_Z_SCALE = 0.2F;

    private static final float BOW_MIN_SHAKE_CHARGE = 0.1F;

    private final Minecraft minecraft;

    private ItemStack mainHandItem = ItemStack.EMPTY;

    private ItemStack offHandItem = ItemStack.EMPTY;

    private float mainHandHeight;

    private float oMainHandHeight;

    private float offHandHeight;

    private float oOffHandHeight;

    private final EntityRenderDispatcher entityRenderDispatcher;

    private final ItemRenderer itemRenderer;

    public ItemInHandRenderer(Minecraft minecraft0, EntityRenderDispatcher entityRenderDispatcher1, ItemRenderer itemRenderer2) {
        this.minecraft = minecraft0;
        this.entityRenderDispatcher = entityRenderDispatcher1;
        this.itemRenderer = itemRenderer2;
    }

    public void renderItem(LivingEntity livingEntity0, ItemStack itemStack1, ItemDisplayContext itemDisplayContext2, boolean boolean3, PoseStack poseStack4, MultiBufferSource multiBufferSource5, int int6) {
        if (!itemStack1.isEmpty()) {
            this.itemRenderer.renderStatic(livingEntity0, itemStack1, itemDisplayContext2, boolean3, poseStack4, multiBufferSource5, livingEntity0.m_9236_(), int6, OverlayTexture.NO_OVERLAY, livingEntity0.m_19879_() + itemDisplayContext2.ordinal());
        }
    }

    private float calculateMapTilt(float float0) {
        float $$1 = 1.0F - float0 / 45.0F + 0.1F;
        $$1 = Mth.clamp($$1, 0.0F, 1.0F);
        return -Mth.cos($$1 * (float) Math.PI) * 0.5F + 0.5F;
    }

    private void renderMapHand(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, HumanoidArm humanoidArm3) {
        RenderSystem.setShaderTexture(0, this.minecraft.player.m_108560_());
        PlayerRenderer $$4 = (PlayerRenderer) this.entityRenderDispatcher.<AbstractClientPlayer>getRenderer(this.minecraft.player);
        poseStack0.pushPose();
        float $$5 = humanoidArm3 == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        poseStack0.mulPose(Axis.YP.rotationDegrees(92.0F));
        poseStack0.mulPose(Axis.XP.rotationDegrees(45.0F));
        poseStack0.mulPose(Axis.ZP.rotationDegrees($$5 * -41.0F));
        poseStack0.translate($$5 * 0.3F, -1.1F, 0.45F);
        if (humanoidArm3 == HumanoidArm.RIGHT) {
            $$4.renderRightHand(poseStack0, multiBufferSource1, int2, this.minecraft.player);
        } else {
            $$4.renderLeftHand(poseStack0, multiBufferSource1, int2, this.minecraft.player);
        }
        poseStack0.popPose();
    }

    private void renderOneHandedMap(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, float float3, HumanoidArm humanoidArm4, float float5, ItemStack itemStack6) {
        float $$7 = humanoidArm4 == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        poseStack0.translate($$7 * 0.125F, -0.125F, 0.0F);
        if (!this.minecraft.player.m_20145_()) {
            poseStack0.pushPose();
            poseStack0.mulPose(Axis.ZP.rotationDegrees($$7 * 10.0F));
            this.renderPlayerArm(poseStack0, multiBufferSource1, int2, float3, float5, humanoidArm4);
            poseStack0.popPose();
        }
        poseStack0.pushPose();
        poseStack0.translate($$7 * 0.51F, -0.08F + float3 * -1.2F, -0.75F);
        float $$8 = Mth.sqrt(float5);
        float $$9 = Mth.sin($$8 * (float) Math.PI);
        float $$10 = -0.5F * $$9;
        float $$11 = 0.4F * Mth.sin($$8 * (float) (Math.PI * 2));
        float $$12 = -0.3F * Mth.sin(float5 * (float) Math.PI);
        poseStack0.translate($$7 * $$10, $$11 - 0.3F * $$9, $$12);
        poseStack0.mulPose(Axis.XP.rotationDegrees($$9 * -45.0F));
        poseStack0.mulPose(Axis.YP.rotationDegrees($$7 * $$9 * -30.0F));
        this.renderMap(poseStack0, multiBufferSource1, int2, itemStack6);
        poseStack0.popPose();
    }

    private void renderTwoHandedMap(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, float float3, float float4, float float5) {
        float $$6 = Mth.sqrt(float5);
        float $$7 = -0.2F * Mth.sin(float5 * (float) Math.PI);
        float $$8 = -0.4F * Mth.sin($$6 * (float) Math.PI);
        poseStack0.translate(0.0F, -$$7 / 2.0F, $$8);
        float $$9 = this.calculateMapTilt(float3);
        poseStack0.translate(0.0F, 0.04F + float4 * -1.2F + $$9 * -0.5F, -0.72F);
        poseStack0.mulPose(Axis.XP.rotationDegrees($$9 * -85.0F));
        if (!this.minecraft.player.m_20145_()) {
            poseStack0.pushPose();
            poseStack0.mulPose(Axis.YP.rotationDegrees(90.0F));
            this.renderMapHand(poseStack0, multiBufferSource1, int2, HumanoidArm.RIGHT);
            this.renderMapHand(poseStack0, multiBufferSource1, int2, HumanoidArm.LEFT);
            poseStack0.popPose();
        }
        float $$10 = Mth.sin($$6 * (float) Math.PI);
        poseStack0.mulPose(Axis.XP.rotationDegrees($$10 * 20.0F));
        poseStack0.scale(2.0F, 2.0F, 2.0F);
        this.renderMap(poseStack0, multiBufferSource1, int2, this.mainHandItem);
    }

    private void renderMap(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, ItemStack itemStack3) {
        poseStack0.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack0.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack0.scale(0.38F, 0.38F, 0.38F);
        poseStack0.translate(-0.5F, -0.5F, 0.0F);
        poseStack0.scale(0.0078125F, 0.0078125F, 0.0078125F);
        Integer $$4 = MapItem.getMapId(itemStack3);
        MapItemSavedData $$5 = MapItem.getSavedData($$4, this.minecraft.level);
        VertexConsumer $$6 = multiBufferSource1.getBuffer($$5 == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
        Matrix4f $$7 = poseStack0.last().pose();
        $$6.vertex($$7, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(int2).endVertex();
        $$6.vertex($$7, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(int2).endVertex();
        $$6.vertex($$7, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(int2).endVertex();
        $$6.vertex($$7, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(int2).endVertex();
        if ($$5 != null) {
            this.minecraft.gameRenderer.getMapRenderer().render(poseStack0, multiBufferSource1, $$4, $$5, false, int2);
        }
    }

    private void renderPlayerArm(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, float float3, float float4, HumanoidArm humanoidArm5) {
        boolean $$6 = humanoidArm5 != HumanoidArm.LEFT;
        float $$7 = $$6 ? 1.0F : -1.0F;
        float $$8 = Mth.sqrt(float4);
        float $$9 = -0.3F * Mth.sin($$8 * (float) Math.PI);
        float $$10 = 0.4F * Mth.sin($$8 * (float) (Math.PI * 2));
        float $$11 = -0.4F * Mth.sin(float4 * (float) Math.PI);
        poseStack0.translate($$7 * ($$9 + 0.64000005F), $$10 + -0.6F + float3 * -0.6F, $$11 + -0.71999997F);
        poseStack0.mulPose(Axis.YP.rotationDegrees($$7 * 45.0F));
        float $$12 = Mth.sin(float4 * float4 * (float) Math.PI);
        float $$13 = Mth.sin($$8 * (float) Math.PI);
        poseStack0.mulPose(Axis.YP.rotationDegrees($$7 * $$13 * 70.0F));
        poseStack0.mulPose(Axis.ZP.rotationDegrees($$7 * $$12 * -20.0F));
        AbstractClientPlayer $$14 = this.minecraft.player;
        RenderSystem.setShaderTexture(0, $$14.getSkinTextureLocation());
        poseStack0.translate($$7 * -1.0F, 3.6F, 3.5F);
        poseStack0.mulPose(Axis.ZP.rotationDegrees($$7 * 120.0F));
        poseStack0.mulPose(Axis.XP.rotationDegrees(200.0F));
        poseStack0.mulPose(Axis.YP.rotationDegrees($$7 * -135.0F));
        poseStack0.translate($$7 * 5.6F, 0.0F, 0.0F);
        PlayerRenderer $$15 = (PlayerRenderer) this.entityRenderDispatcher.<AbstractClientPlayer>getRenderer($$14);
        if ($$6) {
            $$15.renderRightHand(poseStack0, multiBufferSource1, int2, $$14);
        } else {
            $$15.renderLeftHand(poseStack0, multiBufferSource1, int2, $$14);
        }
    }

    private void applyEatTransform(PoseStack poseStack0, float float1, HumanoidArm humanoidArm2, ItemStack itemStack3) {
        float $$4 = (float) this.minecraft.player.m_21212_() - float1 + 1.0F;
        float $$5 = $$4 / (float) itemStack3.getUseDuration();
        if ($$5 < 0.8F) {
            float $$6 = Mth.abs(Mth.cos($$4 / 4.0F * (float) Math.PI) * 0.1F);
            poseStack0.translate(0.0F, $$6, 0.0F);
        }
        float $$7 = 1.0F - (float) Math.pow((double) $$5, 27.0);
        int $$8 = humanoidArm2 == HumanoidArm.RIGHT ? 1 : -1;
        poseStack0.translate($$7 * 0.6F * (float) $$8, $$7 * -0.5F, $$7 * 0.0F);
        poseStack0.mulPose(Axis.YP.rotationDegrees((float) $$8 * $$7 * 90.0F));
        poseStack0.mulPose(Axis.XP.rotationDegrees($$7 * 10.0F));
        poseStack0.mulPose(Axis.ZP.rotationDegrees((float) $$8 * $$7 * 30.0F));
    }

    private void applyBrushTransform(PoseStack poseStack0, float float1, HumanoidArm humanoidArm2, ItemStack itemStack3, float float4) {
        this.applyItemArmTransform(poseStack0, humanoidArm2, float4);
        float $$5 = (float) (this.minecraft.player.m_21212_() % 10);
        float $$6 = $$5 - float1 + 1.0F;
        float $$7 = 1.0F - $$6 / 10.0F;
        float $$8 = -90.0F;
        float $$9 = 60.0F;
        float $$10 = 150.0F;
        float $$11 = -15.0F;
        int $$12 = 2;
        float $$13 = -15.0F + 75.0F * Mth.cos($$7 * 2.0F * (float) Math.PI);
        if (humanoidArm2 != HumanoidArm.RIGHT) {
            poseStack0.translate(0.1, 0.83, 0.35);
            poseStack0.mulPose(Axis.XP.rotationDegrees(-80.0F));
            poseStack0.mulPose(Axis.YP.rotationDegrees(-90.0F));
            poseStack0.mulPose(Axis.XP.rotationDegrees($$13));
            poseStack0.translate(-0.3, 0.22, 0.35);
        } else {
            poseStack0.translate(-0.25, 0.22, 0.35);
            poseStack0.mulPose(Axis.XP.rotationDegrees(-80.0F));
            poseStack0.mulPose(Axis.YP.rotationDegrees(90.0F));
            poseStack0.mulPose(Axis.ZP.rotationDegrees(0.0F));
            poseStack0.mulPose(Axis.XP.rotationDegrees($$13));
        }
    }

    private void applyItemArmAttackTransform(PoseStack poseStack0, HumanoidArm humanoidArm1, float float2) {
        int $$3 = humanoidArm1 == HumanoidArm.RIGHT ? 1 : -1;
        float $$4 = Mth.sin(float2 * float2 * (float) Math.PI);
        poseStack0.mulPose(Axis.YP.rotationDegrees((float) $$3 * (45.0F + $$4 * -20.0F)));
        float $$5 = Mth.sin(Mth.sqrt(float2) * (float) Math.PI);
        poseStack0.mulPose(Axis.ZP.rotationDegrees((float) $$3 * $$5 * -20.0F));
        poseStack0.mulPose(Axis.XP.rotationDegrees($$5 * -80.0F));
        poseStack0.mulPose(Axis.YP.rotationDegrees((float) $$3 * -45.0F));
    }

    private void applyItemArmTransform(PoseStack poseStack0, HumanoidArm humanoidArm1, float float2) {
        int $$3 = humanoidArm1 == HumanoidArm.RIGHT ? 1 : -1;
        poseStack0.translate((float) $$3 * 0.56F, -0.52F + float2 * -0.6F, -0.72F);
    }

    public void renderHandsWithItems(float float0, PoseStack poseStack1, MultiBufferSource.BufferSource multiBufferSourceBufferSource2, LocalPlayer localPlayer3, int int4) {
        float $$5 = localPlayer3.m_21324_(float0);
        InteractionHand $$6 = (InteractionHand) MoreObjects.firstNonNull(localPlayer3.f_20912_, InteractionHand.MAIN_HAND);
        float $$7 = Mth.lerp(float0, localPlayer3.f_19860_, localPlayer3.m_146909_());
        ItemInHandRenderer.HandRenderSelection $$8 = evaluateWhichHandsToRender(localPlayer3);
        float $$9 = Mth.lerp(float0, localPlayer3.xBobO, localPlayer3.xBob);
        float $$10 = Mth.lerp(float0, localPlayer3.yBobO, localPlayer3.yBob);
        poseStack1.mulPose(Axis.XP.rotationDegrees((localPlayer3.getViewXRot(float0) - $$9) * 0.1F));
        poseStack1.mulPose(Axis.YP.rotationDegrees((localPlayer3.getViewYRot(float0) - $$10) * 0.1F));
        if ($$8.renderMainHand) {
            float $$11 = $$6 == InteractionHand.MAIN_HAND ? $$5 : 0.0F;
            float $$12 = 1.0F - Mth.lerp(float0, this.oMainHandHeight, this.mainHandHeight);
            this.renderArmWithItem(localPlayer3, float0, $$7, InteractionHand.MAIN_HAND, $$11, this.mainHandItem, $$12, poseStack1, multiBufferSourceBufferSource2, int4);
        }
        if ($$8.renderOffHand) {
            float $$13 = $$6 == InteractionHand.OFF_HAND ? $$5 : 0.0F;
            float $$14 = 1.0F - Mth.lerp(float0, this.oOffHandHeight, this.offHandHeight);
            this.renderArmWithItem(localPlayer3, float0, $$7, InteractionHand.OFF_HAND, $$13, this.offHandItem, $$14, poseStack1, multiBufferSourceBufferSource2, int4);
        }
        multiBufferSourceBufferSource2.endBatch();
    }

    @VisibleForTesting
    static ItemInHandRenderer.HandRenderSelection evaluateWhichHandsToRender(LocalPlayer localPlayer0) {
        ItemStack $$1 = localPlayer0.m_21205_();
        ItemStack $$2 = localPlayer0.m_21206_();
        boolean $$3 = $$1.is(Items.BOW) || $$2.is(Items.BOW);
        boolean $$4 = $$1.is(Items.CROSSBOW) || $$2.is(Items.CROSSBOW);
        if (!$$3 && !$$4) {
            return ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        } else if (localPlayer0.isUsingItem()) {
            return selectionUsingItemWhileHoldingBowLike(localPlayer0);
        } else {
            return isChargedCrossbow($$1) ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        }
    }

    private static ItemInHandRenderer.HandRenderSelection selectionUsingItemWhileHoldingBowLike(LocalPlayer localPlayer0) {
        ItemStack $$1 = localPlayer0.m_21211_();
        InteractionHand $$2 = localPlayer0.getUsedItemHand();
        if (!$$1.is(Items.BOW) && !$$1.is(Items.CROSSBOW)) {
            return $$2 == InteractionHand.MAIN_HAND && isChargedCrossbow(localPlayer0.m_21206_()) ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        } else {
            return ItemInHandRenderer.HandRenderSelection.onlyForHand($$2);
        }
    }

    private static boolean isChargedCrossbow(ItemStack itemStack0) {
        return itemStack0.is(Items.CROSSBOW) && CrossbowItem.isCharged(itemStack0);
    }

    private void renderArmWithItem(AbstractClientPlayer abstractClientPlayer0, float float1, float float2, InteractionHand interactionHand3, float float4, ItemStack itemStack5, float float6, PoseStack poseStack7, MultiBufferSource multiBufferSource8, int int9) {
        if (!abstractClientPlayer0.m_150108_()) {
            boolean $$10 = interactionHand3 == InteractionHand.MAIN_HAND;
            HumanoidArm $$11 = $$10 ? abstractClientPlayer0.m_5737_() : abstractClientPlayer0.m_5737_().getOpposite();
            poseStack7.pushPose();
            if (itemStack5.isEmpty()) {
                if ($$10 && !abstractClientPlayer0.m_20145_()) {
                    this.renderPlayerArm(poseStack7, multiBufferSource8, int9, float6, float4, $$11);
                }
            } else if (itemStack5.is(Items.FILLED_MAP)) {
                if ($$10 && this.offHandItem.isEmpty()) {
                    this.renderTwoHandedMap(poseStack7, multiBufferSource8, int9, float2, float6, float4);
                } else {
                    this.renderOneHandedMap(poseStack7, multiBufferSource8, int9, float6, $$11, float4, itemStack5);
                }
            } else if (itemStack5.is(Items.CROSSBOW)) {
                boolean $$12 = CrossbowItem.isCharged(itemStack5);
                boolean $$13 = $$11 == HumanoidArm.RIGHT;
                int $$14 = $$13 ? 1 : -1;
                if (abstractClientPlayer0.m_6117_() && abstractClientPlayer0.m_21212_() > 0 && abstractClientPlayer0.m_7655_() == interactionHand3) {
                    this.applyItemArmTransform(poseStack7, $$11, float6);
                    poseStack7.translate((float) $$14 * -0.4785682F, -0.094387F, 0.05731531F);
                    poseStack7.mulPose(Axis.XP.rotationDegrees(-11.935F));
                    poseStack7.mulPose(Axis.YP.rotationDegrees((float) $$14 * 65.3F));
                    poseStack7.mulPose(Axis.ZP.rotationDegrees((float) $$14 * -9.785F));
                    float $$15 = (float) itemStack5.getUseDuration() - ((float) this.minecraft.player.m_21212_() - float1 + 1.0F);
                    float $$16 = $$15 / (float) CrossbowItem.getChargeDuration(itemStack5);
                    if ($$16 > 1.0F) {
                        $$16 = 1.0F;
                    }
                    if ($$16 > 0.1F) {
                        float $$17 = Mth.sin(($$15 - 0.1F) * 1.3F);
                        float $$18 = $$16 - 0.1F;
                        float $$19 = $$17 * $$18;
                        poseStack7.translate($$19 * 0.0F, $$19 * 0.004F, $$19 * 0.0F);
                    }
                    poseStack7.translate($$16 * 0.0F, $$16 * 0.0F, $$16 * 0.04F);
                    poseStack7.scale(1.0F, 1.0F, 1.0F + $$16 * 0.2F);
                    poseStack7.mulPose(Axis.YN.rotationDegrees((float) $$14 * 45.0F));
                } else {
                    float $$20 = -0.4F * Mth.sin(Mth.sqrt(float4) * (float) Math.PI);
                    float $$21 = 0.2F * Mth.sin(Mth.sqrt(float4) * (float) (Math.PI * 2));
                    float $$22 = -0.2F * Mth.sin(float4 * (float) Math.PI);
                    poseStack7.translate((float) $$14 * $$20, $$21, $$22);
                    this.applyItemArmTransform(poseStack7, $$11, float6);
                    this.applyItemArmAttackTransform(poseStack7, $$11, float4);
                    if ($$12 && float4 < 0.001F && $$10) {
                        poseStack7.translate((float) $$14 * -0.641864F, 0.0F, 0.0F);
                        poseStack7.mulPose(Axis.YP.rotationDegrees((float) $$14 * 10.0F));
                    }
                }
                this.renderItem(abstractClientPlayer0, itemStack5, $$13 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !$$13, poseStack7, multiBufferSource8, int9);
            } else {
                boolean $$23 = $$11 == HumanoidArm.RIGHT;
                if (abstractClientPlayer0.m_6117_() && abstractClientPlayer0.m_21212_() > 0 && abstractClientPlayer0.m_7655_() == interactionHand3) {
                    int $$24 = $$23 ? 1 : -1;
                    switch(itemStack5.getUseAnimation()) {
                        case NONE:
                            this.applyItemArmTransform(poseStack7, $$11, float6);
                            break;
                        case EAT:
                        case DRINK:
                            this.applyEatTransform(poseStack7, float1, $$11, itemStack5);
                            this.applyItemArmTransform(poseStack7, $$11, float6);
                            break;
                        case BLOCK:
                            this.applyItemArmTransform(poseStack7, $$11, float6);
                            break;
                        case BOW:
                            this.applyItemArmTransform(poseStack7, $$11, float6);
                            poseStack7.translate((float) $$24 * -0.2785682F, 0.18344387F, 0.15731531F);
                            poseStack7.mulPose(Axis.XP.rotationDegrees(-13.935F));
                            poseStack7.mulPose(Axis.YP.rotationDegrees((float) $$24 * 35.3F));
                            poseStack7.mulPose(Axis.ZP.rotationDegrees((float) $$24 * -9.785F));
                            float $$25 = (float) itemStack5.getUseDuration() - ((float) this.minecraft.player.m_21212_() - float1 + 1.0F);
                            float $$26 = $$25 / 20.0F;
                            $$26 = ($$26 * $$26 + $$26 * 2.0F) / 3.0F;
                            if ($$26 > 1.0F) {
                                $$26 = 1.0F;
                            }
                            if ($$26 > 0.1F) {
                                float $$27 = Mth.sin(($$25 - 0.1F) * 1.3F);
                                float $$28 = $$26 - 0.1F;
                                float $$29 = $$27 * $$28;
                                poseStack7.translate($$29 * 0.0F, $$29 * 0.004F, $$29 * 0.0F);
                            }
                            poseStack7.translate($$26 * 0.0F, $$26 * 0.0F, $$26 * 0.04F);
                            poseStack7.scale(1.0F, 1.0F, 1.0F + $$26 * 0.2F);
                            poseStack7.mulPose(Axis.YN.rotationDegrees((float) $$24 * 45.0F));
                            break;
                        case SPEAR:
                            this.applyItemArmTransform(poseStack7, $$11, float6);
                            poseStack7.translate((float) $$24 * -0.5F, 0.7F, 0.1F);
                            poseStack7.mulPose(Axis.XP.rotationDegrees(-55.0F));
                            poseStack7.mulPose(Axis.YP.rotationDegrees((float) $$24 * 35.3F));
                            poseStack7.mulPose(Axis.ZP.rotationDegrees((float) $$24 * -9.785F));
                            float $$30 = (float) itemStack5.getUseDuration() - ((float) this.minecraft.player.m_21212_() - float1 + 1.0F);
                            float $$31 = $$30 / 10.0F;
                            if ($$31 > 1.0F) {
                                $$31 = 1.0F;
                            }
                            if ($$31 > 0.1F) {
                                float $$32 = Mth.sin(($$30 - 0.1F) * 1.3F);
                                float $$33 = $$31 - 0.1F;
                                float $$34 = $$32 * $$33;
                                poseStack7.translate($$34 * 0.0F, $$34 * 0.004F, $$34 * 0.0F);
                            }
                            poseStack7.translate(0.0F, 0.0F, $$31 * 0.2F);
                            poseStack7.scale(1.0F, 1.0F, 1.0F + $$31 * 0.2F);
                            poseStack7.mulPose(Axis.YN.rotationDegrees((float) $$24 * 45.0F));
                            break;
                        case BRUSH:
                            this.applyBrushTransform(poseStack7, float1, $$11, itemStack5, float6);
                    }
                } else if (abstractClientPlayer0.m_21209_()) {
                    this.applyItemArmTransform(poseStack7, $$11, float6);
                    int $$35 = $$23 ? 1 : -1;
                    poseStack7.translate((float) $$35 * -0.4F, 0.8F, 0.3F);
                    poseStack7.mulPose(Axis.YP.rotationDegrees((float) $$35 * 65.0F));
                    poseStack7.mulPose(Axis.ZP.rotationDegrees((float) $$35 * -85.0F));
                } else {
                    float $$36 = -0.4F * Mth.sin(Mth.sqrt(float4) * (float) Math.PI);
                    float $$37 = 0.2F * Mth.sin(Mth.sqrt(float4) * (float) (Math.PI * 2));
                    float $$38 = -0.2F * Mth.sin(float4 * (float) Math.PI);
                    int $$39 = $$23 ? 1 : -1;
                    poseStack7.translate((float) $$39 * $$36, $$37, $$38);
                    this.applyItemArmTransform(poseStack7, $$11, float6);
                    this.applyItemArmAttackTransform(poseStack7, $$11, float4);
                }
                this.renderItem(abstractClientPlayer0, itemStack5, $$23 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !$$23, poseStack7, multiBufferSource8, int9);
            }
            poseStack7.popPose();
        }
    }

    public void tick() {
        this.oMainHandHeight = this.mainHandHeight;
        this.oOffHandHeight = this.offHandHeight;
        LocalPlayer $$0 = this.minecraft.player;
        ItemStack $$1 = $$0.m_21205_();
        ItemStack $$2 = $$0.m_21206_();
        if (ItemStack.matches(this.mainHandItem, $$1)) {
            this.mainHandItem = $$1;
        }
        if (ItemStack.matches(this.offHandItem, $$2)) {
            this.offHandItem = $$2;
        }
        if ($$0.isHandsBusy()) {
            this.mainHandHeight = Mth.clamp(this.mainHandHeight - 0.4F, 0.0F, 1.0F);
            this.offHandHeight = Mth.clamp(this.offHandHeight - 0.4F, 0.0F, 1.0F);
        } else {
            float $$3 = $$0.m_36403_(1.0F);
            this.mainHandHeight = this.mainHandHeight + Mth.clamp((this.mainHandItem == $$1 ? $$3 * $$3 * $$3 : 0.0F) - this.mainHandHeight, -0.4F, 0.4F);
            this.offHandHeight = this.offHandHeight + Mth.clamp((float) (this.offHandItem == $$2 ? 1 : 0) - this.offHandHeight, -0.4F, 0.4F);
        }
        if (this.mainHandHeight < 0.1F) {
            this.mainHandItem = $$1;
        }
        if (this.offHandHeight < 0.1F) {
            this.offHandItem = $$2;
        }
    }

    public void itemUsed(InteractionHand interactionHand0) {
        if (interactionHand0 == InteractionHand.MAIN_HAND) {
            this.mainHandHeight = 0.0F;
        } else {
            this.offHandHeight = 0.0F;
        }
    }

    @VisibleForTesting
    static enum HandRenderSelection {

        RENDER_BOTH_HANDS(true, true), RENDER_MAIN_HAND_ONLY(true, false), RENDER_OFF_HAND_ONLY(false, true);

        final boolean renderMainHand;

        final boolean renderOffHand;

        private HandRenderSelection(boolean p_172928_, boolean p_172929_) {
            this.renderMainHand = p_172928_;
            this.renderOffHand = p_172929_;
        }

        public static ItemInHandRenderer.HandRenderSelection onlyForHand(InteractionHand p_172932_) {
            return p_172932_ == InteractionHand.MAIN_HAND ? RENDER_MAIN_HAND_ONLY : RENDER_OFF_HAND_ONLY;
        }
    }
}