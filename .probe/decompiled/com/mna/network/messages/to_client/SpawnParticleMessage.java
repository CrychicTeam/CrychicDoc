package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import com.mna.tools.math.Vector3;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class SpawnParticleMessage extends BaseMessage {

    Vector3 position;

    Vector3 speed;

    int color;

    ResourceLocation type;

    public SpawnParticleMessage(double x, double y, double z, double vX, double vY, double vZ, int color, ResourceLocation type) {
        this.position = new Vector3(x, y, z);
        this.speed = new Vector3(vX, vY, vZ);
        this.type = type;
        this.color = color;
        this.messageIsValid = true;
    }

    public SpawnParticleMessage() {
        this.messageIsValid = false;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public Vector3 getSpeed() {
        return this.speed;
    }

    public int getColor() {
        return this.color;
    }

    public ResourceLocation getType() {
        return this.type;
    }

    public static SpawnParticleMessage decode(FriendlyByteBuf buf) {
        SpawnParticleMessage msg = new SpawnParticleMessage();
        try {
            msg.type = buf.readResourceLocation();
            msg.position = new Vector3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            msg.speed = new Vector3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            msg.color = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading SpawnParticleMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(SpawnParticleMessage message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.getType());
        buf.writeDouble((double) message.getPosition().x);
        buf.writeDouble((double) message.getPosition().y);
        buf.writeDouble((double) message.getPosition().z);
        buf.writeDouble((double) message.getSpeed().x);
        buf.writeDouble((double) message.getSpeed().y);
        buf.writeDouble((double) message.getSpeed().z);
        buf.writeInt(message.getColor());
    }
}