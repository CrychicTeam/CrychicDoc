package net.minecraft.network.protocol.login;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ServerboundHelloPacket(String f_238040_, Optional<UUID> f_240375_) implements Packet<ServerLoginPacketListener> {

    private final String name;

    private final Optional<UUID> profileId;

    public ServerboundHelloPacket(FriendlyByteBuf p_179827_) {
        this(p_179827_.readUtf(16), p_179827_.readOptional(FriendlyByteBuf::m_130259_));
    }

    public ServerboundHelloPacket(String f_238040_, Optional<UUID> f_240375_) {
        this.name = f_238040_;
        this.profileId = f_240375_;
    }

    @Override
    public void write(FriendlyByteBuf p_134851_) {
        p_134851_.writeUtf(this.name, 16);
        p_134851_.writeOptional(this.profileId, FriendlyByteBuf::m_130077_);
    }

    public void handle(ServerLoginPacketListener p_134848_) {
        p_134848_.handleHello(this);
    }
}