package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.TileEntityMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class InscriptionTableSetModifierMessage extends TileEntityMessage {

    private ResourceLocation modifier;

    private int index;

    public InscriptionTableSetModifierMessage() {
        super(null);
        this.messageIsValid = false;
    }

    public InscriptionTableSetModifierMessage(BlockPos position, ResourceLocation modifier, int index) {
        super(position);
        this.modifier = modifier;
        this.index = index;
        this.messageIsValid = true;
    }

    public final ResourceLocation getModifier() {
        return this.modifier;
    }

    public final int getIndex() {
        return this.index;
    }

    public static final InscriptionTableSetModifierMessage decode(FriendlyByteBuf buf) {
        InscriptionTableSetModifierMessage msg = new InscriptionTableSetModifierMessage();
        try {
            msg.modifier = buf.readResourceLocation();
            msg.index = buf.readInt();
            msg.pos = buf.readBlockPos();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading MagicSyncMessageToClient: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static final void encode(InscriptionTableSetModifierMessage msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.modifier);
        buf.writeInt(msg.index);
        buf.writeBlockPos(msg.pos);
    }
}