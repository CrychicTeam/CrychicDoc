package eu.midnightdust.lib.config;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import eu.midnightdust.lib.util.PlatformFunctions;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class AutoCommand {

    public static List<LiteralArgumentBuilder<CommandSourceStack>> commands = new ArrayList();

    private LiteralArgumentBuilder<CommandSourceStack> command;

    final Field entry;

    final String modid;

    public AutoCommand(Field entry, String modid) {
        this.entry = entry;
        this.modid = modid;
    }

    public void register() {
        this.command = Commands.literal(this.modid);
        this.command();
        LiteralArgumentBuilder<CommandSourceStack> finalized = (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) Commands.literal("midnightconfig").requires(source -> source.hasPermission(2))).then(this.command);
        PlatformFunctions.registerCommand(finalized);
        commands.add(finalized);
    }

    private void command() {
        if (this.entry.getType() == int.class) {
            this.command = (LiteralArgumentBuilder<CommandSourceStack>) this.command.then(((LiteralArgumentBuilder) Commands.literal(this.entry.getName()).executes(ctx -> this.getValue((CommandSourceStack) ctx.getSource()))).then(Commands.argument("value", IntegerArgumentType.integer((int) ((MidnightConfig.Entry) this.entry.getAnnotation(MidnightConfig.Entry.class)).min(), (int) ((MidnightConfig.Entry) this.entry.getAnnotation(MidnightConfig.Entry.class)).max())).executes(ctx -> this.setValue((CommandSourceStack) ctx.getSource(), IntegerArgumentType.getInteger(ctx, "value")))));
        } else if (this.entry.getType() == double.class) {
            this.command = (LiteralArgumentBuilder<CommandSourceStack>) this.command.then(((LiteralArgumentBuilder) Commands.literal(this.entry.getName()).executes(ctx -> this.getValue((CommandSourceStack) ctx.getSource()))).then(Commands.argument("value", DoubleArgumentType.doubleArg(((MidnightConfig.Entry) this.entry.getAnnotation(MidnightConfig.Entry.class)).min(), ((MidnightConfig.Entry) this.entry.getAnnotation(MidnightConfig.Entry.class)).max())).executes(ctx -> this.setValue((CommandSourceStack) ctx.getSource(), DoubleArgumentType.getDouble(ctx, "value")))));
        } else if (this.entry.getType() == boolean.class) {
            this.command = (LiteralArgumentBuilder<CommandSourceStack>) this.command.then(((LiteralArgumentBuilder) Commands.literal(this.entry.getName()).executes(ctx -> this.getValue((CommandSourceStack) ctx.getSource()))).then(Commands.literal("true").executes(ctx -> this.setValue((CommandSourceStack) ctx.getSource(), true))));
            this.command = (LiteralArgumentBuilder<CommandSourceStack>) this.command.then(((LiteralArgumentBuilder) Commands.literal(this.entry.getName()).executes(ctx -> this.getValue((CommandSourceStack) ctx.getSource()))).then(Commands.literal("false").executes(ctx -> this.setValue((CommandSourceStack) ctx.getSource(), false))));
        } else if (this.entry.getType().isEnum()) {
            for (int i = 0; i < this.entry.getType().getEnumConstants().length; i++) {
                Object enumValue = Arrays.stream(this.entry.getType().getEnumConstants()).toList().get(i);
                this.command = (LiteralArgumentBuilder<CommandSourceStack>) this.command.then(((LiteralArgumentBuilder) Commands.literal(this.entry.getName()).executes(ctx -> this.getValue((CommandSourceStack) ctx.getSource()))).then(Commands.literal(enumValue.toString()).executes(ctx -> this.setValue((CommandSourceStack) ctx.getSource(), enumValue))));
            }
        } else {
            this.command = (LiteralArgumentBuilder<CommandSourceStack>) this.command.then(((LiteralArgumentBuilder) Commands.literal(this.entry.getName()).executes(ctx -> this.getValue((CommandSourceStack) ctx.getSource()))).then(Commands.argument("value", StringArgumentType.string()).executes(ctx -> this.setValue((CommandSourceStack) ctx.getSource(), StringArgumentType.getString(ctx, "value")))));
        }
    }

    private int setValue(CommandSourceStack source, Object value) {
        try {
            this.entry.set(null, value);
            MidnightConfig.write(this.modid);
        } catch (Exception var4) {
            source.sendFailure(Component.literal("Could not set " + this.entry.getName() + " to value " + value + ": " + var4));
            return 0;
        }
        source.sendSuccess(() -> Component.literal("Successfully set " + this.entry.getName() + " to " + value), true);
        return 1;
    }

    private int getValue(CommandSourceStack source) {
        source.sendSuccess(() -> {
            try {
                return Component.literal("The value of " + this.entry.getName() + " is " + this.entry.get(null));
            } catch (IllegalAccessException var2) {
                throw new RuntimeException(var2);
            }
        }, false);
        return 0;
    }
}