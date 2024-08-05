package net.mehvahdjukaar.supplementaries.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ReloadConfigsCommand implements Command<CommandSourceStack> {

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return ((LiteralArgumentBuilder) Commands.literal("reload").requires(p -> p.hasPermission(0))).executes(new ReloadConfigsCommand());
    }

    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommonConfigs.SPEC.sendSyncedConfigsToAllPlayers();
        ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("message.supplementaries.command.configs_reloaded"), false);
        return 0;
    }
}