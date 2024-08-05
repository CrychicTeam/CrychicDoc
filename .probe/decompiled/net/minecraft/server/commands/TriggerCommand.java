package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class TriggerCommand {

    private static final SimpleCommandExceptionType ERROR_NOT_PRIMED = new SimpleCommandExceptionType(Component.translatable("commands.trigger.failed.unprimed"));

    private static final SimpleCommandExceptionType ERROR_INVALID_OBJECTIVE = new SimpleCommandExceptionType(Component.translatable("commands.trigger.failed.invalid"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) Commands.literal("trigger").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("objective", ObjectiveArgument.objective()).suggests((p_139146_, p_139147_) -> suggestObjectives((CommandSourceStack) p_139146_.getSource(), p_139147_)).executes(p_139165_ -> simpleTrigger((CommandSourceStack) p_139165_.getSource(), getScore(((CommandSourceStack) p_139165_.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(p_139165_, "objective"))))).then(Commands.literal("add").then(Commands.argument("value", IntegerArgumentType.integer()).executes(p_139159_ -> addValue((CommandSourceStack) p_139159_.getSource(), getScore(((CommandSourceStack) p_139159_.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(p_139159_, "objective")), IntegerArgumentType.getInteger(p_139159_, "value")))))).then(Commands.literal("set").then(Commands.argument("value", IntegerArgumentType.integer()).executes(p_139144_ -> setValue((CommandSourceStack) p_139144_.getSource(), getScore(((CommandSourceStack) p_139144_.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(p_139144_, "objective")), IntegerArgumentType.getInteger(p_139144_, "value")))))));
    }

    public static CompletableFuture<Suggestions> suggestObjectives(CommandSourceStack commandSourceStack0, SuggestionsBuilder suggestionsBuilder1) {
        Entity $$2 = commandSourceStack0.getEntity();
        List<String> $$3 = Lists.newArrayList();
        if ($$2 != null) {
            Scoreboard $$4 = commandSourceStack0.getServer().getScoreboard();
            String $$5 = $$2.getScoreboardName();
            for (Objective $$6 : $$4.getObjectives()) {
                if ($$6.getCriteria() == ObjectiveCriteria.TRIGGER && $$4.hasPlayerScore($$5, $$6)) {
                    Score $$7 = $$4.getOrCreatePlayerScore($$5, $$6);
                    if (!$$7.isLocked()) {
                        $$3.add($$6.getName());
                    }
                }
            }
        }
        return SharedSuggestionProvider.suggest($$3, suggestionsBuilder1);
    }

    private static int addValue(CommandSourceStack commandSourceStack0, Score score1, int int2) {
        score1.add(int2);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.trigger.add.success", score1.getObjective().getFormattedDisplayName(), int2), true);
        return score1.getScore();
    }

    private static int setValue(CommandSourceStack commandSourceStack0, Score score1, int int2) {
        score1.setScore(int2);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.trigger.set.success", score1.getObjective().getFormattedDisplayName(), int2), true);
        return int2;
    }

    private static int simpleTrigger(CommandSourceStack commandSourceStack0, Score score1) {
        score1.add(1);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.trigger.simple.success", score1.getObjective().getFormattedDisplayName()), true);
        return score1.getScore();
    }

    private static Score getScore(ServerPlayer serverPlayer0, Objective objective1) throws CommandSyntaxException {
        if (objective1.getCriteria() != ObjectiveCriteria.TRIGGER) {
            throw ERROR_INVALID_OBJECTIVE.create();
        } else {
            Scoreboard $$2 = serverPlayer0.m_36329_();
            String $$3 = serverPlayer0.m_6302_();
            if (!$$2.hasPlayerScore($$3, objective1)) {
                throw ERROR_NOT_PRIMED.create();
            } else {
                Score $$4 = $$2.getOrCreatePlayerScore($$3, objective1);
                if ($$4.isLocked()) {
                    throw ERROR_NOT_PRIMED.create();
                } else {
                    $$4.setLocked(true);
                    return $$4;
                }
            }
        }
    }
}