package org.violetmoon.quark.addons.oddities.block.be;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.magnetsystem.MagnetSystem;
import org.violetmoon.quark.addons.oddities.module.MagnetsModule;
import org.violetmoon.quark.api.IMagnetMoveAction;
import org.violetmoon.quark.base.Quark;

public class MagnetizedBlockBlockEntity extends BlockEntity {

    private BlockState magnetState;

    private CompoundTag subTile;

    private Direction magnetFacing;

    private static final ThreadLocal<Direction> MOVING_ENTITY = ThreadLocal.withInitial(() -> null);

    private float progress;

    private float lastProgress;

    private long lastTicked;

    public MagnetizedBlockBlockEntity(BlockPos pos, BlockState state) {
        super(MagnetsModule.magnetizedBlockType, pos, state);
    }

    public MagnetizedBlockBlockEntity(BlockPos pos, BlockState state, BlockState magnetStateIn, CompoundTag subTileIn, Direction magnetFacingIn) {
        this(pos, state);
        this.magnetState = magnetStateIn;
        this.subTile = subTileIn;
        this.magnetFacing = magnetFacingIn;
    }

    public Direction getFacing() {
        return this.magnetFacing;
    }

    public float getProgress(float ticks) {
        if (ticks > 1.0F) {
            ticks = 1.0F;
        }
        return Mth.lerp(ticks, this.lastProgress, this.progress);
    }

    public float getOffsetX(float ticks) {
        return (float) this.magnetFacing.getStepX() * this.getExtendedProgress(this.getProgress(ticks));
    }

    public float getOffsetY(float ticks) {
        return (float) this.magnetFacing.getStepY() * this.getExtendedProgress(this.getProgress(ticks));
    }

    public float getOffsetZ(float ticks) {
        return (float) this.magnetFacing.getStepZ() * this.getExtendedProgress(this.getProgress(ticks));
    }

    private float getExtendedProgress(float partialTicks) {
        return partialTicks - 1.0F;
    }

