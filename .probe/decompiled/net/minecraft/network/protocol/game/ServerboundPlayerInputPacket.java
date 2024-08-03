package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPlayerInputPacket implements Packet<ServerGamePacketListener> {

    private static final int FLAG_JUMPING = 1;

    private static final int FLAG_SHIFT_KEY_DOWN = 2;

    private final float xxa;

    private final float zza;

    private final boolean isJumping;

    private final boolean isShiftKeyDown;

    public ServerboundPlayerInputPacket(float float0, float float1, boolean boolean2, boolean boolean3) {
        this.xxa = float0;
        this.zza = float1;
        this.isJumping = boolean2;
        this.isShiftKeyDown = boolean3;
    }

    public ServerboundPlayerInputPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.xxa = friendlyByteBuf0.readFloat();
        this.zza = friendlyByteBuf0.readFloat();
        byte $$1 = friendlyByteBuf0.readByte();
        this.isJumping = ($$1 & 1) > 0;
        this.isShiftKeyDown = ($$1 & 2) > 0;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeFloat(this.xxa);
        friendlyByteBuf0.writeFloat(this.zza);
        byte $$1 = 0;
        if (this.isJumping) {
            $$1 = (byte) ($$1 | 1);
        }
        if (this.isShiftKeyDown) {
            $$1 = (byte) ($$1 | 2);
        }
        friendlyByteBuf0.writeByte($$1);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handlePlayerInput(this);
    }

    public float getXxa() {
        return this.xxa;
    }

    public float getZza() {
        return this.zza;
    }

    public boolean isJumping() {
        return this.isJumping;
    }

    public boolean isShiftKeyDown() {
        return this.isShiftKeyDown;
    }
}