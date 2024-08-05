package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.network.messages.TileEntityMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class InscriptionTableCraftingUpdateMessage extends TileEntityMessage {

    private int totalCraftTicks;

    private int craftTicksPassed;

    private int burnTicksRemaining;

    private int paperRemaining;

    private int inkRemaining;

    public InscriptionTableCraftingUpdateMessage() {
        super(null);
        this.messageIsValid = false;
    }

    public InscriptionTableCraftingUpdateMessage(BlockPos position, int totalCraftTicks, int craftTicksPassed, int burnTicksRemaining, int paperRemaining, int inkRemaining) {
        super(position);
        this.totalCraftTicks = totalCraftTicks;
        this.craftTicksPassed = craftTicksPassed;
        this.burnTicksRemaining = burnTicksRemaining;
        this.paperRemaining = paperRemaining;
        this.inkRemaining = inkRemaining;
        this.messageIsValid = true;
    }

    public static final InscriptionTableCraftingUpdateMessage decode(FriendlyByteBuf buf) {
        InscriptionTableCraftingUpdateMessage msg = new InscriptionTableCraftingUpdateMessage();
        try {
            msg.pos = buf.readBlockPos();
            msg.totalCraftTicks = buf.readInt();
            msg.craftTicksPassed = buf.readInt();
            msg.burnTicksRemaining = buf.readInt();
            msg.paperRemaining = buf.readInt();
            msg.inkRemaining = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading MagicSyncMessageToClient: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public int getTotalCraftTicks() {
        return this.totalCraftTicks;
    }

    public int getCraftTicksPassed() {
        return this.craftTicksPassed;
    }

    public int getBurnTimeRemaining() {
        return this.burnTicksRemaining;
    }

    public int getPaperRemaining() {
        return this.paperRemaining;
    }

    public int getInkRemaining() {
        return this.inkRemaining;
    }

    public static final void encode(InscriptionTableCraftingUpdateMessage msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.totalCraftTicks);
        buf.writeInt(msg.craftTicksPassed);
        buf.writeInt(msg.burnTicksRemaining);
        buf.writeInt(msg.paperRemaining);
        buf.writeInt(msg.inkRemaining);
    }

    public static InscriptionTableCraftingUpdateMessage fromInscriptionTable(InscriptionTableTile te) {
        return new InscriptionTableCraftingUpdateMessage(te.m_58899_(), te.getCraftTicks(), te.getCraftTicksConsumed(), te.getBurnTicksRemaining(), te.getPaperRemaining(), te.getInkRemaining());
    }
}