package com.corosus.mobtimizations;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CommandMisc {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal(getCommandName()).requires(s -> s.hasPermission(2))).then(Commands.literal("toggle_on").executes(c -> {
            Mobtimizations.modActive = !Mobtimizations.modActive;
            ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Mod is now " + (Mobtimizations.modActive ? "active" : "inactive")), true);
            return 1;
        }))).then(Commands.literal("cancels").executes(c -> {
            ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Cancels " + Mobtimizations.getCancels()), true);
            return 1;
        })));
    }

    public static String getCommandName() {
        return "mobtimizations";
    }
}