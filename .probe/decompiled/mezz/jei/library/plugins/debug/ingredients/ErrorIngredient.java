package mezz.jei.library.plugins.debug.ingredients;

import mezz.jei.api.ingredients.IIngredientType;

public class ErrorIngredient {

    public static final IIngredientType<ErrorIngredient> TYPE = () -> ErrorIngredient.class;

    private final ErrorIngredient.CrashType crashType;

    public ErrorIngredient(ErrorIngredient.CrashType crashType) {
        this.crashType = crashType;
    }

    public ErrorIngredient.CrashType getCrashType() {
        return this.crashType;
    }

    public static enum CrashType {

        RenderBreakVertexBufferCrash, TooltipCrash
    }
}