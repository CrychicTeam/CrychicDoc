package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "crockpot")
public class ExplosionCraftingEvent {

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        Level level = event.getLevel();
        if (!level.isClientSide) {
            List<BlockPos> affectedBlocks = event.getAffectedBlocks();
            List<Entity> affectedEntities = event.getAffectedEntities();
            affectedBlocks.forEach(affectedBlock -> {
                BlockState blockState = level.getBlockState(affectedBlock);
                ExplosionCraftingRecipe.Wrapper container = new ExplosionCraftingRecipe.Wrapper(blockState.m_60734_().asItem().getDefaultInstance(), true);
                Optional<ExplosionCraftingRecipe> optionalRecipe = level.getRecipeManager().getRecipeFor(CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get(), container, level);
                if (optionalRecipe.isPresent()) {
                    blockState.onBlockExploded(level, affectedBlock, event.getExplosion());
                    spawnAsInvulnerableEntity(level, affectedBlock, ((ExplosionCraftingRecipe) optionalRecipe.get()).assemble(container, level.registryAccess()));
                }
            });
            affectedEntities.forEach(affectedEntity -> {
                if (affectedEntity instanceof ItemEntity itemEntity && affectedEntity.isAlive()) {
                    ExplosionCraftingRecipe.Wrapper container = new ExplosionCraftingRecipe.Wrapper(itemEntity.getItem());
                    Optional<ExplosionCraftingRecipe> optionalRecipe = level.getRecipeManager().getRecipeFor(CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get(), container, level);
                    if (optionalRecipe.isPresent()) {
                        while (!itemEntity.getItem().isEmpty()) {
                            shrinkItemEntity(itemEntity, 1);
                            spawnAsInvulnerableEntity(level, itemEntity.m_20183_(), ((ExplosionCraftingRecipe) optionalRecipe.get()).assemble(container, level.registryAccess()));
                        }
                    }
                }
            });
        }
    }

    private static void spawnAsInvulnerableEntity(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty()) {
            double x = (double) pos.m_123341_() + Mth.nextDouble(level.random, 0.25, 0.75);
            double y = (double) pos.m_123342_() + Mth.nextDouble(level.random, 0.25, 0.75);
            double z = (double) pos.m_123343_() + Mth.nextDouble(level.random, 0.25, 0.75);
            ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack);
            itemEntity.setDefaultPickUpDelay();
            itemEntity.m_20331_(true);
            level.m_7967_(itemEntity);
        }
    }

    private static void shrinkItemEntity(ItemEntity itemEntity, int count) {
        itemEntity.setNeverPickUp();
        ItemStack itemStack = itemEntity.getItem().copy();
        itemStack.shrink(count);
        itemEntity.setItem(itemStack);
        itemEntity.setDefaultPickUpDelay();
    }
}