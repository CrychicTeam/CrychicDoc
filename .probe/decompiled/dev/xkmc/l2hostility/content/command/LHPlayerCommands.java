package dev.xkmc.l2hostility.content.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.init.data.LangData;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class LHPlayerCommands extends HostilityCommands {

    protected static LiteralArgumentBuilder<CommandSourceStack> build() {
        return (LiteralArgumentBuilder<CommandSourceStack>) literal("player").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) argument("player", EntityArgument.players()).then(difficulty())).then(trait())).then(dim()));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> difficulty() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) literal("difficulty").then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) literal("base").then(((LiteralArgumentBuilder) literal("set").requires(e -> e.hasPermission(2))).then(argument("level", IntegerArgumentType.integer(0)).executes(playerLevel((player, level) -> player.getLevelEditor().setBase(level)))))).then(((LiteralArgumentBuilder) literal("add").requires(e -> e.hasPermission(2))).then(argument("level", IntegerArgumentType.integer()).executes(playerLevel((player, level) -> player.getLevelEditor().addBase(level)))))).then(literal("get").executes(playerGet(player -> LangData.COMMAND_PLAYER_GET_BASE.get(player.player.getDisplayName(), player.getLevelEditor().getBase())))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) literal("total").then(((LiteralArgumentBuilder) literal("set").requires(e -> e.hasPermission(2))).then(argument("level", IntegerArgumentType.integer(0)).executes(playerLevel((player, level) -> player.getLevelEditor().setTotal(level)))))).then(((LiteralArgumentBuilder) literal("add").requires(e -> e.hasPermission(2))).then(argument("level", IntegerArgumentType.integer()).executes(playerLevel((player, level) -> player.getLevelEditor().addTotal(level)))))).then(literal("get").executes(playerGet(player -> LangData.COMMAND_PLAYER_GET_TOTAL.get(player.player.getDisplayName(), player.getLevelEditor().getBase())))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> trait() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) literal("traitCap").then(((LiteralArgumentBuilder) literal("set").requires(e -> e.hasPermission(2))).then(argument("level", IntegerArgumentType.integer(0, TraitManager.getMaxLevel())).executes(playerLevel((player, level) -> {
            player.maxRankKilled = level;
            return true;
        }))))).then(literal("get").executes(playerGet(player -> LangData.COMMAND_PLAYER_GET_TRAIT_CAP.get(player.player.getDisplayName(), player.maxRankKilled))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> dim() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) literal("dimensions").then(((LiteralArgumentBuilder) literal("clear").requires(e -> e.hasPermission(2))).executes(playerRun(player -> {
            boolean ans = !player.dimensions.isEmpty();
            player.dimensions.clear();
            return ans;
        })))).then(literal("get").executes(playerGet(player -> LangData.COMMAND_PLAYER_GET_DIM.get(player.player.getDisplayName(), player.dimensions.size()))));
    }

    private static Command<CommandSourceStack> playerRun(LHPlayerCommands.PlayerCommand cmd) {
        return ctx -> {
            EntitySelector sel = (EntitySelector) ctx.getArgument("player", EntitySelector.class);
            List<ServerPlayer> list = sel.findPlayers((CommandSourceStack) ctx.getSource());
            int count = iterate(list, cmd::run);
            printCompletion((CommandSourceStack) ctx.getSource(), count);
            return 0;
        };
    }

    private static Command<CommandSourceStack> playerGet(LHPlayerCommands.PlayerGet cmd) {
        return ctx -> {
            EntitySelector sel = (EntitySelector) ctx.getArgument("player", EntitySelector.class);
            for (ServerPlayer e : sel.findPlayers((CommandSourceStack) ctx.getSource())) {
                ((CommandSourceStack) ctx.getSource()).sendSystemMessage(cmd.run(PlayerDifficulty.HOLDER.get(e)));
            }
            return 0;
        };
    }

    private static Command<CommandSourceStack> playerLevel(LHPlayerCommands.PlayerLevelCommand cmd) {
        return ctx -> {
            int level = (Integer) ctx.getArgument("level", Integer.class);
            EntitySelector sel = (EntitySelector) ctx.getArgument("player", EntitySelector.class);
            List<ServerPlayer> list = sel.findPlayers((CommandSourceStack) ctx.getSource());
            int count = iterate(list, cap -> cmd.run(cap, level));
            printCompletion((CommandSourceStack) ctx.getSource(), count);
            return 0;
        };
    }

    private static int iterate(List<ServerPlayer> list, Predicate<PlayerDifficulty> task) {
        int count = 0;
        for (ServerPlayer e : list) {
            PlayerDifficulty cap = PlayerDifficulty.HOLDER.get(e);
            if (task.test(cap)) {
                cap.sync();
                count++;
            }
        }
        return count;
    }

    private static void printCompletion(CommandSourceStack ctx, int count) {
        if (count > 0) {
            ctx.sendSystemMessage(LangData.COMMAND_PLAYER_SUCCEED.get(count));
        } else {
            ctx.sendSystemMessage(LangData.COMMAND_PLAYER_FAIL.get().withStyle(ChatFormatting.RED));
        }
    }

    private interface PlayerCommand {

        boolean run(PlayerDifficulty var1);
    }

    private interface PlayerGet {

        Component run(PlayerDifficulty var1);
    }

    private interface PlayerLevelCommand {

        boolean run(PlayerDifficulty var1, int var2);
    }
}