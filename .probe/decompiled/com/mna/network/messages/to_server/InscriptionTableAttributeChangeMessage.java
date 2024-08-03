package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.api.spells.attributes.Attribute;
import com.mna.network.messages.TileEntityMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class InscriptionTableAttributeChangeMessage extends TileEntityMessage {

    private Attribute attr;

    private float value;

    private InscriptionTableAttributeChangeMessage.ChangeType type;

    public InscriptionTableAttributeChangeMessage() {
        super(null);
        this.messageIsValid = false;
    }

    public InscriptionTableAttributeChangeMessage(BlockPos position, Attribute attr, float value, InscriptionTableAttributeChangeMessage.ChangeType type) {
        super(position);
        this.attr = attr;
        this.value = value;
        this.type = type;
        this.messageIsValid = true;
    }

    public final Attribute getAttribute() {
        return this.attr;
    }

    public final float getValue() {
        return this.value;
    }

    public final InscriptionTableAttributeChangeMessage.ChangeType getChangeType() {
        return this.type;
    }

    public static final InscriptionTableAttributeChangeMessage decode(FriendlyByteBuf buf) {
        InscriptionTableAttributeChangeMessage msg = new InscriptionTableAttributeChangeMessage();
        try {
            msg.attr = Attribute.valueOf(buf.readUtf(32767));
            msg.value = buf.readFloat();
            msg.type = InscriptionTableAttributeChangeMessage.ChangeType.valueOf(buf.readUtf(512));
            msg.pos = buf.readBlockPos();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading MagicSyncMessageToClient: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static final void encode(InscriptionTableAttributeChangeMessage msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.attr.name());
        buf.writeFloat(msg.value);
        buf.writeUtf(msg.type.name());
        buf.writeBlockPos(msg.pos);
    }

    public static enum ChangeType {

        SHAPE, COMPONENT
    }
}