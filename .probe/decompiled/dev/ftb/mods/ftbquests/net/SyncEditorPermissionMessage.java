package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import net.minecraft.network.FriendlyByteBuf;

public class SyncEditorPermissionMessage extends BaseS2CMessage {

    private final boolean hasPermission;

    public SyncEditorPermissionMessage(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    public SyncEditorPermissionMessage(FriendlyByteBuf buf) {
        this.hasPermission = buf.readBoolean();
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.SYNC_EDITOR_PERMISSION;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.hasPermission);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.setEditorPermission(this.hasPermission);
    }
}