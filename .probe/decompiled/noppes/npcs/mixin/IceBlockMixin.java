package noppes.npcs.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import noppes.npcs.CustomNpcs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ IceBlock.class })
public class IceBlockMixin {

    @Inject(at = { @At("HEAD") }, method = { "randomTick" }, cancellable = true)
    private void setupAnimPre(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3, CallbackInfo ci) {
        if (!CustomNpcs.IceMeltsEnabled) {
            ci.cancel();
        }
    }
}