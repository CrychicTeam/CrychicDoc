package snownee.kiwi.mixin.customization;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.kiwi.customization.CustomizationHooks;
import snownee.kiwi.customization.block.KBlockSettings;

@Mixin({ HalfTransparentBlock.class })
public abstract class HalfTransparentBlockMixin {

    @Inject(method = { "skipRendering" }, at = { @At("HEAD") }, cancellable = true)
    private void kiwi$skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide, CallbackInfoReturnable<Boolean> cir) {
        KBlockSettings settings = KBlockSettings.of(this);
        if (settings != null && settings.glassType != null) {
            cir.setReturnValue(CustomizationHooks.skipGlassRendering(pState, pAdjacentBlockState, pSide));
        }
        settings = KBlockSettings.of(pAdjacentBlockState.m_60734_());
        if (settings != null && settings.glassType != null && pAdjacentBlockState.m_60719_(pState, pSide.getOpposite())) {
            cir.setReturnValue(true);
        }
    }
}