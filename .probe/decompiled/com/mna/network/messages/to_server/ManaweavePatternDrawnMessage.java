package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

public class ManaweavePatternDrawnMessage extends BaseMessage {

    private ResourceLocation patternID;

    private Vec3 position;

    private InteractionHand hand;

    private int ticksDrawn;

    public ManaweavePatternDrawnMessage(ResourceLocation patternID, Vec3 position, InteractionHand hand, int ticksDrawn) {
        this.patternID = patternID;
        this.position = position;
        this.hand = hand;
        this.ticksDrawn = ticksDrawn;
        this.messageIsValid = true;
    }

    public ManaweavePatternDrawnMessage() {
        this.messageIsValid = false;
    }

    public ResourceLocation getPatternID() {
        return this.patternID;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public int getTicksDrawn() {
        return this.ticksDrawn;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public static ManaweavePatternDrawnMessage decode(FriendlyByteBuf buf) {
        ManaweavePatternDrawnMessage msg = new ManaweavePatternDrawnMessage();
        try {
            msg.patternID = buf.readResourceLocation();
            msg.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            msg.hand = InteractionHand.values()[buf.readInt()];
            msg.ticksDrawn = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading ManaweavePatternDrawnMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(ManaweavePatternDrawnMessage msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.getPatternID());
        buf.writeDouble(msg.getPosition().x);
        buf.writeDouble(msg.getPosition().y);
        buf.writeDouble(msg.getPosition().z);
        buf.writeInt(msg.getHand().ordinal());
        buf.writeInt(msg.getTicksDrawn());
    }
}