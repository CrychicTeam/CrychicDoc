package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ClientboundRemoveMobEffectPacket implements Packet<ClientGamePacketListener> {

    private final int entityId;

    private final MobEffect effect;

    public ClientboundRemoveMobEffectPacket(int int0, MobEffect mobEffect1) {
        this.entityId = int0;
        this.effect = mobEffect1;
    }

    public ClientboundRemoveMobEffectPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.entityId = friendlyByteBuf0.readVarInt();
        this.effect = friendlyByteBuf0.readById(BuiltInRegistries.MOB_EFFECT);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.entityId);
        friendlyByteBuf0.writeId(BuiltInRegistries.MOB_EFFECT, this.effect);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleRemoveMobEffect(this);
    }

    @Nullable
    public Entity getEntity(Level level0) {
        return level0.getEntity(this.entityId);
    }

    @Nullable
    public MobEffect getEffect() {
        return this.effect;
    }
}