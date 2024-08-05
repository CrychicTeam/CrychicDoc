package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Locale;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.Vec2;

public class WorldBorderCommand {

    private static final SimpleCommandExceptionType ERROR_SAME_CENTER = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.center.failed"));

    private static final SimpleCommandExceptionType ERROR_SAME_SIZE = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.nochange"));

    private static final SimpleCommandExceptionType ERROR_TOO_SMALL = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.small"));

    private static final SimpleCommandExceptionType ERROR_TOO_BIG = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.big", 5.999997E7F));

    private static final SimpleCommandExceptionType ERROR_TOO_FAR_OUT = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.far", 2.9999984E7));

    private static final SimpleCommandExceptionType ERROR_SAME_WARNING_TIME = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.warning.time.failed"));

    private static final SimpleCommandExceptionType ERROR_SAME_WARNING_DISTANCE = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.warning.distance.failed"));

    private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_BUFFER = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.damage.buffer.failed"));

    private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_AMOUNT = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.damage.amount.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("worldborder").requires(p_139268_ -> p_139268_.hasPermission(2))).then(Commands.literal("add").then(((RequiredArgumentBuilder) Commands.argument("distance", DoubleArgumentType.doubleArg(-5.999997E7F, 5.999997E7F)).executes(p_289305_ -> setSize((CommandSourceStack) p_289305_.getSource(), ((CommandSourceStack) p_289305_.getSource()).getLevel().m_6857_().getSize() + DoubleArgumentType.getDouble(p_289305_, "distance"), 0L))).then(Commands.argument("time", IntegerArgumentType.integer(0)).executes(p_289306_ -> setSize((CommandSourceStack) p_289306_.getSource(), ((CommandSourceStack) p_289306_.getSource()).getLevel().m_6857_().getSize() + DoubleArgumentType.getDouble(p_289306_, "distance"), ((CommandSourceStack) p_289306_.getSource()).getLevel().m_6857_().getLerpRemainingTime() + (long) IntegerArgumentType.getInteger(p_289306_, "time") * 1000L)))))).then(Commands.literal("set").then(((RequiredArgumentBuilder) Commands.argument("distance", DoubleArgumentType.doubleArg(-5.999997E7F, 5.999997E7F)).executes(p_139286_ -> setSize((CommandSourceStack) p_139286_.getSource(), DoubleArgumentType.getDouble(p_139286_, "distance"), 0L))).then(Commands.argument("time", IntegerArgumentType.integer(0)).executes(p_139284_ -> setSize((CommandSourceStack) p_139284_.getSource(), DoubleArgumentType.getDouble(p_139284_, "distance"), (long) IntegerArgumentType.getInteger(p_139284_, "time") * 1000L)))))).then(Commands.literal("center").then(Commands.argument("pos", Vec2Argument.vec2()).executes(p_139282_ -> setCenter((CommandSourceStack) p_139282_.getSource(), Vec2Argument.getVec2(p_139282_, "pos")))))).then(((LiteralArgumentBuilder) Commands.literal("damage").then(Commands.literal("amount").then(Commands.argument("damagePerBlock", FloatArgumentType.floatArg(0.0F)).executes(p_139280_ -> setDamageAmount((CommandSourceStack) p_139280_.getSource(), FloatArgumentType.getFloat(p_139280_, "damagePerBlock")))))).then(Commands.literal("buffer").then(Commands.argument("distance", FloatArgumentType.floatArg(0.0F)).executes(p_139278_ -> setDamageBuffer((CommandSourceStack) p_139278_.getSource(), FloatArgumentType.getFloat(p_139278_, "distance"))))))).then(Commands.literal("get").executes(p_139276_ -> getSize((CommandSourceStack) p_139276_.getSource())))).then(((LiteralArgumentBuilder) Commands.literal("warning").then(Commands.literal("distance").then(Commands.argument("distance", IntegerArgumentType.integer(0)).executes(p_139266_ -> setWarningDistance((CommandSourceStack) p_139266_.getSource(), IntegerArgumentType.getInteger(p_139266_, "distance")))))).then(Commands.literal("time").then(Commands.argument("time", IntegerArgumentType.integer(0)).executes(p_139249_ -> setWarningTime((CommandSourceStack) p_139249_.getSource(), IntegerArgumentType.getInteger(p_139249_, "time")))))));
    }

    private static int setDamageBuffer(CommandSourceStack commandSourceStack0, float float1) throws CommandSyntaxException {
        WorldBorder $$2 = commandSourceStack0.getServer().overworld().m_6857_();
        if ($$2.getDamageSafeZone() == (double) float1) {
            throw ERROR_SAME_DAMAGE_BUFFER.create();
        } else {
            $$2.setDamageSafeZone((double) float1);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.worldborder.damage.buffer.success", String.format(Locale.ROOT, "%.2f", float1)), true);
            return (int) float1;
        }
    }

    private static int setDamageAmount(CommandSourceStack commandSourceStack0, float float1) throws CommandSyntaxException {
        WorldBorder $$2 = commandSourceStack0.getServer().overworld().m_6857_();
        if ($$2.getDamagePerBlock() == (double) float1) {
            throw ERROR_SAME_DAMAGE_AMOUNT.create();
        } else {
            $$2.setDamagePerBlock((double) float1);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.worldborder.damage.amount.success", String.format(Locale.ROOT, "%.2f", float1)), true);
            return (int) float1;
        }
    }

    private static int setWarningTime(CommandSourceStack commandSourceStack0, int int1) throws CommandSyntaxException {
        WorldBorder $$2 = commandSourceStack0.getServer().overworld().m_6857_();
        if ($$2.getWarningTime() == int1) {
            throw ERROR_SAME_WARNING_TIME.create();
        } else {
            $$2.setWarningTime(int1);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.worldborder.warning.time.success", int1), true);
            return int1;
        }
    }

    private static int setWarningDistance(CommandSourceStack commandSourceStack0, int int1) throws CommandSyntaxException {
        WorldBorder $$2 = commandSourceStack0.getServer().overworld().m_6857_();
        if ($$2.getWarningBlocks() == int1) {
            throw ERROR_SAME_WARNING_DISTANCE.create();
        } else {
            $$2.setWarningBlocks(int1);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.worldborder.warning.distance.success", int1), true);
            return int1;
        }
    }

    private static int getSize(CommandSourceStack commandSourceStack0) {
        double $$1 = commandSourceStack0.getServer().overworld().m_6857_().getSize();
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.worldborder.get", String.format(Locale.ROOT, "%.0f", $$1)), false);
        return Mth.floor($$1 + 0.5);
    }

    private static int setCenter(CommandSourceStack commandSourceStack0, Vec2 vec1) throws CommandSyntaxException {
        WorldBorder $$2 = commandSourceStack0.getServer().overworld().m_6857_();
        if ($$2.getCenterX() == (double) vec1.x && $$2.getCenterZ() == (double) vec1.y) {
            throw ERROR_SAME_CENTER.create();
        } else if (!((double) Math.abs(vec1.x) > 2.9999984E7) && !((double) Math.abs(vec1.y) > 2.9999984E7)) {
            $$2.setCenter((double) vec1.x, (double) vec1.y);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.worldborder.center.success", String.format(Locale.ROOT, "%.2f", vec1.x), String.format(Locale.ROOT, "%.2f", vec1.y)), true);
            return 0;
        } else {
            throw ERROR_TOO_FAR_OUT.create();
        }
    }

    private static int setSize(CommandSourceStack commandSourceStack0, double double1, long long2) throws CommandSyntaxException {
        WorldBorder $$3 = commandSourceStack0.getServer().overworld().m_6857_();
        double $$4 = $$3.getSize();
        if ($$4 == double1) {
            throw ERROR_SAME_SIZE.create();
        } else if (double1 < 1.0) {
            throw ERROR_TOO_SMALL.create();
        } else if (double1 > 5.999997E7F) {
            throw ERROR_TOO_BIG.create();
        } else {
            if (long2 > 0L) {
                $$3.lerpSizeBetween($$4, double1, long2);
                if (double1 > $$4) {
                    commandSourceStack0.sendSuccess(() -> Component.translatable("commands.worldborder.set.grow", String.format(Locale.ROOT, "%.1f", double1), Long.toString(long2 / 1000L)), true);
                } else {
                    commandSourceStack0.sendSuccess(() -> Component.translatable("commands.worldborder.set.shrink", String.format(Locale.ROOT, "%.1f", double1), Long.toString(long2 / 1000L)), true);
                }
            } else {
                $$3.setSize(double1);
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.worldborder.set.immediate", String.format(Locale.ROOT, "%.1f", double1)), true);
            }
            return (int) (double1 - $$4);
        }
    }
}