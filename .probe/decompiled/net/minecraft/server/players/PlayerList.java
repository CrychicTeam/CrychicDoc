package net.minecraft.server.players;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.FileUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetSimulationDistancePacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateEnabledFeaturesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagNetworkSerialization;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.slf4j.Logger;

public abstract class PlayerList {

    public static final File USERBANLIST_FILE = new File("banned-players.json");

    public static final File IPBANLIST_FILE = new File("banned-ips.json");

    public static final File OPLIST_FILE = new File("ops.json");

    public static final File WHITELIST_FILE = new File("whitelist.json");

    public static final Component CHAT_FILTERED_FULL = Component.translatable("chat.filtered_full");

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int SEND_PLAYER_INFO_INTERVAL = 600;

    private static final SimpleDateFormat BAN_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

    private final MinecraftServer server;

    private final List<ServerPlayer> players = Lists.newArrayList();

    private final Map<UUID, ServerPlayer> playersByUUID = Maps.newHashMap();

    private final UserBanList bans = new UserBanList(USERBANLIST_FILE);

    private final IpBanList ipBans = new IpBanList(IPBANLIST_FILE);

    private final ServerOpList ops = new ServerOpList(OPLIST_FILE);

    private final UserWhiteList whitelist = new UserWhiteList(WHITELIST_FILE);

    private final Map<UUID, ServerStatsCounter> stats = Maps.newHashMap();

    private final Map<UUID, PlayerAdvancements> advancements = Maps.newHashMap();

    private final PlayerDataStorage playerIo;

    private boolean doWhiteList;

    private final LayeredRegistryAccess<RegistryLayer> registries;

    private final RegistryAccess.Frozen synchronizedRegistries;

    protected final int maxPlayers;

    private int viewDistance;

    private int simulationDistance;

    private boolean allowCheatsForAllPlayers;

    private static final boolean ALLOW_LOGOUTIVATOR = false;

    private int sendAllPlayerInfoIn;

    public PlayerList(MinecraftServer minecraftServer0, LayeredRegistryAccess<RegistryLayer> layeredRegistryAccessRegistryLayer1, PlayerDataStorage playerDataStorage2, int int3) {
        this.server = minecraftServer0;
        this.registries = layeredRegistryAccessRegistryLayer1;
        this.synchronizedRegistries = new RegistryAccess.ImmutableRegistryAccess(RegistrySynchronization.networkedRegistries(layeredRegistryAccessRegistryLayer1)).m_203557_();
        this.maxPlayers = int3;
        this.playerIo = playerDataStorage2;
    }

