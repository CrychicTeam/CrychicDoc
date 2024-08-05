package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ReturnCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("return").requires(p_281281_ -> p_281281_.hasPermission(2))).then(Commands.argument("value", IntegerArgumentType.integer()).executes(p_281464_ -> setReturn((CommandSourceStack) p_281464_.getSource(), IntegerArgumentType.getInteger(p_281464_, "value")))));
    }

    private static int setReturn(CommandSourceStack commandSourceStack0, int int1) {
        commandSourceStack0.getReturnValueConsumer().accept(int1);
        return int1;
    }
}