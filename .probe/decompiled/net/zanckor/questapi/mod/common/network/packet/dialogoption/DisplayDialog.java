package net.zanckor.questapi.mod.common.network.packet.dialogoption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.dialog.codec.Conversation;
import net.zanckor.questapi.api.file.dialog.codec.NPCDialog;
import net.zanckor.questapi.api.screen.NpcType;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;
import net.zanckor.questapi.mod.common.util.MCUtil;

public class DisplayDialog {

    Conversation dialogTemplate;

    String identifier;

    int dialogID;

    int optionSize;

    String questDialog;

    HashMap<Integer, List<String>> optionStrings = new HashMap();

    HashMap<Integer, List<Integer>> optionIntegers = new HashMap();

    UUID entityUUID;

    String resourceLocation;

    Item item;

    NpcType npcType;

    private void displayDialog(Conversation conversation, String identifier, int dialogID, Player player) throws IOException {
        this.dialogTemplate = conversation;
        this.dialogID = dialogID;
        this.identifier = identifier != null ? identifier : "questapi";
        QuestDialogManager.currentDialog.put(player, dialogID);
        MCUtil.writeDialogRead(player, conversation.getConversationID(), dialogID);
    }

    public DisplayDialog(Conversation dialogTemplate, String identifier, int dialogID, Player player, Entity entity) throws IOException {
        this.displayDialog(dialogTemplate, identifier, dialogID, player);
        this.entityUUID = entity == null ? player.m_20148_() : entity.getUUID();
        this.npcType = NpcType.UUID;
    }

    public DisplayDialog(Conversation dialogTemplate, String identifier, int dialogID, Player player, String resourceLocation) throws IOException {
        this.displayDialog(dialogTemplate, identifier, dialogID, player);
        this.resourceLocation = resourceLocation;
        this.npcType = NpcType.RESOURCE_LOCATION;
    }

    public DisplayDialog(Conversation dialogTemplate, String identifier, int dialogID, Player player, Item item) throws IOException {
        this.displayDialog(dialogTemplate, identifier, dialogID, player);
        this.item = item;
        this.npcType = NpcType.ITEM;
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        NPCDialog.QuestDialog dialog = (NPCDialog.QuestDialog) this.dialogTemplate.getDialog().get(this.dialogID);
        this.encodeNpcType(buffer);
        buffer.writeUtf(this.identifier);
        buffer.writeUtf(dialog.getDialogText());
        buffer.writeInt(dialog.getOptions().size());
        for (NPCDialog.DialogOption option : dialog.getOptions()) {
            String optionText = option.getText() == null ? "" : option.getText();
            String optionQuestID = option.getQuest_id() == null ? "" : option.getQuest_id();
            String optionGlobalID = option.getGlobal_id() == null ? "" : option.getGlobal_id();
            buffer.writeUtf(optionText);
            buffer.writeUtf(option.getType());
            buffer.writeUtf(optionQuestID);
            buffer.writeUtf(optionGlobalID);
            buffer.writeInt(option.getDialog());
        }
    }

    private void encodeNpcType(FriendlyByteBuf buf) {
        buf.writeEnum(this.npcType);
        switch(this.npcType) {
            case ITEM:
                buf.writeItem(this.item.getDefaultInstance());
                break;
            case UUID:
                buf.writeUUID(this.entityUUID);
                break;
            case RESOURCE_LOCATION:
                buf.writeUtf(this.resourceLocation);
        }
    }

    public DisplayDialog(FriendlyByteBuf buffer) {
        this.decodeNpcType(buffer);
        this.identifier = buffer.readUtf();
        this.questDialog = buffer.readUtf();
        this.optionSize = buffer.readInt();
        for (int optionSizeIndex = 0; optionSizeIndex < this.optionSize; optionSizeIndex++) {
            List<String> optionStringData = new ArrayList();
            List<Integer> optionIntegerData = new ArrayList();
            optionStringData.add(buffer.readUtf());
            optionStringData.add(buffer.readUtf());
            optionStringData.add(buffer.readUtf());
            optionStringData.add(buffer.readUtf());
            optionIntegerData.add(buffer.readInt());
            this.optionStrings.put(optionSizeIndex, optionStringData);
            this.optionIntegers.put(optionSizeIndex, optionIntegerData);
        }
    }

    private void decodeNpcType(FriendlyByteBuf buf) {
        this.npcType = buf.readEnum(NpcType.class);
        switch(this.npcType) {
            case ITEM:
                this.item = buf.readItem().getItem();
                break;
            case UUID:
                this.entityUUID = buf.readUUID();
                break;
            case RESOURCE_LOCATION:
                this.resourceLocation = buf.readUtf();
        }
    }

    public static void handler(DisplayDialog msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.displayDialog(msg.identifier, msg.dialogID, msg.questDialog, msg.optionSize, msg.optionIntegers, msg.optionStrings, msg.entityUUID, msg.resourceLocation, msg.item, msg.npcType)));
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}