package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

public class DataPackCommand {

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_PACK = new DynamicCommandExceptionType(p_136868_ -> Component.translatable("commands.datapack.unknown", p_136868_));

    private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_ENABLED = new DynamicCommandExceptionType(p_136857_ -> Component.translatable("commands.datapack.enable.failed", p_136857_));

    private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_DISABLED = new DynamicCommandExceptionType(p_136833_ -> Component.translatable("commands.datapack.disable.failed", p_136833_));

    private static final Dynamic2CommandExceptionType ERROR_PACK_FEATURES_NOT_ENABLED = new Dynamic2CommandExceptionType((p_248117_, p_248118_) -> Component.translatable("commands.datapack.enable.failed.no_flags", p_248117_, p_248118_));

    private static final SuggestionProvider<CommandSourceStack> SELECTED_PACKS = (p_136848_, p_136849_) -> SharedSuggestionProvider.suggest(((CommandSourceStack) p_136848_.getSource()).getServer().getPackRepository().getSelectedIds().stream().map(StringArgumentType::escapeIfRequired), p_136849_);

    private static final SuggestionProvider<CommandSourceStack> UNSELECTED_PACKS = (p_248113_, p_248114_) -> {
        PackRepository $$2 = ((CommandSourceStack) p_248113_.getSource()).getServer().getPackRepository();
        Collection<String> $$3 = $$2.getSelectedIds();
        FeatureFlagSet $$4 = ((CommandSourceStack) p_248113_.getSource()).enabledFeatures();
        return SharedSuggestionProvider.suggest($$2.getAvailablePacks().stream().filter(p_248116_ -> p_248116_.getRequestedFeatures().isSubsetOf($$4)).map(Pack::m_10446_).filter(p_250072_ -> !$$3.contains(p_250072_)).map(StringArgumentType::escapeIfRequired), p_248114_);
    };

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("datapack").requires(p_136872_ -> p_136872_.hasPermission(2))).then(Commands.literal("enable").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("name", StringArgumentType.string()).suggests(UNSELECTED_PACKS).executes(p_136876_ -> enablePack((CommandSourceStack) p_136876_.getSource(), getPack(p_136876_, "name", true), (p_180059_, p_180060_) -> p_180060_.getDefaultPosition().insert(p_180059_, p_180060_, p_180062_ -> p_180062_, false)))).then(Commands.literal("after").then(Commands.argument("existing", StringArgumentType.string()).suggests(SELECTED_PACKS).executes(p_136880_ -> enablePack((CommandSourceStack) p_136880_.getSource(), getPack(p_136880_, "name", true), (p_180056_, p_180057_) -> p_180056_.add(p_180056_.indexOf(getPack(p_136880_, "existing", false)) + 1, p_180057_)))))).then(Commands.literal("before").then(Commands.argument("existing", StringArgumentType.string()).suggests(SELECTED_PACKS).executes(p_136878_ -> enablePack((CommandSourceStack) p_136878_.getSource(), getPack(p_136878_, "name", true), (p_180046_, p_180047_) -> p_180046_.add(p_180046_.indexOf(getPack(p_136878_, "existing", false)), p_180047_)))))).then(Commands.literal("last").executes(p_136874_ -> enablePack((CommandSourceStack) p_136874_.getSource(), getPack(p_136874_, "name", true), List::add)))).then(Commands.literal("first").executes(p_136882_ -> enablePack((CommandSourceStack) p_136882_.getSource(), getPack(p_136882_, "name", true), (p_180052_, p_180053_) -> p_180052_.add(0, p_180053_))))))).then(Commands.literal("disable").then(Commands.argument("name", StringArgumentType.string()).suggests(SELECTED_PACKS).executes(p_136870_ -> disablePack((CommandSourceStack) p_136870_.getSource(), getPack(p_136870_, "name", false)))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("list").executes(p_136864_ -> listPacks((CommandSourceStack) p_136864_.getSource()))).then(Commands.literal("available").executes(p_136846_ -> listAvailablePacks((CommandSourceStack) p_136846_.getSource())))).then(Commands.literal("enabled").executes(p_136811_ -> listEnabledPacks((CommandSourceStack) p_136811_.getSource())))));
    }

    private static int enablePack(CommandSourceStack commandSourceStack0, Pack pack1, DataPackCommand.Inserter dataPackCommandInserter2) throws CommandSyntaxException {
        PackRepository $$3 = commandSourceStack0.getServer().getPackRepository();
        List<Pack> $$4 = Lists.newArrayList($$3.getSelectedPacks());
        dataPackCommandInserter2.apply($$4, pack1);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.datapack.modify.enable", pack1.getChatLink(true)), true);
        ReloadCommand.reloadPacks((Collection<String>) $$4.stream().map(Pack::m_10446_).collect(Collectors.toList()), commandSourceStack0);
        return $$4.size();
    }

    private static int disablePack(CommandSourceStack commandSourceStack0, Pack pack1) {
        PackRepository $$2 = commandSourceStack0.getServer().getPackRepository();
        List<Pack> $$3 = Lists.newArrayList($$2.getSelectedPacks());
        $$3.remove(pack1);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.datapack.modify.disable", pack1.getChatLink(true)), true);
        ReloadCommand.reloadPacks((Collection<String>) $$3.stream().map(Pack::m_10446_).collect(Collectors.toList()), commandSourceStack0);
        return $$3.size();
    }

    private static int listPacks(CommandSourceStack commandSourceStack0) {
        return listEnabledPacks(commandSourceStack0) + listAvailablePacks(commandSourceStack0);
    }

    private static int listAvailablePacks(CommandSourceStack commandSourceStack0) {
        PackRepository $$1 = commandSourceStack0.getServer().getPackRepository();
        $$1.reload();
        Collection<Pack> $$2 = $$1.getSelectedPacks();
        Collection<Pack> $$3 = $$1.getAvailablePacks();
        FeatureFlagSet $$4 = commandSourceStack0.enabledFeatures();
        List<Pack> $$5 = $$3.stream().filter(p_248121_ -> !$$2.contains(p_248121_) && p_248121_.getRequestedFeatures().isSubsetOf($$4)).toList();
        if ($$5.isEmpty()) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.datapack.list.available.none"), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.datapack.list.available.success", $$5.size(), ComponentUtils.formatList($$5, p_136844_ -> p_136844_.getChatLink(false))), false);
        }
        return $$5.size();
    }

    private static int listEnabledPacks(CommandSourceStack commandSourceStack0) {
        PackRepository $$1 = commandSourceStack0.getServer().getPackRepository();
        $$1.reload();
        Collection<? extends Pack> $$2 = $$1.getSelectedPacks();
        if ($$2.isEmpty()) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.datapack.list.enabled.none"), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.datapack.list.enabled.success", $$2.size(), ComponentUtils.formatList($$2, p_136807_ -> p_136807_.getChatLink(true))), false);
        }
        return $$2.size();
    }

    private static Pack getPack(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1, boolean boolean2) throws CommandSyntaxException {
        String $$3 = StringArgumentType.getString(commandContextCommandSourceStack0, string1);
        PackRepository $$4 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getPackRepository();
        Pack $$5 = $$4.getPack($$3);
        if ($$5 == null) {
            throw ERROR_UNKNOWN_PACK.create($$3);
        } else {
            boolean $$6 = $$4.getSelectedPacks().contains($$5);
            if (boolean2 && $$6) {
                throw ERROR_PACK_ALREADY_ENABLED.create($$3);
            } else if (!boolean2 && !$$6) {
                throw ERROR_PACK_ALREADY_DISABLED.create($$3);
            } else {
                FeatureFlagSet $$7 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).enabledFeatures();
                FeatureFlagSet $$8 = $$5.getRequestedFeatures();
                if (!$$8.isSubsetOf($$7)) {
                    throw ERROR_PACK_FEATURES_NOT_ENABLED.create($$3, FeatureFlags.printMissingFlags($$7, $$8));
                } else {
                    return $$5;
                }
            }
        }
    }

    interface Inserter {

        void apply(List<Pack> var1, Pack var2) throws CommandSyntaxException;
    }
}