package de.keksuccino.fancymenu.util.rendering.text.color;

import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class DynamicTextColorFormatter extends TextColorFormatter {

    protected Supplier<DrawableColor> colorSupplier;

    public DynamicTextColorFormatter(char code, @NotNull Supplier<DrawableColor> colorSupplier) {
        super(code, DrawableColor.WHITE);
        this.colorSupplier = colorSupplier;
    }

    @Override
    public DrawableColor getColor() {
        return (DrawableColor) this.colorSupplier.get();
    }
}