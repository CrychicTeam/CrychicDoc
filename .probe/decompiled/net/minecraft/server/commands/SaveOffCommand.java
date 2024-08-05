package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class SaveOffCommand {

    private static final SimpleCommandExceptionType ERROR_ALREADY_OFF = new SimpleCommandExceptionType(Component.translatable("commands.save.alreadyOff"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("save-off").requires(p_138289_ -> p_138289_.hasPermission(4))).executes(p_138287_ -> {
            CommandSourceStack $$1 = (CommandSourceStack) p_138287_.getSource();
            boolean $$2 = false;
            for (ServerLevel $$3 : $$1.getServer().getAllLevels()) {
                if ($$3 != null && !$$3.noSave) {
                    $$3.noSave = true;
                    $$2 = true;
                }
            }
            if (!$$2) {
                throw ERROR_ALREADY_OFF.create();
            } else {
                $$1.sendSuccess(() -> Component.translatable("commands.save.disabled"), true);
                return 1;
            }
        }));
    }
}