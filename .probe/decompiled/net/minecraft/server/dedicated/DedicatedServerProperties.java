package net.minecraft.server.dedicated;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.slf4j.Logger;

public class DedicatedServerProperties extends Settings<DedicatedServerProperties> {

    static final Logger LOGGER = LogUtils.getLogger();

    private static final Pattern SHA1 = Pattern.compile("^[a-fA-F0-9]{40}$");

    private static final Splitter COMMA_SPLITTER = Splitter.on(',').trimResults();

    public final boolean onlineMode = this.m_139836_("online-mode", true);

    public final boolean preventProxyConnections = this.m_139836_("prevent-proxy-connections", false);

    public final String serverIp = this.m_139811_("server-ip", "");

    public final boolean spawnAnimals = this.m_139836_("spawn-animals", true);

    public final boolean spawnNpcs = this.m_139836_("spawn-npcs", true);

    public final boolean pvp = this.m_139836_("pvp", true);

    public final boolean allowFlight = this.m_139836_("allow-flight", false);

    public final String motd = this.m_139811_("motd", "A Minecraft Server");

    public final boolean forceGameMode = this.m_139836_("force-gamemode", false);

    public final boolean enforceWhitelist = this.m_139836_("enforce-whitelist", false);

    public final Difficulty difficulty = (Difficulty) this.m_139821_("difficulty", m_139850_(Difficulty::m_19029_, Difficulty::m_19031_), Difficulty::m_19036_, Difficulty.EASY);

    public final GameType gamemode = (GameType) this.m_139821_("gamemode", m_139850_(GameType::m_46393_, GameType::m_46400_), GameType::m_46405_, GameType.SURVIVAL);

    public final String levelName = this.m_139811_("level-name", "world");

    public final int serverPort = this.m_139805_("server-port", 25565);

    @Nullable
    public final Boolean announcePlayerAchievements = this.m_139859_("announce-player-achievements");

    public final boolean enableQuery = this.m_139836_("enable-query", false);

    public final int queryPort = this.m_139805_("query.port", 25565);

    public final boolean enableRcon = this.m_139836_("enable-rcon", false);

    public final int rconPort = this.m_139805_("rcon.port", 25575);

    public final String rconPassword = this.m_139811_("rcon.password", "");

    public final boolean hardcore = this.m_139836_("hardcore", false);

    public final boolean allowNether = this.m_139836_("allow-nether", true);

    public final boolean spawnMonsters = this.m_139836_("spawn-monsters", true);

    public final boolean useNativeTransport = this.m_139836_("use-native-transport", true);

    public final boolean enableCommandBlock = this.m_139836_("enable-command-block", false);

    public final int spawnProtection = this.m_139805_("spawn-protection", 16);

    public final int opPermissionLevel = this.m_139805_("op-permission-level", 4);

    public final int functionPermissionLevel = this.m_139805_("function-permission-level", 2);

    public final long maxTickTime = this.m_139808_("max-tick-time", TimeUnit.MINUTES.toMillis(1L));

    public final int maxChainedNeighborUpdates = this.m_139805_("max-chained-neighbor-updates", 1000000);

    public final int rateLimitPacketsPerSecond = this.m_139805_("rate-limit", 0);

    public final int viewDistance = this.m_139805_("view-distance", 10);

    public final int simulationDistance = this.m_139805_("simulation-distance", 10);

    public final int maxPlayers = this.m_139805_("max-players", 20);

    public final int networkCompressionThreshold = this.m_139805_("network-compression-threshold", 256);

    public final boolean broadcastRconToOps = this.m_139836_("broadcast-rcon-to-ops", true);

    public final boolean broadcastConsoleToOps = this.m_139836_("broadcast-console-to-ops", true);

    public final int maxWorldSize = this.m_139832_("max-world-size", p_139771_ -> Mth.clamp(p_139771_, 1, 29999984), 29999984);

    public final boolean syncChunkWrites = this.m_139836_("sync-chunk-writes", true);

