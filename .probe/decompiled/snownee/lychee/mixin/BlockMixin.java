package snownee.lychee.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.lychee.random_block_ticking.RandomlyTickable;

@Mixin({ Block.class })
public class BlockMixin implements RandomlyTickable {

    @Unique
    private boolean lychee$randomlyTickable;

    @Inject(at = { @At("HEAD") }, method = { "isRandomlyTicking" }, cancellable = true)
    private void isRandomlyTicking(BlockState blockState, CallbackInfoReturnable<Boolean> ci) {
        if (this.lychee$randomlyTickable) {
            ci.setReturnValue(true);
        }
    }

    @Override
    public void lychee$setTickable(boolean randomlyTickable) {
        this.lychee$randomlyTickable = randomlyTickable;
    }

    @Override
    public boolean lychee$isTickable() {
        return this.lychee$randomlyTickable;
    }
}