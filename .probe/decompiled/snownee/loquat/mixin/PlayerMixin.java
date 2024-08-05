package snownee.loquat.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.loquat.core.RestrictInstance;
import snownee.loquat.core.select.SelectionManager;
import snownee.loquat.duck.LoquatPlayer;

@Mixin({ Player.class })
public class PlayerMixin implements LoquatPlayer {

    private SelectionManager loquat$selection;

    @Override
    public SelectionManager loquat$getSelectionManager() {
        if (this.loquat$selection == null) {
            this.loquat$selection = new SelectionManager(((Entity) this).level().isClientSide);
        }
        return this.loquat$selection;
    }

    @Inject(method = { "blockActionRestricted" }, at = { @At("HEAD") }, cancellable = true)
    private void loquat$blockActionRestricted(Level level, BlockPos pos, GameType gameMode, CallbackInfoReturnable<Boolean> cir) {
        if (!gameMode.isCreative() && RestrictInstance.of((Player) this).isRestricted(pos, RestrictInstance.RestrictBehavior.DESTROY)) {
            cir.setReturnValue(true);
        }
    }
}