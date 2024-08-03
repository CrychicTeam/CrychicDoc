package dev.latvian.mods.kubejs.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.util.NotificationBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class NotificationMessage extends BaseS2CMessage {

    private final NotificationBuilder notification;

    public NotificationMessage(NotificationBuilder notification) {
        this.notification = notification;
    }

    NotificationMessage(FriendlyByteBuf buf) {
        this.notification = new NotificationBuilder(buf);
    }

    @Override
    public MessageType getType() {
        return KubeJSNet.NOTIFICATION;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        this.notification.write(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player p0 = KubeJS.PROXY.getClientPlayer();
        if (p0 != null) {
            p0.kjs$notify(this.notification);
        }
    }
}