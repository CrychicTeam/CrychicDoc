package net.minecraft.network.chat.contents;

import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;

public class KeybindContents implements ComponentContents {

    private final String name;

    @Nullable
    private Supplier<Component> nameResolver;

    public KeybindContents(String string0) {
        this.name = string0;
    }

    private Component getNestedComponent() {
        if (this.nameResolver == null) {
            this.nameResolver = (Supplier<Component>) KeybindResolver.keyResolver.apply(this.name);
        }
        return (Component) this.nameResolver.get();
    }

    @Override
    public <T> Optional<T> visit(FormattedText.ContentConsumer<T> formattedTextContentConsumerT0) {
        return this.getNestedComponent().visit(formattedTextContentConsumerT0);
    }

    @Override
    public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> formattedTextStyledContentConsumerT0, Style style1) {
        return this.getNestedComponent().visit(formattedTextStyledContentConsumerT0, style1);
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            if (object0 instanceof KeybindContents $$1 && this.name.equals($$1.name)) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return "keybind{" + this.name + "}";
    }

    public String getName() {
        return this.name;
    }
}