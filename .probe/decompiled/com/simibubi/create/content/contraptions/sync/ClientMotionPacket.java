package com.simibubi.create.content.contraptions.sync;

import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class ClientMotionPacket extends SimplePacketBase {

    private Vec3 motion;

    private boolean onGround;

    private float limbSwing;

    public ClientMotionPacket(Vec3 motion, boolean onGround, float limbSwing) {
        this.motion = motion;
        this.onGround = onGround;
        this.limbSwing = limbSwing;
    }

    public ClientMotionPacket(FriendlyByteBuf buffer) {
        this.motion = new Vec3((double) buffer.readFloat(), (double) buffer.readFloat(), (double) buffer.readFloat());
        this.onGround = buffer.readBoolean();
        this.limbSwing = buffer.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeFloat((float) this.motion.x);
        buffer.writeFloat((float) this.motion.y);
        buffer.writeFloat((float) this.motion.z);
        buffer.writeBoolean(this.onGround);
        buffer.writeFloat(this.limbSwing);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender != null) {
                sender.m_20256_(this.motion);
                sender.m_6853_(this.onGround);
                if (this.onGround) {
                    sender.m_142535_(sender.f_19789_, 1.0F, sender.m_269291_().fall());
                    sender.f_19789_ = 0.0F;
                    sender.connection.aboveGroundTickCount = 0;
                    sender.connection.aboveGroundVehicleTickCount = 0;
                }
                AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> sender), new LimbSwingUpdatePacket(sender.m_19879_(), sender.m_20182_(), this.limbSwing));
            }
        });
        return true;
    }
}