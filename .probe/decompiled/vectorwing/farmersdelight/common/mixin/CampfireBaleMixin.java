package vectorwing.farmersdelight.common.mixin;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@Mixin({ CampfireBlock.class })
public abstract class CampfireBaleMixin {

    @Inject(at = { @At("HEAD") }, method = { "isSmokeSource" }, cancellable = true)
    public void isFDSmokeSource(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Set<Block> hayBales = Sets.newHashSet(new Block[] { ModBlocks.STRAW_BALE.get(), ModBlocks.RICE_BALE.get() });
        if (hayBales.contains(state.m_60734_())) {
            cir.setReturnValue(true);
        }
    }
}