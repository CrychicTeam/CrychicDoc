package com.simibubi.create.foundation.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface CustomRenderedArmorItem {

    @OnlyIn(Dist.CLIENT)
    void renderArmorPiece(HumanoidArmorLayer<?, ?, ?> var1, PoseStack var2, MultiBufferSource var3, LivingEntity var4, EquipmentSlot var5, int var6, HumanoidModel<?> var7, ItemStack var8);
}