package snownee.jade.gui.config.value;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class InputOptionValue<T> extends OptionValue<T> {

    public static final Predicate<String> INTEGER = s -> s.matches("[-+]?[0-9]+");

    public static final Predicate<String> FLOAT = s -> s.matches("[-+]?([0-9]*\\.[0-9]+|[0-9]+)");

    private final EditBox textField;

    private final Predicate<String> validator;

    public InputOptionValue(Runnable responder, String optionName, T value, Consumer<T> setter, Predicate<String> validator) {
        super(optionName, setter);
        this.value = value;
        this.validator = validator;
        this.textField = new EditBox(this.client.font, 0, 0, 98, 18, Component.literal(""));
        this.textField.setValue(String.valueOf(value));
        this.textField.setResponder(s -> {
            if (this.validator.test(s)) {
                this.setValue(s);
                this.textField.setTextColor(ChatFormatting.WHITE.getColor());
            } else {
                this.textField.setTextColor(ChatFormatting.RED.getColor());
            }
            responder.run();
        });
        this.addWidget(this.textField, 0);
    }

    private void setValue(String text) {
        if (this.value instanceof String) {
            this.value = (T) text;
        }
        try {
            if (this.value instanceof Integer) {
                this.value = (T) Integer.valueOf(text);
            } else if (this.value instanceof Short) {
                this.value = (T) Short.valueOf(text);
            } else if (this.value instanceof Byte) {
                this.value = (T) Byte.valueOf(text);
            } else if (this.value instanceof Long) {
                this.value = (T) Long.valueOf(text);
            } else if (this.value instanceof Double) {
                this.value = (T) Double.valueOf(text);
            } else if (this.value instanceof Float) {
                this.value = (T) Float.valueOf(text);
            }
        } catch (NumberFormatException var3) {
        }
        this.save();
    }

    @Override
    public boolean isValidValue() {
        return this.validator.test(this.textField.getValue());
    }

    @Override
    public void setValue(T value) {
        this.textField.setValue(String.valueOf(value));
    }
}