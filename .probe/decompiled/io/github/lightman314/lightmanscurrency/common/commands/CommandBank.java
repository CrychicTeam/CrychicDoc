package io.github.lightman314.lightmanscurrency.common.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.money.bank.BankAPI;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin.PlayerBankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.source.builtin.PlayerBankAccountSource;
import io.github.lightman314.lightmanscurrency.api.money.bank.source.builtin.TeamBankAccountSource;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.common.commands.arguments.MoneyValueArgument;
import io.github.lightman314.lightmanscurrency.common.teams.Team;
import io.github.lightman314.lightmanscurrency.common.teams.TeamSaveData;
import java.util.List;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

public class CommandBank {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        LiteralArgumentBuilder<CommandSourceStack> bankCommand = (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("lcbank").requires(stack -> stack.hasPermission(2))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("give").then(Commands.literal("all").then(((RequiredArgumentBuilder) Commands.argument("amount", MoneyValueArgument.argument(context)).executes(c -> giveAll(c, true))).then(Commands.argument("notifyPlayers", BoolArgumentType.bool()).executes(c -> giveAll(c, BoolArgumentType.getBool(c, "notifyPlayers"))))))).then(Commands.literal("allPlayers").then(((RequiredArgumentBuilder) Commands.argument("amount", MoneyValueArgument.argument(context)).executes(c -> giveAllPlayers(c, true))).then(Commands.argument("notifyPlayers", BoolArgumentType.bool()).executes(c -> giveAllPlayers(c, BoolArgumentType.getBool(c, "notifyPlayers"))))))).then(Commands.literal("allTeams").then(((RequiredArgumentBuilder) Commands.argument("amount", MoneyValueArgument.argument(context)).executes(c -> giveAllTeams(c, true))).then(Commands.argument("notifyPlayers", BoolArgumentType.bool()).executes(c -> giveAllTeams(c, BoolArgumentType.getBool(c, "notifyPlayers"))))))).then(Commands.literal("players").then(Commands.argument("players", EntityArgument.players()).then(((RequiredArgumentBuilder) Commands.argument("amount", MoneyValueArgument.argument(context)).executes(c -> givePlayers(c, true))).then(Commands.argument("notifyPlayers", BoolArgumentType.bool()).executes(c -> givePlayers(c, BoolArgumentType.getBool(c, "notifyPlayers")))))))).then(Commands.literal("team").then(Commands.argument("teamID", LongArgumentType.longArg(0L)).then(((RequiredArgumentBuilder) Commands.argument("amount", MoneyValueArgument.argument(context)).executes(c -> giveTeam(c, true))).then(Commands.argument("notifyPlayers", BoolArgumentType.bool()).executes(c -> giveTeam(c, BoolArgumentType.getBool(c, "notifyPlayers"))))))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("take").then(Commands.literal("all").then(((RequiredArgumentBuilder) Commands.argument("amount", MoneyValueArgument.argument(context)).executes(c -> takeAll(c, true))).then(Commands.argument("notifyPlayers", BoolArgumentType.bool()).executes(c -> takeAll(c, BoolArgumentType.getBool(c, "notifyPlayers"))))))).then(Commands.literal("allPlayers").then(((RequiredArgumentBuilder) Commands.argument("amount", MoneyValueArgument.argument(context)).executes(c -> takeAllPlayers(c, true))).then(Commands.argument("notifyPlayers", BoolArgumentType.bool()).executes(c -> takeAllPlayers(c, BoolArgumentType.getBool(c, "notifyPlayers"))))))).then(Commands.literal("allTeams").then(((RequiredArgumentBuilder) Commands.argument("amount", MoneyValueArgument.argument(context)).executes(c -> takeAllTeams(c, true))).then(Commands.argument("notifyPlayers", BoolArgumentType.bool()).executes(c -> takeAllTeams(c, BoolArgumentType.getBool(c, "notifyPlayers"))))))).then(Commands.literal("players").then(Commands.argument("players", EntityArgument.players()).then(((RequiredArgumentBuilder) Commands.argument("amount", MoneyValueArgument.argument(context)).executes(c -> takePlayers(c, true))).then(Commands.argument("notifyPlayers", BoolArgumentType.bool()).executes(c -> takePlayers(c, BoolArgumentType.getBool(c, "notifyPlayers")))))))).then(Commands.literal("team").then(Commands.argument("teamID", LongArgumentType.longArg(0L)).then(((RequiredArgumentBuilder) Commands.argument("amount", MoneyValueArgument.argument(context)).executes(c -> takeTeam(c, true))).then(Commands.argument("notifyPlayers", BoolArgumentType.bool()).executes(c -> takeTeam(c, BoolArgumentType.getBool(c, "notifyPlayers"))))))));
        dispatcher.register(bankCommand);
    }

    static int giveAll(CommandContext<CommandSourceStack> commandContext, boolean notifyPlayers) throws CommandSyntaxException {
        MoneyValue amount = MoneyValueArgument.getMoneyValue(commandContext, "amount");
        return giveTo((CommandSourceStack) commandContext.getSource(), BankAPI.API.GetAllBankReferences(false), amount, notifyPlayers);
    }

    static int giveAllPlayers(CommandContext<CommandSourceStack> commandContext, boolean notifyPlayers) throws CommandSyntaxException {
        MoneyValue amount = MoneyValueArgument.getMoneyValue(commandContext, "amount");
        return giveTo((CommandSourceStack) commandContext.getSource(), PlayerBankAccountSource.INSTANCE.CollectAllReferences(false), amount, notifyPlayers);
    }

    static int giveAllTeams(CommandContext<CommandSourceStack> commandContext, boolean notifyPlayers) throws CommandSyntaxException {
        MoneyValue amount = MoneyValueArgument.getMoneyValue(commandContext, "amount");
        return giveTo((CommandSourceStack) commandContext.getSource(), TeamBankAccountSource.INSTANCE.CollectAllReferences(false), amount, notifyPlayers);
    }

    static int givePlayers(CommandContext<CommandSourceStack> commandContext, boolean notifyPlayers) throws CommandSyntaxException {
        MoneyValue amount = MoneyValueArgument.getMoneyValue(commandContext, "amount");
        return giveTo((CommandSourceStack) commandContext.getSource(), EntityArgument.getPlayers(commandContext, "players").stream().map(PlayerBankReference::of).toList(), amount, notifyPlayers);
    }

    static int giveTeam(CommandContext<CommandSourceStack> commandContext, boolean notifyPlayers) throws CommandSyntaxException {
        long teamID = LongArgumentType.getLong(commandContext, "teamID");
        MoneyValue amount = MoneyValueArgument.getMoneyValue(commandContext, "amount");
        CommandSourceStack source = (CommandSourceStack) commandContext.getSource();
        Team team = TeamSaveData.GetTeam(false, teamID);
        if (team == null) {
            EasyText.sendCommandFail(source, LCText.COMMAND_BANK_TEAM_NULL.get(teamID));
            return 0;
        } else if (!team.hasBankAccount()) {
            EasyText.sendCommandFail(source, LCText.COMMAND_BANK_TEAM_NO_BANK.get(teamID));
            return 0;
        } else {
            return giveTo(source, Lists.newArrayList(new BankReference[] { team.getBankReference() }), amount, notifyPlayers);
        }
    }

    static int giveTo(CommandSourceStack source, List<BankReference> accounts, MoneyValue amount, boolean notifyPlayers) {
        int count = 0;
        Component bankName = null;
        for (BankReference account : accounts) {
            if (BankAPI.API.BankDepositFromServer(account.get(), amount, notifyPlayers)) {
                if (++count == 1) {
                    bankName = account.get().getName();
                }
            }
        }
        if (count < 1) {
            EasyText.sendCommandFail(source, LCText.COMMAND_BANK_GIVE_FAIL.get());
        } else if (count == 1) {
            EasyText.sendCommandSucess(source, LCText.COMMAND_BANK_GIVE_SUCCESS_SINGLE.get(amount.getText(), bankName), true);
        } else {
            EasyText.sendCommandSucess(source, LCText.COMMAND_BANK_GIVE_SUCCESS.get(amount.getText(), count), true);
        }
        return count;
    }

    static int takeAll(CommandContext<CommandSourceStack> commandContext, boolean notifyPlayers) throws CommandSyntaxException {
        MoneyValue amount = MoneyValueArgument.getMoneyValue(commandContext, "amount");
        return takeFrom((CommandSourceStack) commandContext.getSource(), BankAPI.API.GetAllBankReferences(false), amount, notifyPlayers);
    }

    static int takeAllPlayers(CommandContext<CommandSourceStack> commandContext, boolean notifyPlayers) throws CommandSyntaxException {
        MoneyValue amount = MoneyValueArgument.getMoneyValue(commandContext, "amount");
        return takeFrom((CommandSourceStack) commandContext.getSource(), PlayerBankAccountSource.INSTANCE.CollectAllReferences(false), amount, notifyPlayers);
    }

    static int takeAllTeams(CommandContext<CommandSourceStack> commandContext, boolean notifyPlayers) throws CommandSyntaxException {
        MoneyValue amount = MoneyValueArgument.getMoneyValue(commandContext, "amount");
        return takeFrom((CommandSourceStack) commandContext.getSource(), TeamBankAccountSource.INSTANCE.CollectAllReferences(false), amount, notifyPlayers);
    }

    static int takePlayers(CommandContext<CommandSourceStack> commandContext, boolean notifyPlayers) throws CommandSyntaxException {
        MoneyValue amount = MoneyValueArgument.getMoneyValue(commandContext, "amount");
        return takeFrom((CommandSourceStack) commandContext.getSource(), EntityArgument.getPlayers(commandContext, "players").stream().map(PlayerBankReference::of).toList(), amount, notifyPlayers);
    }

    static int takeTeam(CommandContext<CommandSourceStack> commandContext, boolean notifyPlayers) throws CommandSyntaxException {
        long teamID = LongArgumentType.getLong(commandContext, "teamID");
        MoneyValue amount = MoneyValueArgument.getMoneyValue(commandContext, "amount");
        CommandSourceStack source = (CommandSourceStack) commandContext.getSource();
        Team team = TeamSaveData.GetTeam(false, teamID);
        if (team == null) {
            EasyText.sendCommandFail(source, LCText.COMMAND_BANK_TEAM_NULL.get(teamID));
            return 0;
        } else if (!team.hasBankAccount()) {
            EasyText.sendCommandFail(source, LCText.COMMAND_BANK_TEAM_NO_BANK.get(teamID));
            return 0;
        } else {
            return takeFrom((CommandSourceStack) commandContext.getSource(), Lists.newArrayList(new BankReference[] { team.getBankReference() }), amount, notifyPlayers);
        }
    }

    static int takeFrom(CommandSourceStack source, List<BankReference> accounts, MoneyValue amount, boolean notifyPlayers) {
        int count = 0;
        Component bankName = null;
        MoneyValue largestAmount = MoneyValue.empty();
        for (BankReference account : accounts) {
            Pair<Boolean, MoneyValue> result = BankAPI.API.BankWithdrawFromServer(account.get(), amount, notifyPlayers);
            if ((Boolean) result.getFirst()) {
                if (++count == 1) {
                    bankName = account.get().getName();
                }
                if (((MoneyValue) result.getSecond()).getCoreValue() > largestAmount.getCoreValue()) {
                    largestAmount = (MoneyValue) result.getSecond();
                }
            }
        }
        if (count < 1) {
            EasyText.sendCommandFail(source, LCText.COMMAND_BANK_TAKE_FAIL.get());
        } else if (count == 1) {
            EasyText.sendCommandSucess(source, LCText.COMMAND_BANK_TAKE_SUCCESS_SINGLE.get(largestAmount.getText(), bankName), true);
        } else {
            EasyText.sendCommandSucess(source, LCText.COMMAND_BANK_TAKE_SUCCESS.get(largestAmount.getText(), count), true);
        }
        return count;
    }
}