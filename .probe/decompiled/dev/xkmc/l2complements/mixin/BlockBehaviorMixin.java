package dev.xkmc.l2complements.mixin;

import dev.xkmc.l2complements.content.item.equipments.SculkiumTool;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ BlockBehaviour.class })
public class BlockBehaviorMixin {

    @Inject(at = { @At("HEAD") }, method = { "getDestroyProgress" })
    public void l2complements_getDestroyProgress_cacheBlockHardness(BlockState state, Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        SculkiumTool.cachedHardness = state.m_60800_(level, pos);
    }
}