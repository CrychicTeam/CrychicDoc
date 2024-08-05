package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import com.mna.recipes.multiblock.MultiblockDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public class StructureSyncMessage extends BaseMessage {

    private CompoundTag _data;

    private ResourceLocation structureID;

    private StructureSyncMessage(CompoundTag data, ResourceLocation structureID) {
        this._data = data;
        this.structureID = structureID;
        this.messageIsValid = true;
    }

    private StructureSyncMessage() {
        this.messageIsValid = false;
    }

    public CompoundTag getData() {
        return this._data;
    }

    public ResourceLocation getStructureID() {
        return this.structureID;
    }

    public static StructureSyncMessage decode(FriendlyByteBuf buf) {
        StructureSyncMessage msg = new StructureSyncMessage();
        try {
            msg._data = buf.readNbt();
            msg.structureID = buf.readResourceLocation();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading StructureSyncMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(StructureSyncMessage msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.getData());
        buf.writeResourceLocation(msg.getStructureID());
    }

    public static StructureSyncMessage fromMultiblock(MultiblockDefinition multiblock, ServerLevel world) {
        return new StructureSyncMessage(multiblock.serializeCoreBlocks(world), multiblock.m_6423_());
    }
}