package net.minecraft.network.protocol.game;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ClientboundExplodePacket implements Packet<ClientGamePacketListener> {

    private final double x;

    private final double y;

    private final double z;

    private final float power;

    private final List<BlockPos> toBlow;

    private final float knockbackX;

    private final float knockbackY;

    private final float knockbackZ;

    public ClientboundExplodePacket(double double0, double double1, double double2, float float3, List<BlockPos> listBlockPos4, @Nullable Vec3 vec5) {
        this.x = double0;
        this.y = double1;
        this.z = double2;
        this.power = float3;
        this.toBlow = Lists.newArrayList(listBlockPos4);
        if (vec5 != null) {
            this.knockbackX = (float) vec5.x;
            this.knockbackY = (float) vec5.y;
            this.knockbackZ = (float) vec5.z;
        } else {
            this.knockbackX = 0.0F;
            this.knockbackY = 0.0F;
            this.knockbackZ = 0.0F;
        }
    }

    public ClientboundExplodePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.x = friendlyByteBuf0.readDouble();
        this.y = friendlyByteBuf0.readDouble();
        this.z = friendlyByteBuf0.readDouble();
        this.power = friendlyByteBuf0.readFloat();
        int $$1 = Mth.floor(this.x);
        int $$2 = Mth.floor(this.y);
        int $$3 = Mth.floor(this.z);
        this.toBlow = friendlyByteBuf0.readList(p_178850_ -> {
            int $$4 = p_178850_.readByte() + $$1;
            int $$5 = p_178850_.readByte() + $$2;
            int $$6 = p_178850_.readByte() + $$3;
            return new BlockPos($$4, $$5, $$6);
        });
        this.knockbackX = friendlyByteBuf0.readFloat();
        this.knockbackY = friendlyByteBuf0.readFloat();
        this.knockbackZ = friendlyByteBuf0.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeDouble(this.x);
        friendlyByteBuf0.writeDouble(this.y);
        friendlyByteBuf0.writeDouble(this.z);
        friendlyByteBuf0.writeFloat(this.power);
        int $$1 = Mth.floor(this.x);
        int $$2 = Mth.floor(this.y);
        int $$3 = Mth.floor(this.z);
        friendlyByteBuf0.writeCollection(this.toBlow, (p_178855_, p_178856_) -> {
            int $$5 = p_178856_.m_123341_() - $$1;
            int $$6 = p_178856_.m_123342_() - $$2;
            int $$7 = p_178856_.m_123343_() - $$3;
            p_178855_.writeByte($$5);
            p_178855_.writeByte($$6);
            p_178855_.writeByte($$7);
        });
        friendlyByteBuf0.writeFloat(this.knockbackX);
        friendlyByteBuf0.writeFloat(this.knockbackY);
        friendlyByteBuf0.writeFloat(this.knockbackZ);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleExplosion(this);
    }

    public float getKnockbackX() {
        return this.knockbackX;
    }

    public float getKnockbackY() {
        return this.knockbackY;
    }

    public float getKnockbackZ() {
        return this.knockbackZ;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getPower() {
        return this.power;
    }

    public List<BlockPos> getToBlow() {
        return this.toBlow;
    }
}