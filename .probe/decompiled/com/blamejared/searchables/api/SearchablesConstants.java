package com.blamejared.searchables.api;

import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

public class SearchablesConstants {

    public static final String MOD_ID = "searchables";

    public static final String STRING_CHARACTERS = "'\"`";

    public static final Component COMPONENT_SEARCH = Component.translatable("options.search");

    public static final Predicate<String> VALID_SUGGESTION = s -> {
        int quoteCount = 0;
        for (int i = 0; i < "'\"`".length(); i++) {
            if (StringUtils.contains(s, "'\"`".charAt(i))) {
                quoteCount++;
            }
        }
        return quoteCount < 3;
    };

    public static final Function<String, String> QUOTE = Util.memoize((Function<String, String>) (s -> {
        if (StringUtils.containsNone(s, "'\"` ")) {
            return s;
        } else {
            char quoteChar = '"';
            while (StringUtils.contains(s, quoteChar)) {
                quoteChar = (char) (switch(quoteChar) {
                    case '"' ->
                        39;
                    case '\'' ->
                        96;
                    default ->
                        throw new IllegalStateException("Unable to nicely wrap {" + s + "}! Make sure to filter Strings through 'SearchableComponent#VALID_SUGGESTION'!");
                });
            }
            return StringUtils.wrap(s, quoteChar);
        }
    }));

    public static ResourceLocation rl(String path) {
        return new ResourceLocation("searchables", path);
    }
}