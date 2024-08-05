package net.minecraft.server.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class DemoMode extends ServerPlayerGameMode {

    public static final int DEMO_DAYS = 5;

    public static final int TOTAL_PLAY_TICKS = 120500;

    private boolean displayedIntro;

    private boolean demoHasEnded;

    private int demoEndedReminder;

    private int gameModeTicks;

    public DemoMode(ServerPlayer serverPlayer0) {
        super(serverPlayer0);
    }

    @Override
    public void tick() {
        super.tick();
        this.gameModeTicks++;
        long $$0 = this.f_9244_.m_46467_();
        long $$1 = $$0 / 24000L + 1L;
        if (!this.displayedIntro && this.gameModeTicks > 20) {
            this.displayedIntro = true;
            this.f_9245_.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 0.0F));
        }
        this.demoHasEnded = $$0 > 120500L;
        if (this.demoHasEnded) {
            this.demoEndedReminder++;
        }
        if ($$0 % 24000L == 500L) {
            if ($$1 <= 6L) {
                if ($$1 == 6L) {
                    this.f_9245_.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 104.0F));
                } else {
                    this.f_9245_.sendSystemMessage(Component.translatable("demo.day." + $$1));
                }
            }
        } else if ($$1 == 1L) {
            if ($$0 == 100L) {
                this.f_9245_.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 101.0F));
            } else if ($$0 == 175L) {
                this.f_9245_.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 102.0F));
            } else if ($$0 == 250L) {
                this.f_9245_.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 103.0F));
            }
        } else if ($$1 == 5L && $$0 % 24000L == 22000L) {
            this.f_9245_.sendSystemMessage(Component.translatable("demo.day.warning"));
        }
    }

    private void outputDemoReminder() {
        if (this.demoEndedReminder > 100) {
            this.f_9245_.sendSystemMessage(Component.translatable("demo.reminder"));
            this.demoEndedReminder = 0;
        }
    }

    @Override
    public void handleBlockBreakAction(BlockPos blockPos0, ServerboundPlayerActionPacket.Action serverboundPlayerActionPacketAction1, Direction direction2, int int3, int int4) {
        if (this.demoHasEnded) {
            this.outputDemoReminder();
        } else {
            super.handleBlockBreakAction(blockPos0, serverboundPlayerActionPacketAction1, direction2, int3, int4);
        }
    }

    @Override
    public InteractionResult useItem(ServerPlayer serverPlayer0, Level level1, ItemStack itemStack2, InteractionHand interactionHand3) {
        if (this.demoHasEnded) {
            this.outputDemoReminder();
            return InteractionResult.PASS;
        } else {
            return super.useItem(serverPlayer0, level1, itemStack2, interactionHand3);
        }
    }

    @Override
    public InteractionResult useItemOn(ServerPlayer serverPlayer0, Level level1, ItemStack itemStack2, InteractionHand interactionHand3, BlockHitResult blockHitResult4) {
        if (this.demoHasEnded) {
            this.outputDemoReminder();
            return InteractionResult.PASS;
        } else {
            return super.useItemOn(serverPlayer0, level1, itemStack2, interactionHand3, blockHitResult4);
        }
    }
}