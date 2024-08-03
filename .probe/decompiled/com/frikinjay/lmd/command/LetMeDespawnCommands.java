package com.frikinjay.lmd.command;

import com.frikinjay.lmd.LetMeDespawn;
import com.frikinjay.lmd.config.LetMeDespawnConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.MobCategory;

public class LetMeDespawnCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("lmd").then(Commands.literal("reload").executes(LetMeDespawnCommands::reloadConfig))).then(Commands.literal("add").then(Commands.argument("mobName", StringArgumentType.greedyString()).suggests((context, builder) -> {
            BuiltInRegistries.ENTITY_TYPE.forEach(entityType -> {
                if (entityType.getCategory().equals(MobCategory.MONSTER)) {
                    builder.suggest(BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString());
                }
            });
            return builder.buildFuture();
        }).executes(LetMeDespawnCommands::addMob)))).then(Commands.literal("remove").then(Commands.argument("mobName", StringArgumentType.greedyString()).suggests(LetMeDespawnCommands::suggestMobNames).executes(LetMeDespawnCommands::removeMob))));
    }

    private static CompletableFuture<Suggestions> suggestMobNames(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        LetMeDespawnConfig config = LetMeDespawn.config;
        config.getMobNames().stream().filter(mobName -> mobName.startsWith(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        LetMeDespawn.config = LetMeDespawnConfig.load();
        ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("LetMeDespawn configuration reloaded!").withStyle(style -> style.withColor(ChatFormatting.GREEN)), true);
        return 1;
    }

    private static int addMob(CommandContext<CommandSourceStack> context) {
        String mobName = StringArgumentType.getString(context, "mobName");
        if (LetMeDespawn.config.getMobNames().contains(mobName)) {
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Mob '" + mobName + "' is already in the configuration.").withStyle(style -> style.withColor(TextColor.parseColor(ChatFormatting.RED.toString()))));
        } else {
            LetMeDespawn.config.addMobName(mobName);
            ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Added '" + mobName + "' to LetMeDespawn configuration.").withStyle(style -> style.withColor(TextColor.parseColor(ChatFormatting.AQUA.toString()))), true);
        }
        return 1;
    }

    private static int removeMob(CommandContext<CommandSourceStack> context) {
        String mobName = StringArgumentType.getString(context, "mobName");
        if (LetMeDespawn.config.getMobNames().contains(mobName)) {
            LetMeDespawn.config.removeMobName(mobName);
            ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Removed '" + mobName + "' from LetMeDespawn configuration.").withStyle(style -> style.withColor(TextColor.parseColor(ChatFormatting.GOLD.toString()))), true);
        } else {
            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Mob '" + mobName + "' is not in the configuration.").withStyle(style -> style.withColor(TextColor.parseColor(ChatFormatting.RED.toString()))));
        }
        return 1;
    }
}