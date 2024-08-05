package net.mehvahdjukaar.supplementaries.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mojang.authlib.GameProfile;
import net.mehvahdjukaar.supplementaries.api.IQuiverEntity;
import net.mehvahdjukaar.supplementaries.client.cannon.CannonController;
import net.mehvahdjukaar.supplementaries.common.items.QuiverItem;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LocalPlayer.class })
public abstract class LocalPlayerMixin extends AbstractClientPlayer implements IQuiverEntity {

    @Unique
    private ItemStack supplementaries$quiver = ItemStack.EMPTY;

    protected LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = { "hasEnoughImpulseToStartSprinting" }, at = { @At("RETURN") }, cancellable = true)
    private void hasEnoughImpulseToStartSprinting(CallbackInfoReturnable<Boolean> cir) {
        if ((Boolean) cir.getReturnValue() && this.m_21023_((MobEffect) ModRegistry.OVERENCUMBERED.get())) {
            cir.setReturnValue(false);
        }
    }

    @ModifyExpressionValue(method = { "aiStep" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z") })
    private boolean supplementaries$preventQuiverSlow(boolean original) {
        return this.m_21211_().getItem() == ModRegistry.QUIVER_ITEM.get() && CommonConfigs.Tools.QUIVER_PREVENTS_SLOWS.get() ? false : original;
    }

    @Inject(method = { "tick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V", shift = Shift.AFTER) })
    private void checkIfHasQuiver(CallbackInfo ci) {
        this.supplementaries$quiver = QuiverItem.getQuiver(this);
    }

    @Override
    public ItemStack supplementaries$getQuiver() {
        return this.supplementaries$quiver;
    }

    @Override
    public void supplementaries$setQuiver(ItemStack quiver) {
        this.supplementaries$quiver = quiver;
    }

    @WrapWithCondition(method = { "aiStep" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;tick(ZF)V") })
    public boolean supplementaries$preventMovementWhileOperatingCannon(Input instance, boolean bl, float f) {
        if (CannonController.isActive()) {
            CannonController.onInputUpdate(instance);
            return false;
        } else {
            return true;
        }
    }
}