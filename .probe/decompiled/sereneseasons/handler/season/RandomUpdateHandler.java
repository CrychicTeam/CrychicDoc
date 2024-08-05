package sereneseasons.handler.season;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.SeasonsConfig;
import sereneseasons.config.ServerConfig;
import sereneseasons.init.ModTags;
import sereneseasons.season.SeasonHooks;

@EventBusSubscriber
public class RandomUpdateHandler {

    private static void adjustWeatherFrequency(Level world, Season season) {
        if (SeasonsConfig.changeWeatherFrequency.get()) {
            ServerLevelData serverLevelData = (ServerLevelData) world.getLevelData();
            if (season == Season.WINTER) {
                if (serverLevelData.m_6534_()) {
                    serverLevelData.setThundering(false);
                }
                if (!world.getLevelData().isRaining() && serverLevelData.getRainTime() > 36000) {
                    serverLevelData.setRainTime(world.random.nextInt(24000) + 12000);
                }
            } else if (season == Season.SPRING) {
                if (!world.getLevelData().isRaining() && serverLevelData.getRainTime() > 96000) {
                    serverLevelData.setRainTime(world.random.nextInt(84000) + 12000);
                }
            } else if (season == Season.SUMMER && !world.getLevelData().isThundering() && serverLevelData.getThunderTime() > 36000) {
                serverLevelData.setThunderTime(world.random.nextInt(24000) + 12000);
            }
        }
    }

    private static void meltInChunk(ChunkMap chunkManager, LevelChunk chunkIn, float meltChance) {
        ServerLevel world = chunkManager.level;
        ChunkPos chunkpos = chunkIn.m_7697_();
        int i = chunkpos.getMinBlockX();
        int j = chunkpos.getMinBlockZ();
        if (meltChance > 0.0F && world.f_46441_.nextFloat() < meltChance) {
            BlockPos topAirPos = world.m_5452_(Heightmap.Types.MOTION_BLOCKING, world.m_46496_(i, 0, j, 15));
            BlockPos topGroundPos = topAirPos.below();
            BlockState aboveGroundState = world.m_8055_(topAirPos);
            BlockState groundState = world.m_8055_(topGroundPos);
            Holder<Biome> biome = world.m_204166_(topAirPos);
            if (biome.is(ModTags.Biomes.BLACKLISTED_BIOMES)) {
                return;
            }
            if (SeasonHooks.getBiomeTemperature((Level) world, biome, topGroundPos) >= 0.15F) {
                if (aboveGroundState.m_60734_() == Blocks.SNOW) {
                    world.m_46597_(topAirPos, Blocks.AIR.defaultBlockState());
                } else if (groundState.m_60734_() == Blocks.ICE) {
                    ((IceBlock) Blocks.ICE).melt(groundState, world, topGroundPos);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
            Season.SubSeason subSeason = SeasonHelper.getSeasonState(event.level).getSubSeason();
            Season season = subSeason.getSeason();
            ServerConfig.MeltChanceInfo meltInfo = ServerConfig.getMeltInfo(subSeason);
            float meltRand = meltInfo.getMeltChance() / 100.0F;
            int rolls = meltInfo.getRolls();
            adjustWeatherFrequency(event.level, season);
            if (rolls > 0 && meltRand > 0.0F && SeasonsConfig.generateSnowAndIce.get() && ServerConfig.isDimensionWhitelisted(event.level.dimension())) {
                ServerLevel level = (ServerLevel) event.level;
                ChunkMap chunkMap = level.getChunkSource().chunkMap;
                DistanceManager distanceManager = chunkMap.getDistanceManager();
                int l = distanceManager.getNaturalSpawnChunkCount();
                List<RandomUpdateHandler.ChunkAndHolder> list = Lists.newArrayListWithCapacity(l);
                for (ChunkHolder chunkholder : chunkMap.getChunks()) {
                    LevelChunk levelchunk = chunkholder.getTickingChunk();
                    if (levelchunk != null) {
                        list.add(new RandomUpdateHandler.ChunkAndHolder(levelchunk, chunkholder));
                    }
                }
                Collections.shuffle(list);
                for (RandomUpdateHandler.ChunkAndHolder serverchunkcache$chunkandholder : list) {
                    LevelChunk levelChunk = serverchunkcache$chunkandholder.chunk;
                    ChunkPos chunkpos = levelChunk.m_7697_();
                    if ((chunkMap.anyPlayerCloseEnoughForSpawning(chunkpos) || distanceManager.shouldForceTicks(chunkpos.toLong())) && level.shouldTickBlocksAt(chunkpos.toLong())) {
                        for (int i = 0; i < rolls; i++) {
                            meltInChunk(chunkMap, levelChunk, meltRand);
                        }
                    }
                }
            }
        }
    }

    static record ChunkAndHolder(LevelChunk chunk, ChunkHolder holder) {
    }
}