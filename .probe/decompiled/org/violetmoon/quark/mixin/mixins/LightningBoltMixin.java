package org.violetmoon.quark.mixin.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Optional;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.zeta.block.ext.CustomWeatheringCopper;

@Mixin({ LightningBolt.class })
public class LightningBoltMixin {

    @WrapOperation(method = { "clearCopperOnLightningStrike" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/WeatheringCopper;getFirst(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;") })
    private static BlockState customCopperGetFirst(BlockState baseState, Operation<BlockState> original) {
        return baseState.m_60734_() instanceof CustomWeatheringCopper customCopper ? customCopper.getFirst(baseState) : (BlockState) original.call(new Object[] { baseState });
    }

    @WrapOperation(method = { "randomStepCleaningCopper" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/WeatheringCopper;getPrevious(Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/Optional;") })
    private static Optional<BlockState> customCopperGetPrevious(BlockState baseState, Operation<Optional<BlockState>> original) {
        return baseState.m_60734_() instanceof CustomWeatheringCopper customCopper ? customCopper.getPrevious(baseState) : (Optional) original.call(new Object[] { baseState });
    }
}