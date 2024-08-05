package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class TimeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("time").requires(p_139076_ -> p_139076_.hasPermission(2))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("set").then(Commands.literal("day").executes(p_139101_ -> setTime((CommandSourceStack) p_139101_.getSource(), 1000)))).then(Commands.literal("noon").executes(p_139099_ -> setTime((CommandSourceStack) p_139099_.getSource(), 6000)))).then(Commands.literal("night").executes(p_139097_ -> setTime((CommandSourceStack) p_139097_.getSource(), 13000)))).then(Commands.literal("midnight").executes(p_139095_ -> setTime((CommandSourceStack) p_139095_.getSource(), 18000)))).then(Commands.argument("time", TimeArgument.time()).executes(p_139093_ -> setTime((CommandSourceStack) p_139093_.getSource(), IntegerArgumentType.getInteger(p_139093_, "time")))))).then(Commands.literal("add").then(Commands.argument("time", TimeArgument.time()).executes(p_139091_ -> addTime((CommandSourceStack) p_139091_.getSource(), IntegerArgumentType.getInteger(p_139091_, "time")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("query").then(Commands.literal("daytime").executes(p_139086_ -> queryTime((CommandSourceStack) p_139086_.getSource(), getDayTime(((CommandSourceStack) p_139086_.getSource()).getLevel()))))).then(Commands.literal("gametime").executes(p_288691_ -> queryTime((CommandSourceStack) p_288691_.getSource(), (int) (((CommandSourceStack) p_288691_.getSource()).getLevel().m_46467_() % 2147483647L))))).then(Commands.literal("day").executes(p_288689_ -> queryTime((CommandSourceStack) p_288689_.getSource(), (int) (((CommandSourceStack) p_288689_.getSource()).getLevel().m_46468_() / 24000L % 2147483647L))))));
    }

    private static int getDayTime(ServerLevel serverLevel0) {
        return (int) (serverLevel0.m_46468_() % 24000L);
    }

    private static int queryTime(CommandSourceStack commandSourceStack0, int int1) {
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.time.query", int1), false);
        return int1;
    }

    public static int setTime(CommandSourceStack commandSourceStack0, int int1) {
        for (ServerLevel $$2 : commandSourceStack0.getServer().getAllLevels()) {
            $$2.setDayTime((long) int1);
        }
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.time.set", int1), true);
        return getDayTime(commandSourceStack0.getLevel());
    }

    public static int addTime(CommandSourceStack commandSourceStack0, int int1) {
        for (ServerLevel $$2 : commandSourceStack0.getServer().getAllLevels()) {
            $$2.setDayTime($$2.m_46468_() + (long) int1);
        }
        int $$3 = getDayTime(commandSourceStack0.getLevel());
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.time.set", $$3), true);
        return $$3;
    }
}