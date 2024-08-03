package com.mna.mixins;

import com.mna.entities.constructs.ConstructAssemblyStand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ArmorStand.class })
public class ArmorStandMixin {

    @Inject(method = { "interactAt(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;" }, at = { @At("HEAD") }, cancellable = true)
    public void mna_convertToConstructStand(Player player, Vec3 vector, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callback) {
        ArmorStand self = (ArmorStand) this;
        ItemStack stack = player.m_21120_(hand);
        if (ConstructAssemblyStand.checkAndConvert(self, stack, player, hand)) {
            callback.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}