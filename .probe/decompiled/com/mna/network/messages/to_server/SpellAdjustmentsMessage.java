package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;

public class SpellAdjustmentsMessage extends BaseMessage {

    private CompoundTag spellValues;

    private InteractionHand hand;

    public SpellAdjustmentsMessage(CompoundTag spellValues, InteractionHand hand) {
        this.spellValues = spellValues;
        this.hand = hand;
        this.messageIsValid = true;
    }

    public SpellAdjustmentsMessage() {
        this.messageIsValid = false;
    }

    public CompoundTag getData() {
        return this.spellValues;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public static SpellAdjustmentsMessage decode(FriendlyByteBuf buf) {
        SpellAdjustmentsMessage msg = new SpellAdjustmentsMessage();
        try {
            msg.spellValues = buf.readNbt();
            msg.hand = InteractionHand.values()[buf.readInt()];
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading SpellAdjustmentsMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(SpellAdjustmentsMessage msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.getData());
        buf.writeInt(msg.hand.ordinal());
    }
}