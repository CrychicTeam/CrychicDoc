package com.github.alexthe666.iceandfire.entity.tile;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityDreadSpawner extends SpawnerBlockEntity {

    private final BlockEntityType<?> type;

    private final DreadSpawnerBaseLogic spawner = new DreadSpawnerBaseLogic() {

        @Override
        public void broadcastEvent(Level p_155767_, @NotNull BlockPos p_155768_, int p_155769_) {
            p_155767_.blockEvent(p_155768_, Blocks.SPAWNER, p_155769_, 0);
        }

        @Override
        public void setNextSpawnData(@Nullable Level p_155771_, @NotNull BlockPos p_155772_, @NotNull SpawnData p_155773_) {
            super.m_142667_(p_155771_, p_155772_, p_155773_);
            if (p_155771_ != null) {
                BlockState blockstate = p_155771_.getBlockState(p_155772_);
                p_155771_.sendBlockUpdated(p_155772_, blockstate, blockstate, 4);
            }
        }

        @Nullable
        public BlockEntity getSpawnerBlockEntity() {
            return TileEntityDreadSpawner.this;
        }
    };

    public TileEntityDreadSpawner(BlockPos pos, BlockState state) {
        super(pos, state);
        this.type = IafTileEntityRegistry.DREAD_SPAWNER.get();
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.spawner.m_151328_(this.f_58857_, this.f_58858_, compoundTag0);
    }

    public CompoundTag save(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        this.spawner.m_186381_(compoundTag0);
        return compoundTag0;
    }

    public static void clientTick(Level level0, BlockPos blockPos1, BlockState blockState2, TileEntityDreadSpawner tileEntityDreadSpawner3) {
        tileEntityDreadSpawner3.spawner.clientTick(level0, blockPos1);
    }

    public static void serverTick(Level level0, BlockPos blockPos1, BlockState blockState2, TileEntityDreadSpawner tileEntityDreadSpawner3) {
        tileEntityDreadSpawner3.spawner.m_151311_((ServerLevel) level0, blockPos1);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = this.save(new CompoundTag());
        compoundtag.remove("SpawnPotentials");
        return compoundtag;
    }

    @Override
    public boolean triggerEvent(int int0, int int1) {
        return this.spawner.m_151316_(this.f_58857_, int0) || super.triggerEvent(int0, int1);
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    @NotNull
    @Override
    public BaseSpawner getSpawner() {
        return this.spawner;
    }

    @NotNull
    @Override
    public BlockEntityType<?> getType() {
        return this.type != null ? this.type : super.m_58903_();
    }
}