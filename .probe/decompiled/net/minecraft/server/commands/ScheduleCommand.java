package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.timers.FunctionCallback;
import net.minecraft.world.level.timers.FunctionTagCallback;
import net.minecraft.world.level.timers.TimerQueue;

public class ScheduleCommand {

    private static final SimpleCommandExceptionType ERROR_SAME_TICK = new SimpleCommandExceptionType(Component.translatable("commands.schedule.same_tick"));

    private static final DynamicCommandExceptionType ERROR_CANT_REMOVE = new DynamicCommandExceptionType(p_138437_ -> Component.translatable("commands.schedule.cleared.failure", p_138437_));

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_SCHEDULE = (p_138424_, p_138425_) -> SharedSuggestionProvider.suggest(((CommandSourceStack) p_138424_.getSource()).getServer().getWorldData().overworldData().getScheduledEvents().getEventsIds(), p_138425_);

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("schedule").requires(p_138427_ -> p_138427_.hasPermission(2))).then(Commands.literal("function").then(Commands.argument("function", FunctionArgument.functions()).suggests(FunctionCommand.SUGGEST_FUNCTION).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("time", TimeArgument.time()).executes(p_138459_ -> schedule((CommandSourceStack) p_138459_.getSource(), FunctionArgument.getFunctionOrTag(p_138459_, "function"), IntegerArgumentType.getInteger(p_138459_, "time"), true))).then(Commands.literal("append").executes(p_138457_ -> schedule((CommandSourceStack) p_138457_.getSource(), FunctionArgument.getFunctionOrTag(p_138457_, "function"), IntegerArgumentType.getInteger(p_138457_, "time"), false)))).then(Commands.literal("replace").executes(p_138455_ -> schedule((CommandSourceStack) p_138455_.getSource(), FunctionArgument.getFunctionOrTag(p_138455_, "function"), IntegerArgumentType.getInteger(p_138455_, "time"), true))))))).then(Commands.literal("clear").then(Commands.argument("function", StringArgumentType.greedyString()).suggests(SUGGEST_SCHEDULE).executes(p_138422_ -> remove((CommandSourceStack) p_138422_.getSource(), StringArgumentType.getString(p_138422_, "function"))))));
    }

    private static int schedule(CommandSourceStack commandSourceStack0, Pair<ResourceLocation, Either<CommandFunction, Collection<CommandFunction>>> pairResourceLocationEitherCommandFunctionCollectionCommandFunction1, int int2, boolean boolean3) throws CommandSyntaxException {
        if (int2 == 0) {
            throw ERROR_SAME_TICK.create();
        } else {
            long $$4 = commandSourceStack0.getLevel().m_46467_() + (long) int2;
            ResourceLocation $$5 = (ResourceLocation) pairResourceLocationEitherCommandFunctionCollectionCommandFunction1.getFirst();
            TimerQueue<MinecraftServer> $$6 = commandSourceStack0.getServer().getWorldData().overworldData().getScheduledEvents();
            ((Either) pairResourceLocationEitherCommandFunctionCollectionCommandFunction1.getSecond()).ifLeft(p_288541_ -> {
                String $$7 = $$5.toString();
                if (boolean3) {
                    $$6.remove($$7);
                }
                $$6.schedule($$7, $$4, new FunctionCallback($$5));
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.schedule.created.function", $$5, int2, $$4), true);
            }).ifRight(p_288548_ -> {
                String $$7 = "#" + $$5;
                if (boolean3) {
                    $$6.remove($$7);
                }
                $$6.schedule($$7, $$4, new FunctionTagCallback($$5));
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.schedule.created.tag", $$5, int2, $$4), true);
            });
            return Math.floorMod($$4, Integer.MAX_VALUE);
        }
    }

    private static int remove(CommandSourceStack commandSourceStack0, String string1) throws CommandSyntaxException {
        int $$2 = commandSourceStack0.getServer().getWorldData().overworldData().getScheduledEvents().remove(string1);
        if ($$2 == 0) {
            throw ERROR_CANT_REMOVE.create(string1);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.schedule.cleared.success", $$2, string1), true);
            return $$2;
        }
    }
}