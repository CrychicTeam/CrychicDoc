package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class SetLiftPositionMessage extends BaseMessage {

    Vec3 position;

    public SetLiftPositionMessage(double x, double y, double z) {
        this.position = new Vec3(x, y, z);
        this.messageIsValid = true;
    }

    public SetLiftPositionMessage() {
        this.messageIsValid = false;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public static SetLiftPositionMessage decode(FriendlyByteBuf buf) {
        SetLiftPositionMessage msg = new SetLiftPositionMessage();
        try {
            msg.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading ManaweavePatternDrawnMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(SetLiftPositionMessage message, FriendlyByteBuf buf) {
        buf.writeDouble(message.getPosition().x);
        buf.writeDouble(message.getPosition().y);
        buf.writeDouble(message.getPosition().z);
    }
}