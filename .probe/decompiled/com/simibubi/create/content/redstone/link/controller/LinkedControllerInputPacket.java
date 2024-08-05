package com.simibubi.create.content.redstone.link.controller;

import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.foundation.utility.Couple;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LinkedControllerInputPacket extends LinkedControllerPacketBase {

    private Collection<Integer> activatedButtons;

    private boolean press;

    public LinkedControllerInputPacket(Collection<Integer> activatedButtons, boolean press) {
        this(activatedButtons, press, null);
    }

    public LinkedControllerInputPacket(Collection<Integer> activatedButtons, boolean press, BlockPos lecternPos) {
        super(lecternPos);
        this.activatedButtons = activatedButtons;
        this.press = press;
    }

    public LinkedControllerInputPacket(FriendlyByteBuf buffer) {
        super(buffer);
        this.activatedButtons = new ArrayList();
        this.press = buffer.readBoolean();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            this.activatedButtons.add(buffer.readVarInt());
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        super.write(buffer);
        buffer.writeBoolean(this.press);
        buffer.writeVarInt(this.activatedButtons.size());
        this.activatedButtons.forEach(buffer::m_130130_);
    }

    @Override
    protected void handleLectern(ServerPlayer player, LecternControllerBlockEntity lectern) {
        if (lectern.isUsedBy(player)) {
            this.handleItem(player, lectern.getController());
        }
    }

    @Override
    protected void handleItem(ServerPlayer player, ItemStack heldItem) {
        Level world = player.m_20193_();
        UUID uniqueID = player.m_20148_();
        BlockPos pos = player.m_20183_();
        if (!player.isSpectator() || !this.press) {
            LinkedControllerServerHandler.receivePressed(world, pos, uniqueID, (List<Couple<RedstoneLinkNetworkHandler.Frequency>>) this.activatedButtons.stream().map(i -> LinkedControllerItem.toFrequency(heldItem, i)).collect(Collectors.toList()), this.press);
        }
    }
}