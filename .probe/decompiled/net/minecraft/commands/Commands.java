package net.minecraft.commands;

import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.mojang.logging.LogUtils;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.ArgumentUtils;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.gametest.framework.TestCommand;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.commands.AdvancementCommands;
import net.minecraft.server.commands.AttributeCommand;
import net.minecraft.server.commands.BanIpCommands;
import net.minecraft.server.commands.BanListCommands;
import net.minecraft.server.commands.BanPlayerCommands;
import net.minecraft.server.commands.BossBarCommands;
import net.minecraft.server.commands.ClearInventoryCommands;
import net.minecraft.server.commands.CloneCommands;
import net.minecraft.server.commands.DamageCommand;
import net.minecraft.server.commands.DataPackCommand;
import net.minecraft.server.commands.DeOpCommands;
import net.minecraft.server.commands.DebugCommand;
import net.minecraft.server.commands.DefaultGameModeCommands;
import net.minecraft.server.commands.DifficultyCommand;
import net.minecraft.server.commands.EffectCommands;
import net.minecraft.server.commands.EmoteCommands;
import net.minecraft.server.commands.EnchantCommand;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.server.commands.ExperienceCommand;
import net.minecraft.server.commands.FillBiomeCommand;
import net.minecraft.server.commands.FillCommand;
import net.minecraft.server.commands.ForceLoadCommand;
import net.minecraft.server.commands.FunctionCommand;
import net.minecraft.server.commands.GameModeCommand;
import net.minecraft.server.commands.GameRuleCommand;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.commands.HelpCommand;
import net.minecraft.server.commands.ItemCommands;
import net.minecraft.server.commands.JfrCommand;
import net.minecraft.server.commands.KickCommand;
import net.minecraft.server.commands.KillCommand;
import net.minecraft.server.commands.ListPlayersCommand;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.server.commands.LootCommand;
import net.minecraft.server.commands.MsgCommand;
import net.minecraft.server.commands.OpCommand;
import net.minecraft.server.commands.PardonCommand;
import net.minecraft.server.commands.PardonIpCommand;
import net.minecraft.server.commands.ParticleCommand;
import net.minecraft.server.commands.PerfCommand;
import net.minecraft.server.commands.PlaceCommand;
import net.minecraft.server.commands.PlaySoundCommand;
import net.minecraft.server.commands.PublishCommand;
import net.minecraft.server.commands.RecipeCommand;
import net.minecraft.server.commands.ReloadCommand;
import net.minecraft.server.commands.ReturnCommand;
import net.minecraft.server.commands.RideCommand;
import net.minecraft.server.commands.SaveAllCommand;
import net.minecraft.server.commands.SaveOffCommand;
import net.minecraft.server.commands.SaveOnCommand;
import net.minecraft.server.commands.SayCommand;
import net.minecraft.server.commands.ScheduleCommand;
import net.minecraft.server.commands.ScoreboardCommand;
import net.minecraft.server.commands.SeedCommand;
import net.minecraft.server.commands.SetBlockCommand;
import net.minecraft.server.commands.SetPlayerIdleTimeoutCommand;
import net.minecraft.server.commands.SetSpawnCommand;
import net.minecraft.server.commands.SetWorldSpawnCommand;
import net.minecraft.server.commands.SpawnArmorTrimsCommand;
import net.minecraft.server.commands.SpectateCommand;
import net.minecraft.server.commands.SpreadPlayersCommand;
import net.minecraft.server.commands.StopCommand;
import net.minecraft.server.commands.StopSoundCommand;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.server.commands.TagCommand;
import net.minecraft.server.commands.TeamCommand;
import net.minecraft.server.commands.TeamMsgCommand;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.commands.TellRawCommand;
import net.minecraft.server.commands.TimeCommand;
import net.minecraft.server.commands.TitleCommand;
import net.minecraft.server.commands.TriggerCommand;
import net.minecraft.server.commands.WeatherCommand;
import net.minecraft.server.commands.WhitelistCommand;
import net.minecraft.server.commands.WorldBorderCommand;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import org.slf4j.Logger;

public class Commands {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int LEVEL_ALL = 0;

    public static final int LEVEL_MODERATORS = 1;

    public static final int LEVEL_GAMEMASTERS = 2;

    public static final int LEVEL_ADMINS = 3;

    public static final int LEVEL_OWNERS = 4;

    private final CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher();

