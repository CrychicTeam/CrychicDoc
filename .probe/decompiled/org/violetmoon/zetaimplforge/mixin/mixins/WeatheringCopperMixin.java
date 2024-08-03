package org.violetmoon.zetaimplforge.mixin.mixins;

import net.minecraft.world.level.block.WeatheringCopper;
import org.spongepowered.asm.mixin.Mixin;
import org.violetmoon.zeta.mixin.plugin.DelegateInterfaceMixin;
import org.violetmoon.zeta.mixin.plugin.DelegateReturnValueModifier;
import org.violetmoon.zetaimplforge.mixin.delegate.WeatheringCopperDelegate;

@DelegateInterfaceMixin(delegate = WeatheringCopperDelegate.class, methods = { @DelegateReturnValueModifier(target = { "getPrevious(Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/Optional;" }, delegate = "customWeatheringPrevious", desc = "(Ljava/util/Optional;Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/Optional;"), @DelegateReturnValueModifier(target = { "getFirst(Lnet/minecraft/world/level/block/state/BlockState;)Ljnet/minecraft/world/level/block/state/BlockState;" }, delegate = "customWeatheringFirst", desc = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;"), @DelegateReturnValueModifier(target = { "m_154899_(Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/Optional;" }, delegate = "customWeatheringPrevious", desc = "(Ljava/util/Optional;Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/Optional;"), @DelegateReturnValueModifier(target = { "m_154906_(Lnet/minecraft/world/level/block/state/BlockState;)Ljnet/minecraft/world/level/block/state/BlockState;" }, delegate = "customWeatheringFirst", desc = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;") })
@Mixin({ WeatheringCopper.class })
public interface WeatheringCopperMixin {
}