    public final boolean enableJmxMonitoring = this.m_139836_("enable-jmx-monitoring", false);

    public final boolean enableStatus = this.m_139836_("enable-status", true);

    public final boolean hideOnlinePlayers = this.m_139836_("hide-online-players", false);

    public final int entityBroadcastRangePercentage = this.m_139832_("entity-broadcast-range-percentage", p_139769_ -> Mth.clamp(p_139769_, 10, 1000), 100);

    public final String textFilteringConfig = this.m_139811_("text-filtering-config", "");

    public final Optional<MinecraftServer.ServerResourcePackInfo> serverResourcePackInfo;

    public final DataPackConfig initialDataPackConfiguration;

    public final Settings<DedicatedServerProperties>.MutableValue<Integer> playerIdleTimeout = this.m_139861_("player-idle-timeout", 0);

    public final Settings<DedicatedServerProperties>.MutableValue<Boolean> whiteList = this.m_139873_("white-list", false);

    public final boolean enforceSecureProfile = this.m_139836_("enforce-secure-profile", true);

    private final DedicatedServerProperties.WorldDimensionData worldDimensionData;

    public final WorldOptions worldOptions;

    public DedicatedServerProperties(Properties properties0) {
        super(properties0);
        String $$1 = this.m_139811_("level-seed", "");
        boolean $$2 = this.m_139836_("generate-structures", true);
        long $$3 = WorldOptions.parseSeed($$1).orElse(WorldOptions.randomSeed());
        this.worldOptions = new WorldOptions($$3, $$2, false);
        this.worldDimensionData = new DedicatedServerProperties.WorldDimensionData((JsonObject) this.m_139817_("generator-settings", p_211543_ -> GsonHelper.parse(!p_211543_.isEmpty() ? p_211543_ : "{}"), new JsonObject()), (String) this.m_139817_("level-type", p_211541_ -> p_211541_.toLowerCase(Locale.ROOT), WorldPresets.NORMAL.location().toString()));
        this.serverResourcePackInfo = getServerPackInfo(this.m_139811_("resource-pack", ""), this.m_139811_("resource-pack-sha1", ""), this.m_139803_("resource-pack-hash"), this.m_139836_("require-resource-pack", false), this.m_139811_("resource-pack-prompt", ""));
        this.initialDataPackConfiguration = getDatapackConfig(this.m_139811_("initial-enabled-packs", String.join(",", WorldDataConfiguration.DEFAULT.dataPacks().getEnabled())), this.m_139811_("initial-disabled-packs", String.join(",", WorldDataConfiguration.DEFAULT.dataPacks().getDisabled())));
    }

    public static DedicatedServerProperties fromFile(Path path0) {
        return new DedicatedServerProperties(m_139839_(path0));
    }

    protected DedicatedServerProperties reload(RegistryAccess registryAccess0, Properties properties1) {
        return new DedicatedServerProperties(properties1);
    }

    @Nullable
    private static Component parseResourcePackPrompt(String string0) {
        if (!Strings.isNullOrEmpty(string0)) {
            try {
                return Component.Serializer.fromJson(string0);
            } catch (Exception var2) {
                LOGGER.warn("Failed to parse resource pack prompt '{}'", string0, var2);
            }
        }
        return null;
    }

    private static Optional<MinecraftServer.ServerResourcePackInfo> getServerPackInfo(String string0, String string1, @Nullable String string2, boolean boolean3, String string4) {
        if (string0.isEmpty()) {
            return Optional.empty();
        } else {
            String $$5;
            if (!string1.isEmpty()) {
                $$5 = string1;
                if (!Strings.isNullOrEmpty(string2)) {
                    LOGGER.warn("resource-pack-hash is deprecated and found along side resource-pack-sha1. resource-pack-hash will be ignored.");
                }
            } else if (!Strings.isNullOrEmpty(string2)) {
                LOGGER.warn("resource-pack-hash is deprecated. Please use resource-pack-sha1 instead.");
                $$5 = string2;
            } else {
                $$5 = "";
            }
            if ($$5.isEmpty()) {
                LOGGER.warn("You specified a resource pack without providing a sha1 hash. Pack will be updated on the client only if you change the name of the pack.");
            } else if (!SHA1.matcher($$5).matches()) {
                LOGGER.warn("Invalid sha1 for resource-pack-sha1");
            }
            Component $$8 = parseResourcePackPrompt(string4);
            return Optional.of(new MinecraftServer.ServerResourcePackInfo(string0, $$5, boolean3, $$8));
        }
    }

