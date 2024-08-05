package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public abstract class ClientboundMoveEntityPacket implements Packet<ClientGamePacketListener> {

    protected final int entityId;

    protected final short xa;

    protected final short ya;

    protected final short za;

    protected final byte yRot;

    protected final byte xRot;

    protected final boolean onGround;

    protected final boolean hasRot;

    protected final boolean hasPos;

    protected ClientboundMoveEntityPacket(int int0, short short1, short short2, short short3, byte byte4, byte byte5, boolean boolean6, boolean boolean7, boolean boolean8) {
        this.entityId = int0;
        this.xa = short1;
        this.ya = short2;
        this.za = short3;
        this.yRot = byte4;
        this.xRot = byte5;
        this.onGround = boolean6;
        this.hasRot = boolean7;
        this.hasPos = boolean8;
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleMoveEntity(this);
    }

    public String toString() {
        return "Entity_" + super.toString();
    }

    @Nullable
    public Entity getEntity(Level level0) {
        return level0.getEntity(this.entityId);
    }

    public short getXa() {
        return this.xa;
    }

    public short getYa() {
        return this.ya;
    }

    public short getZa() {
        return this.za;
    }

    public byte getyRot() {
        return this.yRot;
    }

    public byte getxRot() {
        return this.xRot;
    }

    public boolean hasRotation() {
        return this.hasRot;
    }

    public boolean hasPosition() {
        return this.hasPos;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public static class Pos extends ClientboundMoveEntityPacket {

        public Pos(int int0, short short1, short short2, short short3, boolean boolean4) {
            super(int0, short1, short2, short3, (byte) 0, (byte) 0, boolean4, false, true);
        }

        public static ClientboundMoveEntityPacket.Pos read(FriendlyByteBuf friendlyByteBuf0) {
            int $$1 = friendlyByteBuf0.readVarInt();
            short $$2 = friendlyByteBuf0.readShort();
            short $$3 = friendlyByteBuf0.readShort();
            short $$4 = friendlyByteBuf0.readShort();
            boolean $$5 = friendlyByteBuf0.readBoolean();
            return new ClientboundMoveEntityPacket.Pos($$1, $$2, $$3, $$4, $$5);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeVarInt(this.f_132499_);
            friendlyByteBuf0.writeShort(this.f_132500_);
            friendlyByteBuf0.writeShort(this.f_132501_);
            friendlyByteBuf0.writeShort(this.f_132502_);
            friendlyByteBuf0.writeBoolean(this.f_132505_);
        }
    }

    public static class PosRot extends ClientboundMoveEntityPacket {

        public PosRot(int int0, short short1, short short2, short short3, byte byte4, byte byte5, boolean boolean6) {
            super(int0, short1, short2, short3, byte4, byte5, boolean6, true, true);
        }

        public static ClientboundMoveEntityPacket.PosRot read(FriendlyByteBuf friendlyByteBuf0) {
            int $$1 = friendlyByteBuf0.readVarInt();
            short $$2 = friendlyByteBuf0.readShort();
            short $$3 = friendlyByteBuf0.readShort();
            short $$4 = friendlyByteBuf0.readShort();
            byte $$5 = friendlyByteBuf0.readByte();
            byte $$6 = friendlyByteBuf0.readByte();
            boolean $$7 = friendlyByteBuf0.readBoolean();
            return new ClientboundMoveEntityPacket.PosRot($$1, $$2, $$3, $$4, $$5, $$6, $$7);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeVarInt(this.f_132499_);
            friendlyByteBuf0.writeShort(this.f_132500_);
            friendlyByteBuf0.writeShort(this.f_132501_);
            friendlyByteBuf0.writeShort(this.f_132502_);
            friendlyByteBuf0.writeByte(this.f_132503_);
            friendlyByteBuf0.writeByte(this.f_132504_);
            friendlyByteBuf0.writeBoolean(this.f_132505_);
        }
    }

    public static class Rot extends ClientboundMoveEntityPacket {

        public Rot(int int0, byte byte1, byte byte2, boolean boolean3) {
            super(int0, (short) 0, (short) 0, (short) 0, byte1, byte2, boolean3, true, false);
        }

        public static ClientboundMoveEntityPacket.Rot read(FriendlyByteBuf friendlyByteBuf0) {
            int $$1 = friendlyByteBuf0.readVarInt();
            byte $$2 = friendlyByteBuf0.readByte();
            byte $$3 = friendlyByteBuf0.readByte();
            boolean $$4 = friendlyByteBuf0.readBoolean();
            return new ClientboundMoveEntityPacket.Rot($$1, $$2, $$3, $$4);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeVarInt(this.f_132499_);
            friendlyByteBuf0.writeByte(this.f_132503_);
            friendlyByteBuf0.writeByte(this.f_132504_);
            friendlyByteBuf0.writeBoolean(this.f_132505_);
        }
    }
}