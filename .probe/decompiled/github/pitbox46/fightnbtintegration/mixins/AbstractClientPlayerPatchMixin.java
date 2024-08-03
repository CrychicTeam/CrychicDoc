package github.pitbox46.fightnbtintegration.mixins;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = { AbstractClientPlayerPatch.class }, remap = false)
public abstract class AbstractClientPlayerPatchMixin<T extends AbstractClientPlayer> extends PlayerPatch<T> {

    @Inject(at = { @At(value = "INVOKE", target = "Lyesman/epicfight/api/client/animation/ClientAnimator;isAiming()Z", ordinal = 0) }, method = { "updateMotion" })
    private void setTetraCrossbowsAsCrossbow(boolean considerInaction, CallbackInfo ci) {
        ItemStack mainhandItem = this.original.m_21205_();
        if (mainhandItem.getItem().getClass().getName().equals("se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem") && CrossbowItem.isCharged(mainhandItem)) {
            this.currentCompositeMotion = LivingMotions.AIM;
        }
    }
}