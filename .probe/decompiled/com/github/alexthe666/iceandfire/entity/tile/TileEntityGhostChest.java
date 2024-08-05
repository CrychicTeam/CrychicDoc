package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityGhostChest extends ChestBlockEntity {

    public TileEntityGhostChest(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.GHOST_CHEST.get(), pos, state);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    public void startOpen(@NotNull Player player) {
        super.startOpen(player);
        if (this.f_58857_.m_46791_() != Difficulty.PEACEFUL) {
            EntityGhost ghost = IafEntityRegistry.GHOST.get().create(this.f_58857_);
            ghost.m_19890_((double) ((float) this.f_58858_.m_123341_() + 0.5F), (double) ((float) this.f_58858_.m_123342_() + 0.5F), (double) ((float) this.f_58858_.m_123343_() + 0.5F), ThreadLocalRandom.current().nextFloat() * 360.0F, 0.0F);
            if (!this.f_58857_.isClientSide) {
                ghost.finalizeSpawn((ServerLevel) this.f_58857_, this.f_58857_.getCurrentDifficultyAt(this.f_58858_), MobSpawnType.SPAWNER, null, null);
                if (!player.isCreative()) {
                    ghost.m_6710_(player);
                }
                ghost.m_21530_();
                this.f_58857_.m_7967_(ghost);
            }
            ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
            ghost.m_21446_(this.f_58858_, 4);
            ghost.setFromChest(true);
        }
    }

    @Override
    protected void signalOpenCount(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, int int0, int int1) {
        super.signalOpenCount(level, pos, state, int0, int1);
        level.updateNeighborsAt(pos.below(), state.m_60734_());
    }
}