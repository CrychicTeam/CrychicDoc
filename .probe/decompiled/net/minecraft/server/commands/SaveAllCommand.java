package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

public class SaveAllCommand {

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.save.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("save-all").requires(p_138276_ -> p_138276_.hasPermission(4))).executes(p_138281_ -> saveAll((CommandSourceStack) p_138281_.getSource(), false))).then(Commands.literal("flush").executes(p_138274_ -> saveAll((CommandSourceStack) p_138274_.getSource(), true))));
    }

    private static int saveAll(CommandSourceStack commandSourceStack0, boolean boolean1) throws CommandSyntaxException {
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.save.saving"), false);
        MinecraftServer $$2 = commandSourceStack0.getServer();
        boolean $$3 = $$2.saveEverything(true, boolean1, true);
        if (!$$3) {
            throw ERROR_FAILED.create();
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.save.success"), true);
            return 1;
        }
    }
}