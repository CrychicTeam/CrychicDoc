package com.simibubi.create.content.decoration;

import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.function.Predicate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;

public class MetalLadderBlock extends LadderBlock implements IWrenchable {

    private static final int placementHelperId = PlacementHelpers.register(new MetalLadderBlock.PlacementHelper());

    public MetalLadderBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean supportsExternalFaceHiding(BlockState state) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pDirection) {
        return pDirection == Direction.UP && pAdjacentBlockState.m_60734_() instanceof LadderBlock;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (!player.m_6144_() && player.mayBuild()) {
            ItemStack heldItem = player.m_21120_(hand);
            IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
            return helper.matchesItem(heldItem) ? helper.getOffset(player, world, state, pos, ray).placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray) : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem && ((BlockItem) i.getItem()).getBlock() instanceof MetalLadderBlock;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> s.m_60734_() instanceof LadderBlock;
        }

        public int attachedLadders(Level world, BlockPos pos, Direction direction) {
            BlockPos checkPos = pos.relative(direction);
            BlockState state = world.getBlockState(checkPos);
            int count;
            for (count = 0; this.getStatePredicate().test(state); state = world.getBlockState(checkPos)) {
                count++;
                checkPos = checkPos.relative(direction);
            }
            return count;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            Direction dir = player.m_146909_() < 0.0F ? Direction.UP : Direction.DOWN;
            int range = AllConfigs.server().equipment.placementAssistRange.get();
            if (player != null) {
                AttributeInstance reach = player.m_21051_(ForgeMod.BLOCK_REACH.get());
                if (reach != null && reach.hasModifier(ExtendoGripItem.singleRangeAttributeModifier)) {
                    range += 4;
                }
            }
            int ladders = this.attachedLadders(world, pos, dir);
            if (ladders >= range) {
                return PlacementOffset.fail();
            } else {
                BlockPos newPos = pos.relative(dir, ladders + 1);
                BlockState newState = world.getBlockState(newPos);
                if (!state.m_60710_(world, newPos)) {
                    return PlacementOffset.fail();
                } else {
                    return newState.m_247087_() ? PlacementOffset.success(newPos, bState -> (BlockState) bState.m_61124_(LadderBlock.FACING, (Direction) state.m_61143_(LadderBlock.FACING))) : PlacementOffset.fail();
                }
            }
        }
    }
}