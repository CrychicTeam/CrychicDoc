package journeymap.client.data;

import com.google.common.base.Strings;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.dto.RealmsServer;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.feature.Feature;
import journeymap.client.feature.FeatureManager;
import journeymap.client.log.JMLogger;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionCoord;
import journeymap.common.Journeymap;
import journeymap.common.helper.DimensionHelper;
import journeymap.common.log.LogFormatter;
import journeymap.common.version.VersionCheck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.DownloadedPackSource;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.LevelData;
import org.apache.commons.lang3.text.WordUtils;

public class WorldData extends CacheLoader<Class, WorldData> {

    private static String DAYTIME = Constants.getString("jm.theme.labelsource.gametime.day");

    private static String SUNRISE = Constants.getString("jm.theme.labelsource.gametime.sunrise");

    private static String SUNSET = Constants.getString("jm.theme.labelsource.gametime.sunset");

    private static String NIGHT = Constants.getString("jm.theme.labelsource.gametime.night");

    String name;

    String dimension;

    long time;

    boolean hardcore;

    boolean singlePlayer;

    Map<Feature, Boolean> features;

    String jm_version;

    String latest_journeymap_version;

    String mc_version;

    String mod_name = JourneymapClient.MOD_NAME;

    String iconSetName;

    String[] iconSetNames;

    public static boolean isHardcoreAndMultiplayer() {
        WorldData world = DataCache.INSTANCE.getWorld(false);
        return world.hardcore && !world.singlePlayer;
    }

    private static String getServerName() {
        try {
            String serverName = null;
            Minecraft mc = Minecraft.getInstance();
            if (!mc.hasSingleplayerServer()) {
                try {
                    ClientPacketListener netHandler = mc.getConnection();
                    if (netHandler.callbackScreen instanceof RealmsScreen realmsScreen && realmsScreen instanceof RealmsMainScreen mainScreen) {
                        RealmsServer selectedServer = mainScreen.getSelectedServer();
                        for (RealmsServer realmsServer : mainScreen.realmsServers) {
                            if (realmsServer.id == selectedServer.id) {
                                serverName = realmsServer.name;
                                break;
                            }
                        }
                    }
                } catch (Throwable var10) {
                    Journeymap.getLogger().error("Unable to get Realms server name: " + LogFormatter.toString(var10));
                }
            }
            if (serverName != null) {
                return serverName;
            } else {
                mc = Minecraft.getInstance();
                ServerData serverData = mc.getCurrentServer();
                if (serverData != null) {
                    serverName = serverData.name;
                    if (serverName != null) {
                        serverName = serverName.replaceAll("\\W+", "~").trim();
                        if (Strings.isNullOrEmpty(serverName.replaceAll("~", ""))) {
                            serverName = serverData.ip;
                        }
                        return serverName;
                    }
                }
                return null;
            }
        } catch (Throwable var11) {
            Journeymap.getLogger().error("Couldn't get service name: " + LogFormatter.toString(var11));
            return getLegacyServerName();
        }
    }

