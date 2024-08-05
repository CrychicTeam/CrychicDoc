package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;

public class PatterningPrismPasteMessage extends BaseMessage {

    private CompoundTag logic;

    private InteractionHand hand;

    private boolean isParticleEmitter;

    public PatterningPrismPasteMessage(CompoundTag logic, InteractionHand hand, boolean isParticleEmitter) {
        this.logic = logic;
        this.hand = hand;
        this.isParticleEmitter = isParticleEmitter;
        this.messageIsValid = true;
    }

    public PatterningPrismPasteMessage() {
        this.messageIsValid = false;
    }

    public CompoundTag getLogic() {
        return this.logic;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public boolean isParticleEmitter() {
        return this.isParticleEmitter;
    }

    public static PatterningPrismPasteMessage decode(FriendlyByteBuf buf) {
        PatterningPrismPasteMessage msg = new PatterningPrismPasteMessage();
        try {
            msg.logic = buf.readAnySizeNbt();
            msg.hand = InteractionHand.values()[buf.readInt()];
            msg.isParticleEmitter = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading PatterningPrismPasteMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(PatterningPrismPasteMessage msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.getLogic());
        buf.writeInt(msg.hand.ordinal());
        buf.writeBoolean(msg.isParticleEmitter());
    }
}