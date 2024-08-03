package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedLiquidBlockExtension;
import net.minecraft.world.level.block.LiquidBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ LiquidBlock.class })
public class MixinLiquidBlock implements InjectedLiquidBlockExtension {
}