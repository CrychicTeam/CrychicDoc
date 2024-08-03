package net.zanckor.questapi.mod.common.network.packet.quest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;
import net.zanckor.questapi.util.GsonManager;

public class ActiveQuestList {

    List<String> questFileList = new ArrayList();

    int listSize;

    public ActiveQuestList(UUID playerUUID) {
        try {
            File[] activeQuests = CommonMain.getActiveQuest(CommonMain.getUserFolder(playerUUID)).toFile().listFiles();
            if (activeQuests != null) {
                for (File file : activeQuests) {
                    this.questFileList.add(Files.readString(file.toPath()));
                }
            }
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
    }

    public ActiveQuestList(FriendlyByteBuf buffer) {
        this.listSize = buffer.readInt();
        for (int i = 0; i < this.listSize; i++) {
            this.questFileList.add(buffer.readUtf());
        }
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeInt(this.questFileList.size());
        for (String s : this.questFileList) {
            buffer.writeUtf(s);
        }
    }

    public static void handler(ActiveQuestList msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            List<UserQuest> userQuestList = new ArrayList();
            msg.questFileList.forEach(questFile -> {
                try {
                    userQuestList.add((UserQuest) GsonManager.getJsonClass(questFile, UserQuest.class));
                } catch (IOException var3) {
                    throw new RuntimeException(var3);
                }
            });
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.setActiveQuestList(userQuestList));
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}