package com.github.alexmodguy.alexscaves.server.level.map;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.item.CaveMapItem;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRarity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.message.UpdateCaveBiomeMapTagMessage;
import com.google.common.base.Stopwatch;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraftforge.common.WorldWorkerManager;
import net.minecraftforge.registries.ForgeRegistries;

public class CaveBiomeMapWorldWorker implements WorldWorkerManager.IWorker {

    private final Stopwatch stopwatch;

    private ItemStack map;

    private BlockPos center;

    private Player player;

    private ServerLevel serverLevel;

    private ResourceKey<Biome> biomeResourceKey;

    private UUID taskUUID;

    private Direction sampleDirection = Direction.UP;

    private BlockPos lastSampledPos = null;

    private BlockPos lastBiomePos = null;

    private boolean complete;

    private int samples = 0;

    private static final int SAMPLE_INCREMENT = AlexsCaves.COMMON_CONFIG.caveMapSearchWidth.get();

    private int width = 0;

    private int nextWidth = SAMPLE_INCREMENT;

    private BlockPos.MutableBlockPos nextPos = new BlockPos.MutableBlockPos();

    public CaveBiomeMapWorldWorker(ItemStack map, ServerLevel serverLevel, BlockPos center, Player player, UUID taskUUID) {
        this.map = map;
        this.serverLevel = serverLevel;
        this.center = center;
        ResourceKey<Biome> from = CaveMapItem.getBiomeTarget(map);
        this.biomeResourceKey = from == null ? ACBiomeRegistry.MAGNETIC_CAVES : from;
        this.player = player;
        this.taskUUID = taskUUID;
        this.stopwatch = Stopwatch.createStarted(Util.TICKER);
        this.nextPos.set(center);
    }

    @Override
    public boolean hasWork() {
        boolean ret = !this.complete && this.samples < AlexsCaves.COMMON_CONFIG.caveMapSearchAttempts.get() && !this.serverLevel.getServer().isStopped();
        if (!ret) {
            this.onWorkComplete(this.getLastFoundBiome());
        }
        return ret;
    }

    @Override
    public boolean doWork() {
        if (this.hasWork()) {
            this.samples++;
            int y = this.player.m_146904_();
            ServerChunkCache cache = this.serverLevel.getChunkSource();
            BiomeSource source = cache.getGenerator().getBiomeSource();
            Climate.Sampler sampler = cache.randomState().sampler();
            int height = 64;
            if (this.sampleDirection != Direction.UP) {
                this.nextPos.move(this.sampleDirection, SAMPLE_INCREMENT);
            }
            int[] searchedHeights = Mth.outFromOrigin(y, this.serverLevel.m_141937_() + 1, this.serverLevel.m_151558_(), 64).toArray();
            int nextBlockX = this.nextPos.m_123341_();
            int nextBlockZ = this.nextPos.m_123343_();
            int quartX = QuartPos.fromBlock(nextBlockX);
            int quartZ = QuartPos.fromBlock(nextBlockZ);
            if (ACBiomeRarity.isQuartInRareBiome(this.serverLevel.getSeed(), quartX, quartZ)) {
                for (int blockY : searchedHeights) {
                    int quartY = QuartPos.fromBlock(blockY);
                    Biome biome = (Biome) source.getNoiseBiome(quartX, quartY, quartZ, sampler).get();
                    if (verifyBiomeRespectRegistry(this.serverLevel, biome, this.biomeResourceKey)) {
                        this.lastBiomePos = new BlockPos(nextBlockX, blockY, nextBlockZ);
                    }
                }
            }
            this.width = this.width + SAMPLE_INCREMENT;
            if (this.width >= this.nextWidth) {
                if (this.sampleDirection == Direction.UP) {
                    this.sampleDirection = Direction.NORTH;
                } else {
                    this.nextWidth = this.nextWidth + SAMPLE_INCREMENT;
                    this.sampleDirection = this.sampleDirection.getClockWise();
                }
                this.width = 0;
            }
            this.lastSampledPos = this.nextPos.immutable();
            if (this.lastBiomePos == null) {
                return true;
            }
        }
        if (!this.complete) {
            this.onWorkComplete(this.lastBiomePos);
            this.complete = true;
        }
        return false;
    }

