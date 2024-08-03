package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class TogglePinnedMessage extends BaseC2SMessage {

    private final long id;

    TogglePinnedMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
    }

    public TogglePinnedMessage(long i) {
        this.id = i;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.TOGGLE_PINNED;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ServerPlayer player = (ServerPlayer) context.getPlayer();
        TeamData data = ServerQuestFile.INSTANCE.getOrCreateTeamData(player);
        boolean newPinned = !data.isQuestPinned(player, this.id);
        data.setQuestPinned(player, this.id, newPinned);
        new TogglePinnedResponseMessage(this.id, newPinned).sendTo(player);
    }
}