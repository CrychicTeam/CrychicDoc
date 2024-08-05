package com.simibubi.create.foundation.gui.menu;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ClearMenuPacket extends SimplePacketBase {

    public ClearMenuPacket() {
    }

    public ClearMenuPacket(FriendlyByteBuf buffer) {
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (player.f_36096_ instanceof IClearableMenu) {
                    ((IClearableMenu) player.f_36096_).clearContents();
                }
            }
        });
        return true;
    }
}