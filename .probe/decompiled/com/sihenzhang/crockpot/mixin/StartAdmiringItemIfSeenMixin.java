package com.sihenzhang.crockpot.mixin;

import com.mojang.datafixers.kinds.IdF.Mu;
import com.mojang.datafixers.util.Unit;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.StartAdmiringItemIfSeen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ StartAdmiringItemIfSeen.class })
public abstract class StartAdmiringItemIfSeenMixin {

    @Inject(method = { "lambda$create$0(Lnet/minecraft/world/entity/ai/behavior/declarative/BehaviorBuilder$Instance;Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryAccessor;Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryAccessor;ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;J)Z" }, at = { @At(value = "RETURN", ordinal = 0) }, cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void checkExtraStartConditionsHandler(BehaviorBuilder.Instance<LivingEntity> behaviorBuilderInstance, MemoryAccessor<Mu, ItemEntity> nearestVisibleWantedItemMemoryAccessor, MemoryAccessor<com.mojang.datafixers.kinds.Const.Mu<Unit>, Boolean> admiringItemMemoryAccessor, int admireDuration, ServerLevel level, LivingEntity owner, long gameTime, CallbackInfoReturnable<Boolean> cir, ItemEntity itemEntity) {
        if (!itemEntity.getItem().is(ItemTags.PIGLIN_REPELLENTS) && !IPiglinAiMixin.callIsFood(itemEntity.getItem()) && level.getRecipeManager().getRecipeFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SimpleContainer(itemEntity.getItem()), level).isPresent()) {
            admiringItemMemoryAccessor.setWithExpiry(true, (long) admireDuration);
            cir.setReturnValue(true);
        }
    }
}