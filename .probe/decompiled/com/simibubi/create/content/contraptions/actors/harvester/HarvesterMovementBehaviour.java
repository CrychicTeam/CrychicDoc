package com.simibubi.create.content.contraptions.actors.harvester;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class HarvesterMovementBehaviour implements MovementBehaviour {

    @Override
    public boolean isActive(MovementContext context) {
        return MovementBehaviour.super.isActive(context) && !VecHelper.isVecPointingTowards(context.relativeMotion, ((Direction) context.state.m_61143_(HarvesterBlock.f_54117_)).getOpposite());
    }

    @Override
    public boolean hasSpecialInstancedRendering() {
        return true;
    }

    @Nullable
    @Override
    public ActorInstance createInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        return new HarvesterActorInstance(materialManager, simulationWorld, context);
    }

    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffers) {
        if (!ContraptionRenderDispatcher.canInstance()) {
            HarvesterRenderer.renderInContraption(context, renderWorld, matrices, buffers);
        }
    }

    @Override
    public Vec3 getActiveAreaOffset(MovementContext context) {
        return Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(HarvesterBlock.f_54117_)).getNormal()).scale(0.45);
    }

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        Level world = context.world;
        BlockState stateVisited = world.getBlockState(pos);
        boolean notCropButCuttable = false;
        if (!world.isClientSide) {
            if (!this.isValidCrop(world, pos, stateVisited)) {
                if (!this.isValidOther(world, pos, stateVisited)) {
                    return;
                }
                notCropButCuttable = true;
            }
            ItemStack item = ItemStack.EMPTY;
            float effectChance = 1.0F;
            if (stateVisited.m_204336_(BlockTags.LEAVES)) {
                item = new ItemStack(Items.SHEARS);
                effectChance = 0.45F;
            }
            MutableBoolean seedSubtracted = new MutableBoolean(notCropButCuttable);
            BlockHelper.destroyBlockAs(world, pos, null, item, effectChance, stack -> {
                if (AllConfigs.server().kinetics.harvesterReplants.get() && !seedSubtracted.getValue() && ItemHelper.sameItem(stack, new ItemStack(stateVisited.m_60734_()))) {
                    stack.shrink(1);
                    seedSubtracted.setTrue();
                }
                this.dropItem(context, stack);
            });
            BlockState cutCrop = this.cutCrop(world, pos, stateVisited);
            world.setBlockAndUpdate(pos, cutCrop.m_60710_(world, pos) ? cutCrop : Blocks.AIR.defaultBlockState());
        }
    }

    public boolean isValidCrop(Level world, BlockPos pos, BlockState state) {
        boolean harvestPartial = AllConfigs.server().kinetics.harvestPartiallyGrown.get();
        boolean replant = AllConfigs.server().kinetics.harvesterReplants.get();
        if (state.m_60734_() instanceof CropBlock) {
            CropBlock crop = (CropBlock) state.m_60734_();
            return !harvestPartial ? crop.isMaxAge(state) : state != crop.getStateForAge(0) || !replant;
        } else {
            if (state.m_60812_(world, pos).isEmpty() || state.m_60734_() instanceof CocoaBlock) {
                for (Property<?> property : state.m_61147_()) {
                    if (property instanceof IntegerProperty) {
                        IntegerProperty ageProperty = (IntegerProperty) property;
                        if (property.getName().equals(BlockStateProperties.AGE_1.m_61708_())) {
                            int age = (Integer) state.m_61143_(ageProperty);
                            if ((!(state.m_60734_() instanceof SweetBerryBushBlock) || age > 1 || !replant) && (age != 0 || !replant) && (harvestPartial || ageProperty.getPossibleValues().size() - 1 == age)) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    public boolean isValidOther(Level world, BlockPos pos, BlockState state) {
        if (state.m_60734_() instanceof CropBlock) {
            return false;
        } else if (state.m_60734_() instanceof SugarCaneBlock) {
            return true;
        } else if (state.m_204336_(BlockTags.LEAVES)) {
            return true;
        } else if (state.m_60734_() instanceof CocoaBlock) {
            return (Integer) state.m_61143_(CocoaBlock.AGE) == 2;
        } else {
            if (state.m_60812_(world, pos).isEmpty()) {
                if (state.m_60734_() instanceof GrowingPlantBlock) {
                    return true;
                }
                for (Property<?> property : state.m_61147_()) {
                    if (property instanceof IntegerProperty && property.getName().equals(BlockStateProperties.AGE_1.m_61708_())) {
                        return false;
                    }
                }
                if (state.m_60734_() instanceof IPlantable) {
                    return true;
                }
            }
            return false;
        }
    }

    private BlockState cutCrop(Level world, BlockPos pos, BlockState state) {
        if (!AllConfigs.server().kinetics.harvesterReplants.get()) {
            return state.m_60819_().isEmpty() ? Blocks.AIR.defaultBlockState() : state.m_60819_().createLegacyBlock();
        } else {
            Block block = state.m_60734_();
            if (block instanceof CropBlock crop) {
                return crop.getStateForAge(0);
            } else if (block == Blocks.SWEET_BERRY_BUSH) {
                return (BlockState) state.m_61124_(BlockStateProperties.AGE_3, 1);
            } else if (block != Blocks.SUGAR_CANE && !(block instanceof GrowingPlantBlock)) {
                if (state.m_60812_(world, pos).isEmpty() || block instanceof CocoaBlock) {
                    for (Property<?> property : state.m_61147_()) {
                        if (property instanceof IntegerProperty && property.getName().equals(BlockStateProperties.AGE_1.m_61708_())) {
                            return (BlockState) state.m_61124_(property, 0);
                        }
                    }
                }
                return state.m_60819_().isEmpty() ? Blocks.AIR.defaultBlockState() : state.m_60819_().createLegacyBlock();
            } else {
                return state.m_60819_().isEmpty() ? Blocks.AIR.defaultBlockState() : state.m_60819_().createLegacyBlock();
            }
        }
    }
}