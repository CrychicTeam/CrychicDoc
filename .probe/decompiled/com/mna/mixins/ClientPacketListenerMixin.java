package com.mna.mixins;

import com.mna.effects.EffectInit;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ClientPacketListener.class })
public class ClientPacketListenerMixin {

    @Shadow
    private ClientLevel level;

    @Inject(at = { @At("RETURN") }, method = { "handleRemoveMobEffect" })
    public void mna$handleRemoveMobEffect(ClientboundRemoveMobEffectPacket packet, CallbackInfo cir) {
        Entity entity = packet.getEntity(this.level);
        if (entity instanceof LivingEntity && packet.getEffect() == EffectInit.REDUCE.get()) {
            entity.refreshDimensions();
        }
    }

    @Inject(at = { @At("RETURN") }, method = { "handleUpdateMobEffect" })
    public void mna$handleUpdateMobEffect(ClientboundUpdateMobEffectPacket packet, CallbackInfo cir) {
        Entity entity = this.level.getEntity(packet.getEntityId());
        if (entity != null && entity instanceof LivingEntity) {
            MobEffect mobeffect = packet.getEffect();
            if (mobeffect != null && entity instanceof LivingEntity && mobeffect == EffectInit.REDUCE.get()) {
                entity.refreshDimensions();
            }
        }
    }
}