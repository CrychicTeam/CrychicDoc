package me.jellysquid.mods.lithium.mixin.world.block_entity_ticking.sleeping;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ServerLevel.class })
public class ServerWorldMixin {

    @Redirect(method = { "dumpBlockEntities(Ljava/io/Writer;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/BlockEntityTickInvoker;getPos()Lnet/minecraft/util/math/BlockPos;"))
    private BlockPos getPosOrOrigin(TickingBlockEntity instance) {
        BlockPos pos = instance.getPos();
        return pos == null ? BlockPos.ZERO : pos;
    }
}