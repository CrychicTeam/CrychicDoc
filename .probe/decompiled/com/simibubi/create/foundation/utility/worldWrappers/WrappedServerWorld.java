package com.simibubi.create.foundation.utility.worldWrappers;

import com.simibubi.create.foundation.mixin.accessor.EntityAccessor;
import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.ticks.LevelTicks;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WrappedServerWorld extends ServerLevel {

    protected ServerLevel world;

    public WrappedServerWorld(ServerLevel world) {
        super(world.getServer(), Util.backgroundExecutor(), world.getServer().storageSource, (ServerLevelData) world.m_6106_(), world.m_46472_(), new LevelStem(world.m_204156_(), world.getChunkSource().getGenerator()), new DummyStatusListener(), world.m_46659_(), world.m_7062_().biomeZoomSeed, Collections.emptyList(), false, world.getRandomSequences());
        this.world = world;
    }

    @Override
    public float getSunAngle(float p_72826_1_) {
        return 0.0F;
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
    public LevelTicks<Block> getBlockTicks() {
        return super.getBlockTicks();
    }

    @Override
    public LevelTicks<Fluid> getFluidTicks() {
        return super.getFluidTicks();
    }

    @Override
    public void levelEvent(Player player, int type, BlockPos pos, int data) {
    }

    @Override
    public List<ServerPlayer> players() {
        return Collections.emptyList();
    }

    @Override
    public void playSound(Player player, double x, double y, double z, SoundEvent soundIn, SoundSource category, float volume, float pitch) {
    }

    @Override
    public void playSound(Player p_217384_1_, Entity p_217384_2_, SoundEvent p_217384_3_, SoundSource p_217384_4_, float p_217384_5_, float p_217384_6_) {
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
        return this.world.addFreshEntity(entityIn);
    }

    @Override
    public void setMapData(String mapId, MapItemSavedData mapDataIn) {
    }

    @Override
    public int getFreeMapId() {
        return 0;
    }

    @Override
    public void destroyBlockProgress(int breakerId, BlockPos pos, int progress) {
    }

    @Override
    public RecipeManager getRecipeManager() {
        return this.world.getRecipeManager();
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int p_225604_1_, int p_225604_2_, int p_225604_3_) {
        return this.world.getUncachedNoiseBiome(p_225604_1_, p_225604_2_, p_225604_3_);
    }
}