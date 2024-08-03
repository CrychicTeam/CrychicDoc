package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class PlayerBounceMessage extends BaseMessage {

    private Vec3 velocity;

    public PlayerBounceMessage() {
        this.messageIsValid = false;
    }

    public PlayerBounceMessage(Vec3 velocity) {
        this.velocity = velocity;
        this.messageIsValid = true;
    }

    public Vec3 getVelocity() {
        return this.velocity;
    }

    public static final PlayerBounceMessage decode(FriendlyByteBuf buf) {
        PlayerBounceMessage msg = new PlayerBounceMessage();
        try {
            msg.velocity = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading PlayerBouncePacket: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(PlayerBounceMessage message, FriendlyByteBuf buf) {
        buf.writeDouble(message.velocity.x);
        buf.writeDouble(message.velocity.y);
        buf.writeDouble(message.velocity.z);
    }
}