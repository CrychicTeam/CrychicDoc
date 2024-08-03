package com.simibubi.create.foundation.gui.menu;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class GhostItemSubmitPacket extends SimplePacketBase {

    private final ItemStack item;

    private final int slot;

    public GhostItemSubmitPacket(ItemStack item, int slot) {
        this.item = item;
        this.slot = slot;
    }

    public GhostItemSubmitPacket(FriendlyByteBuf buffer) {
        this.item = buffer.readItem();
        this.slot = buffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(this.item);
        buffer.writeInt(this.slot);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (player.f_36096_ instanceof GhostItemMenu<?> menu) {
                    menu.ghostInventory.setStackInSlot(this.slot, this.item);
                    menu.m_38853_(36 + this.slot).setChanged();
                }
            }
        });
        return true;
    }
}