package net.minecraft.network.protocol.game;

import com.google.common.collect.Sets;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public record ClientboundLoginPacket(int f_132360_, boolean f_132362_, GameType f_132363_, @Nullable GameType f_132364_, Set<ResourceKey<Level>> f_132365_, RegistryAccess.Frozen f_132366_, ResourceKey<DimensionType> f_132367_, ResourceKey<Level> f_132368_, long f_132361_, int f_132369_, int f_132370_, int f_195761_, boolean f_132371_, boolean f_132372_, boolean f_132373_, boolean f_132374_, Optional<GlobalPos> f_238174_, int f_286971_) implements Packet<ClientGamePacketListener> {

    private final int playerId;

    private final boolean hardcore;

    private final GameType gameType;

    @Nullable
    private final GameType previousGameType;

    private final Set<ResourceKey<Level>> levels;

    private final RegistryAccess.Frozen registryHolder;

    private final ResourceKey<DimensionType> dimensionType;

    private final ResourceKey<Level> dimension;

    private final long seed;

    private final int maxPlayers;

    private final int chunkRadius;

    private final int simulationDistance;

    private final boolean reducedDebugInfo;

    private final boolean showDeathScreen;

    private final boolean isDebug;

    private final boolean isFlat;

    private final Optional<GlobalPos> lastDeathLocation;

    private final int portalCooldown;

    private static final RegistryOps<Tag> BUILTIN_CONTEXT_OPS = RegistryOps.create(NbtOps.INSTANCE, RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));

    public ClientboundLoginPacket(FriendlyByteBuf p_178960_) {
        this(p_178960_.readInt(), p_178960_.readBoolean(), GameType.byId(p_178960_.readByte()), GameType.byNullableId(p_178960_.readByte()), p_178960_.readCollection(Sets::newHashSetWithExpectedSize, p_258210_ -> p_258210_.readResourceKey(Registries.DIMENSION)), p_178960_.<RegistryAccess>readWithCodec(BUILTIN_CONTEXT_OPS, RegistrySynchronization.NETWORK_CODEC).freeze(), p_178960_.readResourceKey(Registries.DIMENSION_TYPE), p_178960_.readResourceKey(Registries.DIMENSION), p_178960_.readLong(), p_178960_.readVarInt(), p_178960_.readVarInt(), p_178960_.readVarInt(), p_178960_.readBoolean(), p_178960_.readBoolean(), p_178960_.readBoolean(), p_178960_.readBoolean(), p_178960_.readOptional(FriendlyByteBuf::m_236872_), p_178960_.readVarInt());
    }

    public ClientboundLoginPacket(int f_132360_, boolean f_132362_, GameType f_132363_, @Nullable GameType f_132364_, Set<ResourceKey<Level>> f_132365_, RegistryAccess.Frozen f_132366_, ResourceKey<DimensionType> f_132367_, ResourceKey<Level> f_132368_, long f_132361_, int f_132369_, int f_132370_, int f_195761_, boolean f_132371_, boolean f_132372_, boolean f_132373_, boolean f_132374_, Optional<GlobalPos> f_238174_, int f_286971_) {
        this.playerId = f_132360_;
        this.hardcore = f_132362_;
        this.gameType = f_132363_;
        this.previousGameType = f_132364_;
        this.levels = f_132365_;
        this.registryHolder = f_132366_;
        this.dimensionType = f_132367_;
        this.dimension = f_132368_;
        this.seed = f_132361_;
        this.maxPlayers = f_132369_;
        this.chunkRadius = f_132370_;
        this.simulationDistance = f_195761_;
        this.reducedDebugInfo = f_132371_;
        this.showDeathScreen = f_132372_;
        this.isDebug = f_132373_;
        this.isFlat = f_132374_;
        this.lastDeathLocation = f_238174_;
        this.portalCooldown = f_286971_;
    }

    @Override
    public void write(FriendlyByteBuf p_132400_) {
        p_132400_.writeInt(this.playerId);
        p_132400_.writeBoolean(this.hardcore);
        p_132400_.writeByte(this.gameType.getId());
        p_132400_.writeByte(GameType.getNullableId(this.previousGameType));
        p_132400_.writeCollection(this.levels, FriendlyByteBuf::m_236858_);
        p_132400_.writeWithCodec(BUILTIN_CONTEXT_OPS, RegistrySynchronization.NETWORK_CODEC, this.registryHolder);
        p_132400_.writeResourceKey(this.dimensionType);
        p_132400_.writeResourceKey(this.dimension);
        p_132400_.writeLong(this.seed);
        p_132400_.writeVarInt(this.maxPlayers);
        p_132400_.writeVarInt(this.chunkRadius);
        p_132400_.writeVarInt(this.simulationDistance);
        p_132400_.writeBoolean(this.reducedDebugInfo);
        p_132400_.writeBoolean(this.showDeathScreen);
        p_132400_.writeBoolean(this.isDebug);
        p_132400_.writeBoolean(this.isFlat);
        p_132400_.writeOptional(this.lastDeathLocation, FriendlyByteBuf::m_236814_);
        p_132400_.writeVarInt(this.portalCooldown);
    }

    public void handle(ClientGamePacketListener p_132397_) {
        p_132397_.handleLogin(this);
    }
}