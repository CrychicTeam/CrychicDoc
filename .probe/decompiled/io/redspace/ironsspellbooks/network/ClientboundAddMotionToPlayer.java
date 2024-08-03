package io.redspace.ironsspellbooks.network;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundAddMotionToPlayer {

    private final double x;

    private final double y;

    private final double z;

    private final boolean preserveMomentum;

    public ClientboundAddMotionToPlayer(double x, double y, double z, boolean preserveMomentum) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.preserveMomentum = preserveMomentum;
    }

    public ClientboundAddMotionToPlayer(Vec3 motion, boolean preserveMomentum) {
        this.x = motion.x;
        this.y = motion.y;
        this.z = motion.z;
        this.preserveMomentum = preserveMomentum;
    }

    public ClientboundAddMotionToPlayer(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.preserveMomentum = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeBoolean(this.preserveMomentum);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> {
        });
        if (this.preserveMomentum) {
            Minecraft.getInstance().player.m_5997_(this.x, this.y, this.z);
        } else {
            Minecraft.getInstance().player.m_20334_(this.x, this.y, this.z);
        }
        return true;
    }
}