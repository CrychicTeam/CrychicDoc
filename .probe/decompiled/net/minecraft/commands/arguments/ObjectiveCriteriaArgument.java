package net.minecraft.commands.arguments;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class ObjectiveCriteriaArgument implements ArgumentType<ObjectiveCriteria> {

    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo.bar.baz", "minecraft:foo");

    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(p_102569_ -> Component.translatable("argument.criteria.invalid", p_102569_));

    private ObjectiveCriteriaArgument() {
    }

    public static ObjectiveCriteriaArgument criteria() {
        return new ObjectiveCriteriaArgument();
    }

    public static ObjectiveCriteria getCriteria(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (ObjectiveCriteria) commandContextCommandSourceStack0.getArgument(string1, ObjectiveCriteria.class);
    }

    public ObjectiveCriteria parse(StringReader stringReader0) throws CommandSyntaxException {
        int $$1 = stringReader0.getCursor();
        while (stringReader0.canRead() && stringReader0.peek() != ' ') {
            stringReader0.skip();
        }
        String $$2 = stringReader0.getString().substring($$1, stringReader0.getCursor());
        return (ObjectiveCriteria) ObjectiveCriteria.byName($$2).orElseThrow(() -> {
            stringReader0.setCursor($$1);
            return ERROR_INVALID_VALUE.create($$2);
        });
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        List<String> $$2 = Lists.newArrayList(ObjectiveCriteria.getCustomCriteriaNames());
        for (StatType<?> $$3 : BuiltInRegistries.STAT_TYPE) {
            for (Object $$4 : $$3.getRegistry()) {
                String $$5 = this.getName($$3, $$4);
                $$2.add($$5);
            }
        }
        return SharedSuggestionProvider.suggest($$2, suggestionsBuilder1);
    }

    public <T> String getName(StatType<T> statTypeT0, Object object1) {
        return Stat.buildName(statTypeT0, (T) object1);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}