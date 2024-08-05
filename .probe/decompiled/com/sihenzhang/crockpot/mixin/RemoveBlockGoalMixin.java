package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.block.food.PowCakeBlock;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ RemoveBlockGoal.class })
public abstract class RemoveBlockGoalMixin {

    @Redirect(method = { "tick()V" }, at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;", ordinal = 0))
    private ItemStack replaceParticles(ItemLike pItem) {
        return (RemoveBlockGoal) this instanceof PowCakeBlock.AnimalEatPowCakeGoal ? new ItemStack(CrockPotItems.POW_CAKE.get()) : new ItemStack(pItem);
    }
}