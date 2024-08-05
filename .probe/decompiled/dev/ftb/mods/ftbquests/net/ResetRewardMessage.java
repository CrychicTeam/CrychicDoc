package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class ResetRewardMessage extends BaseS2CMessage {

    private final UUID team;

    private final UUID player;

    private final long id;

    ResetRewardMessage(FriendlyByteBuf buffer) {
        this.team = buffer.readUUID();
        this.player = buffer.readUUID();
        this.id = buffer.readLong();
    }

    public ResetRewardMessage(UUID t, UUID p, long i) {
        this.team = t;
        this.player = p;
        this.id = i;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.RESET_REWARD;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.team);
        buffer.writeUUID(this.player);
        buffer.writeLong(this.id);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.resetReward(this.team, this.player, this.id);
    }
}