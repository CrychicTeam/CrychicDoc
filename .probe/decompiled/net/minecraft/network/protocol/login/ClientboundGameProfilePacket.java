package net.minecraft.network.protocol.login;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundGameProfilePacket implements Packet<ClientLoginPacketListener> {

    private final GameProfile gameProfile;

    public ClientboundGameProfilePacket(GameProfile gameProfile0) {
        this.gameProfile = gameProfile0;
    }

    public ClientboundGameProfilePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.gameProfile = friendlyByteBuf0.readGameProfile();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeGameProfile(this.gameProfile);
    }

    public void handle(ClientLoginPacketListener clientLoginPacketListener0) {
        clientLoginPacketListener0.handleGameProfile(this);
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
}