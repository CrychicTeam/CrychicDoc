package net.minecraft.commands.arguments;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class ScoreHolderArgument implements ArgumentType<ScoreHolderArgument.Result> {

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_SCORE_HOLDERS = (p_108221_, p_108222_) -> {
        StringReader $$2 = new StringReader(p_108222_.getInput());
        $$2.setCursor(p_108222_.getStart());
        EntitySelectorParser $$3 = new EntitySelectorParser($$2);
        try {
            $$3.parse();
        } catch (CommandSyntaxException var5) {
        }
        return $$3.fillSuggestions(p_108222_, p_171606_ -> SharedSuggestionProvider.suggest(((CommandSourceStack) p_108221_.getSource()).getOnlinePlayerNames(), p_171606_));
    };

    private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "*", "@e");

    private static final SimpleCommandExceptionType ERROR_NO_RESULTS = new SimpleCommandExceptionType(Component.translatable("argument.scoreHolder.empty"));

    final boolean multiple;

    public ScoreHolderArgument(boolean boolean0) {
        this.multiple = boolean0;
    }

    public static String getName(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return (String) getNames(commandContextCommandSourceStack0, string1).iterator().next();
    }

    public static Collection<String> getNames(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return getNames(commandContextCommandSourceStack0, string1, Collections::emptyList);
    }

    public static Collection<String> getNamesWithDefaultWildcard(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return getNames(commandContextCommandSourceStack0, string1, ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getScoreboard()::m_83482_);
    }

    public static Collection<String> getNames(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1, Supplier<Collection<String>> supplierCollectionString2) throws CommandSyntaxException {
        Collection<String> $$3 = ((ScoreHolderArgument.Result) commandContextCommandSourceStack0.getArgument(string1, ScoreHolderArgument.Result.class)).getNames((CommandSourceStack) commandContextCommandSourceStack0.getSource(), supplierCollectionString2);
        if ($$3.isEmpty()) {
            throw EntityArgument.NO_ENTITIES_FOUND.create();
        } else {
            return $$3;
        }
    }

    public static ScoreHolderArgument scoreHolder() {
        return new ScoreHolderArgument(false);
    }

    public static ScoreHolderArgument scoreHolders() {
        return new ScoreHolderArgument(true);
    }

    public ScoreHolderArgument.Result parse(StringReader stringReader0) throws CommandSyntaxException {
        if (stringReader0.canRead() && stringReader0.peek() == '@') {
            EntitySelectorParser $$1 = new EntitySelectorParser(stringReader0);
            EntitySelector $$2 = $$1.parse();
            if (!this.multiple && $$2.getMaxResults() > 1) {
                throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
            } else {
                return new ScoreHolderArgument.SelectorResult($$2);
            }
        } else {
            int $$3 = stringReader0.getCursor();
            while (stringReader0.canRead() && stringReader0.peek() != ' ') {
                stringReader0.skip();
            }
            String $$4 = stringReader0.getString().substring($$3, stringReader0.getCursor());
            if ($$4.equals("*")) {
                return (p_108231_, p_108232_) -> {
                    Collection<String> $$2 = (Collection<String>) p_108232_.get();
                    if ($$2.isEmpty()) {
                        throw ERROR_NO_RESULTS.create();
                    } else {
                        return $$2;
                    }
                };
            } else {
                Collection<String> $$5 = Collections.singleton($$4);
                return (p_108237_, p_108238_) -> $$5;
            }
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class Info implements ArgumentTypeInfo<ScoreHolderArgument, ScoreHolderArgument.Info.Template> {

        private static final byte FLAG_MULTIPLE = 1;

        public void serializeToNetwork(ScoreHolderArgument.Info.Template scoreHolderArgumentInfoTemplate0, FriendlyByteBuf friendlyByteBuf1) {
            int $$2 = 0;
            if (scoreHolderArgumentInfoTemplate0.multiple) {
                $$2 |= 1;
            }
            friendlyByteBuf1.writeByte($$2);
        }

        public ScoreHolderArgument.Info.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
            byte $$1 = friendlyByteBuf0.readByte();
            boolean $$2 = ($$1 & 1) != 0;
            return new ScoreHolderArgument.Info.Template($$2);
        }

        public void serializeToJson(ScoreHolderArgument.Info.Template scoreHolderArgumentInfoTemplate0, JsonObject jsonObject1) {
            jsonObject1.addProperty("amount", scoreHolderArgumentInfoTemplate0.multiple ? "multiple" : "single");
        }

        public ScoreHolderArgument.Info.Template unpack(ScoreHolderArgument scoreHolderArgument0) {
            return new ScoreHolderArgument.Info.Template(scoreHolderArgument0.multiple);
        }

        public final class Template implements ArgumentTypeInfo.Template<ScoreHolderArgument> {

            final boolean multiple;

            Template(boolean boolean0) {
                this.multiple = boolean0;
            }

            public ScoreHolderArgument instantiate(CommandBuildContext commandBuildContext0) {
                return new ScoreHolderArgument(this.multiple);
            }

            @Override
            public ArgumentTypeInfo<ScoreHolderArgument, ?> type() {
                return Info.this;
            }
        }
    }

    @FunctionalInterface
    public interface Result {

        Collection<String> getNames(CommandSourceStack var1, Supplier<Collection<String>> var2) throws CommandSyntaxException;
    }

    public static class SelectorResult implements ScoreHolderArgument.Result {

        private final EntitySelector selector;

        public SelectorResult(EntitySelector entitySelector0) {
            this.selector = entitySelector0;
        }

        @Override
        public Collection<String> getNames(CommandSourceStack commandSourceStack0, Supplier<Collection<String>> supplierCollectionString1) throws CommandSyntaxException {
            List<? extends Entity> $$2 = this.selector.findEntities(commandSourceStack0);
            if ($$2.isEmpty()) {
                throw EntityArgument.NO_ENTITIES_FOUND.create();
            } else {
                List<String> $$3 = Lists.newArrayList();
                for (Entity $$4 : $$2) {
                    $$3.add($$4.getScoreboardName());
                }
                return $$3;
            }
        }
    }
}