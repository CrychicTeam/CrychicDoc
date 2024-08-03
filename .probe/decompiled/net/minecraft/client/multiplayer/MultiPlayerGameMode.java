package net.minecraft.client.multiplayer;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.StatsCounter;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

public class MultiPlayerGameMode {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Minecraft minecraft;

    private final ClientPacketListener connection;

    private BlockPos destroyBlockPos = new BlockPos(-1, -1, -1);

    private ItemStack destroyingItem = ItemStack.EMPTY;

    private float destroyProgress;

    private float destroyTicks;

    private int destroyDelay;

    private boolean isDestroying;

    private GameType localPlayerMode = GameType.DEFAULT_MODE;

    @Nullable
    private GameType previousLocalPlayerMode;

    private int carriedIndex;

    public MultiPlayerGameMode(Minecraft minecraft0, ClientPacketListener clientPacketListener1) {
        this.minecraft = minecraft0;
        this.connection = clientPacketListener1;
    }

    public void adjustPlayer(Player player0) {
        this.localPlayerMode.updatePlayerAbilities(player0.getAbilities());
    }

    public void setLocalMode(GameType gameType0, @Nullable GameType gameType1) {
        this.localPlayerMode = gameType0;
        this.previousLocalPlayerMode = gameType1;
        this.localPlayerMode.updatePlayerAbilities(this.minecraft.player.m_150110_());
    }

    public void setLocalMode(GameType gameType0) {
        if (gameType0 != this.localPlayerMode) {
            this.previousLocalPlayerMode = this.localPlayerMode;
        }
        this.localPlayerMode = gameType0;
        this.localPlayerMode.updatePlayerAbilities(this.minecraft.player.m_150110_());
    }

    public boolean canHurtPlayer() {
        return this.localPlayerMode.isSurvival();
    }

    public boolean destroyBlock(BlockPos blockPos0) {
        if (this.minecraft.player.m_36187_(this.minecraft.level, blockPos0, this.localPlayerMode)) {
            return false;
        } else {
            Level $$1 = this.minecraft.level;
            BlockState $$2 = $$1.getBlockState(blockPos0);
            if (!this.minecraft.player.m_21205_().getItem().canAttackBlock($$2, $$1, blockPos0, this.minecraft.player)) {
                return false;
            } else {
                Block $$3 = $$2.m_60734_();
                if ($$3 instanceof GameMasterBlock && !this.minecraft.player.m_36337_()) {
                    return false;
                } else if ($$2.m_60795_()) {
                    return false;
                } else {
                    $$3.playerWillDestroy($$1, blockPos0, $$2, this.minecraft.player);
                    FluidState $$4 = $$1.getFluidState(blockPos0);
                    boolean $$5 = $$1.setBlock(blockPos0, $$4.createLegacyBlock(), 11);
                    if ($$5) {
                        $$3.destroy($$1, blockPos0, $$2);
                    }
                    return $$5;
                }
            }
        }
    }

