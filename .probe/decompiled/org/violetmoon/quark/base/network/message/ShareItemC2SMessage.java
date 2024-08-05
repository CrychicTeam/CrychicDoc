package org.violetmoon.quark.base.network.message;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.management.module.ItemSharingModule;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;

public class ShareItemC2SMessage implements IZetaMessage {

    public ItemStack toShare;

    public ShareItemC2SMessage() {
    }

    public ShareItemC2SMessage(ItemStack stack) {
        this.toShare = stack;
    }

    @Override
    public boolean receive(IZetaNetworkEventContext context) {
        ServerPlayer sender = context.getSender();
        context.enqueueWork(() -> {
            if (sender != null) {
                MinecraftServer server = sender.m_20194_();
                if (server != null) {
                    if (ItemSharingModule.canShare(sender.m_20148_(), server)) {
                        Component senderName = sender.m_5446_();
                        Quark.ZETA.network.sendToAllPlayers(new ShareItemS2CMessage(sender.m_20148_(), senderName, this.toShare), server);
                    }
                }
            }
        });
        return true;
    }
}