package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.TileEntityMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class LodestarLogicSetMessage extends TileEntityMessage {

    private CompoundTag logic;

    public LodestarLogicSetMessage(BlockPos pos, CompoundTag logic) {
        super(pos);
        this.logic = logic;
        this.messageIsValid = true;
    }

    public LodestarLogicSetMessage() {
        super(null);
        this.messageIsValid = false;
    }

    public CompoundTag getLogic() {
        return this.logic;
    }

    public static LodestarLogicSetMessage decode(FriendlyByteBuf buf) {
        LodestarLogicSetMessage msg = new LodestarLogicSetMessage();
        try {
            msg.pos = buf.readBlockPos();
            msg.logic = buf.readAnySizeNbt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading LodestarLogicSetMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(LodestarLogicSetMessage msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.getPosition());
        buf.writeNbt(msg.getLogic());
    }
}