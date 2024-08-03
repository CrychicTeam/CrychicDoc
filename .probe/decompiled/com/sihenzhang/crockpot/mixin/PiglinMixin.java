package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Piglin.class })
public abstract class PiglinMixin extends AbstractPiglin {

    private PiglinMixin(EntityType<? extends AbstractPiglin> entityTypeExtendsAbstractPiglin0, Level level1) {
        super(entityTypeExtendsAbstractPiglin0, level1);
    }

    @Inject(method = { "holdInOffHand(Lnet/minecraft/world/item/ItemStack;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/Piglin;setItemSlotAndDropWhenKilled(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V", ordinal = 0) }, cancellable = true)
    private void holdInOffHandHandler(ItemStack itemStack, CallbackInfo ci) {
        if (!itemStack.is(ItemTags.PIGLIN_REPELLENTS) && !IPiglinAiMixin.callIsFood(itemStack) && this.m_9236_().getRecipeManager().getRecipeFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SimpleContainer(itemStack), this.m_9236_()).isPresent()) {
            this.m_8061_(EquipmentSlot.OFFHAND, itemStack);
            this.m_21508_(EquipmentSlot.OFFHAND);
            ci.cancel();
        }
    }

    @Inject(method = { "getArmPose()Lnet/minecraft/world/entity/monster/piglin/PiglinArmPose;" }, at = { @At(value = "JUMP", opcode = 153, ordinal = 2) }, cancellable = true)
    private void getArmPoseHandler(CallbackInfoReturnable<PiglinArmPose> cir) {
        ItemStack offhandStack = this.m_21206_();
        if (!offhandStack.is(ItemTags.PIGLIN_REPELLENTS) && !IPiglinAiMixin.callIsFood(offhandStack) && this.m_9236_().getRecipeManager().getRecipeFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SimpleContainer(offhandStack), this.m_9236_()).isPresent()) {
            cir.setReturnValue(PiglinArmPose.ADMIRING_ITEM);
        }
    }
}