package com.mna.tools;

import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.api.tools.MATags;
import com.mna.entities.EntityInit;
import com.mna.entities.boss.PumpkinKing;
import com.mna.events.seasonal.SeasonalHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayerFactory;

public class ShearHelper {

    public static List<ItemStack> shearBlock(ServerLevel world, BlockPos position, Direction face, Player player) {
        return shearBlock(world, position, face, player, new ItemStack(Items.SHEARS));
    }

    public static List<ItemStack> shearBlock(ServerLevel world, BlockPos position, Direction face, Player player, ItemStack shearStack) {
        List<ItemStack> output = new ArrayList();
        if (world.m_5776_()) {
            return output;
        } else if (shearSpecial(world, position, face)) {
            return output;
        } else {
            BlockState targetState = world.m_8055_(position);
            if (targetState.m_60734_() instanceof IForgeShearable) {
                output = shearIForgeShearable((IForgeShearable) targetState.m_60734_(), player, position, shearStack, world);
                if (output.size() > 0) {
                    return output;
                }
            }
            BlockEntity te = world.m_7702_(position);
            if (te == null || !world.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent() && !(te instanceof TileEntityWithInventory)) {
                Player entity = FakePlayerFactory.getMinecraft(world);
                entity.getInventory().clearContent();
                entity.m_21008_(InteractionHand.MAIN_HAND, new ItemStack(Items.SHEARS));
                entity.m_6034_((double) position.m_123341_(), (double) position.m_123342_(), (double) position.m_123343_());
                output = new ArrayList();
                BlockHitResult brtr = new BlockHitResult(new Vec3((double) position.m_123341_() + 0.5, (double) position.m_123342_() + 0.5, (double) position.m_123343_() + 0.5), face, position, true);
                InteractionResult interRes = world.m_8055_(position).m_60664_(world, entity, InteractionHand.MAIN_HAND, brtr);
                if (interRes == InteractionResult.FAIL || interRes == InteractionResult.PASS || interRes == InteractionResult.CONSUME) {
                    List<ItemStack> shearDrops = Block.getDrops(targetState, world, position, te, entity, shearStack);
                    List<ItemStack> nonShearDrops = Block.getDrops(targetState, world, position, te, entity, ItemStack.EMPTY);
                    if (shearDrops.size() != nonShearDrops.size()) {
                        world.m_7471_(position, false);
                        output.addAll(shearDrops);
                    } else {
                        entity.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        for (ItemStack item : entity.getInventory().items) {
                            if (!item.isEmpty()) {
                                output.add(item);
                            }
                        }
                        for (ItemStack itemx : entity.getInventory().offhand) {
                            if (!itemx.isEmpty()) {
                                output.add(itemx);
                            }
                        }
                    }
                }
                return output;
            } else {
                return output;
            }
        }
    }

    public static List<ItemStack> shearEntity(ServerLevel world, Player player, Entity entity, InteractionHand hand) {
        ArrayList<ItemStack> output = new ArrayList();
        if (entity.level().isClientSide()) {
            return output;
        } else {
            return (List<ItemStack>) (entity instanceof IForgeShearable ? shearIForgeShearable((IForgeShearable) entity, player, entity.blockPosition(), player.m_21120_(hand), world) : output);
        }
    }

    public static boolean canBlockBeSheared(Level world, BlockState state, BlockPos pos) {
        if (state.m_60734_() instanceof IForgeShearable && ((IForgeShearable) state.m_60734_()).isShearable(new ItemStack(Items.SHEARS), world, pos)) {
            return true;
        } else {
            return state.m_60734_() instanceof BeehiveBlock ? (Integer) state.m_61143_(BeehiveBlock.HONEY_LEVEL) >= 5 : MATags.isBlockIn(state.m_60734_(), MATags.Blocks.SHEARABLES);
        }
    }

    private static List<ItemStack> shearIForgeShearable(IForgeShearable target, Player player, BlockPos pos, ItemStack heldItem, Level world) {
        List<ItemStack> output = new ArrayList();
        if (target.isShearable(heldItem, world, pos)) {
            int enchantmentLevel = heldItem.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
            output = target.onSheared(player, heldItem, world, pos, enchantmentLevel);
        }
        return output;
    }

    private static boolean shearSpecial(ServerLevel world, BlockPos pos, Direction face) {
        if (!SeasonalHelper.isHalloween()) {
            return false;
        } else if (world.m_142425_(EntityInit.PUMPKIN_KING.get(), new AABB(pos).inflate(16.0), e -> true).size() != 0) {
            return false;
        } else {
            for (int i = 1; i < 5; i++) {
                if (!world.m_46859_(pos.above(i))) {
                    return false;
                }
            }
            BlockState state = world.m_8055_(pos);
            if (state.m_60734_() == Blocks.PUMPKIN) {
                world.m_7471_(pos, false);
                PumpkinKing eds = new PumpkinKing(world);
                eds.m_6034_((double) ((float) pos.m_123341_() + 0.5F), (double) pos.m_123342_(), (double) ((float) pos.m_123343_() + 0.5F));
                eds.setupSpawn();
                switch(face) {
                    case EAST:
                        eds.m_146922_(270.0F);
                        break;
                    case NORTH:
                        eds.m_146922_(-180.0F);
                        break;
                    case SOUTH:
                        eds.m_146922_(0.0F);
                        break;
                    case WEST:
                    case UP:
                    case DOWN:
                    default:
                        eds.m_146922_(90.0F);
                }
                eds.f_19859_ = eds.m_146908_();
                eds.f_20883_ = eds.m_146908_();
                eds.f_20884_ = eds.m_146908_();
                world.addFreshEntity(eds);
                return true;
            } else {
                return false;
            }
        }
    }
}