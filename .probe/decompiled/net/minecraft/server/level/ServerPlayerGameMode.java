package net.minecraft.server.level;

import com.mojang.logging.LogUtils;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class ServerPlayerGameMode {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected ServerLevel level;

    protected final ServerPlayer player;

    private GameType gameModeForPlayer = GameType.DEFAULT_MODE;

    @Nullable
    private GameType previousGameModeForPlayer;

    private boolean isDestroyingBlock;

    private int destroyProgressStart;

    private BlockPos destroyPos = BlockPos.ZERO;

    private int gameTicks;

    private boolean hasDelayedDestroy;

    private BlockPos delayedDestroyPos = BlockPos.ZERO;

    private int delayedTickStart;

    private int lastSentState = -1;

    public ServerPlayerGameMode(ServerPlayer serverPlayer0) {
        this.player = serverPlayer0;
        this.level = serverPlayer0.serverLevel();
    }

    public boolean changeGameModeForPlayer(GameType gameType0) {
        if (gameType0 == this.gameModeForPlayer) {
            return false;
        } else {
            this.setGameModeForPlayer(gameType0, this.previousGameModeForPlayer);
            this.player.onUpdateAbilities();
            this.player.server.getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE, this.player));
            this.level.updateSleepingPlayerList();
            return true;
        }
    }

    protected void setGameModeForPlayer(GameType gameType0, @Nullable GameType gameType1) {
        this.previousGameModeForPlayer = gameType1;
        this.gameModeForPlayer = gameType0;
        gameType0.updatePlayerAbilities(this.player.m_150110_());
    }

    public GameType getGameModeForPlayer() {
        return this.gameModeForPlayer;
    }

    @Nullable
    public GameType getPreviousGameModeForPlayer() {
        return this.previousGameModeForPlayer;
    }

    public boolean isSurvival() {
        return this.gameModeForPlayer.isSurvival();
    }

    public boolean isCreative() {
        return this.gameModeForPlayer.isCreative();
    }

    public void tick() {
        this.gameTicks++;
        if (this.hasDelayedDestroy) {
            BlockState $$0 = this.level.m_8055_(this.delayedDestroyPos);
            if ($$0.m_60795_()) {
                this.hasDelayedDestroy = false;
            } else {
                float $$1 = this.incrementDestroyProgress($$0, this.delayedDestroyPos, this.delayedTickStart);
                if ($$1 >= 1.0F) {
                    this.hasDelayedDestroy = false;
                    this.destroyBlock(this.delayedDestroyPos);
                }
            }
        } else if (this.isDestroyingBlock) {
            BlockState $$2 = this.level.m_8055_(this.destroyPos);
            if ($$2.m_60795_()) {
                this.level.destroyBlockProgress(this.player.m_19879_(), this.destroyPos, -1);
                this.lastSentState = -1;
                this.isDestroyingBlock = false;
            } else {
                this.incrementDestroyProgress($$2, this.destroyPos, this.destroyProgressStart);
            }
        }
    }

    private float incrementDestroyProgress(BlockState blockState0, BlockPos blockPos1, int int2) {
        int $$3 = this.gameTicks - int2;
        float $$4 = blockState0.m_60625_(this.player, this.player.m_9236_(), blockPos1) * (float) ($$3 + 1);
        int $$5 = (int) ($$4 * 10.0F);
        if ($$5 != this.lastSentState) {
            this.level.destroyBlockProgress(this.player.m_19879_(), blockPos1, $$5);
            this.lastSentState = $$5;
        }
        return $$4;
    }

    private void debugLogging(BlockPos blockPos0, boolean boolean1, int int2, String string3) {
    }

    public void handleBlockBreakAction(BlockPos blockPos0, ServerboundPlayerActionPacket.Action serverboundPlayerActionPacketAction1, Direction direction2, int int3, int int4) {
        if (this.player.m_146892_().distanceToSqr(Vec3.atCenterOf(blockPos0)) > ServerGamePacketListenerImpl.MAX_INTERACTION_DISTANCE) {
            this.debugLogging(blockPos0, false, int4, "too far");
        } else if (blockPos0.m_123342_() >= int3) {
            this.player.connection.send(new ClientboundBlockUpdatePacket(blockPos0, this.level.m_8055_(blockPos0)));
            this.debugLogging(blockPos0, false, int4, "too high");
        } else {
            if (serverboundPlayerActionPacketAction1 == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                if (!this.level.mayInteract(this.player, blockPos0)) {
                    this.player.connection.send(new ClientboundBlockUpdatePacket(blockPos0, this.level.m_8055_(blockPos0)));
                    this.debugLogging(blockPos0, false, int4, "may not interact");
                    return;
                }
                if (this.isCreative()) {
                    this.destroyAndAck(blockPos0, int4, "creative destroy");
                    return;
                }
                if (this.player.m_36187_(this.level, blockPos0, this.gameModeForPlayer)) {
                    this.player.connection.send(new ClientboundBlockUpdatePacket(blockPos0, this.level.m_8055_(blockPos0)));
                    this.debugLogging(blockPos0, false, int4, "block action restricted");
                    return;
                }
                this.destroyProgressStart = this.gameTicks;
                float $$5 = 1.0F;
                BlockState $$6 = this.level.m_8055_(blockPos0);
                if (!$$6.m_60795_()) {
                    $$6.m_60686_(this.level, blockPos0, this.player);
                    $$5 = $$6.m_60625_(this.player, this.player.m_9236_(), blockPos0);
                }
                if (!$$6.m_60795_() && $$5 >= 1.0F) {
                    this.destroyAndAck(blockPos0, int4, "insta mine");
                } else {
                    if (this.isDestroyingBlock) {
                        this.player.connection.send(new ClientboundBlockUpdatePacket(this.destroyPos, this.level.m_8055_(this.destroyPos)));
                        this.debugLogging(blockPos0, false, int4, "abort destroying since another started (client insta mine, server disagreed)");
                    }
                    this.isDestroyingBlock = true;
                    this.destroyPos = blockPos0.immutable();
                    int $$7 = (int) ($$5 * 10.0F);
                    this.level.destroyBlockProgress(this.player.m_19879_(), blockPos0, $$7);
                    this.debugLogging(blockPos0, true, int4, "actual start of destroying");
                    this.lastSentState = $$7;
                }
            } else if (serverboundPlayerActionPacketAction1 == ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK) {
                if (blockPos0.equals(this.destroyPos)) {
                    int $$8 = this.gameTicks - this.destroyProgressStart;
                    BlockState $$9 = this.level.m_8055_(blockPos0);
                    if (!$$9.m_60795_()) {
                        float $$10 = $$9.m_60625_(this.player, this.player.m_9236_(), blockPos0) * (float) ($$8 + 1);
                        if ($$10 >= 0.7F) {
                            this.isDestroyingBlock = false;
                            this.level.destroyBlockProgress(this.player.m_19879_(), blockPos0, -1);
                            this.destroyAndAck(blockPos0, int4, "destroyed");
                            return;
                        }
                        if (!this.hasDelayedDestroy) {
                            this.isDestroyingBlock = false;
                            this.hasDelayedDestroy = true;
                            this.delayedDestroyPos = blockPos0;
                            this.delayedTickStart = this.destroyProgressStart;
                        }
                    }
                }
                this.debugLogging(blockPos0, true, int4, "stopped destroying");
            } else if (serverboundPlayerActionPacketAction1 == ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) {
                this.isDestroyingBlock = false;
                if (!Objects.equals(this.destroyPos, blockPos0)) {
                    LOGGER.warn("Mismatch in destroy block pos: {} {}", this.destroyPos, blockPos0);
                    this.level.destroyBlockProgress(this.player.m_19879_(), this.destroyPos, -1);
                    this.debugLogging(blockPos0, true, int4, "aborted mismatched destroying");
                }
                this.level.destroyBlockProgress(this.player.m_19879_(), blockPos0, -1);
                this.debugLogging(blockPos0, true, int4, "aborted destroying");
            }
        }
    }

    public void destroyAndAck(BlockPos blockPos0, int int1, String string2) {
        if (this.destroyBlock(blockPos0)) {
            this.debugLogging(blockPos0, true, int1, string2);
        } else {
            this.player.connection.send(new ClientboundBlockUpdatePacket(blockPos0, this.level.m_8055_(blockPos0)));
            this.debugLogging(blockPos0, false, int1, string2);
        }
    }

    public boolean destroyBlock(BlockPos blockPos0) {
        BlockState $$1 = this.level.m_8055_(blockPos0);
        if (!this.player.m_21205_().getItem().canAttackBlock($$1, this.level, blockPos0, this.player)) {
            return false;
        } else {
            BlockEntity $$2 = this.level.m_7702_(blockPos0);
            Block $$3 = $$1.m_60734_();
            if ($$3 instanceof GameMasterBlock && !this.player.m_36337_()) {
                this.level.sendBlockUpdated(blockPos0, $$1, $$1, 3);
                return false;
            } else if (this.player.m_36187_(this.level, blockPos0, this.gameModeForPlayer)) {
                return false;
            } else {
                $$3.playerWillDestroy(this.level, blockPos0, $$1, this.player);
                boolean $$4 = this.level.m_7471_(blockPos0, false);
                if ($$4) {
                    $$3.destroy(this.level, blockPos0, $$1);
                }
                if (this.isCreative()) {
                    return true;
                } else {
                    ItemStack $$5 = this.player.m_21205_();
                    ItemStack $$6 = $$5.copy();
                    boolean $$7 = this.player.m_36298_($$1);
                    $$5.mineBlock(this.level, $$1, blockPos0, this.player);
                    if ($$4 && $$7) {
                        $$3.playerDestroy(this.level, this.player, blockPos0, $$1, $$2, $$6);
                    }
                    return true;
                }
            }
        }
    }

    public InteractionResult useItem(ServerPlayer serverPlayer0, Level level1, ItemStack itemStack2, InteractionHand interactionHand3) {
        if (this.gameModeForPlayer == GameType.SPECTATOR) {
            return InteractionResult.PASS;
        } else if (serverPlayer0.m_36335_().isOnCooldown(itemStack2.getItem())) {
            return InteractionResult.PASS;
        } else {
            int $$4 = itemStack2.getCount();
            int $$5 = itemStack2.getDamageValue();
            InteractionResultHolder<ItemStack> $$6 = itemStack2.use(level1, serverPlayer0, interactionHand3);
            ItemStack $$7 = $$6.getObject();
            if ($$7 == itemStack2 && $$7.getCount() == $$4 && $$7.getUseDuration() <= 0 && $$7.getDamageValue() == $$5) {
                return $$6.getResult();
            } else if ($$6.getResult() == InteractionResult.FAIL && $$7.getUseDuration() > 0 && !serverPlayer0.m_6117_()) {
                return $$6.getResult();
            } else {
                if (itemStack2 != $$7) {
                    serverPlayer0.m_21008_(interactionHand3, $$7);
                }
                if (this.isCreative() && $$7 != ItemStack.EMPTY) {
                    $$7.setCount($$4);
                    if ($$7.isDamageableItem() && $$7.getDamageValue() != $$5) {
                        $$7.setDamageValue($$5);
                    }
                }
                if ($$7.isEmpty()) {
                    serverPlayer0.m_21008_(interactionHand3, ItemStack.EMPTY);
                }
                if (!serverPlayer0.m_6117_()) {
                    serverPlayer0.f_36095_.m_150429_();
                }
                return $$6.getResult();
            }
        }
    }

    public InteractionResult useItemOn(ServerPlayer serverPlayer0, Level level1, ItemStack itemStack2, InteractionHand interactionHand3, BlockHitResult blockHitResult4) {
        BlockPos $$5 = blockHitResult4.getBlockPos();
        BlockState $$6 = level1.getBlockState($$5);
        if (!$$6.m_60734_().m_245993_(level1.m_246046_())) {
            return InteractionResult.FAIL;
        } else if (this.gameModeForPlayer == GameType.SPECTATOR) {
            MenuProvider $$7 = $$6.m_60750_(level1, $$5);
            if ($$7 != null) {
                serverPlayer0.openMenu($$7);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else {
            boolean $$8 = !serverPlayer0.m_21205_().isEmpty() || !serverPlayer0.m_21206_().isEmpty();
            boolean $$9 = serverPlayer0.m_36341_() && $$8;
            ItemStack $$10 = itemStack2.copy();
            if (!$$9) {
                InteractionResult $$11 = $$6.m_60664_(level1, serverPlayer0, interactionHand3, blockHitResult4);
                if ($$11.consumesAction()) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer0, $$5, $$10);
                    return $$11;
                }
            }
            if (!itemStack2.isEmpty() && !serverPlayer0.m_36335_().isOnCooldown(itemStack2.getItem())) {
                UseOnContext $$12 = new UseOnContext(serverPlayer0, interactionHand3, blockHitResult4);
                InteractionResult $$14;
                if (this.isCreative()) {
                    int $$13 = itemStack2.getCount();
                    $$14 = itemStack2.useOn($$12);
                    itemStack2.setCount($$13);
                } else {
                    $$14 = itemStack2.useOn($$12);
                }
                if ($$14.consumesAction()) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer0, $$5, $$10);
                }
                return $$14;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public void setLevel(ServerLevel serverLevel0) {
        this.level = serverLevel0;
    }
}