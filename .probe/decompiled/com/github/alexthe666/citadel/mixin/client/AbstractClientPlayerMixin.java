package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.client.rewards.CitadelCapes;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ AbstractClientPlayer.class })
public abstract class AbstractClientPlayerMixin extends Player {

    public AbstractClientPlayerMixin(Level level0, BlockPos blockPos1, float float2, GameProfile gameProfile3) {
        super(level0, blockPos1, float2, gameProfile3);
    }

    @Inject(at = { @At("HEAD") }, remap = true, method = { "Lnet/minecraft/client/player/AbstractClientPlayer;getCloakTextureLocation()Lnet/minecraft/resources/ResourceLocation;" }, cancellable = true)
    private void citadel_getCapeLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        CitadelCapes.Cape cape = CitadelCapes.getCurrentCape(this);
        if (cape != null) {
            cir.setReturnValue(cape.getTexture());
        }
    }

    @Inject(at = { @At("HEAD") }, remap = true, method = { "Lnet/minecraft/client/player/AbstractClientPlayer;getElytraTextureLocation()Lnet/minecraft/resources/ResourceLocation;" }, cancellable = true)
    private void citadel_getElytraLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        CitadelCapes.Cape cape = CitadelCapes.getCurrentCape(this);
        if (cape != null) {
            cir.setReturnValue(cape.getTexture());
        }
    }
}