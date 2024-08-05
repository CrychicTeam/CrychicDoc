package com.simibubi.create.foundation.utility.worldWrappers;

import com.simibubi.create.foundation.mixin.accessor.EntityAccessor;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.ticks.LevelTickAccess;

public class WrappedWorld extends Level {

    protected Level world;

    protected ChunkSource chunkSource;

    protected LevelEntityGetter<Entity> entityGetter = new DummyLevelEntityGetter<>();

    public WrappedWorld(Level world) {
        super((WritableLevelData) world.getLevelData(), world.dimension(), world.registryAccess(), world.dimensionTypeRegistration(), world::m_46473_, world.isClientSide, world.isDebug(), 0L, 0);
        this.world = world;
    }

    public void setChunkSource(ChunkSource source) {
        this.chunkSource = source;
    }

    public Level getLevel() {
        return this.world;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.world.getLightEngine();
    }

    @Override
    public BlockState getBlockState(@Nullable BlockPos pos) {
        return this.world.getBlockState(pos);
    }

    @Override
    public boolean isStateAtPosition(BlockPos p_217375_1_, Predicate<BlockState> p_217375_2_) {
        return this.world.isStateAtPosition(p_217375_1_, p_217375_2_);
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return this.world.getBlockEntity(pos);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState newState, int flags) {
        return this.world.setBlock(pos, newState, flags);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPos pos) {
        return 15;
    }

    @Override
    public void sendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, int flags) {
        this.world.sendBlockUpdated(pos, oldState, newState, flags);
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return this.world.m_183326_();
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return this.world.m_183324_();
    }

    @Override
    public ChunkSource getChunkSource() {
        return this.chunkSource != null ? this.chunkSource : this.world.m_7726_();
    }

    @Override
    public void levelEvent(@Nullable Player player, int type, BlockPos pos, int data) {
    }

    @Override
    public List<? extends Player> players() {
        return Collections.emptyList();
    }

    @Override
    public void playSeededSound(Player player0, double double1, double double2, double double3, SoundEvent soundEvent4, SoundSource soundSource5, float float6, float float7, long long8) {
    }

    @Override
    public void playSeededSound(Player pPlayer, double pX, double pY, double pZ, Holder<SoundEvent> pSound, SoundSource pSource, float pVolume, float pPitch, long pSeed) {
    }

    @Override
    public void playSeededSound(Player pPlayer, Entity pEntity, Holder<SoundEvent> pSound, SoundSource pCategory, float pVolume, float pPitch, long pSeed) {
    }

    @Override
    public void playSound(@Nullable Player player, double x, double y, double z, SoundEvent soundIn, SoundSource category, float volume, float pitch) {
    }

    @Override
    public void playSound(@Nullable Player p_217384_1_, Entity p_217384_2_, SoundEvent p_217384_3_, SoundSource p_217384_4_, float p_217384_5_, float p_217384_6_) {
    }

    @Override
    public Entity getEntity(int id) {
        return null;
    }

    @Override
    public MapItemSavedData getMapData(String mapName) {
        return null;
    }

    @Override
    public boolean addFreshEntity(Entity entityIn) {
        ((EntityAccessor) entityIn).create$callSetLevel(this.world);
        return this.world.m_7967_(entityIn);
    }

    @Override
    public void setMapData(String pMapId, MapItemSavedData pData) {
    }

    @Override
    public int getFreeMapId() {
        return this.world.getFreeMapId();
    }

    @Override
    public void destroyBlockProgress(int breakerId, BlockPos pos, int progress) {
    }

    @Override
    public Scoreboard getScoreboard() {
        return this.world.getScoreboard();
    }

    @Override
    public RecipeManager getRecipeManager() {
        return this.world.getRecipeManager();
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int p_225604_1_, int p_225604_2_, int p_225604_3_) {
        return this.world.m_203675_(p_225604_1_, p_225604_2_, p_225604_3_);
    }

    @Override
    public RegistryAccess registryAccess() {
        return this.world.registryAccess();
    }

    @Override
    public float getShade(Direction p_230487_1_, boolean p_230487_2_) {
        return this.world.m_7717_(p_230487_1_, p_230487_2_);
    }

    @Override
    public void updateNeighbourForOutputSignal(BlockPos p_175666_1_, Block p_175666_2_) {
    }

    @Override
    public void gameEvent(Entity pEntity, GameEvent pEvent, BlockPos pPos) {
    }

    @Override
    public void gameEvent(GameEvent gameEvent0, Vec3 vec1, GameEvent.Context gameEventContext2) {
    }

    @Override
    public String gatherChunkSourceStats() {
        return this.world.gatherChunkSourceStats();
    }

    @Override
    protected LevelEntityGetter<Entity> getEntities() {
        return this.entityGetter;
    }

    @Override
    public int getMaxBuildHeight() {
        return this.m_141937_() + this.m_141928_();
    }

    @Override
    public int getSectionsCount() {
        return this.getMaxSection() - this.getMinSection();
    }

    @Override
    public int getMinSection() {
        return SectionPos.blockToSectionCoord(this.m_141937_());
    }

    @Override
    public int getMaxSection() {
        return SectionPos.blockToSectionCoord(this.getMaxBuildHeight() - 1) + 1;
    }

    @Override
    public boolean isOutsideBuildHeight(BlockPos pos) {
        return this.isOutsideBuildHeight(pos.m_123342_());
    }

    @Override
    public boolean isOutsideBuildHeight(int y) {
        return y < this.m_141937_() || y >= this.getMaxBuildHeight();
    }

    @Override
    public int getSectionIndex(int y) {
        return this.getSectionIndexFromSectionY(SectionPos.blockToSectionCoord(y));
    }

    @Override
    public int getSectionIndexFromSectionY(int sectionY) {
        return sectionY - this.getMinSection();
    }

    @Override
    public int getSectionYFromSectionIndex(int sectionIndex) {
        return sectionIndex + this.getMinSection();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return this.world.m_246046_();
    }
}