package org.violetmoon.quark.addons.oddities.magnetsystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.addons.oddities.block.MovingMagnetizedBlock;
import org.violetmoon.quark.addons.oddities.block.be.MagnetBlockEntity;
import org.violetmoon.quark.addons.oddities.module.MagnetsModule;
import org.violetmoon.quark.api.IMagnetMoveAction;
import org.violetmoon.quark.api.IMagnetTracker;
import org.violetmoon.quark.api.QuarkCapabilities;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.api.ICollateralMover;
import org.violetmoon.zeta.event.play.ZRecipeCrawl;
import org.violetmoon.zeta.util.RegistryUtil;

public class MagnetSystem {

    private static final HashSet<Block> magnetizableBlocks = new HashSet();

    private static final HashSet<Item> magnetizableItems = new HashSet();

    private static final HashMap<Block, IMagnetMoveAction> BLOCK_MOVE_ACTIONS = new HashMap();

    public static IMagnetMoveAction getMoveAction(Block block) {
        return block instanceof IMagnetMoveAction ? (IMagnetMoveAction) block : (IMagnetMoveAction) BLOCK_MOVE_ACTIONS.get(block);
    }

    @Nullable
    public static IMagnetTracker getTracker(Level level) {
        return Quark.ZETA.capabilityManager.getCapability(QuarkCapabilities.MAGNET_TRACKER_CAPABILITY, level);
    }

    public static void tick(boolean start, Level level) {
        IMagnetTracker tracker = getTracker(level);
        if (tracker != null) {
            if (!start) {
                for (BlockPos pos : tracker.getTrackedPositions()) {
                    tracker.actOnForces(pos);
                }
            }
            tracker.clear();
        }
    }

    public static void onRecipeReset() {
        magnetizableBlocks.clear();
    }

    public static void onDigest(ZRecipeCrawl.Digest digest) {
        digest.recursivelyFindCraftedItemsFromStrings(MagnetsModule.magneticDerivationList, Collections.emptyList(), Collections.emptyList(), i -> {
            if (i instanceof BlockItem bi) {
                magnetizableBlocks.add(bi.getBlock());
            }
            magnetizableItems.add(i);
        });
        List<Block> magneticBlockWhitelist = RegistryUtil.massRegistryGet(MagnetsModule.magneticWhitelist, BuiltInRegistries.BLOCK);
        List<Block> magneticBlockBlacklist = RegistryUtil.massRegistryGet(MagnetsModule.magneticBlacklist, BuiltInRegistries.BLOCK);
        magnetizableBlocks.addAll(magneticBlockWhitelist);
        magneticBlockBlacklist.forEach(magnetizableBlocks::remove);
        List<Item> magneticItemWhitelist = RegistryUtil.massRegistryGet(MagnetsModule.magneticWhitelist, BuiltInRegistries.ITEM);
        List<Item> magneticItemBlacklist = RegistryUtil.massRegistryGet(MagnetsModule.magneticBlacklist, BuiltInRegistries.ITEM);
        magnetizableItems.addAll(magneticItemWhitelist);
        magneticItemBlacklist.forEach(magnetizableItems::remove);
    }

    public static void applyForce(Level world, BlockPos pos, int magnitude, boolean pushing, Direction dir, int distance, BlockPos origin) {
        IMagnetTracker tracker = getTracker(world);
        if (tracker != null) {
            tracker.applyForce(pos, magnitude, pushing, dir, distance, origin);
        }
    }

    public static ICollateralMover.MoveResult getPushAction(MagnetBlockEntity magnet, BlockPos pos, BlockState state, Direction moveDir) {
        if (state.m_60734_() instanceof MovingMagnetizedBlock) {
            return ICollateralMover.MoveResult.SKIP;
        } else {
            Level world = magnet.m_58904_();
            if (world != null && canBlockBeMagneticallyMoved(state, pos, world, moveDir, magnet)) {
                BlockPos frontPos = pos.relative(moveDir);
                BlockState frontState = world.getBlockState(frontPos);
                if (state.m_60734_() instanceof ICollateralMover cm && cm.isCollateralMover(world, magnet.m_58899_(), moveDir, pos)) {
                    return cm.getCollateralMovement(world, magnet.m_58899_(), moveDir, moveDir, pos);
                }
                if (frontState.m_60795_()) {
                    return ICollateralMover.MoveResult.MOVE;
                }
                if (frontState.m_60811_() == PushReaction.DESTROY) {
                    return ICollateralMover.MoveResult.BREAK;
                }
            }
            return ICollateralMover.MoveResult.SKIP;
        }
    }

    public static boolean isItemMagnetic(Item item) {
        return item == Items.AIR ? false : magnetizableItems.contains(item);
    }

    public static boolean isBlockMagnetic(BlockState state) {
        Block block = state.m_60734_();
        return block != MagnetsModule.magnet && !state.m_60795_() ? magnetizableBlocks.contains(block) || BLOCK_MOVE_ACTIONS.containsKey(block) : false;
    }

    public static boolean canBlockBeMagneticallyMoved(BlockState state, BlockPos pos, Level level, Direction moveDir, BlockEntity magnet) {
        Block block = state.m_60734_();
        if ((block == Blocks.PISTON || block == Blocks.STICKY_PISTON) && (Boolean) state.m_61143_(PistonBaseBlock.EXTENDED)) {
            return false;
        } else if (block != MagnetsModule.magnet && !state.m_60795_()) {
            IMagnetMoveAction action = getMoveAction(block);
            return action != null ? action.canMagnetMove(level, pos, moveDir, state, magnet) : magnetizableBlocks.contains(block);
        } else {
            return false;
        }
    }

    static {
        DefaultMoveActions.addActions(BLOCK_MOVE_ACTIONS);
    }
}