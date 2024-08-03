package dev.shadowsoffire.attributeslib.mixin.client;

import dev.shadowsoffire.attributeslib.api.AttributeChangedValueEvent;
import java.util.Iterator;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ ClientPacketListener.class })
public class ClientPacketListenerMixin {

    private double apoth_lastValue;

    @Inject(at = { @At(value = "INVOKE", target = "net/minecraft/world/entity/ai/attributes/AttributeInstance.setBaseValue(D)V") }, method = { "handleUpdateAttributes(Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket;)V" }, require = 1, locals = LocalCapture.CAPTURE_FAILHARD)
    public void apoth_recordOldAttrValue(ClientboundUpdateAttributesPacket packet, CallbackInfo ci, Entity entity, AttributeMap map, Iterator<ClientboundUpdateAttributesPacket.AttributeSnapshot> it, ClientboundUpdateAttributesPacket.AttributeSnapshot snapshot, AttributeInstance inst) {
        this.apoth_lastValue = inst.getValue();
    }

    @Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;addTransientModifier(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V", shift = Shift.BY, by = 5) }, method = { "handleUpdateAttributes(Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket;)V" }, require = 1, locals = LocalCapture.CAPTURE_FAILHARD)
    public void apoth_postAttrChangedEvent(ClientboundUpdateAttributesPacket packet, CallbackInfo ci, Entity entity, AttributeMap map, Iterator<ClientboundUpdateAttributesPacket.AttributeSnapshot> it, ClientboundUpdateAttributesPacket.AttributeSnapshot snapshot, @Nullable AttributeInstance inst) {
        if (inst != null) {
            double newValue = inst.getValue();
            if (newValue != this.apoth_lastValue) {
                MinecraftForge.EVENT_BUS.post(new AttributeChangedValueEvent((LivingEntity) entity, inst, this.apoth_lastValue, newValue));
            }
        }
    }
}