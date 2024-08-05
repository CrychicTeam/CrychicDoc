package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class PossessionInputMessage extends BaseMessage {

    private PossessionInputMessage.MessageType type;

    private float yaw;

    private float yawHead;

    private float pitch;

    private float forward;

    private float strafe;

    private boolean leftclick;

    private boolean jump;

    private boolean sneak;

    private PossessionInputMessage(PossessionInputMessage.MessageType type, float forward, float strafe, float yaw, float yawHead, float pitch, boolean leftClick, boolean jump, boolean sneak) {
        this.type = type;
        this.forward = forward;
        this.strafe = strafe;
        this.yaw = yaw;
        this.yawHead = yawHead;
        this.pitch = pitch;
        this.leftclick = leftClick;
        this.jump = jump;
        this.sneak = sneak;
        this.messageIsValid = true;
    }

    public PossessionInputMessage() {
        this.messageIsValid = false;
    }

    public PossessionInputMessage.MessageType getType() {
        return this.type;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getYawHead() {
        return this.yawHead;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getForward() {
        return this.forward;
    }

    public float getStrafe() {
        return this.strafe;
    }

    public boolean getLeftClick() {
        return this.leftclick;
    }

    public boolean getJump() {
        return this.jump;
    }

    public boolean getSneak() {
        return this.sneak;
    }

    public static PossessionInputMessage decode(FriendlyByteBuf buf) {
        PossessionInputMessage msg = new PossessionInputMessage();
        try {
            msg.type = PossessionInputMessage.MessageType.values()[buf.readInt()];
            switch(msg.type) {
                case CLICK:
                    msg.leftclick = buf.readBoolean();
                    break;
                case MOVEMENT:
                    msg.forward = buf.readFloat();
                    msg.strafe = buf.readFloat();
                    msg.yaw = buf.readFloat();
                    msg.yawHead = buf.readFloat();
                    msg.pitch = buf.readFloat();
                    msg.jump = buf.readBoolean();
                    msg.sneak = buf.readBoolean();
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading PosessionInputMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(PossessionInputMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getType().ordinal());
        switch(msg.getType()) {
            case CLICK:
                buf.writeBoolean(msg.getLeftClick());
                break;
            case MOVEMENT:
                buf.writeFloat(msg.getForward());
                buf.writeFloat(msg.getStrafe());
                buf.writeFloat(msg.getYaw());
                buf.writeFloat(msg.getYawHead());
                buf.writeFloat(msg.getPitch());
                buf.writeBoolean(msg.getJump());
                buf.writeBoolean(msg.getSneak());
        }
    }

    public static PossessionInputMessage click() {
        return new PossessionInputMessage(PossessionInputMessage.MessageType.CLICK, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, true, false, false);
    }

    public static PossessionInputMessage movement(float forward, float strafe, boolean jump, boolean sneak, float yaw, float yawHead, float pitch) {
        return new PossessionInputMessage(PossessionInputMessage.MessageType.MOVEMENT, forward, strafe, yaw, yawHead, pitch, false, jump, sneak);
    }

    public static enum MessageType {

        CLICK, MOVEMENT
    }
}