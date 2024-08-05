package sereneseasons.handler.season;

import java.util.HashMap;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import sereneseasons.api.SSGameRules;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonChangedEvent;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;
import sereneseasons.handler.PacketHandler;
import sereneseasons.init.ModTags;
import sereneseasons.network.message.MessageSyncSeasonCycle;
import sereneseasons.season.SeasonSavedData;
import sereneseasons.season.SeasonTime;

public class SeasonHandler implements SeasonHelper.ISeasonDataProvider {

    private Season.SubSeason lastSeason = null;

    public static final HashMap<ResourceKey<Level>, Integer> clientSeasonCycleTicks = new HashMap();

    public static final HashMap<ResourceKey<Level>, Integer> prevServerSeasonCycleTicks = new HashMap();

    @SubscribeEvent
    public void onWorldTick(TickEvent.LevelTickEvent event) {
        Level world = event.level;
        if (event.phase == TickEvent.Phase.END && !world.isClientSide) {
            if (!ServerConfig.progressSeasonWhileOffline.get()) {
                MinecraftServer server = world.getServer();
                if (server != null && server.getPlayerList().getPlayerCount() == 0) {
                    return;
                }
            }
            if (!world.getGameRules().getBoolean(SSGameRules.RULE_DOSEASONCYCLE)) {
                return;
            }
            SeasonSavedData savedData = getSeasonSavedData(world);
            savedData.seasonCycleTicks = Mth.clamp(savedData.seasonCycleTicks, 0, SeasonTime.ZERO.getCycleDuration());
            if (++savedData.seasonCycleTicks > SeasonTime.ZERO.getCycleDuration()) {
                savedData.seasonCycleTicks = 0;
            }
            if (savedData.seasonCycleTicks % 20 == 0) {
                sendSeasonUpdate(world);
            }
            savedData.m_77762_();
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        Level world = player.m_9236_();
        sendSeasonUpdate(world);
    }

    public static SeasonTime getClientSeasonTime() {
        Integer i = (Integer) clientSeasonCycleTicks.get(Minecraft.getInstance().level.m_46472_());
        return new SeasonTime(i == null ? 0 : i);
    }

    @SubscribeEvent
    public void onWorldLoaded(LevelEvent.Load event) {
        clientSeasonCycleTicks.clear();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player != null) {
            ResourceKey<Level> dimension = Minecraft.getInstance().player.m_9236_().dimension();
            if (event.phase == TickEvent.Phase.END && ServerConfig.isDimensionWhitelisted(dimension)) {
                clientSeasonCycleTicks.compute(dimension, (k, v) -> v == null ? 0 : v + 1);
                if ((Integer) clientSeasonCycleTicks.get(dimension) > SeasonTime.ZERO.getCycleDuration()) {
                    clientSeasonCycleTicks.put(dimension, 0);
                }
                SeasonTime calendar = new SeasonTime((Integer) clientSeasonCycleTicks.get(dimension));
                if (calendar.getSubSeason() != this.lastSeason) {
                    Minecraft.getInstance().levelRenderer.allChanged();
                    this.lastSeason = calendar.getSubSeason();
                }
            }
        }
    }

    public static void sendSeasonUpdate(Level level) {
        if (!level.isClientSide) {
            SeasonSavedData savedData = getSeasonSavedData(level);
            SeasonTime newTime = new SeasonTime(savedData.seasonCycleTicks);
            SeasonTime prevTime = new SeasonTime((Integer) prevServerSeasonCycleTicks.computeIfAbsent(level.dimension(), key -> newTime.getSeasonCycleTicks()));
            Season.SubSeason prevSeason = prevTime.getSubSeason();
            Season.TropicalSeason prevTropicalSeason = prevTime.getTropicalSeason();
            Season.SubSeason newSeason = newTime.getSubSeason();
            Season.TropicalSeason newTropicalSeason = newTime.getTropicalSeason();
            prevServerSeasonCycleTicks.put(level.dimension(), newTime.getSeasonCycleTicks());
            if (!prevSeason.equals(newSeason)) {
                MinecraftForge.EVENT_BUS.post(new SeasonChangedEvent.Standard(level, prevSeason, newSeason));
            }
            if (!prevTropicalSeason.equals(newTropicalSeason)) {
                MinecraftForge.EVENT_BUS.post(new SeasonChangedEvent.Tropical(level, prevTropicalSeason, newTropicalSeason));
            }
            PacketHandler.HANDLER.send(PacketDistributor.ALL.noArg(), new MessageSyncSeasonCycle(level.dimension(), savedData.seasonCycleTicks));
        }
    }

    public static SeasonSavedData getSeasonSavedData(Level w) {
        if (!w.isClientSide() && w instanceof ServerLevel world) {
            DimensionDataStorage saveDataManager = world.getChunkSource().getDataStorage();
            Supplier<SeasonSavedData> defaultSaveDataSupplier = () -> {
                SeasonSavedData savedData = new SeasonSavedData();
                int startingSeason = ServerConfig.startingSubSeason.get();
                if (startingSeason == 0) {
                    savedData.seasonCycleTicks = world.f_46441_.nextInt(12) * SeasonTime.ZERO.getSubSeasonDuration();
                }
                if (startingSeason > 0) {
                    savedData.seasonCycleTicks = (startingSeason - 1) * SeasonTime.ZERO.getSubSeasonDuration();
                }
                savedData.m_77762_();
                return savedData;
            };
            return saveDataManager.computeIfAbsent(SeasonSavedData::load, defaultSaveDataSupplier, "seasons");
        } else {
            return null;
        }
    }

    @Override
    public ISeasonState getServerSeasonState(Level world) {
        SeasonSavedData savedData = getSeasonSavedData(world);
        return new SeasonTime(savedData.seasonCycleTicks);
    }

    @Override
    public ISeasonState getClientSeasonState() {
        Integer i = (Integer) clientSeasonCycleTicks.get(Minecraft.getInstance().level.m_46472_());
        return new SeasonTime(i == null ? 0 : i);
    }

    @Override
    public boolean usesTropicalSeasons(Holder<Biome> biome) {
        return biome.is(ModTags.Biomes.TROPICAL_BIOMES);
    }
}