    public void placeNewPlayer(Connection connection0, ServerPlayer serverPlayer1) {
        GameProfile $$2 = serverPlayer1.m_36316_();
        GameProfileCache $$3 = this.server.getProfileCache();
        String $$5;
        if ($$3 != null) {
            Optional<GameProfile> $$4 = $$3.get($$2.getId());
            $$5 = (String) $$4.map(GameProfile::getName).orElse($$2.getName());
            $$3.add($$2);
        } else {
            $$5 = $$2.getName();
        }
        CompoundTag $$7 = this.load(serverPlayer1);
        ResourceKey<Level> $$8 = $$7 != null ? (ResourceKey) DimensionType.parseLegacy(new Dynamic(NbtOps.INSTANCE, $$7.get("Dimension"))).resultOrPartial(LOGGER::error).orElse(Level.OVERWORLD) : Level.OVERWORLD;
        ServerLevel $$9 = this.server.getLevel($$8);
        ServerLevel $$10;
        if ($$9 == null) {
            LOGGER.warn("Unknown respawn dimension {}, defaulting to overworld", $$8);
            $$10 = this.server.overworld();
        } else {
            $$10 = $$9;
        }
        serverPlayer1.setServerLevel($$10);
        String $$12 = "local";
        if (connection0.getRemoteAddress() != null) {
            $$12 = connection0.getRemoteAddress().toString();
        }
        LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", new Object[] { serverPlayer1.m_7755_().getString(), $$12, serverPlayer1.m_19879_(), serverPlayer1.m_20185_(), serverPlayer1.m_20186_(), serverPlayer1.m_20189_() });
        LevelData $$13 = $$10.m_6106_();
        serverPlayer1.loadGameTypes($$7);
        ServerGamePacketListenerImpl $$14 = new ServerGamePacketListenerImpl(this.server, connection0, serverPlayer1);
        GameRules $$15 = $$10.m_46469_();
        boolean $$16 = $$15.getBoolean(GameRules.RULE_DO_IMMEDIATE_RESPAWN);
        boolean $$17 = $$15.getBoolean(GameRules.RULE_REDUCEDDEBUGINFO);
        $$14.send(new ClientboundLoginPacket(serverPlayer1.m_19879_(), $$13.isHardcore(), serverPlayer1.gameMode.getGameModeForPlayer(), serverPlayer1.gameMode.getPreviousGameModeForPlayer(), this.server.levelKeys(), this.synchronizedRegistries, $$10.m_220362_(), $$10.m_46472_(), BiomeManager.obfuscateSeed($$10.getSeed()), this.getMaxPlayers(), this.viewDistance, this.simulationDistance, $$17, !$$16, $$10.m_46659_(), $$10.isFlat(), serverPlayer1.m_219759_(), serverPlayer1.m_287157_()));
        $$14.send(new ClientboundUpdateEnabledFeaturesPacket(FeatureFlags.REGISTRY.toNames($$10.enabledFeatures())));
        $$14.send(new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.BRAND, new FriendlyByteBuf(Unpooled.buffer()).writeUtf(this.getServer().getServerModName())));
        $$14.send(new ClientboundChangeDifficultyPacket($$13.getDifficulty(), $$13.isDifficultyLocked()));
        $$14.send(new ClientboundPlayerAbilitiesPacket(serverPlayer1.m_150110_()));
        $$14.send(new ClientboundSetCarriedItemPacket(serverPlayer1.m_150109_().selected));
        $$14.send(new ClientboundUpdateRecipesPacket(this.server.getRecipeManager().getRecipes()));
        $$14.send(new ClientboundUpdateTagsPacket(TagNetworkSerialization.serializeTagsToNetwork(this.registries)));
        this.sendPlayerPermissionLevel(serverPlayer1);
        serverPlayer1.getStats().markAllDirty();
        serverPlayer1.getRecipeBook().sendInitialRecipeBook(serverPlayer1);
        this.updateEntireScoreboard($$10.getScoreboard(), serverPlayer1);
        this.server.invalidateStatus();
        MutableComponent $$18;
        if (serverPlayer1.m_36316_().getName().equalsIgnoreCase($$5)) {
            $$18 = Component.translatable("multiplayer.player.joined", serverPlayer1.m_5446_());
        } else {
            $$18 = Component.translatable("multiplayer.player.joined.renamed", serverPlayer1.m_5446_(), $$5);
        }
        this.broadcastSystemMessage($$18.withStyle(ChatFormatting.YELLOW), false);
        $$14.teleport(serverPlayer1.m_20185_(), serverPlayer1.m_20186_(), serverPlayer1.m_20189_(), serverPlayer1.m_146908_(), serverPlayer1.m_146909_());
        ServerStatus $$20 = this.server.getStatus();
        if ($$20 != null) {
            serverPlayer1.sendServerStatus($$20);
        }
        serverPlayer1.connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(this.players));
        this.players.add(serverPlayer1);
        this.playersByUUID.put(serverPlayer1.m_20148_(), serverPlayer1);
        this.broadcastAll(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(serverPlayer1)));
        this.sendLevelInfo(serverPlayer1, $$10);
        $$10.addNewPlayer(serverPlayer1);
        this.server.getCustomBossEvents().onPlayerConnect(serverPlayer1);
        this.server.getServerResourcePack().ifPresent(p_215606_ -> serverPlayer1.sendTexturePack(p_215606_.url(), p_215606_.hash(), p_215606_.isRequired(), p_215606_.prompt()));
        for (MobEffectInstance $$21 : serverPlayer1.m_21220_()) {
            $$14.send(new ClientboundUpdateMobEffectPacket(serverPlayer1.m_19879_(), $$21));
        }
        if ($$7 != null && $$7.contains("RootVehicle", 10)) {
            CompoundTag $$22 = $$7.getCompound("RootVehicle");
            Entity $$23 = EntityType.loadEntityRecursive($$22.getCompound("Entity"), $$10, p_215603_ -> !$$10.addWithUUID(p_215603_) ? null : p_215603_);
            if ($$23 != null) {
                UUID $$24;
                if ($$22.hasUUID("Attach")) {
                    $$24 = $$22.getUUID("Attach");
                } else {
                    $$24 = null;
                }
                if ($$23.getUUID().equals($$24)) {
                    serverPlayer1.startRiding($$23, true);
                } else {
                    for (Entity $$26 : $$23.getIndirectPassengers()) {
                        if ($$26.getUUID().equals($$24)) {
                            serverPlayer1.startRiding($$26, true);
                            break;
                        }
                    }
                }
                if (!serverPlayer1.m_20159_()) {
                    LOGGER.warn("Couldn't reattach entity to player");
                    $$23.discard();
                    for (Entity $$27 : $$23.getIndirectPassengers()) {
                        $$27.discard();
                    }
                }
            }
        }
        serverPlayer1.initInventoryMenu();
    }

    protected void updateEntireScoreboard(ServerScoreboard serverScoreboard0, ServerPlayer serverPlayer1) {
        Set<Objective> $$2 = Sets.newHashSet();
        for (PlayerTeam $$3 : serverScoreboard0.m_83491_()) {
            serverPlayer1.connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket($$3, true));
        }
        for (int $$4 = 0; $$4 < 19; $$4++) {
            Objective $$5 = serverScoreboard0.m_83416_($$4);
            if ($$5 != null && !$$2.contains($$5)) {
                for (Packet<?> $$7 : serverScoreboard0.getStartTrackingPackets($$5)) {
                    serverPlayer1.connection.send($$7);
                }
                $$2.add($$5);
            }
        }
    }

    public void addWorldborderListener(ServerLevel serverLevel0) {
        serverLevel0.m_6857_().addListener(new BorderChangeListener() {

            @Override
            public void onBorderSizeSet(WorldBorder p_11321_, double p_11322_) {
                PlayerList.this.broadcastAll(new ClientboundSetBorderSizePacket(p_11321_));
            }

            @Override
            public void onBorderSizeLerping(WorldBorder p_11328_, double p_11329_, double p_11330_, long p_11331_) {
                PlayerList.this.broadcastAll(new ClientboundSetBorderLerpSizePacket(p_11328_));
            }

            @Override
            public void onBorderCenterSet(WorldBorder p_11324_, double p_11325_, double p_11326_) {
                PlayerList.this.broadcastAll(new ClientboundSetBorderCenterPacket(p_11324_));
            }

            @Override
            public void onBorderSetWarningTime(WorldBorder p_11333_, int p_11334_) {
                PlayerList.this.broadcastAll(new ClientboundSetBorderWarningDelayPacket(p_11333_));
            }

            @Override
            public void onBorderSetWarningBlocks(WorldBorder p_11339_, int p_11340_) {
                PlayerList.this.broadcastAll(new ClientboundSetBorderWarningDistancePacket(p_11339_));
            }

            @Override
            public void onBorderSetDamagePerBlock(WorldBorder p_11336_, double p_11337_) {
            }

            @Override
            public void onBorderSetDamageSafeZOne(WorldBorder p_11342_, double p_11343_) {
            }
        });
    }

    @Nullable
    public CompoundTag load(ServerPlayer serverPlayer0) {
        CompoundTag $$1 = this.server.getWorldData().getLoadedPlayerTag();
        CompoundTag $$2;
        if (this.server.isSingleplayerOwner(serverPlayer0.m_36316_()) && $$1 != null) {
            $$2 = $$1;
            serverPlayer0.m_20258_($$1);
            LOGGER.debug("loading single player");
        } else {
            $$2 = this.playerIo.load(serverPlayer0);
        }
        return $$2;
    }

    protected void save(ServerPlayer serverPlayer0) {
        this.playerIo.save(serverPlayer0);
        ServerStatsCounter $$1 = (ServerStatsCounter) this.stats.get(serverPlayer0.m_20148_());
        if ($$1 != null) {
            $$1.save();
        }
        PlayerAdvancements $$2 = (PlayerAdvancements) this.advancements.get(serverPlayer0.m_20148_());
        if ($$2 != null) {
            $$2.save();
        }
    }

    public void remove(ServerPlayer serverPlayer0) {
        ServerLevel $$1 = serverPlayer0.serverLevel();
        serverPlayer0.m_36220_(Stats.LEAVE_GAME);
        this.save(serverPlayer0);
        if (serverPlayer0.m_20159_()) {
            Entity $$2 = serverPlayer0.m_20201_();
            if ($$2.hasExactlyOnePlayerPassenger()) {
                LOGGER.debug("Removing player mount");
                serverPlayer0.stopRiding();
                $$2.getPassengersAndSelf().forEach(p_215620_ -> p_215620_.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER));
            }
        }
        serverPlayer0.m_19877_();
        $$1.removePlayerImmediately(serverPlayer0, Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        serverPlayer0.getAdvancements().stopListening();
        this.players.remove(serverPlayer0);
        this.server.getCustomBossEvents().onPlayerDisconnect(serverPlayer0);
        UUID $$3 = serverPlayer0.m_20148_();
        ServerPlayer $$4 = (ServerPlayer) this.playersByUUID.get($$3);
        if ($$4 == serverPlayer0) {
            this.playersByUUID.remove($$3);
            this.stats.remove($$3);
            this.advancements.remove($$3);
        }
        this.broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(serverPlayer0.m_20148_())));
    }

    @Nullable
    public Component canPlayerLogin(SocketAddress socketAddress0, GameProfile gameProfile1) {
        if (this.bans.isBanned(gameProfile1)) {
            UserBanListEntry $$2 = (UserBanListEntry) this.bans.m_11388_(gameProfile1);
            MutableComponent $$3 = Component.translatable("multiplayer.disconnect.banned.reason", $$2.m_10962_());
            if ($$2.m_10961_() != null) {
                $$3.append(Component.translatable("multiplayer.disconnect.banned.expiration", BAN_DATE_FORMAT.format($$2.m_10961_())));
            }
            return $$3;
        } else if (!this.isWhiteListed(gameProfile1)) {
            return Component.translatable("multiplayer.disconnect.not_whitelisted");
        } else if (this.ipBans.isBanned(socketAddress0)) {
            IpBanListEntry $$4 = this.ipBans.get(socketAddress0);
            MutableComponent $$5 = Component.translatable("multiplayer.disconnect.banned_ip.reason", $$4.m_10962_());
            if ($$4.m_10961_() != null) {
                $$5.append(Component.translatable("multiplayer.disconnect.banned_ip.expiration", BAN_DATE_FORMAT.format($$4.m_10961_())));
            }
            return $$5;
        } else {
            return this.players.size() >= this.maxPlayers && !this.canBypassPlayerLimit(gameProfile1) ? Component.translatable("multiplayer.disconnect.server_full") : null;
        }
    }

    public ServerPlayer getPlayerForLogin(GameProfile gameProfile0) {
        UUID $$1 = UUIDUtil.getOrCreatePlayerUUID(gameProfile0);
        List<ServerPlayer> $$2 = Lists.newArrayList();
        for (int $$3 = 0; $$3 < this.players.size(); $$3++) {
            ServerPlayer $$4 = (ServerPlayer) this.players.get($$3);
            if ($$4.m_20148_().equals($$1)) {
                $$2.add($$4);
            }
        }
        ServerPlayer $$5 = (ServerPlayer) this.playersByUUID.get(gameProfile0.getId());
        if ($$5 != null && !$$2.contains($$5)) {
            $$2.add($$5);
        }
        for (ServerPlayer $$6 : $$2) {
            $$6.connection.disconnect(Component.translatable("multiplayer.disconnect.duplicate_login"));
        }
        return new ServerPlayer(this.server, this.server.overworld(), gameProfile0);
    }

    public ServerPlayer respawn(ServerPlayer serverPlayer0, boolean boolean1) {
        this.players.remove(serverPlayer0);
        serverPlayer0.serverLevel().removePlayerImmediately(serverPlayer0, Entity.RemovalReason.DISCARDED);
        BlockPos $$2 = serverPlayer0.getRespawnPosition();
        float $$3 = serverPlayer0.getRespawnAngle();
        boolean $$4 = serverPlayer0.isRespawnForced();
        ServerLevel $$5 = this.server.getLevel(serverPlayer0.getRespawnDimension());
        Optional<Vec3> $$6;
        if ($$5 != null && $$2 != null) {
            $$6 = Player.findRespawnPositionAndUseSpawnBlock($$5, $$2, $$3, $$4, boolean1);
        } else {
            $$6 = Optional.empty();
        }
        ServerLevel $$8 = $$5 != null && $$6.isPresent() ? $$5 : this.server.overworld();
        ServerPlayer $$9 = new ServerPlayer(this.server, $$8, serverPlayer0.m_36316_());
        $$9.connection = serverPlayer0.connection;
        $$9.restoreFrom(serverPlayer0, boolean1);
        $$9.m_20234_(serverPlayer0.m_19879_());
        $$9.m_36163_(serverPlayer0.m_5737_());
        for (String $$10 : serverPlayer0.m_19880_()) {
            $$9.m_20049_($$10);
        }
        boolean $$11 = false;
        if ($$6.isPresent()) {
            BlockState $$12 = $$8.m_8055_($$2);
            boolean $$13 = $$12.m_60713_(Blocks.RESPAWN_ANCHOR);
            Vec3 $$14 = (Vec3) $$6.get();
            float $$17;
            if (!$$12.m_204336_(BlockTags.BEDS) && !$$13) {
                $$17 = $$3;
            } else {
                Vec3 $$15 = Vec3.atBottomCenterOf($$2).subtract($$14).normalize();
                $$17 = (float) Mth.wrapDegrees(Mth.atan2($$15.z, $$15.x) * 180.0F / (float) Math.PI - 90.0);
            }
            $$9.m_7678_($$14.x, $$14.y, $$14.z, $$17, 0.0F);
            $$9.setRespawnPosition($$8.m_46472_(), $$2, $$3, $$4, false);
            $$11 = !boolean1 && $$13;
        } else if ($$2 != null) {
            $$9.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0F));
        }
        while (!$$8.m_45786_($$9) && $$9.m_20186_() < (double) $$8.m_151558_()) {
            $$9.m_6034_($$9.m_20185_(), $$9.m_20186_() + 1.0, $$9.m_20189_());
        }
        byte $$18 = (byte) (boolean1 ? 1 : 0);
        LevelData $$19 = $$9.m_9236_().getLevelData();
        $$9.connection.send(new ClientboundRespawnPacket($$9.m_9236_().dimensionTypeId(), $$9.m_9236_().dimension(), BiomeManager.obfuscateSeed($$9.serverLevel().getSeed()), $$9.gameMode.getGameModeForPlayer(), $$9.gameMode.getPreviousGameModeForPlayer(), $$9.m_9236_().isDebug(), $$9.serverLevel().isFlat(), $$18, $$9.m_219759_(), $$9.m_287157_()));
        $$9.connection.teleport($$9.m_20185_(), $$9.m_20186_(), $$9.m_20189_(), $$9.m_146908_(), $$9.m_146909_());
        $$9.connection.send(new ClientboundSetDefaultSpawnPositionPacket($$8.m_220360_(), $$8.m_220361_()));
        $$9.connection.send(new ClientboundChangeDifficultyPacket($$19.getDifficulty(), $$19.isDifficultyLocked()));
        $$9.connection.send(new ClientboundSetExperiencePacket($$9.f_36080_, $$9.f_36079_, $$9.f_36078_));
        this.sendLevelInfo($$9, $$8);
        this.sendPlayerPermissionLevel($$9);
        $$8.addRespawnedPlayer($$9);
        this.players.add($$9);
        this.playersByUUID.put($$9.m_20148_(), $$9);
        $$9.initInventoryMenu();
        $$9.m_21153_($$9.m_21223_());
        if ($$11) {
            $$9.connection.send(new ClientboundSoundPacket(SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, (double) $$2.m_123341_(), (double) $$2.m_123342_(), (double) $$2.m_123343_(), 1.0F, 1.0F, $$8.m_213780_().nextLong()));
        }
        return $$9;
    }

    public void sendPlayerPermissionLevel(ServerPlayer serverPlayer0) {
        GameProfile $$1 = serverPlayer0.m_36316_();
        int $$2 = this.server.getProfilePermissions($$1);
        this.sendPlayerPermissionLevel(serverPlayer0, $$2);
    }

    public void tick() {
        if (++this.sendAllPlayerInfoIn > 600) {
            this.broadcastAll(new ClientboundPlayerInfoUpdatePacket(EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY), this.players));
            this.sendAllPlayerInfoIn = 0;
        }
    }

    public void broadcastAll(Packet<?> packet0) {
        for (ServerPlayer $$1 : this.players) {
            $$1.connection.send(packet0);
        }
    }

    public void broadcastAll(Packet<?> packet0, ResourceKey<Level> resourceKeyLevel1) {
        for (ServerPlayer $$2 : this.players) {
            if ($$2.m_9236_().dimension() == resourceKeyLevel1) {
                $$2.connection.send(packet0);
            }
        }
    }

    public void broadcastSystemToTeam(Player player0, Component component1) {
        Team $$2 = player0.m_5647_();
        if ($$2 != null) {
            for (String $$4 : $$2.getPlayers()) {
                ServerPlayer $$5 = this.getPlayerByName($$4);
                if ($$5 != null && $$5 != player0) {
                    $$5.sendSystemMessage(component1);
                }
            }
        }
    }

    public void broadcastSystemToAllExceptTeam(Player player0, Component component1) {
        Team $$2 = player0.m_5647_();
        if ($$2 == null) {
            this.broadcastSystemMessage(component1, false);
        } else {
            for (int $$3 = 0; $$3 < this.players.size(); $$3++) {
                ServerPlayer $$4 = (ServerPlayer) this.players.get($$3);
                if ($$4.m_5647_() != $$2) {
                    $$4.sendSystemMessage(component1);
                }
            }
        }
    }

    public String[] getPlayerNamesArray() {
        String[] $$0 = new String[this.players.size()];
        for (int $$1 = 0; $$1 < this.players.size(); $$1++) {
            $$0[$$1] = ((ServerPlayer) this.players.get($$1)).m_36316_().getName();
        }
        return $$0;
    }

    public UserBanList getBans() {
        return this.bans;
    }

    public IpBanList getIpBans() {
        return this.ipBans;
    }

    public void op(GameProfile gameProfile0) {
        this.ops.m_11381_(new ServerOpListEntry(gameProfile0, this.server.getOperatorUserPermissionLevel(), this.ops.canBypassPlayerLimit(gameProfile0)));
        ServerPlayer $$1 = this.getPlayer(gameProfile0.getId());
        if ($$1 != null) {
            this.sendPlayerPermissionLevel($$1);
        }
    }

    public void deop(GameProfile gameProfile0) {
        this.ops.m_11393_(gameProfile0);
        ServerPlayer $$1 = this.getPlayer(gameProfile0.getId());
        if ($$1 != null) {
            this.sendPlayerPermissionLevel($$1);
        }
    }

    private void sendPlayerPermissionLevel(ServerPlayer serverPlayer0, int int1) {
        if (serverPlayer0.connection != null) {
            byte $$2;
            if (int1 <= 0) {
                $$2 = 24;
            } else if (int1 >= 4) {
                $$2 = 28;
            } else {
                $$2 = (byte) (24 + int1);
            }
            serverPlayer0.connection.send(new ClientboundEntityEventPacket(serverPlayer0, $$2));
        }
        this.server.getCommands().sendCommands(serverPlayer0);
    }

    public boolean isWhiteListed(GameProfile gameProfile0) {
        return !this.doWhiteList || this.ops.m_11396_(gameProfile0) || this.whitelist.m_11396_(gameProfile0);
    }

    public boolean isOp(GameProfile gameProfile0) {
        return this.ops.m_11396_(gameProfile0) || this.server.isSingleplayerOwner(gameProfile0) && this.server.getWorldData().getAllowCommands() || this.allowCheatsForAllPlayers;
    }

    @Nullable
    public ServerPlayer getPlayerByName(String string0) {
        for (ServerPlayer $$1 : this.players) {
            if ($$1.m_36316_().getName().equalsIgnoreCase(string0)) {
                return $$1;
            }
        }
        return null;
    }

    public void broadcast(@Nullable Player player0, double double1, double double2, double double3, double double4, ResourceKey<Level> resourceKeyLevel5, Packet<?> packet6) {
        for (int $$7 = 0; $$7 < this.players.size(); $$7++) {
            ServerPlayer $$8 = (ServerPlayer) this.players.get($$7);
            if ($$8 != player0 && $$8.m_9236_().dimension() == resourceKeyLevel5) {
                double $$9 = double1 - $$8.m_20185_();
                double $$10 = double2 - $$8.m_20186_();
                double $$11 = double3 - $$8.m_20189_();
                if ($$9 * $$9 + $$10 * $$10 + $$11 * $$11 < double4 * double4) {
                    $$8.connection.send(packet6);
                }
            }
        }
    }

    public void saveAll() {
        for (int $$0 = 0; $$0 < this.players.size(); $$0++) {
            this.save((ServerPlayer) this.players.get($$0));
        }
    }

    public UserWhiteList getWhiteList() {
        return this.whitelist;
    }

    public String[] getWhiteListNames() {
        return this.whitelist.getUserList();
    }

    public ServerOpList getOps() {
        return this.ops;
    }

    public String[] getOpNames() {
        return this.ops.getUserList();
    }

    public void reloadWhiteList() {
    }

    public void sendLevelInfo(ServerPlayer serverPlayer0, ServerLevel serverLevel1) {
        WorldBorder $$2 = this.server.overworld().m_6857_();
        serverPlayer0.connection.send(new ClientboundInitializeBorderPacket($$2));
        serverPlayer0.connection.send(new ClientboundSetTimePacket(serverLevel1.m_46467_(), serverLevel1.m_46468_(), serverLevel1.m_46469_().getBoolean(GameRules.RULE_DAYLIGHT)));
        serverPlayer0.connection.send(new ClientboundSetDefaultSpawnPositionPacket(serverLevel1.m_220360_(), serverLevel1.m_220361_()));
        if (serverLevel1.m_46471_()) {
            serverPlayer0.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0F));
            serverPlayer0.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, serverLevel1.m_46722_(1.0F)));
            serverPlayer0.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, serverLevel1.m_46661_(1.0F)));
        }
    }

    public void sendAllPlayerInfo(ServerPlayer serverPlayer0) {
        serverPlayer0.f_36095_.m_150429_();
        serverPlayer0.resetSentInfo();
        serverPlayer0.connection.send(new ClientboundSetCarriedItemPacket(serverPlayer0.m_150109_().selected));
    }

    public int getPlayerCount() {
        return this.players.size();
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public boolean isUsingWhitelist() {
        return this.doWhiteList;
    }

    public void setUsingWhiteList(boolean boolean0) {
        this.doWhiteList = boolean0;
    }

    public List<ServerPlayer> getPlayersWithAddress(String string0) {
        List<ServerPlayer> $$1 = Lists.newArrayList();
        for (ServerPlayer $$2 : this.players) {
            if ($$2.getIpAddress().equals(string0)) {
                $$1.add($$2);
            }
        }
        return $$1;
    }

    public int getViewDistance() {
        return this.viewDistance;
    }

    public int getSimulationDistance() {
        return this.simulationDistance;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    @Nullable
    public CompoundTag getSingleplayerData() {
        return null;
    }

    public void setAllowCheatsForAllPlayers(boolean boolean0) {
        this.allowCheatsForAllPlayers = boolean0;
    }

    public void removeAll() {
        for (int $$0 = 0; $$0 < this.players.size(); $$0++) {
            ((ServerPlayer) this.players.get($$0)).connection.disconnect(Component.translatable("multiplayer.disconnect.server_shutdown"));
        }
    }

    public void broadcastSystemMessage(Component component0, boolean boolean1) {
        this.broadcastSystemMessage(component0, p_215639_ -> component0, boolean1);
    }

    public void broadcastSystemMessage(Component component0, Function<ServerPlayer, Component> functionServerPlayerComponent1, boolean boolean2) {
        this.server.sendSystemMessage(component0);
        for (ServerPlayer $$3 : this.players) {
            Component $$4 = (Component) functionServerPlayerComponent1.apply($$3);
            if ($$4 != null) {
                $$3.sendSystemMessage($$4, boolean2);
            }
        }
    }

    public void broadcastChatMessage(PlayerChatMessage playerChatMessage0, CommandSourceStack commandSourceStack1, ChatType.Bound chatTypeBound2) {
        this.broadcastChatMessage(playerChatMessage0, commandSourceStack1::m_243061_, commandSourceStack1.getPlayer(), chatTypeBound2);
    }

    public void broadcastChatMessage(PlayerChatMessage playerChatMessage0, ServerPlayer serverPlayer1, ChatType.Bound chatTypeBound2) {
        this.broadcastChatMessage(playerChatMessage0, serverPlayer1::m_143421_, serverPlayer1, chatTypeBound2);
    }

    private void broadcastChatMessage(PlayerChatMessage playerChatMessage0, Predicate<ServerPlayer> predicateServerPlayer1, @Nullable ServerPlayer serverPlayer2, ChatType.Bound chatTypeBound3) {
        boolean $$4 = this.verifyChatTrusted(playerChatMessage0);
        this.server.logChatMessage(playerChatMessage0.decoratedContent(), chatTypeBound3, $$4 ? null : "Not Secure");
        OutgoingChatMessage $$5 = OutgoingChatMessage.create(playerChatMessage0);
        boolean $$6 = false;
        for (ServerPlayer $$7 : this.players) {
            boolean $$8 = predicateServerPlayer1.test($$7);
            $$7.sendChatMessage($$5, $$8, chatTypeBound3);
            $$6 |= $$8 && playerChatMessage0.isFullyFiltered();
        }
        if ($$6 && serverPlayer2 != null) {
            serverPlayer2.sendSystemMessage(CHAT_FILTERED_FULL);
        }
    }

    private boolean verifyChatTrusted(PlayerChatMessage playerChatMessage0) {
        return playerChatMessage0.hasSignature() && !playerChatMessage0.hasExpiredServer(Instant.now());
    }

    public ServerStatsCounter getPlayerStats(Player player0) {
        UUID $$1 = player0.m_20148_();
        ServerStatsCounter $$2 = (ServerStatsCounter) this.stats.get($$1);
        if ($$2 == null) {
            File $$3 = this.server.getWorldPath(LevelResource.PLAYER_STATS_DIR).toFile();
            File $$4 = new File($$3, $$1 + ".json");
            if (!$$4.exists()) {
                File $$5 = new File($$3, player0.getName().getString() + ".json");
                Path $$6 = $$5.toPath();
                if (FileUtil.isPathNormalized($$6) && FileUtil.isPathPortable($$6) && $$6.startsWith($$3.getPath()) && $$5.isFile()) {
                    $$5.renameTo($$4);
                }
            }
            $$2 = new ServerStatsCounter(this.server, $$4);
            this.stats.put($$1, $$2);
        }
        return $$2;
    }

    public PlayerAdvancements getPlayerAdvancements(ServerPlayer serverPlayer0) {
        UUID $$1 = serverPlayer0.m_20148_();
        PlayerAdvancements $$2 = (PlayerAdvancements) this.advancements.get($$1);
        if ($$2 == null) {
            Path $$3 = this.server.getWorldPath(LevelResource.PLAYER_ADVANCEMENTS_DIR).resolve($$1 + ".json");
            $$2 = new PlayerAdvancements(this.server.getFixerUpper(), this, this.server.getAdvancements(), $$3, serverPlayer0);
            this.advancements.put($$1, $$2);
        }
        $$2.setPlayer(serverPlayer0);
        return $$2;
    }

    public void setViewDistance(int int0) {
        this.viewDistance = int0;
        this.broadcastAll(new ClientboundSetChunkCacheRadiusPacket(int0));
        for (ServerLevel $$1 : this.server.getAllLevels()) {
            if ($$1 != null) {
                $$1.getChunkSource().setViewDistance(int0);
            }
        }
    }

    public void setSimulationDistance(int int0) {
        this.simulationDistance = int0;
        this.broadcastAll(new ClientboundSetSimulationDistancePacket(int0));
        for (ServerLevel $$1 : this.server.getAllLevels()) {
            if ($$1 != null) {
                $$1.getChunkSource().setSimulationDistance(int0);
            }
        }
    }

    public List<ServerPlayer> getPlayers() {
        return this.players;
    }

    @Nullable
    public ServerPlayer getPlayer(UUID uUID0) {
        return (ServerPlayer) this.playersByUUID.get(uUID0);
    }

    public boolean canBypassPlayerLimit(GameProfile gameProfile0) {
        return false;
    }

    public void reloadResources() {
        for (PlayerAdvancements $$0 : this.advancements.values()) {
            $$0.reload(this.server.getAdvancements());
        }
        this.broadcastAll(new ClientboundUpdateTagsPacket(TagNetworkSerialization.serializeTagsToNetwork(this.registries)));
        ClientboundUpdateRecipesPacket $$1 = new ClientboundUpdateRecipesPacket(this.server.getRecipeManager().getRecipes());
        for (ServerPlayer $$2 : this.players) {
            $$2.connection.send($$1);
            $$2.getRecipeBook().sendInitialRecipeBook($$2);
        }
    }

    public boolean isAllowCheatsForAllPlayers() {
        return this.allowCheatsForAllPlayers;
    }
}