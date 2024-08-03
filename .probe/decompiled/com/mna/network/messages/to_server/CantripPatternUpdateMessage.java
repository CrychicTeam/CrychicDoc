package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.network.messages.BaseMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class CantripPatternUpdateMessage extends BaseMessage {

    private CompoundTag nbt;

    public CantripPatternUpdateMessage(CompoundTag nbt) {
        this.nbt = nbt;
        this.messageIsValid = true;
    }

    private CantripPatternUpdateMessage() {
        this.messageIsValid = false;
    }

    public CompoundTag getData() {
        return this.nbt;
    }

    public void setData(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static final CantripPatternUpdateMessage decode(FriendlyByteBuf buf) {
        CantripPatternUpdateMessage msg = new CantripPatternUpdateMessage();
        try {
            msg.setData(buf.readNbt());
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading MagicSyncMessageToClient: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static final void encode(CantripPatternUpdateMessage msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.getData());
    }

    public static final CantripPatternUpdateMessage fromCapability(IPlayerMagic magic) {
        return new CantripPatternUpdateMessage(magic.getCantripData().writeToNBT(true));
    }
}