    public static String getLegacyServerName() {
        try {
            Connection netManager = Minecraft.getInstance().getConnection().getConnection();
            if (netManager != null) {
                SocketAddress socketAddress = netManager.getRemoteAddress();
                if (socketAddress != null && socketAddress instanceof InetSocketAddress inetAddr) {
                    return inetAddr.getHostName();
                }
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error("Couldn't get server name: " + LogFormatter.toString(var3));
        }
        return "server";
    }

    public static String getWorldName(Minecraft mc) {
        String serverName = null;
        if (mc.hasSingleplayerServer()) {
            return mc.getSingleplayerServer().f_129744_.getLevelId();
        } else {
            serverName = getServerName();
            if (serverName == null) {
                return "offline";
            } else {
                if (Strings.isNullOrEmpty(serverName.trim())) {
                    serverName = "unnamed";
                }
                return serverName.trim();
            }
        }
    }

    public static List<WorldData.DimensionProvider> getDimensionProviders() {
        return getDimensionProviders(Lists.newArrayList());
    }

    public static List<WorldData.DimensionProvider> getDimensionProviders(List<String> requiredDimensionList) {
        try {
            HashSet<String> requiredDims = new HashSet(requiredDimensionList);
            HashMap<String, WorldData.DimensionProvider> dimProviders = new HashMap();
            org.apache.logging.log4j.Level logLevel = org.apache.logging.log4j.Level.DEBUG;
            Journeymap.getLogger().log(logLevel, String.format("Required dimensions from waypoints: %s", requiredDimensionList));
            Minecraft mc = Minecraft.getInstance();
            ResourceKey<Level> dimension = DimensionHelper.getDimension(mc.player);
            String dimId = DimensionHelper.getDimKeyName(dimension);
            WorldData.DimensionProvider playerDimProvider = new WorldData.WrappedProvider(dimension);
            dimProviders.put(dimId, playerDimProvider);
            requiredDims.remove(dimId);
            Journeymap.getLogger().log(logLevel, String.format("Using player's provider for dim %s: %s", dimId, getSafeDimensionName(playerDimProvider)));
            for (ResourceKey<Level> dim : DimensionHelper.getClientDimList()) {
                try {
                    WorldData.DimensionProvider dimProvider = new WorldData.WrappedProvider(dim);
                    dimProviders.put(DimensionHelper.getDimKeyName(dim), dimProvider);
                    Journeymap.getLogger().log(logLevel, String.format("DimensionManager.getProvider(%s): %s", dim, getSafeDimensionName(dimProvider)));
                } catch (Throwable var11) {
                    JMLogger.throwLogOnce(String.format("Couldn't DimensionManager.getProvider(%s) because of error: %s", dim, var11), var11);
                }
            }
            requiredDims.removeAll(dimProviders.keySet());
            for (String dim : requiredDims) {
                if (!dimProviders.containsKey(dim)) {
                    dimProviders.put(dim, new WorldData.DummyProvider(DimensionHelper.getWorldKeyForName(dim)));
                    Journeymap.getLogger().warn(String.format("Used DummyProvider for required dim: %s", dim));
                }
            }
            ArrayList<WorldData.DimensionProvider> providerList = new ArrayList(dimProviders.values());
            Collections.sort(providerList, new Comparator<WorldData.DimensionProvider>() {

                public int compare(WorldData.DimensionProvider o1, WorldData.DimensionProvider o2) {
                    return String.valueOf(o1.getDimensionId()).compareTo(o2.getDimensionId());
                }
            });
            return providerList;
        } catch (Throwable var12) {
            Journeymap.getLogger().error("Unexpected error in WorldData.getDimensionProviders(): ", var12);
            return Collections.emptyList();
        }
    }

    public static String getSafeDimensionName(WorldData.DimensionProvider dimensionProvider) {
        if (dimensionProvider != null && dimensionProvider.getDimensionId() != null) {
            try {
                return dimensionProvider.getDimensionId();
            } catch (Exception var3) {
                Minecraft mc = Minecraft.getInstance();
                return Constants.getString("jm.common.dimension", DimensionHelper.getDimName(mc.level));
            }
        } else {
            return null;
        }
    }

    public static String getDimension() {
        Player player = Minecraft.getInstance().player;
        String dim = DimensionHelper.getDimName(player);
        String dimName = getSafeDimensionName(new WorldData.WrappedProvider(Minecraft.getInstance().player.m_9236_().dimension()));
        return dimName + " (" + dim + ")";
    }

    public WorldData load(Class aClass) throws Exception {
        Minecraft mc = Minecraft.getInstance();
        LevelData worldInfo = mc.level.getLevelData();
        IntegratedServer server = mc.getSingleplayerServer();
        boolean multiplayer = server == null || server.isPublished();
        this.name = getWorldName(mc);
        this.dimension = DimensionHelper.getDimKeyName(mc.level);
        this.hardcore = worldInfo.isHardcore();
        this.singlePlayer = !multiplayer;
        this.time = mc.level.m_46468_() % 24000L;
        this.features = FeatureManager.getInstance().getAllowedFeatures();
        this.mod_name = JourneymapClient.MOD_NAME;
        this.jm_version = Journeymap.JM_VERSION.toString();
        this.latest_journeymap_version = VersionCheck.getVersionAvailable();
        this.mc_version = (String) DownloadedPackSource.getDownloadHeaders().get("X-Minecraft-Version");
        return this;
    }

    public static String getLightLevel() {
        BlockPos blockpos = Minecraft.getInstance().player.m_20183_();
        Level world = Minecraft.getInstance().level;
        LevelChunk chunk = world.getChunkAt(blockpos);
        int light = chunk.getLevel().getLightEngine().getRawBrightness(blockpos, 0);
        int lightSky = world.m_45517_(LightLayer.SKY, blockpos);
        int lightBlock = world.m_45517_(LightLayer.BLOCK, blockpos);
        return String.format("Light: %s (%s sky, %s block)", light, lightSky, lightBlock);
    }

    public static String getRegion() {
        BlockPos blockpos = Minecraft.getInstance().player.m_20183_();
        LevelChunk chunk = Minecraft.getInstance().level.m_46745_(blockpos);
        RegionCoord regionCoord = RegionCoord.fromChunkPos(null, MapType.none(), chunk.m_7697_().x, chunk.m_7697_().z);
        return "Region: x:" + regionCoord.regionX + " z:" + regionCoord.regionZ;
    }

    public static String getRealGameTime() {
        String format = JourneymapClient.getInstance().getActiveMiniMapProperties().gameTimeRealFormat.get();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        Minecraft minecraft = Minecraft.getInstance();
        long time = minecraft.level.m_46468_();
        long hour = (time / 1000L + 6L) % 24L;
        long minute = time % 1000L * 60L / 1000L;
        double ticks = (double) time - Math.floor((double) time / 16.666666666666668) * 16.666666666666668;
        long seconds = (long) Math.floor(ticks / 0.2777777777777778);
        try {
            String timeString = String.format(Locale.ENGLISH, "%02d:%02d:%02d", Math.max(0L, hour), Math.max(0L, minute), Math.max(0L, seconds));
            String formattedTime = LocalTime.parse(timeString).format(dtf);
            return formattedTime + " " + getContextTime(time % 24000L);
        } catch (Exception var16) {
            Journeymap.getLogger().error("Unable to calculate time for GameTimeReal time:{}, hour:{}, minute:{}, seconds:{}, ticks:{}", time, hour, minute, ticks, seconds, var16);
            return "00e:00e:00e";
        }
    }

    public static String getMoonPhase() {
        int phase = Minecraft.getInstance().level.m_46941_();
        long worldTime = Minecraft.getInstance().level.m_46468_() % 24000L;
        if (isDay(worldTime)) {
            phase = 10;
        }
        return WorldData.MoonPhase.fromPhase(phase);
    }

    public static String getSystemTime() {
        DateFormat timeFormat = new SimpleDateFormat(JourneymapClient.getInstance().getActiveMiniMapProperties().systemTimeRealFormat.get());
        return timeFormat.format(new Date());
    }

    public static String getGameTime() {
        if (Minecraft.getInstance().level != null) {
            long worldTime = Minecraft.getInstance().level.m_46468_() % 24000L;
            long allSecs = worldTime / 20L;
            return String.format("%02d:%02d %s", (long) Math.floor((double) (allSecs / 60L)), (long) Math.ceil((double) (allSecs % 60L)), getContextTime(worldTime));
        } else {
            return "";
        }
    }

    private static String getContextTime(long worldTime) {
        if (worldTime < 12000L) {
            return DAYTIME;
        } else if (worldTime < 13800L) {
            return SUNSET;
        } else {
            return worldTime < 22200L ? NIGHT : SUNRISE;
        }
    }

    public static boolean isDay(long worldTime) {
        return worldTime % 24000L < 13800L;
    }

    public static boolean isNight(long worldTime) {
        return worldTime % 24000L >= 13800L;
    }

    public long getTTL() {
        return 1000L;
    }

    public interface DimensionProvider {

        String getDimensionId();

        ResourceKey<Level> getDimension();

        String getName();
    }

    static class DummyProvider implements WorldData.DimensionProvider {

        final ResourceKey<Level> dim;

        DummyProvider(ResourceKey<Level> dim) {
            this.dim = dim;
        }

        @Override
        public String getDimensionId() {
            return DimensionHelper.getDimKeyName(this.dim);
        }

        @Override
        public ResourceKey<Level> getDimension() {
            return null;
        }

        @Override
        public String getName() {
            return "Dimension " + DimensionHelper.getDimName(this.dim);
        }
    }

    static enum MoonPhase {

        DAY_TIME(10, "jm.theme.labelsource.moonphase.day"),
        FULL_MOON(0, "jm.theme.labelsource.moonphase.full_moon"),
        WANING_GIBBOUS(1, "jm.theme.labelsource.moonphase.waning_gibbous"),
        THIRD_QUARTER(2, "jm.theme.labelsource.moonphase.third_quarter"),
        WANING_CRESCENT(3, "jm.theme.labelsource.moonphase.waning_crescent"),
        NEW_MOON(4, "jm.theme.labelsource.moonphase.new_moon"),
        WAXING_CRESCENT(5, "jm.theme.labelsource.moonphase.waxing_crescent"),
        FIRST_QUARTER(6, "jm.theme.labelsource.moonphase.first_quarter"),
        WAXING_GIBBOUS(7, "jm.theme.labelsource.moonphase.waxing_gibbous");

        final int phase;

        final String key;

        static final Map<Integer, String> PHASE_MAP = (Map<Integer, String>) Stream.of(values()).collect(Collectors.toMap(e -> e.phase, e -> e.key));

        private MoonPhase(int phase, String key) {
            this.phase = phase;
            this.key = key;
        }

        public static String fromPhase(int phase) {
            String phaseName = Constants.getString((String) PHASE_MAP.get(phase));
            String moonPhase = Constants.getString("jm.theme.labelsource.moonphase.pre");
            return moonPhase + phaseName;
        }
    }

    public static class WrappedProvider implements WorldData.DimensionProvider {

        ResourceKey<Level> dimension;

        public WrappedProvider(ResourceKey<Level> dimension) {
            this.dimension = dimension;
        }

        @Override
        public String getDimensionId() {
            return DimensionHelper.getDimKeyName(this.dimension);
        }

        @Override
        public ResourceKey<Level> getDimension() {
            return this.dimension;
        }

        @Override
        public String getName() {
            String dim = DimensionHelper.getDimName(this.dimension).replace("_", " ");
            if (dim.length() > 25) {
                String pre = dim.substring(0, 9);
                String suffix = dim.substring(dim.length() - 5, dim.length() - 1);
                dim = pre + " ... " + suffix;
            }
            return WordUtils.capitalize(dim);
        }
    }
}