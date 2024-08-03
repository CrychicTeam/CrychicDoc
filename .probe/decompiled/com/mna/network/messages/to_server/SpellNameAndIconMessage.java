package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;

public class SpellNameAndIconMessage extends BaseMessage {

    private int iconIndex;

    private String name;

    private int itemIndex = -1;

    private InteractionHand hand;

    public SpellNameAndIconMessage(String name, int iconIndex, InteractionHand hand) {
        this.name = name;
        this.iconIndex = iconIndex;
        this.hand = hand;
        this.messageIsValid = true;
    }

    public SpellNameAndIconMessage() {
        this.messageIsValid = false;
    }

    public void setItemIndex(int index) {
        this.itemIndex = index;
    }

    public int getItemIndex() {
        return this.itemIndex;
    }

    public int getIconIndex() {
        return this.iconIndex;
    }

    public String getName() {
        return this.name;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public static SpellNameAndIconMessage decode(FriendlyByteBuf buf) {
        SpellNameAndIconMessage msg = new SpellNameAndIconMessage();
        try {
            msg.iconIndex = buf.readInt();
            msg.name = buf.readUtf(32767);
            msg.hand = InteractionHand.values()[buf.readByte()];
            msg.itemIndex = buf.readBoolean() ? buf.readInt() : -1;
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading ManaweavePatternDrawnMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(SpellNameAndIconMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.iconIndex);
        buf.writeUtf(msg.name);
        buf.writeByte(msg.hand.ordinal());
        if (msg.itemIndex > -1) {
            buf.writeBoolean(true);
            buf.writeInt(msg.itemIndex);
        } else {
            buf.writeBoolean(false);
        }
    }
}