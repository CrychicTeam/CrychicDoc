package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public abstract class ServerboundMovePlayerPacket implements Packet<ServerGamePacketListener> {

    protected final double x;

    protected final double y;

    protected final double z;

    protected final float yRot;

    protected final float xRot;

    protected final boolean onGround;

    protected final boolean hasPos;

    protected final boolean hasRot;

    protected ServerboundMovePlayerPacket(double double0, double double1, double double2, float float3, float float4, boolean boolean5, boolean boolean6, boolean boolean7) {
        this.x = double0;
        this.y = double1;
        this.z = double2;
        this.yRot = float3;
        this.xRot = float4;
        this.onGround = boolean5;
        this.hasPos = boolean6;
        this.hasRot = boolean7;
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleMovePlayer(this);
    }

    public double getX(double double0) {
        return this.hasPos ? this.x : double0;
    }

    public double getY(double double0) {
        return this.hasPos ? this.y : double0;
    }

    public double getZ(double double0) {
        return this.hasPos ? this.z : double0;
    }

    public float getYRot(float float0) {
        return this.hasRot ? this.yRot : float0;
    }

    public float getXRot(float float0) {
        return this.hasRot ? this.xRot : float0;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public boolean hasPosition() {
        return this.hasPos;
    }

    public boolean hasRotation() {
        return this.hasRot;
    }

    public static class Pos extends ServerboundMovePlayerPacket {

        public Pos(double double0, double double1, double double2, boolean boolean3) {
            super(double0, double1, double2, 0.0F, 0.0F, boolean3, true, false);
        }

        public static ServerboundMovePlayerPacket.Pos read(FriendlyByteBuf friendlyByteBuf0) {
            double $$1 = friendlyByteBuf0.readDouble();
            double $$2 = friendlyByteBuf0.readDouble();
            double $$3 = friendlyByteBuf0.readDouble();
            boolean $$4 = friendlyByteBuf0.readUnsignedByte() != 0;
            return new ServerboundMovePlayerPacket.Pos($$1, $$2, $$3, $$4);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeDouble(this.f_134118_);
            friendlyByteBuf0.writeDouble(this.f_134119_);
            friendlyByteBuf0.writeDouble(this.f_134120_);
            friendlyByteBuf0.writeByte(this.f_134123_ ? 1 : 0);
        }
    }

    public static class PosRot extends ServerboundMovePlayerPacket {

        public PosRot(double double0, double double1, double double2, float float3, float float4, boolean boolean5) {
            super(double0, double1, double2, float3, float4, boolean5, true, true);
        }

        public static ServerboundMovePlayerPacket.PosRot read(FriendlyByteBuf friendlyByteBuf0) {
            double $$1 = friendlyByteBuf0.readDouble();
            double $$2 = friendlyByteBuf0.readDouble();
            double $$3 = friendlyByteBuf0.readDouble();
            float $$4 = friendlyByteBuf0.readFloat();
            float $$5 = friendlyByteBuf0.readFloat();
            boolean $$6 = friendlyByteBuf0.readUnsignedByte() != 0;
            return new ServerboundMovePlayerPacket.PosRot($$1, $$2, $$3, $$4, $$5, $$6);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeDouble(this.f_134118_);
            friendlyByteBuf0.writeDouble(this.f_134119_);
            friendlyByteBuf0.writeDouble(this.f_134120_);
            friendlyByteBuf0.writeFloat(this.f_134121_);
            friendlyByteBuf0.writeFloat(this.f_134122_);
            friendlyByteBuf0.writeByte(this.f_134123_ ? 1 : 0);
        }
    }

    public static class Rot extends ServerboundMovePlayerPacket {

        public Rot(float float0, float float1, boolean boolean2) {
            super(0.0, 0.0, 0.0, float0, float1, boolean2, false, true);
        }

        public static ServerboundMovePlayerPacket.Rot read(FriendlyByteBuf friendlyByteBuf0) {
            float $$1 = friendlyByteBuf0.readFloat();
            float $$2 = friendlyByteBuf0.readFloat();
            boolean $$3 = friendlyByteBuf0.readUnsignedByte() != 0;
            return new ServerboundMovePlayerPacket.Rot($$1, $$2, $$3);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeFloat(this.f_134121_);
            friendlyByteBuf0.writeFloat(this.f_134122_);
            friendlyByteBuf0.writeByte(this.f_134123_ ? 1 : 0);
        }
    }

    public static class StatusOnly extends ServerboundMovePlayerPacket {

        public StatusOnly(boolean boolean0) {
            super(0.0, 0.0, 0.0, 0.0F, 0.0F, boolean0, false, false);
        }

        public static ServerboundMovePlayerPacket.StatusOnly read(FriendlyByteBuf friendlyByteBuf0) {
            boolean $$1 = friendlyByteBuf0.readUnsignedByte() != 0;
            return new ServerboundMovePlayerPacket.StatusOnly($$1);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeByte(this.f_134123_ ? 1 : 0);
        }
    }
}