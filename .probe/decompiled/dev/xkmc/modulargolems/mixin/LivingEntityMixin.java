package dev.xkmc.modulargolems.mixin;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.item.equipments.GolemEquipmentItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ LivingEntity.class })
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;travel(Lnet/minecraft/world/phys/Vec3;)V") }, method = { "aiStep" })
    public void modulargolems$travelRiddenByGolem(LivingEntity le, Vec3 vec3, Operation<Void> op) {
        if (le.m_6688_() instanceof AbstractGolemEntity && !(le instanceof AbstractGolemEntity)) {
            op.call(new Object[] { le, vec3.normalize() });
        } else {
            op.call(new Object[] { le, vec3 });
        }
    }

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getAttributeModifiers(Lnet/minecraft/world/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;") }, method = { "collectEquipmentChanges" })
    public Multimap<Attribute, AttributeModifier> modulargolems$collectEquipmentChanges$specialEquipment(ItemStack stack, EquipmentSlot slot, Operation<Multimap<Attribute, AttributeModifier>> op) {
        return stack.getItem() instanceof GolemEquipmentItem item ? item.getGolemModifiers(stack, this, slot) : (Multimap) op.call(new Object[] { stack, slot });
    }

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;position()Lnet/minecraft/world/phys/Vec3;") }, method = { "dropExperience" })
    public Vec3 modulargolems$dropExperience$moveToGolem(LivingEntity killed, Operation<Vec3> original) {
        if (killed.getLastHurtMob() instanceof AbstractGolemEntity<?, ?> e && e.hasFlag(GolemFlags.PICKUP)) {
            return e.m_20182_();
        }
        return (Vec3) original.call(new Object[] { killed });
    }
}