    private void moveCollidedEntities(float progress) {
        if (this.f_58857_ != null) {
            boolean sticky = Quark.ZETA.blockExtensions.get(this.magnetState).isStickyBlockZeta(this.magnetState);
            Direction direction = this.magnetFacing;
            double movement = (double) (progress - this.progress);
            VoxelShape collision = this.magnetState.m_60812_(this.f_58857_, this.m_58899_());
            if (!collision.isEmpty()) {
                List<AABB> boundingBoxes = collision.toAabbs();
                AABB containingBox = this.moveByPositionAndProgress(this.getEnclosingBox(boundingBoxes));
                List<Entity> entities = this.f_58857_.m_45933_(null, this.getMovementArea(containingBox, direction, movement).minmax(containingBox));
                if (!entities.isEmpty()) {
                    for (Entity entity : entities) {
                        if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                            if (sticky) {
                                Vec3 motion = entity.getDeltaMovement();
                                double dX = motion.x;
                                double dY = motion.y;
                                double dZ = motion.z;
                                switch(direction.getAxis()) {
                                    case X:
                                        dX = (double) direction.getStepX();
                                        break;
                                    case Y:
                                        dY = (double) direction.getStepY();
                                        break;
                                    case Z:
                                        dZ = (double) direction.getStepZ();
                                }
                                entity.setDeltaMovement(dX, dY, dZ);
                            }
                            double motion = 0.0;
                            for (AABB aList : boundingBoxes) {
                                AABB movementArea = this.getMovementArea(this.moveByPositionAndProgress(aList), direction, movement);
                                AABB entityBox = entity.getBoundingBox();
                                if (movementArea.intersects(entityBox)) {
                                    motion = Math.max(motion, this.getMovement(movementArea, direction, entityBox));
                                    if (motion >= movement) {
                                        break;
                                    }
                                }
                            }
                            if (motion > 0.0) {
                                motion = Math.min(motion, movement) + 0.01;
                                MOVING_ENTITY.set(direction);
                                entity.move(MoverType.PISTON, new Vec3(motion * (double) direction.getStepX(), motion * (double) direction.getStepY(), motion * (double) direction.getStepZ()));
                                MOVING_ENTITY.set(null);
                            }
                        }
                    }
                }
            }
        }
    }

    private AABB getEnclosingBox(List<AABB> boxes) {
        double minX = 0.0;
        double minY = 0.0;
        double minZ = 0.0;
        double maxX = 1.0;
        double maxY = 1.0;
        double maxZ = 1.0;
        for (AABB bb : boxes) {
            minX = Math.min(bb.minX, minX);
            minY = Math.min(bb.minY, minY);
            minZ = Math.min(bb.minZ, minZ);
            maxX = Math.max(bb.maxX, maxX);
            maxY = Math.max(bb.maxY, maxY);
            maxZ = Math.max(bb.maxZ, maxZ);
        }
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private double getMovement(AABB bb1, Direction facing, AABB bb2) {
        return switch(facing.getAxis()) {
            case X ->
                getDeltaX(bb1, facing, bb2);
            case Z ->
                getDeltaZ(bb1, facing, bb2);
            default ->
                getDeltaY(bb1, facing, bb2);
        };
    }

    private AABB moveByPositionAndProgress(AABB bb) {
        double progress = (double) this.getExtendedProgress(this.progress);
        return bb.move((double) this.f_58858_.m_123341_() + progress * (double) this.magnetFacing.getStepX(), (double) this.f_58858_.m_123342_() + progress * (double) this.magnetFacing.getStepY(), (double) this.f_58858_.m_123343_() + progress * (double) this.magnetFacing.getStepZ());
    }

    private AABB getMovementArea(AABB bb, Direction dir, double movement) {
        double d0 = movement * (double) dir.getAxisDirection().getStep();
        double d1 = Math.min(d0, 0.0);
        double d2 = Math.max(d0, 0.0);
        return switch(dir) {
            case WEST ->
                new AABB(bb.minX + d1, bb.minY, bb.minZ, bb.minX + d2, bb.maxY, bb.maxZ);
            case EAST ->
                new AABB(bb.maxX + d1, bb.minY, bb.minZ, bb.maxX + d2, bb.maxY, bb.maxZ);
            case DOWN ->
                new AABB(bb.minX, bb.minY + d1, bb.minZ, bb.maxX, bb.minY + d2, bb.maxZ);
            case NORTH ->
                new AABB(bb.minX, bb.minY, bb.minZ + d1, bb.maxX, bb.maxY, bb.minZ + d2);
            case SOUTH ->
                new AABB(bb.minX, bb.minY, bb.maxZ + d1, bb.maxX, bb.maxY, bb.maxZ + d2);
            default ->
                new AABB(bb.minX, bb.maxY + d1, bb.minZ, bb.maxX, bb.maxY + d2, bb.maxZ);
        };
    }

    private static double getDeltaX(AABB bb1, Direction facing, AABB bb2) {
        return facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? bb1.maxX - bb2.minX : bb2.maxX - bb1.minX;
    }

    private static double getDeltaY(AABB bb1, Direction facing, AABB bb2) {
        return facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? bb1.maxY - bb2.minY : bb2.maxY - bb1.minY;
    }

    private static double getDeltaZ(AABB bb1, Direction facing, AABB bb2) {
        return facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? bb1.maxZ - bb2.minZ : bb2.maxZ - bb1.minZ;
    }

    public BlockState getMagnetState() {
        return this.magnetState;
    }

    private IMagnetMoveAction getMoveAction() {
        Block block = this.magnetState.m_60734_();
        return block instanceof IMagnetMoveAction ? (IMagnetMoveAction) block : MagnetSystem.getMoveAction(block);
    }

    public void finalizeContents(BlockState blockState) {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            SoundType soundType = blockState.m_60827_();
            this.f_58857_.playSound(null, this.f_58858_, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) * 0.05F, soundType.getPitch() * 0.8F);
            BlockEntity newTile = this.getSubTile(this.f_58858_);
            if (newTile != null) {
                this.f_58857_.setBlockEntity(newTile);
            }
            IMagnetMoveAction action = this.getMoveAction();
            if (action != null) {
                action.onMagnetMoved(this.f_58857_, this.f_58858_, this.magnetFacing, blockState, newTile);
            }
        }
    }

    public BlockEntity getSubTile(BlockPos pos) {
        if (this.subTile != null && !this.subTile.isEmpty()) {
            CompoundTag tileData = this.subTile.copy();
            tileData.putInt("x", this.f_58858_.m_123341_());
            tileData.putInt("y", this.f_58858_.m_123342_());
            tileData.putInt("z", this.f_58858_.m_123343_());
            return BlockEntity.loadStatic(pos, this.magnetState, this.subTile);
        } else {
            return null;
        }
    }

    public void clearMagnetTileEntity() {
        if (this.lastProgress < 1.0F && this.f_58857_ != null) {
            this.progress = 1.0F;
            this.lastProgress = this.progress;
            this.f_58857_.removeBlockEntity(this.f_58858_);
            this.m_7651_();
            if (this.f_58857_.getBlockState(this.f_58858_).m_60734_() == MagnetsModule.magnetized_block) {
                BlockState blockstate = Block.updateFromNeighbourShapes(this.magnetState, this.f_58857_, this.f_58858_);
                this.setAndUpdateBlock(blockstate, 3);
            }
        }
    }

    private void setAndUpdateBlock(BlockState blockstate, int flag) {
        if (this.f_58857_ != null) {
            this.f_58857_.setBlock(this.f_58858_, blockstate, flag);
            this.f_58857_.neighborChanged(this.f_58858_, blockstate.m_60734_(), this.f_58858_);
            if ((blockstate.m_60734_() instanceof ButtonBlock || blockstate.m_60734_() instanceof BasePressurePlateBlock) && this.f_58857_ instanceof ServerLevel serverLevel) {
                blockstate.m_222963_(serverLevel, this.f_58858_, serverLevel.f_46441_);
                blockstate = this.f_58857_.getBlockState(this.f_58858_);
            }
            this.finalizeContents(blockstate);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MagnetizedBlockBlockEntity be) {
        be.tick();
    }

    public void tick() {
        if (this.f_58857_ != null) {
            this.lastTicked = this.f_58857_.getGameTime();
            this.lastProgress = this.progress;
            if (this.lastProgress >= 1.0F) {
                this.f_58857_.removeBlockEntity(this.f_58858_);
                this.m_7651_();
                if (this.magnetState != null && this.f_58857_.getBlockState(this.f_58858_).m_60734_() == MagnetsModule.magnetized_block) {
                    BlockState blockstate = Block.updateFromNeighbourShapes(this.magnetState, this.f_58857_, this.f_58858_);
                    if (blockstate.m_60795_()) {
                        this.f_58857_.setBlock(this.f_58858_, this.magnetState, 84);
                        Block.updateOrDestroy(this.magnetState, blockstate, this.f_58857_, this.f_58858_, 3);
                    } else {
                        if (blockstate.m_61148_().containsKey(BlockStateProperties.WATERLOGGED) && (Boolean) blockstate.m_61143_(BlockStateProperties.WATERLOGGED)) {
                            blockstate = (BlockState) blockstate.m_61124_(BlockStateProperties.WATERLOGGED, Boolean.FALSE);
                        }
                        this.setAndUpdateBlock(blockstate, 67);
                    }
                }
            } else {
                float newProgress = this.progress + 0.5F;
                this.moveCollidedEntities(newProgress);
                this.progress = newProgress;
                if (this.progress >= 1.0F) {
                    this.progress = 1.0F;
                }
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.magnetState = NbtUtils.readBlockState(this.f_58857_.m_246945_(Registries.BLOCK), compound.getCompound("blockState"));
        this.magnetFacing = Direction.from3DDataValue(compound.getInt("facing"));
        this.progress = compound.getFloat("progress");
        this.lastProgress = this.progress;
        this.subTile = compound.getCompound("subTile");
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        return this.writeNBTData(this.serializeNBT(), false);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.writeNBTData(nbt, true);
    }

    private CompoundTag writeNBTData(CompoundTag compound, boolean includeSubTile) {
        compound.put("blockState", NbtUtils.writeBlockState(this.magnetState));
        if (includeSubTile) {
            compound.put("subTile", this.subTile);
        }
        compound.putInt("facing", this.magnetFacing.get3DDataValue());
        compound.putFloat("progress", this.lastProgress);
        return compound;
    }

    public VoxelShape getCollisionShape(BlockGetter world, BlockPos pos) {
        Direction direction = (Direction) MOVING_ENTITY.get();
        if ((double) this.progress < 1.0 && direction == this.magnetFacing) {
            return Shapes.empty();
        } else {
            float progress = this.getExtendedProgress(this.progress);
            double dX = (double) ((float) this.magnetFacing.getStepX() * progress);
            double dY = (double) ((float) this.magnetFacing.getStepY() * progress);
            double dZ = (double) ((float) this.magnetFacing.getStepZ() * progress);
            return this.magnetState.m_60812_(world, pos).move(dX, dY, dZ);
        }
    }

    public long getLastTicked() {
        return this.lastTicked;
    }
}