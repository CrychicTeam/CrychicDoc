package com.simibubi.create.foundation.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface CustomArmPoseItem {

    @Nullable
    HumanoidModel.ArmPose getArmPose(ItemStack var1, AbstractClientPlayer var2, InteractionHand var3);
}