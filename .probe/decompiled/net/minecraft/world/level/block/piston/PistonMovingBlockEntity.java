package net.minecraft.world.level.block.piston;

import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PistonMovingBlockEntity extends BlockEntity {

    private static final int TICKS_TO_EXTEND = 2;

    private static final double PUSH_OFFSET = 0.01;

    public static final double TICK_MOVEMENT = 0.51;

    private BlockState movedState = Blocks.AIR.defaultBlockState();

    private Direction direction;

    private boolean extending;

    private boolean isSourcePiston;

    private static final ThreadLocal<Direction> NOCLIP = ThreadLocal.withInitial(() -> null);

    private float progress;

    private float progressO;

    private long lastTicked;

    private int deathTicks;

    public PistonMovingBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.PISTON, blockPos0, blockState1);
    }

    public PistonMovingBlockEntity(BlockPos blockPos0, BlockState blockState1, BlockState blockState2, Direction direction3, boolean boolean4, boolean boolean5) {
        this(blockPos0, blockState1);
        this.movedState = blockState2;
        this.direction = direction3;
        this.extending = boolean4;
        this.isSourcePiston = boolean5;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public boolean isExtending() {
        return this.extending;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean isSourcePiston() {
        return this.isSourcePiston;
    }

    public float getProgress(float float0) {
        if (float0 > 1.0F) {
            float0 = 1.0F;
        }
        return Mth.lerp(float0, this.progressO, this.progress);
    }

    public float getXOff(float float0) {
        return (float) this.direction.getStepX() * this.getExtendedProgress(this.getProgress(float0));
    }

    public float getYOff(float float0) {
        return (float) this.direction.getStepY() * this.getExtendedProgress(this.getProgress(float0));
    }

    public float getZOff(float float0) {
        return (float) this.direction.getStepZ() * this.getExtendedProgress(this.getProgress(float0));
    }

    private float getExtendedProgress(float float0) {
        return this.extending ? float0 - 1.0F : 1.0F - float0;
    }

    private BlockState getCollisionRelatedBlockState() {
        return !this.isExtending() && this.isSourcePiston() && this.movedState.m_60734_() instanceof PistonBaseBlock ? (BlockState) ((BlockState) ((BlockState) Blocks.PISTON_HEAD.defaultBlockState().m_61124_(PistonHeadBlock.SHORT, this.progress > 0.25F)).m_61124_(PistonHeadBlock.TYPE, this.movedState.m_60713_(Blocks.STICKY_PISTON) ? PistonType.STICKY : PistonType.DEFAULT)).m_61124_(PistonHeadBlock.f_52588_, (Direction) this.movedState.m_61143_(PistonBaseBlock.f_52588_)) : this.movedState;
    }

    private static void moveCollidedEntities(Level level0, BlockPos blockPos1, float float2, PistonMovingBlockEntity pistonMovingBlockEntity3) {
        Direction $$4 = pistonMovingBlockEntity3.getMovementDirection();
        double $$5 = (double) (float2 - pistonMovingBlockEntity3.progress);
        VoxelShape $$6 = pistonMovingBlockEntity3.getCollisionRelatedBlockState().m_60812_(level0, blockPos1);
        if (!$$6.isEmpty()) {
            AABB $$7 = moveByPositionAndProgress(blockPos1, $$6.bounds(), pistonMovingBlockEntity3);
            List<Entity> $$8 = level0.m_45933_(null, PistonMath.getMovementArea($$7, $$4, $$5).minmax($$7));
            if (!$$8.isEmpty()) {
                List<AABB> $$9 = $$6.toAabbs();
                boolean $$10 = pistonMovingBlockEntity3.movedState.m_60713_(Blocks.SLIME_BLOCK);
                Iterator var12 = $$8.iterator();
                while (true) {
                    Entity $$11;
                    while (true) {
                        if (!var12.hasNext()) {
                            return;
                        }
                        $$11 = (Entity) var12.next();
                        if ($$11.getPistonPushReaction() != PushReaction.IGNORE) {
                            if (!$$10) {
                                break;
                            }
                            if (!($$11 instanceof ServerPlayer)) {
                                Vec3 $$12 = $$11.getDeltaMovement();
                                double $$13 = $$12.x;
                                double $$14 = $$12.y;
                                double $$15 = $$12.z;
                                switch($$4.getAxis()) {
                                    case X:
                                        $$13 = (double) $$4.getStepX();
                                        break;
                                    case Y:
                                        $$14 = (double) $$4.getStepY();
                                        break;
                                    case Z:
                                        $$15 = (double) $$4.getStepZ();
                                }
                                $$11.setDeltaMovement($$13, $$14, $$15);
                                break;
                            }
                        }
                    }
                    double $$16 = 0.0;
                    for (AABB $$17 : $$9) {
                        AABB $$18 = PistonMath.getMovementArea(moveByPositionAndProgress(blockPos1, $$17, pistonMovingBlockEntity3), $$4, $$5);
                        AABB $$19 = $$11.getBoundingBox();
                        if ($$18.intersects($$19)) {
                            $$16 = Math.max($$16, getMovement($$18, $$4, $$19));
                            if ($$16 >= $$5) {
                                break;
                            }
                        }
                    }
                    if (!($$16 <= 0.0)) {
                        $$16 = Math.min($$16, $$5) + 0.01;
                        moveEntityByPiston($$4, $$11, $$16, $$4);
                        if (!pistonMovingBlockEntity3.extending && pistonMovingBlockEntity3.isSourcePiston) {
                            fixEntityWithinPistonBase(blockPos1, $$11, $$4, $$5);
                        }
                    }
                }
            }
        }
    }

    private static void moveEntityByPiston(Direction direction0, Entity entity1, double double2, Direction direction3) {
        NOCLIP.set(direction0);
        entity1.move(MoverType.PISTON, new Vec3(double2 * (double) direction3.getStepX(), double2 * (double) direction3.getStepY(), double2 * (double) direction3.getStepZ()));
        NOCLIP.set(null);
    }

    private static void moveStuckEntities(Level level0, BlockPos blockPos1, float float2, PistonMovingBlockEntity pistonMovingBlockEntity3) {
        if (pistonMovingBlockEntity3.isStickyForEntities()) {
            Direction $$4 = pistonMovingBlockEntity3.getMovementDirection();
            if ($$4.getAxis().isHorizontal()) {
                double $$5 = pistonMovingBlockEntity3.movedState.m_60812_(level0, blockPos1).max(Direction.Axis.Y);
                AABB $$6 = moveByPositionAndProgress(blockPos1, new AABB(0.0, $$5, 0.0, 1.0, 1.5000010000000001, 1.0), pistonMovingBlockEntity3);
                double $$7 = (double) (float2 - pistonMovingBlockEntity3.progress);
                for (Entity $$9 : level0.getEntities((Entity) null, $$6, p_287552_ -> matchesStickyCritera($$6, p_287552_, blockPos1))) {
                    moveEntityByPiston($$4, $$9, $$7, $$4);
                }
            }
        }
    }

    private static boolean matchesStickyCritera(AABB aABB0, Entity entity1, BlockPos blockPos2) {
        return entity1.getPistonPushReaction() == PushReaction.NORMAL && entity1.onGround() && (entity1.isSupportedBy(blockPos2) || entity1.getX() >= aABB0.minX && entity1.getX() <= aABB0.maxX && entity1.getZ() >= aABB0.minZ && entity1.getZ() <= aABB0.maxZ);
    }

    private boolean isStickyForEntities() {
        return this.movedState.m_60713_(Blocks.HONEY_BLOCK);
    }

    public Direction getMovementDirection() {
        return this.extending ? this.direction : this.direction.getOpposite();
    }

    private static double getMovement(AABB aABB0, Direction direction1, AABB aABB2) {
        switch(direction1) {
            case EAST:
                return aABB0.maxX - aABB2.minX;
            case WEST:
                return aABB2.maxX - aABB0.minX;
            case UP:
            default:
                return aABB0.maxY - aABB2.minY;
            case DOWN:
                return aABB2.maxY - aABB0.minY;
            case SOUTH:
                return aABB0.maxZ - aABB2.minZ;
            case NORTH:
                return aABB2.maxZ - aABB0.minZ;
        }
    }

    private static AABB moveByPositionAndProgress(BlockPos blockPos0, AABB aABB1, PistonMovingBlockEntity pistonMovingBlockEntity2) {
        double $$3 = (double) pistonMovingBlockEntity2.getExtendedProgress(pistonMovingBlockEntity2.progress);
        return aABB1.move((double) blockPos0.m_123341_() + $$3 * (double) pistonMovingBlockEntity2.direction.getStepX(), (double) blockPos0.m_123342_() + $$3 * (double) pistonMovingBlockEntity2.direction.getStepY(), (double) blockPos0.m_123343_() + $$3 * (double) pistonMovingBlockEntity2.direction.getStepZ());
    }

    private static void fixEntityWithinPistonBase(BlockPos blockPos0, Entity entity1, Direction direction2, double double3) {
        AABB $$4 = entity1.getBoundingBox();
        AABB $$5 = Shapes.block().bounds().move(blockPos0);
        if ($$4.intersects($$5)) {
            Direction $$6 = direction2.getOpposite();
            double $$7 = getMovement($$5, $$6, $$4) + 0.01;
            double $$8 = getMovement($$5, $$6, $$4.intersect($$5)) + 0.01;
            if (Math.abs($$7 - $$8) < 0.01) {
                $$7 = Math.min($$7, double3) + 0.01;
                moveEntityByPiston(direction2, entity1, $$7, $$6);
            }
        }
    }

    public BlockState getMovedState() {
        return this.movedState;
    }

    public void finalTick() {
        if (this.f_58857_ != null && (this.progressO < 1.0F || this.f_58857_.isClientSide)) {
            this.progress = 1.0F;
            this.progressO = this.progress;
            this.f_58857_.removeBlockEntity(this.f_58858_);
            this.m_7651_();
            if (this.f_58857_.getBlockState(this.f_58858_).m_60713_(Blocks.MOVING_PISTON)) {
                BlockState $$0;
                if (this.isSourcePiston) {
                    $$0 = Blocks.AIR.defaultBlockState();
                } else {
                    $$0 = Block.updateFromNeighbourShapes(this.movedState, this.f_58857_, this.f_58858_);
                }
                this.f_58857_.setBlock(this.f_58858_, $$0, 3);
                this.f_58857_.neighborChanged(this.f_58858_, $$0.m_60734_(), this.f_58858_);
            }
        }
    }

    public static void tick(Level level0, BlockPos blockPos1, BlockState blockState2, PistonMovingBlockEntity pistonMovingBlockEntity3) {
        pistonMovingBlockEntity3.lastTicked = level0.getGameTime();
        pistonMovingBlockEntity3.progressO = pistonMovingBlockEntity3.progress;
        if (pistonMovingBlockEntity3.progressO >= 1.0F) {
            if (level0.isClientSide && pistonMovingBlockEntity3.deathTicks < 5) {
                pistonMovingBlockEntity3.deathTicks++;
            } else {
                level0.removeBlockEntity(blockPos1);
                pistonMovingBlockEntity3.m_7651_();
                if (level0.getBlockState(blockPos1).m_60713_(Blocks.MOVING_PISTON)) {
                    BlockState $$4 = Block.updateFromNeighbourShapes(pistonMovingBlockEntity3.movedState, level0, blockPos1);
                    if ($$4.m_60795_()) {
                        level0.setBlock(blockPos1, pistonMovingBlockEntity3.movedState, 84);
                        Block.updateOrDestroy(pistonMovingBlockEntity3.movedState, $$4, level0, blockPos1, 3);
                    } else {
                        if ($$4.m_61138_(BlockStateProperties.WATERLOGGED) && (Boolean) $$4.m_61143_(BlockStateProperties.WATERLOGGED)) {
                            $$4 = (BlockState) $$4.m_61124_(BlockStateProperties.WATERLOGGED, false);
                        }
                        level0.setBlock(blockPos1, $$4, 67);
                        level0.neighborChanged(blockPos1, $$4.m_60734_(), blockPos1);
                    }
                }
            }
        } else {
            float $$5 = pistonMovingBlockEntity3.progress + 0.5F;
            moveCollidedEntities(level0, blockPos1, $$5, pistonMovingBlockEntity3);
            moveStuckEntities(level0, blockPos1, $$5, pistonMovingBlockEntity3);
            pistonMovingBlockEntity3.progress = $$5;
            if (pistonMovingBlockEntity3.progress >= 1.0F) {
                pistonMovingBlockEntity3.progress = 1.0F;
            }
        }
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        HolderGetter<Block> $$1 = (HolderGetter<Block>) (this.f_58857_ != null ? this.f_58857_.m_246945_(Registries.BLOCK) : BuiltInRegistries.BLOCK.m_255303_());
        this.movedState = NbtUtils.readBlockState($$1, compoundTag0.getCompound("blockState"));
        this.direction = Direction.from3DDataValue(compoundTag0.getInt("facing"));
        this.progress = compoundTag0.getFloat("progress");
        this.progressO = this.progress;
        this.extending = compoundTag0.getBoolean("extending");
        this.isSourcePiston = compoundTag0.getBoolean("source");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        compoundTag0.put("blockState", NbtUtils.writeBlockState(this.movedState));
        compoundTag0.putInt("facing", this.direction.get3DDataValue());
        compoundTag0.putFloat("progress", this.progressO);
        compoundTag0.putBoolean("extending", this.extending);
        compoundTag0.putBoolean("source", this.isSourcePiston);
    }

    public VoxelShape getCollisionShape(BlockGetter blockGetter0, BlockPos blockPos1) {
        VoxelShape $$2;
        if (!this.extending && this.isSourcePiston && this.movedState.m_60734_() instanceof PistonBaseBlock) {
            $$2 = ((BlockState) this.movedState.m_61124_(PistonBaseBlock.EXTENDED, true)).m_60812_(blockGetter0, blockPos1);
        } else {
            $$2 = Shapes.empty();
        }
        Direction $$4 = (Direction) NOCLIP.get();
        if ((double) this.progress < 1.0 && $$4 == this.getMovementDirection()) {
            return $$2;
        } else {
            BlockState $$5;
            if (this.isSourcePiston()) {
                $$5 = (BlockState) ((BlockState) Blocks.PISTON_HEAD.defaultBlockState().m_61124_(PistonHeadBlock.f_52588_, this.direction)).m_61124_(PistonHeadBlock.SHORT, this.extending != 1.0F - this.progress < 0.25F);
            } else {
                $$5 = this.movedState;
            }
            float $$7 = this.getExtendedProgress(this.progress);
            double $$8 = (double) ((float) this.direction.getStepX() * $$7);
            double $$9 = (double) ((float) this.direction.getStepY() * $$7);
            double $$10 = (double) ((float) this.direction.getStepZ() * $$7);
            return Shapes.or($$2, $$5.m_60812_(blockGetter0, blockPos1).move($$8, $$9, $$10));
        }
    }

    public long getLastTicked() {
        return this.lastTicked;
    }

    @Override
    public void setLevel(Level level0) {
        super.setLevel(level0);
        if (level0.m_246945_(Registries.BLOCK).m_254902_(this.movedState.m_60734_().builtInRegistryHolder().key()).isEmpty()) {
            this.movedState = Blocks.AIR.defaultBlockState();
        }
    }
}