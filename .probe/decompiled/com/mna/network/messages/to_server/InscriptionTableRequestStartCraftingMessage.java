package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.network.messages.TileEntityMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class InscriptionTableRequestStartCraftingMessage extends TileEntityMessage {

    public InscriptionTableRequestStartCraftingMessage() {
        super(null);
        this.messageIsValid = false;
    }

    public InscriptionTableRequestStartCraftingMessage(BlockPos position) {
        super(position);
        this.messageIsValid = true;
    }

    public static final InscriptionTableRequestStartCraftingMessage decode(FriendlyByteBuf buf) {
        InscriptionTableRequestStartCraftingMessage msg = new InscriptionTableRequestStartCraftingMessage();
        try {
            msg.pos = buf.readBlockPos();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading MagicSyncMessageToClient: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static final void encode(InscriptionTableRequestStartCraftingMessage msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static final InscriptionTableRequestStartCraftingMessage fromInscriptionTable(InscriptionTableTile te) {
        return new InscriptionTableRequestStartCraftingMessage(te.m_58899_());
    }
}