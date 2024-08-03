package net.minecraft.server.level;

import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.BlockUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundHorseScreenOpenPacket;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatEndPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatEnterPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundResourcePackPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ServerItemCooldowns;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.slf4j.Logger;

public class ServerPlayer extends Player {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int NEUTRAL_MOB_DEATH_NOTIFICATION_RADII_XZ = 32;

    private static final int NEUTRAL_MOB_DEATH_NOTIFICATION_RADII_Y = 10;

    public ServerGamePacketListenerImpl connection;

    public final MinecraftServer server;

    public final ServerPlayerGameMode gameMode;

    private final PlayerAdvancements advancements;

    private final ServerStatsCounter stats;

    private float lastRecordedHealthAndAbsorption = Float.MIN_VALUE;

    private int lastRecordedFoodLevel = Integer.MIN_VALUE;

    private int lastRecordedAirLevel = Integer.MIN_VALUE;

    private int lastRecordedArmor = Integer.MIN_VALUE;

    private int lastRecordedLevel = Integer.MIN_VALUE;

    private int lastRecordedExperience = Integer.MIN_VALUE;

    private float lastSentHealth = -1.0E8F;

    private int lastSentFood = -99999999;

    private boolean lastFoodSaturationZero = true;

    private int lastSentExp = -99999999;

    private int spawnInvulnerableTime = 60;

    private ChatVisiblity chatVisibility = ChatVisiblity.FULL;

    private boolean canChatColor = true;

    private long lastActionTime = Util.getMillis();

    @Nullable
    private Entity camera;

    private boolean isChangingDimension;

    private boolean seenCredits;

    private final ServerRecipeBook recipeBook = new ServerRecipeBook();

    @Nullable
    private Vec3 levitationStartPos;

    private int levitationStartTime;

    private boolean disconnected;

    @Nullable
    private Vec3 startingToFallPosition;

    @Nullable
    private Vec3 enteredNetherPosition;

    @Nullable
    private Vec3 enteredLavaOnVehiclePosition;

    private SectionPos lastSectionPos = SectionPos.of(0, 0, 0);

    private ResourceKey<Level> respawnDimension = Level.OVERWORLD;

    @Nullable
    private BlockPos respawnPosition;

    private boolean respawnForced;

    private float respawnAngle;

    private final TextFilter textFilter;

    private boolean textFilteringEnabled;

    private boolean allowsListing;

    private WardenSpawnTracker wardenSpawnTracker = new WardenSpawnTracker(0, 0, 0);

    private final ContainerSynchronizer containerSynchronizer = new ContainerSynchronizer() {

        @Override
        public void sendInitialData(AbstractContainerMenu p_143448_, NonNullList<ItemStack> p_143449_, ItemStack p_143450_, int[] p_143451_) {
            ServerPlayer.this.connection.send(new ClientboundContainerSetContentPacket(p_143448_.containerId, p_143448_.incrementStateId(), p_143449_, p_143450_));
            for (int $$4 = 0; $$4 < p_143451_.length; $$4++) {
                this.broadcastDataValue(p_143448_, $$4, p_143451_[$$4]);
            }
        }

        @Override
        public void sendSlotChange(AbstractContainerMenu p_143441_, int p_143442_, ItemStack p_143443_) {
            ServerPlayer.this.connection.send(new ClientboundContainerSetSlotPacket(p_143441_.containerId, p_143441_.incrementStateId(), p_143442_, p_143443_));
        }

        @Override
        public void sendCarriedChange(AbstractContainerMenu p_143445_, ItemStack p_143446_) {
            ServerPlayer.this.connection.send(new ClientboundContainerSetSlotPacket(-1, p_143445_.incrementStateId(), -1, p_143446_));
        }

        @Override
        public void sendDataChange(AbstractContainerMenu p_143437_, int p_143438_, int p_143439_) {
            this.broadcastDataValue(p_143437_, p_143438_, p_143439_);
        }

        private void broadcastDataValue(AbstractContainerMenu p_143455_, int p_143456_, int p_143457_) {
            ServerPlayer.this.connection.send(new ClientboundContainerSetDataPacket(p_143455_.containerId, p_143456_, p_143457_));
        }
    };

    private final ContainerListener containerListener = new ContainerListener() {

        @Override
        public void slotChanged(AbstractContainerMenu p_143466_, int p_143467_, ItemStack p_143468_) {
            Slot $$3 = p_143466_.getSlot(p_143467_);
            if (!($$3 instanceof ResultSlot)) {
                if ($$3.container == ServerPlayer.this.m_150109_()) {
                    CriteriaTriggers.INVENTORY_CHANGED.trigger(ServerPlayer.this, ServerPlayer.this.m_150109_(), p_143468_);
                }
            }
        }

        @Override
        public void dataChanged(AbstractContainerMenu p_143462_, int p_143463_, int p_143464_) {
        }
    };

    @Nullable
    private RemoteChatSession chatSession;

    private int containerCounter;

    public int latency;

    public boolean wonGame;

    public ServerPlayer(MinecraftServer minecraftServer0, ServerLevel serverLevel1, GameProfile gameProfile2) {
        super(serverLevel1, serverLevel1.m_220360_(), serverLevel1.m_220361_(), gameProfile2);
        this.textFilter = minecraftServer0.createTextFilterForPlayer(this);
        this.gameMode = minecraftServer0.createGameModeForPlayer(this);
        this.server = minecraftServer0;
        this.stats = minecraftServer0.getPlayerList().getPlayerStats(this);
        this.advancements = minecraftServer0.getPlayerList().getPlayerAdvancements(this);
        this.m_274367_(1.0F);
        this.fudgeSpawnLocation(serverLevel1);
    }

    private void fudgeSpawnLocation(ServerLevel serverLevel0) {
        BlockPos $$1 = serverLevel0.m_220360_();
        if (serverLevel0.m_6042_().hasSkyLight() && serverLevel0.getServer().getWorldData().getGameType() != GameType.ADVENTURE) {
            int $$2 = Math.max(0, this.server.getSpawnRadius(serverLevel0));
            int $$3 = Mth.floor(serverLevel0.m_6857_().getDistanceToBorder((double) $$1.m_123341_(), (double) $$1.m_123343_()));
            if ($$3 < $$2) {
                $$2 = $$3;
            }
            if ($$3 <= 1) {
                $$2 = 1;
            }
            long $$4 = (long) ($$2 * 2 + 1);
            long $$5 = $$4 * $$4;
            int $$6 = $$5 > 2147483647L ? Integer.MAX_VALUE : (int) $$5;
            int $$7 = this.getCoprime($$6);
            int $$8 = RandomSource.create().nextInt($$6);
            for (int $$9 = 0; $$9 < $$6; $$9++) {
                int $$10 = ($$8 + $$7 * $$9) % $$6;
                int $$11 = $$10 % ($$2 * 2 + 1);
                int $$12 = $$10 / ($$2 * 2 + 1);
                BlockPos $$13 = PlayerRespawnLogic.getOverworldRespawnPos(serverLevel0, $$1.m_123341_() + $$11 - $$2, $$1.m_123343_() + $$12 - $$2);
                if ($$13 != null) {
                    this.m_20035_($$13, 0.0F, 0.0F);
                    if (serverLevel0.m_45786_(this)) {
                        break;
                    }
                }
            }
        } else {
            this.m_20035_($$1, 0.0F, 0.0F);
            while (!serverLevel0.m_45786_(this) && this.m_20186_() < (double) (serverLevel0.m_151558_() - 1)) {
                this.m_6034_(this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_());
            }
        }
    }

