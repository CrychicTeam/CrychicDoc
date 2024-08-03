package net.minecraft.commands.arguments;

import com.google.common.collect.Iterables;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class EntityArgument implements ArgumentType<EntitySelector> {

    private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "@e", "@e[type=foo]", "dd12be42-52a9-4a91-a8a1-11c01849e498");

    public static final SimpleCommandExceptionType ERROR_NOT_SINGLE_ENTITY = new SimpleCommandExceptionType(Component.translatable("argument.entity.toomany"));

    public static final SimpleCommandExceptionType ERROR_NOT_SINGLE_PLAYER = new SimpleCommandExceptionType(Component.translatable("argument.player.toomany"));

    public static final SimpleCommandExceptionType ERROR_ONLY_PLAYERS_ALLOWED = new SimpleCommandExceptionType(Component.translatable("argument.player.entities"));

    public static final SimpleCommandExceptionType NO_ENTITIES_FOUND = new SimpleCommandExceptionType(Component.translatable("argument.entity.notfound.entity"));

    public static final SimpleCommandExceptionType NO_PLAYERS_FOUND = new SimpleCommandExceptionType(Component.translatable("argument.entity.notfound.player"));

    public static final SimpleCommandExceptionType ERROR_SELECTORS_NOT_ALLOWED = new SimpleCommandExceptionType(Component.translatable("argument.entity.selector.not_allowed"));

    final boolean single;

    final boolean playersOnly;

    protected EntityArgument(boolean boolean0, boolean boolean1) {
        this.single = boolean0;
        this.playersOnly = boolean1;
    }

    public static EntityArgument entity() {
        return new EntityArgument(true, false);
    }

    public static Entity getEntity(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return ((EntitySelector) commandContextCommandSourceStack0.getArgument(string1, EntitySelector.class)).findSingleEntity((CommandSourceStack) commandContextCommandSourceStack0.getSource());
    }

    public static EntityArgument entities() {
        return new EntityArgument(false, false);
    }

    public static Collection<? extends Entity> getEntities(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        Collection<? extends Entity> $$2 = getOptionalEntities(commandContextCommandSourceStack0, string1);
        if ($$2.isEmpty()) {
            throw NO_ENTITIES_FOUND.create();
        } else {
            return $$2;
        }
    }

    public static Collection<? extends Entity> getOptionalEntities(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return ((EntitySelector) commandContextCommandSourceStack0.getArgument(string1, EntitySelector.class)).findEntities((CommandSourceStack) commandContextCommandSourceStack0.getSource());
    }

    public static Collection<ServerPlayer> getOptionalPlayers(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return ((EntitySelector) commandContextCommandSourceStack0.getArgument(string1, EntitySelector.class)).findPlayers((CommandSourceStack) commandContextCommandSourceStack0.getSource());
    }

    public static EntityArgument player() {
        return new EntityArgument(true, true);
    }

    public static ServerPlayer getPlayer(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return ((EntitySelector) commandContextCommandSourceStack0.getArgument(string1, EntitySelector.class)).findSinglePlayer((CommandSourceStack) commandContextCommandSourceStack0.getSource());
    }

    public static EntityArgument players() {
        return new EntityArgument(false, true);
    }

    public static Collection<ServerPlayer> getPlayers(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        List<ServerPlayer> $$2 = ((EntitySelector) commandContextCommandSourceStack0.getArgument(string1, EntitySelector.class)).findPlayers((CommandSourceStack) commandContextCommandSourceStack0.getSource());
        if ($$2.isEmpty()) {
            throw NO_PLAYERS_FOUND.create();
        } else {
            return $$2;
        }
    }

    public EntitySelector parse(StringReader stringReader0) throws CommandSyntaxException {
        int $$1 = 0;
        EntitySelectorParser $$2 = new EntitySelectorParser(stringReader0);
        EntitySelector $$3 = $$2.parse();
        if ($$3.getMaxResults() > 1 && this.single) {
            if (this.playersOnly) {
                stringReader0.setCursor(0);
                throw ERROR_NOT_SINGLE_PLAYER.createWithContext(stringReader0);
            } else {
                stringReader0.setCursor(0);
                throw ERROR_NOT_SINGLE_ENTITY.createWithContext(stringReader0);
            }
        } else if ($$3.includesEntities() && this.playersOnly && !$$3.isSelfSelector()) {
            stringReader0.setCursor(0);
            throw ERROR_ONLY_PLAYERS_ALLOWED.createWithContext(stringReader0);
        } else {
            return $$3;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        if (commandContextS0.getSource() instanceof SharedSuggestionProvider $$2) {
            StringReader $$3 = new StringReader(suggestionsBuilder1.getInput());
            $$3.setCursor(suggestionsBuilder1.getStart());
            EntitySelectorParser $$4 = new EntitySelectorParser($$3, $$2.hasPermission(2));
            try {
                $$4.parse();
            } catch (CommandSyntaxException var7) {
            }
            return $$4.fillSuggestions(suggestionsBuilder1, p_91457_ -> {
                Collection<String> $$2x = $$2.getOnlinePlayerNames();
                Iterable<String> $$3x = (Iterable<String>) (this.playersOnly ? $$2x : Iterables.concat($$2x, $$2.getSelectedEntities()));
                SharedSuggestionProvider.suggest($$3x, p_91457_);
            });
        } else {
            return Suggestions.empty();
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class Info implements ArgumentTypeInfo<EntityArgument, EntityArgument.Info.Template> {

        private static final byte FLAG_SINGLE = 1;

        private static final byte FLAG_PLAYERS_ONLY = 2;

        public void serializeToNetwork(EntityArgument.Info.Template entityArgumentInfoTemplate0, FriendlyByteBuf friendlyByteBuf1) {
            int $$2 = 0;
            if (entityArgumentInfoTemplate0.single) {
                $$2 |= 1;
            }
            if (entityArgumentInfoTemplate0.playersOnly) {
                $$2 |= 2;
            }
            friendlyByteBuf1.writeByte($$2);
        }

        public EntityArgument.Info.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
            byte $$1 = friendlyByteBuf0.readByte();
            return new EntityArgument.Info.Template(($$1 & 1) != 0, ($$1 & 2) != 0);
        }

        public void serializeToJson(EntityArgument.Info.Template entityArgumentInfoTemplate0, JsonObject jsonObject1) {
            jsonObject1.addProperty("amount", entityArgumentInfoTemplate0.single ? "single" : "multiple");
            jsonObject1.addProperty("type", entityArgumentInfoTemplate0.playersOnly ? "players" : "entities");
        }

        public EntityArgument.Info.Template unpack(EntityArgument entityArgument0) {
            return new EntityArgument.Info.Template(entityArgument0.single, entityArgument0.playersOnly);
        }

        public final class Template implements ArgumentTypeInfo.Template<EntityArgument> {

            final boolean single;

            final boolean playersOnly;

            Template(boolean boolean0, boolean boolean1) {
                this.single = boolean0;
                this.playersOnly = boolean1;
            }

            public EntityArgument instantiate(CommandBuildContext commandBuildContext0) {
                return new EntityArgument(this.single, this.playersOnly);
            }

            @Override
            public ArgumentTypeInfo<EntityArgument, ?> type() {
                return Info.this;
            }
        }
    }
}