    public boolean startDestroyBlock(BlockPos blockPos0, Direction direction1) {
        if (this.minecraft.player.m_36187_(this.minecraft.level, blockPos0, this.localPlayerMode)) {
            return false;
        } else if (!this.minecraft.level.m_6857_().isWithinBounds(blockPos0)) {
            return false;
        } else {
            if (this.localPlayerMode.isCreative()) {
                BlockState $$2 = this.minecraft.level.m_8055_(blockPos0);
                this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, blockPos0, $$2, 1.0F);
                this.startPrediction(this.minecraft.level, p_233757_ -> {
                    this.destroyBlock(blockPos0);
                    return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, blockPos0, direction1, p_233757_);
                });
                this.destroyDelay = 5;
            } else if (!this.isDestroying || !this.sameDestroyTarget(blockPos0)) {
                if (this.isDestroying) {
                    this.connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, this.destroyBlockPos, direction1));
                }
                BlockState $$3 = this.minecraft.level.m_8055_(blockPos0);
                this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, blockPos0, $$3, 0.0F);
                this.startPrediction(this.minecraft.level, p_233728_ -> {
                    boolean $$4 = !$$3.m_60795_();
                    if ($$4 && this.destroyProgress == 0.0F) {
                        $$3.m_60686_(this.minecraft.level, blockPos0, this.minecraft.player);
                    }
                    if ($$4 && $$3.m_60625_(this.minecraft.player, this.minecraft.player.m_9236_(), blockPos0) >= 1.0F) {
                        this.destroyBlock(blockPos0);
                    } else {
                        this.isDestroying = true;
                        this.destroyBlockPos = blockPos0;
                        this.destroyingItem = this.minecraft.player.m_21205_();
                        this.destroyProgress = 0.0F;
                        this.destroyTicks = 0.0F;
                        this.minecraft.level.destroyBlockProgress(this.minecraft.player.m_19879_(), this.destroyBlockPos, this.getDestroyStage());
                    }
                    return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, blockPos0, direction1, p_233728_);
                });
            }
            return true;
        }
    }

    public void stopDestroyBlock() {
        if (this.isDestroying) {
            BlockState $$0 = this.minecraft.level.m_8055_(this.destroyBlockPos);
            this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, this.destroyBlockPos, $$0, -1.0F);
            this.connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, this.destroyBlockPos, Direction.DOWN));
            this.isDestroying = false;
            this.destroyProgress = 0.0F;
            this.minecraft.level.destroyBlockProgress(this.minecraft.player.m_19879_(), this.destroyBlockPos, -1);
            this.minecraft.player.m_36334_();
        }
    }

    public boolean continueDestroyBlock(BlockPos blockPos0, Direction direction1) {
        this.ensureHasSentCarriedItem();
        if (this.destroyDelay > 0) {
            this.destroyDelay--;
            return true;
        } else if (this.localPlayerMode.isCreative() && this.minecraft.level.m_6857_().isWithinBounds(blockPos0)) {
            this.destroyDelay = 5;
            BlockState $$2 = this.minecraft.level.m_8055_(blockPos0);
            this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, blockPos0, $$2, 1.0F);
            this.startPrediction(this.minecraft.level, p_233753_ -> {
                this.destroyBlock(blockPos0);
                return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, blockPos0, direction1, p_233753_);
            });
            return true;
        } else if (this.sameDestroyTarget(blockPos0)) {
            BlockState $$3 = this.minecraft.level.m_8055_(blockPos0);
            if ($$3.m_60795_()) {
                this.isDestroying = false;
                return false;
            } else {
                this.destroyProgress = this.destroyProgress + $$3.m_60625_(this.minecraft.player, this.minecraft.player.m_9236_(), blockPos0);
                if (this.destroyTicks % 4.0F == 0.0F) {
                    SoundType $$4 = $$3.m_60827_();
                    this.minecraft.getSoundManager().play(new SimpleSoundInstance($$4.getHitSound(), SoundSource.BLOCKS, ($$4.getVolume() + 1.0F) / 8.0F, $$4.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), blockPos0));
                }
                this.destroyTicks++;
                this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, blockPos0, $$3, Mth.clamp(this.destroyProgress, 0.0F, 1.0F));
                if (this.destroyProgress >= 1.0F) {
                    this.isDestroying = false;
                    this.startPrediction(this.minecraft.level, p_233739_ -> {
                        this.destroyBlock(blockPos0);
                        return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, blockPos0, direction1, p_233739_);
                    });
                    this.destroyProgress = 0.0F;
                    this.destroyTicks = 0.0F;
                    this.destroyDelay = 5;
                }
                this.minecraft.level.destroyBlockProgress(this.minecraft.player.m_19879_(), this.destroyBlockPos, this.getDestroyStage());
                return true;
            }
        } else {
            return this.startDestroyBlock(blockPos0, direction1);
        }
    }

    private void startPrediction(ClientLevel clientLevel0, PredictiveAction predictiveAction1) {
        try (BlockStatePredictionHandler $$2 = clientLevel0.getBlockStatePredictionHandler().startPredicting()) {
            int $$3 = $$2.currentSequence();
            Packet<ServerGamePacketListener> $$4 = predictiveAction1.predict($$3);
            this.connection.send($$4);
        }
    }

    public float getPickRange() {
        return this.localPlayerMode.isCreative() ? 5.0F : 4.5F;
    }

    public void tick() {
        this.ensureHasSentCarriedItem();
        if (this.connection.getConnection().isConnected()) {
            this.connection.getConnection().tick();
        } else {
            this.connection.getConnection().handleDisconnection();
        }
    }

    private boolean sameDestroyTarget(BlockPos blockPos0) {
        ItemStack $$1 = this.minecraft.player.m_21205_();
        return blockPos0.equals(this.destroyBlockPos) && ItemStack.isSameItemSameTags($$1, this.destroyingItem);
    }

    private void ensureHasSentCarriedItem() {
        int $$0 = this.minecraft.player.m_150109_().selected;
        if ($$0 != this.carriedIndex) {
            this.carriedIndex = $$0;
            this.connection.send(new ServerboundSetCarriedItemPacket(this.carriedIndex));
        }
    }

    public InteractionResult useItemOn(LocalPlayer localPlayer0, InteractionHand interactionHand1, BlockHitResult blockHitResult2) {
        this.ensureHasSentCarriedItem();
        if (!this.minecraft.level.m_6857_().isWithinBounds(blockHitResult2.getBlockPos())) {
            return InteractionResult.FAIL;
        } else {
            MutableObject<InteractionResult> $$3 = new MutableObject();
            this.startPrediction(this.minecraft.level, p_233745_ -> {
                $$3.setValue(this.performUseItemOn(localPlayer0, interactionHand1, blockHitResult2));
                return new ServerboundUseItemOnPacket(interactionHand1, blockHitResult2, p_233745_);
            });
            return (InteractionResult) $$3.getValue();
        }
    }

    private InteractionResult performUseItemOn(LocalPlayer localPlayer0, InteractionHand interactionHand1, BlockHitResult blockHitResult2) {
        BlockPos $$3 = blockHitResult2.getBlockPos();
        ItemStack $$4 = localPlayer0.m_21120_(interactionHand1);
        if (this.localPlayerMode == GameType.SPECTATOR) {
            return InteractionResult.SUCCESS;
        } else {
            boolean $$5 = !localPlayer0.m_21205_().isEmpty() || !localPlayer0.m_21206_().isEmpty();
            boolean $$6 = localPlayer0.m_36341_() && $$5;
            if (!$$6) {
                BlockState $$7 = this.minecraft.level.m_8055_($$3);
                if (!this.connection.isFeatureEnabled($$7.m_60734_().m_245183_())) {
                    return InteractionResult.FAIL;
                }
                InteractionResult $$8 = $$7.m_60664_(this.minecraft.level, localPlayer0, interactionHand1, blockHitResult2);
                if ($$8.consumesAction()) {
                    return $$8;
                }
            }
            if (!$$4.isEmpty() && !localPlayer0.m_36335_().isOnCooldown($$4.getItem())) {
                UseOnContext $$9 = new UseOnContext(localPlayer0, interactionHand1, blockHitResult2);
                InteractionResult $$11;
                if (this.localPlayerMode.isCreative()) {
                    int $$10 = $$4.getCount();
                    $$11 = $$4.useOn($$9);
                    $$4.setCount($$10);
                } else {
                    $$11 = $$4.useOn($$9);
                }
                return $$11;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public InteractionResult useItem(Player player0, InteractionHand interactionHand1) {
        if (this.localPlayerMode == GameType.SPECTATOR) {
            return InteractionResult.PASS;
        } else {
            this.ensureHasSentCarriedItem();
            this.connection.send(new ServerboundMovePlayerPacket.PosRot(player0.m_20185_(), player0.m_20186_(), player0.m_20189_(), player0.m_146908_(), player0.m_146909_(), player0.m_20096_()));
            MutableObject<InteractionResult> $$2 = new MutableObject();
            this.startPrediction(this.minecraft.level, p_233720_ -> {
                ServerboundUseItemPacket $$4 = new ServerboundUseItemPacket(interactionHand1, p_233720_);
                ItemStack $$5 = player0.m_21120_(interactionHand1);
                if (player0.getCooldowns().isOnCooldown($$5.getItem())) {
                    $$2.setValue(InteractionResult.PASS);
                    return $$4;
                } else {
                    InteractionResultHolder<ItemStack> $$6 = $$5.use(this.minecraft.level, player0, interactionHand1);
                    ItemStack $$7 = $$6.getObject();
                    if ($$7 != $$5) {
                        player0.m_21008_(interactionHand1, $$7);
                    }
                    $$2.setValue($$6.getResult());
                    return $$4;
                }
            });
            return (InteractionResult) $$2.getValue();
        }
    }

    public LocalPlayer createPlayer(ClientLevel clientLevel0, StatsCounter statsCounter1, ClientRecipeBook clientRecipeBook2) {
        return this.createPlayer(clientLevel0, statsCounter1, clientRecipeBook2, false, false);
    }

    public LocalPlayer createPlayer(ClientLevel clientLevel0, StatsCounter statsCounter1, ClientRecipeBook clientRecipeBook2, boolean boolean3, boolean boolean4) {
        return new LocalPlayer(this.minecraft, clientLevel0, this.connection, statsCounter1, clientRecipeBook2, boolean3, boolean4);
    }

    public void attack(Player player0, Entity entity1) {
        this.ensureHasSentCarriedItem();
        this.connection.send(ServerboundInteractPacket.createAttackPacket(entity1, player0.m_6144_()));
        if (this.localPlayerMode != GameType.SPECTATOR) {
            player0.attack(entity1);
            player0.resetAttackStrengthTicker();
        }
    }

    public InteractionResult interact(Player player0, Entity entity1, InteractionHand interactionHand2) {
        this.ensureHasSentCarriedItem();
        this.connection.send(ServerboundInteractPacket.createInteractionPacket(entity1, player0.m_6144_(), interactionHand2));
        return this.localPlayerMode == GameType.SPECTATOR ? InteractionResult.PASS : player0.interactOn(entity1, interactionHand2);
    }

    public InteractionResult interactAt(Player player0, Entity entity1, EntityHitResult entityHitResult2, InteractionHand interactionHand3) {
        this.ensureHasSentCarriedItem();
        Vec3 $$4 = entityHitResult2.m_82450_().subtract(entity1.getX(), entity1.getY(), entity1.getZ());
        this.connection.send(ServerboundInteractPacket.createInteractionPacket(entity1, player0.m_6144_(), interactionHand3, $$4));
        return this.localPlayerMode == GameType.SPECTATOR ? InteractionResult.PASS : entity1.interactAt(player0, $$4, interactionHand3);
    }

    public void handleInventoryMouseClick(int int0, int int1, int int2, ClickType clickType3, Player player4) {
        AbstractContainerMenu $$5 = player4.containerMenu;
        if (int0 != $$5.containerId) {
            LOGGER.warn("Ignoring click in mismatching container. Click in {}, player has {}.", int0, $$5.containerId);
        } else {
            NonNullList<Slot> $$6 = $$5.slots;
            int $$7 = $$6.size();
            List<ItemStack> $$8 = Lists.newArrayListWithCapacity($$7);
            for (Slot $$9 : $$6) {
                $$8.add($$9.getItem().copy());
            }
            $$5.clicked(int1, int2, clickType3, player4);
            Int2ObjectMap<ItemStack> $$10 = new Int2ObjectOpenHashMap();
            for (int $$11 = 0; $$11 < $$7; $$11++) {
                ItemStack $$12 = (ItemStack) $$8.get($$11);
                ItemStack $$13 = $$6.get($$11).getItem();
                if (!ItemStack.matches($$12, $$13)) {
                    $$10.put($$11, $$13.copy());
                }
            }
            this.connection.send(new ServerboundContainerClickPacket(int0, $$5.getStateId(), int1, int2, clickType3, $$5.getCarried().copy(), $$10));
        }
    }

    public void handlePlaceRecipe(int int0, Recipe<?> recipe1, boolean boolean2) {
        this.connection.send(new ServerboundPlaceRecipePacket(int0, recipe1, boolean2));
    }

    public void handleInventoryButtonClick(int int0, int int1) {
        this.connection.send(new ServerboundContainerButtonClickPacket(int0, int1));
    }

    public void handleCreativeModeItemAdd(ItemStack itemStack0, int int1) {
        if (this.localPlayerMode.isCreative() && this.connection.isFeatureEnabled(itemStack0.getItem().requiredFeatures())) {
            this.connection.send(new ServerboundSetCreativeModeSlotPacket(int1, itemStack0));
        }
    }

    public void handleCreativeModeItemDrop(ItemStack itemStack0) {
        if (this.localPlayerMode.isCreative() && !itemStack0.isEmpty() && this.connection.isFeatureEnabled(itemStack0.getItem().requiredFeatures())) {
            this.connection.send(new ServerboundSetCreativeModeSlotPacket(-1, itemStack0));
        }
    }

    public void releaseUsingItem(Player player0) {
        this.ensureHasSentCarriedItem();
        this.connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN));
        player0.m_21253_();
    }

    public boolean hasExperience() {
        return this.localPlayerMode.isSurvival();
    }

    public boolean hasMissTime() {
        return !this.localPlayerMode.isCreative();
    }

    public boolean hasInfiniteItems() {
        return this.localPlayerMode.isCreative();
    }

    public boolean hasFarPickRange() {
        return this.localPlayerMode.isCreative();
    }

    public boolean isServerControlledInventory() {
        return this.minecraft.player.m_20159_() && this.minecraft.player.m_20202_() instanceof HasCustomInventoryScreen;
    }

    public boolean isAlwaysFlying() {
        return this.localPlayerMode == GameType.SPECTATOR;
    }

    @Nullable
    public GameType getPreviousPlayerMode() {
        return this.previousLocalPlayerMode;
    }

    public GameType getPlayerMode() {
        return this.localPlayerMode;
    }

    public boolean isDestroying() {
        return this.isDestroying;
    }

    public int getDestroyStage() {
        return this.destroyProgress > 0.0F ? (int) (this.destroyProgress * 10.0F) : -1;
    }

    public void handlePickItem(int int0) {
        this.connection.send(new ServerboundPickItemPacket(int0));
    }
}