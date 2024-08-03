package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.TileEntityMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class RunescribingTableMutexChangeMessage extends TileEntityMessage {

    long hMutex;

    long vMutex;

    int playerTier;

    boolean isRemove;

    public RunescribingTableMutexChangeMessage() {
        super(null);
        this.messageIsValid = false;
    }

    public RunescribingTableMutexChangeMessage(BlockPos position, long hMutex, long vMutex, int playerTier, boolean isRemove) {
        super(position);
        this.hMutex = hMutex;
        this.vMutex = vMutex;
        this.playerTier = playerTier;
        this.isRemove = isRemove;
        this.messageIsValid = true;
    }

    public long getHMutex() {
        return this.hMutex;
    }

    public long getVMutex() {
        return this.vMutex;
    }

    public int getPlayerTier() {
        return this.playerTier;
    }

    public boolean getIsRemove() {
        return this.isRemove;
    }

    public static final RunescribingTableMutexChangeMessage decode(FriendlyByteBuf buf) {
        RunescribingTableMutexChangeMessage msg = new RunescribingTableMutexChangeMessage();
        try {
            msg.pos = buf.readBlockPos();
            msg.hMutex = buf.readLong();
            msg.vMutex = buf.readLong();
            msg.playerTier = buf.readInt();
            msg.isRemove = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading MagicSyncMessageToClient: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static final void encode(RunescribingTableMutexChangeMessage msg, FriendlyByteBuf buf) {
        try {
            buf.writeBlockPos(msg.pos);
            buf.writeLong(msg.hMutex);
            buf.writeLong(msg.vMutex);
            buf.writeInt(msg.playerTier);
            buf.writeBoolean(msg.isRemove);
        } catch (Exception var3) {
            ManaAndArtifice.LOGGER.error("Exception while writing RunescribingTableMutexChangeMessage: " + var3);
        }
    }
}