package net.minecraft.world.level;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.redstone.NeighborUpdater;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.TickPriority;

public interface LevelAccessor extends CommonLevelAccessor, LevelTimeAccess {

    @Override
    default long dayTime() {
        return this.getLevelData().getDayTime();
    }

    long nextSubTickCount();

    LevelTickAccess<Block> getBlockTicks();

    private <T> ScheduledTick<T> createTick(BlockPos blockPos0, T t1, int int2, TickPriority tickPriority3) {
        return new ScheduledTick<>(t1, blockPos0, this.getLevelData().getGameTime() + (long) int2, tickPriority3, this.nextSubTickCount());
    }

    private <T> ScheduledTick<T> createTick(BlockPos blockPos0, T t1, int int2) {
        return new ScheduledTick<>(t1, blockPos0, this.getLevelData().getGameTime() + (long) int2, this.nextSubTickCount());
    }

    default void scheduleTick(BlockPos blockPos0, Block block1, int int2, TickPriority tickPriority3) {
        this.getBlockTicks().m_183393_(this.createTick(blockPos0, block1, int2, tickPriority3));
    }

    default void scheduleTick(BlockPos blockPos0, Block block1, int int2) {
        this.getBlockTicks().m_183393_(this.createTick(blockPos0, block1, int2));
    }

    LevelTickAccess<Fluid> getFluidTicks();

    default void scheduleTick(BlockPos blockPos0, Fluid fluid1, int int2, TickPriority tickPriority3) {
        this.getFluidTicks().m_183393_(this.createTick(blockPos0, fluid1, int2, tickPriority3));
    }

    default void scheduleTick(BlockPos blockPos0, Fluid fluid1, int int2) {
        this.getFluidTicks().m_183393_(this.createTick(blockPos0, fluid1, int2));
    }

    LevelData getLevelData();

    DifficultyInstance getCurrentDifficultyAt(BlockPos var1);

    @Nullable
    MinecraftServer getServer();

    default Difficulty getDifficulty() {
        return this.getLevelData().getDifficulty();
    }

    ChunkSource getChunkSource();

    @Override
    default boolean hasChunk(int int0, int int1) {
        return this.getChunkSource().hasChunk(int0, int1);
    }

    RandomSource getRandom();

    default void blockUpdated(BlockPos blockPos0, Block block1) {
    }

    default void neighborShapeChanged(Direction direction0, BlockState blockState1, BlockPos blockPos2, BlockPos blockPos3, int int4, int int5) {
        NeighborUpdater.executeShapeUpdate(this, direction0, blockState1, blockPos2, blockPos3, int4, int5 - 1);
    }

    default void playSound(@Nullable Player player0, BlockPos blockPos1, SoundEvent soundEvent2, SoundSource soundSource3) {
        this.playSound(player0, blockPos1, soundEvent2, soundSource3, 1.0F, 1.0F);
    }

    void playSound(@Nullable Player var1, BlockPos var2, SoundEvent var3, SoundSource var4, float var5, float var6);

    void addParticle(ParticleOptions var1, double var2, double var4, double var6, double var8, double var10, double var12);

    void levelEvent(@Nullable Player var1, int var2, BlockPos var3, int var4);

    default void levelEvent(int int0, BlockPos blockPos1, int int2) {
        this.levelEvent(null, int0, blockPos1, int2);
    }

    void gameEvent(GameEvent var1, Vec3 var2, GameEvent.Context var3);

    default void gameEvent(@Nullable Entity entity0, GameEvent gameEvent1, Vec3 vec2) {
        this.gameEvent(gameEvent1, vec2, new GameEvent.Context(entity0, null));
    }

    default void gameEvent(@Nullable Entity entity0, GameEvent gameEvent1, BlockPos blockPos2) {
        this.gameEvent(gameEvent1, blockPos2, new GameEvent.Context(entity0, null));
    }

    default void gameEvent(GameEvent gameEvent0, BlockPos blockPos1, GameEvent.Context gameEventContext2) {
        this.gameEvent(gameEvent0, Vec3.atCenterOf(blockPos1), gameEventContext2);
    }
}