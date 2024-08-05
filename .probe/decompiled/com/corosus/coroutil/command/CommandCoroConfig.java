package com.corosus.coroutil.command;

import com.corosus.coroutil.util.CULog;
import com.corosus.modconfig.ConfigMod;
import com.corosus.modconfig.CoroConfigRegistry;
import com.corosus.modconfig.ModConfigData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig.Type;

public class CommandCoroConfig {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal(getCommandName()).requires(s -> s.hasPermission(2))).then(Commands.literal("config").then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("common").then(argumentReload(Type.COMMON))).then(argumentSave())).then(argumentGet())).then(argumentSet()))));
    }

    public static ArgumentBuilder<CommandSourceStack, ?> argumentReload(Type type) {
        return Commands.literal("reload").executes(c -> {
            CULog.log("reloading all mods common configurations from disk");
            ConfigTracker.INSTANCE.loadConfigs(type, ConfigMod.instance().getConfigPath());
            ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Reloading all common configs from disk"), true);
            return 1;
        });
    }

    public static ArgumentBuilder<CommandSourceStack, ?> argumentSave() {
        return Commands.literal("save").executes(c -> {
            CULog.log("saving all coro mods runtime configs to disk");
            CoroConfigRegistry.instance().forceSaveAllFilesFromRuntimeSettings();
            ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Saving all common coro configs to disk"), true);
            return 1;
        });
    }

    public static ArgumentBuilder<CommandSourceStack, ?> argumentGet() {
        return Commands.literal("get").then(Commands.argument("file_name", StringArgumentType.word()).suggests((p_136339_, p_136340_) -> SharedSuggestionProvider.suggest(getConfigs(), p_136340_)).then(Commands.argument("setting_name", StringArgumentType.word()).suggests((p_136339_, p_136340_) -> SharedSuggestionProvider.suggest(getConfigSettings(StringArgumentType.getString(p_136339_, "file_name")), p_136340_)).executes(c -> {
            String fileName = fileToConfig(StringArgumentType.getString(c, "file_name"));
            String configName = ((ModConfigData) CoroConfigRegistry.instance().lookupFilePathToConfig.get(fileName)).configID;
            String settingName = StringArgumentType.getString(c, "setting_name");
            Object obj = CoroConfigRegistry.instance().getField(configName, settingName);
            ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal(settingName + " = " + obj + " in " + fileName), true);
            return 1;
        })));
    }

    public static ArgumentBuilder<CommandSourceStack, ?> argumentSet() {
        return Commands.literal("set").then(Commands.argument("file_name", StringArgumentType.word()).suggests((p_136339_, p_136340_) -> SharedSuggestionProvider.suggest(getConfigs(), p_136340_)).then(Commands.argument("setting_name", StringArgumentType.word()).suggests((p_136339_, p_136340_) -> SharedSuggestionProvider.suggest(getConfigSettings(StringArgumentType.getString(p_136339_, "file_name")), p_136340_)).then(Commands.argument("value", StringArgumentType.string()).executes(c -> {
            String fileName = fileToConfig(StringArgumentType.getString(c, "file_name"));
            String configName = ((ModConfigData) CoroConfigRegistry.instance().lookupFilePathToConfig.get(fileName)).configID;
            String settingName = StringArgumentType.getString(c, "setting_name");
            String value = StringArgumentType.getString(c, "value");
            boolean result = CoroConfigRegistry.instance().updateField(configName, settingName, value);
            if (result) {
                Object obj = CoroConfigRegistry.instance().getField(configName, settingName);
                CoroConfigRegistry.instance().forceSaveAllFilesFromRuntimeSettings();
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Set " + settingName + " to " + obj + " in " + fileName), true);
            } else {
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Invalid setting to use for " + settingName), true);
            }
            return 1;
        }))));
    }

    public static String getCommandName() {
        return "coro";
    }

    public static Iterable<String> getConfigs() {
        return CoroConfigRegistry.instance().lookupFilePathToConfig.keySet().stream().map(e -> e.replace("\\", "--")).toList();
    }

    public static Iterable<String> getConfigSettings(String config_name) {
        ModConfigData modConfigData = (ModConfigData) CoroConfigRegistry.instance().lookupFilePathToConfig.get(fileToConfig(config_name));
        if (modConfigData != null) {
            List<String> joinedList = new ArrayList();
            joinedList.addAll(modConfigData.valsString.keySet());
            joinedList.addAll(modConfigData.valsInteger.keySet());
            joinedList.addAll(modConfigData.valsDouble.keySet());
            joinedList.addAll(modConfigData.valsBoolean.keySet());
            Collections.sort(joinedList);
            return joinedList;
        } else {
            return List.of("<-- invalid config name");
        }
    }

    public static String fileToConfig(String str) {
        return str.replace("--", "\\");
    }
}