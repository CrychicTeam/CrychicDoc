package net.zanckor.questapi.mod.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.zanckor.questapi.mod.common.network.packet.dialogoption.AddQuest;
import net.zanckor.questapi.mod.common.network.packet.dialogoption.CloseDialog;
import net.zanckor.questapi.mod.common.network.packet.dialogoption.DialogRequestPacket;
import net.zanckor.questapi.mod.common.network.packet.dialogoption.DisplayDialog;
import net.zanckor.questapi.mod.common.network.packet.npcmarker.ValidNPCMarker;
import net.zanckor.questapi.mod.common.network.packet.quest.ActiveQuestList;
import net.zanckor.questapi.mod.common.network.packet.quest.QuestDataPacket;
import net.zanckor.questapi.mod.common.network.packet.quest.TimerPacket;
import net.zanckor.questapi.mod.common.network.packet.quest.ToastPacket;
import net.zanckor.questapi.mod.common.network.packet.screen.ModifyTrackedQuests;
import net.zanckor.questapi.mod.common.network.packet.screen.OpenVanillaEntityScreen;
import net.zanckor.questapi.mod.common.network.packet.screen.RemovedQuest;
import net.zanckor.questapi.mod.common.network.packet.screen.RequestActiveQuests;
import net.zanckor.questapi.mod.common.network.packet.screen.UpdateQuestTracked;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "2.1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("questapi", "forge_network"), () -> "2.1", "2.1"::equals, "2.1"::equals);

    public static void register() {
        int index = 0;
        CHANNEL.messageBuilder(QuestDataPacket.class, index++, NetworkDirection.PLAY_TO_SERVER).encoder(QuestDataPacket::encodeBuffer).decoder(QuestDataPacket::new).consumerNetworkThread(QuestDataPacket::handler).add();
        CHANNEL.messageBuilder(TimerPacket.class, index++, NetworkDirection.PLAY_TO_SERVER).encoder(TimerPacket::encodeBuffer).decoder(TimerPacket::new).consumerNetworkThread(TimerPacket::handler).add();
        CHANNEL.messageBuilder(RequestActiveQuests.class, index++, NetworkDirection.PLAY_TO_SERVER).encoder(RequestActiveQuests::encodeBuffer).decoder(RequestActiveQuests::new).consumerNetworkThread(RequestActiveQuests::handler).add();
        CHANNEL.messageBuilder(DialogRequestPacket.class, index++, NetworkDirection.PLAY_TO_SERVER).encoder(DialogRequestPacket::encodeBuffer).decoder(DialogRequestPacket::new).consumerNetworkThread(DialogRequestPacket::handler).add();
        CHANNEL.messageBuilder(AddQuest.class, index++, NetworkDirection.PLAY_TO_SERVER).encoder(AddQuest::encodeBuffer).decoder(AddQuest::new).consumerNetworkThread(AddQuest::handler).add();
        CHANNEL.messageBuilder(OpenVanillaEntityScreen.class, index++, NetworkDirection.PLAY_TO_SERVER).encoder(OpenVanillaEntityScreen::encodeBuffer).decoder(OpenVanillaEntityScreen::new).consumerNetworkThread(OpenVanillaEntityScreen::handler).add();
        CHANNEL.messageBuilder(ToastPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(ToastPacket::encodeBuffer).decoder(ToastPacket::new).consumerNetworkThread(ToastPacket::handler).add();
        CHANNEL.messageBuilder(DisplayDialog.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(DisplayDialog::encodeBuffer).decoder(DisplayDialog::new).consumerNetworkThread(DisplayDialog::handler).add();
        CHANNEL.messageBuilder(ModifyTrackedQuests.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(ModifyTrackedQuests::encodeBuffer).decoder(ModifyTrackedQuests::new).consumerNetworkThread(ModifyTrackedQuests::handler).add();
        CHANNEL.messageBuilder(CloseDialog.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(CloseDialog::encodeBuffer).decoder(CloseDialog::new).consumerNetworkThread(CloseDialog::handler).add();
        CHANNEL.messageBuilder(UpdateQuestTracked.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(UpdateQuestTracked::encodeBuffer).decoder(UpdateQuestTracked::new).consumerNetworkThread(UpdateQuestTracked::handler).add();
        CHANNEL.messageBuilder(RemovedQuest.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(RemovedQuest::encodeBuffer).decoder(RemovedQuest::new).consumerNetworkThread(RemovedQuest::handler).add();
        CHANNEL.messageBuilder(ValidNPCMarker.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(ValidNPCMarker::encodeBuffer).decoder(ValidNPCMarker::new).consumerNetworkThread(ValidNPCMarker::handler).add();
        CHANNEL.messageBuilder(ActiveQuestList.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(ActiveQuestList::encodeBuffer).decoder(ActiveQuestList::new).consumerNetworkThread(ActiveQuestList::handler).add();
    }
}