    private int getCoprime(int int0) {
        return int0 <= 16 ? int0 - 1 : 17;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("warden_spawn_tracker", 10)) {
            WardenSpawnTracker.CODEC.parse(new Dynamic(NbtOps.INSTANCE, compoundTag0.get("warden_spawn_tracker"))).resultOrPartial(LOGGER::error).ifPresent(p_248205_ -> this.wardenSpawnTracker = p_248205_);
        }
        if (compoundTag0.contains("enteredNetherPosition", 10)) {
            CompoundTag $$1 = compoundTag0.getCompound("enteredNetherPosition");
            this.enteredNetherPosition = new Vec3($$1.getDouble("x"), $$1.getDouble("y"), $$1.getDouble("z"));
        }
        this.seenCredits = compoundTag0.getBoolean("seenCredits");
        if (compoundTag0.contains("recipeBook", 10)) {
            this.recipeBook.fromNbt(compoundTag0.getCompound("recipeBook"), this.server.getRecipeManager());
        }
        if (this.m_5803_()) {
            this.m_5796_();
        }
        if (compoundTag0.contains("SpawnX", 99) && compoundTag0.contains("SpawnY", 99) && compoundTag0.contains("SpawnZ", 99)) {
            this.respawnPosition = new BlockPos(compoundTag0.getInt("SpawnX"), compoundTag0.getInt("SpawnY"), compoundTag0.getInt("SpawnZ"));
            this.respawnForced = compoundTag0.getBoolean("SpawnForced");
            this.respawnAngle = compoundTag0.getFloat("SpawnAngle");
            if (compoundTag0.contains("SpawnDimension")) {
                this.respawnDimension = (ResourceKey<Level>) Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundTag0.get("SpawnDimension")).resultOrPartial(LOGGER::error).orElse(Level.OVERWORLD);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        WardenSpawnTracker.CODEC.encodeStart(NbtOps.INSTANCE, this.wardenSpawnTracker).resultOrPartial(LOGGER::error).ifPresent(p_9134_ -> compoundTag0.put("warden_spawn_tracker", p_9134_));
        this.storeGameTypes(compoundTag0);
        compoundTag0.putBoolean("seenCredits", this.seenCredits);
        if (this.enteredNetherPosition != null) {
            CompoundTag $$1 = new CompoundTag();
            $$1.putDouble("x", this.enteredNetherPosition.x);
            $$1.putDouble("y", this.enteredNetherPosition.y);
            $$1.putDouble("z", this.enteredNetherPosition.z);
            compoundTag0.put("enteredNetherPosition", $$1);
        }
        Entity $$2 = this.m_20201_();
        Entity $$3 = this.m_20202_();
        if ($$3 != null && $$2 != this && $$2.hasExactlyOnePlayerPassenger()) {
            CompoundTag $$4 = new CompoundTag();
            CompoundTag $$5 = new CompoundTag();
            $$2.save($$5);
            $$4.putUUID("Attach", $$3.getUUID());
            $$4.put("Entity", $$5);
            compoundTag0.put("RootVehicle", $$4);
        }
        compoundTag0.put("recipeBook", this.recipeBook.toNbt());
        compoundTag0.putString("Dimension", this.m_9236_().dimension().location().toString());
        if (this.respawnPosition != null) {
            compoundTag0.putInt("SpawnX", this.respawnPosition.m_123341_());
            compoundTag0.putInt("SpawnY", this.respawnPosition.m_123342_());
            compoundTag0.putInt("SpawnZ", this.respawnPosition.m_123343_());
            compoundTag0.putBoolean("SpawnForced", this.respawnForced);
            compoundTag0.putFloat("SpawnAngle", this.respawnAngle);
            ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, this.respawnDimension.location()).resultOrPartial(LOGGER::error).ifPresent(p_248207_ -> compoundTag0.put("SpawnDimension", p_248207_));
        }
    }

    public void setExperiencePoints(int int0) {
        float $$1 = (float) this.m_36323_();
        float $$2 = ($$1 - 1.0F) / $$1;
        this.f_36080_ = Mth.clamp((float) int0 / $$1, 0.0F, $$2);
        this.lastSentExp = -1;
    }

    public void setExperienceLevels(int int0) {
        this.f_36078_ = int0;
        this.lastSentExp = -1;
    }

    @Override
    public void giveExperienceLevels(int int0) {
        super.giveExperienceLevels(int0);
        this.lastSentExp = -1;
    }

    @Override
    public void onEnchantmentPerformed(ItemStack itemStack0, int int1) {
        super.onEnchantmentPerformed(itemStack0, int1);
        this.lastSentExp = -1;
    }

    private void initMenu(AbstractContainerMenu abstractContainerMenu0) {
        abstractContainerMenu0.addSlotListener(this.containerListener);
        abstractContainerMenu0.setSynchronizer(this.containerSynchronizer);
    }

    public void initInventoryMenu() {
        this.initMenu(this.f_36095_);
    }

    @Override
    public void onEnterCombat() {
        super.m_8108_();
        this.connection.send(new ClientboundPlayerCombatEnterPacket());
    }

    @Override
    public void onLeaveCombat() {
        super.m_8098_();
        this.connection.send(new ClientboundPlayerCombatEndPacket(this.m_21231_()));
    }

    @Override
    protected void onInsideBlock(BlockState blockState0) {
        CriteriaTriggers.ENTER_BLOCK.trigger(this, blockState0);
    }

    @Override
    protected ItemCooldowns createItemCooldowns() {
        return new ServerItemCooldowns(this);
    }

    @Override
    public void tick() {
        this.gameMode.tick();
        this.wardenSpawnTracker.tick();
        this.spawnInvulnerableTime--;
        if (this.f_19802_ > 0) {
            this.f_19802_--;
        }
        this.f_36096_.broadcastChanges();
        if (!this.m_9236_().isClientSide && !this.f_36096_.stillValid(this)) {
            this.closeContainer();
            this.f_36096_ = this.f_36095_;
        }
        Entity $$0 = this.getCamera();
        if ($$0 != this) {
            if ($$0.isAlive()) {
                this.m_19890_($$0.getX(), $$0.getY(), $$0.getZ(), $$0.getYRot(), $$0.getXRot());
                this.serverLevel().getChunkSource().move(this);
                if (this.m_36342_()) {
                    this.setCamera(this);
                }
            } else {
                this.setCamera(this);
            }
        }
        CriteriaTriggers.TICK.trigger(this);
        if (this.levitationStartPos != null) {
            CriteriaTriggers.LEVITATION.trigger(this, this.levitationStartPos, this.f_19797_ - this.levitationStartTime);
        }
        this.trackStartFallingPosition();
        this.trackEnteredOrExitedLavaOnVehicle();
        this.advancements.flushDirty(this);
    }

    public void doTick() {
        try {
            if (!this.isSpectator() || !this.m_146899_()) {
                super.tick();
            }
            for (int $$0 = 0; $$0 < this.m_150109_().getContainerSize(); $$0++) {
                ItemStack $$1 = this.m_150109_().getItem($$0);
                if ($$1.getItem().isComplex()) {
                    Packet<?> $$2 = ((ComplexItem) $$1.getItem()).getUpdatePacket($$1, this.m_9236_(), this);
                    if ($$2 != null) {
                        this.connection.send($$2);
                    }
                }
            }
            if (this.m_21223_() != this.lastSentHealth || this.lastSentFood != this.f_36097_.getFoodLevel() || this.f_36097_.getSaturationLevel() == 0.0F != this.lastFoodSaturationZero) {
                this.connection.send(new ClientboundSetHealthPacket(this.m_21223_(), this.f_36097_.getFoodLevel(), this.f_36097_.getSaturationLevel()));
                this.lastSentHealth = this.m_21223_();
                this.lastSentFood = this.f_36097_.getFoodLevel();
                this.lastFoodSaturationZero = this.f_36097_.getSaturationLevel() == 0.0F;
            }
            if (this.m_21223_() + this.m_6103_() != this.lastRecordedHealthAndAbsorption) {
                this.lastRecordedHealthAndAbsorption = this.m_21223_() + this.m_6103_();
                this.updateScoreForCriteria(ObjectiveCriteria.HEALTH, Mth.ceil(this.lastRecordedHealthAndAbsorption));
            }
            if (this.f_36097_.getFoodLevel() != this.lastRecordedFoodLevel) {
                this.lastRecordedFoodLevel = this.f_36097_.getFoodLevel();
                this.updateScoreForCriteria(ObjectiveCriteria.FOOD, Mth.ceil((float) this.lastRecordedFoodLevel));
            }
            if (this.m_20146_() != this.lastRecordedAirLevel) {
                this.lastRecordedAirLevel = this.m_20146_();
                this.updateScoreForCriteria(ObjectiveCriteria.AIR, Mth.ceil((float) this.lastRecordedAirLevel));
            }
            if (this.m_21230_() != this.lastRecordedArmor) {
                this.lastRecordedArmor = this.m_21230_();
                this.updateScoreForCriteria(ObjectiveCriteria.ARMOR, Mth.ceil((float) this.lastRecordedArmor));
            }
            if (this.f_36079_ != this.lastRecordedExperience) {
                this.lastRecordedExperience = this.f_36079_;
                this.updateScoreForCriteria(ObjectiveCriteria.EXPERIENCE, Mth.ceil((float) this.lastRecordedExperience));
            }
            if (this.f_36078_ != this.lastRecordedLevel) {
                this.lastRecordedLevel = this.f_36078_;
                this.updateScoreForCriteria(ObjectiveCriteria.LEVEL, Mth.ceil((float) this.lastRecordedLevel));
            }
            if (this.f_36079_ != this.lastSentExp) {
                this.lastSentExp = this.f_36079_;
                this.connection.send(new ClientboundSetExperiencePacket(this.f_36080_, this.f_36079_, this.f_36078_));
            }
            if (this.f_19797_ % 20 == 0) {
                CriteriaTriggers.LOCATION.trigger(this);
            }
        } catch (Throwable var4) {
            CrashReport $$4 = CrashReport.forThrowable(var4, "Ticking player");
            CrashReportCategory $$5 = $$4.addCategory("Player being ticked");
            this.m_7976_($$5);
            throw new ReportedException($$4);
        }
    }

    @Override
    public void resetFallDistance() {
        if (this.m_21223_() > 0.0F && this.startingToFallPosition != null) {
            CriteriaTriggers.FALL_FROM_HEIGHT.trigger(this, this.startingToFallPosition);
        }
        this.startingToFallPosition = null;
        super.m_183634_();
    }

    public void trackStartFallingPosition() {
        if (this.f_19789_ > 0.0F && this.startingToFallPosition == null) {
            this.startingToFallPosition = this.m_20182_();
        }
    }

    public void trackEnteredOrExitedLavaOnVehicle() {
        if (this.m_20202_() != null && this.m_20202_().isInLava()) {
            if (this.enteredLavaOnVehiclePosition == null) {
                this.enteredLavaOnVehiclePosition = this.m_20182_();
            } else {
                CriteriaTriggers.RIDE_ENTITY_IN_LAVA_TRIGGER.trigger(this, this.enteredLavaOnVehiclePosition);
            }
        }
        if (this.enteredLavaOnVehiclePosition != null && (this.m_20202_() == null || !this.m_20202_().isInLava())) {
            this.enteredLavaOnVehiclePosition = null;
        }
    }

    private void updateScoreForCriteria(ObjectiveCriteria objectiveCriteria0, int int1) {
        this.m_36329_().forAllObjectives(objectiveCriteria0, this.m_6302_(), p_9178_ -> p_9178_.setScore(int1));
    }

    @Override
    public void die(DamageSource damageSource0) {
        this.m_146850_(GameEvent.ENTITY_DIE);
        boolean $$1 = this.m_9236_().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES);
        if ($$1) {
            Component $$2 = this.m_21231_().getDeathMessage();
            this.connection.send(new ClientboundPlayerCombatKillPacket(this.m_19879_(), $$2), PacketSendListener.exceptionallySend(() -> {
                int $$1x = 256;
                ???;
                Component $$3x = Component.translatable("death.attack.message_too_long", Component.literal($$2x).withStyle(ChatFormatting.YELLOW));
                Component $$4x = Component.translatable("death.attack.even_more_magic", this.m_5446_()).withStyle(p_143420_ -> p_143420_.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, $$3x)));
                return new ClientboundPlayerCombatKillPacket(this.m_19879_(), $$4x);
            }));
            Team $$3 = this.m_5647_();
            if ($$3 == null || $$3.getDeathMessageVisibility() == Team.Visibility.ALWAYS) {
                this.server.getPlayerList().broadcastSystemMessage($$2, false);
            } else if ($$3.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS) {
                this.server.getPlayerList().broadcastSystemToTeam(this, $$2);
            } else if ($$3.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OWN_TEAM) {
                this.server.getPlayerList().broadcastSystemToAllExceptTeam(this, $$2);
            }
        } else {
            this.connection.send(new ClientboundPlayerCombatKillPacket(this.m_19879_(), CommonComponents.EMPTY));
        }
        this.m_36328_();
        if (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
            this.tellNeutralMobsThatIDied();
        }
        if (!this.isSpectator()) {
            this.m_6668_(damageSource0);
        }
        this.m_36329_().forAllObjectives(ObjectiveCriteria.DEATH_COUNT, this.m_6302_(), Score::m_83392_);
        LivingEntity $$4 = this.m_21232_();
        if ($$4 != null) {
            this.m_36246_(Stats.ENTITY_KILLED_BY.get($$4.m_6095_()));
            $$4.m_5993_(this, this.f_20897_, damageSource0);
            this.m_21268_($$4);
        }
        this.m_9236_().broadcastEntityEvent(this, (byte) 3);
        this.m_36220_(Stats.DEATHS);
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        this.m_20095_();
        this.m_146917_(0);
        this.m_146868_(false);
        this.m_21231_().recheckStatus();
        this.m_219749_(Optional.of(GlobalPos.of(this.m_9236_().dimension(), this.m_20183_())));
    }

    private void tellNeutralMobsThatIDied() {
        AABB $$0 = new AABB(this.m_20183_()).inflate(32.0, 10.0, 32.0);
        this.m_9236_().m_6443_(Mob.class, $$0, EntitySelector.NO_SPECTATORS).stream().filter(p_9188_ -> p_9188_ instanceof NeutralMob).forEach(p_9057_ -> ((NeutralMob) p_9057_).playerDied(this));
    }

    @Override
    public void awardKillScore(Entity entity0, int int1, DamageSource damageSource2) {
        if (entity0 != this) {
            super.m_5993_(entity0, int1, damageSource2);
            this.m_36401_(int1);
            String $$3 = this.m_6302_();
            String $$4 = entity0.getScoreboardName();
            this.m_36329_().forAllObjectives(ObjectiveCriteria.KILL_COUNT_ALL, $$3, Score::m_83392_);
            if (entity0 instanceof Player) {
                this.m_36220_(Stats.PLAYER_KILLS);
                this.m_36329_().forAllObjectives(ObjectiveCriteria.KILL_COUNT_PLAYERS, $$3, Score::m_83392_);
            } else {
                this.m_36220_(Stats.MOB_KILLS);
            }
            this.handleTeamKill($$3, $$4, ObjectiveCriteria.TEAM_KILL);
            this.handleTeamKill($$4, $$3, ObjectiveCriteria.KILLED_BY_TEAM);
            CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(this, entity0, damageSource2);
        }
    }

    private void handleTeamKill(String string0, String string1, ObjectiveCriteria[] objectiveCriteria2) {
        PlayerTeam $$3 = this.m_36329_().getPlayersTeam(string1);
        if ($$3 != null) {
            int $$4 = $$3.getColor().getId();
            if ($$4 >= 0 && $$4 < objectiveCriteria2.length) {
                this.m_36329_().forAllObjectives(objectiveCriteria2[$$4], string0, Score::m_83392_);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.isInvulnerableTo(damageSource0)) {
            return false;
        } else {
            boolean $$2 = this.server.isDedicatedServer() && this.isPvpAllowed() && damageSource0.is(DamageTypeTags.IS_FALL);
            if (!$$2 && this.spawnInvulnerableTime > 0 && !damageSource0.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                return false;
            } else {
                Entity $$3 = damageSource0.getEntity();
                if ($$3 instanceof Player $$4 && !this.canHarmPlayer($$4)) {
                    return false;
                }
                if ($$3 instanceof AbstractArrow $$5 && $$5.m_19749_() instanceof Player $$7 && !this.canHarmPlayer($$7)) {
                    return false;
                }
                return super.hurt(damageSource0, float1);
            }
        }
    }

    @Override
    public boolean canHarmPlayer(Player player0) {
        return !this.isPvpAllowed() ? false : super.canHarmPlayer(player0);
    }

    private boolean isPvpAllowed() {
        return this.server.isPvpAllowed();
    }

    @Nullable
    @Override
    protected PortalInfo findDimensionEntryPoint(ServerLevel serverLevel0) {
        PortalInfo $$1 = super.m_7937_(serverLevel0);
        if ($$1 != null && this.m_9236_().dimension() == Level.OVERWORLD && serverLevel0.m_46472_() == Level.END) {
            Vec3 $$2 = $$1.pos.add(0.0, -1.0, 0.0);
            return new PortalInfo($$2, Vec3.ZERO, 90.0F, 0.0F);
        } else {
            return $$1;
        }
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerLevel serverLevel0) {
        this.isChangingDimension = true;
        ServerLevel $$1 = this.serverLevel();
        ResourceKey<Level> $$2 = $$1.m_46472_();
        if ($$2 == Level.END && serverLevel0.m_46472_() == Level.OVERWORLD) {
            this.m_19877_();
            this.serverLevel().removePlayerImmediately(this, Entity.RemovalReason.CHANGED_DIMENSION);
            if (!this.wonGame) {
                this.wonGame = true;
                this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, this.seenCredits ? 0.0F : 1.0F));
                this.seenCredits = true;
            }
            return this;
        } else {
            LevelData $$3 = serverLevel0.m_6106_();
            this.connection.send(new ClientboundRespawnPacket(serverLevel0.m_220362_(), serverLevel0.m_46472_(), BiomeManager.obfuscateSeed(serverLevel0.getSeed()), this.gameMode.getGameModeForPlayer(), this.gameMode.getPreviousGameModeForPlayer(), serverLevel0.m_46659_(), serverLevel0.isFlat(), (byte) 3, this.m_219759_(), this.m_287157_()));
            this.connection.send(new ClientboundChangeDifficultyPacket($$3.getDifficulty(), $$3.isDifficultyLocked()));
            PlayerList $$4 = this.server.getPlayerList();
            $$4.sendPlayerPermissionLevel(this);
            $$1.removePlayerImmediately(this, Entity.RemovalReason.CHANGED_DIMENSION);
            this.m_146912_();
            PortalInfo $$5 = this.findDimensionEntryPoint(serverLevel0);
            if ($$5 != null) {
                $$1.m_46473_().push("moving");
                if ($$2 == Level.OVERWORLD && serverLevel0.m_46472_() == Level.NETHER) {
                    this.enteredNetherPosition = this.m_20182_();
                } else if (serverLevel0.m_46472_() == Level.END) {
                    this.createEndPlatform(serverLevel0, BlockPos.containing($$5.pos));
                }
                $$1.m_46473_().pop();
                $$1.m_46473_().push("placing");
                this.setServerLevel(serverLevel0);
                this.connection.teleport($$5.pos.x, $$5.pos.y, $$5.pos.z, $$5.yRot, $$5.xRot);
                this.connection.resetPosition();
                serverLevel0.addDuringPortalTeleport(this);
                $$1.m_46473_().pop();
                this.triggerDimensionChangeTriggers($$1);
                this.connection.send(new ClientboundPlayerAbilitiesPacket(this.m_150110_()));
                $$4.sendLevelInfo(this, serverLevel0);
                $$4.sendAllPlayerInfo(this);
                for (MobEffectInstance $$6 : this.m_21220_()) {
                    this.connection.send(new ClientboundUpdateMobEffectPacket(this.m_19879_(), $$6));
                }
                this.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                this.lastSentExp = -1;
                this.lastSentHealth = -1.0F;
                this.lastSentFood = -1;
            }
            return this;
        }
    }

    private void createEndPlatform(ServerLevel serverLevel0, BlockPos blockPos1) {
        BlockPos.MutableBlockPos $$2 = blockPos1.mutable();
        for (int $$3 = -2; $$3 <= 2; $$3++) {
            for (int $$4 = -2; $$4 <= 2; $$4++) {
                for (int $$5 = -1; $$5 < 3; $$5++) {
                    BlockState $$6 = $$5 == -1 ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
                    serverLevel0.m_46597_($$2.set(blockPos1).move($$4, $$5, $$3), $$6);
                }
            }
        }
    }

    @Override
    protected Optional<BlockUtil.FoundRectangle> getExitPortal(ServerLevel serverLevel0, BlockPos blockPos1, boolean boolean2, WorldBorder worldBorder3) {
        Optional<BlockUtil.FoundRectangle> $$4 = super.m_183318_(serverLevel0, blockPos1, boolean2, worldBorder3);
        if ($$4.isPresent()) {
            return $$4;
        } else {
            Direction.Axis $$5 = (Direction.Axis) this.m_9236_().getBlockState(this.f_19819_).m_61145_(NetherPortalBlock.AXIS).orElse(Direction.Axis.X);
            Optional<BlockUtil.FoundRectangle> $$6 = serverLevel0.getPortalForcer().createPortal(blockPos1, $$5);
            if (!$$6.isPresent()) {
                LOGGER.error("Unable to create a portal, likely target out of worldborder");
            }
            return $$6;
        }
    }

    private void triggerDimensionChangeTriggers(ServerLevel serverLevel0) {
        ResourceKey<Level> $$1 = serverLevel0.m_46472_();
        ResourceKey<Level> $$2 = this.m_9236_().dimension();
        CriteriaTriggers.CHANGED_DIMENSION.trigger(this, $$1, $$2);
        if ($$1 == Level.NETHER && $$2 == Level.OVERWORLD && this.enteredNetherPosition != null) {
            CriteriaTriggers.NETHER_TRAVEL.trigger(this, this.enteredNetherPosition);
        }
        if ($$2 != Level.NETHER) {
            this.enteredNetherPosition = null;
        }
    }

    @Override
    public boolean broadcastToPlayer(ServerPlayer serverPlayer0) {
        if (serverPlayer0.isSpectator()) {
            return this.getCamera() == this;
        } else {
            return this.isSpectator() ? false : super.m_6459_(serverPlayer0);
        }
    }

    @Override
    public void take(Entity entity0, int int1) {
        super.m_7938_(entity0, int1);
        this.f_36096_.broadcastChanges();
    }

    @Override
    public Either<Player.BedSleepingProblem, Unit> startSleepInBed(BlockPos blockPos0) {
        Direction $$1 = (Direction) this.m_9236_().getBlockState(blockPos0).m_61143_(HorizontalDirectionalBlock.FACING);
        if (this.m_5803_() || !this.m_6084_()) {
            return Either.left(Player.BedSleepingProblem.OTHER_PROBLEM);
        } else if (!this.m_9236_().dimensionType().natural()) {
            return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_HERE);
        } else if (!this.bedInRange(blockPos0, $$1)) {
            return Either.left(Player.BedSleepingProblem.TOO_FAR_AWAY);
        } else if (this.bedBlocked(blockPos0, $$1)) {
            return Either.left(Player.BedSleepingProblem.OBSTRUCTED);
        } else {
            this.setRespawnPosition(this.m_9236_().dimension(), blockPos0, this.m_146908_(), false, true);
            if (this.m_9236_().isDay()) {
                return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
            } else {
                if (!this.isCreative()) {
                    double $$2 = 8.0;
                    double $$3 = 5.0;
                    Vec3 $$4 = Vec3.atBottomCenterOf(blockPos0);
                    List<Monster> $$5 = this.m_9236_().m_6443_(Monster.class, new AABB($$4.x() - 8.0, $$4.y() - 5.0, $$4.z() - 8.0, $$4.x() + 8.0, $$4.y() + 5.0, $$4.z() + 8.0), p_9062_ -> p_9062_.isPreventingPlayerRest(this));
                    if (!$$5.isEmpty()) {
                        return Either.left(Player.BedSleepingProblem.NOT_SAFE);
                    }
                }
                Either<Player.BedSleepingProblem, Unit> $$6 = super.startSleepInBed(blockPos0).ifRight(p_9029_ -> {
                    this.m_36220_(Stats.SLEEP_IN_BED);
                    CriteriaTriggers.SLEPT_IN_BED.trigger(this);
                });
                if (!this.serverLevel().canSleepThroughNights()) {
                    this.displayClientMessage(Component.translatable("sleep.not_possible"), true);
                }
                ((ServerLevel) this.m_9236_()).updateSleepingPlayerList();
                return $$6;
            }
        }
    }

    @Override
    public void startSleeping(BlockPos blockPos0) {
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        super.m_5802_(blockPos0);
    }

    private boolean bedInRange(BlockPos blockPos0, Direction direction1) {
        return this.isReachableBedBlock(blockPos0) || this.isReachableBedBlock(blockPos0.relative(direction1.getOpposite()));
    }

    private boolean isReachableBedBlock(BlockPos blockPos0) {
        Vec3 $$1 = Vec3.atBottomCenterOf(blockPos0);
        return Math.abs(this.m_20185_() - $$1.x()) <= 3.0 && Math.abs(this.m_20186_() - $$1.y()) <= 2.0 && Math.abs(this.m_20189_() - $$1.z()) <= 3.0;
    }

    private boolean bedBlocked(BlockPos blockPos0, Direction direction1) {
        BlockPos $$2 = blockPos0.above();
        return !this.m_36350_($$2) || !this.m_36350_($$2.relative(direction1.getOpposite()));
    }

    @Override
    public void stopSleepInBed(boolean boolean0, boolean boolean1) {
        if (this.m_5803_()) {
            this.serverLevel().getChunkSource().broadcastAndSend(this, new ClientboundAnimatePacket(this, 2));
        }
        super.stopSleepInBed(boolean0, boolean1);
        if (this.connection != null) {
            this.connection.teleport(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
        }
    }

    @Override
    public void dismountTo(double double0, double double1, double double2) {
        this.m_6038_();
        this.m_6034_(double0, double1, double2);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource0) {
        return super.isInvulnerableTo(damageSource0) || this.isChangingDimension();
    }

    @Override
    protected void checkFallDamage(double double0, boolean boolean1, BlockState blockState2, BlockPos blockPos3) {
    }

    @Override
    protected void onChangedBlock(BlockPos blockPos0) {
        if (!this.isSpectator()) {
            super.m_5806_(blockPos0);
        }
    }

    public void doCheckFallDamage(double double0, double double1, double double2, boolean boolean3) {
        if (!this.m_146899_()) {
            this.m_289600_(boolean3, new Vec3(double0, double1, double2));
            BlockPos $$4 = this.m_216999_();
            super.m_7840_(double1, boolean3, this.m_9236_().getBlockState($$4), $$4);
        }
    }

    @Override
    public void openTextEdit(SignBlockEntity signBlockEntity0, boolean boolean1) {
        this.connection.send(new ClientboundBlockUpdatePacket(this.m_9236_(), signBlockEntity0.m_58899_()));
        this.connection.send(new ClientboundOpenSignEditorPacket(signBlockEntity0.m_58899_(), boolean1));
    }

    private void nextContainerCounter() {
        this.containerCounter = this.containerCounter % 100 + 1;
    }

    @Override
    public OptionalInt openMenu(@Nullable MenuProvider menuProvider0) {
        if (menuProvider0 == null) {
            return OptionalInt.empty();
        } else {
            if (this.f_36096_ != this.f_36095_) {
                this.closeContainer();
            }
            this.nextContainerCounter();
            AbstractContainerMenu $$1 = menuProvider0.m_7208_(this.containerCounter, this.m_150109_(), this);
            if ($$1 == null) {
                if (this.isSpectator()) {
                    this.displayClientMessage(Component.translatable("container.spectatorCantOpen").withStyle(ChatFormatting.RED), true);
                }
                return OptionalInt.empty();
            } else {
                this.connection.send(new ClientboundOpenScreenPacket($$1.containerId, $$1.getType(), menuProvider0.getDisplayName()));
                this.initMenu($$1);
                this.f_36096_ = $$1;
                return OptionalInt.of(this.containerCounter);
            }
        }
    }

    @Override
    public void sendMerchantOffers(int int0, MerchantOffers merchantOffers1, int int2, int int3, boolean boolean4, boolean boolean5) {
        this.connection.send(new ClientboundMerchantOffersPacket(int0, merchantOffers1, int2, int3, boolean4, boolean5));
    }

    @Override
    public void openHorseInventory(AbstractHorse abstractHorse0, Container container1) {
        if (this.f_36096_ != this.f_36095_) {
            this.closeContainer();
        }
        this.nextContainerCounter();
        this.connection.send(new ClientboundHorseScreenOpenPacket(this.containerCounter, container1.getContainerSize(), abstractHorse0.m_19879_()));
        this.f_36096_ = new HorseInventoryMenu(this.containerCounter, this.m_150109_(), container1, abstractHorse0);
        this.initMenu(this.f_36096_);
    }

    @Override
    public void openItemGui(ItemStack itemStack0, InteractionHand interactionHand1) {
        if (itemStack0.is(Items.WRITTEN_BOOK)) {
            if (WrittenBookItem.resolveBookComponents(itemStack0, this.m_20203_(), this)) {
                this.f_36096_.broadcastChanges();
            }
            this.connection.send(new ClientboundOpenBookPacket(interactionHand1));
        }
    }

    @Override
    public void openCommandBlock(CommandBlockEntity commandBlockEntity0) {
        this.connection.send(ClientboundBlockEntityDataPacket.create(commandBlockEntity0, BlockEntity::m_187482_));
    }

    @Override
    public void closeContainer() {
        this.connection.send(new ClientboundContainerClosePacket(this.f_36096_.containerId));
        this.doCloseContainer();
    }

    @Override
    public void doCloseContainer() {
        this.f_36096_.removed(this);
        this.f_36095_.m_150414_(this.f_36096_);
        this.f_36096_ = this.f_36095_;
    }

    public void setPlayerInput(float float0, float float1, boolean boolean2, boolean boolean3) {
        if (this.m_20159_()) {
            if (float0 >= -1.0F && float0 <= 1.0F) {
                this.f_20900_ = float0;
            }
            if (float1 >= -1.0F && float1 <= 1.0F) {
                this.f_20902_ = float1;
            }
            this.f_20899_ = boolean2;
            this.m_20260_(boolean3);
        }
    }

    @Override
    public void awardStat(Stat<?> stat0, int int1) {
        this.stats.m_13023_(this, stat0, int1);
        this.m_36329_().forAllObjectives(stat0, this.m_6302_(), p_8996_ -> p_8996_.add(int1));
    }

    @Override
    public void resetStat(Stat<?> stat0) {
        this.stats.setValue(this, stat0, 0);
        this.m_36329_().forAllObjectives(stat0, this.m_6302_(), Score::m_83401_);
    }

    @Override
    public int awardRecipes(Collection<Recipe<?>> collectionRecipe0) {
        return this.recipeBook.addRecipes(collectionRecipe0, this);
    }

    @Override
    public void triggerRecipeCrafted(Recipe<?> recipe0, List<ItemStack> listItemStack1) {
        CriteriaTriggers.RECIPE_CRAFTED.trigger(this, recipe0.getId(), listItemStack1);
    }

    @Override
    public void awardRecipesByKey(ResourceLocation[] resourceLocation0) {
        List<Recipe<?>> $$1 = Lists.newArrayList();
        for (ResourceLocation $$2 : resourceLocation0) {
            this.server.getRecipeManager().byKey($$2).ifPresent($$1::add);
        }
        this.awardRecipes($$1);
    }

    @Override
    public int resetRecipes(Collection<Recipe<?>> collectionRecipe0) {
        return this.recipeBook.removeRecipes(collectionRecipe0, this);
    }

    @Override
    public void giveExperiencePoints(int int0) {
        super.giveExperiencePoints(int0);
        this.lastSentExp = -1;
    }

    public void disconnect() {
        this.disconnected = true;
        this.m_20153_();
        if (this.m_5803_()) {
            this.stopSleepInBed(true, false);
        }
    }

    public boolean hasDisconnected() {
        return this.disconnected;
    }

    public void resetSentInfo() {
        this.lastSentHealth = -1.0E8F;
    }

    @Override
    public void displayClientMessage(Component component0, boolean boolean1) {
        this.sendSystemMessage(component0, boolean1);
    }

    @Override
    protected void completeUsingItem() {
        if (!this.f_20935_.isEmpty() && this.m_6117_()) {
            this.connection.send(new ClientboundEntityEventPacket(this, (byte) 9));
            super.m_8095_();
        }
    }

    @Override
    public void lookAt(EntityAnchorArgument.Anchor entityAnchorArgumentAnchor0, Vec3 vec1) {
        super.m_7618_(entityAnchorArgumentAnchor0, vec1);
        this.connection.send(new ClientboundPlayerLookAtPacket(entityAnchorArgumentAnchor0, vec1.x, vec1.y, vec1.z));
    }

    public void lookAt(EntityAnchorArgument.Anchor entityAnchorArgumentAnchor0, Entity entity1, EntityAnchorArgument.Anchor entityAnchorArgumentAnchor2) {
        Vec3 $$3 = entityAnchorArgumentAnchor2.apply(entity1);
        super.m_7618_(entityAnchorArgumentAnchor0, $$3);
        this.connection.send(new ClientboundPlayerLookAtPacket(entityAnchorArgumentAnchor0, entity1, entityAnchorArgumentAnchor2));
    }

    public void restoreFrom(ServerPlayer serverPlayer0, boolean boolean1) {
        this.wardenSpawnTracker = serverPlayer0.wardenSpawnTracker;
        this.textFilteringEnabled = serverPlayer0.textFilteringEnabled;
        this.chatSession = serverPlayer0.chatSession;
        this.gameMode.setGameModeForPlayer(serverPlayer0.gameMode.getGameModeForPlayer(), serverPlayer0.gameMode.getPreviousGameModeForPlayer());
        this.onUpdateAbilities();
        if (boolean1) {
            this.m_150109_().replaceWith(serverPlayer0.m_150109_());
            this.m_21153_(serverPlayer0.m_21223_());
            this.f_36097_ = serverPlayer0.f_36097_;
            this.f_36078_ = serverPlayer0.f_36078_;
            this.f_36079_ = serverPlayer0.f_36079_;
            this.f_36080_ = serverPlayer0.f_36080_;
            this.m_36397_(serverPlayer0.m_36344_());
            this.f_19819_ = serverPlayer0.f_19819_;
        } else if (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) || serverPlayer0.isSpectator()) {
            this.m_150109_().replaceWith(serverPlayer0.m_150109_());
            this.f_36078_ = serverPlayer0.f_36078_;
            this.f_36079_ = serverPlayer0.f_36079_;
            this.f_36080_ = serverPlayer0.f_36080_;
            this.m_36397_(serverPlayer0.m_36344_());
        }
        this.f_36081_ = serverPlayer0.f_36081_;
        this.f_36094_ = serverPlayer0.f_36094_;
        this.m_20088_().set(f_36089_, (Byte) serverPlayer0.m_20088_().get(f_36089_));
        this.lastSentExp = -1;
        this.lastSentHealth = -1.0F;
        this.lastSentFood = -1;
        this.recipeBook.m_12685_(serverPlayer0.recipeBook);
        this.seenCredits = serverPlayer0.seenCredits;
        this.enteredNetherPosition = serverPlayer0.enteredNetherPosition;
        this.m_36362_(serverPlayer0.m_36331_());
        this.m_36364_(serverPlayer0.m_36332_());
        this.m_219749_(serverPlayer0.m_219759_());
    }

    @Override
    protected void onEffectAdded(MobEffectInstance mobEffectInstance0, @Nullable Entity entity1) {
        super.m_142540_(mobEffectInstance0, entity1);
        this.connection.send(new ClientboundUpdateMobEffectPacket(this.m_19879_(), mobEffectInstance0));
        if (mobEffectInstance0.getEffect() == MobEffects.LEVITATION) {
            this.levitationStartTime = this.f_19797_;
            this.levitationStartPos = this.m_20182_();
        }
        CriteriaTriggers.EFFECTS_CHANGED.trigger(this, entity1);
    }

    @Override
    protected void onEffectUpdated(MobEffectInstance mobEffectInstance0, boolean boolean1, @Nullable Entity entity2) {
        super.m_141973_(mobEffectInstance0, boolean1, entity2);
        this.connection.send(new ClientboundUpdateMobEffectPacket(this.m_19879_(), mobEffectInstance0));
        CriteriaTriggers.EFFECTS_CHANGED.trigger(this, entity2);
    }

    @Override
    protected void onEffectRemoved(MobEffectInstance mobEffectInstance0) {
        super.m_7285_(mobEffectInstance0);
        this.connection.send(new ClientboundRemoveMobEffectPacket(this.m_19879_(), mobEffectInstance0.getEffect()));
        if (mobEffectInstance0.getEffect() == MobEffects.LEVITATION) {
            this.levitationStartPos = null;
        }
        CriteriaTriggers.EFFECTS_CHANGED.trigger(this, null);
    }

    @Override
    public void teleportTo(double double0, double double1, double double2) {
        this.connection.teleport(double0, double1, double2, this.m_146908_(), this.m_146909_(), RelativeMovement.ROTATION);
    }

    @Override
    public void teleportRelative(double double0, double double1, double double2) {
        this.connection.teleport(this.m_20185_() + double0, this.m_20186_() + double1, this.m_20189_() + double2, this.m_146908_(), this.m_146909_(), RelativeMovement.ALL);
    }

    @Override
    public boolean teleportTo(ServerLevel serverLevel0, double double1, double double2, double double3, Set<RelativeMovement> setRelativeMovement4, float float5, float float6) {
        ChunkPos $$7 = new ChunkPos(BlockPos.containing(double1, double2, double3));
        serverLevel0.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, $$7, 1, this.m_19879_());
        this.stopRiding();
        if (this.m_5803_()) {
            this.stopSleepInBed(true, true);
        }
        if (serverLevel0 == this.m_9236_()) {
            this.connection.teleport(double1, double2, double3, float5, float6, setRelativeMovement4);
        } else {
            this.teleportTo(serverLevel0, double1, double2, double3, float5, float6);
        }
        this.m_5616_(float5);
        return true;
    }

    @Override
    public void moveTo(double double0, double double1, double double2) {
        super.m_6027_(double0, double1, double2);
        this.connection.resetPosition();
    }

    @Override
    public void crit(Entity entity0) {
        this.serverLevel().getChunkSource().broadcastAndSend(this, new ClientboundAnimatePacket(entity0, 4));
    }

    @Override
    public void magicCrit(Entity entity0) {
        this.serverLevel().getChunkSource().broadcastAndSend(this, new ClientboundAnimatePacket(entity0, 5));
    }

    @Override
    public void onUpdateAbilities() {
        if (this.connection != null) {
            this.connection.send(new ClientboundPlayerAbilitiesPacket(this.m_150110_()));
            this.updateInvisibilityStatus();
        }
    }

    public ServerLevel serverLevel() {
        return (ServerLevel) this.m_9236_();
    }

    public boolean setGameMode(GameType gameType0) {
        if (!this.gameMode.changeGameModeForPlayer(gameType0)) {
            return false;
        } else {
            this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE, (float) gameType0.getId()));
            if (gameType0 == GameType.SPECTATOR) {
                this.m_36328_();
                this.stopRiding();
            } else {
                this.setCamera(this);
            }
            this.onUpdateAbilities();
            this.m_21210_();
            return true;
        }
    }

    @Override
    public boolean isSpectator() {
        return this.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
    }

    @Override
    public boolean isCreative() {
        return this.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
    }

    @Override
    public void sendSystemMessage(Component component0) {
        this.sendSystemMessage(component0, false);
    }

    public void sendSystemMessage(Component component0, boolean boolean1) {
        if (this.acceptsSystemMessages(boolean1)) {
            this.connection.send(new ClientboundSystemChatPacket(component0, boolean1), PacketSendListener.exceptionallySend(() -> {
                if (this.acceptsSystemMessages(false)) {
                    int $$1 = 256;
                    ???;
                    Component $$3 = Component.literal($$2).withStyle(ChatFormatting.YELLOW);
                    return new ClientboundSystemChatPacket(Component.translatable("multiplayer.message_not_delivered", $$3).withStyle(ChatFormatting.RED), false);
                } else {
                    return null;
                }
            }));
        }
    }

    public void sendChatMessage(OutgoingChatMessage outgoingChatMessage0, boolean boolean1, ChatType.Bound chatTypeBound2) {
        if (this.acceptsChatMessages()) {
            outgoingChatMessage0.sendToPlayer(this, boolean1, chatTypeBound2);
        }
    }

    public String getIpAddress() {
        return this.connection.getRemoteAddress() instanceof InetSocketAddress $$1 ? InetAddresses.toAddrString($$1.getAddress()) : "<unknown>";
    }

    public void updateOptions(ServerboundClientInformationPacket serverboundClientInformationPacket0) {
        this.chatVisibility = serverboundClientInformationPacket0.chatVisibility();
        this.canChatColor = serverboundClientInformationPacket0.chatColors();
        this.textFilteringEnabled = serverboundClientInformationPacket0.textFilteringEnabled();
        this.allowsListing = serverboundClientInformationPacket0.allowsListing();
        this.m_20088_().set(f_36089_, (byte) serverboundClientInformationPacket0.modelCustomisation());
        this.m_20088_().set(f_36090_, (byte) (serverboundClientInformationPacket0.mainHand() == HumanoidArm.LEFT ? 0 : 1));
    }

    public boolean canChatInColor() {
        return this.canChatColor;
    }

    public ChatVisiblity getChatVisibility() {
        return this.chatVisibility;
    }

    private boolean acceptsSystemMessages(boolean boolean0) {
        return this.chatVisibility == ChatVisiblity.HIDDEN ? boolean0 : true;
    }

    private boolean acceptsChatMessages() {
        return this.chatVisibility == ChatVisiblity.FULL;
    }

    public void sendTexturePack(String string0, String string1, boolean boolean2, @Nullable Component component3) {
        this.connection.send(new ClientboundResourcePackPacket(string0, string1, boolean2, component3));
    }

    public void sendServerStatus(ServerStatus serverStatus0) {
        this.connection.send(new ClientboundServerDataPacket(serverStatus0.description(), serverStatus0.favicon().map(ServerStatus.Favicon::f_271462_), serverStatus0.enforcesSecureChat()));
    }

    @Override
    protected int getPermissionLevel() {
        return this.server.getProfilePermissions(this.m_36316_());
    }

    public void resetLastActionTime() {
        this.lastActionTime = Util.getMillis();
    }

    public ServerStatsCounter getStats() {
        return this.stats;
    }

    public ServerRecipeBook getRecipeBook() {
        return this.recipeBook;
    }

    @Override
    protected void updateInvisibilityStatus() {
        if (this.isSpectator()) {
            this.m_21218_();
            this.m_6842_(true);
        } else {
            super.m_8034_();
        }
    }

    public Entity getCamera() {
        return (Entity) (this.camera == null ? this : this.camera);
    }

    public void setCamera(@Nullable Entity entity0) {
        Entity $$1 = this.getCamera();
        this.camera = (Entity) (entity0 == null ? this : entity0);
        if ($$1 != this.camera) {
            if (this.camera.level() instanceof ServerLevel $$2) {
                this.teleportTo($$2, this.camera.getX(), this.camera.getY(), this.camera.getZ(), Set.of(), this.m_146908_(), this.m_146909_());
            }
            if (entity0 != null) {
                this.serverLevel().getChunkSource().move(this);
            }
            this.connection.send(new ClientboundSetCameraPacket(this.camera));
            this.connection.resetPosition();
        }
    }

    @Override
    protected void processPortalCooldown() {
        if (!this.isChangingDimension) {
            super.m_8021_();
        }
    }

    @Override
    public void attack(Entity entity0) {
        if (this.gameMode.getGameModeForPlayer() == GameType.SPECTATOR) {
            this.setCamera(entity0);
        } else {
            super.attack(entity0);
        }
    }

    public long getLastActionTime() {
        return this.lastActionTime;
    }

    @Nullable
    public Component getTabListDisplayName() {
        return null;
    }

    @Override
    public void swing(InteractionHand interactionHand0) {
        super.m_6674_(interactionHand0);
        this.m_36334_();
    }

    public boolean isChangingDimension() {
        return this.isChangingDimension;
    }

    public void hasChangedDimension() {
        this.isChangingDimension = false;
    }

    public PlayerAdvancements getAdvancements() {
        return this.advancements;
    }

    public void teleportTo(ServerLevel serverLevel0, double double1, double double2, double double3, float float4, float float5) {
        this.setCamera(this);
        this.stopRiding();
        if (serverLevel0 == this.m_9236_()) {
            this.connection.teleport(double1, double2, double3, float4, float5);
        } else {
            ServerLevel $$6 = this.serverLevel();
            LevelData $$7 = serverLevel0.m_6106_();
            this.connection.send(new ClientboundRespawnPacket(serverLevel0.m_220362_(), serverLevel0.m_46472_(), BiomeManager.obfuscateSeed(serverLevel0.getSeed()), this.gameMode.getGameModeForPlayer(), this.gameMode.getPreviousGameModeForPlayer(), serverLevel0.m_46659_(), serverLevel0.isFlat(), (byte) 3, this.m_219759_(), this.m_287157_()));
            this.connection.send(new ClientboundChangeDifficultyPacket($$7.getDifficulty(), $$7.isDifficultyLocked()));
            this.server.getPlayerList().sendPlayerPermissionLevel(this);
            $$6.removePlayerImmediately(this, Entity.RemovalReason.CHANGED_DIMENSION);
            this.m_146912_();
            this.m_7678_(double1, double2, double3, float4, float5);
            this.setServerLevel(serverLevel0);
            serverLevel0.addDuringCommandTeleport(this);
            this.triggerDimensionChangeTriggers($$6);
            this.connection.teleport(double1, double2, double3, float4, float5);
            this.server.getPlayerList().sendLevelInfo(this, serverLevel0);
            this.server.getPlayerList().sendAllPlayerInfo(this);
        }
    }

    @Nullable
    public BlockPos getRespawnPosition() {
        return this.respawnPosition;
    }

    public float getRespawnAngle() {
        return this.respawnAngle;
    }

    public ResourceKey<Level> getRespawnDimension() {
        return this.respawnDimension;
    }

    public boolean isRespawnForced() {
        return this.respawnForced;
    }

    public void setRespawnPosition(ResourceKey<Level> resourceKeyLevel0, @Nullable BlockPos blockPos1, float float2, boolean boolean3, boolean boolean4) {
        if (blockPos1 != null) {
            boolean $$5 = blockPos1.equals(this.respawnPosition) && resourceKeyLevel0.equals(this.respawnDimension);
            if (boolean4 && !$$5) {
                this.sendSystemMessage(Component.translatable("block.minecraft.set_spawn"));
            }
            this.respawnPosition = blockPos1;
            this.respawnDimension = resourceKeyLevel0;
            this.respawnAngle = float2;
            this.respawnForced = boolean3;
        } else {
            this.respawnPosition = null;
            this.respawnDimension = Level.OVERWORLD;
            this.respawnAngle = 0.0F;
            this.respawnForced = false;
        }
    }

    public void trackChunk(ChunkPos chunkPos0, Packet<?> packet1) {
        this.connection.send(packet1);
    }

    public void untrackChunk(ChunkPos chunkPos0) {
        if (this.m_6084_()) {
            this.connection.send(new ClientboundForgetLevelChunkPacket(chunkPos0.x, chunkPos0.z));
        }
    }

    public SectionPos getLastSectionPos() {
        return this.lastSectionPos;
    }

    public void setLastSectionPos(SectionPos sectionPos0) {
        this.lastSectionPos = sectionPos0;
    }

    @Override
    public void playNotifySound(SoundEvent soundEvent0, SoundSource soundSource1, float float2, float float3) {
        this.connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundEvent0), soundSource1, this.m_20185_(), this.m_20186_(), this.m_20189_(), float2, float3, this.f_19796_.nextLong()));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddPlayerPacket(this);
    }

    @Override
    public ItemEntity drop(ItemStack itemStack0, boolean boolean1, boolean boolean2) {
        ItemEntity $$3 = super.drop(itemStack0, boolean1, boolean2);
        if ($$3 == null) {
            return null;
        } else {
            this.m_9236_().m_7967_($$3);
            ItemStack $$4 = $$3.getItem();
            if (boolean2) {
                if (!$$4.isEmpty()) {
                    this.awardStat(Stats.ITEM_DROPPED.get($$4.getItem()), itemStack0.getCount());
                }
                this.m_36220_(Stats.DROP);
            }
            return $$3;
        }
    }

    public TextFilter getTextFilter() {
        return this.textFilter;
    }

    public void setServerLevel(ServerLevel serverLevel0) {
        this.m_284535_(serverLevel0);
        this.gameMode.setLevel(serverLevel0);
    }

    @Nullable
    private static GameType readPlayerMode(@Nullable CompoundTag compoundTag0, String string1) {
        return compoundTag0 != null && compoundTag0.contains(string1, 99) ? GameType.byId(compoundTag0.getInt(string1)) : null;
    }

    private GameType calculateGameModeForNewPlayer(@Nullable GameType gameType0) {
        GameType $$1 = this.server.getForcedGameType();
        if ($$1 != null) {
            return $$1;
        } else {
            return gameType0 != null ? gameType0 : this.server.getDefaultGameType();
        }
    }

    public void loadGameTypes(@Nullable CompoundTag compoundTag0) {
        this.gameMode.setGameModeForPlayer(this.calculateGameModeForNewPlayer(readPlayerMode(compoundTag0, "playerGameType")), readPlayerMode(compoundTag0, "previousPlayerGameType"));
    }

    private void storeGameTypes(CompoundTag compoundTag0) {
        compoundTag0.putInt("playerGameType", this.gameMode.getGameModeForPlayer().getId());
        GameType $$1 = this.gameMode.getPreviousGameModeForPlayer();
        if ($$1 != null) {
            compoundTag0.putInt("previousPlayerGameType", $$1.getId());
        }
    }

    @Override
    public boolean isTextFilteringEnabled() {
        return this.textFilteringEnabled;
    }

    public boolean shouldFilterMessageTo(ServerPlayer serverPlayer0) {
        return serverPlayer0 == this ? false : this.textFilteringEnabled || serverPlayer0.textFilteringEnabled;
    }

    @Override
    public boolean mayInteract(Level level0, BlockPos blockPos1) {
        return super.m_142265_(level0, blockPos1) && level0.mayInteract(this, blockPos1);
    }

    @Override
    protected void updateUsingItem(ItemStack itemStack0) {
        CriteriaTriggers.USING_ITEM.trigger(this, itemStack0);
        super.m_142106_(itemStack0);
    }

    public boolean drop(boolean boolean0) {
        Inventory $$1 = this.m_150109_();
        ItemStack $$2 = $$1.removeFromSelected(boolean0);
        this.f_36096_.findSlot($$1, $$1.selected).ifPresent(p_287377_ -> this.f_36096_.setRemoteSlot(p_287377_, $$1.getSelected()));
        return this.drop($$2, false, true) != null;
    }

    public boolean allowsListing() {
        return this.allowsListing;
    }

    @Override
    public Optional<WardenSpawnTracker> getWardenSpawnTracker() {
        return Optional.of(this.wardenSpawnTracker);
    }

    @Override
    public void onItemPickup(ItemEntity itemEntity0) {
        super.m_21053_(itemEntity0);
        Entity $$1 = itemEntity0.getOwner();
        if ($$1 != null) {
            CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_PLAYER.trigger(this, itemEntity0.getItem(), $$1);
        }
    }

    public void setChatSession(RemoteChatSession remoteChatSession0) {
        this.chatSession = remoteChatSession0;
    }

    @Nullable
    public RemoteChatSession getChatSession() {
        return this.chatSession != null && this.chatSession.hasExpired() ? null : this.chatSession;
    }

    @Override
    public void indicateDamage(double double0, double double1) {
        this.f_263750_ = (float) (Mth.atan2(double1, double0) * 180.0F / (float) Math.PI - (double) this.m_146908_());
        this.connection.send(new ClientboundHurtAnimationPacket(this));
    }

    @Override
    public boolean startRiding(Entity entity0, boolean boolean1) {
        if (!super.m_7998_(entity0, boolean1)) {
            return false;
        } else {
            entity0.positionRider(this);
            this.connection.teleport(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
            if (entity0 instanceof LivingEntity $$2) {
                for (MobEffectInstance $$3 : $$2.getActiveEffects()) {
                    this.connection.send(new ClientboundUpdateMobEffectPacket(entity0.getId(), $$3));
                }
            }
            return true;
        }
    }

    @Override
    public void stopRiding() {
        Entity $$0 = this.m_20202_();
        super.m_8127_();
        if ($$0 instanceof LivingEntity $$1) {
            for (MobEffectInstance $$2 : $$1.getActiveEffects()) {
                this.connection.send(new ClientboundRemoveMobEffectPacket($$0.getId(), $$2.getEffect()));
            }
        }
    }
}