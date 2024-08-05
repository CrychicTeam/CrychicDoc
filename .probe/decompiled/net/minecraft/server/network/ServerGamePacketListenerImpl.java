package net.minecraft.server.network;

import com.google.common.collect.Lists;
import com.google.common.primitives.Floats;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.net.SocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.commands.CommandSigningContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.chat.LastSeenMessagesValidator;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MessageSignatureCache;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.chat.SignableCommand;
import net.minecraft.network.chat.SignedMessageBody;
import net.minecraft.network.chat.SignedMessageChain;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundBlockChangedAckPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundTagQueryPacket;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQuery;
import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundChatAckPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.network.protocol.game.ServerboundEntityTagQuery;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FutureChain;
import net.minecraft.util.Mth;
import net.minecraft.util.SignatureValidator;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

public class ServerGamePacketListenerImpl implements ServerPlayerConnection, TickablePacketListener, ServerGamePacketListener {

    static final Logger LOGGER = LogUtils.getLogger();

    private static final int LATENCY_CHECK_INTERVAL = 15000;

    public static final double MAX_INTERACTION_DISTANCE = Mth.square(6.0);

    private static final int NO_BLOCK_UPDATES_TO_ACK = -1;

    private static final int TRACKED_MESSAGE_DISCONNECT_THRESHOLD = 4096;

    private static final Component CHAT_VALIDATION_FAILED = Component.translatable("multiplayer.disconnect.chat_validation_failed");

    private final Connection connection;

    private final MinecraftServer server;

    public ServerPlayer player;

    private int tickCount;

    private int ackBlockChangesUpTo = -1;

    private long keepAliveTime;

    private boolean keepAlivePending;

    private long keepAliveChallenge;

    private int chatSpamTickCount;

    private int dropSpamTickCount;

    private double firstGoodX;

    private double firstGoodY;

    private double firstGoodZ;

    private double lastGoodX;

    private double lastGoodY;

    private double lastGoodZ;

    @Nullable
    private Entity lastVehicle;

    private double vehicleFirstGoodX;

    private double vehicleFirstGoodY;

    private double vehicleFirstGoodZ;

    private double vehicleLastGoodX;

    private double vehicleLastGoodY;

    private double vehicleLastGoodZ;

    @Nullable
    private Vec3 awaitingPositionFromClient;

    private int awaitingTeleport;

    private int awaitingTeleportTime;

    private boolean clientIsFloating;

    private int aboveGroundTickCount;

    private boolean clientVehicleIsFloating;

    private int aboveGroundVehicleTickCount;

    private int receivedMovePacketCount;

    private int knownMovePacketCount;

    private final AtomicReference<Instant> lastChatTimeStamp = new AtomicReference(Instant.EPOCH);

    @Nullable
    private RemoteChatSession chatSession;

    private SignedMessageChain.Decoder signedMessageDecoder;

    private final LastSeenMessagesValidator lastSeenMessages = new LastSeenMessagesValidator(20);

    private final MessageSignatureCache messageSignatureCache = MessageSignatureCache.createDefault();

    private final FutureChain chatMessageChain;

    public ServerGamePacketListenerImpl(MinecraftServer minecraftServer0, Connection connection1, ServerPlayer serverPlayer2) {
        this.server = minecraftServer0;
        this.connection = connection1;
        connection1.setListener(this);
        this.player = serverPlayer2;
        serverPlayer2.connection = this;
        this.keepAliveTime = Util.getMillis();
        serverPlayer2.getTextFilter().join();
        this.signedMessageDecoder = minecraftServer0.enforceSecureProfile() ? SignedMessageChain.Decoder.REJECT_ALL : SignedMessageChain.Decoder.unsigned(serverPlayer2.m_20148_());
        this.chatMessageChain = new FutureChain(minecraftServer0);
    }

    @Override
    public void tick() {
        if (this.ackBlockChangesUpTo > -1) {
            this.send(new ClientboundBlockChangedAckPacket(this.ackBlockChangesUpTo));
            this.ackBlockChangesUpTo = -1;
        }
        this.resetPosition();
        this.player.f_19854_ = this.player.m_20185_();
        this.player.f_19855_ = this.player.m_20186_();
        this.player.f_19856_ = this.player.m_20189_();
        this.player.doTick();
        this.player.m_19890_(this.firstGoodX, this.firstGoodY, this.firstGoodZ, this.player.m_146908_(), this.player.m_146909_());
        this.tickCount++;
        this.knownMovePacketCount = this.receivedMovePacketCount;
        if (this.clientIsFloating && !this.player.m_5803_() && !this.player.m_20159_() && !this.player.m_21224_()) {
            if (++this.aboveGroundTickCount > 80) {
                LOGGER.warn("{} was kicked for floating too long!", this.player.m_7755_().getString());
                this.disconnect(Component.translatable("multiplayer.disconnect.flying"));
                return;
            }
        } else {
            this.clientIsFloating = false;
            this.aboveGroundTickCount = 0;
        }
        this.lastVehicle = this.player.m_20201_();
        if (this.lastVehicle != this.player && this.lastVehicle.getControllingPassenger() == this.player) {
            this.vehicleFirstGoodX = this.lastVehicle.getX();
            this.vehicleFirstGoodY = this.lastVehicle.getY();
            this.vehicleFirstGoodZ = this.lastVehicle.getZ();
            this.vehicleLastGoodX = this.lastVehicle.getX();
            this.vehicleLastGoodY = this.lastVehicle.getY();
            this.vehicleLastGoodZ = this.lastVehicle.getZ();
            if (this.clientVehicleIsFloating && this.player.m_20201_().getControllingPassenger() == this.player) {
                if (++this.aboveGroundVehicleTickCount > 80) {
                    LOGGER.warn("{} was kicked for floating a vehicle too long!", this.player.m_7755_().getString());
                    this.disconnect(Component.translatable("multiplayer.disconnect.flying"));
                    return;
                }
            } else {
                this.clientVehicleIsFloating = false;
                this.aboveGroundVehicleTickCount = 0;
            }
        } else {
            this.lastVehicle = null;
            this.clientVehicleIsFloating = false;
            this.aboveGroundVehicleTickCount = 0;
        }
        this.server.getProfiler().push("keepAlive");
        long $$0 = Util.getMillis();
        if ($$0 - this.keepAliveTime >= 15000L) {
            if (this.keepAlivePending) {
                this.disconnect(Component.translatable("disconnect.timeout"));
            } else {
                this.keepAlivePending = true;
                this.keepAliveTime = $$0;
                this.keepAliveChallenge = $$0;
                this.send(new ClientboundKeepAlivePacket(this.keepAliveChallenge));
            }
        }
        this.server.getProfiler().pop();
        if (this.chatSpamTickCount > 0) {
            this.chatSpamTickCount--;
        }
        if (this.dropSpamTickCount > 0) {
            this.dropSpamTickCount--;
        }
        if (this.player.getLastActionTime() > 0L && this.server.getPlayerIdleTimeout() > 0 && Util.getMillis() - this.player.getLastActionTime() > (long) this.server.getPlayerIdleTimeout() * 1000L * 60L) {
            this.disconnect(Component.translatable("multiplayer.disconnect.idling"));
        }
    }

    public void resetPosition() {
        this.firstGoodX = this.player.m_20185_();
        this.firstGoodY = this.player.m_20186_();
        this.firstGoodZ = this.player.m_20189_();
        this.lastGoodX = this.player.m_20185_();
        this.lastGoodY = this.player.m_20186_();
        this.lastGoodZ = this.player.m_20189_();
    }

    @Override
    public boolean isAcceptingMessages() {
        return this.connection.isConnected();
    }

    private boolean isSingleplayerOwner() {
        return this.server.isSingleplayerOwner(this.player.m_36316_());
    }

    public void disconnect(Component component0) {
        this.connection.send(new ClientboundDisconnectPacket(component0), PacketSendListener.thenRun(() -> this.connection.disconnect(component0)));
        this.connection.setReadOnly();
        this.server.m_18709_(this.connection::m_129541_);
    }

    private <T, R> CompletableFuture<R> filterTextPacket(T t0, BiFunction<TextFilter, T, CompletableFuture<R>> biFunctionTextFilterTCompletableFutureR1) {
        return ((CompletableFuture) biFunctionTextFilterTCompletableFutureR1.apply(this.player.getTextFilter(), t0)).thenApply(p_264862_ -> {
            if (!this.isAcceptingMessages()) {
                LOGGER.debug("Ignoring packet due to disconnection");
                throw new CancellationException("disconnected");
            } else {
                return p_264862_;
            }
        });
    }

    private CompletableFuture<FilteredText> filterTextPacket(String string0) {
        return this.filterTextPacket(string0, TextFilter::m_6770_);
    }

    private CompletableFuture<List<FilteredText>> filterTextPacket(List<String> listString0) {
        return this.filterTextPacket(listString0, TextFilter::m_5925_);
    }