    public void onWorkComplete(@Nullable BlockPos biomeCorner) {
        if (!this.complete) {
            CompoundTag tag = this.map.getOrCreateTag();
            if (biomeCorner != null) {
                BlockPos centered = this.calculateBiomeCenter(biomeCorner);
                this.fillOutMapColors(centered, tag);
                tag.putInt("BiomeX", centered.m_123341_());
                tag.putInt("BiomeY", centered.m_123342_());
                tag.putInt("BiomeZ", centered.m_123343_());
                tag.putLong("RandomSeed", this.serverLevel.m_213780_().nextLong());
                tag.putBoolean("Filled", true);
                AlexsCaves.LOGGER.info("Found {} at {} {} {} in {}s", new Object[] { this.biomeResourceKey.location(), centered.m_123341_(), centered.m_123342_(), centered.m_123343_(), this.stopwatch.elapsed().toSeconds() });
            } else {
                int distance = 0;
                if (this.lastSampledPos != null) {
                    distance = (int) Math.sqrt(this.center.m_123331_(this.lastSampledPos));
                }
                this.player.m_213846_(Component.translatable("item.alexscaves.cave_map.error", distance).withStyle(ChatFormatting.RED));
                AlexsCaves.LOGGER.info("Could not find {} after {}s", this.biomeResourceKey.location(), this.stopwatch.elapsed().toSeconds());
            }
            tag.putBoolean("Loading", false);
            tag.remove("MapUUID");
            this.map.setTag(tag);
            AlexsCaves.sendMSGToAll(new UpdateCaveBiomeMapTagMessage(this.player.m_20148_(), this.getTaskUUID(), tag));
        }
        this.complete = true;
    }

    @Nullable
    public BlockPos getLastFoundBiome() {
        return this.lastBiomePos;
    }

    private static boolean verifyBiomeRespectRegistry(Level level, Biome biome, ResourceKey<Biome> matches) {
        Optional<Registry<Biome>> biomeRegistry = level.registryAccess().registry(ForgeRegistries.Keys.BIOMES);
        if (!biomeRegistry.isPresent()) {
            return false;
        } else {
            Optional<ResourceKey<Biome>> resourceKey = ((Registry) biomeRegistry.get()).getResourceKey(biome);
            return resourceKey.isPresent() && ((ResourceKey) resourceKey.get()).equals(matches);
        }
    }

    private BlockPos calculateBiomeCenter(BlockPos biomeCorner) {
        ServerChunkCache cache = this.serverLevel.getChunkSource();
        BiomeSource source = cache.getGenerator().getBiomeSource();
        Climate.Sampler sampler = cache.randomState().sampler();
        int biomeNorth = 0;
        int biomeSouth = 0;
        int biomeEast = 0;
        int biomeWest = 0;
        BlockPos yCentered;
        if (this.mapBiomeBeneathSurfaceOnly()) {
            int iterations = 0;
            for (yCentered = biomeCorner; iterations < 256 && this.getNoiseBiomeAtPos(source, yCentered, sampler).is(this.biomeResourceKey); yCentered = yCentered.above()) {
                iterations++;
            }
            yCentered = yCentered.below(10);
        } else {
            int biomeUp = 0;
            int biomeDown = 0;
            while (biomeUp < 32 && this.getNoiseBiomeAtPos(source, biomeCorner.above(biomeUp), sampler).is(this.biomeResourceKey)) {
                biomeUp += 8;
            }
            while (biomeDown < 64 && this.getNoiseBiomeAtPos(source, biomeCorner.below(biomeDown), sampler).is(this.biomeResourceKey)) {
                biomeDown += 8;
            }
            yCentered = biomeCorner.atY((int) Math.floor((double) ((float) biomeUp * 0.25F)) - biomeDown);
        }
        while (biomeNorth < 800 && this.getNoiseBiomeAtPos(source, yCentered.north(biomeNorth), sampler).is(this.biomeResourceKey)) {
            biomeNorth += 8;
        }
        while (biomeSouth < 800 && this.getNoiseBiomeAtPos(source, yCentered.south(biomeSouth), sampler).is(this.biomeResourceKey)) {
            biomeSouth += 8;
        }
        while (biomeEast < 800 && this.getNoiseBiomeAtPos(source, yCentered.east(biomeEast), sampler).is(this.biomeResourceKey)) {
            biomeEast += 8;
        }
        while (biomeWest < 800 && this.getNoiseBiomeAtPos(source, yCentered.west(biomeWest), sampler).is(this.biomeResourceKey)) {
            biomeWest += 8;
        }
        return yCentered.offset(biomeEast - biomeWest, 0, biomeSouth - biomeNorth);
    }

