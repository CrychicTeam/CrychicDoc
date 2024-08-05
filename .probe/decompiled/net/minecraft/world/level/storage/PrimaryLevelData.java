package net.minecraft.world.level.storage;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.CrashReportCategory;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.timers.TimerCallbacks;
import net.minecraft.world.level.timers.TimerQueue;
import org.slf4j.Logger;

public class PrimaryLevelData implements ServerLevelData, WorldData {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected static final String PLAYER = "Player";

    protected static final String WORLD_GEN_SETTINGS = "WorldGenSettings";

    private LevelSettings settings;

    private final WorldOptions worldOptions;

    private final PrimaryLevelData.SpecialWorldProperty specialWorldProperty;

    private final Lifecycle worldGenSettingsLifecycle;

    private int xSpawn;

    private int ySpawn;

    private int zSpawn;

    private float spawnAngle;

    private long gameTime;

    private long dayTime;

    @Nullable
    private final DataFixer fixerUpper;

    private final int playerDataVersion;

    private boolean upgradedPlayerTag;

    @Nullable
    private CompoundTag loadedPlayerTag;

    private final int version;

    private int clearWeatherTime;

    private boolean raining;

    private int rainTime;

    private boolean thundering;

    private int thunderTime;

    private boolean initialized;

    private boolean difficultyLocked;

    private WorldBorder.Settings worldBorder;

    private EndDragonFight.Data endDragonFightData;

    @Nullable
    private CompoundTag customBossEvents;

    private int wanderingTraderSpawnDelay;

    private int wanderingTraderSpawnChance;

    @Nullable
    private UUID wanderingTraderId;

    private final Set<String> knownServerBrands;

    private boolean wasModded;

    private final Set<String> removedFeatureFlags;

    private final TimerQueue<MinecraftServer> scheduledEvents;

    private PrimaryLevelData(@Nullable DataFixer dataFixer0, int int1, @Nullable CompoundTag compoundTag2, boolean boolean3, int int4, int int5, int int6, float float7, long long8, long long9, int int10, int int11, int int12, boolean boolean13, int int14, boolean boolean15, boolean boolean16, boolean boolean17, WorldBorder.Settings worldBorderSettings18, int int19, int int20, @Nullable UUID uUID21, Set<String> setString22, Set<String> setString23, TimerQueue<MinecraftServer> timerQueueMinecraftServer24, @Nullable CompoundTag compoundTag25, EndDragonFight.Data endDragonFightData26, LevelSettings levelSettings27, WorldOptions worldOptions28, PrimaryLevelData.SpecialWorldProperty primaryLevelDataSpecialWorldProperty29, Lifecycle lifecycle30) {
        this.fixerUpper = dataFixer0;
        this.wasModded = boolean3;
        this.xSpawn = int4;
        this.ySpawn = int5;
        this.zSpawn = int6;
        this.spawnAngle = float7;
        this.gameTime = long8;
        this.dayTime = long9;
        this.version = int10;
        this.clearWeatherTime = int11;
        this.rainTime = int12;
        this.raining = boolean13;
        this.thunderTime = int14;
        this.thundering = boolean15;
        this.initialized = boolean16;
        this.difficultyLocked = boolean17;
        this.worldBorder = worldBorderSettings18;
        this.wanderingTraderSpawnDelay = int19;
        this.wanderingTraderSpawnChance = int20;
        this.wanderingTraderId = uUID21;
        this.knownServerBrands = setString22;
        this.removedFeatureFlags = setString23;
        this.loadedPlayerTag = compoundTag2;
        this.playerDataVersion = int1;
        this.scheduledEvents = timerQueueMinecraftServer24;
        this.customBossEvents = compoundTag25;
        this.endDragonFightData = endDragonFightData26;
        this.settings = levelSettings27;
        this.worldOptions = worldOptions28;
        this.specialWorldProperty = primaryLevelDataSpecialWorldProperty29;
        this.worldGenSettingsLifecycle = lifecycle30;
    }

