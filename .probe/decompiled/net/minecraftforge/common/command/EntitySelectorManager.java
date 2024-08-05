package net.minecraftforge.common.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.HashMap;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;

public class EntitySelectorManager {

    private static final HashMap<String, IEntitySelectorType> REGISTRY = new HashMap();

    public static void register(String token, IEntitySelectorType type) {
        if (token.isEmpty()) {
            throw new IllegalArgumentException("Token must not be empty");
        } else if (Arrays.asList("p", "a", "r", "s", "e").contains(token)) {
            throw new IllegalArgumentException("Token clashes with vanilla @" + token);
        } else {
            for (char c : token.toCharArray()) {
                if (!StringReader.isAllowedInUnquotedString(c)) {
                    throw new IllegalArgumentException("Token must only contain allowed characters");
                }
            }
            REGISTRY.put(token, type);
        }
    }

    public static EntitySelector parseSelector(EntitySelectorParser parser) throws CommandSyntaxException {
        if (parser.getReader().canRead()) {
            int i = parser.getReader().getCursor();
            String token = parser.getReader().readUnquotedString();
            IEntitySelectorType type = (IEntitySelectorType) REGISTRY.get(token);
            if (type != null) {
                return type.build(parser);
            }
            parser.getReader().setCursor(i);
        }
        return null;
    }

    public static void fillSelectorSuggestions(SuggestionsBuilder suggestionBuilder) {
        REGISTRY.forEach((token, type) -> suggestionBuilder.suggest("@" + token, type.getSuggestionTooltip()));
    }
}