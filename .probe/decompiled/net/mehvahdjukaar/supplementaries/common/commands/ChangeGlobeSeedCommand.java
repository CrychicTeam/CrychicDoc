package net.mehvahdjukaar.supplementaries.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Random;
import net.mehvahdjukaar.supplementaries.common.misc.globe.GlobeData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class ChangeGlobeSeedCommand implements Command<CommandSourceStack> {

    private static final Random rand = new Random();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return ((LiteralArgumentBuilder) Commands.literal("newseed").requires(cs -> cs.hasPermission(2))).executes(new ChangeGlobeSeedCommand());
    }

    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerLevel level = ((CommandSourceStack) context.getSource()).getLevel();
        GlobeData newData = new GlobeData(rand.nextLong());
        GlobeData.set(level, newData);
        newData.sendToClient(level);
        ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("message.supplementaries.command.globe_changed"), false);
        return 0;
    }
}