    public PrimaryLevelData(LevelSettings levelSettings0, WorldOptions worldOptions1, PrimaryLevelData.SpecialWorldProperty primaryLevelDataSpecialWorldProperty2, Lifecycle lifecycle3) {
        this(null, SharedConstants.getCurrentVersion().getDataVersion().getVersion(), null, false, 0, 0, 0, 0.0F, 0L, 0L, 19133, 0, 0, false, 0, false, false, false, WorldBorder.DEFAULT_SETTINGS, 0, 0, null, Sets.newLinkedHashSet(), new HashSet(), new TimerQueue<>(TimerCallbacks.SERVER_CALLBACKS), null, EndDragonFight.Data.DEFAULT, levelSettings0.copy(), worldOptions1, primaryLevelDataSpecialWorldProperty2, lifecycle3);
    }

    public static <T> PrimaryLevelData parse(Dynamic<T> dynamicT0, DataFixer dataFixer1, int int2, @Nullable CompoundTag compoundTag3, LevelSettings levelSettings4, LevelVersion levelVersion5, PrimaryLevelData.SpecialWorldProperty primaryLevelDataSpecialWorldProperty6, WorldOptions worldOptions7, Lifecycle lifecycle8) {
        long $$9 = dynamicT0.get("Time").asLong(0L);
        return new PrimaryLevelData(dataFixer1, int2, compoundTag3, dynamicT0.get("WasModded").asBoolean(false), dynamicT0.get("SpawnX").asInt(0), dynamicT0.get("SpawnY").asInt(0), dynamicT0.get("SpawnZ").asInt(0), dynamicT0.get("SpawnAngle").asFloat(0.0F), $$9, dynamicT0.get("DayTime").asLong($$9), levelVersion5.levelDataVersion(), dynamicT0.get("clearWeatherTime").asInt(0), dynamicT0.get("rainTime").asInt(0), dynamicT0.get("raining").asBoolean(false), dynamicT0.get("thunderTime").asInt(0), dynamicT0.get("thundering").asBoolean(false), dynamicT0.get("initialized").asBoolean(true), dynamicT0.get("DifficultyLocked").asBoolean(false), WorldBorder.Settings.read(dynamicT0, WorldBorder.DEFAULT_SETTINGS), dynamicT0.get("WanderingTraderSpawnDelay").asInt(0), dynamicT0.get("WanderingTraderSpawnChance").asInt(0), (UUID) dynamicT0.get("WanderingTraderId").read(UUIDUtil.CODEC).result().orElse(null), (Set<String>) dynamicT0.get("ServerBrands").asStream().flatMap(p_78529_ -> p_78529_.asString().result().stream()).collect(Collectors.toCollection(Sets::newLinkedHashSet)), (Set<String>) dynamicT0.get("removed_features").asStream().flatMap(p_277335_ -> p_277335_.asString().result().stream()).collect(Collectors.toSet()), new TimerQueue<>(TimerCallbacks.SERVER_CALLBACKS, dynamicT0.get("ScheduledEvents").asStream()), (CompoundTag) dynamicT0.get("CustomBossEvents").orElseEmptyMap().getValue(), (EndDragonFight.Data) dynamicT0.get("DragonFight").read(EndDragonFight.Data.CODEC).resultOrPartial(LOGGER::error).orElse(EndDragonFight.Data.DEFAULT), levelSettings4, worldOptions7, primaryLevelDataSpecialWorldProperty6, lifecycle8);
    }

    @Override
    public CompoundTag createTag(RegistryAccess registryAccess0, @Nullable CompoundTag compoundTag1) {
        this.updatePlayerTag();
        if (compoundTag1 == null) {
            compoundTag1 = this.loadedPlayerTag;
        }
        CompoundTag $$2 = new CompoundTag();
        this.setTagData(registryAccess0, $$2, compoundTag1);
        return $$2;
    }

