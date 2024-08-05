package dev.xkmc.l2complements.mixin;

import dev.xkmc.l2complements.events.SpecialEquipmentEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ServerPlayerGameMode.class })
public class ServerPlayerGameModeMixin {

    @Shadow
    @Final
    protected ServerPlayer player;

    @Inject(at = { @At("HEAD") }, method = { "destroyBlock" })
    public void l2complements_destroyBlock_markPlayerBeginBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        SpecialEquipmentEvents.pushPlayer(this.player, pos);
    }

    @Inject(at = { @At("RETURN") }, method = { "destroyBlock" })
    public void l2complements_destroyBlock_markPlayerEndBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        SpecialEquipmentEvents.popPlayer(this.player);
    }
}