package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.MovingBlockData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class CrushedBlockEntity extends AbstractMovingBlockEntity {

    private static final EntityDataAccessor<Float> CRUSH_PROGRESS = SynchedEntityData.defineId(CrushedBlockEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> DROP_CHANCE = SynchedEntityData.defineId(CrushedBlockEntity.class, EntityDataSerializers.FLOAT);

    private float prevCrushProgress = 0.0F;

    private float crushProgress = 0.0F;

    private boolean droppedItems = false;

    public CrushedBlockEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public CrushedBlockEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.CRUSHED_BLOCK.get(), level);
        this.m_20011_(this.m_142242_());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(CRUSH_PROGRESS, 0.0F);
        this.f_19804_.define(DROP_CHANCE, 1.0F);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.crushProgress = compound.getFloat("CrushProgress");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("CrushProgress", this.crushProgress);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevCrushProgress = this.crushProgress;
        if (this.crushProgress >= 1.0F) {
            BlockPos pos = BlockPos.containing(this.m_20185_(), this.m_20191_().maxY, this.m_20189_());
            if (!this.m_9236_().isClientSide && !this.droppedItems) {
                for (MovingBlockData dataBlock : this.getData()) {
                    BlockPos offset = dataBlock.getOffset();
                    if (this.m_9236_().random.nextFloat() < this.getDropChance()) {
                        BlockPos dropAtPos = pos.offset(offset);
                        while (this.m_9236_().getBlockState(dropAtPos).m_60795_() && dropAtPos.m_123342_() > this.m_9236_().m_141937_()) {
                            dropAtPos = dropAtPos.below();
                        }
                        this.createBlockDropAt(dropAtPos.above(), dataBlock.getState(), dataBlock.blockData);
                    }
                }
                this.droppedItems = true;
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (this.m_20096_()) {
            this.crushProgress = Math.min(this.crushProgress + 0.334F, 1.0F);
        } else {
            this.m_20256_(this.m_20184_().add(0.0, -1.0, 0.0));
        }
    }

    public float getCrushProgress(float partialTick) {
        return this.prevCrushProgress + (this.crushProgress - this.prevCrushProgress) * partialTick;
    }

    protected float getDropChance() {
        return this.f_19804_.get(DROP_CHANCE);
    }

    public void setDropChance(float f) {
        this.f_19804_.set(DROP_CHANCE, f);
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