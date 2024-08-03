package net.minecraft.server.commands;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import java.util.Map;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class HelpCommand {

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.help.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("help").executes(p_288460_ -> {
            Map<CommandNode<CommandSourceStack>, String> $$2 = commandDispatcherCommandSourceStack0.getSmartUsage(commandDispatcherCommandSourceStack0.getRoot(), (CommandSourceStack) p_288460_.getSource());
            for (String $$3 : $$2.values()) {
                ((CommandSourceStack) p_288460_.getSource()).sendSuccess(() -> Component.literal("/" + $$3), false);
            }
            return $$2.size();
        })).then(Commands.argument("command", StringArgumentType.greedyString()).executes(p_288458_ -> {
            ParseResults<CommandSourceStack> $$2 = commandDispatcherCommandSourceStack0.parse(StringArgumentType.getString(p_288458_, "command"), (CommandSourceStack) p_288458_.getSource());
            if ($$2.getContext().getNodes().isEmpty()) {
                throw ERROR_FAILED.create();
            } else {
                Map<CommandNode<CommandSourceStack>, String> $$3 = commandDispatcherCommandSourceStack0.getSmartUsage(((ParsedCommandNode) Iterables.getLast($$2.getContext().getNodes())).getNode(), (CommandSourceStack) p_288458_.getSource());
                for (String $$4 : $$3.values()) {
                    ((CommandSourceStack) p_288458_.getSource()).sendSuccess(() -> Component.literal("/" + $$2.getReader().getString() + " " + $$4), false);
                }
                return $$3.size();
            }
        })));
    }
}