    private void setTagData(RegistryAccess registryAccess0, CompoundTag compoundTag1, @Nullable CompoundTag compoundTag2) {
        compoundTag1.put("ServerBrands", stringCollectionToTag(this.knownServerBrands));
        compoundTag1.putBoolean("WasModded", this.wasModded);
        if (!this.removedFeatureFlags.isEmpty()) {
            compoundTag1.put("removed_features", stringCollectionToTag(this.removedFeatureFlags));
        }
        CompoundTag $$3 = new CompoundTag();
        $$3.putString("Name", SharedConstants.getCurrentVersion().getName());
        $$3.putInt("Id", SharedConstants.getCurrentVersion().getDataVersion().getVersion());
        $$3.putBoolean("Snapshot", !SharedConstants.getCurrentVersion().isStable());
        $$3.putString("Series", SharedConstants.getCurrentVersion().getDataVersion().getSeries());
        compoundTag1.put("Version", $$3);
        NbtUtils.addCurrentDataVersion(compoundTag1);
        DynamicOps<Tag> $$4 = RegistryOps.create(NbtOps.INSTANCE, registryAccess0);
        WorldGenSettings.encode($$4, this.worldOptions, registryAccess0).resultOrPartial(Util.prefix("WorldGenSettings: ", LOGGER::error)).ifPresent(p_78574_ -> compoundTag1.put("WorldGenSettings", p_78574_));
        compoundTag1.putInt("GameType", this.settings.gameType().getId());
        compoundTag1.putInt("SpawnX", this.xSpawn);
        compoundTag1.putInt("SpawnY", this.ySpawn);
        compoundTag1.putInt("SpawnZ", this.zSpawn);
        compoundTag1.putFloat("SpawnAngle", this.spawnAngle);
        compoundTag1.putLong("Time", this.gameTime);
        compoundTag1.putLong("DayTime", this.dayTime);
        compoundTag1.putLong("LastPlayed", Util.getEpochMillis());
        compoundTag1.putString("LevelName", this.settings.levelName());
        compoundTag1.putInt("version", 19133);
        compoundTag1.putInt("clearWeatherTime", this.clearWeatherTime);
        compoundTag1.putInt("rainTime", this.rainTime);
        compoundTag1.putBoolean("raining", this.raining);
        compoundTag1.putInt("thunderTime", this.thunderTime);
        compoundTag1.putBoolean("thundering", this.thundering);
        compoundTag1.putBoolean("hardcore", this.settings.hardcore());
        compoundTag1.putBoolean("allowCommands", this.settings.allowCommands());
        compoundTag1.putBoolean("initialized", this.initialized);
        this.worldBorder.write(compoundTag1);
        compoundTag1.putByte("Difficulty", (byte) this.settings.difficulty().getId());
        compoundTag1.putBoolean("DifficultyLocked", this.difficultyLocked);
        compoundTag1.put("GameRules", this.settings.gameRules().createTag());
        compoundTag1.put("DragonFight", Util.getOrThrow(EndDragonFight.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.endDragonFightData), IllegalStateException::new));
        if (compoundTag2 != null) {
            compoundTag1.put("Player", compoundTag2);
        }
        DataResult<Tag> $$5 = WorldDataConfiguration.CODEC.encodeStart(NbtOps.INSTANCE, this.settings.getDataConfiguration());
        $$5.get().ifLeft(p_248505_ -> compoundTag1.merge((CompoundTag) p_248505_)).ifRight(p_248506_ -> LOGGER.warn("Failed to encode configuration {}", p_248506_.message()));
        if (this.customBossEvents != null) {
            compoundTag1.put("CustomBossEvents", this.customBossEvents);
        }
        compoundTag1.put("ScheduledEvents", this.scheduledEvents.store());
        compoundTag1.putInt("WanderingTraderSpawnDelay", this.wanderingTraderSpawnDelay);
        compoundTag1.putInt("WanderingTraderSpawnChance", this.wanderingTraderSpawnChance);
        if (this.wanderingTraderId != null) {
            compoundTag1.putUUID("WanderingTraderId", this.wanderingTraderId);
        }
    }

    private static ListTag stringCollectionToTag(Set<String> setString0) {
        ListTag $$1 = new ListTag();
        setString0.stream().map(StringTag::m_129297_).forEach($$1::add);
        return $$1;
    }

    @Override
    public int getXSpawn() {
        return this.xSpawn;
    }

    @Override
    public int getYSpawn() {
        return this.ySpawn;
    }

    @Override
    public int getZSpawn() {
        return this.zSpawn;
    }

    @Override
    public float getSpawnAngle() {
        return this.spawnAngle;
    }

    @Override
    public long getGameTime() {
        return this.gameTime;
    }

    @Override
    public long getDayTime() {
        return this.dayTime;
    }

    private void updatePlayerTag() {
        if (!this.upgradedPlayerTag && this.loadedPlayerTag != null) {
            if (this.playerDataVersion < SharedConstants.getCurrentVersion().getDataVersion().getVersion()) {
                if (this.fixerUpper == null) {
                    throw (NullPointerException) Util.pauseInIde(new NullPointerException("Fixer Upper not set inside LevelData, and the player tag is not upgraded."));
                }
                this.loadedPlayerTag = DataFixTypes.PLAYER.updateToCurrentVersion(this.fixerUpper, this.loadedPlayerTag, this.playerDataVersion);
            }
            this.upgradedPlayerTag = true;
        }
    }

    @Override
    public CompoundTag getLoadedPlayerTag() {
        this.updatePlayerTag();
        return this.loadedPlayerTag;
    }

    @Override
    public void setXSpawn(int int0) {
        this.xSpawn = int0;
    }

    @Override
    public void setYSpawn(int int0) {
        this.ySpawn = int0;
    }

    @Override
    public void setZSpawn(int int0) {
        this.zSpawn = int0;
    }

    @Override
    public void setSpawnAngle(float float0) {
        this.spawnAngle = float0;
    }

    @Override
    public void setGameTime(long long0) {
        this.gameTime = long0;
    }

    @Override
    public void setDayTime(long long0) {
        this.dayTime = long0;
    }

    @Override
    public void setSpawn(BlockPos blockPos0, float float1) {
        this.xSpawn = blockPos0.m_123341_();
        this.ySpawn = blockPos0.m_123342_();
        this.zSpawn = blockPos0.m_123343_();
        this.spawnAngle = float1;
    }

    @Override
    public String getLevelName() {
        return this.settings.levelName();
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public int getClearWeatherTime() {
        return this.clearWeatherTime;
    }

    @Override
    public void setClearWeatherTime(int int0) {
        this.clearWeatherTime = int0;
    }

    @Override
    public boolean isThundering() {
        return this.thundering;
    }

    @Override
    public void setThundering(boolean boolean0) {
        this.thundering = boolean0;
    }

    @Override
    public int getThunderTime() {
        return this.thunderTime;
    }

    @Override
    public void setThunderTime(int int0) {
        this.thunderTime = int0;
    }

    @Override
    public boolean isRaining() {
        return this.raining;
    }

    @Override
    public void setRaining(boolean boolean0) {
        this.raining = boolean0;
    }

    @Override
    public int getRainTime() {
        return this.rainTime;
    }

    @Override
    public void setRainTime(int int0) {
        this.rainTime = int0;
    }

    @Override
    public GameType getGameType() {
        return this.settings.gameType();
    }

    @Override
    public void setGameType(GameType gameType0) {
        this.settings = this.settings.withGameType(gameType0);
    }

    @Override
    public boolean isHardcore() {
        return this.settings.hardcore();
    }

    @Override
    public boolean getAllowCommands() {
        return this.settings.allowCommands();
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    @Override
    public void setInitialized(boolean boolean0) {
        this.initialized = boolean0;
    }

    @Override
    public GameRules getGameRules() {
        return this.settings.gameRules();
    }

    @Override
    public WorldBorder.Settings getWorldBorder() {
        return this.worldBorder;
    }

    @Override
    public void setWorldBorder(WorldBorder.Settings worldBorderSettings0) {
        this.worldBorder = worldBorderSettings0;
    }

    @Override
    public Difficulty getDifficulty() {
        return this.settings.difficulty();
    }

    @Override
    public void setDifficulty(Difficulty difficulty0) {
        this.settings = this.settings.withDifficulty(difficulty0);
    }

    @Override
    public boolean isDifficultyLocked() {
        return this.difficultyLocked;
    }

    @Override
    public void setDifficultyLocked(boolean boolean0) {
        this.difficultyLocked = boolean0;
    }

    @Override
    public TimerQueue<MinecraftServer> getScheduledEvents() {
        return this.scheduledEvents;
    }

    @Override
    public void fillCrashReportCategory(CrashReportCategory crashReportCategory0, LevelHeightAccessor levelHeightAccessor1) {
        ServerLevelData.super.fillCrashReportCategory(crashReportCategory0, levelHeightAccessor1);
        WorldData.super.fillCrashReportCategory(crashReportCategory0);
    }

    @Override
    public WorldOptions worldGenOptions() {
        return this.worldOptions;
    }

    @Override
    public boolean isFlatWorld() {
        return this.specialWorldProperty == PrimaryLevelData.SpecialWorldProperty.FLAT;
    }

    @Override
    public boolean isDebugWorld() {
        return this.specialWorldProperty == PrimaryLevelData.SpecialWorldProperty.DEBUG;
    }

    @Override
    public Lifecycle worldGenSettingsLifecycle() {
        return this.worldGenSettingsLifecycle;
    }

    @Override
    public EndDragonFight.Data endDragonFightData() {
        return this.endDragonFightData;
    }

    @Override
    public void setEndDragonFightData(EndDragonFight.Data endDragonFightData0) {
        this.endDragonFightData = endDragonFightData0;
    }

    @Override
    public WorldDataConfiguration getDataConfiguration() {
        return this.settings.getDataConfiguration();
    }

    @Override
    public void setDataConfiguration(WorldDataConfiguration worldDataConfiguration0) {
        this.settings = this.settings.withDataConfiguration(worldDataConfiguration0);
    }

    @Nullable
    @Override
    public CompoundTag getCustomBossEvents() {
        return this.customBossEvents;
    }

    @Override
    public void setCustomBossEvents(@Nullable CompoundTag compoundTag0) {
        this.customBossEvents = compoundTag0;
    }

    @Override
    public int getWanderingTraderSpawnDelay() {
        return this.wanderingTraderSpawnDelay;
    }

    @Override
    public void setWanderingTraderSpawnDelay(int int0) {
        this.wanderingTraderSpawnDelay = int0;
    }

    @Override
    public int getWanderingTraderSpawnChance() {
        return this.wanderingTraderSpawnChance;
    }

    @Override
    public void setWanderingTraderSpawnChance(int int0) {
        this.wanderingTraderSpawnChance = int0;
    }

    @Nullable
    @Override
    public UUID getWanderingTraderId() {
        return this.wanderingTraderId;
    }

    @Override
    public void setWanderingTraderId(UUID uUID0) {
        this.wanderingTraderId = uUID0;
    }

    @Override
    public void setModdedInfo(String string0, boolean boolean1) {
        this.knownServerBrands.add(string0);
        this.wasModded |= boolean1;
    }

    @Override
    public boolean wasModded() {
        return this.wasModded;
    }

    @Override
    public Set<String> getKnownServerBrands() {
        return ImmutableSet.copyOf(this.knownServerBrands);
    }

    @Override
    public Set<String> getRemovedFeatureFlags() {
        return Set.copyOf(this.removedFeatureFlags);
    }

    @Override
    public ServerLevelData overworldData() {
        return this;
    }

    @Override
    public LevelSettings getLevelSettings() {
        return this.settings.copy();
    }

    @Deprecated
    public static enum SpecialWorldProperty {

        NONE, FLAT, DEBUG
    }
}