package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class UuidArgument implements ArgumentType<UUID> {

    public static final SimpleCommandExceptionType ERROR_INVALID_UUID = new SimpleCommandExceptionType(Component.translatable("argument.uuid.invalid"));

    private static final Collection<String> EXAMPLES = Arrays.asList("dd12be42-52a9-4a91-a8a1-11c01849e498");

    private static final Pattern ALLOWED_CHARACTERS = Pattern.compile("^([-A-Fa-f0-9]+)");

    public static UUID getUuid(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (UUID) commandContextCommandSourceStack0.getArgument(string1, UUID.class);
    }

    public static UuidArgument uuid() {
        return new UuidArgument();
    }

    public UUID parse(StringReader stringReader0) throws CommandSyntaxException {
        String $$1 = stringReader0.getRemaining();
        Matcher $$2 = ALLOWED_CHARACTERS.matcher($$1);
        if ($$2.find()) {
            String $$3 = $$2.group(1);
            try {
                UUID $$4 = UUID.fromString($$3);
                stringReader0.setCursor(stringReader0.getCursor() + $$3.length());
                return $$4;
            } catch (IllegalArgumentException var6) {
            }
        }
        throw ERROR_INVALID_UUID.create();
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}