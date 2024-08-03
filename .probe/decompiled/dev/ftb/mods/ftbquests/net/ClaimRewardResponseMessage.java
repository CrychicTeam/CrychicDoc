package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class ClaimRewardResponseMessage extends BaseS2CMessage {

    private final UUID team;

    private final UUID player;

    private final long reward;

    ClaimRewardResponseMessage(FriendlyByteBuf buffer) {
        this.team = buffer.readUUID();
        this.player = buffer.readUUID();
        this.reward = buffer.readLong();
    }

    public ClaimRewardResponseMessage(UUID t, UUID p, long r) {
        this.team = t;
        this.player = p;
        this.reward = r;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.CLAIM_REWARD_RESPONSE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.team);
        buffer.writeUUID(this.player);
        buffer.writeLong(this.reward);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.claimReward(this.team, this.player, this.reward);
    }
}