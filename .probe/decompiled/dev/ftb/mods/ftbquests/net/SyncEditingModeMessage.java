package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class SyncEditingModeMessage extends BaseS2CMessage {

    private final UUID uuid;

    private final boolean editingMode;

    public SyncEditingModeMessage(FriendlyByteBuf buffer) {
        this.uuid = buffer.readUUID();
        this.editingMode = buffer.readBoolean();
    }

    public SyncEditingModeMessage(UUID id, boolean e) {
        this.uuid = id;
        this.editingMode = e;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.SYNC_EDITING_MODE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.uuid);
        buffer.writeBoolean(this.editingMode);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.syncEditingMode(this.uuid, this.editingMode);
    }
}