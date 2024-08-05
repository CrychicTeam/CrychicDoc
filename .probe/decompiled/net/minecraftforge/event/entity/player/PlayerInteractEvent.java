package net.minecraftforge.event.entity.player;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class PlayerInteractEvent extends PlayerEvent {

    private final InteractionHand hand;

    private final BlockPos pos;

    @Nullable
    private final Direction face;

    private InteractionResult cancellationResult = InteractionResult.PASS;

    private PlayerInteractEvent(Player player, InteractionHand hand, BlockPos pos, @Nullable Direction face) {
        super((Player) Preconditions.checkNotNull(player, "Null player in PlayerInteractEvent!"));
        this.hand = (InteractionHand) Preconditions.checkNotNull(hand, "Null hand in PlayerInteractEvent!");
        this.pos = (BlockPos) Preconditions.checkNotNull(pos, "Null position in PlayerInteractEvent!");
        this.face = face;
    }

    @NotNull
    public InteractionHand getHand() {
        return this.hand;
    }

    @NotNull
    public ItemStack getItemStack() {
        return this.getEntity().m_21120_(this.hand);
    }

    @NotNull
    public BlockPos getPos() {
        return this.pos;
    }

    @Nullable
    public Direction getFace() {
        return this.face;
    }

    public Level getLevel() {
        return this.getEntity().m_9236_();
    }

    public LogicalSide getSide() {
        return this.getLevel().isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER;
    }

    public InteractionResult getCancellationResult() {
        return this.cancellationResult;
    }

    public void setCancellationResult(InteractionResult result) {
        this.cancellationResult = result;
    }

    @Cancelable
    public static class EntityInteract extends PlayerInteractEvent {

        private final Entity target;

        public EntityInteract(Player player, InteractionHand hand, Entity target) {
            super(player, hand, target.blockPosition(), null);
            this.target = target;
        }

        public Entity getTarget() {
            return this.target;
        }
    }

    @Cancelable
    public static class EntityInteractSpecific extends PlayerInteractEvent {

        private final Vec3 localPos;

        private final Entity target;

        public EntityInteractSpecific(Player player, InteractionHand hand, Entity target, Vec3 localPos) {
            super(player, hand, target.blockPosition(), null);
            this.localPos = localPos;
            this.target = target;
        }

        public Vec3 getLocalPos() {
            return this.localPos;
        }

        public Entity getTarget() {
            return this.target;
        }
    }

    @Cancelable
    public static class LeftClickBlock extends PlayerInteractEvent {

        private Result useBlock = Result.DEFAULT;

        private Result useItem = Result.DEFAULT;

        private final PlayerInteractEvent.LeftClickBlock.Action action;

        @Deprecated(since = "1.20.1", forRemoval = true)
        public LeftClickBlock(Player player, BlockPos pos, Direction face) {
            this(player, pos, face, PlayerInteractEvent.LeftClickBlock.Action.START);
        }

        @Internal
        public LeftClickBlock(Player player, BlockPos pos, Direction face, PlayerInteractEvent.LeftClickBlock.Action action) {
            super(player, InteractionHand.MAIN_HAND, pos, face);
            this.action = action;
        }

        public Result getUseBlock() {
            return this.useBlock;
        }

        public Result getUseItem() {
            return this.useItem;
        }

        @NotNull
        public PlayerInteractEvent.LeftClickBlock.Action getAction() {
            return this.action;
        }

        public void setUseBlock(Result triggerBlock) {
            this.useBlock = triggerBlock;
        }

        public void setUseItem(Result triggerItem) {
            this.useItem = triggerItem;
        }

        public void setCanceled(boolean canceled) {
            super.setCanceled(canceled);
            if (canceled) {
                this.useBlock = Result.DENY;
                this.useItem = Result.DENY;
            }
        }

        public static enum Action {

            START, STOP, ABORT, CLIENT_HOLD;

            public static PlayerInteractEvent.LeftClickBlock.Action convert(ServerboundPlayerActionPacket.Action action) {
                return switch(action) {
                    case START_DESTROY_BLOCK ->
                        START;
                    case STOP_DESTROY_BLOCK ->
                        STOP;
                    case ABORT_DESTROY_BLOCK ->
                        ABORT;
                    default ->
                        START;
                };
            }
        }
    }

    public static class LeftClickEmpty extends PlayerInteractEvent {

        public LeftClickEmpty(Player player) {
            super(player, InteractionHand.MAIN_HAND, player.m_20183_(), null);
        }
    }

    @Cancelable
    public static class RightClickBlock extends PlayerInteractEvent {

        private Result useBlock = Result.DEFAULT;

        private Result useItem = Result.DEFAULT;

        private BlockHitResult hitVec;

        public RightClickBlock(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
            super(player, hand, pos, hitVec.getDirection());
            this.hitVec = hitVec;
        }

        public Result getUseBlock() {
            return this.useBlock;
        }

        public Result getUseItem() {
            return this.useItem;
        }

        public BlockHitResult getHitVec() {
            return this.hitVec;
        }

        public void setUseBlock(Result triggerBlock) {
            this.useBlock = triggerBlock;
        }

        public void setUseItem(Result triggerItem) {
            this.useItem = triggerItem;
        }

        public void setCanceled(boolean canceled) {
            super.setCanceled(canceled);
            if (canceled) {
                this.useBlock = Result.DENY;
                this.useItem = Result.DENY;
            }
        }
    }

    public static class RightClickEmpty extends PlayerInteractEvent {

        public RightClickEmpty(Player player, InteractionHand hand) {
            super(player, hand, player.m_20183_(), null);
        }
    }

    @Cancelable
    public static class RightClickItem extends PlayerInteractEvent {

        public RightClickItem(Player player, InteractionHand hand) {
            super(player, hand, player.m_20183_(), null);
        }
    }
}