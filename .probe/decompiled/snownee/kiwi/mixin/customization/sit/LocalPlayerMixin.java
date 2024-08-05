package snownee.kiwi.mixin.customization.sit;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.kiwi.customization.block.behavior.SitManager;

@Mixin({ LocalPlayer.class })
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    public LocalPlayerMixin(ClientLevel pClientLevel, GameProfile pGameProfile) {
        super(pClientLevel, pGameProfile);
    }

    @Inject(method = { "rideTick" }, at = { @At("HEAD") })
    private void kiwi$rideTick(CallbackInfo ci) {
        Entity vehicle = this.m_20202_();
        if (SitManager.isSeatEntity(vehicle)) {
            SitManager.clampRotation(this, vehicle);
        }
    }
}