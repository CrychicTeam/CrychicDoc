package com.simibubi.create.content.equipment.clipboard;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ClipboardEditPacket extends SimplePacketBase {

    private int hotbarSlot;

    private CompoundTag data;

    private BlockPos targetedBlock;

    public ClipboardEditPacket(int hotbarSlot, CompoundTag data, @Nullable BlockPos targetedBlock) {
        this.hotbarSlot = hotbarSlot;
        this.data = data;
        this.targetedBlock = targetedBlock;
    }

    public ClipboardEditPacket(FriendlyByteBuf buffer) {
        this.hotbarSlot = buffer.readVarInt();
        this.data = buffer.readNbt();
        if (buffer.readBoolean()) {
            this.targetedBlock = buffer.readBlockPos();
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.hotbarSlot);
        buffer.writeNbt(this.data);
        buffer.writeBoolean(this.targetedBlock != null);
        if (this.targetedBlock != null) {
            buffer.writeBlockPos(this.targetedBlock);
        }
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (this.targetedBlock != null) {
                Level world = sender.m_9236_();
                if (world != null && world.isLoaded(this.targetedBlock)) {
                    if (this.targetedBlock.m_123314_(sender.m_20183_(), 20.0)) {
                        if (world.getBlockEntity(this.targetedBlock) instanceof ClipboardBlockEntity cbe) {
                            cbe.dataContainer.setTag(this.data.isEmpty() ? null : this.data);
                            cbe.onEditedBy(sender);
                        }
                    }
                }
            } else {
                ItemStack itemStack = sender.m_150109_().getItem(this.hotbarSlot);
                if (AllBlocks.CLIPBOARD.isIn(itemStack)) {
                    itemStack.setTag(this.data.isEmpty() ? null : this.data);
                }
            }
        });
        return true;
    }
}