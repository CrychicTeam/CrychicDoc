package com.simibubi.create.content.kinetics.deployer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

public class BeltDeployerCallbacks {

    public static BeltProcessingBehaviour.ProcessingResult onItemReceived(TransportedItemStack s, TransportedItemStackHandlerBehaviour i, DeployerBlockEntity blockEntity) {
        if (blockEntity.getSpeed() == 0.0F) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else if (blockEntity.mode == DeployerBlockEntity.Mode.PUNCH) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else {
            BlockState blockState = blockEntity.m_58900_();
            if (!blockState.m_61138_(DirectionalKineticBlock.FACING) || blockState.m_61143_(DirectionalKineticBlock.FACING) != Direction.DOWN) {
                return BeltProcessingBehaviour.ProcessingResult.PASS;
            } else if (blockEntity.state != DeployerBlockEntity.State.WAITING) {
                return BeltProcessingBehaviour.ProcessingResult.HOLD;
            } else if (blockEntity.redstoneLocked) {
                return BeltProcessingBehaviour.ProcessingResult.PASS;
            } else {
                DeployerFakePlayer player = blockEntity.getPlayer();
                ItemStack held = player == null ? ItemStack.EMPTY : player.m_21205_();
                if (held.isEmpty()) {
                    return BeltProcessingBehaviour.ProcessingResult.HOLD;
                } else if (blockEntity.getRecipe(s.stack) == null) {
                    return BeltProcessingBehaviour.ProcessingResult.PASS;
                } else {
                    blockEntity.start();
                    return BeltProcessingBehaviour.ProcessingResult.HOLD;
                }
            }
        }
    }

    public static BeltProcessingBehaviour.ProcessingResult whenItemHeld(TransportedItemStack s, TransportedItemStackHandlerBehaviour i, DeployerBlockEntity blockEntity) {
        if (blockEntity.getSpeed() == 0.0F) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else {
            BlockState blockState = blockEntity.m_58900_();
            if (blockState.m_61138_(DirectionalKineticBlock.FACING) && blockState.m_61143_(DirectionalKineticBlock.FACING) == Direction.DOWN) {
                DeployerFakePlayer player = blockEntity.getPlayer();
                ItemStack held = player == null ? ItemStack.EMPTY : player.m_21205_();
                if (held.isEmpty()) {
                    return BeltProcessingBehaviour.ProcessingResult.HOLD;
                } else {
                    Recipe<?> recipe = blockEntity.getRecipe(s.stack);
                    if (recipe == null) {
                        return BeltProcessingBehaviour.ProcessingResult.PASS;
                    } else if (blockEntity.state == DeployerBlockEntity.State.RETRACTING && blockEntity.timer == 1000) {
                        activate(s, i, blockEntity, recipe);
                        return BeltProcessingBehaviour.ProcessingResult.HOLD;
                    } else {
                        if (blockEntity.state == DeployerBlockEntity.State.WAITING) {
                            if (blockEntity.redstoneLocked) {
                                return BeltProcessingBehaviour.ProcessingResult.PASS;
                            }
                            blockEntity.start();
                        }
                        return BeltProcessingBehaviour.ProcessingResult.HOLD;
                    }
                }
            } else {
                return BeltProcessingBehaviour.ProcessingResult.PASS;
            }
        }
    }

    public static void activate(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler, DeployerBlockEntity blockEntity, Recipe<?> recipe) {
        List<TransportedItemStack> collect = (List<TransportedItemStack>) RecipeApplier.applyRecipeOn(blockEntity.m_58904_(), ItemHandlerHelper.copyStackWithSize(transported.stack, 1), recipe).stream().map(stack -> {
            TransportedItemStack copy = transported.copy();
            boolean centered = BeltHelper.isItemUpright(stack);
            copy.stack = stack;
            copy.locked = true;
            copy.angle = centered ? 180 : Create.RANDOM.nextInt(360);
            return copy;
        }).map(t -> {
            t.locked = false;
            return t;
        }).collect(Collectors.toList());
        blockEntity.award(AllAdvancements.DEPLOYER);
        TransportedItemStack left = transported.copy();
        blockEntity.player.spawnedItemEffects = transported.stack.copy();
        left.stack.shrink(1);
        ItemStack resultItem = null;
        if (collect.isEmpty()) {
            resultItem = left.stack.copy();
            handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertTo(left));
        } else {
            resultItem = ((TransportedItemStack) collect.get(0)).stack.copy();
            handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertToAndLeaveHeld(collect, left));
        }
        ItemStack heldItem = blockEntity.player.m_21205_();
        boolean unbreakable = heldItem.hasTag() && heldItem.getTag().getBoolean("Unbreakable");
        boolean keepHeld = recipe instanceof ItemApplicationRecipe && ((ItemApplicationRecipe) recipe).shouldKeepHeldItem();
        if (!unbreakable && !keepHeld) {
            if (heldItem.isDamageableItem()) {
                heldItem.hurtAndBreak(1, blockEntity.player, s -> s.m_21190_(InteractionHand.MAIN_HAND));
            } else {
                heldItem.shrink(1);
            }
        }
        if (resultItem != null && !resultItem.isEmpty()) {
            awardAdvancements(blockEntity, resultItem);
        }
        BlockPos pos = blockEntity.m_58899_();
        Level world = blockEntity.m_58904_();
        if (heldItem.isEmpty()) {
            world.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 0.25F, 1.0F);
        }
        world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.25F, 0.75F);
        if (recipe instanceof SandPaperPolishingRecipe) {
            AllSoundEvents.SANDING_SHORT.playOnServer(world, pos, 0.35F, 1.0F);
        }
        blockEntity.sendData();
    }

    private static void awardAdvancements(DeployerBlockEntity blockEntity, ItemStack created) {
        CreateAdvancement advancement = null;
        if (AllBlocks.ANDESITE_CASING.isIn(created)) {
            advancement = AllAdvancements.ANDESITE_CASING;
        } else if (AllBlocks.BRASS_CASING.isIn(created)) {
            advancement = AllAdvancements.BRASS_CASING;
        } else if (AllBlocks.COPPER_CASING.isIn(created)) {
            advancement = AllAdvancements.COPPER_CASING;
        } else {
            if (!AllBlocks.RAILWAY_CASING.isIn(created)) {
                return;
            }
            advancement = AllAdvancements.TRAIN_CASING;
        }
        blockEntity.award(advancement);
    }
}