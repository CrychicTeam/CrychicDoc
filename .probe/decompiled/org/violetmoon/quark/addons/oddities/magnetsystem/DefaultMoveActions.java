package org.violetmoon.quark.addons.oddities.magnetsystem;

import com.mojang.authlib.GameProfile;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.violetmoon.quark.addons.oddities.module.MagnetsModule;
import org.violetmoon.quark.api.IMagnetMoveAction;

public class DefaultMoveActions {

    private static final GameProfile FAKE_PLAYER_PROFILE = new GameProfile(UUID.randomUUID(), "[MagnetStonecutter]");

    public static void addActions(HashMap<Block, IMagnetMoveAction> map) {
        map.put(Blocks.STONECUTTER, DefaultMoveActions::stonecutterMoved);
        map.put(Blocks.HOPPER, DefaultMoveActions::hopperMoved);
    }

    private static void stonecutterMoved(Level world, BlockPos pos, Direction direction, BlockState state, BlockEntity tile) {
        if (world instanceof ServerLevel serverLevel) {
            BlockPos up = pos.above();
            BlockState breakState = world.getBlockState(up);
            double hardness = (double) breakState.m_60800_(world, up);
            if (hardness > -1.0 && hardness < 3.0) {
                if (MagnetsModule.stoneCutterSilkTouch) {
                    destroyBlockWithSilkTouch(breakState, up, serverLevel, 512);
                } else {
                    world.m_46961_(up, true);
                }
            }
        }
    }

    private static boolean destroyBlockWithSilkTouch(BlockState blockstate, BlockPos pPos, ServerLevel level, int pRecursionLeft) {
        if (blockstate.m_60795_()) {
            return false;
        } else {
            FluidState fluidstate = level.m_6425_(pPos);
            if (!(blockstate.m_60734_() instanceof BaseFireBlock)) {
                level.m_46796_(2001, pPos, Block.getId(blockstate));
            }
            FakePlayer player = FakePlayerFactory.get(level, FAKE_PLAYER_PROFILE);
            ItemStack tool = Items.NETHERITE_PICKAXE.getDefaultInstance();
            EnchantmentHelper.setEnchantments(Map.of(Enchantments.SILK_TOUCH, 1), tool);
            player.m_21008_(InteractionHand.MAIN_HAND, tool);
            BlockEntity blockentity = blockstate.m_155947_() ? level.m_7702_(pPos) : null;
            Block.dropResources(blockstate, level, pPos, blockentity, player, tool);
            boolean flag = level.m_6933_(pPos, fluidstate.createLegacyBlock(), 3, pRecursionLeft);
            if (flag) {
                level.m_220407_(GameEvent.BLOCK_DESTROY, pPos, GameEvent.Context.of(null, blockstate));
            }
            return flag;
        }
    }

    private static void hopperMoved(Level world, BlockPos pos, Direction direction, BlockState state, BlockEntity tile) {
        if (!world.isClientSide && tile instanceof HopperBlockEntity hopper) {
            hopper.setCooldown(0);
            Direction dir = (Direction) state.m_61143_(HopperBlock.FACING);
            BlockPos offPos = pos.relative(dir);
            BlockPos targetPos = pos.relative(direction);
            if (offPos.equals(targetPos)) {
                return;
            }
            if (world.m_46859_(offPos)) {
                for (int i = 0; i < hopper.getContainerSize(); i++) {
                    ItemStack stack = hopper.m_8020_(i);
                    if (!stack.isEmpty()) {
                        ItemStack drop = stack.copy();
                        drop.setCount(1);
                        hopper.removeItem(i, 1);
                        boolean shouldDrop = true;
                        if (drop.getItem() instanceof BlockItem blockItem) {
                            BlockPos groundPos = offPos.below();
                            if (world.m_46859_(groundPos)) {
                                groundPos = groundPos.below();
                            }
                            Block seedType = blockItem.getBlock();
                            if (seedType instanceof IPlantable plantable) {
                                BlockState groundBlock = world.getBlockState(groundPos);
                                if (groundBlock.m_60734_().canSustainPlant(groundBlock, world, groundPos, Direction.UP, plantable)) {
                                    BlockPos seedPos = groundPos.above();
                                    if (state.m_60710_(world, seedPos)) {
                                        BlockState seedState = seedType.defaultBlockState();
                                        world.playSound(null, seedPos, seedType.getSoundType(seedState).getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                        boolean canPlace = true;
                                        if (seedState.m_60734_() instanceof DoublePlantBlock) {
                                            canPlace = false;
                                            BlockPos abovePos = seedPos.above();
                                            if (world.m_46859_(abovePos)) {
                                                world.setBlockAndUpdate(abovePos, (BlockState) seedState.m_61124_(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
                                                canPlace = true;
                                            }
                                        }
                                        if (canPlace) {
                                            world.setBlockAndUpdate(seedPos, seedState);
                                            shouldDrop = false;
                                        }
                                    }
                                }
                            }
                        }
                        if (shouldDrop) {
                            double x = (double) pos.m_123341_() + 0.5 + (double) dir.getStepX() * 0.7;
                            double y = (double) pos.m_123342_() + 0.15 + (double) dir.getStepY() * 0.4;
                            double z = (double) pos.m_123343_() + 0.5 + (double) dir.getStepZ() * 0.7;
                            ItemEntity entity = new ItemEntity(world, x, y, z, drop);
                            entity.m_20256_(Vec3.ZERO);
                            world.m_7967_(entity);
                        }
                        return;
                    }
                }
            }
        }
    }
}