    private Holder<Biome> getNoiseBiomeAtPos(BiomeSource source, BlockPos pos, Climate.Sampler sampler) {
        return source.getNoiseBiome(pos.m_123341_() >> 2, pos.m_123342_() >> 2, pos.m_123343_() >> 2, sampler);
    }

    private void fillOutMapColors(BlockPos first, CompoundTag tag) {
        Registry<Biome> registry = (Registry<Biome>) this.serverLevel.m_9598_().registry(Registries.BIOME).orElse(null);
        byte[] arr = new byte[16384];
        Map<Integer, Byte> biomeMap = new HashMap();
        ServerChunkCache cache = this.serverLevel.getChunkSource();
        BiomeSource source = cache.getGenerator().getBiomeSource();
        Climate.Sampler sampler = cache.randomState().sampler();
        if (registry != null) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int scale = 7;
            int j = first.m_123341_();
            int k = first.m_123343_();
            int l = j / scale - 64;
            int i1 = k / scale - 64;
            for (int j1 = 0; j1 < 128; j1++) {
                for (int k1 = 0; k1 < 128; k1++) {
                    mutableBlockPos.set((l + k1) * scale, first.m_123342_(), (i1 + j1) * scale);
                    Holder<Biome> holder1 = null;
                    if (this.mapBiomeBeneathSurfaceOnly()) {
                        mutableBlockPos.setY(first.m_123342_() - 5);
                        holder1 = source.getNoiseBiome(mutableBlockPos.m_123341_() >> 2, mutableBlockPos.m_123342_() >> 2, mutableBlockPos.m_123343_() >> 2, sampler);
                    } else {
                        for (int yUpFromBottom = this.serverLevel.m_141937_() + 1; yUpFromBottom < this.serverLevel.m_5736_(); yUpFromBottom += 32) {
                            mutableBlockPos.setY(yUpFromBottom);
                            holder1 = source.getNoiseBiome(mutableBlockPos.m_123341_() >> 2, mutableBlockPos.m_123342_() >> 2, mutableBlockPos.m_123343_() >> 2, sampler);
                            if (holder1.is(this.biomeResourceKey)) {
                                break;
                            }
                        }
                    }
                    int id = holder1 == null ? 0 : registry.getId(holder1.value());
                    byte biomeHash;
                    if (biomeMap.containsKey(id)) {
                        biomeHash = (Byte) biomeMap.get(id);
                    } else {
                        biomeHash = (byte) biomeMap.size();
                        biomeMap.put(id, biomeHash);
                    }
                    arr[j1 * 128 + k1] = biomeHash;
                }
            }
        }
        ListTag listTag = new ListTag();
        for (Entry<Integer, Byte> entry : biomeMap.entrySet()) {
            CompoundTag biomeEntryTag = new CompoundTag();
            biomeEntryTag.putInt("BiomeID", (Integer) entry.getKey());
            biomeEntryTag.putInt("BiomeHash", (Byte) entry.getValue());
            listTag.add(biomeEntryTag);
        }
        tag.put("MapBiomeList", listTag);
        tag.putByteArray("MapBiomes", arr);
    }

    private boolean mapBiomeBeneathSurfaceOnly() {
        return this.biomeResourceKey.equals(ACBiomeRegistry.ABYSSAL_CHASM);
    }

    public UUID getTaskUUID() {
        return this.taskUUID;
    }
}