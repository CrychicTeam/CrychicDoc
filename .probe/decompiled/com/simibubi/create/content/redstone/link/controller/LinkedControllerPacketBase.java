package com.simibubi.create.content.redstone.link.controller;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public abstract class LinkedControllerPacketBase extends SimplePacketBase {

    private BlockPos lecternPos;

    public LinkedControllerPacketBase(BlockPos lecternPos) {
        this.lecternPos = lecternPos;
    }

    public LinkedControllerPacketBase(FriendlyByteBuf buffer) {
        if (buffer.readBoolean()) {
            this.lecternPos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
    }

    protected boolean inLectern() {
        return this.lecternPos != null;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.inLectern());
        if (this.inLectern()) {
            buffer.writeInt(this.lecternPos.m_123341_());
            buffer.writeInt(this.lecternPos.m_123342_());
            buffer.writeInt(this.lecternPos.m_123343_());
        }
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (this.inLectern()) {
                    BlockEntity be = player.m_9236_().getBlockEntity(this.lecternPos);
                    if (!(be instanceof LecternControllerBlockEntity)) {
                        return;
                    }
                    this.handleLectern(player, (LecternControllerBlockEntity) be);
                } else {
                    ItemStack controller = player.m_21205_();
                    if (!AllItems.LINKED_CONTROLLER.isIn(controller)) {
                        controller = player.m_21206_();
                        if (!AllItems.LINKED_CONTROLLER.isIn(controller)) {
                            return;
                        }
                    }
                    this.handleItem(player, controller);
                }
            }
        });
        return true;
    }

    protected abstract void handleItem(ServerPlayer var1, ItemStack var2);

    protected abstract void handleLectern(ServerPlayer var1, LecternControllerBlockEntity var2);
}