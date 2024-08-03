package net.minecraft.commands.arguments;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class GameProfileArgument implements ArgumentType<GameProfileArgument.Result> {

    private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "dd12be42-52a9-4a91-a8a1-11c01849e498", "@e");

    public static final SimpleCommandExceptionType ERROR_UNKNOWN_PLAYER = new SimpleCommandExceptionType(Component.translatable("argument.player.unknown"));

    public static Collection<GameProfile> getGameProfiles(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return ((GameProfileArgument.Result) commandContextCommandSourceStack0.getArgument(string1, GameProfileArgument.Result.class)).getNames((CommandSourceStack) commandContextCommandSourceStack0.getSource());
    }

    public static GameProfileArgument gameProfile() {
        return new GameProfileArgument();
    }

    public GameProfileArgument.Result parse(StringReader stringReader0) throws CommandSyntaxException {
        if (stringReader0.canRead() && stringReader0.peek() == '@') {
            EntitySelectorParser $$1 = new EntitySelectorParser(stringReader0);
            EntitySelector $$2 = $$1.parse();
            if ($$2.includesEntities()) {
                throw EntityArgument.ERROR_ONLY_PLAYERS_ALLOWED.create();
            } else {
                return new GameProfileArgument.SelectorResult($$2);
            }
        } else {
            int $$3 = stringReader0.getCursor();
            while (stringReader0.canRead() && stringReader0.peek() != ' ') {
                stringReader0.skip();
            }
            String $$4 = stringReader0.getString().substring($$3, stringReader0.getCursor());
            return p_94595_ -> {
                Optional<GameProfile> $$2 = p_94595_.getServer().getProfileCache().get($$4);
                return Collections.singleton((GameProfile) $$2.orElseThrow(ERROR_UNKNOWN_PLAYER::create));
            };
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        if (commandContextS0.getSource() instanceof SharedSuggestionProvider) {
            StringReader $$2 = new StringReader(suggestionsBuilder1.getInput());
            $$2.setCursor(suggestionsBuilder1.getStart());
            EntitySelectorParser $$3 = new EntitySelectorParser($$2);
            try {
                $$3.parse();
            } catch (CommandSyntaxException var6) {
            }
            return $$3.fillSuggestions(suggestionsBuilder1, p_94589_ -> SharedSuggestionProvider.suggest(((SharedSuggestionProvider) commandContextS0.getSource()).getOnlinePlayerNames(), p_94589_));
        } else {
            return Suggestions.empty();
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @FunctionalInterface
    public interface Result {

        Collection<GameProfile> getNames(CommandSourceStack var1) throws CommandSyntaxException;
    }

    public static class SelectorResult implements GameProfileArgument.Result {

        private final EntitySelector selector;

        public SelectorResult(EntitySelector entitySelector0) {
            this.selector = entitySelector0;
        }

        @Override
        public Collection<GameProfile> getNames(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
            List<ServerPlayer> $$1 = this.selector.findPlayers(commandSourceStack0);
            if ($$1.isEmpty()) {
                throw EntityArgument.NO_PLAYERS_FOUND.create();
            } else {
                List<GameProfile> $$2 = Lists.newArrayList();
                for (ServerPlayer $$3 : $$1) {
                    $$2.add($$3.m_36316_());
                }
                return $$2;
            }
        }
    }
}