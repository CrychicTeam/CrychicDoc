package com.simibubi.create.content.kinetics.deployer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.utility.BlockHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.wrapper.RecipeWrapper;

@EventBusSubscriber
public class ManualApplicationRecipe extends ItemApplicationRecipe {

    @SubscribeEvent
    public static void manualApplicationRecipesApplyInWorld(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        ItemStack heldItem = event.getItemStack();
        BlockPos pos = event.getPos();
        BlockState blockState = level.getBlockState(pos);
        if (!heldItem.isEmpty()) {
            if (!blockState.m_60795_()) {
                if (!event.isCanceled()) {
                    RecipeType<Recipe<RecipeWrapper>> type = AllRecipeTypes.ITEM_APPLICATION.getType();
                    Optional<Recipe<RecipeWrapper>> foundRecipe = level.getRecipeManager().<RecipeWrapper, Recipe<RecipeWrapper>>getAllRecipesFor(type).stream().filter(r -> {
                        ManualApplicationRecipe mar = (ManualApplicationRecipe) r;
                        return mar.testBlock(blockState) && mar.ingredients.get(1).test(heldItem);
                    }).findFirst();
                    if (!foundRecipe.isEmpty()) {
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        event.setCanceled(true);
                        if (!level.isClientSide()) {
                            level.playSound(null, pos, SoundEvents.COPPER_BREAK, SoundSource.PLAYERS, 1.0F, 1.45F);
                            ManualApplicationRecipe recipe = (ManualApplicationRecipe) foundRecipe.get();
                            level.m_46961_(pos, false);
                            BlockState transformedBlock = recipe.transformBlock(blockState);
                            level.setBlock(pos, transformedBlock, 3);
                            recipe.rollResults().forEach(stack -> Block.popResource(level, pos, stack));
                            boolean creative = event.getEntity() != null && event.getEntity().isCreative();
                            boolean unbreakable = heldItem.hasTag() && heldItem.getTag().getBoolean("Unbreakable");
                            boolean keepHeld = recipe.shouldKeepHeldItem() || creative;
                            if (!unbreakable && !keepHeld) {
                                if (heldItem.isDamageableItem()) {
                                    heldItem.hurtAndBreak(1, event.getEntity(), s -> s.m_21190_(InteractionHand.MAIN_HAND));
                                } else {
                                    heldItem.shrink(1);
                                }
                            }
                            awardAdvancements(event.getEntity(), transformedBlock);
                        }
                    }
                }
            }
        }
    }

    private static void awardAdvancements(Player player, BlockState placed) {
        CreateAdvancement advancement = null;
        if (AllBlocks.ANDESITE_CASING.has(placed)) {
            advancement = AllAdvancements.ANDESITE_CASING;
        } else if (AllBlocks.BRASS_CASING.has(placed)) {
            advancement = AllAdvancements.BRASS_CASING;
        } else if (AllBlocks.COPPER_CASING.has(placed)) {
            advancement = AllAdvancements.COPPER_CASING;
        } else {
            if (!AllBlocks.RAILWAY_CASING.has(placed)) {
                return;
            }
            advancement = AllAdvancements.TRAIN_CASING;
        }
        advancement.awardTo(player);
    }

    public ManualApplicationRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.ITEM_APPLICATION, params);
    }

    public static DeployerApplicationRecipe asDeploying(Recipe<?> recipe) {
        ManualApplicationRecipe mar = (ManualApplicationRecipe) recipe;
        ProcessingRecipeBuilder<DeployerApplicationRecipe> builder = new ProcessingRecipeBuilder<>(DeployerApplicationRecipe::new, new ResourceLocation(mar.id.getNamespace(), mar.id.getPath() + "_using_deployer")).require(mar.ingredients.get(0)).require(mar.ingredients.get(1));
        for (ProcessingOutput output : mar.results) {
            builder.output(output);
        }
        if (mar.shouldKeepHeldItem()) {
            builder.toolNotConsumed();
        }
        return builder.build();
    }

    public boolean testBlock(BlockState in) {
        return this.ingredients.get(0).test(new ItemStack(in.m_60734_().asItem()));
    }

    public BlockState transformBlock(BlockState in) {
        ProcessingOutput mainOutput = this.results.get(0);
        ItemStack output = mainOutput.rollOutput();
        return output.getItem() instanceof BlockItem bi ? BlockHelper.copyProperties(in, bi.getBlock().defaultBlockState()) : Blocks.AIR.defaultBlockState();
    }

    @Override
    public List<ItemStack> rollResults() {
        return this.rollResults(this.getRollableResultsExceptBlock());
    }

    public List<ProcessingOutput> getRollableResultsExceptBlock() {
        ProcessingOutput mainOutput = this.results.get(0);
        return (List<ProcessingOutput>) (mainOutput.getStack().getItem() instanceof BlockItem ? this.results.subList(1, this.results.size()) : this.results);
    }
}