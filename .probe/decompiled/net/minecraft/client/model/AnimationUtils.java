package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CrossbowItem;

public class AnimationUtils {

    public static void animateCrossbowHold(ModelPart modelPart0, ModelPart modelPart1, ModelPart modelPart2, boolean boolean3) {
        ModelPart $$4 = boolean3 ? modelPart0 : modelPart1;
        ModelPart $$5 = boolean3 ? modelPart1 : modelPart0;
        $$4.yRot = (boolean3 ? -0.3F : 0.3F) + modelPart2.yRot;
        $$5.yRot = (boolean3 ? 0.6F : -0.6F) + modelPart2.yRot;
        $$4.xRot = (float) (-Math.PI / 2) + modelPart2.xRot + 0.1F;
        $$5.xRot = -1.5F + modelPart2.xRot;
    }

    public static void animateCrossbowCharge(ModelPart modelPart0, ModelPart modelPart1, LivingEntity livingEntity2, boolean boolean3) {
        ModelPart $$4 = boolean3 ? modelPart0 : modelPart1;
        ModelPart $$5 = boolean3 ? modelPart1 : modelPart0;
        $$4.yRot = boolean3 ? -0.8F : 0.8F;
        $$4.xRot = -0.97079635F;
        $$5.xRot = $$4.xRot;
        float $$6 = (float) CrossbowItem.getChargeDuration(livingEntity2.getUseItem());
        float $$7 = Mth.clamp((float) livingEntity2.getTicksUsingItem(), 0.0F, $$6);
        float $$8 = $$7 / $$6;
        $$5.yRot = Mth.lerp($$8, 0.4F, 0.85F) * (float) (boolean3 ? 1 : -1);
        $$5.xRot = Mth.lerp($$8, $$5.xRot, (float) (-Math.PI / 2));
    }

    public static <T extends Mob> void swingWeaponDown(ModelPart modelPart0, ModelPart modelPart1, T t2, float float3, float float4) {
        float $$5 = Mth.sin(float3 * (float) Math.PI);
        float $$6 = Mth.sin((1.0F - (1.0F - float3) * (1.0F - float3)) * (float) Math.PI);
        modelPart0.zRot = 0.0F;
        modelPart1.zRot = 0.0F;
        modelPart0.yRot = (float) (Math.PI / 20);
        modelPart1.yRot = (float) (-Math.PI / 20);
        if (t2.getMainArm() == HumanoidArm.RIGHT) {
            modelPart0.xRot = -1.8849558F + Mth.cos(float4 * 0.09F) * 0.15F;
            modelPart1.xRot = -0.0F + Mth.cos(float4 * 0.19F) * 0.5F;
            modelPart0.xRot += $$5 * 2.2F - $$6 * 0.4F;
            modelPart1.xRot += $$5 * 1.2F - $$6 * 0.4F;
        } else {
            modelPart0.xRot = -0.0F + Mth.cos(float4 * 0.19F) * 0.5F;
            modelPart1.xRot = -1.8849558F + Mth.cos(float4 * 0.09F) * 0.15F;
            modelPart0.xRot += $$5 * 1.2F - $$6 * 0.4F;
            modelPart1.xRot += $$5 * 2.2F - $$6 * 0.4F;
        }
        bobArms(modelPart0, modelPart1, float4);
    }

    public static void bobModelPart(ModelPart modelPart0, float float1, float float2) {
        modelPart0.zRot = modelPart0.zRot + float2 * (Mth.cos(float1 * 0.09F) * 0.05F + 0.05F);
        modelPart0.xRot = modelPart0.xRot + float2 * Mth.sin(float1 * 0.067F) * 0.05F;
    }

    public static void bobArms(ModelPart modelPart0, ModelPart modelPart1, float float2) {
        bobModelPart(modelPart0, float2, 1.0F);
        bobModelPart(modelPart1, float2, -1.0F);
    }

    public static void animateZombieArms(ModelPart modelPart0, ModelPart modelPart1, boolean boolean2, float float3, float float4) {
        float $$5 = Mth.sin(float3 * (float) Math.PI);
        float $$6 = Mth.sin((1.0F - (1.0F - float3) * (1.0F - float3)) * (float) Math.PI);
        modelPart1.zRot = 0.0F;
        modelPart0.zRot = 0.0F;
        modelPart1.yRot = -(0.1F - $$5 * 0.6F);
        modelPart0.yRot = 0.1F - $$5 * 0.6F;
        float $$7 = (float) -Math.PI / (boolean2 ? 1.5F : 2.25F);
        modelPart1.xRot = $$7;
        modelPart0.xRot = $$7;
        modelPart1.xRot += $$5 * 1.2F - $$6 * 0.4F;
        modelPart0.xRot += $$5 * 1.2F - $$6 * 0.4F;
        bobArms(modelPart1, modelPart0, float4);
    }
}