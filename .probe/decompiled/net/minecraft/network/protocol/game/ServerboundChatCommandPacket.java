package net.minecraft.network.protocol.game;

import java.time.Instant;
import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.protocol.Packet;

public record ServerboundChatCommandPacket(String f_237922_, Instant f_237923_, long f_240858_, ArgumentSignatures f_237924_, LastSeenMessages.Update f_241638_) implements Packet<ServerGamePacketListener> {

    private final String command;

    private final Instant timeStamp;

    private final long salt;

    private final ArgumentSignatures argumentSignatures;

    private final LastSeenMessages.Update lastSeenMessages;

    public ServerboundChatCommandPacket(FriendlyByteBuf p_237932_) {
        this(p_237932_.readUtf(256), p_237932_.readInstant(), p_237932_.readLong(), new ArgumentSignatures(p_237932_), new LastSeenMessages.Update(p_237932_));
    }

    public ServerboundChatCommandPacket(String f_237922_, Instant f_237923_, long f_240858_, ArgumentSignatures f_237924_, LastSeenMessages.Update f_241638_) {
        this.command = f_237922_;
        this.timeStamp = f_237923_;
        this.salt = f_240858_;
        this.argumentSignatures = f_237924_;
        this.lastSeenMessages = f_241638_;
    }

    @Override
    public void write(FriendlyByteBuf p_237936_) {
        p_237936_.writeUtf(this.command, 256);
        p_237936_.writeInstant(this.timeStamp);
        p_237936_.writeLong(this.salt);
        this.argumentSignatures.write(p_237936_);
        this.lastSeenMessages.write(p_237936_);
    }

    public void handle(ServerGamePacketListener p_237940_) {
        p_237940_.handleChatCommand(this);
    }
}