package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class SyncLockMessage extends BaseS2CMessage {

    private final UUID id;

    private final boolean lock;

    public SyncLockMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readUUID();
        this.lock = buffer.readBoolean();
    }

    public SyncLockMessage(UUID i, boolean e) {
        this.id = i;
        this.lock = e;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.SYNC_LOCK;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.id);
        buffer.writeBoolean(this.lock);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.syncLock(this.id, this.lock);
    }
}