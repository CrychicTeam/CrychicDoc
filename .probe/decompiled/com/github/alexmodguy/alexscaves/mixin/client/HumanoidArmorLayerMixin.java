package com.github.alexmodguy.alexscaves.mixin.client;

import com.github.alexmodguy.alexscaves.client.render.item.ACArmorRenderProperties;
import com.github.alexmodguy.alexscaves.server.item.CustomArmorPostRender;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ HumanoidArmorLayer.class })
public abstract class HumanoidArmorLayerMixin extends RenderLayer {

    private static final Map<String, ResourceLocation> AC_ARMOR_LOCATION_CACHE = Maps.newHashMap();

    private ItemStack lastArmorItemStackRendered = ItemStack.EMPTY;

    @Shadow
    protected abstract void setPartVisibility(HumanoidModel var1, EquipmentSlot var2);

    public HumanoidArmorLayerMixin(RenderLayerParent renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(method = { "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V" }, at = { @At("HEAD") }, remap = true, cancellable = true)
    private void ac_renderArmorPiece(PoseStack poseStack, MultiBufferSource multiBufferSource, LivingEntity livingEntity, EquipmentSlot equipmentSlot, int light, HumanoidModel humanoidModel, CallbackInfo ci) {
        ItemStack itemstack = livingEntity.getItemBySlot(equipmentSlot);
        if (itemstack.getItem() instanceof CustomArmorPostRender) {
            ci.cancel();
            this.lastArmorItemStackRendered = livingEntity.getItemBySlot(equipmentSlot);
            if (itemstack.getItem() instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() == equipmentSlot) {
                boolean legs = equipmentSlot == EquipmentSlot.LEGS;
                HumanoidModel model = this.m_117386_() instanceof HumanoidModel humanoidModel1 ? humanoidModel1 : humanoidModel;
                Model armorModel = ForgeHooksClient.getArmorModel(livingEntity, itemstack, equipmentSlot, model);
                this.setPartVisibility((HumanoidModel) armorModel, equipmentSlot);
                ResourceLocation texture = this.getACArmorResource(livingEntity, itemstack, equipmentSlot, null);
                ACArmorRenderProperties.renderCustomArmor(poseStack, multiBufferSource, light, this.lastArmorItemStackRendered, armorItem, armorModel, legs, texture);
            }
        }
    }

    private ResourceLocation getACArmorResource(LivingEntity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        ArmorItem item = (ArmorItem) stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(58);
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format(Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, slot == EquipmentSlot.LEGS ? 2 : 1, type == null ? "" : String.format(Locale.ROOT, "_%s", type));
        s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = (ResourceLocation) AC_ARMOR_LOCATION_CACHE.get(s1);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s1);
            AC_ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
        }
        return resourcelocation;
    }
}