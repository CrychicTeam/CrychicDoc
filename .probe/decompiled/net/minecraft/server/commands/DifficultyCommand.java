package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;

public class DifficultyCommand {

    private static final DynamicCommandExceptionType ERROR_ALREADY_DIFFICULT = new DynamicCommandExceptionType(p_136948_ -> Component.translatable("commands.difficulty.failure", p_136948_));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        LiteralArgumentBuilder<CommandSourceStack> $$1 = Commands.literal("difficulty");
        for (Difficulty $$2 : Difficulty.values()) {
            $$1.then(Commands.literal($$2.getKey()).executes(p_136937_ -> setDifficulty((CommandSourceStack) p_136937_.getSource(), $$2)));
        }
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) $$1.requires(p_136943_ -> p_136943_.hasPermission(2))).executes(p_288367_ -> {
            Difficulty $$1x = ((CommandSourceStack) p_288367_.getSource()).getLevel().m_46791_();
            ((CommandSourceStack) p_288367_.getSource()).sendSuccess(() -> Component.translatable("commands.difficulty.query", $$1x.getDisplayName()), false);
            return $$1x.getId();
        }));
    }

    public static int setDifficulty(CommandSourceStack commandSourceStack0, Difficulty difficulty1) throws CommandSyntaxException {
        MinecraftServer $$2 = commandSourceStack0.getServer();
        if ($$2.getWorldData().getDifficulty() == difficulty1) {
            throw ERROR_ALREADY_DIFFICULT.create(difficulty1.getKey());
        } else {
            $$2.setDifficulty(difficulty1, true);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.difficulty.success", difficulty1.getDisplayName()), true);
            return 0;
        }
    }
}