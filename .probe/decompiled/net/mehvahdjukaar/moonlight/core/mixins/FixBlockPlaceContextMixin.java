package net.mehvahdjukaar.moonlight.core.mixins;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ BlockPlaceContext.class })
public abstract class FixBlockPlaceContextMixin extends UseOnContext {

    @Shadow
    protected boolean replaceClicked;

    protected FixBlockPlaceContextMixin(Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        super(player, interactionHand, blockHitResult);
    }

    @Inject(method = { "getNearestLookingDirection" }, at = { @At("HEAD") }, cancellable = true)
    public void fixNotAccountingForNullPlayer1(CallbackInfoReturnable<Direction> cir) {
        if (this.m_43723_() == null) {
            cir.setReturnValue(Direction.NORTH);
        }
    }

    @Inject(method = { "getNearestLookingVerticalDirection" }, at = { @At("HEAD") }, cancellable = true)
    public void fixNotAccountingForNullPlayer2(CallbackInfoReturnable<Direction> cir) {
        if (this.m_43723_() == null) {
            cir.setReturnValue(Direction.UP);
        }
    }

    @Inject(method = { "getNearestLookingDirections" }, at = { @At("HEAD") }, cancellable = true)
    public void fixNotAccountingForNullPlayer3(CallbackInfoReturnable<Direction[]> cir) {
        if (this.m_43723_() == null) {
            Direction[] directions = Direction.values();
            if (this.replaceClicked) {
                cir.setReturnValue(directions);
            } else {
                Direction direction = this.m_43719_();
                int i = 0;
                while (i < directions.length && directions[i] != direction.getOpposite()) {
                    i++;
                }
                if (i > 0) {
                    System.arraycopy(directions, 0, directions, 1, i);
                    directions[0] = direction.getOpposite();
                }
                cir.setReturnValue(directions);
            }
        }
    }
}