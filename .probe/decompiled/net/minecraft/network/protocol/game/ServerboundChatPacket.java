package net.minecraft.network.protocol.game;

import java.time.Instant;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.protocol.Packet;

public record ServerboundChatPacket(String f_133827_, Instant f_237950_, long f_240906_, @Nullable MessageSignature f_240898_, LastSeenMessages.Update f_241662_) implements Packet<ServerGamePacketListener> {

    private final String message;

    private final Instant timeStamp;

    private final long salt;

    @Nullable
    private final MessageSignature signature;

    private final LastSeenMessages.Update lastSeenMessages;

    public ServerboundChatPacket(FriendlyByteBuf p_179545_) {
        this(p_179545_.readUtf(256), p_179545_.readInstant(), p_179545_.readLong(), p_179545_.readNullable(MessageSignature::m_245099_), new LastSeenMessages.Update(p_179545_));
    }

    public ServerboundChatPacket(String f_133827_, Instant f_237950_, long f_240906_, @Nullable MessageSignature f_240898_, LastSeenMessages.Update f_241662_) {
        this.message = f_133827_;
        this.timeStamp = f_237950_;
        this.salt = f_240906_;
        this.signature = f_240898_;
        this.lastSeenMessages = f_241662_;
    }

    @Override
    public void write(FriendlyByteBuf p_133839_) {
        p_133839_.writeUtf(this.message, 256);
        p_133839_.writeInstant(this.timeStamp);
        p_133839_.writeLong(this.salt);
        p_133839_.writeNullable(this.signature, MessageSignature::m_246050_);
        this.lastSeenMessages.write(p_133839_);
    }

    public void handle(ServerGamePacketListener p_133836_) {
        p_133836_.handleChat(this);
    }
}