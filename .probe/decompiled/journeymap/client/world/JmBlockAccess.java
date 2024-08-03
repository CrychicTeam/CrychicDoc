package journeymap.client.world;

import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import journeymap.client.data.DataCache;
import journeymap.client.model.ChunkMD;
import journeymap.common.helper.DimensionHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

@MethodsReturnNonnullByDefault
public enum JmBlockAccess implements BlockAndTintGetter {

    INSTANCE;

    @Override
    public BlockEntity getBlockEntity(@NotNull BlockPos pos) {
        return this.getWorld().getBlockEntity(pos);
    }

    @Override
    public BlockState getBlockState(@NotNull BlockPos pos) {
        if (!this.isValid(pos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            ChunkMD chunkMD = this.getChunkMDFromBlockCoords(pos);
            return chunkMD != null && chunkMD.hasChunk() ? chunkMD.getChunkBlockState(new BlockPos(pos.m_123341_() & 15, pos.m_123342_(), pos.m_123343_() & 15)) : Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public FluidState getFluidState(@NotNull BlockPos blockPos) {
        return this.getWorld().getFluidState(blockPos);
    }

    @Override
    public int getLightEmission(@NotNull BlockPos p_217298_1_) {
        return this.getWorld().m_7146_(p_217298_1_);
    }

    @Override
    public int getMaxLightLevel() {
        return this.getWorld().m_7469_();
    }

    @Override
    public int getHeight() {
        return this.getWorld().m_141928_();
    }

    @Override
    public int getMinBuildHeight() {
        return this.getWorld().m_141937_();
    }

    @Override
    public int getMaxBuildHeight() {
        return this.getWorld().m_151558_();
    }

    @Override
    public BlockHitResult clip(ClipContext context) {
        return this.getWorld().m_45547_(context);
    }

    @Nullable
    @Override
    public BlockHitResult clipWithInteractionOverride(Vec3 vec3d, Vec3 vec3d_1, BlockPos blockPos, VoxelShape voxelShape, BlockState blockState) {
        return this.getWorld().m_45558_(vec3d, vec3d_1, blockPos, voxelShape, blockState);
    }

    public Biome getBiome(BlockPos pos) {
        ClientLevel world = Minecraft.getInstance().level;
        return this.getBiome(pos, world.m_9598_().registryOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS));
    }

    @Nullable
    public Biome getBiome(BlockPos pos, Biome defaultBiome) {
        ChunkMD chunkMD = this.getChunkMDFromBlockCoords(pos);
        if (chunkMD != null && chunkMD.hasChunk()) {
            Biome biome = chunkMD.getBiome(pos);
            if (biome != null) {
                return biome;
            }
        }
        if (Minecraft.getInstance().hasSingleplayerServer()) {
            MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
            if (server != null) {
                try {
                    return (Biome) server.getLevel(DimensionHelper.getDimension(Minecraft.getInstance().player)).m_203495_(pos.m_123341_() >> 2, pos.m_123342_() >> 2, pos.m_123343_() >> 2).value();
                } catch (Exception var6) {
                    return (Biome) server.getLevel(DimensionHelper.getDimension(Minecraft.getInstance().player)).m_204166_(pos).value();
                }
            }
        }
        return defaultBiome;
    }

    public Level getWorld() {
        return Minecraft.getInstance().level;
    }

    private boolean isValid(BlockPos pos) {
        return pos.m_123341_() >= -30000000 && pos.m_123343_() >= -30000000 && pos.m_123341_() < 30000000 && pos.m_123343_() < 30000000 && pos.m_123342_() >= 0 && pos.m_123342_() < 256;
    }

    @Nullable
    private ChunkMD getChunkMDFromBlockCoords(BlockPos pos) {
        return DataCache.INSTANCE.getChunkMD(ChunkPos.asLong(pos));
    }

    @Override
    public float getShade(@NotNull Direction p_230487_1_, boolean p_230487_2_) {
        return this.getWorld().m_7717_(p_230487_1_, p_230487_2_);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.getWorld().getLightEngine();
    }

    @Override
    public int getBlockTint(@NotNull BlockPos blockPos, ColorResolver colorResolver) {
        int i = Minecraft.getInstance().options.biomeBlendRadius().get();
        int j = (i * 2 + 1) * (i * 2 + 1);
        int k = 0;
        int l = 0;
        int i1 = 0;
        Cursor3D cursor3d = new Cursor3D(blockPos.m_123341_() - i, blockPos.m_123342_(), blockPos.m_123343_() - i, blockPos.m_123341_() + i, blockPos.m_123342_(), blockPos.m_123343_() + i);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        while (cursor3d.advance()) {
            blockpos$mutableblockpos.set(cursor3d.nextX(), cursor3d.nextY(), cursor3d.nextZ());
            int j1 = colorResolver.getColor(this.getBiome(blockpos$mutableblockpos), (double) blockpos$mutableblockpos.m_123341_(), (double) blockpos$mutableblockpos.m_123343_());
            k += (j1 & 0xFF0000) >> 16;
            l += (j1 & 0xFF00) >> 8;
            i1 += j1 & 0xFF;
        }
        return (k / j & 0xFF) << 16 | (l / j & 0xFF) << 8 | i1 / j & 0xFF;
    }

    @Override
    public int getBrightness(@NotNull LightLayer lightType, @NotNull BlockPos blockPos) {
        return this.getWorld().m_45517_(lightType, blockPos);
    }

    @Override
    public int getRawBrightness(@NotNull BlockPos blockPosIn, int amount) {
        return this.getWorld().m_45524_(blockPosIn, amount);
    }

    @Override
    public boolean canSeeSky(@NotNull BlockPos blockPosIn) {
        return this.getWorld().m_45527_(blockPosIn);
    }
}