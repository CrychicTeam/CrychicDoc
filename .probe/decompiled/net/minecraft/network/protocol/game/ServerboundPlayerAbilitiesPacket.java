package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Abilities;

public class ServerboundPlayerAbilitiesPacket implements Packet<ServerGamePacketListener> {

    private static final int FLAG_FLYING = 2;

    private final boolean isFlying;

    public ServerboundPlayerAbilitiesPacket(Abilities abilities0) {
        this.isFlying = abilities0.flying;
    }

    public ServerboundPlayerAbilitiesPacket(FriendlyByteBuf friendlyByteBuf0) {
        byte $$1 = friendlyByteBuf0.readByte();
        this.isFlying = ($$1 & 2) != 0;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        byte $$1 = 0;
        if (this.isFlying) {
            $$1 = (byte) ($$1 | 2);
        }
        friendlyByteBuf0.writeByte($$1);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handlePlayerAbilities(this);
    }

    public boolean isFlying() {
        return this.isFlying;
    }
}