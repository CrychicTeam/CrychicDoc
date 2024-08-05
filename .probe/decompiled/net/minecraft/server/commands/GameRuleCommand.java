package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameRules;

public class GameRuleCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        final LiteralArgumentBuilder<CommandSourceStack> $$1 = (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("gamerule").requires(p_137750_ -> p_137750_.hasPermission(2));
        GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {

            @Override
            public <T extends GameRules.Value<T>> void visit(GameRules.Key<T> p_137764_, GameRules.Type<T> p_137765_) {
                $$1.then(((LiteralArgumentBuilder) Commands.literal(p_137764_.getId()).executes(p_137771_ -> GameRuleCommand.queryRule((CommandSourceStack) p_137771_.getSource(), p_137764_))).then(p_137765_.createArgument("value").executes(p_137768_ -> GameRuleCommand.setRule(p_137768_, p_137764_))));
            }
        });
        commandDispatcherCommandSourceStack0.register($$1);
    }

    static <T extends GameRules.Value<T>> int setRule(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, GameRules.Key<T> gameRulesKeyT1) {
        CommandSourceStack $$2 = (CommandSourceStack) commandContextCommandSourceStack0.getSource();
        T $$3 = $$2.getServer().getGameRules().getRule(gameRulesKeyT1);
        $$3.setFromArgument(commandContextCommandSourceStack0, "value");
        $$2.sendSuccess(() -> Component.translatable("commands.gamerule.set", gameRulesKeyT1.getId(), $$3.toString()), true);
        return $$3.getCommandResult();
    }

    static <T extends GameRules.Value<T>> int queryRule(CommandSourceStack commandSourceStack0, GameRules.Key<T> gameRulesKeyT1) {
        T $$2 = commandSourceStack0.getServer().getGameRules().getRule(gameRulesKeyT1);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.gamerule.query", gameRulesKeyT1.getId(), $$2.toString()), false);
        return $$2.getCommandResult();
    }
}