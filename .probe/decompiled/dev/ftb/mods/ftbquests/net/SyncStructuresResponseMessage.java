package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.task.StructureTask;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;

public class SyncStructuresResponseMessage extends BaseS2CMessage {

    private final List<String> data = new ArrayList();

    public SyncStructuresResponseMessage(MinecraftServer server) {
        this.data.addAll(server.registryAccess().m_175515_(Registries.STRUCTURE).registryKeySet().stream().map(o -> o.location().toString()).sorted(String::compareTo).toList());
        this.data.addAll(server.registryAccess().m_175515_(Registries.STRUCTURE).getTagNames().map(o -> "#" + o.location()).sorted(String::compareTo).toList());
    }

    public SyncStructuresResponseMessage(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            this.data.add(buf.readUtf(32767));
        }
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.SYNC_STRUCTURES_RESPONSE;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.data.size());
        this.data.forEach(buf::m_130070_);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        StructureTask.syncKnownStructureList(this.data);
    }
}