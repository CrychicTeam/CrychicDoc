package dev.latvian.mods.kubejs.recipe;

import dev.latvian.mods.kubejs.util.MutedError;

public class RecipeExceptionJS extends IllegalArgumentException implements MutedError {

    public boolean error = false;

    public RecipeExceptionJS(String m) {
        super(m);
    }

    public RecipeExceptionJS(String m, Throwable cause) {
        super(m, cause);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getMessage());
        if (this.error) {
            sb.append(" [error]");
        }
        if (this.getCause() != null) {
            sb.append("\ncause: ");
            sb.append(this.getCause());
        }
        return sb.toString();
    }

    public RecipeExceptionJS error() {
        this.error = true;
        return this;
    }
}