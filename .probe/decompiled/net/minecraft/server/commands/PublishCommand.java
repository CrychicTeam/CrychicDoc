package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.level.GameType;

public class PublishCommand {

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.publish.failed"));

    private static final DynamicCommandExceptionType ERROR_ALREADY_PUBLISHED = new DynamicCommandExceptionType(p_138194_ -> Component.translatable("commands.publish.alreadyPublished", p_138194_));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("publish").requires(p_138189_ -> p_138189_.hasPermission(4))).executes(p_258235_ -> publish((CommandSourceStack) p_258235_.getSource(), HttpUtil.getAvailablePort(), false, null))).then(((RequiredArgumentBuilder) Commands.argument("allowCommands", BoolArgumentType.bool()).executes(p_258236_ -> publish((CommandSourceStack) p_258236_.getSource(), HttpUtil.getAvailablePort(), BoolArgumentType.getBool(p_258236_, "allowCommands"), null))).then(((RequiredArgumentBuilder) Commands.argument("gamemode", GameModeArgument.gameMode()).executes(p_258237_ -> publish((CommandSourceStack) p_258237_.getSource(), HttpUtil.getAvailablePort(), BoolArgumentType.getBool(p_258237_, "allowCommands"), GameModeArgument.getGameMode(p_258237_, "gamemode")))).then(Commands.argument("port", IntegerArgumentType.integer(0, 65535)).executes(p_258238_ -> publish((CommandSourceStack) p_258238_.getSource(), IntegerArgumentType.getInteger(p_258238_, "port"), BoolArgumentType.getBool(p_258238_, "allowCommands"), GameModeArgument.getGameMode(p_258238_, "gamemode")))))));
    }

    private static int publish(CommandSourceStack commandSourceStack0, int int1, boolean boolean2, @Nullable GameType gameType3) throws CommandSyntaxException {
        if (commandSourceStack0.getServer().isPublished()) {
            throw ERROR_ALREADY_PUBLISHED.create(commandSourceStack0.getServer().getPort());
        } else if (!commandSourceStack0.getServer().publishServer(gameType3, boolean2, int1)) {
            throw ERROR_FAILED.create();
        } else {
            commandSourceStack0.sendSuccess(() -> getSuccessMessage(int1), true);
            return int1;
        }
    }

    public static MutableComponent getSuccessMessage(int int0) {
        Component $$1 = ComponentUtils.copyOnClickText(String.valueOf(int0));
        return Component.translatable("commands.publish.started", $$1);
    }
}