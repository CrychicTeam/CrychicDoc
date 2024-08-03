package org.violetmoon.zeta.piston;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.api.ICollateralMover;
import org.violetmoon.zeta.api.IConditionalSticky;
import org.violetmoon.zeta.api.IIndirectConnector;
import org.violetmoon.zeta.block.ext.IZetaBlockExtensions;
import org.violetmoon.zeta.mixin.mixins.AccessorPistonStructureResolver;

public class ZetaPistonStructureResolver extends PistonStructureResolver {

    private final PistonStructureResolver parent;

    private final Level world;

    private final BlockPos pistonPos;

    private final BlockPos blockToMove;

    private final Direction moveDirection;

    private final List<BlockPos> toMove = Lists.newArrayList();

    private final List<BlockPos> toDestroy = Lists.newArrayList();

    public ZetaPistonStructureResolver(PistonStructureResolver parent) {
        super(((AccessorPistonStructureResolver) parent).zeta$level(), ((AccessorPistonStructureResolver) parent).zeta$pistonPos(), ((AccessorPistonStructureResolver) parent).zeta$pistonDirection(), ((AccessorPistonStructureResolver) parent).zeta$extending());
        this.parent = parent;
        this.world = ((AccessorPistonStructureResolver) parent).zeta$level();
        this.pistonPos = ((AccessorPistonStructureResolver) parent).zeta$pistonPos();
        this.moveDirection = parent.getPushDirection();
        this.blockToMove = ((AccessorPistonStructureResolver) parent).zeta$startPos();
    }

