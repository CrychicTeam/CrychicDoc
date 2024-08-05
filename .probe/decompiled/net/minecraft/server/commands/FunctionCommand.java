package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Collection;
import java.util.OptionalInt;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerFunctionManager;
import org.apache.commons.lang3.mutable.MutableObject;

public class FunctionCommand {

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_FUNCTION = (p_137719_, p_137720_) -> {
        ServerFunctionManager $$2 = ((CommandSourceStack) p_137719_.getSource()).getServer().getFunctions();
        SharedSuggestionProvider.suggestResource($$2.getTagNames(), p_137720_, "#");
        return SharedSuggestionProvider.suggestResource($$2.getFunctionNames(), p_137720_);
    };

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("function").requires(p_137722_ -> p_137722_.hasPermission(2))).then(Commands.argument("name", FunctionArgument.functions()).suggests(SUGGEST_FUNCTION).executes(p_137717_ -> runFunction((CommandSourceStack) p_137717_.getSource(), FunctionArgument.getFunctions(p_137717_, "name")))));
    }

    private static int runFunction(CommandSourceStack commandSourceStack0, Collection<CommandFunction> collectionCommandFunction1) {
        int $$2 = 0;
        boolean $$3 = false;
        for (CommandFunction $$4 : collectionCommandFunction1) {
            MutableObject<OptionalInt> $$5 = new MutableObject(OptionalInt.empty());
            int $$6 = commandSourceStack0.getServer().getFunctions().execute($$4, commandSourceStack0.withSuppressedOutput().withMaximumPermission(2).withReturnValueConsumer(p_280947_ -> $$5.setValue(OptionalInt.of(p_280947_))));
            OptionalInt $$7 = (OptionalInt) $$5.getValue();
            $$2 += $$7.orElse($$6);
            $$3 |= $$7.isPresent();
        }
        int $$8 = $$2;
        if (collectionCommandFunction1.size() == 1) {
            if ($$3) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.function.success.single.result", $$8, ((CommandFunction) collectionCommandFunction1.iterator().next()).getId()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.function.success.single", $$8, ((CommandFunction) collectionCommandFunction1.iterator().next()).getId()), true);
            }
        } else if ($$3) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.function.success.multiple.result", collectionCommandFunction1.size()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.function.success.multiple", $$8, collectionCommandFunction1.size()), true);
        }
        return $$2;
    }
}