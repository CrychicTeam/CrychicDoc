package net.minecraft.world.level.portal;

import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PortalShape {

    private static final int MIN_WIDTH = 2;

    public static final int MAX_WIDTH = 21;

    private static final int MIN_HEIGHT = 3;

    public static final int MAX_HEIGHT = 21;

    private static final BlockBehaviour.StatePredicate FRAME = (p_77720_, p_77721_, p_77722_) -> p_77720_.m_60713_(Blocks.OBSIDIAN);

    private static final float SAFE_TRAVEL_MAX_ENTITY_XY = 4.0F;

    private static final double SAFE_TRAVEL_MAX_VERTICAL_DELTA = 1.0;

    private final LevelAccessor level;

    private final Direction.Axis axis;

    private final Direction rightDir;

    private int numPortalBlocks;

    @Nullable
    private BlockPos bottomLeft;

    private int height;

    private final int width;

    public static Optional<PortalShape> findEmptyPortalShape(LevelAccessor levelAccessor0, BlockPos blockPos1, Direction.Axis directionAxis2) {
        return findPortalShape(levelAccessor0, blockPos1, p_77727_ -> p_77727_.isValid() && p_77727_.numPortalBlocks == 0, directionAxis2);
    }

    public static Optional<PortalShape> findPortalShape(LevelAccessor levelAccessor0, BlockPos blockPos1, Predicate<PortalShape> predicatePortalShape2, Direction.Axis directionAxis3) {
        Optional<PortalShape> $$4 = Optional.of(new PortalShape(levelAccessor0, blockPos1, directionAxis3)).filter(predicatePortalShape2);
        if ($$4.isPresent()) {
            return $$4;
        } else {
            Direction.Axis $$5 = directionAxis3 == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(new PortalShape(levelAccessor0, blockPos1, $$5)).filter(predicatePortalShape2);
        }
    }

    public PortalShape(LevelAccessor levelAccessor0, BlockPos blockPos1, Direction.Axis directionAxis2) {
        this.level = levelAccessor0;
        this.axis = directionAxis2;
        this.rightDir = directionAxis2 == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.bottomLeft = this.calculateBottomLeft(blockPos1);
        if (this.bottomLeft == null) {
            this.bottomLeft = blockPos1;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.calculateWidth();
            if (this.width > 0) {
                this.height = this.calculateHeight();
            }
        }
    }

    @Nullable
    private BlockPos calculateBottomLeft(BlockPos blockPos0) {
        int $$1 = Math.max(this.level.m_141937_(), blockPos0.m_123342_() - 21);
        while (blockPos0.m_123342_() > $$1 && isEmpty(this.level.m_8055_(blockPos0.below()))) {
            blockPos0 = blockPos0.below();
        }
        Direction $$2 = this.rightDir.getOpposite();
        int $$3 = this.getDistanceUntilEdgeAboveFrame(blockPos0, $$2) - 1;
        return $$3 < 0 ? null : blockPos0.relative($$2, $$3);
    }

    private int calculateWidth() {
        int $$0 = this.getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
        return $$0 >= 2 && $$0 <= 21 ? $$0 : 0;
    }

    private int getDistanceUntilEdgeAboveFrame(BlockPos blockPos0, Direction direction1) {
        BlockPos.MutableBlockPos $$2 = new BlockPos.MutableBlockPos();
        for (int $$3 = 0; $$3 <= 21; $$3++) {
            $$2.set(blockPos0).move(direction1, $$3);
            BlockState $$4 = this.level.m_8055_($$2);
            if (!isEmpty($$4)) {
                if (FRAME.test($$4, this.level, $$2)) {
                    return $$3;
                }
                break;
            }
            BlockState $$5 = this.level.m_8055_($$2.move(Direction.DOWN));
            if (!FRAME.test($$5, this.level, $$2)) {
                break;
            }
        }
        return 0;
    }

    private int calculateHeight() {
        BlockPos.MutableBlockPos $$0 = new BlockPos.MutableBlockPos();
        int $$1 = this.getDistanceUntilTop($$0);
        return $$1 >= 3 && $$1 <= 21 && this.hasTopFrame($$0, $$1) ? $$1 : 0;
    }

    private boolean hasTopFrame(BlockPos.MutableBlockPos blockPosMutableBlockPos0, int int1) {
        for (int $$2 = 0; $$2 < this.width; $$2++) {
            BlockPos.MutableBlockPos $$3 = blockPosMutableBlockPos0.set(this.bottomLeft).move(Direction.UP, int1).move(this.rightDir, $$2);
            if (!FRAME.test(this.level.m_8055_($$3), this.level, $$3)) {
                return false;
            }
        }
        return true;
    }

    private int getDistanceUntilTop(BlockPos.MutableBlockPos blockPosMutableBlockPos0) {
        for (int $$1 = 0; $$1 < 21; $$1++) {
            blockPosMutableBlockPos0.set(this.bottomLeft).move(Direction.UP, $$1).move(this.rightDir, -1);
            if (!FRAME.test(this.level.m_8055_(blockPosMutableBlockPos0), this.level, blockPosMutableBlockPos0)) {
                return $$1;
            }
            blockPosMutableBlockPos0.set(this.bottomLeft).move(Direction.UP, $$1).move(this.rightDir, this.width);
            if (!FRAME.test(this.level.m_8055_(blockPosMutableBlockPos0), this.level, blockPosMutableBlockPos0)) {
                return $$1;
            }
            for (int $$2 = 0; $$2 < this.width; $$2++) {
                blockPosMutableBlockPos0.set(this.bottomLeft).move(Direction.UP, $$1).move(this.rightDir, $$2);
                BlockState $$3 = this.level.m_8055_(blockPosMutableBlockPos0);
                if (!isEmpty($$3)) {
                    return $$1;
                }
                if ($$3.m_60713_(Blocks.NETHER_PORTAL)) {
                    this.numPortalBlocks++;
                }
            }
        }
        return 21;
    }

    private static boolean isEmpty(BlockState blockState0) {
        return blockState0.m_60795_() || blockState0.m_204336_(BlockTags.FIRE) || blockState0.m_60713_(Blocks.NETHER_PORTAL);
    }

    public boolean isValid() {
        return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    public void createPortalBlocks() {
        BlockState $$0 = (BlockState) Blocks.NETHER_PORTAL.defaultBlockState().m_61124_(NetherPortalBlock.AXIS, this.axis);
        BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach(p_77725_ -> this.level.m_7731_(p_77725_, $$0, 18));
    }

    public boolean isComplete() {
        return this.isValid() && this.numPortalBlocks == this.width * this.height;
    }

    public static Vec3 getRelativePosition(BlockUtil.FoundRectangle blockUtilFoundRectangle0, Direction.Axis directionAxis1, Vec3 vec2, EntityDimensions entityDimensions3) {
        double $$4 = (double) blockUtilFoundRectangle0.axis1Size - (double) entityDimensions3.width;
        double $$5 = (double) blockUtilFoundRectangle0.axis2Size - (double) entityDimensions3.height;
        BlockPos $$6 = blockUtilFoundRectangle0.minCorner;
        double $$8;
        if ($$4 > 0.0) {
            float $$7 = (float) $$6.m_123304_(directionAxis1) + entityDimensions3.width / 2.0F;
            $$8 = Mth.clamp(Mth.inverseLerp(vec2.get(directionAxis1) - (double) $$7, 0.0, $$4), 0.0, 1.0);
        } else {
            $$8 = 0.5;
        }
        double $$11;
        if ($$5 > 0.0) {
            Direction.Axis $$10 = Direction.Axis.Y;
            $$11 = Mth.clamp(Mth.inverseLerp(vec2.get($$10) - (double) $$6.m_123304_($$10), 0.0, $$5), 0.0, 1.0);
        } else {
            $$11 = 0.0;
        }
        Direction.Axis $$13 = directionAxis1 == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        double $$14 = vec2.get($$13) - ((double) $$6.m_123304_($$13) + 0.5);
        return new Vec3($$8, $$11, $$14);
    }

    public static PortalInfo createPortalInfo(ServerLevel serverLevel0, BlockUtil.FoundRectangle blockUtilFoundRectangle1, Direction.Axis directionAxis2, Vec3 vec3, Entity entity4, Vec3 vec5, float float6, float float7) {
        BlockPos $$8 = blockUtilFoundRectangle1.minCorner;
        BlockState $$9 = serverLevel0.m_8055_($$8);
        Direction.Axis $$10 = (Direction.Axis) $$9.m_61145_(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        double $$11 = (double) blockUtilFoundRectangle1.axis1Size;
        double $$12 = (double) blockUtilFoundRectangle1.axis2Size;
        EntityDimensions $$13 = entity4.getDimensions(entity4.getPose());
        int $$14 = directionAxis2 == $$10 ? 0 : 90;
        Vec3 $$15 = directionAxis2 == $$10 ? vec5 : new Vec3(vec5.z, vec5.y, -vec5.x);
        double $$16 = (double) $$13.width / 2.0 + ($$11 - (double) $$13.width) * vec3.x();
        double $$17 = ($$12 - (double) $$13.height) * vec3.y();
        double $$18 = 0.5 + vec3.z();
        boolean $$19 = $$10 == Direction.Axis.X;
        Vec3 $$20 = new Vec3((double) $$8.m_123341_() + ($$19 ? $$16 : $$18), (double) $$8.m_123342_() + $$17, (double) $$8.m_123343_() + ($$19 ? $$18 : $$16));
        Vec3 $$21 = findCollisionFreePosition($$20, serverLevel0, entity4, $$13);
        return new PortalInfo($$21, $$15, float6 + (float) $$14, float7);
    }

    private static Vec3 findCollisionFreePosition(Vec3 vec0, ServerLevel serverLevel1, Entity entity2, EntityDimensions entityDimensions3) {
        if (!(entityDimensions3.width > 4.0F) && !(entityDimensions3.height > 4.0F)) {
            double $$4 = (double) entityDimensions3.height / 2.0;
            Vec3 $$5 = vec0.add(0.0, $$4, 0.0);
            VoxelShape $$6 = Shapes.create(AABB.ofSize($$5, (double) entityDimensions3.width, 0.0, (double) entityDimensions3.width).expandTowards(0.0, 1.0, 0.0).inflate(1.0E-6));
            Optional<Vec3> $$7 = serverLevel1.m_151418_(entity2, $$6, $$5, (double) entityDimensions3.width, (double) entityDimensions3.height, (double) entityDimensions3.width);
            Optional<Vec3> $$8 = $$7.map(p_259019_ -> p_259019_.subtract(0.0, $$4, 0.0));
            return (Vec3) $$8.orElse(vec0);
        } else {
            return vec0;
        }
    }
}