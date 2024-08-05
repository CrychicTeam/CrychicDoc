package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import net.minecraft.commands.arguments.OperationArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.ScoreboardSlotArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class ScoreboardCommand {

    private static final SimpleCommandExceptionType ERROR_OBJECTIVE_ALREADY_EXISTS = new SimpleCommandExceptionType(Component.translatable("commands.scoreboard.objectives.add.duplicate"));

    private static final SimpleCommandExceptionType ERROR_DISPLAY_SLOT_ALREADY_EMPTY = new SimpleCommandExceptionType(Component.translatable("commands.scoreboard.objectives.display.alreadyEmpty"));

    private static final SimpleCommandExceptionType ERROR_DISPLAY_SLOT_ALREADY_SET = new SimpleCommandExceptionType(Component.translatable("commands.scoreboard.objectives.display.alreadySet"));

    private static final SimpleCommandExceptionType ERROR_TRIGGER_ALREADY_ENABLED = new SimpleCommandExceptionType(Component.translatable("commands.scoreboard.players.enable.failed"));

    private static final SimpleCommandExceptionType ERROR_NOT_TRIGGER = new SimpleCommandExceptionType(Component.translatable("commands.scoreboard.players.enable.invalid"));

    private static final Dynamic2CommandExceptionType ERROR_NO_VALUE = new Dynamic2CommandExceptionType((p_138534_, p_138535_) -> Component.translatable("commands.scoreboard.players.get.null", p_138534_, p_138535_));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("scoreboard").requires(p_138552_ -> p_138552_.hasPermission(2))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("objectives").then(Commands.literal("list").executes(p_138585_ -> listObjectives((CommandSourceStack) p_138585_.getSource())))).then(Commands.literal("add").then(Commands.argument("objective", StringArgumentType.word()).then(((RequiredArgumentBuilder) Commands.argument("criteria", ObjectiveCriteriaArgument.criteria()).executes(p_138583_ -> addObjective((CommandSourceStack) p_138583_.getSource(), StringArgumentType.getString(p_138583_, "objective"), ObjectiveCriteriaArgument.getCriteria(p_138583_, "criteria"), Component.literal(StringArgumentType.getString(p_138583_, "objective"))))).then(Commands.argument("displayName", ComponentArgument.textComponent()).executes(p_138581_ -> addObjective((CommandSourceStack) p_138581_.getSource(), StringArgumentType.getString(p_138581_, "objective"), ObjectiveCriteriaArgument.getCriteria(p_138581_, "criteria"), ComponentArgument.getComponent(p_138581_, "displayName")))))))).then(Commands.literal("modify").then(((RequiredArgumentBuilder) Commands.argument("objective", ObjectiveArgument.objective()).then(Commands.literal("displayname").then(Commands.argument("displayName", ComponentArgument.textComponent()).executes(p_138579_ -> setDisplayName((CommandSourceStack) p_138579_.getSource(), ObjectiveArgument.getObjective(p_138579_, "objective"), ComponentArgument.getComponent(p_138579_, "displayName")))))).then(createRenderTypeModify())))).then(Commands.literal("remove").then(Commands.argument("objective", ObjectiveArgument.objective()).executes(p_138577_ -> removeObjective((CommandSourceStack) p_138577_.getSource(), ObjectiveArgument.getObjective(p_138577_, "objective")))))).then(Commands.literal("setdisplay").then(((RequiredArgumentBuilder) Commands.argument("slot", ScoreboardSlotArgument.displaySlot()).executes(p_138575_ -> clearDisplaySlot((CommandSourceStack) p_138575_.getSource(), ScoreboardSlotArgument.getDisplaySlot(p_138575_, "slot")))).then(Commands.argument("objective", ObjectiveArgument.objective()).executes(p_138573_ -> setDisplaySlot((CommandSourceStack) p_138573_.getSource(), ScoreboardSlotArgument.getDisplaySlot(p_138573_, "slot"), ObjectiveArgument.getObjective(p_138573_, "objective")))))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("players").then(((LiteralArgumentBuilder) Commands.literal("list").executes(p_138571_ -> listTrackedPlayers((CommandSourceStack) p_138571_.getSource()))).then(Commands.argument("target", ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).executes(p_138569_ -> listTrackedPlayerScores((CommandSourceStack) p_138569_.getSource(), ScoreHolderArgument.getName(p_138569_, "target")))))).then(Commands.literal("set").then(Commands.argument("targets", ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("objective", ObjectiveArgument.objective()).then(Commands.argument("score", IntegerArgumentType.integer()).executes(p_138567_ -> setScore((CommandSourceStack) p_138567_.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(p_138567_, "targets"), ObjectiveArgument.getWritableObjective(p_138567_, "objective"), IntegerArgumentType.getInteger(p_138567_, "score")))))))).then(Commands.literal("get").then(Commands.argument("target", ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("objective", ObjectiveArgument.objective()).executes(p_138565_ -> getScore((CommandSourceStack) p_138565_.getSource(), ScoreHolderArgument.getName(p_138565_, "target"), ObjectiveArgument.getObjective(p_138565_, "objective"))))))).then(Commands.literal("add").then(Commands.argument("targets", ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("objective", ObjectiveArgument.objective()).then(Commands.argument("score", IntegerArgumentType.integer(0)).executes(p_138563_ -> addScore((CommandSourceStack) p_138563_.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(p_138563_, "targets"), ObjectiveArgument.getWritableObjective(p_138563_, "objective"), IntegerArgumentType.getInteger(p_138563_, "score")))))))).then(Commands.literal("remove").then(Commands.argument("targets", ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("objective", ObjectiveArgument.objective()).then(Commands.argument("score", IntegerArgumentType.integer(0)).executes(p_138561_ -> removeScore((CommandSourceStack) p_138561_.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(p_138561_, "targets"), ObjectiveArgument.getWritableObjective(p_138561_, "objective"), IntegerArgumentType.getInteger(p_138561_, "score")))))))).then(Commands.literal("reset").then(((RequiredArgumentBuilder) Commands.argument("targets", ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).executes(p_138559_ -> resetScores((CommandSourceStack) p_138559_.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(p_138559_, "targets")))).then(Commands.argument("objective", ObjectiveArgument.objective()).executes(p_138550_ -> resetScore((CommandSourceStack) p_138550_.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(p_138550_, "targets"), ObjectiveArgument.getObjective(p_138550_, "objective"))))))).then(Commands.literal("enable").then(Commands.argument("targets", ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("objective", ObjectiveArgument.objective()).suggests((p_138473_, p_138474_) -> suggestTriggers((CommandSourceStack) p_138473_.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(p_138473_, "targets"), p_138474_)).executes(p_138537_ -> enableTrigger((CommandSourceStack) p_138537_.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(p_138537_, "targets"), ObjectiveArgument.getObjective(p_138537_, "objective"))))))).then(Commands.literal("operation").then(Commands.argument("targets", ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("targetObjective", ObjectiveArgument.objective()).then(Commands.argument("operation", OperationArgument.operation()).then(Commands.argument("source", ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("sourceObjective", ObjectiveArgument.objective()).executes(p_138471_ -> performOperation((CommandSourceStack) p_138471_.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(p_138471_, "targets"), ObjectiveArgument.getWritableObjective(p_138471_, "targetObjective"), OperationArgument.getOperation(p_138471_, "operation"), ScoreHolderArgument.getNamesWithDefaultWildcard(p_138471_, "source"), ObjectiveArgument.getObjective(p_138471_, "sourceObjective")))))))))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createRenderTypeModify() {
        LiteralArgumentBuilder<CommandSourceStack> $$0 = Commands.literal("rendertype");
        for (ObjectiveCriteria.RenderType $$1 : ObjectiveCriteria.RenderType.values()) {
            $$0.then(Commands.literal($$1.getId()).executes(p_138532_ -> setRenderType((CommandSourceStack) p_138532_.getSource(), ObjectiveArgument.getObjective(p_138532_, "objective"), $$1)));
        }
        return $$0;
    }

    private static CompletableFuture<Suggestions> suggestTriggers(CommandSourceStack commandSourceStack0, Collection<String> collectionString1, SuggestionsBuilder suggestionsBuilder2) {
        List<String> $$3 = Lists.newArrayList();
        Scoreboard $$4 = commandSourceStack0.getServer().getScoreboard();
        for (Objective $$5 : $$4.getObjectives()) {
            if ($$5.getCriteria() == ObjectiveCriteria.TRIGGER) {
                boolean $$6 = false;
                for (String $$7 : collectionString1) {
                    if (!$$4.hasPlayerScore($$7, $$5) || $$4.getOrCreatePlayerScore($$7, $$5).isLocked()) {
                        $$6 = true;
                        break;
                    }
                }
                if ($$6) {
                    $$3.add($$5.getName());
                }
            }
        }
        return SharedSuggestionProvider.suggest($$3, suggestionsBuilder2);
    }

    private static int getScore(CommandSourceStack commandSourceStack0, String string1, Objective objective2) throws CommandSyntaxException {
        Scoreboard $$3 = commandSourceStack0.getServer().getScoreboard();
        if (!$$3.hasPlayerScore(string1, objective2)) {
            throw ERROR_NO_VALUE.create(objective2.getName(), string1);
        } else {
            Score $$4 = $$3.getOrCreatePlayerScore(string1, objective2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.get.success", string1, $$4.getScore(), objective2.getFormattedDisplayName()), false);
            return $$4.getScore();
        }
    }

    private static int performOperation(CommandSourceStack commandSourceStack0, Collection<String> collectionString1, Objective objective2, OperationArgument.Operation operationArgumentOperation3, Collection<String> collectionString4, Objective objective5) throws CommandSyntaxException {
        Scoreboard $$6 = commandSourceStack0.getServer().getScoreboard();
        int $$7 = 0;
        for (String $$8 : collectionString1) {
            Score $$9 = $$6.getOrCreatePlayerScore($$8, objective2);
            for (String $$10 : collectionString4) {
                Score $$11 = $$6.getOrCreatePlayerScore($$10, objective5);
                operationArgumentOperation3.apply($$9, $$11);
            }
            $$7 += $$9.getScore();
        }
        if (collectionString1.size() == 1) {
            int $$12 = $$7;
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.operation.success.single", objective2.getFormattedDisplayName(), collectionString1.iterator().next(), $$12), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.operation.success.multiple", objective2.getFormattedDisplayName(), collectionString1.size()), true);
        }
        return $$7;
    }

    private static int enableTrigger(CommandSourceStack commandSourceStack0, Collection<String> collectionString1, Objective objective2) throws CommandSyntaxException {
        if (objective2.getCriteria() != ObjectiveCriteria.TRIGGER) {
            throw ERROR_NOT_TRIGGER.create();
        } else {
            Scoreboard $$3 = commandSourceStack0.getServer().getScoreboard();
            int $$4 = 0;
            for (String $$5 : collectionString1) {
                Score $$6 = $$3.getOrCreatePlayerScore($$5, objective2);
                if ($$6.isLocked()) {
                    $$6.setLocked(false);
                    $$4++;
                }
            }
            if ($$4 == 0) {
                throw ERROR_TRIGGER_ALREADY_ENABLED.create();
            } else {
                if (collectionString1.size() == 1) {
                    commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.enable.success.single", objective2.getFormattedDisplayName(), collectionString1.iterator().next()), true);
                } else {
                    commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.enable.success.multiple", objective2.getFormattedDisplayName(), collectionString1.size()), true);
                }
                return $$4;
            }
        }
    }

    private static int resetScores(CommandSourceStack commandSourceStack0, Collection<String> collectionString1) {
        Scoreboard $$2 = commandSourceStack0.getServer().getScoreboard();
        for (String $$3 : collectionString1) {
            $$2.resetPlayerScore($$3, null);
        }
        if (collectionString1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.all.single", collectionString1.iterator().next()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.all.multiple", collectionString1.size()), true);
        }
        return collectionString1.size();
    }

    private static int resetScore(CommandSourceStack commandSourceStack0, Collection<String> collectionString1, Objective objective2) {
        Scoreboard $$3 = commandSourceStack0.getServer().getScoreboard();
        for (String $$4 : collectionString1) {
            $$3.resetPlayerScore($$4, objective2);
        }
        if (collectionString1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.specific.single", objective2.getFormattedDisplayName(), collectionString1.iterator().next()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.specific.multiple", objective2.getFormattedDisplayName(), collectionString1.size()), true);
        }
        return collectionString1.size();
    }

    private static int setScore(CommandSourceStack commandSourceStack0, Collection<String> collectionString1, Objective objective2, int int3) {
        Scoreboard $$4 = commandSourceStack0.getServer().getScoreboard();
        for (String $$5 : collectionString1) {
            Score $$6 = $$4.getOrCreatePlayerScore($$5, objective2);
            $$6.setScore(int3);
        }
        if (collectionString1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.set.success.single", objective2.getFormattedDisplayName(), collectionString1.iterator().next(), int3), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.set.success.multiple", objective2.getFormattedDisplayName(), collectionString1.size(), int3), true);
        }
        return int3 * collectionString1.size();
    }

    private static int addScore(CommandSourceStack commandSourceStack0, Collection<String> collectionString1, Objective objective2, int int3) {
        Scoreboard $$4 = commandSourceStack0.getServer().getScoreboard();
        int $$5 = 0;
        for (String $$6 : collectionString1) {
            Score $$7 = $$4.getOrCreatePlayerScore($$6, objective2);
            $$7.setScore($$7.getScore() + int3);
            $$5 += $$7.getScore();
        }
        if (collectionString1.size() == 1) {
            int $$8 = $$5;
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.add.success.single", int3, objective2.getFormattedDisplayName(), collectionString1.iterator().next(), $$8), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.add.success.multiple", int3, objective2.getFormattedDisplayName(), collectionString1.size()), true);
        }
        return $$5;
    }

    private static int removeScore(CommandSourceStack commandSourceStack0, Collection<String> collectionString1, Objective objective2, int int3) {
        Scoreboard $$4 = commandSourceStack0.getServer().getScoreboard();
        int $$5 = 0;
        for (String $$6 : collectionString1) {
            Score $$7 = $$4.getOrCreatePlayerScore($$6, objective2);
            $$7.setScore($$7.getScore() - int3);
            $$5 += $$7.getScore();
        }
        if (collectionString1.size() == 1) {
            int $$8 = $$5;
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.remove.success.single", int3, objective2.getFormattedDisplayName(), collectionString1.iterator().next(), $$8), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.remove.success.multiple", int3, objective2.getFormattedDisplayName(), collectionString1.size()), true);
        }
        return $$5;
    }

    private static int listTrackedPlayers(CommandSourceStack commandSourceStack0) {
        Collection<String> $$1 = commandSourceStack0.getServer().getScoreboard().m_83482_();
        if ($$1.isEmpty()) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.empty"), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.success", $$1.size(), ComponentUtils.formatList($$1)), false);
        }
        return $$1.size();
    }

    private static int listTrackedPlayerScores(CommandSourceStack commandSourceStack0, String string1) {
        Map<Objective, Score> $$2 = commandSourceStack0.getServer().getScoreboard().m_83483_(string1);
        if ($$2.isEmpty()) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.entity.empty", string1), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.entity.success", string1, $$2.size()), false);
            for (Entry<Objective, Score> $$3 : $$2.entrySet()) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.entity.entry", ((Objective) $$3.getKey()).getFormattedDisplayName(), ((Score) $$3.getValue()).getScore()), false);
            }
        }
        return $$2.size();
    }

    private static int clearDisplaySlot(CommandSourceStack commandSourceStack0, int int1) throws CommandSyntaxException {
        Scoreboard $$2 = commandSourceStack0.getServer().getScoreboard();
        if ($$2.getDisplayObjective(int1) == null) {
            throw ERROR_DISPLAY_SLOT_ALREADY_EMPTY.create();
        } else {
            $$2.setDisplayObjective(int1, null);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.display.cleared", Scoreboard.getDisplaySlotNames()[int1]), true);
            return 0;
        }
    }

    private static int setDisplaySlot(CommandSourceStack commandSourceStack0, int int1, Objective objective2) throws CommandSyntaxException {
        Scoreboard $$3 = commandSourceStack0.getServer().getScoreboard();
        if ($$3.getDisplayObjective(int1) == objective2) {
            throw ERROR_DISPLAY_SLOT_ALREADY_SET.create();
        } else {
            $$3.setDisplayObjective(int1, objective2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.display.set", Scoreboard.getDisplaySlotNames()[int1], objective2.getDisplayName()), true);
            return 0;
        }
    }

    private static int setDisplayName(CommandSourceStack commandSourceStack0, Objective objective1, Component component2) {
        if (!objective1.getDisplayName().equals(component2)) {
            objective1.setDisplayName(component2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.displayname", objective1.getName(), objective1.getFormattedDisplayName()), true);
        }
        return 0;
    }

    private static int setRenderType(CommandSourceStack commandSourceStack0, Objective objective1, ObjectiveCriteria.RenderType objectiveCriteriaRenderType2) {
        if (objective1.getRenderType() != objectiveCriteriaRenderType2) {
            objective1.setRenderType(objectiveCriteriaRenderType2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.rendertype", objective1.getFormattedDisplayName()), true);
        }
        return 0;
    }

    private static int removeObjective(CommandSourceStack commandSourceStack0, Objective objective1) {
        Scoreboard $$2 = commandSourceStack0.getServer().getScoreboard();
        $$2.removeObjective(objective1);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.remove.success", objective1.getFormattedDisplayName()), true);
        return $$2.getObjectives().size();
    }

    private static int addObjective(CommandSourceStack commandSourceStack0, String string1, ObjectiveCriteria objectiveCriteria2, Component component3) throws CommandSyntaxException {
        Scoreboard $$4 = commandSourceStack0.getServer().getScoreboard();
        if ($$4.getObjective(string1) != null) {
            throw ERROR_OBJECTIVE_ALREADY_EXISTS.create();
        } else {
            $$4.addObjective(string1, objectiveCriteria2, component3, objectiveCriteria2.getDefaultRenderType());
            Objective $$5 = $$4.getObjective(string1);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.add.success", $$5.getFormattedDisplayName()), true);
            return $$4.getObjectives().size();
        }
    }

    private static int listObjectives(CommandSourceStack commandSourceStack0) {
        Collection<Objective> $$1 = commandSourceStack0.getServer().getScoreboard().m_83466_();
        if ($$1.isEmpty()) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.list.empty"), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.list.success", $$1.size(), ComponentUtils.formatList($$1, Objective::m_83323_)), false);
        }
        return $$1.size();
    }
}