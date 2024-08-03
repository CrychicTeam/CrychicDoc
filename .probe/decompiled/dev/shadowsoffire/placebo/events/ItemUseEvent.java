package dev.shadowsoffire.placebo.events;

import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class ItemUseEvent extends PlayerEvent {

    private final InteractionHand hand;

    private final BlockPos pos;

    @Nullable
    private final Direction face;

    private InteractionResult cancellationResult = InteractionResult.PASS;

    private final UseOnContext ctx;

    public ItemUseEvent(UseOnContext ctx) {
        super(ctx.getPlayer());
        this.hand = (InteractionHand) Preconditions.checkNotNull(ctx.getHand(), "Null hand in ItemUseEvent!");
        this.pos = (BlockPos) Preconditions.checkNotNull(ctx.getClickedPos(), "Null position in ItemUseEvent!");
        this.face = ctx.getClickedFace();
        this.ctx = ctx;
    }

    public UseOnContext getContext() {
        return this.ctx;
    }

    @Nullable
    @Override
    public Player getEntity() {
        return super.getEntity();
    }

    @Nonnull
    public InteractionHand getHand() {
        return this.hand;
    }

    @Nonnull
    public ItemStack getItemStack() {
        return this.ctx.getItemInHand();
    }

    @Nonnull
    public BlockPos getPos() {
        return this.pos;
    }

    @Nullable
    public Direction getFace() {
        return this.face;
    }

    public Level getLevel() {
        return this.ctx.getLevel();
    }

    public InteractionResult getCancellationResult() {
        return this.cancellationResult;
    }

    public void setCancellationResult(InteractionResult result) {
        this.cancellationResult = result;
    }
}