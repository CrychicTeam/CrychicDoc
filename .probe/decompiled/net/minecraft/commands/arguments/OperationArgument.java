package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.scores.Score;

public class OperationArgument implements ArgumentType<OperationArgument.Operation> {

    private static final Collection<String> EXAMPLES = Arrays.asList("=", ">", "<");

    private static final SimpleCommandExceptionType ERROR_INVALID_OPERATION = new SimpleCommandExceptionType(Component.translatable("arguments.operation.invalid"));

    private static final SimpleCommandExceptionType ERROR_DIVIDE_BY_ZERO = new SimpleCommandExceptionType(Component.translatable("arguments.operation.div0"));

    public static OperationArgument operation() {
        return new OperationArgument();
    }

    public static OperationArgument.Operation getOperation(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (OperationArgument.Operation) commandContextCommandSourceStack0.getArgument(string1, OperationArgument.Operation.class);
    }

    public OperationArgument.Operation parse(StringReader stringReader0) throws CommandSyntaxException {
        if (!stringReader0.canRead()) {
            throw ERROR_INVALID_OPERATION.create();
        } else {
            int $$1 = stringReader0.getCursor();
            while (stringReader0.canRead() && stringReader0.peek() != ' ') {
                stringReader0.skip();
            }
            return getOperation(stringReader0.getString().substring($$1, stringReader0.getCursor()));
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return SharedSuggestionProvider.suggest(new String[] { "=", "+=", "-=", "*=", "/=", "%=", "<", ">", "><" }, suggestionsBuilder1);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    private static OperationArgument.Operation getOperation(String string0) throws CommandSyntaxException {
        return (OperationArgument.Operation) (string0.equals("><") ? (p_103279_, p_103280_) -> {
            int $$2 = p_103279_.getScore();
            p_103279_.setScore(p_103280_.getScore());
            p_103280_.setScore($$2);
        } : getSimpleOperation(string0));
    }

    private static OperationArgument.SimpleOperation getSimpleOperation(String string0) throws CommandSyntaxException {
        switch(string0) {
            case "=":
                return (p_103298_, p_103299_) -> p_103299_;
            case "+=":
                return (p_103295_, p_103296_) -> p_103295_ + p_103296_;
            case "-=":
                return (p_103292_, p_103293_) -> p_103292_ - p_103293_;
            case "*=":
                return (p_103289_, p_103290_) -> p_103289_ * p_103290_;
            case "/=":
                return (p_264713_, p_264714_) -> {
                    if (p_264714_ == 0) {
                        throw ERROR_DIVIDE_BY_ZERO.create();
                    } else {
                        return Mth.floorDiv(p_264713_, p_264714_);
                    }
                };
            case "%=":
                return (p_103271_, p_103272_) -> {
                    if (p_103272_ == 0) {
                        throw ERROR_DIVIDE_BY_ZERO.create();
                    } else {
                        return Mth.positiveModulo(p_103271_, p_103272_);
                    }
                };
            case "<":
                return Math::min;
            case ">":
                return Math::max;
            default:
                throw ERROR_INVALID_OPERATION.create();
        }
    }

    @FunctionalInterface
    public interface Operation {

        void apply(Score var1, Score var2) throws CommandSyntaxException;
    }

    @FunctionalInterface
    interface SimpleOperation extends OperationArgument.Operation {

        int apply(int var1, int var2) throws CommandSyntaxException;

        @Override
        default void apply(Score score0, Score score1) throws CommandSyntaxException {
            score0.setScore(this.apply(score0.getScore(), score1.getScore()));
        }
    }
}