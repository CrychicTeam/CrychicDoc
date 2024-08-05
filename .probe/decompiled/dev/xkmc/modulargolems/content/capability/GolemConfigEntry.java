package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.modulargolems.init.ModularGolems;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class GolemConfigEntry {

    public final SyncContainer sync = new SyncContainer();

    private UUID id;

    private int color;

    private Component nameComp;

    @SerialField
    protected String name;

    @SerialField
    public int defaultMode;

    @SerialField
    public boolean summonToPosition;

    @SerialField
    public boolean locked;

    @SerialField
    public PickupFilterConfig pickupFilter = new PickupFilterConfig();

    @SerialField
    public TargetFilterConfig targetFilter = new TargetFilterConfig();

    @SerialField
    public SquadConfig squadConfig = new SquadConfig();

    @SerialField
    public PathConfig pathConfig = new PathConfig();

    public static GolemConfigEntry getDefault(UUID id, int color, Component name) {
        return new GolemConfigEntry(name).init(id, color);
    }

    @Deprecated
    public GolemConfigEntry() {
    }

    private GolemConfigEntry(Component comp) {
        this.nameComp = comp;
        this.name = Component.Serializer.toJson(comp);
        this.targetFilter.initDefault();
    }

    public Component getDisplayName() {
        if (this.nameComp == null) {
            this.nameComp = Component.Serializer.fromJson(this.name);
        }
        if (this.nameComp == null) {
            this.nameComp = Component.literal("Unnamed");
        }
        return this.nameComp;
    }

    public GolemConfigEntry init(UUID id, int color) {
        this.id = id;
        this.color = color;
        return this;
    }

    public void heartBeat(ServerLevel level, ServerPlayer player) {
        if (this.sync.heartBeat(level, player.m_20148_())) {
            ModularGolems.HANDLER.toClientPlayer(new ConfigSyncToClient(this), player);
        }
    }

    public UUID getID() {
        return this.id;
    }

    public int getColor() {
        return this.color;
    }

    public void clientTick(Level level, boolean updated) {
        this.sync.clientTick(this, level, updated);
    }

    public void sync(Level level) {
        if (level instanceof ServerLevel sl) {
            this.sync.sendToAllTracking(sl, new ConfigSyncToClient(this));
        } else {
            ModularGolems.HANDLER.toServer(new ConfigUpdateToServer(level, this));
        }
    }

    public GolemConfigEntry copyFrom(@Nullable GolemConfigEntry entry) {
        if (entry != null) {
            this.sync.clientReplace(entry.sync);
        }
        return this;
    }

    public void setName(Component hoverName, ServerLevel level) {
        this.nameComp = hoverName;
        this.name = Component.Serializer.toJson(hoverName);
        this.sync(level);
    }
}