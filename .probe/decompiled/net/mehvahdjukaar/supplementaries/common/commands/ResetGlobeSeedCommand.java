package net.mehvahdjukaar.supplementaries.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mehvahdjukaar.supplementaries.common.misc.globe.GlobeData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class ResetGlobeSeedCommand implements Command<CommandSourceStack> {

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return ((LiteralArgumentBuilder) Commands.literal("resetseed").requires(cs -> cs.hasPermission(2))).executes(new ResetGlobeSeedCommand());
    }

    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerLevel level = ((CommandSourceStack) context.getSource()).getLevel();
        GlobeData data = new GlobeData(level.getSeed());
        GlobeData.set(level, data);
        data.sendToClient(level);
        ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("message.supplementaries.command.globe_reset"), false);
        return 0;
    }
}