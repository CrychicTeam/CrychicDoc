package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Abilities;

public class ClientboundPlayerAbilitiesPacket implements Packet<ClientGamePacketListener> {

    private static final int FLAG_INVULNERABLE = 1;

    private static final int FLAG_FLYING = 2;

    private static final int FLAG_CAN_FLY = 4;

    private static final int FLAG_INSTABUILD = 8;

    private final boolean invulnerable;

    private final boolean isFlying;

    private final boolean canFly;

    private final boolean instabuild;

    private final float flyingSpeed;

    private final float walkingSpeed;

    public ClientboundPlayerAbilitiesPacket(Abilities abilities0) {
        this.invulnerable = abilities0.invulnerable;
        this.isFlying = abilities0.flying;
        this.canFly = abilities0.mayfly;
        this.instabuild = abilities0.instabuild;
        this.flyingSpeed = abilities0.getFlyingSpeed();
        this.walkingSpeed = abilities0.getWalkingSpeed();
    }

    public ClientboundPlayerAbilitiesPacket(FriendlyByteBuf friendlyByteBuf0) {
        byte $$1 = friendlyByteBuf0.readByte();
        this.invulnerable = ($$1 & 1) != 0;
        this.isFlying = ($$1 & 2) != 0;
        this.canFly = ($$1 & 4) != 0;
        this.instabuild = ($$1 & 8) != 0;
        this.flyingSpeed = friendlyByteBuf0.readFloat();
        this.walkingSpeed = friendlyByteBuf0.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        byte $$1 = 0;
        if (this.invulnerable) {
            $$1 = (byte) ($$1 | 1);
        }
        if (this.isFlying) {
            $$1 = (byte) ($$1 | 2);
        }
        if (this.canFly) {
            $$1 = (byte) ($$1 | 4);
        }
        if (this.instabuild) {
            $$1 = (byte) ($$1 | 8);
        }
        friendlyByteBuf0.writeByte($$1);
        friendlyByteBuf0.writeFloat(this.flyingSpeed);
        friendlyByteBuf0.writeFloat(this.walkingSpeed);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handlePlayerAbilities(this);
    }

    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    public boolean isFlying() {
        return this.isFlying;
    }

    public boolean canFly() {
        return this.canFly;
    }

    public boolean canInstabuild() {
        return this.instabuild;
    }

    public float getFlyingSpeed() {
        return this.flyingSpeed;
    }

    public float getWalkingSpeed() {
        return this.walkingSpeed;
    }
}