package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundResourcePackPacket implements Packet<ClientGamePacketListener> {

    public static final int MAX_HASH_LENGTH = 40;

    private final String url;

    private final String hash;

    private final boolean required;

    @Nullable
    private final Component prompt;

    public ClientboundResourcePackPacket(String string0, String string1, boolean boolean2, @Nullable Component component3) {
        if (string1.length() > 40) {
            throw new IllegalArgumentException("Hash is too long (max 40, was " + string1.length() + ")");
        } else {
            this.url = string0;
            this.hash = string1;
            this.required = boolean2;
            this.prompt = component3;
        }
    }

    public ClientboundResourcePackPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.url = friendlyByteBuf0.readUtf();
        this.hash = friendlyByteBuf0.readUtf(40);
        this.required = friendlyByteBuf0.readBoolean();
        this.prompt = friendlyByteBuf0.readNullable(FriendlyByteBuf::m_130238_);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeUtf(this.url);
        friendlyByteBuf0.writeUtf(this.hash);
        friendlyByteBuf0.writeBoolean(this.required);
        friendlyByteBuf0.writeNullable(this.prompt, FriendlyByteBuf::m_130083_);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleResourcePack(this);
    }

    public String getUrl() {
        return this.url;
    }

    public String getHash() {
        return this.hash;
    }

    public boolean isRequired() {
        return this.required;
    }

    @Nullable
    public Component getPrompt() {
        return this.prompt;
    }
}