package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.LivingEntity;

public record ClientboundHurtAnimationPacket(int f_263825_, float f_263826_) implements Packet<ClientGamePacketListener> {

    private final int id;

    private final float yaw;

    public ClientboundHurtAnimationPacket(LivingEntity p_265293_) {
        this(p_265293_.m_19879_(), p_265293_.getHurtDir());
    }

    public ClientboundHurtAnimationPacket(FriendlyByteBuf p_265181_) {
        this(p_265181_.readVarInt(), p_265181_.readFloat());
    }

    public ClientboundHurtAnimationPacket(int f_263825_, float f_263826_) {
        this.id = f_263825_;
        this.yaw = f_263826_;
    }

    @Override
    public void write(FriendlyByteBuf p_265156_) {
        p_265156_.writeVarInt(this.id);
        p_265156_.writeFloat(this.yaw);
    }

    public void handle(ClientGamePacketListener p_265654_) {
        p_265654_.handleHurtAnimation(this);
    }
}