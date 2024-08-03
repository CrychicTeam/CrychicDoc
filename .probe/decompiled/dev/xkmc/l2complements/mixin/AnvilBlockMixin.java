package dev.xkmc.l2complements.mixin;

import dev.xkmc.l2complements.init.registrate.LCBlocks;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ AnvilBlock.class })
public class AnvilBlockMixin {

    @Inject(method = { "damage" }, at = { @At("HEAD") }, cancellable = true)
    private static void l2complements_damage_eternalNotBreaking(BlockState state, CallbackInfoReturnable<BlockState> info) {
        if (state.m_60734_() == LCBlocks.ETERNAL_ANVIL.get()) {
            info.setReturnValue(state);
            info.cancel();
        }
    }
}