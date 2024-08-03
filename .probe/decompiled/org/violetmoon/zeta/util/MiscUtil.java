package org.violetmoon.zeta.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.LinkedHashSet;
import java.util.Set;
import math.fast.SpeedyMath;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

public class MiscUtil {

    public static final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };

    public static final DyeColor[] CREATIVE_COLOR_ORDER = new DyeColor[] { DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.GRAY, DyeColor.BLACK, DyeColor.BROWN, DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.GREEN, DyeColor.CYAN, DyeColor.LIGHT_BLUE, DyeColor.BLUE, DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK };

    public static BooleanProperty directionProperty(Direction direction) {
        return switch(direction) {
            case DOWN ->
                BlockStateProperties.DOWN;
            case UP ->
                BlockStateProperties.UP;
            case NORTH ->
                BlockStateProperties.NORTH;
            case SOUTH ->
                BlockStateProperties.SOUTH;
            case WEST ->
                BlockStateProperties.WEST;
            case EAST ->
                BlockStateProperties.EAST;
        };
    }

    public static void addGoalJustAfterLatestWithPriority(GoalSelector selector, int priority, Goal goal) {
        Set<WrappedGoal> allGoals = new LinkedHashSet(selector.getAvailableGoals());
        WrappedGoal latestWithPriority = null;
        for (WrappedGoal wrappedGoal : allGoals) {
            if (wrappedGoal.getPriority() == priority) {
                latestWithPriority = wrappedGoal;
            }
        }
        selector.removeAllGoals(g -> true);
        if (latestWithPriority == null) {
            selector.addGoal(priority, goal);
        }
        for (WrappedGoal wrappedGoalx : allGoals) {
            selector.addGoal(wrappedGoalx.getPriority(), wrappedGoalx.getGoal());
            if (wrappedGoalx == latestWithPriority) {
                selector.addGoal(priority, goal);
            }
        }
    }

    public static void damageStack(Player player, InteractionHand hand, ItemStack stack, int dmg) {
        stack.hurtAndBreak(dmg, player, p -> p.m_21190_(hand));
    }

    public static Vec2 getMinecraftAngles(Vec3 direction) {
        direction = direction.normalize();
        double pitch = Math.asin(direction.y);
        double yaw = Math.asin(direction.x / Math.cos(pitch));
        return new Vec2((float) (pitch * 180.0 / Math.PI), (float) (-yaw * 180.0 / Math.PI));
    }

    public static Vec2 getMinecraftAnglesLossy(Vec3 direction) {
        direction = direction.normalize();
        double pitch = SpeedyMath.asin(direction.y);
        double yaw = SpeedyMath.asin(direction.x / SpeedyMath.cos(pitch));
        return new Vec2((float) (pitch * 180.0 / Math.PI), (float) (-yaw * 180.0 / Math.PI));
    }

    public static boolean validSpawnLight(ServerLevelAccessor world, BlockPos pos, RandomSource rand) {
        if (world.m_45517_(LightLayer.SKY, pos) > rand.nextInt(32)) {
            return false;
        } else {
            int light = world.getLevel().m_46470_() ? world.m_46849_(pos, 10) : world.m_46803_(pos);
            return light == 0;
        }
    }

    public static boolean validSpawnLocation(@NotNull EntityType<? extends Mob> type, @NotNull LevelAccessor world, MobSpawnType reason, BlockPos pos) {
        BlockPos below = pos.below();
        if (reason == MobSpawnType.SPAWNER) {
            return true;
        } else {
            BlockState state = world.m_8055_(below);
            return BlockUtils.isStoneBased(state, world, below) && state.m_60643_(world, below, type);
        }
    }

    public static void syncTE(BlockEntity tile) {
        Packet<ClientGamePacketListener> packet = tile.getUpdatePacket();
        if (packet != null && tile.getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(tile.getBlockPos()), false).forEach(e -> e.connection.send(packet));
        }
    }

    public static ItemStack putIntoInv(ItemStack stack, LevelAccessor level, BlockPos blockPos, BlockEntity tile, Direction face, boolean simulate, boolean doSimulation) {
        IItemHandler handler = null;
        if (level != null && blockPos != null && level.m_8055_(blockPos).m_60734_() instanceof WorldlyContainerHolder holder) {
            handler = new SidedInvWrapper(holder.getContainer(level.m_8055_(blockPos), level, blockPos), face);
        } else if (tile != null) {
            LazyOptional<IItemHandler> opt = tile.getCapability(ForgeCapabilities.ITEM_HANDLER, face);
            if (opt.isPresent()) {
                handler = opt.orElse(new ItemStackHandler());
            } else if (tile instanceof WorldlyContainer container) {
                handler = new SidedInvWrapper(container, face);
            } else if (tile instanceof Container container) {
                handler = new InvWrapper(container);
            }
        }
        if (handler == null) {
            return stack;
        } else {
            return simulate && !doSimulation ? ItemStack.EMPTY : ItemHandlerHelper.insertItem(handler, stack, simulate);
        }
    }

    public static boolean canPutIntoInv(ItemStack stack, LevelAccessor level, BlockPos blockPos, BlockEntity tile, Direction face, boolean doSimulation) {
        return putIntoInv(stack, level, blockPos, tile, face, true, doSimulation).isEmpty();
    }

    public static BlockState fromString(String key) {
        try {
            BlockStateParser.BlockResult result = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.m_255303_(), new StringReader(key), false);
            BlockState state = result.blockState();
            return state == null ? Blocks.AIR.defaultBlockState() : state;
        } catch (CommandSyntaxException var3) {
            return Blocks.AIR.defaultBlockState();
        }
    }

    public static BlockBehaviour.Properties copyPropertySafe(BlockBehaviour blockBehaviour) {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.copy(blockBehaviour);
        p.lightLevel(s -> 0);
        p.offsetType(BlockBehaviour.OffsetType.NONE);
        p.mapColor(blockBehaviour.defaultMapColor());
        return p;
    }
}