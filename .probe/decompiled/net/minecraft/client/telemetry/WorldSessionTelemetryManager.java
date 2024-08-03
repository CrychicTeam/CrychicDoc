package net.minecraft.client.telemetry;

import java.time.Duration;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.telemetry.events.PerformanceMetricsEvent;
import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.minecraft.client.telemetry.events.WorldLoadTimesEvent;
import net.minecraft.client.telemetry.events.WorldUnloadEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

public class WorldSessionTelemetryManager {

    private final UUID worldSessionId = UUID.randomUUID();

    private final TelemetryEventSender eventSender;

    private final WorldLoadEvent worldLoadEvent;

    private final WorldUnloadEvent worldUnloadEvent = new WorldUnloadEvent();

    private final PerformanceMetricsEvent performanceMetricsEvent;

    private final WorldLoadTimesEvent worldLoadTimesEvent;

    public WorldSessionTelemetryManager(TelemetryEventSender telemetryEventSender0, boolean boolean1, @Nullable Duration duration2, @Nullable String string3) {
        this.worldLoadEvent = new WorldLoadEvent(string3);
        this.performanceMetricsEvent = new PerformanceMetricsEvent();
        this.worldLoadTimesEvent = new WorldLoadTimesEvent(boolean1, duration2);
        this.eventSender = telemetryEventSender0.decorate(p_261981_ -> {
            this.worldLoadEvent.addProperties(p_261981_);
            p_261981_.put(TelemetryProperty.WORLD_SESSION_ID, this.worldSessionId);
        });
    }

    public void tick() {
        this.performanceMetricsEvent.tick(this.eventSender);
    }

    public void onPlayerInfoReceived(GameType gameType0, boolean boolean1) {
        this.worldLoadEvent.setGameMode(gameType0, boolean1);
        this.worldUnloadEvent.onPlayerInfoReceived();
        this.worldSessionStart();
    }

    public void onServerBrandReceived(String string0) {
        this.worldLoadEvent.setServerBrand(string0);
        this.worldSessionStart();
    }

    public void setTime(long long0) {
        this.worldUnloadEvent.setTime(long0);
    }

    public void worldSessionStart() {
        if (this.worldLoadEvent.send(this.eventSender)) {
            this.worldLoadTimesEvent.send(this.eventSender);
            this.performanceMetricsEvent.m_260947_();
        }
    }

    public void onDisconnect() {
        this.worldLoadEvent.send(this.eventSender);
        this.performanceMetricsEvent.m_261217_();
        this.worldUnloadEvent.send(this.eventSender);
    }

    public void onAdvancementDone(Level level0, Advancement advancement1) {
        ResourceLocation $$2 = advancement1.getId();
        if (advancement1.sendsTelemetryEvent() && "minecraft".equals($$2.getNamespace())) {
            long $$3 = level0.getGameTime();
            this.eventSender.send(TelemetryEventType.ADVANCEMENT_MADE, p_286184_ -> {
                p_286184_.put(TelemetryProperty.ADVANCEMENT_ID, $$2.toString());
                p_286184_.put(TelemetryProperty.ADVANCEMENT_GAME_TIME, $$3);
            });
        }
    }
}