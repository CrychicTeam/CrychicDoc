package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Arrays;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ComponentArgument implements ArgumentType<Component> {

    private static final Collection<String> EXAMPLES = Arrays.asList("\"hello world\"", "\"\"", "\"{\"text\":\"hello world\"}", "[\"\"]");

    public static final DynamicCommandExceptionType ERROR_INVALID_JSON = new DynamicCommandExceptionType(p_87121_ -> Component.translatable("argument.component.invalid", p_87121_));

    private ComponentArgument() {
    }

    public static Component getComponent(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (Component) commandContextCommandSourceStack0.getArgument(string1, Component.class);
    }

    public static ComponentArgument textComponent() {
        return new ComponentArgument();
    }

    public Component parse(StringReader stringReader0) throws CommandSyntaxException {
        try {
            Component $$1 = Component.Serializer.fromJson(stringReader0);
            if ($$1 == null) {
                throw ERROR_INVALID_JSON.createWithContext(stringReader0, "empty");
            } else {
                return $$1;
            }
        } catch (Exception var4) {
            String $$3 = var4.getCause() != null ? var4.getCause().getMessage() : var4.getMessage();
            throw ERROR_INVALID_JSON.createWithContext(stringReader0, $$3);
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}