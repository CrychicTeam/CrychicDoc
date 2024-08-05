package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;

public class TeamCommand {

    private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EXISTS = new SimpleCommandExceptionType(Component.translatable("commands.team.add.duplicate"));

    private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EMPTY = new SimpleCommandExceptionType(Component.translatable("commands.team.empty.unchanged"));

    private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_NAME = new SimpleCommandExceptionType(Component.translatable("commands.team.option.name.unchanged"));

    private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_COLOR = new SimpleCommandExceptionType(Component.translatable("commands.team.option.color.unchanged"));

    private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYFIRE_ENABLED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.friendlyfire.alreadyEnabled"));

    private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYFIRE_DISABLED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.friendlyfire.alreadyDisabled"));

    private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_ENABLED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.seeFriendlyInvisibles.alreadyEnabled"));

    private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_DISABLED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.seeFriendlyInvisibles.alreadyDisabled"));

    private static final SimpleCommandExceptionType ERROR_TEAM_NAMETAG_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.nametagVisibility.unchanged"));

    private static final SimpleCommandExceptionType ERROR_TEAM_DEATH_MESSAGE_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.deathMessageVisibility.unchanged"));

    private static final SimpleCommandExceptionType ERROR_TEAM_COLLISION_UNCHANGED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.collisionRule.unchanged"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("team").requires(p_183713_ -> p_183713_.hasPermission(2))).then(((LiteralArgumentBuilder) Commands.literal("list").executes(p_183711_ -> listTeams((CommandSourceStack) p_183711_.getSource()))).then(Commands.argument("team", TeamArgument.team()).executes(p_138876_ -> listMembers((CommandSourceStack) p_138876_.getSource(), TeamArgument.getTeam(p_138876_, "team")))))).then(Commands.literal("add").then(((RequiredArgumentBuilder) Commands.argument("team", StringArgumentType.word()).executes(p_138995_ -> createTeam((CommandSourceStack) p_138995_.getSource(), StringArgumentType.getString(p_138995_, "team")))).then(Commands.argument("displayName", ComponentArgument.textComponent()).executes(p_138993_ -> createTeam((CommandSourceStack) p_138993_.getSource(), StringArgumentType.getString(p_138993_, "team"), ComponentArgument.getComponent(p_138993_, "displayName"))))))).then(Commands.literal("remove").then(Commands.argument("team", TeamArgument.team()).executes(p_138991_ -> deleteTeam((CommandSourceStack) p_138991_.getSource(), TeamArgument.getTeam(p_138991_, "team")))))).then(Commands.literal("empty").then(Commands.argument("team", TeamArgument.team()).executes(p_138989_ -> emptyTeam((CommandSourceStack) p_138989_.getSource(), TeamArgument.getTeam(p_138989_, "team")))))).then(Commands.literal("join").then(((RequiredArgumentBuilder) Commands.argument("team", TeamArgument.team()).executes(p_138987_ -> joinTeam((CommandSourceStack) p_138987_.getSource(), TeamArgument.getTeam(p_138987_, "team"), Collections.singleton(((CommandSourceStack) p_138987_.getSource()).getEntityOrException().getScoreboardName())))).then(Commands.argument("members", ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).executes(p_138985_ -> joinTeam((CommandSourceStack) p_138985_.getSource(), TeamArgument.getTeam(p_138985_, "team"), ScoreHolderArgument.getNamesWithDefaultWildcard(p_138985_, "members"))))))).then(Commands.literal("leave").then(Commands.argument("members", ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).executes(p_138983_ -> leaveTeam((CommandSourceStack) p_138983_.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(p_138983_, "members")))))).then(Commands.literal("modify").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("team", TeamArgument.team()).then(Commands.literal("displayName").then(Commands.argument("displayName", ComponentArgument.textComponent()).executes(p_138981_ -> setDisplayName((CommandSourceStack) p_138981_.getSource(), TeamArgument.getTeam(p_138981_, "team"), ComponentArgument.getComponent(p_138981_, "displayName")))))).then(Commands.literal("color").then(Commands.argument("value", ColorArgument.color()).executes(p_138979_ -> setColor((CommandSourceStack) p_138979_.getSource(), TeamArgument.getTeam(p_138979_, "team"), ColorArgument.getColor(p_138979_, "value")))))).then(Commands.literal("friendlyFire").then(Commands.argument("allowed", BoolArgumentType.bool()).executes(p_138977_ -> setFriendlyFire((CommandSourceStack) p_138977_.getSource(), TeamArgument.getTeam(p_138977_, "team"), BoolArgumentType.getBool(p_138977_, "allowed")))))).then(Commands.literal("seeFriendlyInvisibles").then(Commands.argument("allowed", BoolArgumentType.bool()).executes(p_138975_ -> setFriendlySight((CommandSourceStack) p_138975_.getSource(), TeamArgument.getTeam(p_138975_, "team"), BoolArgumentType.getBool(p_138975_, "allowed")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("nametagVisibility").then(Commands.literal("never").executes(p_138973_ -> setNametagVisibility((CommandSourceStack) p_138973_.getSource(), TeamArgument.getTeam(p_138973_, "team"), Team.Visibility.NEVER)))).then(Commands.literal("hideForOtherTeams").executes(p_138971_ -> setNametagVisibility((CommandSourceStack) p_138971_.getSource(), TeamArgument.getTeam(p_138971_, "team"), Team.Visibility.HIDE_FOR_OTHER_TEAMS)))).then(Commands.literal("hideForOwnTeam").executes(p_138969_ -> setNametagVisibility((CommandSourceStack) p_138969_.getSource(), TeamArgument.getTeam(p_138969_, "team"), Team.Visibility.HIDE_FOR_OWN_TEAM)))).then(Commands.literal("always").executes(p_138967_ -> setNametagVisibility((CommandSourceStack) p_138967_.getSource(), TeamArgument.getTeam(p_138967_, "team"), Team.Visibility.ALWAYS))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("deathMessageVisibility").then(Commands.literal("never").executes(p_138965_ -> setDeathMessageVisibility((CommandSourceStack) p_138965_.getSource(), TeamArgument.getTeam(p_138965_, "team"), Team.Visibility.NEVER)))).then(Commands.literal("hideForOtherTeams").executes(p_138963_ -> setDeathMessageVisibility((CommandSourceStack) p_138963_.getSource(), TeamArgument.getTeam(p_138963_, "team"), Team.Visibility.HIDE_FOR_OTHER_TEAMS)))).then(Commands.literal("hideForOwnTeam").executes(p_138961_ -> setDeathMessageVisibility((CommandSourceStack) p_138961_.getSource(), TeamArgument.getTeam(p_138961_, "team"), Team.Visibility.HIDE_FOR_OWN_TEAM)))).then(Commands.literal("always").executes(p_138959_ -> setDeathMessageVisibility((CommandSourceStack) p_138959_.getSource(), TeamArgument.getTeam(p_138959_, "team"), Team.Visibility.ALWAYS))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("collisionRule").then(Commands.literal("never").executes(p_138957_ -> setCollision((CommandSourceStack) p_138957_.getSource(), TeamArgument.getTeam(p_138957_, "team"), Team.CollisionRule.NEVER)))).then(Commands.literal("pushOwnTeam").executes(p_138955_ -> setCollision((CommandSourceStack) p_138955_.getSource(), TeamArgument.getTeam(p_138955_, "team"), Team.CollisionRule.PUSH_OWN_TEAM)))).then(Commands.literal("pushOtherTeams").executes(p_138953_ -> setCollision((CommandSourceStack) p_138953_.getSource(), TeamArgument.getTeam(p_138953_, "team"), Team.CollisionRule.PUSH_OTHER_TEAMS)))).then(Commands.literal("always").executes(p_138951_ -> setCollision((CommandSourceStack) p_138951_.getSource(), TeamArgument.getTeam(p_138951_, "team"), Team.CollisionRule.ALWAYS))))).then(Commands.literal("prefix").then(Commands.argument("prefix", ComponentArgument.textComponent()).executes(p_138942_ -> setPrefix((CommandSourceStack) p_138942_.getSource(), TeamArgument.getTeam(p_138942_, "team"), ComponentArgument.getComponent(p_138942_, "prefix")))))).then(Commands.literal("suffix").then(Commands.argument("suffix", ComponentArgument.textComponent()).executes(p_138923_ -> setSuffix((CommandSourceStack) p_138923_.getSource(), TeamArgument.getTeam(p_138923_, "team"), ComponentArgument.getComponent(p_138923_, "suffix"))))))));
    }

    private static int leaveTeam(CommandSourceStack commandSourceStack0, Collection<String> collectionString1) {
        Scoreboard $$2 = commandSourceStack0.getServer().getScoreboard();
        for (String $$3 : collectionString1) {
            $$2.removePlayerFromTeam($$3);
        }
        if (collectionString1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.leave.success.single", collectionString1.iterator().next()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.leave.success.multiple", collectionString1.size()), true);
        }
        return collectionString1.size();
    }

    private static int joinTeam(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1, Collection<String> collectionString2) {
        Scoreboard $$3 = commandSourceStack0.getServer().getScoreboard();
        for (String $$4 : collectionString2) {
            $$3.addPlayerToTeam($$4, playerTeam1);
        }
        if (collectionString2.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.join.success.single", collectionString2.iterator().next(), playerTeam1.getFormattedDisplayName()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.join.success.multiple", collectionString2.size(), playerTeam1.getFormattedDisplayName()), true);
        }
        return collectionString2.size();
    }

    private static int setNametagVisibility(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1, Team.Visibility teamVisibility2) throws CommandSyntaxException {
        if (playerTeam1.getNameTagVisibility() == teamVisibility2) {
            throw ERROR_TEAM_NAMETAG_VISIBLITY_UNCHANGED.create();
        } else {
            playerTeam1.setNameTagVisibility(teamVisibility2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.option.nametagVisibility.success", playerTeam1.getFormattedDisplayName(), teamVisibility2.getDisplayName()), true);
            return 0;
        }
    }

    private static int setDeathMessageVisibility(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1, Team.Visibility teamVisibility2) throws CommandSyntaxException {
        if (playerTeam1.getDeathMessageVisibility() == teamVisibility2) {
            throw ERROR_TEAM_DEATH_MESSAGE_VISIBLITY_UNCHANGED.create();
        } else {
            playerTeam1.setDeathMessageVisibility(teamVisibility2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.option.deathMessageVisibility.success", playerTeam1.getFormattedDisplayName(), teamVisibility2.getDisplayName()), true);
            return 0;
        }
    }

    private static int setCollision(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1, Team.CollisionRule teamCollisionRule2) throws CommandSyntaxException {
        if (playerTeam1.getCollisionRule() == teamCollisionRule2) {
            throw ERROR_TEAM_COLLISION_UNCHANGED.create();
        } else {
            playerTeam1.setCollisionRule(teamCollisionRule2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.option.collisionRule.success", playerTeam1.getFormattedDisplayName(), teamCollisionRule2.getDisplayName()), true);
            return 0;
        }
    }

    private static int setFriendlySight(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1, boolean boolean2) throws CommandSyntaxException {
        if (playerTeam1.canSeeFriendlyInvisibles() == boolean2) {
            if (boolean2) {
                throw ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_ENABLED.create();
            } else {
                throw ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_DISABLED.create();
            }
        } else {
            playerTeam1.setSeeFriendlyInvisibles(boolean2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.option.seeFriendlyInvisibles." + (boolean2 ? "enabled" : "disabled"), playerTeam1.getFormattedDisplayName()), true);
            return 0;
        }
    }

    private static int setFriendlyFire(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1, boolean boolean2) throws CommandSyntaxException {
        if (playerTeam1.isAllowFriendlyFire() == boolean2) {
            if (boolean2) {
                throw ERROR_TEAM_ALREADY_FRIENDLYFIRE_ENABLED.create();
            } else {
                throw ERROR_TEAM_ALREADY_FRIENDLYFIRE_DISABLED.create();
            }
        } else {
            playerTeam1.setAllowFriendlyFire(boolean2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.option.friendlyfire." + (boolean2 ? "enabled" : "disabled"), playerTeam1.getFormattedDisplayName()), true);
            return 0;
        }
    }

    private static int setDisplayName(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1, Component component2) throws CommandSyntaxException {
        if (playerTeam1.getDisplayName().equals(component2)) {
            throw ERROR_TEAM_ALREADY_NAME.create();
        } else {
            playerTeam1.setDisplayName(component2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.option.name.success", playerTeam1.getFormattedDisplayName()), true);
            return 0;
        }
    }

    private static int setColor(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1, ChatFormatting chatFormatting2) throws CommandSyntaxException {
        if (playerTeam1.getColor() == chatFormatting2) {
            throw ERROR_TEAM_ALREADY_COLOR.create();
        } else {
            playerTeam1.setColor(chatFormatting2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.option.color.success", playerTeam1.getFormattedDisplayName(), chatFormatting2.getName()), true);
            return 0;
        }
    }

    private static int emptyTeam(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1) throws CommandSyntaxException {
        Scoreboard $$2 = commandSourceStack0.getServer().getScoreboard();
        Collection<String> $$3 = Lists.newArrayList(playerTeam1.getPlayers());
        if ($$3.isEmpty()) {
            throw ERROR_TEAM_ALREADY_EMPTY.create();
        } else {
            for (String $$4 : $$3) {
                $$2.removePlayerFromTeam($$4, playerTeam1);
            }
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.empty.success", $$3.size(), playerTeam1.getFormattedDisplayName()), true);
            return $$3.size();
        }
    }

    private static int deleteTeam(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1) {
        Scoreboard $$2 = commandSourceStack0.getServer().getScoreboard();
        $$2.removePlayerTeam(playerTeam1);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.remove.success", playerTeam1.getFormattedDisplayName()), true);
        return $$2.getPlayerTeams().size();
    }

    private static int createTeam(CommandSourceStack commandSourceStack0, String string1) throws CommandSyntaxException {
        return createTeam(commandSourceStack0, string1, Component.literal(string1));
    }

    private static int createTeam(CommandSourceStack commandSourceStack0, String string1, Component component2) throws CommandSyntaxException {
        Scoreboard $$3 = commandSourceStack0.getServer().getScoreboard();
        if ($$3.getPlayerTeam(string1) != null) {
            throw ERROR_TEAM_ALREADY_EXISTS.create();
        } else {
            PlayerTeam $$4 = $$3.addPlayerTeam(string1);
            $$4.setDisplayName(component2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.add.success", $$4.getFormattedDisplayName()), true);
            return $$3.getPlayerTeams().size();
        }
    }

    private static int listMembers(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1) {
        Collection<String> $$2 = playerTeam1.getPlayers();
        if ($$2.isEmpty()) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.list.members.empty", playerTeam1.getFormattedDisplayName()), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.list.members.success", playerTeam1.getFormattedDisplayName(), $$2.size(), ComponentUtils.formatList($$2)), false);
        }
        return $$2.size();
    }

    private static int listTeams(CommandSourceStack commandSourceStack0) {
        Collection<PlayerTeam> $$1 = commandSourceStack0.getServer().getScoreboard().m_83491_();
        if ($$1.isEmpty()) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.list.teams.empty"), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.list.teams.success", $$1.size(), ComponentUtils.formatList($$1, PlayerTeam::m_83367_)), false);
        }
        return $$1.size();
    }

    private static int setPrefix(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1, Component component2) {
        playerTeam1.setPlayerPrefix(component2);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.option.prefix.success", component2), false);
        return 1;
    }

    private static int setSuffix(CommandSourceStack commandSourceStack0, PlayerTeam playerTeam1, Component component2) {
        playerTeam1.setPlayerSuffix(component2);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.team.option.suffix.success", component2), false);
        return 1;
    }
}