    private static DataPackConfig getDatapackConfig(String string0, String string1) {
        List<String> $$2 = COMMA_SPLITTER.splitToList(string0);
        List<String> $$3 = COMMA_SPLITTER.splitToList(string1);
        return new DataPackConfig($$2, $$3);
    }

    private static FeatureFlagSet getFeatures(String string0) {
        return FeatureFlags.REGISTRY.fromNames((Iterable<ResourceLocation>) COMMA_SPLITTER.splitToStream(string0).mapMulti((p_248197_, p_248198_) -> {
            ResourceLocation $$2 = ResourceLocation.tryParse(p_248197_);
            if ($$2 == null) {
                LOGGER.warn("Invalid resource location {}, ignoring", p_248197_);
            } else {
                p_248198_.accept($$2);
            }
        }).collect(Collectors.toList()));
    }

    public WorldDimensions createDimensions(RegistryAccess registryAccess0) {
        return this.worldDimensionData.create(registryAccess0);
    }

    static record WorldDimensionData(JsonObject f_244404_, String f_243780_) {

        private final JsonObject generatorSettings;

        private final String levelType;

        private static final Map<String, ResourceKey<WorldPreset>> LEGACY_PRESET_NAMES = Map.of("default", WorldPresets.NORMAL, "largebiomes", WorldPresets.LARGE_BIOMES);

        WorldDimensionData(JsonObject f_244404_, String f_243780_) {
            this.generatorSettings = f_244404_;
            this.levelType = f_243780_;
        }

        public WorldDimensions create(RegistryAccess p_248812_) {
            Registry<WorldPreset> $$1 = p_248812_.registryOrThrow(Registries.WORLD_PRESET);
            Holder.Reference<WorldPreset> $$2 = (Holder.Reference<WorldPreset>) $$1.getHolder(WorldPresets.NORMAL).or(() -> $$1.holders().findAny()).orElseThrow(() -> new IllegalStateException("Invalid datapack contents: can't find default preset"));
            Holder<WorldPreset> $$3 = (Holder<WorldPreset>) Optional.ofNullable(ResourceLocation.tryParse(this.levelType)).map(p_258240_ -> ResourceKey.create(Registries.WORLD_PRESET, p_258240_)).or(() -> Optional.ofNullable((ResourceKey) LEGACY_PRESET_NAMES.get(this.levelType))).flatMap($$1::m_203636_).orElseGet(() -> {
                DedicatedServerProperties.LOGGER.warn("Failed to parse level-type {}, defaulting to {}", this.levelType, $$2.key().location());
                return $$2;
            });
            WorldDimensions $$4 = $$3.value().createWorldDimensions();
            if ($$3.is(WorldPresets.FLAT)) {
                RegistryOps<JsonElement> $$5 = RegistryOps.create(JsonOps.INSTANCE, p_248812_);
                Optional<FlatLevelGeneratorSettings> $$6 = FlatLevelGeneratorSettings.CODEC.parse(new Dynamic($$5, this.generatorSettings())).resultOrPartial(DedicatedServerProperties.LOGGER::error);
                if ($$6.isPresent()) {
                    return $$4.replaceOverworldGenerator(p_248812_, new FlatLevelSource((FlatLevelGeneratorSettings) $$6.get()));
                }
            }
            return $$4;
        }
    }
}