package net.minecraft.network.chat.contents;

import java.util.Optional;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;

public record LiteralContents(String f_237368_) implements ComponentContents {

    private final String text;

    public LiteralContents(String f_237368_) {
        this.text = f_237368_;
    }

    @Override
    public <T> Optional<T> visit(FormattedText.ContentConsumer<T> p_237373_) {
        return p_237373_.accept(this.text);
    }

    @Override
    public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> p_237375_, Style p_237376_) {
        return p_237375_.accept(p_237376_, this.text);
    }

    public String toString() {
        return "literal{" + this.text + "}";
    }
}