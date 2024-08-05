package net.zanckor.questapi.mod.common.network.packet.npcmarker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;

public class ValidNPCMarker {

    List<String> entityTypeList;

    Map<String, String> entityTagMap;

    public ValidNPCMarker() {
    }

    public ValidNPCMarker(FriendlyByteBuf buffer) {
        String entityStringList = buffer.readUtf();
        this.entityTypeList = new ArrayList(Arrays.asList(entityStringList.substring(1, entityStringList.length() - 1).split(",")));
        this.entityTagMap = buffer.readMap(FriendlyByteBuf::m_130277_, FriendlyByteBuf::m_130277_);
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        try {
            buffer.writeUtf(QuestDialogManager.conversationByEntityType.keySet().toString());
            HashMap<String, String> entityTagMap = new HashMap();
            for (String key : QuestDialogManager.conversationByCompoundTag.keySet()) {
                String fileContent = Files.readString(((File) QuestDialogManager.conversationByCompoundTag.get(key)).toPath());
                entityTagMap.put(key, fileContent);
            }
            buffer.writeMap(entityTagMap, FriendlyByteBuf::m_130070_, FriendlyByteBuf::m_130070_);
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        }
    }

    public static void handler(ValidNPCMarker msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.setAvailableEntityTypeForQuest(msg.entityTypeList, msg.entityTagMap)));
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}