package de.keksuccino.fancymenu.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.keksuccino.fancymenu.networking.PacketHandler;
import de.keksuccino.fancymenu.networking.packets.commands.variable.command.VariableCommandPacket;
import de.keksuccino.konkrete.command.CommandUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class VariableCommand {

    public static final Map<String, List<String>> CACHED_VARIABLE_SUGGESTIONS = Collections.synchronizedMap(new HashMap());

    public static void register(CommandDispatcher<CommandSourceStack> d) {
        d.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) net.minecraft.commands.Commands.literal("fmvariable").then(net.minecraft.commands.Commands.literal("get").then(net.minecraft.commands.Commands.argument("variable_name", StringArgumentType.string()).suggests((context, builder) -> CommandUtils.getStringSuggestions(builder, getVariableNameSuggestions(((CommandSourceStack) context.getSource()).getPlayerOrException()))).executes(stack -> getVariable((CommandSourceStack) stack.getSource(), StringArgumentType.getString(stack, "variable_name")))))).then(net.minecraft.commands.Commands.literal("set").then(net.minecraft.commands.Commands.argument("variable_name", StringArgumentType.string()).suggests((context, builder) -> CommandUtils.getStringSuggestions(builder, getVariableNameSuggestions(((CommandSourceStack) context.getSource()).getPlayerOrException()))).then(net.minecraft.commands.Commands.argument("send_chat_feedback", BoolArgumentType.bool()).then(net.minecraft.commands.Commands.argument("set_to_value", StringArgumentType.greedyString()).suggests((context, builder) -> CommandUtils.getStringSuggestions(builder, "<set_to_value>")).executes(stack -> setVariable((CommandSourceStack) stack.getSource(), StringArgumentType.getString(stack, "variable_name"), StringArgumentType.getString(stack, "set_to_value"), BoolArgumentType.getBool(stack, "send_chat_feedback"))))))));
    }

    private static String[] getVariableNameSuggestions(ServerPlayer sender) {
        List<String> l = new ArrayList((Collection) Objects.requireNonNullElse((List) CACHED_VARIABLE_SUGGESTIONS.get(sender.m_20148_().toString()), new ArrayList()));
        if (l.isEmpty()) {
            l.add("<no_variables_found>");
        }
        return (String[]) l.toArray(new String[0]);
    }

    private static int getVariable(CommandSourceStack stack, String variableName) {
        try {
            if (variableName != null) {
                ServerPlayer sender = stack.getPlayerOrException();
                VariableCommandPacket packet = new VariableCommandPacket();
                packet.set = false;
                packet.variable_name = variableName;
                PacketHandler.sendToClient(sender, packet);
            }
        } catch (Exception var4) {
            stack.sendFailure(Component.literal("Error while executing command!"));
            var4.printStackTrace();
        }
        return 1;
    }

    private static int setVariable(CommandSourceStack stack, String variableName, String setToValue, boolean sendFeedback) {
        try {
            if (variableName != null && setToValue != null) {
                ServerPlayer sender = stack.getPlayerOrException();
                VariableCommandPacket packet = new VariableCommandPacket();
                packet.set = true;
                packet.variable_name = variableName;
                packet.set_to_value = setToValue;
                packet.feedback = sendFeedback;
                PacketHandler.sendToClient(sender, packet);
            }
        } catch (Exception var6) {
            stack.sendFailure(Component.literal("Error while executing command!"));
            var6.printStackTrace();
        }
        return 1;
    }
}