package net.mehvahdjukaar.supplementaries.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

public class IUsedToRollTheDice implements Command<CommandSourceStack> {

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return ((LiteralArgumentBuilder) Commands.literal("roll").requires(cs -> cs.hasPermission(0))).then(Commands.argument("dice", IntegerArgumentType.integer(1)).executes(new IUsedToRollTheDice()));
    }

    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        RandomSource r = ((CommandSourceStack) context.getSource()).getLevel().f_46441_;
        int dice = IntegerArgumentType.getInteger(context, "dice");
        int roll = r.nextInt(dice);
        ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("message.supplementaries.command.dice", dice, roll), false);
        return roll;
    }
}