    @Override
    public boolean resolve() {
        if (!ZetaPistonStructureResolver.GlobalSettings.isEnabled()) {
            return this.parent.resolve();
        } else {
            this.toMove.clear();
            this.toDestroy.clear();
            BlockState iblockstate = this.world.getBlockState(this.blockToMove);
            if (!PistonBaseBlock.isPushable(iblockstate, this.world, this.blockToMove, this.moveDirection, false, this.moveDirection)) {
                if (iblockstate.m_60811_() == PushReaction.DESTROY) {
                    this.toDestroy.add(this.blockToMove);
                    return true;
                } else {
                    return false;
                }
            } else if (!this.addBlockLine(this.blockToMove, this.moveDirection)) {
                return false;
            } else {
                for (int i = 0; i < this.toMove.size(); i++) {
                    BlockPos blockpos = (BlockPos) this.toMove.get(i);
                    if (this.addBranchingBlocks(this.world, blockpos, this.isBlockBranching(this.world, blockpos)) == ICollateralMover.MoveResult.PREVENT) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private boolean addBlockLine(BlockPos origin, Direction face) {
        int max = ZetaPistonStructureResolver.GlobalSettings.getPushLimit();
        BlockPos target = origin;
        BlockState state = this.world.getBlockState(origin);
        if (!state.m_60795_() && PistonBaseBlock.isPushable(state, this.world, origin, this.moveDirection, false, face) && !origin.equals(this.pistonPos) && !this.toMove.contains(origin)) {
            int lineLen = 1;
            if (lineLen + this.toMove.size() > max) {
                return false;
            } else {
                BlockPos oldPos = origin;
                BlockState oldState = this.world.getBlockState(origin);
                boolean skippingNext = false;
                while (this.isBlockBranching(this.world, target)) {
                    ICollateralMover.MoveResult res = this.getBranchResult(this.world, target);
                    if (res == ICollateralMover.MoveResult.PREVENT) {
                        return false;
                    }
                    if (res != ICollateralMover.MoveResult.MOVE) {
                        skippingNext = true;
                        break;
                    }
                    target = origin.relative(this.moveDirection.getOpposite(), lineLen);
                    state = this.world.getBlockState(target);
                    if (state.m_60795_() || !PistonBaseBlock.isPushable(state, this.world, target, this.moveDirection, false, this.moveDirection.getOpposite()) || target.equals(this.pistonPos) || this.getStickCompatibility(this.world, state, oldState, target, oldPos) != ICollateralMover.MoveResult.MOVE) {
                        break;
                    }
                    oldState = state;
                    oldPos = target;
                    if (++lineLen + this.toMove.size() > max) {
                        return false;
                    }
                }
                int collisionEnd = 0;
                for (int j = lineLen - 1; j >= 0; j--) {
                    BlockPos movePos = origin.relative(this.moveDirection.getOpposite(), j);
                    if (this.toDestroy.contains(movePos)) {
                        break;
                    }
                    this.toMove.add(movePos);
                    collisionEnd++;
                }
                if (skippingNext) {
                    return true;
                } else {
                    int offset = 1;
                    boolean doneFinding;
                    do {
                        BlockPos currentPos = origin.relative(this.moveDirection, offset);
                        int collisionStart = this.toMove.indexOf(currentPos);
                        if (collisionStart > -1) {
                            this.reorderListAtCollision(collisionEnd, collisionStart);
                            for (int i = 0; i <= collisionStart + collisionEnd; i++) {
                                BlockPos collidingPos = (BlockPos) this.toMove.get(i);
                                if (this.addBranchingBlocks(this.world, collidingPos, this.isBlockBranching(this.world, collidingPos)) == ICollateralMover.MoveResult.PREVENT) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        state = this.world.getBlockState(currentPos);
                        if (state.m_60795_()) {
                            return true;
                        }
                        if (!PistonBaseBlock.isPushable(state, this.world, currentPos, this.moveDirection, true, this.moveDirection) || currentPos.equals(this.pistonPos)) {
                            return false;
                        }
                        if (state.m_60811_() == PushReaction.DESTROY) {
                            this.toDestroy.add(currentPos);
                            this.toMove.remove(currentPos);
                            return true;
                        }
                        doneFinding = false;
                        if (this.isBlockBranching(this.world, currentPos)) {
                            ICollateralMover.MoveResult resx = this.getBranchResult(this.world, currentPos);
                            if (resx == ICollateralMover.MoveResult.PREVENT) {
                                return false;
                            }
                            if (resx != ICollateralMover.MoveResult.MOVE) {
                                doneFinding = true;
                            }
                        }
                        if (this.toMove.size() >= max) {
                            return false;
                        }
                        this.toMove.add(currentPos);
                        collisionEnd++;
                        offset++;
                    } while (!doneFinding);
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    private void reorderListAtCollision(int collisionEnd, int collisionStart) {
        List<BlockPos> before = Lists.newArrayList(this.toMove.subList(0, collisionStart));
        List<BlockPos> collision = Lists.newArrayList(this.toMove.subList(this.toMove.size() - collisionEnd, this.toMove.size()));
        List<BlockPos> after = Lists.newArrayList(this.toMove.subList(collisionStart, this.toMove.size() - collisionEnd));
        this.toMove.clear();
        this.toMove.addAll(before);
        this.toMove.addAll(collision);
        this.toMove.addAll(after);
    }

    private ICollateralMover.MoveResult addBranchingBlocks(Level world, BlockPos fromPos, boolean isSourceBranching) {
        BlockState state = world.getBlockState(fromPos);
        Block block = state.m_60734_();
        Direction opposite = this.moveDirection.getOpposite();
        ICollateralMover.MoveResult retResult = ICollateralMover.MoveResult.SKIP;
        Direction[] var8 = Direction.values();
        int var9 = var8.length;
        int var10 = 0;
        while (var10 < var9) {
            Direction face = var8[var10];
            BlockPos targetPos = fromPos.relative(face);
            BlockState targetState = world.getBlockState(targetPos);
            ICollateralMover.MoveResult res;
            if (!isSourceBranching) {
                IIndirectConnector indirect = getIndirectStickiness(targetState);
                if (indirect != null && indirect.isEnabled() && indirect.canConnectIndirectly(world, targetPos, fromPos, targetState, state)) {
                    res = this.getStickCompatibility(world, state, targetState, fromPos, targetPos);
                } else {
                    res = ICollateralMover.MoveResult.SKIP;
                }
            } else if (block instanceof ICollateralMover collateralMover) {
                res = collateralMover.getCollateralMovement(world, this.pistonPos, this.moveDirection, face, fromPos);
            } else {
                res = this.getStickCompatibility(world, state, targetState, fromPos, targetPos);
            }
            switch(res) {
                case PREVENT:
                    return ICollateralMover.MoveResult.PREVENT;
                case MOVE:
                    if (!this.addBlockLine(targetPos, face)) {
                        return ICollateralMover.MoveResult.PREVENT;
                    }
                default:
                    if (face == opposite) {
                        retResult = res;
                    }
                    var10++;
                    break;
                case BREAK:
                    if (PistonBaseBlock.isPushable(targetState, world, targetPos, this.moveDirection, true, this.moveDirection)) {
                        this.toDestroy.add(targetPos);
                        this.toMove.remove(targetPos);
                        return ICollateralMover.MoveResult.BREAK;
                    }
                    return ICollateralMover.MoveResult.PREVENT;
            }
        }
        return retResult;
    }

    private boolean isBlockBranching(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.m_60734_();
        return block instanceof ICollateralMover ? ((ICollateralMover) block).isCollateralMover(world, this.pistonPos, this.moveDirection, pos) : isBlockSticky(state);
    }

    private ICollateralMover.MoveResult getBranchResult(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.m_60734_() instanceof ICollateralMover collateralMover ? collateralMover.getCollateralMovement(world, this.pistonPos, this.moveDirection, this.moveDirection, pos) : ICollateralMover.MoveResult.MOVE;
    }

    private ICollateralMover.MoveResult getStickCompatibility(Level world, BlockState state1, BlockState state2, BlockPos pos1, BlockPos pos2) {
        IConditionalSticky stick = this.getStickCondition(state1);
        if (stick != null && !stick.canStickToBlock(world, this.pistonPos, pos1, pos2, state1, state2, this.moveDirection)) {
            return ICollateralMover.MoveResult.SKIP;
        } else {
            stick = this.getStickCondition(state2);
            return stick != null && !stick.canStickToBlock(world, this.pistonPos, pos2, pos1, state2, state1, this.moveDirection) ? ICollateralMover.MoveResult.SKIP : ICollateralMover.MoveResult.MOVE;
        }
    }

    private IConditionalSticky getStickCondition(BlockState state) {
        Block block = state.m_60734_();
        if (block instanceof IConditionalSticky) {
            return (IConditionalSticky) block;
        } else {
            IIndirectConnector indirect = getIndirectStickiness(state);
            if (indirect != null) {
                return indirect.isEnabled() ? indirect.getStickyCondition() : null;
            } else {
                return state.isStickyBlock() ? ZetaPistonStructureResolver.DefaultStickCondition.INSTANCE : null;
            }
        }
    }

    @NotNull
    @Override
    public List<BlockPos> getToPush() {
        return !ZetaPistonStructureResolver.GlobalSettings.isEnabled() ? this.parent.getToPush() : this.toMove;
    }

    @NotNull
    @Override
    public List<BlockPos> getToDestroy() {
        return !ZetaPistonStructureResolver.GlobalSettings.isEnabled() ? this.parent.getToDestroy() : this.toDestroy;
    }

    private static IIndirectConnector getIndirectStickiness(BlockState state) {
        for (Pair<Predicate<BlockState>, IIndirectConnector> p : IIndirectConnector.INDIRECT_STICKY_BLOCKS) {
            if (((Predicate) p.getLeft()).test(state)) {
                return (IIndirectConnector) p.getRight();
            }
        }
        return null;
    }

    private static boolean isBlockSticky(BlockState state) {
        if (state.isStickyBlock()) {
            return true;
        } else {
            IIndirectConnector indirect = getIndirectStickiness(state);
            return indirect != null && indirect.isEnabled();
        }
    }

    private static class DefaultStickCondition implements IConditionalSticky {

        private static final ZetaPistonStructureResolver.DefaultStickCondition INSTANCE = new ZetaPistonStructureResolver.DefaultStickCondition();

        @Override
        public boolean canStickToBlock(Level world, BlockPos pistonPos, BlockPos pos, BlockPos slimePos, BlockState state, BlockState slimeState, Direction direction) {
            if (slimeState.m_60734_() instanceof IZetaBlockExtensions ext) {
                return ext.canStickToZeta(slimeState, state);
            } else {
                return state.m_60734_() instanceof IZetaBlockExtensions ext ? ext.canStickToZeta(state, slimeState) : IZetaBlockExtensions.DEFAULT.canStickToZeta(slimeState, state);
            }
        }
    }

    public static class GlobalSettings {

        private static boolean enabled = false;

        private static int pushLimit = 12;

        private static final Set<String> wantsEnabled = new HashSet();

        private static final Object2IntMap<String> wantsPushLimit = new Object2IntOpenHashMap();

        public static boolean isEnabled() {
            return enabled;
        }

        public static int getPushLimit() {
            return pushLimit;
        }

        public static void requestEnabled(String modid, boolean enablePlease) {
            boolean wasEnabled = enabled;
            if (enablePlease) {
                wantsEnabled.add(modid);
            } else {
                wantsEnabled.remove(modid);
            }
            enabled = !wantsEnabled.isEmpty();
            if (!wasEnabled && enabled) {
                Zeta.GLOBAL_LOG.info("'{}' is enabling Zeta's piston structure resolver.", modid);
            } else if (wasEnabled && !enabled) {
                Zeta.GLOBAL_LOG.info("Zeta's piston structure resolver is now disabled.");
            }
        }

        public static void requestPushLimit(String modid, int pushLimitPlease) {
            int wasPushLimit = pushLimit;
            wantsPushLimit.put(modid, pushLimitPlease);
            pushLimit = wantsPushLimit.values().intStream().max().orElse(12);
            if (wasPushLimit < pushLimit) {
                Zeta.GLOBAL_LOG.info("'{}' is raising Zeta's piston structure resolver push limit to {} blocks.", modid, pushLimit);
            }
        }
    }
}