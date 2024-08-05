package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FunctionArgument implements ArgumentType<FunctionArgument.Result> {

    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "#foo");

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(p_120927_ -> Component.translatable("arguments.function.tag.unknown", p_120927_));

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_FUNCTION = new DynamicCommandExceptionType(p_120917_ -> Component.translatable("arguments.function.unknown", p_120917_));

    public static FunctionArgument functions() {
        return new FunctionArgument();
    }

    public FunctionArgument.Result parse(StringReader stringReader0) throws CommandSyntaxException {
        if (stringReader0.canRead() && stringReader0.peek() == '#') {
            stringReader0.skip();
            final ResourceLocation $$1 = ResourceLocation.read(stringReader0);
            return new FunctionArgument.Result() {

                @Override
                public Collection<CommandFunction> create(CommandContext<CommandSourceStack> p_120943_) throws CommandSyntaxException {
                    return FunctionArgument.getFunctionTag(p_120943_, $$1);
                }

                @Override
                public Pair<ResourceLocation, Either<CommandFunction, Collection<CommandFunction>>> unwrap(CommandContext<CommandSourceStack> p_120945_) throws CommandSyntaxException {
                    return Pair.of($$1, Either.right(FunctionArgument.getFunctionTag(p_120945_, $$1)));
                }
            };
        } else {
            final ResourceLocation $$2 = ResourceLocation.read(stringReader0);
            return new FunctionArgument.Result() {

                @Override
                public Collection<CommandFunction> create(CommandContext<CommandSourceStack> p_120952_) throws CommandSyntaxException {
                    return Collections.singleton(FunctionArgument.getFunction(p_120952_, $$2));
                }

                @Override
                public Pair<ResourceLocation, Either<CommandFunction, Collection<CommandFunction>>> unwrap(CommandContext<CommandSourceStack> p_120954_) throws CommandSyntaxException {
                    return Pair.of($$2, Either.left(FunctionArgument.getFunction(p_120954_, $$2)));
                }
            };
        }
    }

    static CommandFunction getFunction(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, ResourceLocation resourceLocation1) throws CommandSyntaxException {
        return (CommandFunction) ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getFunctions().get(resourceLocation1).orElseThrow(() -> ERROR_UNKNOWN_FUNCTION.create(resourceLocation1.toString()));
    }

    static Collection<CommandFunction> getFunctionTag(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, ResourceLocation resourceLocation1) throws CommandSyntaxException {
        Collection<CommandFunction> $$2 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getFunctions().getTag(resourceLocation1);
        if ($$2 == null) {
            throw ERROR_UNKNOWN_TAG.create(resourceLocation1.toString());
        } else {
            return $$2;
        }
    }

    public static Collection<CommandFunction> getFunctions(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return ((FunctionArgument.Result) commandContextCommandSourceStack0.getArgument(string1, FunctionArgument.Result.class)).create(commandContextCommandSourceStack0);
    }

    public static Pair<ResourceLocation, Either<CommandFunction, Collection<CommandFunction>>> getFunctionOrTag(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return ((FunctionArgument.Result) commandContextCommandSourceStack0.getArgument(string1, FunctionArgument.Result.class)).unwrap(commandContextCommandSourceStack0);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public interface Result {

        Collection<CommandFunction> create(CommandContext<CommandSourceStack> var1) throws CommandSyntaxException;

        Pair<ResourceLocation, Either<CommandFunction, Collection<CommandFunction>>> unwrap(CommandContext<CommandSourceStack> var1) throws CommandSyntaxException;
    }
}