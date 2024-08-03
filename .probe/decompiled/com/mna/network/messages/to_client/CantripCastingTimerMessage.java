package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class CantripCastingTimerMessage extends BaseMessage {

    private String cantripID;

    private int ticks;

    public CantripCastingTimerMessage(String cantrip, int ticks) {
        this.cantripID = cantrip;
        this.ticks = ticks;
        this.messageIsValid = true;
    }

    public CantripCastingTimerMessage() {
        this.messageIsValid = false;
    }

    public String getCantripId() {
        return this.cantripID;
    }

    public int getTicks() {
        return this.ticks;
    }

    public static CantripCastingTimerMessage decode(FriendlyByteBuf buf) {
        CantripCastingTimerMessage msg = new CantripCastingTimerMessage();
        try {
            msg.cantripID = buf.readUtf();
            msg.ticks = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading ManaweavePatternDrawnMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(CantripCastingTimerMessage message, FriendlyByteBuf buf) {
        buf.writeUtf(message.getCantripId());
        buf.writeInt(message.getTicks());
    }
}