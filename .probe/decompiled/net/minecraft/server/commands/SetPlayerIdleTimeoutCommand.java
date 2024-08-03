package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class SetPlayerIdleTimeoutCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("setidletimeout").requires(p_138639_ -> p_138639_.hasPermission(3))).then(Commands.argument("minutes", IntegerArgumentType.integer(0)).executes(p_138637_ -> setIdleTimeout((CommandSourceStack) p_138637_.getSource(), IntegerArgumentType.getInteger(p_138637_, "minutes")))));
    }

    private static int setIdleTimeout(CommandSourceStack commandSourceStack0, int int1) {
        commandSourceStack0.getServer().setPlayerIdleTimeout(int1);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.setidletimeout.success", int1), true);
        return int1;
    }
}