package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class SetLiftSpeedMessage extends BaseMessage {

    private int entityID;

    private float liftSpeed;

    public SetLiftSpeedMessage(int entityID, float liftSpeed) {
        this.entityID = entityID;
        this.liftSpeed = liftSpeed;
        this.messageIsValid = true;
    }

    public SetLiftSpeedMessage() {
        this.messageIsValid = false;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public float getLiftSpeed() {
        return this.liftSpeed;
    }

    public static SetLiftSpeedMessage decode(FriendlyByteBuf buf) {
        SetLiftSpeedMessage msg = new SetLiftSpeedMessage();
        try {
            msg.entityID = buf.readInt();
            msg.liftSpeed = buf.readFloat();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading CloudstepJumpMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(SetLiftSpeedMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getEntityID());
        buf.writeFloat(msg.getLiftSpeed());
    }
}