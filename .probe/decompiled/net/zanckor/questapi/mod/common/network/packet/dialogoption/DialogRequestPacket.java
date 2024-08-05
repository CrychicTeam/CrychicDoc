package net.zanckor.questapi.mod.common.network.packet.dialogoption;

import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.api.screen.NpcType;
import net.zanckor.questapi.mod.common.network.handler.ServerHandler;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogOption;

public class DialogRequestPacket {

    EnumDialogOption optionType;

    int optionID;

    UUID entityUUID;

    Item item;

    NpcType npcType;

    public DialogRequestPacket(EnumDialogOption optionType, int optionID, Entity npc, Item item, NpcType npcType) {
        this.optionType = optionType;
        this.optionID = optionID;
        this.entityUUID = npc.getUUID();
        this.item = item;
        this.npcType = npcType;
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.optionType);
        buffer.writeInt(this.optionID);
        this.encodeNpcType(buffer);
    }

    private void encodeNpcType(FriendlyByteBuf buf) {
        buf.writeEnum(this.npcType);
        switch(this.npcType) {
            case ITEM:
                buf.writeItem(this.item.getDefaultInstance());
                break;
            case UUID:
            case RESOURCE_LOCATION:
                buf.writeUUID(this.entityUUID);
        }
    }

    public DialogRequestPacket(FriendlyByteBuf buffer) {
        this.optionType = buffer.readEnum(EnumDialogOption.class);
        this.optionID = buffer.readInt();
        this.decodeNpcType(buffer);
    }

    public void decodeNpcType(FriendlyByteBuf buf) {
        this.npcType = buf.readEnum(NpcType.class);
        switch(this.npcType) {
            case ITEM:
                this.item = buf.readItem().getItem();
                break;
            case UUID:
            case RESOURCE_LOCATION:
                this.entityUUID = buf.readUUID();
        }
    }

    public static void handler(DialogRequestPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ServerPlayer player = ((NetworkEvent.Context) ctx.get()).getSender();
            ServerHandler.requestDialog(player, msg.optionID, msg.optionType, msg.entityUUID, msg.item, msg.npcType);
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}