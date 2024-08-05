package net.minecraft.world.level.block.entity;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SpawnerBlockEntity extends BlockEntity {

    private final BaseSpawner spawner = new BaseSpawner() {

        @Override
        public void broadcastEvent(Level p_155767_, BlockPos p_155768_, int p_155769_) {
            p_155767_.blockEvent(p_155768_, Blocks.SPAWNER, p_155769_, 0);
        }

        @Override
        public void setNextSpawnData(@Nullable Level p_155771_, BlockPos p_155772_, SpawnData p_155773_) {
            super.setNextSpawnData(p_155771_, p_155772_, p_155773_);
            if (p_155771_ != null) {
                BlockState $$3 = p_155771_.getBlockState(p_155772_);
                p_155771_.sendBlockUpdated(p_155772_, $$3, $$3, 4);
            }
        }
    };

    public SpawnerBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.MOB_SPAWNER, blockPos0, blockState1);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.spawner.load(this.f_58857_, this.f_58858_, compoundTag0);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        this.spawner.save(compoundTag0);
    }

    public static void clientTick(Level level0, BlockPos blockPos1, BlockState blockState2, SpawnerBlockEntity spawnerBlockEntity3) {
        spawnerBlockEntity3.spawner.clientTick(level0, blockPos1);
    }

    public static void serverTick(Level level0, BlockPos blockPos1, BlockState blockState2, SpawnerBlockEntity spawnerBlockEntity3) {
        spawnerBlockEntity3.spawner.serverTick((ServerLevel) level0, blockPos1);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag $$0 = this.m_187482_();
        $$0.remove("SpawnPotentials");
        return $$0;
    }

    @Override
    public boolean triggerEvent(int int0, int int1) {
        return this.spawner.onEventTriggered(this.f_58857_, int0) ? true : super.triggerEvent(int0, int1);
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public void setEntityId(EntityType<?> entityType0, RandomSource randomSource1) {
        this.spawner.setEntityId(entityType0, this.f_58857_, randomSource1, this.f_58858_);
    }

    public BaseSpawner getSpawner() {
        return this.spawner;
    }
}