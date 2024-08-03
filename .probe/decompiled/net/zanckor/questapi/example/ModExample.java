package net.zanckor.questapi.example;

import java.io.IOException;
import java.util.Arrays;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.file.quest.register.QuestTemplateRegistry;
import net.zanckor.questapi.api.registry.ScreenRegistry;
import net.zanckor.questapi.example.client.screen.dialog.DialogScreen;
import net.zanckor.questapi.example.client.screen.dialog.MinimalistDialogScreen;
import net.zanckor.questapi.example.client.screen.hud.RenderQuestTracker;
import net.zanckor.questapi.example.client.screen.questlog.QuestLog;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.LoadDialog;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.LoadDialogList;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.LoadQuest;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.LoadTagDialogList;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogOption;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogReq;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumGoalType;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumQuestRequirement;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumQuestReward;

@EventBusSubscriber(modid = "questapi", bus = Bus.FORGE)
public class ModExample {

    public ModExample() {
        CommonMain.Constants.LOG.info("Creating Example code");
        Arrays.stream(EnumGoalType.values()).forEach(QuestTemplateRegistry::registerQuest);
        Arrays.stream(EnumQuestReward.values()).forEach(QuestTemplateRegistry::registerReward);
        Arrays.stream(EnumQuestRequirement.values()).forEach(QuestTemplateRegistry::registerQuestRequirement);
        Arrays.stream(EnumDialogReq.values()).forEach(QuestTemplateRegistry::registerDialogRequirement);
        Arrays.stream(EnumDialogOption.values()).forEach(QuestTemplateRegistry::registerDialogOption);
    }

    @SubscribeEvent
    public static void registerTemplates(ServerAboutToStartEvent e) throws IOException {
        CommonMain.Constants.LOG.info("Register Template files");
        LoadQuest.registerQuest(e.getServer(), "questapi");
        LoadDialog.registerDialog(e.getServer(), "questapi");
        LoadDialogList.registerNPCDialogList(e.getServer(), "questapi");
        LoadTagDialogList.registerNPCTagDialogList(e.getServer(), "questapi");
        LoadQuest.registerDatapackQuest(e.getServer());
        LoadDialog.registerDatapackDialog(e.getServer());
        LoadDialogList.registerDatapackDialogList(e.getServer());
        LoadTagDialogList.registerDatapackTagDialogList(e.getServer());
    }

    @EventBusSubscriber(modid = "questapi", bus = Bus.MOD, value = { Dist.CLIENT })
    public static class ClientModExample {

        @SubscribeEvent
        public static void registerTargetTypeEnum(FMLClientSetupEvent e) {
            Arrays.stream(EnumGoalType.EnumTargetType.values()).forEach(QuestTemplateRegistry::registerTargetType);
        }

        @SubscribeEvent
        public static void registerScreen(FMLClientSetupEvent e) {
            ScreenRegistry.registerDialogScreen("questapi", new DialogScreen(Component.literal("dialog_screen")));
            ScreenRegistry.registerDialogScreen("minimalist_questapi", new MinimalistDialogScreen(Component.literal("minimalist_dialog_screen")));
            ScreenRegistry.registerQuestTrackedScreen("questapi", new RenderQuestTracker());
            ScreenRegistry.registerQuestLogScreen("questapi", new QuestLog(Component.literal("quest_log_screen")));
        }
    }
}