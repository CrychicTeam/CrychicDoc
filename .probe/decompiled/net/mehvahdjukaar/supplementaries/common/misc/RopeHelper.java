package net.mehvahdjukaar.supplementaries.common.misc;

import net.mehvahdjukaar.supplementaries.common.block.blocks.PulleyBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.PulleyBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.ItemsUtil;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RopeHelper {

    public static boolean addRopeDown(BlockPos pos, Level level, @Nullable Player player, InteractionHand hand, Block ropeBlock) {
        return addRope(pos, level, player, hand, ropeBlock, Direction.DOWN, true, Integer.MAX_VALUE);
    }

    public static boolean addRope(BlockPos pos, Level level, @Nullable Player player, InteractionHand hand, Block ropeBlock, Direction moveDir, boolean canPush, int maxDist) {
        BlockState state = level.getBlockState(pos);
        if (maxDist <= 0) {
            return false;
        } else {
            maxDist--;
            if (isCorrectRope(ropeBlock, state, moveDir)) {
                return addRope(pos.relative(moveDir), level, player, hand, ropeBlock, moveDir, canPush, maxDist);
            } else {
                return state.m_60713_((Block) ModRegistry.PULLEY_BLOCK.get()) && level.getBlockEntity(pos) instanceof PulleyBlockTile te ? te.rotateIndirect(player, hand, ropeBlock, moveDir, false) : tryPlaceAndMove(player, hand, level, pos, ropeBlock, moveDir, canPush);
            }
        }
    }

    public static boolean isCorrectRope(Block ropeBlock, BlockState state, Direction direction) {
        return state.m_60734_() instanceof ChainBlock && state.m_61143_(ChainBlock.f_55923_) != direction.getAxis() ? false : ropeBlock == state.m_60734_();
    }

    public static boolean tryPlaceAndMove(@Nullable Player player, InteractionHand hand, Level world, BlockPos pos, Block ropeBlock, Direction moveDir, boolean canPush) {
        ItemStack stack = new ItemStack(ropeBlock);
        BlockPlaceContext context = new BlockPlaceContext(world, player, hand, stack, new BlockHitResult(Vec3.atCenterOf(pos), moveDir.getOpposite(), pos, false));
        if (!context.canPlace()) {
            BlockPos downPos = pos.relative(moveDir);
            if (!canPush || !world.getBlockState(downPos).m_247087_() || !tryMove(pos, downPos, world)) {
                return false;
            }
            context = new BlockPlaceContext(world, player, hand, stack, new BlockHitResult(Vec3.atCenterOf(pos), moveDir.getOpposite(), pos, false));
        }
        BlockState state = ItemsUtil.getPlacementState(context, ropeBlock);
        if (state == null) {
            return false;
        } else if (state == world.getBlockState(context.getClickedPos())) {
            return false;
        } else if (world.setBlock(context.getClickedPos(), state, 11)) {
            if (player != null) {
                BlockState placedState = world.getBlockState(context.getClickedPos());
                Block block = placedState.m_60734_();
                if (block == state.m_60734_()) {
                    block.setPlacedBy(world, context.getClickedPos(), placedState, player, stack);
                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, context.getClickedPos(), stack);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private static boolean isBlockMovable(BlockState state, Level level, BlockPos pos) {
        return state.m_60734_() instanceof PulleyBlock ? false : !state.m_60795_() && !state.m_60713_(Blocks.OBSIDIAN) && !state.m_60713_(Blocks.SPAWNER) && !state.m_60713_(Blocks.CRYING_OBSIDIAN) && !state.m_60713_(Blocks.RESPAWN_ANCHOR) && state.m_60800_(level, pos) != -1.0F;
    }

    public static boolean removeRopeDown(BlockPos pos, Level level, Block ropeBlock) {
        return removeRope(pos, level, ropeBlock, Direction.DOWN, Integer.MAX_VALUE);
    }

    public static boolean removeRope(BlockPos pos, Level level, Block ropeBlock, Direction moveUpDir, int maxDist) {
        if (maxDist <= 0) {
            return false;
        } else {
            maxDist--;
            BlockState state = level.getBlockState(pos);
            if (isCorrectRope(ropeBlock, state, moveUpDir)) {
                return removeRope(pos.relative(moveUpDir), level, ropeBlock, moveUpDir, maxDist);
            } else if (state.m_60713_((Block) ModRegistry.PULLEY_BLOCK.get()) && level.getBlockEntity(pos) instanceof PulleyBlockTile te && !te.m_7983_()) {
                return te.rotateIndirect(null, InteractionHand.MAIN_HAND, ropeBlock, moveUpDir, true);
            } else {
                BlockPos up = pos.relative(moveUpDir.getOpposite());
                if (level.getBlockState(up).m_60734_() != ropeBlock) {
                    return false;
                } else {
                    FluidState fromFluid = level.getFluidState(up);
                    boolean water = fromFluid.getType() == Fluids.WATER && fromFluid.isSource();
                    level.setBlockAndUpdate(up, water ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
                    tryMove(pos, up, level);
                    return true;
                }
            }
        }
    }

    private static boolean tryMove(BlockPos fromPos, BlockPos toPos, Level world) {
        if (toPos.m_123342_() >= world.m_141937_() && toPos.m_123342_() <= world.m_151558_()) {
            BlockState state = world.getBlockState(fromPos);
            PushReaction push = state.m_60811_();
            if (isBlockMovable(state, world, fromPos) && ((push == PushReaction.NORMAL || toPos.m_123342_() < fromPos.m_123342_() && push == PushReaction.PUSH_ONLY) && state.m_60710_(world, toPos) || state.m_204336_(ModTags.ROPE_HANG_TAG))) {
                BlockEntity tile = world.getBlockEntity(fromPos);
                if (tile != null) {
                    if (CompatHandler.QUARK && !QuarkCompat.canMoveBlockEntity(state)) {
                        return false;
                    }
                    tile.setRemoved();
                }
                Fluid fluidState = world.getFluidState(toPos).getType();
                boolean waterFluid = fluidState == Fluids.WATER;
                boolean canHoldWater = false;
                if (state.m_61138_(BlockStateProperties.WATERLOGGED)) {
                    canHoldWater = state.m_204336_(ModTags.WATER_HOLDER);
                    if (!canHoldWater) {
                        state = (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, waterFluid);
                    }
                } else if (state.m_60734_() instanceof AbstractCauldronBlock) {
                    if (waterFluid && state.m_60713_(Blocks.CAULDRON) || state.m_60713_(Blocks.WATER_CAULDRON)) {
                        state = (BlockState) Blocks.WATER_CAULDRON.defaultBlockState().m_61124_(LayeredCauldronBlock.LEVEL, 3);
                    }
                    if (fluidState == Fluids.LAVA && state.m_60713_(Blocks.CAULDRON) || state.m_60713_(Blocks.LAVA_CAULDRON)) {
                        state = Blocks.LAVA_CAULDRON.defaultBlockState();
                    }
                }
                FluidState fromFluid = world.getFluidState(fromPos);
                boolean leaveWater = fromFluid.getType() == Fluids.WATER && fromFluid.isSource() && !canHoldWater;
                world.setBlockAndUpdate(fromPos, leaveWater ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
                BlockState newState = Block.updateFromNeighbourShapes(state, world, toPos);
                world.setBlockAndUpdate(toPos, newState);
                if (tile != null) {
                    CompoundTag tag = tile.saveWithoutMetadata();
                    BlockEntity te = world.getBlockEntity(toPos);
                    if (te != null) {
                        te.load(tag);
                    }
                }
                world.neighborChanged(toPos, state.m_60734_(), toPos);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}