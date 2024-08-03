package net.mehvahdjukaar.amendments.events.behaviors;

import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

class DirectionalCakeConversion implements BlockUse {

    @Override
    public boolean altersWorld() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (Boolean) CommonConfigs.DIRECTIONAL_CAKE.get();
    }

    @Override
    public boolean appliesToBlock(Block block) {
        return block == Blocks.CAKE || block.builtInRegistryHolder().is(BlockTags.CANDLE_CAKES) && Utils.getID(block).getNamespace().equals("minecraft");
    }

    @Override
    public InteractionResult tryPerformingAction(BlockState state, BlockPos pos, Level world, Player player, InteractionHand hand, ItemStack stack, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else if (state.m_204336_(BlockTags.CANDLE_CAKES) && stack.is(ItemTags.CANDLES)) {
            return InteractionResult.PASS;
        } else if (!state.m_60713_(Blocks.CAKE) || !stack.is(ItemTags.CANDLES) && player.m_6350_() != Direction.EAST && (Integer) state.m_61143_(CakeBlock.BITES) == 0) {
            if ((Boolean) CommonConfigs.DOUBLE_CAKES.get() && stack.is(Items.CAKE)) {
                return InteractionResult.PASS;
            } else {
                BlockState newState = ((Block) ModRegistry.DIRECTIONAL_CAKE.get()).defaultBlockState();
                if (world.isClientSide) {
                    world.setBlock(pos, newState, 3);
                }
                BlockHitResult raytrace = new BlockHitResult(new Vec3((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_()), hit.getDirection(), pos, false);
                InteractionResult r = newState.m_60664_(world, player, hand, raytrace);
                if (world instanceof ServerLevel serverLevel) {
                    if (r.consumesAction()) {
                        Block.getDrops(state, serverLevel, pos, null).forEach(d -> {
                            if (d.getItem() != Items.CAKE) {
                                Block.popResource(world, pos, d);
                            }
                        });
                        state.m_222967_(serverLevel, pos, ItemStack.EMPTY, true);
                    } else {
                        world.setBlock(pos, state, 3);
                    }
                }
                return r;
            }
        } else {
            return InteractionResult.PASS;
        }
    }
}