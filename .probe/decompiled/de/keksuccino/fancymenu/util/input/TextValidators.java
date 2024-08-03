package de.keksuccino.fancymenu.util.input;

import de.keksuccino.fancymenu.util.ConsumingSupplier;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;

public class TextValidators {

    private static final CharacterFilter INTEGER_CHARACTER_FILTER = CharacterFilter.buildIntegerFiler();

    private static final CharacterFilter DOUBLE_CHARACTER_FILTER = CharacterFilter.buildDecimalFiler();

    public static final ConsumingSupplier<String, Boolean> NO_EMPTY_STRING_TEXT_VALIDATOR = consumes -> consumes != null && !consumes.replace(" ", "").isEmpty();

    public static final ConsumingSupplier<String, Boolean> NO_EMPTY_STRING_SPACES_ALLOWED_TEXT_VALIDATOR = consumes -> consumes != null && !consumes.isEmpty();

    public static final ConsumingSupplier<String, Boolean> BASIC_URL_TEXT_VALIDATOR = consumes -> consumes != null && !consumes.replace(" ", "").isEmpty() && (consumes.startsWith("http://") || consumes.startsWith("https://")) && consumes.contains(".") ? true : false;

    public static final ConsumingSupplier<String, Boolean> HEX_COLOR_TEXT_VALIDATOR = consumes -> consumes != null && !consumes.replace(" ", "").isEmpty() && DrawableColor.of(consumes) != DrawableColor.EMPTY;

    public static final ConsumingSupplier<String, Boolean> INTEGER_TEXT_VALIDATOR = consumes -> INTEGER_CHARACTER_FILTER.isAllowedText(consumes);

    public static final ConsumingSupplier<String, Boolean> DOUBLE_TEXT_VALIDATOR = consumes -> DOUBLE_CHARACTER_FILTER.isAllowedText(consumes);
}