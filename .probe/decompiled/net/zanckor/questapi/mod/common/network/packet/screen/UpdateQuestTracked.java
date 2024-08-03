package net.zanckor.questapi.mod.common.network.packet.screen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;
import net.zanckor.questapi.util.GsonManager;

public class UpdateQuestTracked {

    private final String userQuestJson;

    public UpdateQuestTracked(UserQuest userQuest) {
        this.userQuestJson = GsonManager.gson.toJson(userQuest);
    }

    public UpdateQuestTracked(FriendlyByteBuf buffer) {
        this.userQuestJson = buffer.readUtf();
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.userQuestJson);
    }

    public static void handler(UpdateQuestTracked msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            try {
                File file = File.createTempFile("userQuest", "json");
                Files.writeString(file.toPath(), msg.userQuestJson);
                UserQuest userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
                if (userQuest != null) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.updateQuestTracked(userQuest));
                }
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}