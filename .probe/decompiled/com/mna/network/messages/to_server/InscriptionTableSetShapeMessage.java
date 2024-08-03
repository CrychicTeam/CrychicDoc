package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.network.messages.TileEntityMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class InscriptionTableSetShapeMessage extends TileEntityMessage {

    private ResourceLocation shape;

    public InscriptionTableSetShapeMessage() {
        super(null);
        this.messageIsValid = false;
    }

    public InscriptionTableSetShapeMessage(BlockPos position, ResourceLocation shape) {
        super(position);
        this.shape = shape;
        this.messageIsValid = true;
    }

    public final ResourceLocation getShape() {
        return this.shape;
    }

    public static final InscriptionTableSetShapeMessage decode(FriendlyByteBuf buf) {
        InscriptionTableSetShapeMessage msg = new InscriptionTableSetShapeMessage();
        try {
            msg.shape = buf.readResourceLocation();
            msg.pos = buf.readBlockPos();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading MagicSyncMessageToClient: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static final void encode(InscriptionTableSetShapeMessage msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.shape);
        buf.writeBlockPos(msg.pos);
    }

    public static final InscriptionTableSetShapeMessage fromInscriptionTable(InscriptionTableTile te) {
        ResourceLocation rLoc = new ResourceLocation("");
        if (te.getCurrentShape() != null) {
            rLoc = te.getCurrentShape().getPart().getRegistryName();
        }
        return new InscriptionTableSetShapeMessage(te.m_58899_(), rLoc);
    }
}