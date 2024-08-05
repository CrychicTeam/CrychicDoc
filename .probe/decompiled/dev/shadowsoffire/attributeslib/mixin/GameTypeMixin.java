package dev.shadowsoffire.attributeslib.mixin;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.shadowsoffire.attributeslib.impl.AttributeEvents;
import dev.shadowsoffire.attributeslib.util.IEntityOwned;
import dev.shadowsoffire.attributeslib.util.IFlying;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GameType.class })
public class GameTypeMixin {

    private boolean apoth_flying;

    @Inject(at = { @At("HEAD") }, method = { "updatePlayerAbilities(Lnet/minecraft/world/entity/player/Abilities;)V" })
    public void apoth_recordOldFlyingAttribs(Abilities abilities, CallbackInfo ci) {
        this.apoth_flying = abilities.flying;
    }

    @Inject(at = { @At("TAIL") }, method = { "updatePlayerAbilities(Lnet/minecraft/world/entity/player/Abilities;)V" })
    public void apoth_flightAttribModifier(Abilities abilities, CallbackInfo ci) {
        Player player = (Player) ((IEntityOwned) abilities).getOwner();
        AttributeEvents.applyCreativeFlightModifier(player, (GameType) this);
        if (player.m_21133_(ALObjects.Attributes.CREATIVE_FLIGHT.get()) > 0.0) {
            abilities.mayfly = true;
            abilities.flying = ((IFlying) player).getAndDestroyFlyingCache() || this.apoth_flying;
        }
    }
}