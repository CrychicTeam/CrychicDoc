package com.xiaohunao.createheatjs.mixin;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.xiaohunao.createheatjs.HeatData;
import com.xiaohunao.createheatjs.util.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ BasinRecipe.class })
public class BasinRecipeMixin {

    @Inject(method = { "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z" }, at = { @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/recipe/HeatCondition;testBlazeBurner(Lcom/simibubi/create/content/processing/burner/BlazeBurnerBlock$HeatLevel;)Z", shift = Shift.AFTER) }, cancellable = true, remap = false)
    private static void applyMixin(BasinBlockEntity basin, Recipe<?> recipe, boolean test, CallbackInfoReturnable<Boolean> cir) {
        Level level = basin.m_58904_();
        if (level != null) {
            BlockPos blockPos = basin.m_58899_().below(1);
            BlockState blockState = level.getBlockState(blockPos);
            if (recipe instanceof BasinRecipe basinRecipe) {
                String heatCondition = basinRecipe.getRequiredHeat().toString();
                HeatData heatData = HeatData.getHeatData(heatCondition);
                heatData.getHeatSource().forEach((stack, predicate) -> {
                    String stackFirst = (String) stack.getFirst();
                    TagKey<Block> blockTagKey = TagKey.create(Registries.BLOCK, ResourceLocation.tryParse(stackFirst));
                    if (!stackFirst.equals("*") && !blockState.m_204336_(blockTagKey) && !blockState.m_60713_(BlockHelper.parseBlockState(stackFirst).m_60734_()) || !predicate.test(level, blockPos, blockState)) {
                        cir.setReturnValue(false);
                    }
                });
            }
        }
    }
}