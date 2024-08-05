package snownee.kiwi.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import snownee.kiwi.Kiwi;
import snownee.kiwi.config.KiwiConfigManager;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.util.KEval;

public class KiwiCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("kiwi");
        builder.then(((LiteralArgumentBuilder) Commands.literal("debug_rules").requires(ctx -> ctx.hasPermission(2))).executes(ctx -> debugRules((CommandSourceStack) ctx.getSource())));
        builder.then(((LiteralArgumentBuilder) Commands.literal("reload").requires(ctx -> ctx.hasPermission(2))).then(Commands.argument("fileName", StringArgumentType.greedyString()).executes(ctx -> {
            String fileName = StringArgumentType.getString(ctx, "fileName");
            if (KiwiConfigManager.refresh(fileName)) {
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Component.translatable("commands.kiwi.reload.success", fileName), true);
                return 1;
            } else {
                ((CommandSourceStack) ctx.getSource()).sendFailure(Component.translatable("commands.kiwi.reload.failed", fileName));
                return 0;
            }
        })));
        builder.then(((LiteralArgumentBuilder) Commands.literal("eval").requires(ctx -> ctx.hasPermission(2))).then(Commands.argument("expression", StringArgumentType.greedyString()).executes(ctx -> eval((CommandSourceStack) ctx.getSource(), StringArgumentType.getString(ctx, "expression")))));
        dispatcher.register(builder);
    }

    private static int debugRules(CommandSourceStack commandSourceStack) {
        Commands commands = commandSourceStack.getServer().getCommands();
        for (String rule : List.of("gamerule doDaylightCycle false", "gamerule doWeatherCycle false", "gamerule doMobLoot false", "gamerule doMobSpawning false", "gamerule keepInventory true", "gamerule doTraderSpawning false", "gamerule doInsomnia false", "difficulty peaceful", "kill @e[type=!minecraft:player]", "time set day", "weather clear", "gamerule doMobLoot true")) {
            commands.performPrefixedCommand(commandSourceStack, rule);
        }
        return 1;
    }

    private static int eval(CommandSourceStack source, String expString) {
        try {
            EvaluationValue value = new Expression(expString, KEval.config()).evaluate();
            String s;
            if (value.isNumberValue()) {
                s = value.getNumberValue().toPlainString();
            } else {
                s = Objects.toString(value.getValue());
            }
            source.sendSuccess(() -> Component.literal(s), false);
            return value.isNullValue() ? 0 : value.getNumberValue().intValue();
        } catch (Throwable var4) {
            if (!Platform.isProduction()) {
                Kiwi.LOGGER.error(expString, var4);
            }
            source.sendFailure(Component.literal(var4.getLocalizedMessage()));
            return 0;
        }
    }
}