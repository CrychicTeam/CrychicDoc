package me.jellysquid.mods.lithium.mixin.alloc.enum_values.piston_block;

import me.jellysquid.mods.lithium.common.util.DirectionConstants;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ PistonBaseBlock.class })
public class PistonBlockMixin {

    @Redirect(method = { "shouldExtend(Lnet/minecraft/world/RedstoneView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] removeAllocation() {
        return DirectionConstants.ALL;
    }
}