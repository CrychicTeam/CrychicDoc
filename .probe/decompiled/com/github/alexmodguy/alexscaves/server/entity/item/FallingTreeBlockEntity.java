package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.MovingBlockData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraftforge.network.PlayMessages;

public class FallingTreeBlockEntity extends AbstractMovingBlockEntity {

    private static final EntityDataAccessor<Direction> FALL_DIRECTION = SynchedEntityData.defineId(FallingTreeBlockEntity.class, EntityDataSerializers.DIRECTION);

    private static final EntityDataAccessor<Float> FALL_PROGRESS = SynchedEntityData.defineId(FallingTreeBlockEntity.class, EntityDataSerializers.FLOAT);

    private float prevFallProgress = 0.0F;

    private boolean droppedItems = false;

    public FallingTreeBlockEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public FallingTreeBlockEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.FALLING_TREE_BLOCK.get(), level);
        this.m_20011_(this.m_142242_());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(FALL_DIRECTION, Direction.NORTH);
        this.f_19804_.define(FALL_PROGRESS, 0.0F);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFallDirection(Direction.from3DDataValue(compound.getByte("FallDirection")));
        this.setFallProgress(compound.getFloat("FallProgress"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("FallDirection", (byte) this.getFallDirection().get3DDataValue());
        compound.putFloat("FallProgress", 0.0F);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevFallProgress = this.getFallProgress();
        if (this.getFallProgress() >= 1.0F) {
            BlockPos pos = BlockPos.containing(this.m_20185_(), this.m_20191_().maxY, this.m_20189_());
            if (!this.m_9236_().isClientSide && !this.droppedItems) {
                for (MovingBlockData dataBlock : this.getData()) {
                    BlockPos offset = dataBlock.getOffset();
                    if (!offset.equals(BlockPos.ZERO)) {
                        BlockPos rotatedOffset = new BlockPos(offset.m_123341_(), offset.m_123343_(), -offset.m_123342_()).rotate(this.getRotationFromDirection(this.getFallDirection()));
                        BlockPos fallPos = pos.offset(rotatedOffset);
                        while (this.m_9236_().getBlockState(fallPos).m_60795_() && fallPos.m_123342_() > this.m_9236_().m_141937_()) {
                            fallPos = fallPos.below();
                        }
                        this.createBlockDropAt(fallPos.above(), dataBlock.getState(), dataBlock.blockData);
                    }
                }
                this.droppedItems = true;
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        this.setFallProgress(Math.min(this.getFallProgress() + 0.05F, 1.0F));
    }

    public Rotation getRotationFromDirection(Direction direction) {
        switch(direction) {
            case NORTH:
                return Rotation.NONE;
            case EAST:
                return Rotation.CLOCKWISE_90;
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }

    public Direction getFallDirection() {
        return this.f_19804_.get(FALL_DIRECTION);
    }

    public void setFallDirection(Direction direction) {
        this.f_19804_.set(FALL_DIRECTION, direction);
    }

    protected float getFallProgress() {
        return this.f_19804_.get(FALL_PROGRESS);
    }

    public float getFallProgress(float partialTick) {
        return this.prevFallProgress + (this.getFallProgress() - this.prevFallProgress) * partialTick;
    }

    public void setFallProgress(float f) {
        this.f_19804_.set(FALL_PROGRESS, f);
    }

    @Override
    public boolean canBePlaced() {
        return false;
    }

    @Override
    public boolean movesEntities() {
        return false;
    }
}