package dev.ftb.mods.ftblibrary.util.client;

import dev.ftb.mods.ftblibrary.ui.Widget;
import java.util.Optional;
import net.minecraft.client.renderer.Rect2i;

public record PositionedIngredient(Object ingredient, Rect2i area, boolean tooltip) {

    public static Optional<PositionedIngredient> of(Object ingredient, Widget widget, boolean tooltip) {
        return Optional.of(new PositionedIngredient(ingredient, new Rect2i(widget.getPosX(), widget.getPosY(), widget.getWidth(), widget.getHeight()), tooltip));
    }

    public static Optional<PositionedIngredient> of(Object ingredient, Widget widget) {
        return of(ingredient, widget, false);
    }
}