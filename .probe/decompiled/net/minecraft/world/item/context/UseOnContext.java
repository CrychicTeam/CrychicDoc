package net.minecraft.world.item.context;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class UseOnContext {

    @Nullable
    private final Player player;

    private final InteractionHand hand;

    private final BlockHitResult hitResult;

    private final Level level;

    private final ItemStack itemStack;

    public UseOnContext(Player player0, InteractionHand interactionHand1, BlockHitResult blockHitResult2) {
        this(player0.m_9236_(), player0, interactionHand1, player0.m_21120_(interactionHand1), blockHitResult2);
    }

    protected UseOnContext(Level level0, @Nullable Player player1, InteractionHand interactionHand2, ItemStack itemStack3, BlockHitResult blockHitResult4) {
        this.player = player1;
        this.hand = interactionHand2;
        this.hitResult = blockHitResult4;
        this.itemStack = itemStack3;
        this.level = level0;
    }

    protected final BlockHitResult getHitResult() {
        return this.hitResult;
    }

    public BlockPos getClickedPos() {
        return this.hitResult.getBlockPos();
    }

    public Direction getClickedFace() {
        return this.hitResult.getDirection();
    }

    public Vec3 getClickLocation() {
        return this.hitResult.m_82450_();
    }

    public boolean isInside() {
        return this.hitResult.isInside();
    }

    public ItemStack getItemInHand() {
        return this.itemStack;
    }

    @Nullable
    public Player getPlayer() {
        return this.player;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public Level getLevel() {
        return this.level;
    }

    public Direction getHorizontalDirection() {
        return this.player == null ? Direction.NORTH : this.player.m_6350_();
    }

    public boolean isSecondaryUseActive() {
        return this.player != null && this.player.isSecondaryUseActive();
    }

    public float getRotation() {
        return this.player == null ? 0.0F : this.player.m_146908_();
    }
}