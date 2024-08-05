package net.mehvahdjukaar.amendments.events.behaviors;

import java.util.function.Predicate;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.SuppCompat;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

class BellChainRing implements BlockUse {

    @Override
    public boolean isEnabled() {
        return (Boolean) CommonConfigs.BELL_CHAIN_RINGING.get();
    }

    @Override
    public boolean appliesToBlock(Block block) {
        return block instanceof ChainBlock || CompatHandler.SUPPLEMENTARIES && SuppCompat.isRope(block);
    }

    @Override
    public InteractionResult tryPerformingAction(BlockState state, BlockPos pos, Level world, Player player, InteractionHand hand, ItemStack stack, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else {
            if (stack.isEmpty() && hand == InteractionHand.MAIN_HAND) {
                Predicate<BlockState> predicate = state.m_60734_() instanceof ChainBlock ? BellChainRing::isVerticalChain : BellChainRing::isRope;
                if (findAndRingBell(world, pos, player, 0, predicate)) {
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
            }
            return InteractionResult.PASS;
        }
    }

    private static boolean isRope(BlockState state) {
        return CompatHandler.SUPPLEMENTARIES ? SuppCompat.isRope(state.m_60734_()) : false;
    }

    private static boolean isVerticalChain(BlockState s) {
        return s.m_60734_() instanceof ChainBlock && s.m_61143_(ChainBlock.f_55923_) == Direction.Axis.Y;
    }

    public static boolean findAndRingBell(Level world, BlockPos pos, Player player, int it, Predicate<BlockState> predicate) {
        if (it > (Integer) CommonConfigs.BELL_CHAIN_LENGTH.get()) {
            return false;
        } else {
            BlockState state = world.getBlockState(pos);
            Block b = state.m_60734_();
            if (predicate.test(state)) {
                return findAndRingBell(world, pos.above(), player, it + 1, predicate);
            } else {
                if (b instanceof BellBlock bellBlock && it != 0) {
                    Direction d = (Direction) state.m_61143_(BellBlock.FACING);
                    BellAttachType att = (BellAttachType) state.m_61143_(BellBlock.ATTACHMENT);
                    if (att == BellAttachType.SINGLE_WALL || att == BellAttachType.DOUBLE_WALL || !Utils.getID(b).getNamespace().equals("create")) {
                        d = d.getClockWise();
                    }
                    BlockHitResult hit = new BlockHitResult(new Vec3((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5), d, pos, true);
                    return bellBlock.onHit(world, state, hit, player, true);
                }
                return false;
            }
        }
    }
}