package net.minecraft.network.protocol.game;

import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.effect.MobEffect;

public class ServerboundSetBeaconPacket implements Packet<ServerGamePacketListener> {

    private final Optional<MobEffect> primary;

    private final Optional<MobEffect> secondary;

    public ServerboundSetBeaconPacket(Optional<MobEffect> optionalMobEffect0, Optional<MobEffect> optionalMobEffect1) {
        this.primary = optionalMobEffect0;
        this.secondary = optionalMobEffect1;
    }

    public ServerboundSetBeaconPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.primary = friendlyByteBuf0.readOptional(p_258214_ -> p_258214_.readById(BuiltInRegistries.MOB_EFFECT));
        this.secondary = friendlyByteBuf0.readOptional(p_258215_ -> p_258215_.readById(BuiltInRegistries.MOB_EFFECT));
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeOptional(this.primary, (p_258216_, p_258217_) -> p_258216_.writeId(BuiltInRegistries.MOB_EFFECT, p_258217_));
        friendlyByteBuf0.writeOptional(this.secondary, (p_258218_, p_258219_) -> p_258218_.writeId(BuiltInRegistries.MOB_EFFECT, p_258219_));
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleSetBeaconPacket(this);
    }

    public Optional<MobEffect> getPrimary() {
        return this.primary;
    }

    public Optional<MobEffect> getSecondary() {
        return this.secondary;
    }
}