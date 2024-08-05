package dev.xkmc.l2complements.mixin;

import dev.xkmc.l2complements.events.MaterialEventHandler;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ItemEntity.class })
public abstract class ItemEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getItem();

    public ItemEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(at = { @At("HEAD") }, method = { "dampensVibrations" }, cancellable = true)
    public void l2complements_dampensVibrations_sculkiumMinedItem(CallbackInfoReturnable<Boolean> cir) {
        ItemEntity self = (ItemEntity) this;
        if (self.getPersistentData().contains("dampensVibrations")) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;onDestroyed(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/damagesource/DamageSource;)V", remap = false) }, method = { "hurt" })
    public void l2complments_hurt_itemBurnt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        if (pSource.is(DamageTypeTags.IS_FIRE)) {
            MaterialEventHandler.onItemKill(this.m_9236_(), this, this.getItem());
        }
    }
}