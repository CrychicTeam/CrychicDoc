package net.minecraft.client.telemetry.events;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.telemetry.TelemetryEventSender;
import net.minecraft.client.telemetry.TelemetryEventType;
import net.minecraft.client.telemetry.TelemetryProperty;
import net.minecraft.client.telemetry.TelemetryPropertyMap;
import net.minecraft.world.level.GameType;

public class WorldLoadEvent {

    private boolean eventSent;

    @Nullable
    private TelemetryProperty.GameMode gameMode;

    @Nullable
    private String serverBrand;

    @Nullable
    private final String minigameName;

    public WorldLoadEvent(@Nullable String string0) {
        this.minigameName = string0;
    }

    public void addProperties(TelemetryPropertyMap.Builder telemetryPropertyMapBuilder0) {
        if (this.serverBrand != null) {
            telemetryPropertyMapBuilder0.put(TelemetryProperty.SERVER_MODDED, !this.serverBrand.equals("vanilla"));
        }
        telemetryPropertyMapBuilder0.put(TelemetryProperty.SERVER_TYPE, this.getServerType());
    }

    private TelemetryProperty.ServerType getServerType() {
        if (Minecraft.getInstance().isConnectedToRealms()) {
            return TelemetryProperty.ServerType.REALM;
        } else {
            return Minecraft.getInstance().hasSingleplayerServer() ? TelemetryProperty.ServerType.LOCAL : TelemetryProperty.ServerType.OTHER;
        }
    }

    public boolean send(TelemetryEventSender telemetryEventSender0) {
        if (!this.eventSent && this.gameMode != null && this.serverBrand != null) {
            this.eventSent = true;
            telemetryEventSender0.send(TelemetryEventType.WORLD_LOADED, p_286185_ -> {
                p_286185_.put(TelemetryProperty.GAME_MODE, this.gameMode);
                if (this.minigameName != null) {
                    p_286185_.put(TelemetryProperty.REALMS_MAP_CONTENT, this.minigameName);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    public void setGameMode(GameType gameType0, boolean boolean1) {
        this.gameMode = switch(gameType0) {
            case SURVIVAL ->
                boolean1 ? TelemetryProperty.GameMode.HARDCORE : TelemetryProperty.GameMode.SURVIVAL;
            case CREATIVE ->
                TelemetryProperty.GameMode.CREATIVE;
            case ADVENTURE ->
                TelemetryProperty.GameMode.ADVENTURE;
            case SPECTATOR ->
                TelemetryProperty.GameMode.SPECTATOR;
        };
    }

    public void setServerBrand(String string0) {
        this.serverBrand = string0;
    }
}