package net.minecraft.network.chat;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.commands.arguments.SignedArgument;

public record SignableCommand<S>(List<SignableCommand.Argument<S>> f_244150_) {

    private final List<SignableCommand.Argument<S>> arguments;

    public SignableCommand(List<SignableCommand.Argument<S>> f_244150_) {
        this.arguments = f_244150_;
    }

    public static <S> SignableCommand<S> of(ParseResults<S> p_250316_) {
        String $$1 = p_250316_.getReader().getString();
        CommandContextBuilder<S> $$2 = p_250316_.getContext();
        CommandContextBuilder<S> $$3 = $$2;
        List<SignableCommand.Argument<S>> $$4 = collectArguments($$1, $$2);
        CommandContextBuilder<S> $$5;
        while (($$5 = $$3.getChild()) != null) {
            boolean $$6 = $$5.getRootNode() != $$2.getRootNode();
            if (!$$6) {
                break;
            }
            $$4.addAll(collectArguments($$1, $$5));
            $$3 = $$5;
        }
        return new SignableCommand<>($$4);
    }

    private static <S> List<SignableCommand.Argument<S>> collectArguments(String p_252055_, CommandContextBuilder<S> p_251770_) {
        List<SignableCommand.Argument<S>> $$2 = new ArrayList();
        for (ParsedCommandNode<S> $$3 : p_251770_.getNodes()) {
            CommandNode $$5 = $$3.getNode();
            if ($$5 instanceof ArgumentCommandNode) {
                ArgumentCommandNode<S, ?> $$4 = (ArgumentCommandNode<S, ?>) $$5;
                if ($$4.getType() instanceof SignedArgument) {
                    ParsedArgument<S, ?> $$5x = (ParsedArgument<S, ?>) p_251770_.getArguments().get($$4.getName());
                    if ($$5x != null) {
                        String $$6 = $$5x.getRange().get(p_252055_);
                        $$2.add(new SignableCommand.Argument($$4, $$6));
                    }
                }
            }
        }
        return $$2;
    }

    public static record Argument<S>(ArgumentCommandNode<S, ?> f_243965_, String f_244218_) {

        private final ArgumentCommandNode<S, ?> node;

        private final String value;

        public Argument(ArgumentCommandNode<S, ?> f_243965_, String f_244218_) {
            this.node = f_243965_;
            this.value = f_244218_;
        }

        public String name() {
            return this.node.getName();
        }
    }
}