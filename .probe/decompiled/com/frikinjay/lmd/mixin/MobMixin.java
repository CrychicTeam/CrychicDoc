package com.frikinjay.lmd.mixin;

import com.frikinjay.lmd.LetMeDespawn;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Mob.class })
public abstract class MobMixin extends LivingEntity {

    @Unique
    private boolean letmedespawn$pickedItems = false;

    @Shadow
    private boolean persistenceRequired;

    protected MobMixin(EntityType<? extends LivingEntity> entityTypeExtendsLivingEntity0, Level level1) {
        super(entityTypeExtendsLivingEntity0, level1);
    }

    @Inject(at = { @At("TAIL") }, method = { "setItemSlotAndDropWhenKilled(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V" })
    private void setItemSlotAndDropWhenKilled(EquipmentSlot equipmentSlot0, ItemStack itemStack1, CallbackInfo info) {
        EquipmentSlot equipmentSlot = m_147233_(itemStack1);
        ItemStack itemStack = this.m_6844_(equipmentSlot);
        itemStack.getOrCreateTag().putBoolean("Picked", true);
        this.letmedespawn$pickedItems = true;
        this.persistenceRequired = this.m_8077_();
    }

    @Redirect(method = { "checkDespawn" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;discard()V"))
    private void yeetusCheckus(Mob instance) {
        if (LetMeDespawn.config.getMobNames().isEmpty() || !LetMeDespawn.config.getMobNames().contains(BuiltInRegistries.ENTITY_TYPE.getKey(this.m_6095_()).toString())) {
            if (this.letmedespawn$pickedItems) {
                this.letmedespawn$dropEquipmentOnDespawn();
            }
            this.m_146870_();
        }
    }

    @Unique
    protected void letmedespawn$dropEquipmentOnDespawn() {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack itemStack = this.m_6844_(equipmentSlot);
            if (itemStack.getTag() != null && !itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack) && itemStack.getTag().getBoolean("Picked")) {
                itemStack.removeTagKey("Picked");
                if (itemStack.getTag() != null && itemStack.getTag().isEmpty()) {
                    itemStack.setTag(null);
                }
                this.m_19983_(itemStack);
                this.m_8061_(equipmentSlot, ItemStack.EMPTY);
            }
        }
    }

    @Unique
    protected void letmedespawn$removeTagOnDeath() {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack itemStack = this.m_6844_(equipmentSlot);
            if (itemStack.getTag() != null && !itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack) && itemStack.getTag().getBoolean("Picked")) {
                itemStack.removeTagKey("Picked");
                if (itemStack.getTag() != null && itemStack.getTag().isEmpty()) {
                    itemStack.setTag(null);
                }
            }
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "dropFromLootTable" })
    protected void dropFromLootTable(DamageSource damageSource0, boolean boolean1, CallbackInfo ci) {
        if (this.letmedespawn$pickedItems) {
            this.letmedespawn$removeTagOnDeath();
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "dropCustomDeathLoot" })
    protected void dropCustomDeathLoot(DamageSource damageSource0, int int1, boolean boolean2, CallbackInfo ci) {
        if (this.letmedespawn$pickedItems) {
            this.letmedespawn$removeTagOnDeath();
        }
    }
}