package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetTitlesAnimationPacket implements Packet<ClientGamePacketListener> {

    private final int fadeIn;

    private final int stay;

    private final int fadeOut;

    public ClientboundSetTitlesAnimationPacket(int int0, int int1, int int2) {
        this.fadeIn = int0;
        this.stay = int1;
        this.fadeOut = int2;
    }

    public ClientboundSetTitlesAnimationPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.fadeIn = friendlyByteBuf0.readInt();
        this.stay = friendlyByteBuf0.readInt();
        this.fadeOut = friendlyByteBuf0.readInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeInt(this.fadeIn);
        friendlyByteBuf0.writeInt(this.stay);
        friendlyByteBuf0.writeInt(this.fadeOut);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.setTitlesAnimation(this);
    }

    public int getFadeIn() {
        return this.fadeIn;
    }

    public int getStay() {
        return this.stay;
    }

    public int getFadeOut() {
        return this.fadeOut;
    }
}