    public Commands(Commands.CommandSelection commandsCommandSelection0, CommandBuildContext commandBuildContext1) {
        AdvancementCommands.register(this.dispatcher);
        AttributeCommand.register(this.dispatcher, commandBuildContext1);
        ExecuteCommand.register(this.dispatcher, commandBuildContext1);
        BossBarCommands.register(this.dispatcher);
        ClearInventoryCommands.register(this.dispatcher, commandBuildContext1);
        CloneCommands.register(this.dispatcher, commandBuildContext1);
        DamageCommand.register(this.dispatcher, commandBuildContext1);
        DataCommands.register(this.dispatcher);
        DataPackCommand.register(this.dispatcher);
        DebugCommand.register(this.dispatcher);
        DefaultGameModeCommands.register(this.dispatcher);
        DifficultyCommand.register(this.dispatcher);
        EffectCommands.register(this.dispatcher, commandBuildContext1);
        EmoteCommands.register(this.dispatcher);
        EnchantCommand.register(this.dispatcher, commandBuildContext1);
        ExperienceCommand.register(this.dispatcher);
        FillCommand.register(this.dispatcher, commandBuildContext1);
        FillBiomeCommand.register(this.dispatcher, commandBuildContext1);
        ForceLoadCommand.register(this.dispatcher);
        FunctionCommand.register(this.dispatcher);
        GameModeCommand.register(this.dispatcher);
        GameRuleCommand.register(this.dispatcher);
        GiveCommand.register(this.dispatcher, commandBuildContext1);
        HelpCommand.register(this.dispatcher);
        ItemCommands.register(this.dispatcher, commandBuildContext1);
        KickCommand.register(this.dispatcher);
        KillCommand.register(this.dispatcher);
        ListPlayersCommand.register(this.dispatcher);
        LocateCommand.register(this.dispatcher, commandBuildContext1);
        LootCommand.register(this.dispatcher, commandBuildContext1);
        MsgCommand.register(this.dispatcher);
        ParticleCommand.register(this.dispatcher, commandBuildContext1);
        PlaceCommand.register(this.dispatcher);
        PlaySoundCommand.register(this.dispatcher);
        ReloadCommand.register(this.dispatcher);
        RecipeCommand.register(this.dispatcher);
        ReturnCommand.register(this.dispatcher);
        RideCommand.register(this.dispatcher);
        SayCommand.register(this.dispatcher);
        ScheduleCommand.register(this.dispatcher);
        ScoreboardCommand.register(this.dispatcher);
        SeedCommand.register(this.dispatcher, commandsCommandSelection0 != Commands.CommandSelection.INTEGRATED);
        SetBlockCommand.register(this.dispatcher, commandBuildContext1);
        SetSpawnCommand.register(this.dispatcher);
        SetWorldSpawnCommand.register(this.dispatcher);
        SpectateCommand.register(this.dispatcher);
        SpreadPlayersCommand.register(this.dispatcher);
        StopSoundCommand.register(this.dispatcher);
        SummonCommand.register(this.dispatcher, commandBuildContext1);
        TagCommand.register(this.dispatcher);
        TeamCommand.register(this.dispatcher);
        TeamMsgCommand.register(this.dispatcher);
        TeleportCommand.register(this.dispatcher);
        TellRawCommand.register(this.dispatcher);
        TimeCommand.register(this.dispatcher);
        TitleCommand.register(this.dispatcher);
        TriggerCommand.register(this.dispatcher);
        WeatherCommand.register(this.dispatcher);
        WorldBorderCommand.register(this.dispatcher);
        if (JvmProfiler.INSTANCE.isAvailable()) {
            JfrCommand.register(this.dispatcher);
        }
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            TestCommand.register(this.dispatcher);
            SpawnArmorTrimsCommand.register(this.dispatcher);
        }
        if (commandsCommandSelection0.includeDedicated) {
            BanIpCommands.register(this.dispatcher);
            BanListCommands.register(this.dispatcher);
            BanPlayerCommands.register(this.dispatcher);
            DeOpCommands.register(this.dispatcher);
            OpCommand.register(this.dispatcher);
            PardonCommand.register(this.dispatcher);
            PardonIpCommand.register(this.dispatcher);
            PerfCommand.register(this.dispatcher);
            SaveAllCommand.register(this.dispatcher);
            SaveOffCommand.register(this.dispatcher);
            SaveOnCommand.register(this.dispatcher);
            SetPlayerIdleTimeoutCommand.register(this.dispatcher);
            StopCommand.register(this.dispatcher);
            WhitelistCommand.register(this.dispatcher);
        }
        if (commandsCommandSelection0.includeIntegrated) {
            PublishCommand.register(this.dispatcher);
        }
        this.dispatcher.setConsumer((p_230954_, p_230955_, p_230956_) -> ((CommandSourceStack) p_230954_.getSource()).onCommandComplete(p_230954_, p_230955_, p_230956_));
    }

    public static <S> ParseResults<S> mapSource(ParseResults<S> parseResultsS0, UnaryOperator<S> unaryOperatorS1) {
        CommandContextBuilder<S> $$2 = parseResultsS0.getContext();
        CommandContextBuilder<S> $$3 = $$2.withSource(unaryOperatorS1.apply($$2.getSource()));
        return new ParseResults($$3, parseResultsS0.getReader(), parseResultsS0.getExceptions());
    }

    public int performPrefixedCommand(CommandSourceStack commandSourceStack0, String string1) {
        string1 = string1.startsWith("/") ? string1.substring(1) : string1;
        return this.performCommand(this.dispatcher.parse(string1, commandSourceStack0), string1);
    }

    public int performCommand(ParseResults<CommandSourceStack> parseResultsCommandSourceStack0, String string1) {
        CommandSourceStack $$2 = (CommandSourceStack) parseResultsCommandSourceStack0.getContext().getSource();
        $$2.getServer().getProfiler().push((Supplier<String>) (() -> "/" + string1));
        byte var20;
        try {
            return this.dispatcher.execute(parseResultsCommandSourceStack0);
        } catch (CommandRuntimeException var13) {
            $$2.sendFailure(var13.getComponent());
            return 0;
        } catch (CommandSyntaxException var14) {
            $$2.sendFailure(ComponentUtils.fromMessage(var14.getRawMessage()));
            if (var14.getInput() != null && var14.getCursor() >= 0) {
                int $$5 = Math.min(var14.getInput().length(), var14.getCursor());
                MutableComponent $$6 = Component.empty().withStyle(ChatFormatting.GRAY).withStyle(p_82134_ -> p_82134_.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + string1)));
                if ($$5 > 10) {
                    $$6.append(CommonComponents.ELLIPSIS);
                }
                $$6.append(var14.getInput().substring(Math.max(0, $$5 - 10), $$5));
                if ($$5 < var14.getInput().length()) {
                    Component $$7 = Component.literal(var14.getInput().substring($$5)).withStyle(ChatFormatting.RED, ChatFormatting.UNDERLINE);
                    $$6.append($$7);
                }
                $$6.append(Component.translatable("command.context.here").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
                $$2.sendFailure($$6);
            }
            return 0;
        } catch (Exception var15) {
            MutableComponent $$9 = Component.literal(var15.getMessage() == null ? var15.getClass().getName() : var15.getMessage());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error("Command exception: /{}", string1, var15);
                StackTraceElement[] $$10 = var15.getStackTrace();
                for (int $$11 = 0; $$11 < Math.min($$10.length, 3); $$11++) {
                    $$9.append("\n\n").append($$10[$$11].getMethodName()).append("\n ").append($$10[$$11].getFileName()).append(":").append(String.valueOf($$10[$$11].getLineNumber()));
                }
            }
            $$2.sendFailure(Component.translatable("command.failed").withStyle(p_82137_ -> p_82137_.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, $$9))));
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                $$2.sendFailure(Component.literal(Util.describeError(var15)));
                LOGGER.error("'/{}' threw an exception", string1, var15);
            }
            var20 = 0;
        } finally {
            $$2.getServer().getProfiler().pop();
        }
        return var20;
    }

    public void sendCommands(ServerPlayer serverPlayer0) {
        Map<CommandNode<CommandSourceStack>, CommandNode<SharedSuggestionProvider>> $$1 = Maps.newHashMap();
        RootCommandNode<SharedSuggestionProvider> $$2 = new RootCommandNode();
        $$1.put(this.dispatcher.getRoot(), $$2);
        this.fillUsableCommands(this.dispatcher.getRoot(), $$2, serverPlayer0.m_20203_(), $$1);
        serverPlayer0.connection.send(new ClientboundCommandsPacket($$2));
    }

    private void fillUsableCommands(CommandNode<CommandSourceStack> commandNodeCommandSourceStack0, CommandNode<SharedSuggestionProvider> commandNodeSharedSuggestionProvider1, CommandSourceStack commandSourceStack2, Map<CommandNode<CommandSourceStack>, CommandNode<SharedSuggestionProvider>> mapCommandNodeCommandSourceStackCommandNodeSharedSuggestionProvider3) {
        for (CommandNode<CommandSourceStack> $$4 : commandNodeCommandSourceStack0.getChildren()) {
            if ($$4.canUse(commandSourceStack2)) {
                ArgumentBuilder<SharedSuggestionProvider, ?> $$5 = $$4.createBuilder();
                $$5.requires(p_82126_ -> true);
                if ($$5.getCommand() != null) {
                    $$5.executes(p_82102_ -> 0);
                }
                if ($$5 instanceof RequiredArgumentBuilder) {
                    RequiredArgumentBuilder<SharedSuggestionProvider, ?> $$6 = (RequiredArgumentBuilder<SharedSuggestionProvider, ?>) $$5;
                    if ($$6.getSuggestionsProvider() != null) {
                        $$6.suggests(SuggestionProviders.safelySwap($$6.getSuggestionsProvider()));
                    }
                }
                if ($$5.getRedirect() != null) {
                    $$5.redirect((CommandNode) mapCommandNodeCommandSourceStackCommandNodeSharedSuggestionProvider3.get($$5.getRedirect()));
                }
                CommandNode<SharedSuggestionProvider> $$7 = $$5.build();
                mapCommandNodeCommandSourceStackCommandNodeSharedSuggestionProvider3.put($$4, $$7);
                commandNodeSharedSuggestionProvider1.addChild($$7);
                if (!$$4.getChildren().isEmpty()) {
                    this.fillUsableCommands($$4, $$7, commandSourceStack2, mapCommandNodeCommandSourceStackCommandNodeSharedSuggestionProvider3);
                }
            }
        }
    }

    public static LiteralArgumentBuilder<CommandSourceStack> literal(String string0) {
        return LiteralArgumentBuilder.literal(string0);
    }

    public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String string0, ArgumentType<T> argumentTypeT1) {
        return RequiredArgumentBuilder.argument(string0, argumentTypeT1);
    }

    public static Predicate<String> createValidator(Commands.ParseFunction commandsParseFunction0) {
        return p_82124_ -> {
            try {
                commandsParseFunction0.parse(new StringReader(p_82124_));
                return true;
            } catch (CommandSyntaxException var3) {
                return false;
            }
        };
    }

    public CommandDispatcher<CommandSourceStack> getDispatcher() {
        return this.dispatcher;
    }

    @Nullable
    public static <S> CommandSyntaxException getParseException(ParseResults<S> parseResultsS0) {
        if (!parseResultsS0.getReader().canRead()) {
            return null;
        } else if (parseResultsS0.getExceptions().size() == 1) {
            return (CommandSyntaxException) parseResultsS0.getExceptions().values().iterator().next();
        } else {
            return parseResultsS0.getContext().getRange().isEmpty() ? CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parseResultsS0.getReader()) : CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parseResultsS0.getReader());
        }
    }

    public static CommandBuildContext createValidationContext(final HolderLookup.Provider holderLookupProvider0) {
        return new CommandBuildContext() {

            @Override
            public <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<T>> p_256244_) {
                final HolderLookup.RegistryLookup<T> $$1 = holderLookupProvider0.lookupOrThrow(p_256244_);
                return new HolderLookup.Delegate<T>($$1) {

                    @Override
                    public Optional<HolderSet.Named<T>> get(TagKey<T> p_255936_) {
                        return Optional.of(this.getOrThrow(p_255936_));
                    }

                    @Override
                    public HolderSet.Named<T> getOrThrow(TagKey<T> p_255953_) {
                        Optional<HolderSet.Named<T>> $$1 = $$1.m_254901_(p_255953_);
                        return (HolderSet.Named<T>) $$1.orElseGet(() -> HolderSet.emptyNamed($$1, p_255953_));
                    }
                };
            }
        };
    }

    public static void validate() {
        CommandBuildContext $$0 = createValidationContext(VanillaRegistries.createLookup());
        CommandDispatcher<CommandSourceStack> $$1 = new Commands(Commands.CommandSelection.ALL, $$0).getDispatcher();
        RootCommandNode<CommandSourceStack> $$2 = $$1.getRoot();
        $$1.findAmbiguities((p_230947_, p_230948_, p_230949_, p_230950_) -> LOGGER.warn("Ambiguity between arguments {} and {} with inputs: {}", new Object[] { $$1.getPath(p_230948_), $$1.getPath(p_230949_), p_230950_ }));
        Set<ArgumentType<?>> $$3 = ArgumentUtils.findUsedArgumentTypes($$2);
        Set<ArgumentType<?>> $$4 = (Set<ArgumentType<?>>) $$3.stream().filter(p_230961_ -> !ArgumentTypeInfos.isClassRecognized(p_230961_.getClass())).collect(Collectors.toSet());
        if (!$$4.isEmpty()) {
            LOGGER.warn("Missing type registration for following arguments:\n {}", $$4.stream().map(p_230952_ -> "\t" + p_230952_).collect(Collectors.joining(",\n")));
            throw new IllegalStateException("Unregistered argument types");
        }
    }

    public static enum CommandSelection {

        ALL(true, true), DEDICATED(false, true), INTEGRATED(true, false);

        final boolean includeIntegrated;

        final boolean includeDedicated;

        private CommandSelection(boolean p_82151_, boolean p_82152_) {
            this.includeIntegrated = p_82151_;
            this.includeDedicated = p_82152_;
        }
    }

    @FunctionalInterface
    public interface ParseFunction {

        void parse(StringReader var1) throws CommandSyntaxException;
    }
}