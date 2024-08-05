package net.zanckor.questapi.mod.common.network.packet.dialogoption;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.mod.common.network.handler.ServerHandler;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogOption;

public class AddQuest {

    EnumDialogOption optionType;

    int optionID;

    public AddQuest(EnumDialogOption optionType, int optionID) {
        this.optionType = optionType;
        this.optionID = optionID;
    }

    public AddQuest(FriendlyByteBuf buffer) {
        this.optionType = buffer.readEnum(EnumDialogOption.class);
        this.optionID = buffer.readInt();
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.optionType);
        buffer.writeInt(this.optionID);
    }

    public static void handler(AddQuest msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Player player = ((NetworkEvent.Context) ctx.get()).getSender();
            ServerHandler.addQuest(player, msg.optionType, msg.optionID);
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}