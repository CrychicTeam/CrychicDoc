package net.minecraft.network.protocol.game;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FilterMask;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.SignedMessageBody;
import net.minecraft.network.protocol.Packet;

public record ClientboundPlayerChatPacket(UUID f_243918_, int f_244283_, @Nullable MessageSignature f_243836_, SignedMessageBody.Packed f_244090_, @Nullable Component f_243686_, FilterMask f_243744_, ChatType.BoundNetwork f_240897_) implements Packet<ClientGamePacketListener> {

    private final UUID sender;

    private final int index;

    @Nullable
    private final MessageSignature signature;

    private final SignedMessageBody.Packed body;

    @Nullable
    private final Component unsignedContent;

    private final FilterMask filterMask;

    private final ChatType.BoundNetwork chatType;

    public ClientboundPlayerChatPacket(FriendlyByteBuf p_237741_) {
        this(p_237741_.readUUID(), p_237741_.readVarInt(), p_237741_.readNullable(MessageSignature::m_245099_), new SignedMessageBody.Packed(p_237741_), p_237741_.readNullable(FriendlyByteBuf::m_130238_), FilterMask.read(p_237741_), new ChatType.BoundNetwork(p_237741_));
    }

    public ClientboundPlayerChatPacket(UUID f_243918_, int f_244283_, @Nullable MessageSignature f_243836_, SignedMessageBody.Packed f_244090_, @Nullable Component f_243686_, FilterMask f_243744_, ChatType.BoundNetwork f_240897_) {
        this.sender = f_243918_;
        this.index = f_244283_;
        this.signature = f_243836_;
        this.body = f_244090_;
        this.unsignedContent = f_243686_;
        this.filterMask = f_243744_;
        this.chatType = f_240897_;
    }

    @Override
    public void write(FriendlyByteBuf p_237755_) {
        p_237755_.writeUUID(this.sender);
        p_237755_.writeVarInt(this.index);
        p_237755_.writeNullable(this.signature, MessageSignature::m_246050_);
        this.body.write(p_237755_);
        p_237755_.writeNullable(this.unsignedContent, FriendlyByteBuf::m_130083_);
        FilterMask.write(p_237755_, this.filterMask);
        this.chatType.write(p_237755_);
    }

    public void handle(ClientGamePacketListener p_237759_) {
        p_237759_.handlePlayerChat(this);
    }

    @Override
    public boolean isSkippable() {
        return true;
    }
}