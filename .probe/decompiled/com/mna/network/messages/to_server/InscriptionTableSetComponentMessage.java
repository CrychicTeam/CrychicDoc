package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.network.messages.TileEntityMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class InscriptionTableSetComponentMessage extends TileEntityMessage {

    private ResourceLocation component;

    public InscriptionTableSetComponentMessage() {
        super(null);
        this.messageIsValid = false;
    }

    public InscriptionTableSetComponentMessage(BlockPos position, ResourceLocation component) {
        super(position);
        this.component = component;
        this.messageIsValid = true;
    }

    public final ResourceLocation getComponent() {
        return this.component;
    }

    public static final InscriptionTableSetComponentMessage decode(FriendlyByteBuf buf) {
        InscriptionTableSetComponentMessage msg = new InscriptionTableSetComponentMessage();
        try {
            msg.component = buf.readResourceLocation();
            msg.pos = buf.readBlockPos();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading MagicSyncMessageToClient: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static final void encode(InscriptionTableSetComponentMessage msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.component);
        buf.writeBlockPos(msg.pos);
    }

    public static final InscriptionTableSetComponentMessage fromInscriptionTable(InscriptionTableTile te) {
        ResourceLocation rLoc = new ResourceLocation("");
        if (te.getCurrentComponent() != null) {
            rLoc = te.getCurrentComponent().getPart().getRegistryName();
        }
        return new InscriptionTableSetComponentMessage(te.m_58899_(), rLoc);
    }
}