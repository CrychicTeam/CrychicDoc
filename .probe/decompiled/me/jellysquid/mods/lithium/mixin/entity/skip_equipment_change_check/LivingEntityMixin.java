package me.jellysquid.mods.lithium.mixin.entity.skip_equipment_change_check;

import java.util.Map;
import java.util.function.Predicate;
import me.jellysquid.mods.lithium.common.entity.EquipmentEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LivingEntity.class })
public abstract class LivingEntityMixin extends Entity implements EquipmentEntity {

    private static final Predicate<ItemStack> DYNAMIC_EQUIPMENT = item -> item.is(Items.CROSSBOW);

    private boolean equipmentChanged = true;

    @Shadow
    public abstract boolean isHolding(Predicate<ItemStack> var1);

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public void lithiumOnEquipmentChanged() {
        this.equipmentChanged = true;
    }

    @Inject(method = { "getEquipmentChanges()Ljava/util/Map;" }, at = { @At("HEAD") }, cancellable = true)
    private void skipSentEquipmentComparison(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        if (!this.equipmentChanged) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = { "sendEquipmentChanges()V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;checkHandStackSwap(Ljava/util/Map;)V") })
    private void resetEquipmentChanged(CallbackInfo ci) {
        if (this instanceof EquipmentEntity.EquipmentTrackingEntity && !this.isHolding(DYNAMIC_EQUIPMENT)) {
            this.equipmentChanged = false;
        }
    }

    @Inject(method = { "eatFood(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;" }, at = { @At("RETURN") })
    private void trackEatingEquipmentChange(Level world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        this.lithiumOnEquipmentChanged();
    }
}