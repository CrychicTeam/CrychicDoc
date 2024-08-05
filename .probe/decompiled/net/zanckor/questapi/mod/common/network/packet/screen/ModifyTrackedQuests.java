package net.zanckor.questapi.mod.common.network.packet.screen;

import java.io.IOException;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;
import net.zanckor.questapi.util.GsonManager;

public class ModifyTrackedQuests {

    private final UserQuest userQuest;

    private final Boolean addQuest;

    public ModifyTrackedQuests(boolean addQuest, UserQuest userQuest) {
        this.userQuest = userQuest;
        this.addQuest = addQuest;
    }

    public ModifyTrackedQuests(FriendlyByteBuf buffer) {
        try {
            this.addQuest = buffer.readBoolean();
            this.userQuest = (UserQuest) GsonManager.getJsonClass(buffer.readUtf(), UserQuest.class);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.addQuest);
        buffer.writeUtf(GsonManager.gson.toJson(this.userQuest));
    }

    public static void handler(ModifyTrackedQuests msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.modifyTrackedQuests(msg.addQuest, msg.userQuest)));
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}