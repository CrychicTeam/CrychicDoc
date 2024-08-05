package net.zanckor.questapi.mod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.server.command.ConfigCommand;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.mod.common.datapack.CompoundTagDialogJSONListener;
import net.zanckor.questapi.mod.common.datapack.DialogJSONListener;
import net.zanckor.questapi.mod.common.datapack.EntityTypeDialogJSONListener;
import net.zanckor.questapi.mod.common.datapack.QuestJSONListener;
import net.zanckor.questapi.mod.server.command.QuestCommand;

@EventBusSubscriber(modid = "questapi", bus = Bus.FORGE)
public class EventHandlerRegister {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent e) {
        CommonMain.Constants.LOG.info("QuestAPI Commands registered");
        CommandDispatcher<CommandSourceStack> dispatcher = e.getDispatcher();
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("quests").then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("add").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("questID", StringArgumentType.string()).suggests(EventHandlerRegister::addQuestSuggestions).executes(context -> {
            try {
                return QuestCommand.addQuest(context, EntityArgument.getPlayer(context, "player").m_20148_(), StringArgumentType.getString(context, "questID"));
            } catch (IOException var2) {
                CommonMain.Constants.LOG.error(var2.getMessage());
                return 0;
            }
        })))).then(Commands.literal("itemDialog").then(Commands.argument("dialogID", StringArgumentType.string()).suggests(EventHandlerRegister::addDialogSuggestions).executes(context -> QuestCommand.putDialogToItem(((CommandSourceStack) context.getSource()).getPlayer().m_21205_(), StringArgumentType.getString(context, "dialogID")))))).then(Commands.literal("itemQuest").then(Commands.argument("questID", StringArgumentType.string()).suggests(EventHandlerRegister::addQuestSuggestions).executes(context -> QuestCommand.putQuestToItem(((CommandSourceStack) context.getSource()).getPlayer().m_21205_(), StringArgumentType.getString(context, "questID"))))))).then(Commands.literal("remove").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("questID", StringArgumentType.string()).suggests(EventHandlerRegister::removeQuestSuggestions).executes(context -> {
            try {
                return QuestCommand.removeQuest(context, EntityArgument.getPlayer(context, "player").m_20148_(), StringArgumentType.getString(context, "questID"));
            } catch (IOException var2) {
                CommonMain.Constants.LOG.error(var2.getMessage());
                return 0;
            }
        }))))).then(Commands.literal("displayDialog").then(Commands.argument("dialogID", StringArgumentType.string()).suggests(EventHandlerRegister::addDialogSuggestions).executes(context -> {
            try {
                return QuestCommand.displayDialog(((CommandSourceStack) context.getSource()).getPlayer(), StringArgumentType.getString(context, "dialogID"));
            } catch (IOException var2) {
                throw new RuntimeException(var2);
            }
        }))));
        ConfigCommand.register(e.getDispatcher());
    }

    private static CompletableFuture<Suggestions> removeQuestSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        Player player = ((CommandSourceStack) ctx.getSource()).getPlayer();
        List<File[]> questsFile = new ArrayList();
        Path userFolder = CommonMain.getUserFolder(player.m_20148_());
        Path activeQuestFolder = CommonMain.getActiveQuest(userFolder);
        Path completedQuestFolder = CommonMain.getCompletedQuest(userFolder);
        Path uncompletedQuestFolder = CommonMain.getFailedQuest(userFolder);
        questsFile.add(activeQuestFolder.toFile().listFiles());
        questsFile.add(completedQuestFolder.toFile().listFiles());
        questsFile.add(uncompletedQuestFolder.toFile().listFiles());
        for (File[] questList : questsFile) {
            for (File quest : questList) {
                builder.suggest(quest.getName().substring(0, quest.getName().length() - 5));
            }
        }
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> trackedQuestSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        Player player = ((CommandSourceStack) ctx.getSource()).getPlayer();
        Path userFolder = CommonMain.getUserFolder(player.m_20148_());
        File[] questList = CommonMain.getActiveQuest(userFolder).toFile().listFiles();
        for (File quest : questList) {
            builder.suggest(quest.getName().substring(0, quest.getName().length() - 5));
        }
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> addQuestSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        File[] questsFile = CommonMain.serverQuests.toFile().listFiles();
        for (File quest : questsFile) {
            builder.suggest(quest.getName().substring(0, quest.getName().length() - 5));
        }
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> addDialogSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        File[] questsFile = CommonMain.serverDialogs.toFile().listFiles();
        for (File quest : questsFile) {
            builder.suggest(quest.getName().substring(0, quest.getName().length() - 5));
        }
        return builder.buildFuture();
    }

    @SubscribeEvent
    public static void jsonListener(AddReloadListenerEvent e) {
        QuestJSONListener.register(e);
        DialogJSONListener.register(e);
        CompoundTagDialogJSONListener.register(e);
        EntityTypeDialogJSONListener.register(e);
    }
}