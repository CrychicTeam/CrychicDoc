package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.IntProvider;

public class WeatherCommand {

    private static final int DEFAULT_TIME = -1;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("weather").requires(p_139171_ -> p_139171_.hasPermission(2))).then(((LiteralArgumentBuilder) Commands.literal("clear").executes(p_264806_ -> setClear((CommandSourceStack) p_264806_.getSource(), -1))).then(Commands.argument("duration", TimeArgument.time(1)).executes(p_264807_ -> setClear((CommandSourceStack) p_264807_.getSource(), IntegerArgumentType.getInteger(p_264807_, "duration")))))).then(((LiteralArgumentBuilder) Commands.literal("rain").executes(p_264805_ -> setRain((CommandSourceStack) p_264805_.getSource(), -1))).then(Commands.argument("duration", TimeArgument.time(1)).executes(p_264809_ -> setRain((CommandSourceStack) p_264809_.getSource(), IntegerArgumentType.getInteger(p_264809_, "duration")))))).then(((LiteralArgumentBuilder) Commands.literal("thunder").executes(p_264808_ -> setThunder((CommandSourceStack) p_264808_.getSource(), -1))).then(Commands.argument("duration", TimeArgument.time(1)).executes(p_264804_ -> setThunder((CommandSourceStack) p_264804_.getSource(), IntegerArgumentType.getInteger(p_264804_, "duration"))))));
    }

    private static int getDuration(CommandSourceStack commandSourceStack0, int int1, IntProvider intProvider2) {
        return int1 == -1 ? intProvider2.sample(commandSourceStack0.getLevel().m_213780_()) : int1;
    }

    private static int setClear(CommandSourceStack commandSourceStack0, int int1) {
        commandSourceStack0.getLevel().setWeatherParameters(getDuration(commandSourceStack0, int1, ServerLevel.RAIN_DELAY), 0, false, false);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.weather.set.clear"), true);
        return int1;
    }

    private static int setRain(CommandSourceStack commandSourceStack0, int int1) {
        commandSourceStack0.getLevel().setWeatherParameters(0, getDuration(commandSourceStack0, int1, ServerLevel.RAIN_DURATION), true, false);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.weather.set.rain"), true);
        return int1;
    }

    private static int setThunder(CommandSourceStack commandSourceStack0, int int1) {
        commandSourceStack0.getLevel().setWeatherParameters(0, getDuration(commandSourceStack0, int1, ServerLevel.THUNDER_DURATION), true, true);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.weather.set.thunder"), true);
        return int1;
    }
}