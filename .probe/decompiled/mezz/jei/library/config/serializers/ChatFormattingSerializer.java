package mezz.jei.library.config.serializers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import mezz.jei.api.runtime.config.IJeiConfigListValueSerializer;
import mezz.jei.api.runtime.config.IJeiConfigValueSerializer;
import mezz.jei.common.config.file.serializers.DeserializeResult;
import net.minecraft.ChatFormatting;

public class ChatFormattingSerializer implements IJeiConfigListValueSerializer<ChatFormatting> {

    public static final ChatFormattingSerializer INSTANCE = new ChatFormattingSerializer();

    private static final ChatFormattingSerializer.ChatFormattingValueSerializer VALUE_SERIALIZER = new ChatFormattingSerializer.ChatFormattingValueSerializer();

    private static final EnumSet<ChatFormatting> INVALID_VALUES = EnumSet.of(ChatFormatting.RESET);

    private static final EnumSet<ChatFormatting> VALID_VALUES = EnumSet.complementOf(INVALID_VALUES);

    private ChatFormattingSerializer() {
    }

    public String serialize(List<ChatFormatting> value) {
        return (String) value.stream().map(VALUE_SERIALIZER::serialize).collect(Collectors.joining(" "));
    }

    public DeserializeResult<List<ChatFormatting>> deserialize(String string) {
        string = string.trim();
        if (string.startsWith("\"") && string.endsWith("\"")) {
            string = string.substring(1, string.length() - 1);
        }
        if (string.isEmpty()) {
            return new DeserializeResult<>(List.of());
        } else {
            List<String> errors = new ArrayList();
            String[] strings = string.split(" ");
            List<ChatFormatting> result = Arrays.stream(strings).mapMulti((s, c) -> {
                IJeiConfigValueSerializer.IDeserializeResult<ChatFormatting> deserializeResult = VALUE_SERIALIZER.deserialize(s);
                deserializeResult.getResult().ifPresent(c);
                errors.addAll(deserializeResult.getErrors());
            }).toList();
            return new DeserializeResult<>(result, errors);
        }
    }

    @Override
    public String getValidValuesDescription() {
        List<ChatFormatting> validColors = new ArrayList();
        List<ChatFormatting> validFormats = new ArrayList();
        for (ChatFormatting chatFormatting : VALID_VALUES) {
            if (chatFormatting.isColor()) {
                validColors.add(chatFormatting);
            } else if (chatFormatting.isFormat()) {
                validFormats.add(chatFormatting);
            }
        }
        return "A chat formatting string.\nUse these formatting colors:\n%s\nWith these formatting options:\n%s".formatted(this.serialize(validColors), this.serialize(validFormats));
    }

    public boolean isValid(List<ChatFormatting> value) {
        return value.stream().allMatch(VALUE_SERIALIZER::isValid);
    }

    @Override
    public IJeiConfigValueSerializer<ChatFormatting> getListValueSerializer() {
        return VALUE_SERIALIZER;
    }

    @Override
    public Optional<Collection<List<ChatFormatting>>> getAllValidValues() {
        return Optional.empty();
    }

    private static class ChatFormattingValueSerializer implements IJeiConfigValueSerializer<ChatFormatting> {

        public String serialize(ChatFormatting value) {
            return value.getName();
        }

        @Override
        public IJeiConfigValueSerializer.IDeserializeResult<ChatFormatting> deserialize(String string) {
            ChatFormatting chatFormatting = ChatFormatting.getByName(string);
            if (chatFormatting == null) {
                return new DeserializeResult<>(null, "No Chat Formatting found for name: '%s'".formatted(string));
            } else {
                return ChatFormattingSerializer.INVALID_VALUES.contains(chatFormatting) ? new DeserializeResult<>(null, "Chat Formatting '%s' is not valid".formatted(string)) : new DeserializeResult<>(chatFormatting);
            }
        }

        public boolean isValid(ChatFormatting value) {
            return ChatFormattingSerializer.VALID_VALUES.contains(value);
        }

        @Override
        public Optional<Collection<ChatFormatting>> getAllValidValues() {
            return Optional.of(ChatFormattingSerializer.VALID_VALUES);
        }

        @Override
        public String getValidValuesDescription() {
            String validValuesString = (String) ChatFormattingSerializer.VALID_VALUES.stream().map(this::serialize).collect(Collectors.joining(", "));
            return "A chat formatting string.\nUse any of these formatting values:\n%s".formatted(validValuesString);
        }
    }
}