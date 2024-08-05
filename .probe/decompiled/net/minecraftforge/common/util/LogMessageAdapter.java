package net.minecraftforge.common.util;

import java.util.function.Consumer;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public record LogMessageAdapter(Consumer<StringBuilder> builder) implements Message, StringBuilderFormattable {

    private static final Object[] EMPTY = new Object[0];

    public String getFormattedMessage() {
        return "";
    }

    public String getFormat() {
        return "";
    }

    public Object[] getParameters() {
        return EMPTY;
    }

    public Throwable getThrowable() {
        return null;
    }

    public void formatTo(StringBuilder buffer) {
        this.builder.accept(buffer);
    }

    public static Message adapt(Consumer<StringBuilder> toConsume) {
        return new LogMessageAdapter(toConsume);
    }
}