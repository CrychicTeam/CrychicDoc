package net.minecraft.network.protocol.game;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class ClientboundRespawnPacket implements Packet<ClientGamePacketListener> {

    public static final byte KEEP_ATTRIBUTES = 1;

    public static final byte KEEP_ENTITY_DATA = 2;

    public static final byte KEEP_ALL_DATA = 3;

    private final ResourceKey<DimensionType> dimensionType;

    private final ResourceKey<Level> dimension;

    private final long seed;

    private final GameType playerGameType;

    @Nullable
    private final GameType previousPlayerGameType;

    private final boolean isDebug;

    private final boolean isFlat;

    private final byte dataToKeep;

    private final Optional<GlobalPos> lastDeathLocation;

    private final int portalCooldown;

    public ClientboundRespawnPacket(ResourceKey<DimensionType> resourceKeyDimensionType0, ResourceKey<Level> resourceKeyLevel1, long long2, GameType gameType3, @Nullable GameType gameType4, boolean boolean5, boolean boolean6, byte byte7, Optional<GlobalPos> optionalGlobalPos8, int int9) {
        this.dimensionType = resourceKeyDimensionType0;
        this.dimension = resourceKeyLevel1;
        this.seed = long2;
        this.playerGameType = gameType3;
        this.previousPlayerGameType = gameType4;
        this.isDebug = boolean5;
        this.isFlat = boolean6;
        this.dataToKeep = byte7;
        this.lastDeathLocation = optionalGlobalPos8;
        this.portalCooldown = int9;
    }

    public ClientboundRespawnPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.dimensionType = friendlyByteBuf0.readResourceKey(Registries.DIMENSION_TYPE);
        this.dimension = friendlyByteBuf0.readResourceKey(Registries.DIMENSION);
        this.seed = friendlyByteBuf0.readLong();
        this.playerGameType = GameType.byId(friendlyByteBuf0.readUnsignedByte());
        this.previousPlayerGameType = GameType.byNullableId(friendlyByteBuf0.readByte());
        this.isDebug = friendlyByteBuf0.readBoolean();
        this.isFlat = friendlyByteBuf0.readBoolean();
        this.dataToKeep = friendlyByteBuf0.readByte();
        this.lastDeathLocation = friendlyByteBuf0.readOptional(FriendlyByteBuf::m_236872_);
        this.portalCooldown = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeResourceKey(this.dimensionType);
        friendlyByteBuf0.writeResourceKey(this.dimension);
        friendlyByteBuf0.writeLong(this.seed);
        friendlyByteBuf0.writeByte(this.playerGameType.getId());
        friendlyByteBuf0.writeByte(GameType.getNullableId(this.previousPlayerGameType));
        friendlyByteBuf0.writeBoolean(this.isDebug);
        friendlyByteBuf0.writeBoolean(this.isFlat);
        friendlyByteBuf0.writeByte(this.dataToKeep);
        friendlyByteBuf0.writeOptional(this.lastDeathLocation, FriendlyByteBuf::m_236814_);
        friendlyByteBuf0.writeVarInt(this.portalCooldown);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleRespawn(this);
    }

    public ResourceKey<DimensionType> getDimensionType() {
        return this.dimensionType;
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    public long getSeed() {
        return this.seed;
    }

    public GameType getPlayerGameType() {
        return this.playerGameType;
    }

    @Nullable
    public GameType getPreviousPlayerGameType() {
        return this.previousPlayerGameType;
    }

    public boolean isDebug() {
        return this.isDebug;
    }

    public boolean isFlat() {
        return this.isFlat;
    }

    public boolean shouldKeep(byte byte0) {
        return (this.dataToKeep & byte0) != 0;
    }

    public Optional<GlobalPos> getLastDeathLocation() {
        return this.lastDeathLocation;
    }

    public int getPortalCooldown() {
        return this.portalCooldown;
    }
}