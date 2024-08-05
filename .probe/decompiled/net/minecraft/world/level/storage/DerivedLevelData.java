package net.minecraft.world.level.storage;

import java.util.UUID;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.timers.TimerQueue;

public class DerivedLevelData implements ServerLevelData {

    private final WorldData worldData;

    private final ServerLevelData wrapped;

    public DerivedLevelData(WorldData worldData0, ServerLevelData serverLevelData1) {
        this.worldData = worldData0;
        this.wrapped = serverLevelData1;
    }

    @Override
    public int getXSpawn() {
        return this.wrapped.m_6789_();
    }

    @Override
    public int getYSpawn() {
        return this.wrapped.m_6527_();
    }

    @Override
    public int getZSpawn() {
        return this.wrapped.m_6526_();
    }

    @Override
    public float getSpawnAngle() {
        return this.wrapped.m_6790_();
    }

    @Override
    public long getGameTime() {
        return this.wrapped.m_6793_();
    }

    @Override
    public long getDayTime() {
        return this.wrapped.m_6792_();
    }

    @Override
    public String getLevelName() {
        return this.worldData.getLevelName();
    }

    @Override
    public int getClearWeatherTime() {
        return this.wrapped.getClearWeatherTime();
    }

    @Override
    public void setClearWeatherTime(int int0) {
    }

    @Override
    public boolean isThundering() {
        return this.wrapped.m_6534_();
    }

    @Override
    public int getThunderTime() {
        return this.wrapped.getThunderTime();
    }

    @Override
    public boolean isRaining() {
        return this.wrapped.m_6533_();
    }

    @Override
    public int getRainTime() {
        return this.wrapped.getRainTime();
    }

    @Override
    public GameType getGameType() {
        return this.worldData.getGameType();
    }

    @Override
    public void setXSpawn(int int0) {
    }

    @Override
    public void setYSpawn(int int0) {
    }

    @Override
    public void setZSpawn(int int0) {
    }

    @Override
    public void setSpawnAngle(float float0) {
    }

    @Override
    public void setGameTime(long long0) {
    }

    @Override
    public void setDayTime(long long0) {
    }

    @Override
    public void setSpawn(BlockPos blockPos0, float float1) {
    }

    @Override
    public void setThundering(boolean boolean0) {
    }

    @Override
    public void setThunderTime(int int0) {
    }

    @Override
    public void setRaining(boolean boolean0) {
    }

    @Override
    public void setRainTime(int int0) {
    }

    @Override
    public void setGameType(GameType gameType0) {
    }

    @Override
    public boolean isHardcore() {
        return this.worldData.isHardcore();
    }

    @Override
    public boolean getAllowCommands() {
        return this.worldData.getAllowCommands();
    }

    @Override
    public boolean isInitialized() {
        return this.wrapped.isInitialized();
    }

    @Override
    public void setInitialized(boolean boolean0) {
    }

    @Override
    public GameRules getGameRules() {
        return this.worldData.getGameRules();
    }

    @Override
    public WorldBorder.Settings getWorldBorder() {
        return this.wrapped.getWorldBorder();
    }

    @Override
    public void setWorldBorder(WorldBorder.Settings worldBorderSettings0) {
    }

    @Override
    public Difficulty getDifficulty() {
        return this.worldData.getDifficulty();
    }

    @Override
    public boolean isDifficultyLocked() {
        return this.worldData.isDifficultyLocked();
    }

    @Override
    public TimerQueue<MinecraftServer> getScheduledEvents() {
        return this.wrapped.getScheduledEvents();
    }

    @Override
    public int getWanderingTraderSpawnDelay() {
        return 0;
    }

    @Override
    public void setWanderingTraderSpawnDelay(int int0) {
    }

    @Override
    public int getWanderingTraderSpawnChance() {
        return 0;
    }

    @Override
    public void setWanderingTraderSpawnChance(int int0) {
    }

    @Override
    public UUID getWanderingTraderId() {
        return null;
    }

    @Override
    public void setWanderingTraderId(UUID uUID0) {
    }

    @Override
    public void fillCrashReportCategory(CrashReportCategory crashReportCategory0, LevelHeightAccessor levelHeightAccessor1) {
        crashReportCategory0.setDetail("Derived", true);
        this.wrapped.fillCrashReportCategory(crashReportCategory0, levelHeightAccessor1);
    }
}