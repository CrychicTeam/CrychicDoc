package net.minecraftforge.server.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.Map;
import java.util.function.Function;

public final class CommandHelper {

    private CommandHelper() {
    }

    public static <S, T> void mergeCommandNode(CommandNode<S> sourceNode, CommandNode<T> resultNode, Map<CommandNode<S>, CommandNode<T>> sourceToResult, S canUse, Command<T> execute, Function<SuggestionProvider<S>, SuggestionProvider<T>> sourceToResultSuggestion) {
        sourceToResult.put(sourceNode, resultNode);
        for (CommandNode<S> sourceChild : sourceNode.getChildren()) {
            if (sourceChild.canUse(canUse)) {
                resultNode.addChild(toResult(sourceChild, sourceToResult, canUse, execute, sourceToResultSuggestion));
            }
        }
    }

    private static <S, T> CommandNode<T> toResult(CommandNode<S> sourceNode, Map<CommandNode<S>, CommandNode<T>> sourceToResult, S canUse, Command<T> execute, Function<SuggestionProvider<S>, SuggestionProvider<T>> sourceToResultSuggestion) {
        if (sourceToResult.containsKey(sourceNode)) {
            return (CommandNode<T>) sourceToResult.get(sourceNode);
        } else {
            ArgumentBuilder<T, ?> resultBuilder;
            if (sourceNode instanceof ArgumentCommandNode<S, ?> sourceArgument) {
                RequiredArgumentBuilder<T, ?> resultArgumentBuilder = RequiredArgumentBuilder.argument(sourceArgument.getName(), sourceArgument.getType());
                if (sourceArgument.getCustomSuggestions() != null) {
                    resultArgumentBuilder.suggests((SuggestionProvider) sourceToResultSuggestion.apply(sourceArgument.getCustomSuggestions()));
                }
                resultBuilder = resultArgumentBuilder;
            } else {
                if (!(sourceNode instanceof LiteralCommandNode<S> sourceLiteral)) {
                    if (sourceNode instanceof RootCommandNode) {
                        CommandNode<T> resultNode = new RootCommandNode();
                        mergeCommandNode(sourceNode, resultNode, sourceToResult, canUse, execute, sourceToResultSuggestion);
                        return resultNode;
                    }
                    throw new IllegalStateException("Node type " + sourceNode + " is not a standard node type");
                }
                resultBuilder = LiteralArgumentBuilder.literal(sourceLiteral.getLiteral());
            }
            if (sourceNode.getCommand() != null) {
                resultBuilder.executes(execute);
            }
            if (sourceNode.getRedirect() != null) {
                resultBuilder.redirect(toResult(sourceNode.getRedirect(), sourceToResult, canUse, execute, sourceToResultSuggestion));
            }
            CommandNode<T> resultNode = resultBuilder.build();
            mergeCommandNode(sourceNode, resultNode, sourceToResult, canUse, execute, sourceToResultSuggestion);
            return resultNode;
        }
    }
}