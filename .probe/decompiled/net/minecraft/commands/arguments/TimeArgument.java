package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class TimeArgument implements ArgumentType<Integer> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0d", "0s", "0t", "0");

    private static final SimpleCommandExceptionType ERROR_INVALID_UNIT = new SimpleCommandExceptionType(Component.translatable("argument.time.invalid_unit"));

    private static final Dynamic2CommandExceptionType ERROR_TICK_COUNT_TOO_LOW = new Dynamic2CommandExceptionType((p_264715_, p_264716_) -> Component.translatable("argument.time.tick_count_too_low", p_264716_, p_264715_));

    private static final Object2IntMap<String> UNITS = new Object2IntOpenHashMap();

    final int minimum;

    private TimeArgument(int int0) {
        this.minimum = int0;
    }

    public static TimeArgument time() {
        return new TimeArgument(0);
    }

    public static TimeArgument time(int int0) {
        return new TimeArgument(int0);
    }

    public Integer parse(StringReader stringReader0) throws CommandSyntaxException {
        float $$1 = stringReader0.readFloat();
        String $$2 = stringReader0.readUnquotedString();
        int $$3 = UNITS.getOrDefault($$2, 0);
        if ($$3 == 0) {
            throw ERROR_INVALID_UNIT.create();
        } else {
            int $$4 = Math.round($$1 * (float) $$3);
            if ($$4 < this.minimum) {
                throw ERROR_TICK_COUNT_TOO_LOW.create($$4, this.minimum);
            } else {
                return $$4;
            }
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        StringReader $$2 = new StringReader(suggestionsBuilder1.getRemaining());
        try {
            $$2.readFloat();
        } catch (CommandSyntaxException var5) {
            return suggestionsBuilder1.buildFuture();
        }
        return SharedSuggestionProvider.suggest(UNITS.keySet(), suggestionsBuilder1.createOffset(suggestionsBuilder1.getStart() + $$2.getCursor()));
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    static {
        UNITS.put("d", 24000);
        UNITS.put("s", 20);
        UNITS.put("t", 1);
        UNITS.put("", 1);
    }

    public static class Info implements ArgumentTypeInfo<TimeArgument, TimeArgument.Info.Template> {

        public void serializeToNetwork(TimeArgument.Info.Template timeArgumentInfoTemplate0, FriendlyByteBuf friendlyByteBuf1) {
            friendlyByteBuf1.writeInt(timeArgumentInfoTemplate0.min);
        }

        public TimeArgument.Info.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
            int $$1 = friendlyByteBuf0.readInt();
            return new TimeArgument.Info.Template($$1);
        }

        public void serializeToJson(TimeArgument.Info.Template timeArgumentInfoTemplate0, JsonObject jsonObject1) {
            jsonObject1.addProperty("min", timeArgumentInfoTemplate0.min);
        }

        public TimeArgument.Info.Template unpack(TimeArgument timeArgument0) {
            return new TimeArgument.Info.Template(timeArgument0.minimum);
        }

        public final class Template implements ArgumentTypeInfo.Template<TimeArgument> {

            final int min;

            Template(int int0) {
                this.min = int0;
            }

            public TimeArgument instantiate(CommandBuildContext commandBuildContext0) {
                return TimeArgument.time(this.min);
            }

            @Override
            public ArgumentTypeInfo<TimeArgument, ?> type() {
                return Info.this;
            }
        }
    }
}