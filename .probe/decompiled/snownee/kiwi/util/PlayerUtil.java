package snownee.kiwi.util;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

public final class PlayerUtil {

    private PlayerUtil() {
    }

    @Nullable
    public static BlockPos tryPlace(Level world, BlockPos pos, Direction side, @Nullable Player player, InteractionHand hand, BlockState state, @Nullable ItemStack stack, boolean playSound, boolean skipCollisionCheck) {
        BlockState worldState = world.getBlockState(pos);
        if ((worldState.m_60734_() != Blocks.SNOW || !worldState.m_61138_(SnowLayerBlock.LAYERS) || (Integer) worldState.m_61143_(SnowLayerBlock.LAYERS) >= 8) && !state.m_60629_(new DirectionalPlaceContext(world, pos, side.getOpposite(), stack == null ? ItemStack.EMPTY : stack, side.getOpposite()))) {
            pos = pos.relative(side);
        }
        if (skipCollisionCheck) {
            return tryPlace(world, pos, side.getOpposite(), player, hand, state, stack, playSound) ? pos : null;
        } else {
            CollisionContext iselectioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
            if (world.m_45752_(state, pos, iselectioncontext)) {
                return tryPlace(world, pos, side.getOpposite(), player, hand, state, stack, playSound) ? pos : null;
            } else {
                return null;
            }
        }
    }

    public static boolean tryPlace(Level world, BlockPos pos, Direction direction, @Nullable Player player, InteractionHand hand, BlockState state, @Nullable ItemStack stack, boolean playSound) {
        if (!world.mayInteract(player, pos)) {
            return false;
        } else if (player != null && !player.mayUseItemAt(pos, direction, stack)) {
            return false;
        } else {
            BlockSnapshot blocksnapshot = BlockSnapshot.create(world.dimension(), world, pos);
            if (!world.setBlockAndUpdate(pos, state)) {
                return false;
            } else if (ForgeEventFactory.onBlockPlace(player, blocksnapshot, direction)) {
                blocksnapshot.restore(true, false);
                return false;
            } else {
                world.setBlock(pos, state, 11);
                BlockState actualState = world.getBlockState(pos);
                if (stack != null) {
                    BlockItem.updateCustomBlockEntityTag(world, player, pos, stack);
                    if (player != null) {
                        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, stack);
                        }
                        actualState.m_60734_().setPlacedBy(world, pos, state, player, stack);
                    }
                    if (player == null || !player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                if (playSound) {
                    SoundType soundtype = actualState.m_60734_().getSoundType(actualState, world, pos, player);
                    world.playSound(player, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                }
                return true;
            }
        }
    }
}