    @Override
    public void handlePlayerInput(ServerboundPlayerInputPacket serverboundPlayerInputPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundPlayerInputPacket0, this, this.player.serverLevel());
        this.player.setPlayerInput(serverboundPlayerInputPacket0.getXxa(), serverboundPlayerInputPacket0.getZza(), serverboundPlayerInputPacket0.isJumping(), serverboundPlayerInputPacket0.isShiftKeyDown());
    }

    private static boolean containsInvalidValues(double double0, double double1, double double2, float float3, float float4) {
        return Double.isNaN(double0) || Double.isNaN(double1) || Double.isNaN(double2) || !Floats.isFinite(float4) || !Floats.isFinite(float3);
    }

    private static double clampHorizontal(double double0) {
        return Mth.clamp(double0, -3.0E7, 3.0E7);
    }

    private static double clampVertical(double double0) {
        return Mth.clamp(double0, -2.0E7, 2.0E7);
    }

    @Override
    public void handleMoveVehicle(ServerboundMoveVehiclePacket serverboundMoveVehiclePacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundMoveVehiclePacket0, this, this.player.serverLevel());
        if (containsInvalidValues(serverboundMoveVehiclePacket0.getX(), serverboundMoveVehiclePacket0.getY(), serverboundMoveVehiclePacket0.getZ(), serverboundMoveVehiclePacket0.getYRot(), serverboundMoveVehiclePacket0.getXRot())) {
            this.disconnect(Component.translatable("multiplayer.disconnect.invalid_vehicle_movement"));
        } else {
            Entity $$1 = this.player.m_20201_();
            if ($$1 != this.player && $$1.getControllingPassenger() == this.player && $$1 == this.lastVehicle) {
                ServerLevel $$2 = this.player.serverLevel();
                double $$3 = $$1.getX();
                double $$4 = $$1.getY();
                double $$5 = $$1.getZ();
                double $$6 = clampHorizontal(serverboundMoveVehiclePacket0.getX());
                double $$7 = clampVertical(serverboundMoveVehiclePacket0.getY());
                double $$8 = clampHorizontal(serverboundMoveVehiclePacket0.getZ());
                float $$9 = Mth.wrapDegrees(serverboundMoveVehiclePacket0.getYRot());
                float $$10 = Mth.wrapDegrees(serverboundMoveVehiclePacket0.getXRot());
                double $$11 = $$6 - this.vehicleFirstGoodX;
                double $$12 = $$7 - this.vehicleFirstGoodY;
                double $$13 = $$8 - this.vehicleFirstGoodZ;
                double $$14 = $$1.getDeltaMovement().lengthSqr();
                double $$15 = $$11 * $$11 + $$12 * $$12 + $$13 * $$13;
                if ($$15 - $$14 > 100.0 && !this.isSingleplayerOwner()) {
                    LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", new Object[] { $$1.getName().getString(), this.player.m_7755_().getString(), $$11, $$12, $$13 });
                    this.connection.send(new ClientboundMoveVehiclePacket($$1));
                    return;
                }
                boolean $$16 = $$2.m_45756_($$1, $$1.getBoundingBox().deflate(0.0625));
                $$11 = $$6 - this.vehicleLastGoodX;
                $$12 = $$7 - this.vehicleLastGoodY - 1.0E-6;
                $$13 = $$8 - this.vehicleLastGoodZ;
                boolean $$17 = $$1.verticalCollisionBelow;
                if ($$1 instanceof LivingEntity $$18 && $$18.onClimbable()) {
                    $$18.m_183634_();
                }
                $$1.move(MoverType.PLAYER, new Vec3($$11, $$12, $$13));
                $$11 = $$6 - $$1.getX();
                $$12 = $$7 - $$1.getY();
                if ($$12 > -0.5 || $$12 < 0.5) {
                    $$12 = 0.0;
                }
                $$13 = $$8 - $$1.getZ();
                $$15 = $$11 * $$11 + $$12 * $$12 + $$13 * $$13;
                boolean $$20 = false;
                if ($$15 > 0.0625) {
                    $$20 = true;
                    LOGGER.warn("{} (vehicle of {}) moved wrongly! {}", new Object[] { $$1.getName().getString(), this.player.m_7755_().getString(), Math.sqrt($$15) });
                }
                $$1.absMoveTo($$6, $$7, $$8, $$9, $$10);
                boolean $$21 = $$2.m_45756_($$1, $$1.getBoundingBox().deflate(0.0625));
                if ($$16 && ($$20 || !$$21)) {
                    $$1.absMoveTo($$3, $$4, $$5, $$9, $$10);
                    this.connection.send(new ClientboundMoveVehiclePacket($$1));
                    return;
                }
                this.player.serverLevel().getChunkSource().move(this.player);
                this.player.m_36378_(this.player.m_20185_() - $$3, this.player.m_20186_() - $$4, this.player.m_20189_() - $$5);
                this.clientVehicleIsFloating = $$12 >= -0.03125 && !$$17 && !this.server.isFlightAllowed() && !$$1.isNoGravity() && this.noBlocksAround($$1);
                this.vehicleLastGoodX = $$1.getX();
                this.vehicleLastGoodY = $$1.getY();
                this.vehicleLastGoodZ = $$1.getZ();
            }
        }
    }

    private boolean noBlocksAround(Entity entity0) {
        return entity0.level().m_45556_(entity0.getBoundingBox().inflate(0.0625).expandTowards(0.0, -0.55, 0.0)).allMatch(BlockBehaviour.BlockStateBase::m_60795_);
    }

    @Override
    public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket serverboundAcceptTeleportationPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundAcceptTeleportationPacket0, this, this.player.serverLevel());
        if (serverboundAcceptTeleportationPacket0.getId() == this.awaitingTeleport) {
            if (this.awaitingPositionFromClient == null) {
                this.disconnect(Component.translatable("multiplayer.disconnect.invalid_player_movement"));
                return;
            }
            this.player.m_19890_(this.awaitingPositionFromClient.x, this.awaitingPositionFromClient.y, this.awaitingPositionFromClient.z, this.player.m_146908_(), this.player.m_146909_());
            this.lastGoodX = this.awaitingPositionFromClient.x;
            this.lastGoodY = this.awaitingPositionFromClient.y;
            this.lastGoodZ = this.awaitingPositionFromClient.z;
            if (this.player.isChangingDimension()) {
                this.player.hasChangedDimension();
            }
            this.awaitingPositionFromClient = null;
        }
    }

    @Override
    public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket serverboundRecipeBookSeenRecipePacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundRecipeBookSeenRecipePacket0, this, this.player.serverLevel());
        this.server.getRecipeManager().byKey(serverboundRecipeBookSeenRecipePacket0.getRecipe()).ifPresent(this.player.getRecipeBook()::m_12721_);
    }

    @Override
    public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket serverboundRecipeBookChangeSettingsPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundRecipeBookChangeSettingsPacket0, this, this.player.serverLevel());
        this.player.getRecipeBook().m_12696_(serverboundRecipeBookChangeSettingsPacket0.getBookType(), serverboundRecipeBookChangeSettingsPacket0.isOpen(), serverboundRecipeBookChangeSettingsPacket0.isFiltering());
    }

    @Override
    public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket serverboundSeenAdvancementsPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundSeenAdvancementsPacket0, this, this.player.serverLevel());
        if (serverboundSeenAdvancementsPacket0.getAction() == ServerboundSeenAdvancementsPacket.Action.OPENED_TAB) {
            ResourceLocation $$1 = serverboundSeenAdvancementsPacket0.getTab();
            Advancement $$2 = this.server.getAdvancements().getAdvancement($$1);
            if ($$2 != null) {
                this.player.getAdvancements().setSelectedTab($$2);
            }
        }
    }

    @Override
    public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket serverboundCommandSuggestionPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundCommandSuggestionPacket0, this, this.player.serverLevel());
        StringReader $$1 = new StringReader(serverboundCommandSuggestionPacket0.getCommand());
        if ($$1.canRead() && $$1.peek() == '/') {
            $$1.skip();
        }
        ParseResults<CommandSourceStack> $$2 = this.server.getCommands().getDispatcher().parse($$1, this.player.m_20203_());
        this.server.getCommands().getDispatcher().getCompletionSuggestions($$2).thenAccept(p_238197_ -> this.connection.send(new ClientboundCommandSuggestionsPacket(serverboundCommandSuggestionPacket0.getId(), p_238197_)));
    }

    @Override
    public void handleSetCommandBlock(ServerboundSetCommandBlockPacket serverboundSetCommandBlockPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundSetCommandBlockPacket0, this, this.player.serverLevel());
        if (!this.server.isCommandBlockEnabled()) {
            this.player.sendSystemMessage(Component.translatable("advMode.notEnabled"));
        } else if (!this.player.m_36337_()) {
            this.player.sendSystemMessage(Component.translatable("advMode.notAllowed"));
        } else {
            BaseCommandBlock $$1 = null;
            CommandBlockEntity $$2 = null;
            BlockPos $$3 = serverboundSetCommandBlockPacket0.getPos();
            BlockEntity $$4 = this.player.m_9236_().getBlockEntity($$3);
            if ($$4 instanceof CommandBlockEntity) {
                $$2 = (CommandBlockEntity) $$4;
                $$1 = $$2.getCommandBlock();
            }
            String $$5 = serverboundSetCommandBlockPacket0.getCommand();
            boolean $$6 = serverboundSetCommandBlockPacket0.isTrackOutput();
            if ($$1 != null) {
                CommandBlockEntity.Mode $$7 = $$2.getMode();
                BlockState $$8 = this.player.m_9236_().getBlockState($$3);
                Direction $$9 = (Direction) $$8.m_61143_(CommandBlock.FACING);
                BlockState $$13 = (BlockState) ((BlockState) (switch(serverboundSetCommandBlockPacket0.getMode()) {
                    case SEQUENCE ->
                        Blocks.CHAIN_COMMAND_BLOCK.defaultBlockState();
                    case AUTO ->
                        Blocks.REPEATING_COMMAND_BLOCK.defaultBlockState();
                    default ->
                        Blocks.COMMAND_BLOCK.defaultBlockState();
                }).m_61124_(CommandBlock.FACING, $$9)).m_61124_(CommandBlock.CONDITIONAL, serverboundSetCommandBlockPacket0.isConditional());
                if ($$13 != $$8) {
                    this.player.m_9236_().setBlock($$3, $$13, 2);
                    $$4.setBlockState($$13);
                    this.player.m_9236_().getChunkAt($$3).setBlockEntity($$4);
                }
                $$1.setCommand($$5);
                $$1.setTrackOutput($$6);
                if (!$$6) {
                    $$1.setLastOutput(null);
                }
                $$2.setAutomatic(serverboundSetCommandBlockPacket0.isAutomatic());
                if ($$7 != serverboundSetCommandBlockPacket0.getMode()) {
                    $$2.onModeSwitch();
                }
                $$1.onUpdated();
                if (!StringUtil.isNullOrEmpty($$5)) {
                    this.player.sendSystemMessage(Component.translatable("advMode.setCommand.success", $$5));
                }
            }
        }
    }

    @Override
    public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket serverboundSetCommandMinecartPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundSetCommandMinecartPacket0, this, this.player.serverLevel());
        if (!this.server.isCommandBlockEnabled()) {
            this.player.sendSystemMessage(Component.translatable("advMode.notEnabled"));
        } else if (!this.player.m_36337_()) {
            this.player.sendSystemMessage(Component.translatable("advMode.notAllowed"));
        } else {
            BaseCommandBlock $$1 = serverboundSetCommandMinecartPacket0.getCommandBlock(this.player.m_9236_());
            if ($$1 != null) {
                $$1.setCommand(serverboundSetCommandMinecartPacket0.getCommand());
                $$1.setTrackOutput(serverboundSetCommandMinecartPacket0.isTrackOutput());
                if (!serverboundSetCommandMinecartPacket0.isTrackOutput()) {
                    $$1.setLastOutput(null);
                }
                $$1.onUpdated();
                this.player.sendSystemMessage(Component.translatable("advMode.setCommand.success", serverboundSetCommandMinecartPacket0.getCommand()));
            }
        }
    }

    @Override
    public void handlePickItem(ServerboundPickItemPacket serverboundPickItemPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundPickItemPacket0, this, this.player.serverLevel());
        this.player.m_150109_().pickSlot(serverboundPickItemPacket0.getSlot());
        this.player.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, this.player.m_150109_().selected, this.player.m_150109_().getItem(this.player.m_150109_().selected)));
        this.player.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, serverboundPickItemPacket0.getSlot(), this.player.m_150109_().getItem(serverboundPickItemPacket0.getSlot())));
        this.player.connection.send(new ClientboundSetCarriedItemPacket(this.player.m_150109_().selected));
    }

    @Override
    public void handleRenameItem(ServerboundRenameItemPacket serverboundRenameItemPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundRenameItemPacket0, this, this.player.serverLevel());
        if (this.player.f_36096_ instanceof AnvilMenu $$1) {
            if (!$$1.m_6875_(this.player)) {
                LOGGER.debug("Player {} interacted with invalid menu {}", this.player, $$1);
                return;
            }
            $$1.setItemName(serverboundRenameItemPacket0.getName());
        }
    }

    @Override
    public void handleSetBeaconPacket(ServerboundSetBeaconPacket serverboundSetBeaconPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundSetBeaconPacket0, this, this.player.serverLevel());
        if (this.player.f_36096_ instanceof BeaconMenu $$1) {
            if (!this.player.f_36096_.stillValid(this.player)) {
                LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.f_36096_);
                return;
            }
            $$1.updateEffects(serverboundSetBeaconPacket0.getPrimary(), serverboundSetBeaconPacket0.getSecondary());
        }
    }

    @Override
    public void handleSetStructureBlock(ServerboundSetStructureBlockPacket serverboundSetStructureBlockPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundSetStructureBlockPacket0, this, this.player.serverLevel());
        if (this.player.m_36337_()) {
            BlockPos $$1 = serverboundSetStructureBlockPacket0.getPos();
            BlockState $$2 = this.player.m_9236_().getBlockState($$1);
            if (this.player.m_9236_().getBlockEntity($$1) instanceof StructureBlockEntity $$4) {
                $$4.setMode(serverboundSetStructureBlockPacket0.getMode());
                $$4.setStructureName(serverboundSetStructureBlockPacket0.getName());
                $$4.setStructurePos(serverboundSetStructureBlockPacket0.getOffset());
                $$4.setStructureSize(serverboundSetStructureBlockPacket0.getSize());
                $$4.setMirror(serverboundSetStructureBlockPacket0.getMirror());
                $$4.setRotation(serverboundSetStructureBlockPacket0.getRotation());
                $$4.setMetaData(serverboundSetStructureBlockPacket0.getData());
                $$4.setIgnoreEntities(serverboundSetStructureBlockPacket0.isIgnoreEntities());
                $$4.setShowAir(serverboundSetStructureBlockPacket0.isShowAir());
                $$4.setShowBoundingBox(serverboundSetStructureBlockPacket0.isShowBoundingBox());
                $$4.setIntegrity(serverboundSetStructureBlockPacket0.getIntegrity());
                $$4.setSeed(serverboundSetStructureBlockPacket0.getSeed());
                if ($$4.hasStructureName()) {
                    String $$5 = $$4.getStructureName();
                    if (serverboundSetStructureBlockPacket0.getUpdateType() == StructureBlockEntity.UpdateType.SAVE_AREA) {
                        if ($$4.saveStructure()) {
                            this.player.displayClientMessage(Component.translatable("structure_block.save_success", $$5), false);
                        } else {
                            this.player.displayClientMessage(Component.translatable("structure_block.save_failure", $$5), false);
                        }
                    } else if (serverboundSetStructureBlockPacket0.getUpdateType() == StructureBlockEntity.UpdateType.LOAD_AREA) {
                        if (!$$4.isStructureLoadable()) {
                            this.player.displayClientMessage(Component.translatable("structure_block.load_not_found", $$5), false);
                        } else if ($$4.loadStructure(this.player.serverLevel())) {
                            this.player.displayClientMessage(Component.translatable("structure_block.load_success", $$5), false);
                        } else {
                            this.player.displayClientMessage(Component.translatable("structure_block.load_prepare", $$5), false);
                        }
                    } else if (serverboundSetStructureBlockPacket0.getUpdateType() == StructureBlockEntity.UpdateType.SCAN_AREA) {
                        if ($$4.detectSize()) {
                            this.player.displayClientMessage(Component.translatable("structure_block.size_success", $$5), false);
                        } else {
                            this.player.displayClientMessage(Component.translatable("structure_block.size_failure"), false);
                        }
                    }
                } else {
                    this.player.displayClientMessage(Component.translatable("structure_block.invalid_structure_name", serverboundSetStructureBlockPacket0.getName()), false);
                }
                $$4.m_6596_();
                this.player.m_9236_().sendBlockUpdated($$1, $$2, $$2, 3);
            }
        }
    }

    @Override
    public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket serverboundSetJigsawBlockPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundSetJigsawBlockPacket0, this, this.player.serverLevel());
        if (this.player.m_36337_()) {
            BlockPos $$1 = serverboundSetJigsawBlockPacket0.getPos();
            BlockState $$2 = this.player.m_9236_().getBlockState($$1);
            if (this.player.m_9236_().getBlockEntity($$1) instanceof JigsawBlockEntity $$4) {
                $$4.setName(serverboundSetJigsawBlockPacket0.getName());
                $$4.setTarget(serverboundSetJigsawBlockPacket0.getTarget());
                $$4.setPool(ResourceKey.create(Registries.TEMPLATE_POOL, serverboundSetJigsawBlockPacket0.getPool()));
                $$4.setFinalState(serverboundSetJigsawBlockPacket0.getFinalState());
                $$4.setJoint(serverboundSetJigsawBlockPacket0.getJoint());
                $$4.m_6596_();
                this.player.m_9236_().sendBlockUpdated($$1, $$2, $$2, 3);
            }
        }
    }

    @Override
    public void handleJigsawGenerate(ServerboundJigsawGeneratePacket serverboundJigsawGeneratePacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundJigsawGeneratePacket0, this, this.player.serverLevel());
        if (this.player.m_36337_()) {
            BlockPos $$1 = serverboundJigsawGeneratePacket0.getPos();
            if (this.player.m_9236_().getBlockEntity($$1) instanceof JigsawBlockEntity $$3) {
                $$3.generate(this.player.serverLevel(), serverboundJigsawGeneratePacket0.levels(), serverboundJigsawGeneratePacket0.keepJigsaws());
            }
        }
    }

    @Override
    public void handleSelectTrade(ServerboundSelectTradePacket serverboundSelectTradePacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundSelectTradePacket0, this, this.player.serverLevel());
        int $$1 = serverboundSelectTradePacket0.getItem();
        if (this.player.f_36096_ instanceof MerchantMenu $$2) {
            if (!$$2.stillValid(this.player)) {
                LOGGER.debug("Player {} interacted with invalid menu {}", this.player, $$2);
                return;
            }
            $$2.setSelectionHint($$1);
            $$2.tryMoveItems($$1);
        }
    }

    @Override
    public void handleEditBook(ServerboundEditBookPacket serverboundEditBookPacket0) {
        int $$1 = serverboundEditBookPacket0.getSlot();
        if (Inventory.isHotbarSlot($$1) || $$1 == 40) {
            List<String> $$2 = Lists.newArrayList();
            Optional<String> $$3 = serverboundEditBookPacket0.getTitle();
            $$3.ifPresent($$2::add);
            serverboundEditBookPacket0.getPages().stream().limit(100L).forEach($$2::add);
            Consumer<List<FilteredText>> $$4 = $$3.isPresent() ? p_238198_ -> this.signBook((FilteredText) p_238198_.get(0), p_238198_.subList(1, p_238198_.size()), $$1) : p_143627_ -> this.updateBookContents(p_143627_, $$1);
            this.filterTextPacket($$2).thenAcceptAsync($$4, this.server);
        }
    }

    private void updateBookContents(List<FilteredText> listFilteredText0, int int1) {
        ItemStack $$2 = this.player.m_150109_().getItem(int1);
        if ($$2.is(Items.WRITABLE_BOOK)) {
            this.updateBookPages(listFilteredText0, UnaryOperator.identity(), $$2);
        }
    }

    private void signBook(FilteredText filteredText0, List<FilteredText> listFilteredText1, int int2) {
        ItemStack $$3 = this.player.m_150109_().getItem(int2);
        if ($$3.is(Items.WRITABLE_BOOK)) {
            ItemStack $$4 = new ItemStack(Items.WRITTEN_BOOK);
            CompoundTag $$5 = $$3.getTag();
            if ($$5 != null) {
                $$4.setTag($$5.copy());
            }
            $$4.addTagElement("author", StringTag.valueOf(this.player.m_7755_().getString()));
            if (this.player.isTextFilteringEnabled()) {
                $$4.addTagElement("title", StringTag.valueOf(filteredText0.filteredOrEmpty()));
            } else {
                $$4.addTagElement("filtered_title", StringTag.valueOf(filteredText0.filteredOrEmpty()));
                $$4.addTagElement("title", StringTag.valueOf(filteredText0.raw()));
            }
            this.updateBookPages(listFilteredText1, p_238206_ -> Component.Serializer.toJson(Component.literal(p_238206_)), $$4);
            this.player.m_150109_().setItem(int2, $$4);
        }
    }

    private void updateBookPages(List<FilteredText> listFilteredText0, UnaryOperator<String> unaryOperatorString1, ItemStack itemStack2) {
        ListTag $$3 = new ListTag();
        if (this.player.isTextFilteringEnabled()) {
            listFilteredText0.stream().map(p_238209_ -> StringTag.valueOf((String) unaryOperatorString1.apply(p_238209_.filteredOrEmpty()))).forEach($$3::add);
        } else {
            CompoundTag $$4 = new CompoundTag();
            int $$5 = 0;
            for (int $$6 = listFilteredText0.size(); $$5 < $$6; $$5++) {
                FilteredText $$7 = (FilteredText) listFilteredText0.get($$5);
                String $$8 = $$7.raw();
                $$3.add(StringTag.valueOf((String) unaryOperatorString1.apply($$8)));
                if ($$7.isFiltered()) {
                    $$4.putString(String.valueOf($$5), (String) unaryOperatorString1.apply($$7.filteredOrEmpty()));
                }
            }
            if (!$$4.isEmpty()) {
                itemStack2.addTagElement("filtered_pages", $$4);
            }
        }
        itemStack2.addTagElement("pages", $$3);
    }

    @Override
    public void handleEntityTagQuery(ServerboundEntityTagQuery serverboundEntityTagQuery0) {
        PacketUtils.ensureRunningOnSameThread(serverboundEntityTagQuery0, this, this.player.serverLevel());
        if (this.player.m_20310_(2)) {
            Entity $$1 = this.player.m_9236_().getEntity(serverboundEntityTagQuery0.getEntityId());
            if ($$1 != null) {
                CompoundTag $$2 = $$1.saveWithoutId(new CompoundTag());
                this.player.connection.send(new ClientboundTagQueryPacket(serverboundEntityTagQuery0.getTransactionId(), $$2));
            }
        }
    }

    @Override
    public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQuery serverboundBlockEntityTagQuery0) {
        PacketUtils.ensureRunningOnSameThread(serverboundBlockEntityTagQuery0, this, this.player.serverLevel());
        if (this.player.m_20310_(2)) {
            BlockEntity $$1 = this.player.m_9236_().getBlockEntity(serverboundBlockEntityTagQuery0.getPos());
            CompoundTag $$2 = $$1 != null ? $$1.saveWithoutMetadata() : null;
            this.player.connection.send(new ClientboundTagQueryPacket(serverboundBlockEntityTagQuery0.getTransactionId(), $$2));
        }
    }

    @Override
    public void handleMovePlayer(ServerboundMovePlayerPacket serverboundMovePlayerPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundMovePlayerPacket0, this, this.player.serverLevel());
        if (containsInvalidValues(serverboundMovePlayerPacket0.getX(0.0), serverboundMovePlayerPacket0.getY(0.0), serverboundMovePlayerPacket0.getZ(0.0), serverboundMovePlayerPacket0.getYRot(0.0F), serverboundMovePlayerPacket0.getXRot(0.0F))) {
            this.disconnect(Component.translatable("multiplayer.disconnect.invalid_player_movement"));
        } else {
            ServerLevel $$1 = this.player.serverLevel();
            if (!this.player.wonGame) {
                if (this.tickCount == 0) {
                    this.resetPosition();
                }
                if (this.awaitingPositionFromClient != null) {
                    if (this.tickCount - this.awaitingTeleportTime > 20) {
                        this.awaitingTeleportTime = this.tickCount;
                        this.teleport(this.awaitingPositionFromClient.x, this.awaitingPositionFromClient.y, this.awaitingPositionFromClient.z, this.player.m_146908_(), this.player.m_146909_());
                    }
                } else {
                    this.awaitingTeleportTime = this.tickCount;
                    double $$2 = clampHorizontal(serverboundMovePlayerPacket0.getX(this.player.m_20185_()));
                    double $$3 = clampVertical(serverboundMovePlayerPacket0.getY(this.player.m_20186_()));
                    double $$4 = clampHorizontal(serverboundMovePlayerPacket0.getZ(this.player.m_20189_()));
                    float $$5 = Mth.wrapDegrees(serverboundMovePlayerPacket0.getYRot(this.player.m_146908_()));
                    float $$6 = Mth.wrapDegrees(serverboundMovePlayerPacket0.getXRot(this.player.m_146909_()));
                    if (this.player.m_20159_()) {
                        this.player.m_19890_(this.player.m_20185_(), this.player.m_20186_(), this.player.m_20189_(), $$5, $$6);
                        this.player.serverLevel().getChunkSource().move(this.player);
                    } else {
                        double $$7 = this.player.m_20185_();
                        double $$8 = this.player.m_20186_();
                        double $$9 = this.player.m_20189_();
                        double $$10 = $$2 - this.firstGoodX;
                        double $$11 = $$3 - this.firstGoodY;
                        double $$12 = $$4 - this.firstGoodZ;
                        double $$13 = this.player.m_20184_().lengthSqr();
                        double $$14 = $$10 * $$10 + $$11 * $$11 + $$12 * $$12;
                        if (this.player.m_5803_()) {
                            if ($$14 > 1.0) {
                                this.teleport(this.player.m_20185_(), this.player.m_20186_(), this.player.m_20189_(), $$5, $$6);
                            }
                        } else {
                            this.receivedMovePacketCount++;
                            int $$15 = this.receivedMovePacketCount - this.knownMovePacketCount;
                            if ($$15 > 5) {
                                LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.m_7755_().getString(), $$15);
                                $$15 = 1;
                            }
                            if (!this.player.isChangingDimension() && (!this.player.m_9236_().getGameRules().getBoolean(GameRules.RULE_DISABLE_ELYTRA_MOVEMENT_CHECK) || !this.player.m_21255_())) {
                                float $$16 = this.player.m_21255_() ? 300.0F : 100.0F;
                                if ($$14 - $$13 > (double) ($$16 * (float) $$15) && !this.isSingleplayerOwner()) {
                                    LOGGER.warn("{} moved too quickly! {},{},{}", new Object[] { this.player.m_7755_().getString(), $$10, $$11, $$12 });
                                    this.teleport(this.player.m_20185_(), this.player.m_20186_(), this.player.m_20189_(), this.player.m_146908_(), this.player.m_146909_());
                                    return;
                                }
                            }
                            AABB $$17 = this.player.m_20191_();
                            $$10 = $$2 - this.lastGoodX;
                            $$11 = $$3 - this.lastGoodY;
                            $$12 = $$4 - this.lastGoodZ;
                            boolean $$18 = $$11 > 0.0;
                            if (this.player.m_20096_() && !serverboundMovePlayerPacket0.isOnGround() && $$18) {
                                this.player.m_6135_();
                            }
                            boolean $$19 = this.player.f_201939_;
                            this.player.m_6478_(MoverType.PLAYER, new Vec3($$10, $$11, $$12));
                            $$10 = $$2 - this.player.m_20185_();
                            $$11 = $$3 - this.player.m_20186_();
                            if ($$11 > -0.5 || $$11 < 0.5) {
                                $$11 = 0.0;
                            }
                            $$12 = $$4 - this.player.m_20189_();
                            $$14 = $$10 * $$10 + $$11 * $$11 + $$12 * $$12;
                            boolean $$21 = false;
                            if (!this.player.isChangingDimension() && $$14 > 0.0625 && !this.player.m_5803_() && !this.player.gameMode.isCreative() && this.player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
                                $$21 = true;
                                LOGGER.warn("{} moved wrongly!", this.player.m_7755_().getString());
                            }
                            if (this.player.f_19794_ || this.player.m_5803_() || (!$$21 || !$$1.m_45756_(this.player, $$17)) && !this.isPlayerCollidingWithAnythingNew($$1, $$17, $$2, $$3, $$4)) {
                                this.player.m_19890_($$2, $$3, $$4, $$5, $$6);
                                this.clientIsFloating = $$11 >= -0.03125 && !$$19 && this.player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR && !this.server.isFlightAllowed() && !this.player.m_150110_().mayfly && !this.player.m_21023_(MobEffects.LEVITATION) && !this.player.m_21255_() && !this.player.m_21209_() && this.noBlocksAround(this.player);
                                this.player.serverLevel().getChunkSource().move(this.player);
                                this.player.doCheckFallDamage(this.player.m_20185_() - $$7, this.player.m_20186_() - $$8, this.player.m_20189_() - $$9, serverboundMovePlayerPacket0.isOnGround());
                                this.player.m_289603_(serverboundMovePlayerPacket0.isOnGround(), new Vec3(this.player.m_20185_() - $$7, this.player.m_20186_() - $$8, this.player.m_20189_() - $$9));
                                if ($$18) {
                                    this.player.resetFallDistance();
                                }
                                this.player.m_36378_(this.player.m_20185_() - $$7, this.player.m_20186_() - $$8, this.player.m_20189_() - $$9);
                                this.lastGoodX = this.player.m_20185_();
                                this.lastGoodY = this.player.m_20186_();
                                this.lastGoodZ = this.player.m_20189_();
                            } else {
                                this.teleport($$7, $$8, $$9, $$5, $$6);
                                this.player.doCheckFallDamage(this.player.m_20185_() - $$7, this.player.m_20186_() - $$8, this.player.m_20189_() - $$9, serverboundMovePlayerPacket0.isOnGround());
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isPlayerCollidingWithAnythingNew(LevelReader levelReader0, AABB aABB1, double double2, double double3, double double4) {
        AABB $$5 = this.player.m_20191_().move(double2 - this.player.m_20185_(), double3 - this.player.m_20186_(), double4 - this.player.m_20189_());
        Iterable<VoxelShape> $$6 = levelReader0.m_186431_(this.player, $$5.deflate(1.0E-5F));
        VoxelShape $$7 = Shapes.create(aABB1.deflate(1.0E-5F));
        for (VoxelShape $$8 : $$6) {
            if (!Shapes.joinIsNotEmpty($$8, $$7, BooleanOp.AND)) {
                return true;
            }
        }
        return false;
    }

    public void teleport(double double0, double double1, double double2, float float3, float float4) {
        this.teleport(double0, double1, double2, float3, float4, Collections.emptySet());
    }

    public void teleport(double double0, double double1, double double2, float float3, float float4, Set<RelativeMovement> setRelativeMovement5) {
        double $$6 = setRelativeMovement5.contains(RelativeMovement.X) ? this.player.m_20185_() : 0.0;
        double $$7 = setRelativeMovement5.contains(RelativeMovement.Y) ? this.player.m_20186_() : 0.0;
        double $$8 = setRelativeMovement5.contains(RelativeMovement.Z) ? this.player.m_20189_() : 0.0;
        float $$9 = setRelativeMovement5.contains(RelativeMovement.Y_ROT) ? this.player.m_146908_() : 0.0F;
        float $$10 = setRelativeMovement5.contains(RelativeMovement.X_ROT) ? this.player.m_146909_() : 0.0F;
        this.awaitingPositionFromClient = new Vec3(double0, double1, double2);
        if (++this.awaitingTeleport == Integer.MAX_VALUE) {
            this.awaitingTeleport = 0;
        }
        this.awaitingTeleportTime = this.tickCount;
        this.player.m_19890_(double0, double1, double2, float3, float4);
        this.player.connection.send(new ClientboundPlayerPositionPacket(double0 - $$6, double1 - $$7, double2 - $$8, float3 - $$9, float4 - $$10, setRelativeMovement5, this.awaitingTeleport));
    }

    @Override
    public void handlePlayerAction(ServerboundPlayerActionPacket serverboundPlayerActionPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundPlayerActionPacket0, this, this.player.serverLevel());
        BlockPos $$1 = serverboundPlayerActionPacket0.getPos();
        this.player.resetLastActionTime();
        ServerboundPlayerActionPacket.Action $$2 = serverboundPlayerActionPacket0.getAction();
        switch($$2) {
            case SWAP_ITEM_WITH_OFFHAND:
                if (!this.player.isSpectator()) {
                    ItemStack $$3 = this.player.m_21120_(InteractionHand.OFF_HAND);
                    this.player.m_21008_(InteractionHand.OFF_HAND, this.player.m_21120_(InteractionHand.MAIN_HAND));
                    this.player.m_21008_(InteractionHand.MAIN_HAND, $$3);
                    this.player.m_5810_();
                }
                return;
            case DROP_ITEM:
                if (!this.player.isSpectator()) {
                    this.player.drop(false);
                }
                return;
            case DROP_ALL_ITEMS:
                if (!this.player.isSpectator()) {
                    this.player.drop(true);
                }
                return;
            case RELEASE_USE_ITEM:
                this.player.m_21253_();
                return;
            case START_DESTROY_BLOCK:
            case ABORT_DESTROY_BLOCK:
            case STOP_DESTROY_BLOCK:
                this.player.gameMode.handleBlockBreakAction($$1, $$2, serverboundPlayerActionPacket0.getDirection(), this.player.m_9236_().m_151558_(), serverboundPlayerActionPacket0.getSequence());
                this.player.connection.ackBlockChangesUpTo(serverboundPlayerActionPacket0.getSequence());
                return;
            default:
                throw new IllegalArgumentException("Invalid player action");
        }
    }

    private static boolean wasBlockPlacementAttempt(ServerPlayer serverPlayer0, ItemStack itemStack1) {
        if (itemStack1.isEmpty()) {
            return false;
        } else {
            Item $$2 = itemStack1.getItem();
            return ($$2 instanceof BlockItem || $$2 instanceof BucketItem) && !serverPlayer0.m_36335_().isOnCooldown($$2);
        }
    }

    @Override
    public void handleUseItemOn(ServerboundUseItemOnPacket serverboundUseItemOnPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundUseItemOnPacket0, this, this.player.serverLevel());
        this.player.connection.ackBlockChangesUpTo(serverboundUseItemOnPacket0.getSequence());
        ServerLevel $$1 = this.player.serverLevel();
        InteractionHand $$2 = serverboundUseItemOnPacket0.getHand();
        ItemStack $$3 = this.player.m_21120_($$2);
        if ($$3.isItemEnabled($$1.enabledFeatures())) {
            BlockHitResult $$4 = serverboundUseItemOnPacket0.getHitResult();
            Vec3 $$5 = $$4.m_82450_();
            BlockPos $$6 = $$4.getBlockPos();
            Vec3 $$7 = Vec3.atCenterOf($$6);
            if (!(this.player.m_146892_().distanceToSqr($$7) > MAX_INTERACTION_DISTANCE)) {
                Vec3 $$8 = $$5.subtract($$7);
                double $$9 = 1.0000001;
                if (Math.abs($$8.x()) < 1.0000001 && Math.abs($$8.y()) < 1.0000001 && Math.abs($$8.z()) < 1.0000001) {
                    Direction $$10 = $$4.getDirection();
                    this.player.resetLastActionTime();
                    int $$11 = this.player.m_9236_().m_151558_();
                    if ($$6.m_123342_() < $$11) {
                        if (this.awaitingPositionFromClient == null && this.player.m_20275_((double) $$6.m_123341_() + 0.5, (double) $$6.m_123342_() + 0.5, (double) $$6.m_123343_() + 0.5) < 64.0 && $$1.mayInteract(this.player, $$6)) {
                            InteractionResult $$12 = this.player.gameMode.useItemOn(this.player, $$1, $$3, $$2, $$4);
                            if ($$10 == Direction.UP && !$$12.consumesAction() && $$6.m_123342_() >= $$11 - 1 && wasBlockPlacementAttempt(this.player, $$3)) {
                                Component $$13 = Component.translatable("build.tooHigh", $$11 - 1).withStyle(ChatFormatting.RED);
                                this.player.sendSystemMessage($$13, true);
                            } else if ($$12.shouldSwing()) {
                                this.player.m_21011_($$2, true);
                            }
                        }
                    } else {
                        Component $$14 = Component.translatable("build.tooHigh", $$11 - 1).withStyle(ChatFormatting.RED);
                        this.player.sendSystemMessage($$14, true);
                    }
                    this.player.connection.send(new ClientboundBlockUpdatePacket($$1, $$6));
                    this.player.connection.send(new ClientboundBlockUpdatePacket($$1, $$6.relative($$10)));
                } else {
                    LOGGER.warn("Rejecting UseItemOnPacket from {}: Location {} too far away from hit block {}.", new Object[] { this.player.m_36316_().getName(), $$5, $$6 });
                }
            }
        }
    }

    @Override
    public void handleUseItem(ServerboundUseItemPacket serverboundUseItemPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundUseItemPacket0, this, this.player.serverLevel());
        this.ackBlockChangesUpTo(serverboundUseItemPacket0.getSequence());
        ServerLevel $$1 = this.player.serverLevel();
        InteractionHand $$2 = serverboundUseItemPacket0.getHand();
        ItemStack $$3 = this.player.m_21120_($$2);
        this.player.resetLastActionTime();
        if (!$$3.isEmpty() && $$3.isItemEnabled($$1.enabledFeatures())) {
            InteractionResult $$4 = this.player.gameMode.useItem(this.player, $$1, $$3, $$2);
            if ($$4.shouldSwing()) {
                this.player.m_21011_($$2, true);
            }
        }
    }

    @Override
    public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket serverboundTeleportToEntityPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundTeleportToEntityPacket0, this, this.player.serverLevel());
        if (this.player.isSpectator()) {
            for (ServerLevel $$1 : this.server.getAllLevels()) {
                Entity $$2 = serverboundTeleportToEntityPacket0.getEntity($$1);
                if ($$2 != null) {
                    this.player.teleportTo($$1, $$2.getX(), $$2.getY(), $$2.getZ(), $$2.getYRot(), $$2.getXRot());
                    return;
                }
            }
        }
    }

    @Override
    public void handleResourcePackResponse(ServerboundResourcePackPacket serverboundResourcePackPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundResourcePackPacket0, this, this.player.serverLevel());
        if (serverboundResourcePackPacket0.getAction() == ServerboundResourcePackPacket.Action.DECLINED && this.server.isResourcePackRequired()) {
            LOGGER.info("Disconnecting {} due to resource pack rejection", this.player.m_7755_());
            this.disconnect(Component.translatable("multiplayer.requiredTexturePrompt.disconnect"));
        }
    }

    @Override
    public void handlePaddleBoat(ServerboundPaddleBoatPacket serverboundPaddleBoatPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundPaddleBoatPacket0, this, this.player.serverLevel());
        if (this.player.m_275832_() instanceof Boat $$2) {
            $$2.setPaddleState(serverboundPaddleBoatPacket0.getLeft(), serverboundPaddleBoatPacket0.getRight());
        }
    }

    @Override
    public void handlePong(ServerboundPongPacket serverboundPongPacket0) {
    }

    @Override
    public void onDisconnect(Component component0) {
        this.chatMessageChain.close();
        LOGGER.info("{} lost connection: {}", this.player.m_7755_().getString(), component0.getString());
        this.server.invalidateStatus();
        this.server.getPlayerList().broadcastSystemMessage(Component.translatable("multiplayer.player.left", this.player.m_5446_()).withStyle(ChatFormatting.YELLOW), false);
        this.player.disconnect();
        this.server.getPlayerList().remove(this.player);
        this.player.getTextFilter().leave();
        if (this.isSingleplayerOwner()) {
            LOGGER.info("Stopping singleplayer server as player logged out");
            this.server.halt(false);
        }
    }

    public void ackBlockChangesUpTo(int int0) {
        if (int0 < 0) {
            throw new IllegalArgumentException("Expected packet sequence nr >= 0");
        } else {
            this.ackBlockChangesUpTo = Math.max(int0, this.ackBlockChangesUpTo);
        }
    }

    @Override
    public void send(Packet<?> packet0) {
        this.send(packet0, null);
    }

    public void send(Packet<?> packet0, @Nullable PacketSendListener packetSendListener1) {
        try {
            this.connection.send(packet0, packetSendListener1);
        } catch (Throwable var6) {
            CrashReport $$3 = CrashReport.forThrowable(var6, "Sending packet");
            CrashReportCategory $$4 = $$3.addCategory("Packet being sent");
            $$4.setDetail("Packet class", (CrashReportDetail<String>) (() -> packet0.getClass().getCanonicalName()));
            throw new ReportedException($$3);
        }
    }

    @Override
    public void handleSetCarriedItem(ServerboundSetCarriedItemPacket serverboundSetCarriedItemPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundSetCarriedItemPacket0, this, this.player.serverLevel());
        if (serverboundSetCarriedItemPacket0.getSlot() >= 0 && serverboundSetCarriedItemPacket0.getSlot() < Inventory.getSelectionSize()) {
            if (this.player.m_150109_().selected != serverboundSetCarriedItemPacket0.getSlot() && this.player.m_7655_() == InteractionHand.MAIN_HAND) {
                this.player.m_5810_();
            }
            this.player.m_150109_().selected = serverboundSetCarriedItemPacket0.getSlot();
            this.player.resetLastActionTime();
        } else {
            LOGGER.warn("{} tried to set an invalid carried item", this.player.m_7755_().getString());
        }
    }

    @Override
    public void handleChat(ServerboundChatPacket serverboundChatPacket0) {
        if (isChatMessageIllegal(serverboundChatPacket0.message())) {
            this.disconnect(Component.translatable("multiplayer.disconnect.illegal_characters"));
        } else {
            Optional<LastSeenMessages> $$1 = this.tryHandleChat(serverboundChatPacket0.message(), serverboundChatPacket0.timeStamp(), serverboundChatPacket0.lastSeenMessages());
            if ($$1.isPresent()) {
                this.server.m_18707_(() -> {
                    PlayerChatMessage $$2;
                    try {
                        $$2 = this.getSignedMessage(serverboundChatPacket0, (LastSeenMessages) $$1.get());
                    } catch (SignedMessageChain.DecodeException var6) {
                        this.handleMessageDecodeFailure(var6);
                        return;
                    }
                    CompletableFuture<FilteredText> $$5 = this.filterTextPacket($$2.signedContent());
                    CompletableFuture<Component> $$6 = this.server.getChatDecorator().decorate(this.player, $$2.decoratedContent());
                    this.chatMessageChain.append(p_248212_ -> CompletableFuture.allOf($$5, $$6).thenAcceptAsync(p_248218_ -> {
                        PlayerChatMessage $$4x = $$2.withUnsignedContent((Component) $$6.join()).filter(((FilteredText) $$5.join()).mask());
                        this.broadcastChatMessage($$4x);
                    }, p_248212_));
                });
            }
        }
    }

    @Override
    public void handleChatCommand(ServerboundChatCommandPacket serverboundChatCommandPacket0) {
        if (isChatMessageIllegal(serverboundChatCommandPacket0.command())) {
            this.disconnect(Component.translatable("multiplayer.disconnect.illegal_characters"));
        } else {
            Optional<LastSeenMessages> $$1 = this.tryHandleChat(serverboundChatCommandPacket0.command(), serverboundChatCommandPacket0.timeStamp(), serverboundChatCommandPacket0.lastSeenMessages());
            if ($$1.isPresent()) {
                this.server.m_18707_(() -> {
                    this.performChatCommand(serverboundChatCommandPacket0, (LastSeenMessages) $$1.get());
                    this.detectRateSpam();
                });
            }
        }
    }

    private void performChatCommand(ServerboundChatCommandPacket serverboundChatCommandPacket0, LastSeenMessages lastSeenMessages1) {
        ParseResults<CommandSourceStack> $$2 = this.parseCommand(serverboundChatCommandPacket0.command());
        Map<String, PlayerChatMessage> $$3;
        try {
            $$3 = this.collectSignedArguments(serverboundChatCommandPacket0, SignableCommand.of($$2), lastSeenMessages1);
        } catch (SignedMessageChain.DecodeException var6) {
            this.handleMessageDecodeFailure(var6);
            return;
        }
        CommandSigningContext $$6 = new CommandSigningContext.SignedArguments($$3);
        $$2 = Commands.mapSource($$2, p_242749_ -> p_242749_.withSigningContext($$6));
        this.server.getCommands().performCommand($$2, serverboundChatCommandPacket0.command());
    }

    private void handleMessageDecodeFailure(SignedMessageChain.DecodeException signedMessageChainDecodeException0) {
        if (signedMessageChainDecodeException0.shouldDisconnect()) {
            this.disconnect(signedMessageChainDecodeException0.m_237308_());
        } else {
            this.player.sendSystemMessage(signedMessageChainDecodeException0.m_237308_().copy().withStyle(ChatFormatting.RED));
        }
    }

    private Map<String, PlayerChatMessage> collectSignedArguments(ServerboundChatCommandPacket serverboundChatCommandPacket0, SignableCommand<?> signableCommand1, LastSeenMessages lastSeenMessages2) throws SignedMessageChain.DecodeException {
        Map<String, PlayerChatMessage> $$3 = new Object2ObjectOpenHashMap();
        for (SignableCommand.Argument<?> $$4 : signableCommand1.arguments()) {
            MessageSignature $$5 = serverboundChatCommandPacket0.argumentSignatures().get($$4.name());
            SignedMessageBody $$6 = new SignedMessageBody($$4.value(), serverboundChatCommandPacket0.timeStamp(), serverboundChatCommandPacket0.salt(), lastSeenMessages2);
            $$3.put($$4.name(), this.signedMessageDecoder.unpack($$5, $$6));
        }
        return $$3;
    }

    private ParseResults<CommandSourceStack> parseCommand(String string0) {
        CommandDispatcher<CommandSourceStack> $$1 = this.server.getCommands().getDispatcher();
        return $$1.parse(string0, this.player.m_20203_());
    }

    private Optional<LastSeenMessages> tryHandleChat(String string0, Instant instant1, LastSeenMessages.Update lastSeenMessagesUpdate2) {
        if (!this.updateChatOrder(instant1)) {
            LOGGER.warn("{} sent out-of-order chat: '{}'", this.player.m_7755_().getString(), string0);
            this.disconnect(Component.translatable("multiplayer.disconnect.out_of_order_chat"));
            return Optional.empty();
        } else {
            Optional<LastSeenMessages> $$3 = this.unpackAndApplyLastSeen(lastSeenMessagesUpdate2);
            if (this.player.getChatVisibility() == ChatVisiblity.HIDDEN) {
                this.send(new ClientboundSystemChatPacket(Component.translatable("chat.disabled.options").withStyle(ChatFormatting.RED), false));
                return Optional.empty();
            } else {
                this.player.resetLastActionTime();
                return $$3;
            }
        }
    }

    private Optional<LastSeenMessages> unpackAndApplyLastSeen(LastSeenMessages.Update lastSeenMessagesUpdate0) {
        synchronized (this.lastSeenMessages) {
            Optional<LastSeenMessages> $$1 = this.lastSeenMessages.applyUpdate(lastSeenMessagesUpdate0);
            if ($$1.isEmpty()) {
                LOGGER.warn("Failed to validate message acknowledgements from {}", this.player.m_7755_().getString());
                this.disconnect(CHAT_VALIDATION_FAILED);
            }
            return $$1;
        }
    }

    private boolean updateChatOrder(Instant instant0) {
        Instant $$1;
        do {
            $$1 = (Instant) this.lastChatTimeStamp.get();
            if (instant0.isBefore($$1)) {
                return false;
            }
        } while (!this.lastChatTimeStamp.compareAndSet($$1, instant0));
        return true;
    }

    private static boolean isChatMessageIllegal(String string0) {
        for (int $$1 = 0; $$1 < string0.length(); $$1++) {
            if (!SharedConstants.isAllowedChatCharacter(string0.charAt($$1))) {
                return true;
            }
        }
        return false;
    }

    private PlayerChatMessage getSignedMessage(ServerboundChatPacket serverboundChatPacket0, LastSeenMessages lastSeenMessages1) throws SignedMessageChain.DecodeException {
        SignedMessageBody $$2 = new SignedMessageBody(serverboundChatPacket0.message(), serverboundChatPacket0.timeStamp(), serverboundChatPacket0.salt(), lastSeenMessages1);
        return this.signedMessageDecoder.unpack(serverboundChatPacket0.signature(), $$2);
    }

    private void broadcastChatMessage(PlayerChatMessage playerChatMessage0) {
        this.server.getPlayerList().broadcastChatMessage(playerChatMessage0, this.player, ChatType.bind(ChatType.CHAT, this.player));
        this.detectRateSpam();
    }

    private void detectRateSpam() {
        this.chatSpamTickCount += 20;
        if (this.chatSpamTickCount > 200 && !this.server.getPlayerList().isOp(this.player.m_36316_())) {
            this.disconnect(Component.translatable("disconnect.spam"));
        }
    }

    @Override
    public void handleChatAck(ServerboundChatAckPacket serverboundChatAckPacket0) {
        synchronized (this.lastSeenMessages) {
            if (!this.lastSeenMessages.applyOffset(serverboundChatAckPacket0.offset())) {
                LOGGER.warn("Failed to validate message acknowledgements from {}", this.player.m_7755_().getString());
                this.disconnect(CHAT_VALIDATION_FAILED);
            }
        }
    }

    @Override
    public void handleAnimate(ServerboundSwingPacket serverboundSwingPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundSwingPacket0, this, this.player.serverLevel());
        this.player.resetLastActionTime();
        this.player.swing(serverboundSwingPacket0.getHand());
    }

    @Override
    public void handlePlayerCommand(ServerboundPlayerCommandPacket serverboundPlayerCommandPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundPlayerCommandPacket0, this, this.player.serverLevel());
        this.player.resetLastActionTime();
        switch(serverboundPlayerCommandPacket0.getAction()) {
            case PRESS_SHIFT_KEY:
                this.player.m_20260_(true);
                break;
            case RELEASE_SHIFT_KEY:
                this.player.m_20260_(false);
                break;
            case START_SPRINTING:
                this.player.m_6858_(true);
                break;
            case STOP_SPRINTING:
                this.player.m_6858_(false);
                break;
            case STOP_SLEEPING:
                if (this.player.m_5803_()) {
                    this.player.stopSleepInBed(false, true);
                    this.awaitingPositionFromClient = this.player.m_20182_();
                }
                break;
            case START_RIDING_JUMP:
                if (this.player.m_275832_() instanceof PlayerRideableJumping $$1) {
                    int $$2 = serverboundPlayerCommandPacket0.getData();
                    if ($$1.canJump() && $$2 > 0) {
                        $$1.handleStartJump($$2);
                    }
                }
                break;
            case STOP_RIDING_JUMP:
                if (this.player.m_275832_() instanceof PlayerRideableJumping $$3) {
                    $$3.handleStopJump();
                }
                break;
            case OPEN_INVENTORY:
                if (this.player.m_20202_() instanceof HasCustomInventoryScreen $$4) {
                    $$4.openCustomInventoryScreen(this.player);
                }
                break;
            case START_FALL_FLYING:
                if (!this.player.m_36319_()) {
                    this.player.m_36321_();
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid client command!");
        }
    }

    public void addPendingMessage(PlayerChatMessage playerChatMessage0) {
        MessageSignature $$1 = playerChatMessage0.signature();
        if ($$1 != null) {
            this.messageSignatureCache.push(playerChatMessage0);
            int $$2;
            synchronized (this.lastSeenMessages) {
                this.lastSeenMessages.addPending($$1);
                $$2 = this.lastSeenMessages.trackedMessagesCount();
            }
            if ($$2 > 4096) {
                this.disconnect(Component.translatable("multiplayer.disconnect.too_many_pending_chats"));
            }
        }
    }

    public void sendPlayerChatMessage(PlayerChatMessage playerChatMessage0, ChatType.Bound chatTypeBound1) {
        this.send(new ClientboundPlayerChatPacket(playerChatMessage0.link().sender(), playerChatMessage0.link().index(), playerChatMessage0.signature(), playerChatMessage0.signedBody().pack(this.messageSignatureCache), playerChatMessage0.unsignedContent(), playerChatMessage0.filterMask(), chatTypeBound1.toNetwork(this.player.m_9236_().registryAccess())));
        this.addPendingMessage(playerChatMessage0);
    }

    public void sendDisguisedChatMessage(Component component0, ChatType.Bound chatTypeBound1) {
        this.send(new ClientboundDisguisedChatPacket(component0, chatTypeBound1.toNetwork(this.player.m_9236_().registryAccess())));
    }

    public SocketAddress getRemoteAddress() {
        return this.connection.getRemoteAddress();
    }

    @Override
    public void handleInteract(ServerboundInteractPacket serverboundInteractPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundInteractPacket0, this, this.player.serverLevel());
        final ServerLevel $$1 = this.player.serverLevel();
        final Entity $$2 = serverboundInteractPacket0.getTarget($$1);
        this.player.resetLastActionTime();
        this.player.m_20260_(serverboundInteractPacket0.isUsingSecondaryAction());
        if ($$2 != null) {
            if (!$$1.m_6857_().isWithinBounds($$2.blockPosition())) {
                return;
            }
            AABB $$3 = $$2.getBoundingBox();
            if ($$3.distanceToSqr(this.player.m_146892_()) < MAX_INTERACTION_DISTANCE) {
                serverboundInteractPacket0.dispatch(new ServerboundInteractPacket.Handler() {

                    private void performInteraction(InteractionHand p_143679_, ServerGamePacketListenerImpl.EntityInteraction p_143680_) {
                        ItemStack $$2 = ServerGamePacketListenerImpl.this.player.m_21120_(p_143679_);
                        if ($$2.isItemEnabled($$1.enabledFeatures())) {
                            ItemStack $$3 = $$2.copy();
                            InteractionResult $$4 = p_143680_.run(ServerGamePacketListenerImpl.this.player, $$2, p_143679_);
                            if ($$4.consumesAction()) {
                                CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(ServerGamePacketListenerImpl.this.player, $$3, $$2);
                                if ($$4.shouldSwing()) {
                                    ServerGamePacketListenerImpl.this.player.m_21011_(p_143679_, true);
                                }
                            }
                        }
                    }

                    @Override
                    public void onInteraction(InteractionHand p_143677_) {
                        this.performInteraction(p_143677_, Player::m_36157_);
                    }

                    @Override
                    public void onInteraction(InteractionHand p_143682_, Vec3 p_143683_) {
                        this.performInteraction(p_143682_, (p_143686_, p_143687_, p_143688_) -> p_143687_.interactAt(p_143686_, p_143683_, p_143688_));
                    }

                    @Override
                    public void onAttack() {
                        if (!($$2 instanceof ItemEntity) && !($$2 instanceof ExperienceOrb) && !($$2 instanceof AbstractArrow) && $$2 != ServerGamePacketListenerImpl.this.player) {
                            ItemStack $$0 = ServerGamePacketListenerImpl.this.player.m_21120_(InteractionHand.MAIN_HAND);
                            if ($$0.isItemEnabled($$1.enabledFeatures())) {
                                ServerGamePacketListenerImpl.this.player.attack($$2);
                            }
                        } else {
                            ServerGamePacketListenerImpl.this.disconnect(Component.translatable("multiplayer.disconnect.invalid_entity_attacked"));
                            ServerGamePacketListenerImpl.LOGGER.warn("Player {} tried to attack an invalid entity", ServerGamePacketListenerImpl.this.player.m_7755_().getString());
                        }
                    }
                });
            }
        }
    }

    @Override
    public void handleClientCommand(ServerboundClientCommandPacket serverboundClientCommandPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundClientCommandPacket0, this, this.player.serverLevel());
        this.player.resetLastActionTime();
        ServerboundClientCommandPacket.Action $$1 = serverboundClientCommandPacket0.getAction();
        switch($$1) {
            case PERFORM_RESPAWN:
                if (this.player.wonGame) {
                    this.player.wonGame = false;
                    this.player = this.server.getPlayerList().respawn(this.player, true);
                    CriteriaTriggers.CHANGED_DIMENSION.trigger(this.player, Level.END, Level.OVERWORLD);
                } else {
                    if (this.player.m_21223_() > 0.0F) {
                        return;
                    }
                    this.player = this.server.getPlayerList().respawn(this.player, false);
                    if (this.server.isHardcore()) {
                        this.player.setGameMode(GameType.SPECTATOR);
                        this.player.m_9236_().getGameRules().getRule(GameRules.RULE_SPECTATORSGENERATECHUNKS).set(false, this.server);
                    }
                }
                break;
            case REQUEST_STATS:
                this.player.getStats().sendStats(this.player);
        }
    }

    @Override
    public void handleContainerClose(ServerboundContainerClosePacket serverboundContainerClosePacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundContainerClosePacket0, this, this.player.serverLevel());
        this.player.doCloseContainer();
    }

    @Override
    public void handleContainerClick(ServerboundContainerClickPacket serverboundContainerClickPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundContainerClickPacket0, this, this.player.serverLevel());
        this.player.resetLastActionTime();
        if (this.player.f_36096_.containerId == serverboundContainerClickPacket0.getContainerId()) {
            if (this.player.isSpectator()) {
                this.player.f_36096_.sendAllDataToRemote();
            } else if (!this.player.f_36096_.stillValid(this.player)) {
                LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.f_36096_);
            } else {
                int $$1 = serverboundContainerClickPacket0.getSlotNum();
                if (!this.player.f_36096_.isValidSlotIndex($$1)) {
                    LOGGER.debug("Player {} clicked invalid slot index: {}, available slots: {}", new Object[] { this.player.m_7755_(), $$1, this.player.f_36096_.slots.size() });
                } else {
                    boolean $$2 = serverboundContainerClickPacket0.getStateId() != this.player.f_36096_.getStateId();
                    this.player.f_36096_.suppressRemoteUpdates();
                    this.player.f_36096_.clicked($$1, serverboundContainerClickPacket0.getButtonNum(), serverboundContainerClickPacket0.getClickType(), this.player);
                    ObjectIterator var4 = Int2ObjectMaps.fastIterable(serverboundContainerClickPacket0.getChangedSlots()).iterator();
                    while (var4.hasNext()) {
                        Entry<ItemStack> $$3 = (Entry<ItemStack>) var4.next();
                        this.player.f_36096_.setRemoteSlotNoCopy($$3.getIntKey(), (ItemStack) $$3.getValue());
                    }
                    this.player.f_36096_.setRemoteCarried(serverboundContainerClickPacket0.getCarriedItem());
                    this.player.f_36096_.resumeRemoteUpdates();
                    if ($$2) {
                        this.player.f_36096_.broadcastFullState();
                    } else {
                        this.player.f_36096_.broadcastChanges();
                    }
                }
            }
        }
    }

    @Override
    public void handlePlaceRecipe(ServerboundPlaceRecipePacket serverboundPlaceRecipePacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundPlaceRecipePacket0, this, this.player.serverLevel());
        this.player.resetLastActionTime();
        if (!this.player.isSpectator() && this.player.f_36096_.containerId == serverboundPlaceRecipePacket0.getContainerId() && this.player.f_36096_ instanceof RecipeBookMenu) {
            if (!this.player.f_36096_.stillValid(this.player)) {
                LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.f_36096_);
            } else {
                this.server.getRecipeManager().byKey(serverboundPlaceRecipePacket0.getRecipe()).ifPresent(p_287379_ -> ((RecipeBookMenu) this.player.f_36096_).handlePlacement(serverboundPlaceRecipePacket0.isShiftDown(), p_287379_, this.player));
            }
        }
    }

    @Override
    public void handleContainerButtonClick(ServerboundContainerButtonClickPacket serverboundContainerButtonClickPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundContainerButtonClickPacket0, this, this.player.serverLevel());
        this.player.resetLastActionTime();
        if (this.player.f_36096_.containerId == serverboundContainerButtonClickPacket0.getContainerId() && !this.player.isSpectator()) {
            if (!this.player.f_36096_.stillValid(this.player)) {
                LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.f_36096_);
            } else {
                boolean $$1 = this.player.f_36096_.clickMenuButton(this.player, serverboundContainerButtonClickPacket0.getButtonId());
                if ($$1) {
                    this.player.f_36096_.broadcastChanges();
                }
            }
        }
    }

    @Override
    public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket serverboundSetCreativeModeSlotPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundSetCreativeModeSlotPacket0, this, this.player.serverLevel());
        if (this.player.gameMode.isCreative()) {
            boolean $$1 = serverboundSetCreativeModeSlotPacket0.getSlotNum() < 0;
            ItemStack $$2 = serverboundSetCreativeModeSlotPacket0.getItem();
            if (!$$2.isItemEnabled(this.player.m_9236_().m_246046_())) {
                return;
            }
            CompoundTag $$3 = BlockItem.getBlockEntityData($$2);
            if (!$$2.isEmpty() && $$3 != null && $$3.contains("x") && $$3.contains("y") && $$3.contains("z")) {
                BlockPos $$4 = BlockEntity.getPosFromTag($$3);
                if (this.player.m_9236_().isLoaded($$4)) {
                    BlockEntity $$5 = this.player.m_9236_().getBlockEntity($$4);
                    if ($$5 != null) {
                        $$5.saveToItem($$2);
                    }
                }
            }
            boolean $$6 = serverboundSetCreativeModeSlotPacket0.getSlotNum() >= 1 && serverboundSetCreativeModeSlotPacket0.getSlotNum() <= 45;
            boolean $$7 = $$2.isEmpty() || $$2.getDamageValue() >= 0 && $$2.getCount() <= 64 && !$$2.isEmpty();
            if ($$6 && $$7) {
                this.player.f_36095_.m_38853_(serverboundSetCreativeModeSlotPacket0.getSlotNum()).setByPlayer($$2);
                this.player.f_36095_.m_38946_();
            } else if ($$1 && $$7 && this.dropSpamTickCount < 200) {
                this.dropSpamTickCount += 20;
                this.player.m_36176_($$2, true);
            }
        }
    }

    @Override
    public void handleSignUpdate(ServerboundSignUpdatePacket serverboundSignUpdatePacket0) {
        List<String> $$1 = (List<String>) Stream.of(serverboundSignUpdatePacket0.getLines()).map(ChatFormatting::m_126649_).collect(Collectors.toList());
        this.filterTextPacket($$1).thenAcceptAsync(p_215245_ -> this.updateSignText(serverboundSignUpdatePacket0, p_215245_), this.server);
    }

    private void updateSignText(ServerboundSignUpdatePacket serverboundSignUpdatePacket0, List<FilteredText> listFilteredText1) {
        this.player.resetLastActionTime();
        ServerLevel $$2 = this.player.serverLevel();
        BlockPos $$3 = serverboundSignUpdatePacket0.getPos();
        if ($$2.m_46805_($$3)) {
            if (!($$2.m_7702_($$3) instanceof SignBlockEntity $$5)) {
                return;
            }
            $$5.updateSignText(this.player, serverboundSignUpdatePacket0.isFrontText(), listFilteredText1);
        }
    }

    @Override
    public void handleKeepAlive(ServerboundKeepAlivePacket serverboundKeepAlivePacket0) {
        if (this.keepAlivePending && serverboundKeepAlivePacket0.getId() == this.keepAliveChallenge) {
            int $$1 = (int) (Util.getMillis() - this.keepAliveTime);
            this.player.latency = (this.player.latency * 3 + $$1) / 4;
            this.keepAlivePending = false;
        } else if (!this.isSingleplayerOwner()) {
            this.disconnect(Component.translatable("disconnect.timeout"));
        }
    }

    @Override
    public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket serverboundPlayerAbilitiesPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundPlayerAbilitiesPacket0, this, this.player.serverLevel());
        this.player.m_150110_().flying = serverboundPlayerAbilitiesPacket0.isFlying() && this.player.m_150110_().mayfly;
    }

    @Override
    public void handleClientInformation(ServerboundClientInformationPacket serverboundClientInformationPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundClientInformationPacket0, this, this.player.serverLevel());
        this.player.updateOptions(serverboundClientInformationPacket0);
    }

    @Override
    public void handleCustomPayload(ServerboundCustomPayloadPacket serverboundCustomPayloadPacket0) {
    }

    @Override
    public void handleChangeDifficulty(ServerboundChangeDifficultyPacket serverboundChangeDifficultyPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundChangeDifficultyPacket0, this, this.player.serverLevel());
        if (this.player.m_20310_(2) || this.isSingleplayerOwner()) {
            this.server.setDifficulty(serverboundChangeDifficultyPacket0.getDifficulty(), false);
        }
    }

    @Override
    public void handleLockDifficulty(ServerboundLockDifficultyPacket serverboundLockDifficultyPacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundLockDifficultyPacket0, this, this.player.serverLevel());
        if (this.player.m_20310_(2) || this.isSingleplayerOwner()) {
            this.server.setDifficultyLocked(serverboundLockDifficultyPacket0.isLocked());
        }
    }

    @Override
    public void handleChatSessionUpdate(ServerboundChatSessionUpdatePacket serverboundChatSessionUpdatePacket0) {
        PacketUtils.ensureRunningOnSameThread(serverboundChatSessionUpdatePacket0, this, this.player.serverLevel());
        RemoteChatSession.Data $$1 = serverboundChatSessionUpdatePacket0.chatSession();
        ProfilePublicKey.Data $$2 = this.chatSession != null ? this.chatSession.profilePublicKey().data() : null;
        ProfilePublicKey.Data $$3 = $$1.profilePublicKey();
        if (!Objects.equals($$2, $$3)) {
            if ($$2 != null && $$3.expiresAt().isBefore($$2.expiresAt())) {
                this.disconnect(ProfilePublicKey.EXPIRED_PROFILE_PUBLIC_KEY);
            } else {
                try {
                    SignatureValidator $$4 = this.server.getProfileKeySignatureValidator();
                    if ($$4 == null) {
                        LOGGER.warn("Ignoring chat session from {} due to missing Services public key", this.player.m_36316_().getName());
                        return;
                    }
                    this.resetPlayerChatState($$1.validate(this.player.m_36316_(), $$4, Duration.ZERO));
                } catch (ProfilePublicKey.ValidationException var6) {
                    LOGGER.error("Failed to validate profile key: {}", var6.getMessage());
                    this.disconnect(var6.m_237308_());
                }
            }
        }
    }

    private void resetPlayerChatState(RemoteChatSession remoteChatSession0) {
        this.chatSession = remoteChatSession0;
        this.signedMessageDecoder = remoteChatSession0.createMessageDecoder(this.player.m_20148_());
        this.chatMessageChain.append(p_253488_ -> {
            this.player.setChatSession(remoteChatSession0);
            this.server.getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.INITIALIZE_CHAT), List.of(this.player)));
            return CompletableFuture.completedFuture(null);
        });
    }

    @Override
    public ServerPlayer getPlayer() {
        return this.player;
    }

    @FunctionalInterface
    interface EntityInteraction {

        InteractionResult run(ServerPlayer var1, Entity var2, InteractionHand var3);
    }
}