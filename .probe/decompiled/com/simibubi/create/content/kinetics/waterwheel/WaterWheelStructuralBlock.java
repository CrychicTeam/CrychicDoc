package com.simibubi.create.content.kinetics.waterwheel;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.goggles.IProxyHoveringInformation;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.render.MultiPosDestructionHandler;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import org.jetbrains.annotations.Nullable;

public class WaterWheelStructuralBlock extends DirectionalBlock implements IWrenchable, IProxyHoveringInformation {

    public WaterWheelStructuralBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.m_7926_(pBuilder.add(f_52588_));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return AllBlocks.LARGE_WATER_WHEEL.asStack();
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();
        if (this.stillValid(level, clickedPos, state, false)) {
            BlockPos masterPos = getMaster(level, clickedPos, state);
            context = new UseOnContext(level, context.getPlayer(), context.getHand(), context.getItemInHand(), new BlockHitResult(context.getClickLocation(), context.getClickedFace(), masterPos, context.isInside()));
            state = level.getBlockState(masterPos);
        }
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!this.stillValid(pLevel, pPos, pState, false)) {
            return InteractionResult.FAIL;
        } else {
            return pLevel.getBlockEntity(getMaster(pLevel, pPos, pState)) instanceof WaterWheelBlockEntity wwt ? wwt.applyMaterialIfValid(pPlayer.m_21120_(pHand)) : InteractionResult.FAIL;
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (this.stillValid(pLevel, pPos, pState, false)) {
            pLevel.m_46961_(getMaster(pLevel, pPos, pState), true);
        }
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (this.stillValid(pLevel, pPos, pState, false)) {
            BlockPos masterPos = getMaster(pLevel, pPos, pState);
            pLevel.destroyBlockProgress(masterPos.hashCode(), masterPos, -1);
            if (!pLevel.isClientSide() && pPlayer.isCreative()) {
                pLevel.m_46961_(masterPos, false);
            }
        }
        super.m_5707_(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (this.stillValid(pLevel, pCurrentPos, pState, false)) {
            BlockPos masterPos = getMaster(pLevel, pCurrentPos, pState);
            if (!pLevel.getBlockTicks().m_183582_(masterPos, (Block) AllBlocks.LARGE_WATER_WHEEL.get())) {
                pLevel.scheduleTick(masterPos, (Block) AllBlocks.LARGE_WATER_WHEEL.get(), 1);
            }
            return pState;
        } else {
            if (pLevel instanceof Level level && !level.isClientSide()) {
                if (!level.m_183326_().m_183582_(pCurrentPos, this)) {
                    level.m_186460_(pCurrentPos, this, 1);
                }
                return pState;
            }
            return pState;
        }
    }

    public static BlockPos getMaster(BlockGetter level, BlockPos pos, BlockState state) {
        Direction direction = (Direction) state.m_61143_(f_52588_);
        BlockPos targetedPos = pos.relative(direction);
        BlockState targetedState = level.getBlockState(targetedPos);
        return targetedState.m_60713_((Block) AllBlocks.WATER_WHEEL_STRUCTURAL.get()) ? getMaster(level, targetedPos, targetedState) : targetedPos;
    }

    public boolean stillValid(BlockGetter level, BlockPos pos, BlockState state, boolean directlyAdjacent) {
        if (!state.m_60713_(this)) {
            return false;
        } else {
            Direction direction = (Direction) state.m_61143_(f_52588_);
            BlockPos targetedPos = pos.relative(direction);
            BlockState targetedState = level.getBlockState(targetedPos);
            return !directlyAdjacent && this.stillValid(level, targetedPos, targetedState, true) ? true : targetedState.m_60734_() instanceof LargeWaterWheelBlock && targetedState.m_61143_(LargeWaterWheelBlock.AXIS) != direction.getAxis();
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!this.stillValid(pLevel, pPos, pState, false)) {
            pLevel.m_46597_(pPos, Blocks.AIR.defaultBlockState());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new WaterWheelStructuralBlock.RenderProperties());
    }

    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        return true;
    }

    @Override
    public BlockPos getInformationSource(Level level, BlockPos pos, BlockState state) {
        return this.stillValid(level, pos, state, false) ? getMaster(level, pos, state) : pos;
    }

    public static class RenderProperties implements IClientBlockExtensions, MultiPosDestructionHandler {

        @Override
        public boolean addDestroyEffects(BlockState state, Level Level, BlockPos pos, ParticleEngine manager) {
            return true;
        }

        @Override
        public boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
            if (target instanceof BlockHitResult bhr) {
                BlockPos targetPos = bhr.getBlockPos();
                WaterWheelStructuralBlock waterWheelStructuralBlock = (WaterWheelStructuralBlock) AllBlocks.WATER_WHEEL_STRUCTURAL.get();
                if (waterWheelStructuralBlock.stillValid(level, targetPos, state, false)) {
                    manager.crack(WaterWheelStructuralBlock.getMaster(level, targetPos, state), bhr.getDirection());
                }
                return true;
            } else {
                return IClientBlockExtensions.super.addHitEffects(state, level, target, manager);
            }
        }

        @Nullable
        @Override
        public Set<BlockPos> getExtraPositions(ClientLevel level, BlockPos pos, BlockState blockState, int progress) {
            WaterWheelStructuralBlock waterWheelStructuralBlock = (WaterWheelStructuralBlock) AllBlocks.WATER_WHEEL_STRUCTURAL.get();
            if (!waterWheelStructuralBlock.stillValid(level, pos, blockState, false)) {
                return null;
            } else {
                HashSet<BlockPos> set = new HashSet();
                set.add(WaterWheelStructuralBlock.getMaster(level, pos, blockState));
                return set;
            }
        }
    }
}