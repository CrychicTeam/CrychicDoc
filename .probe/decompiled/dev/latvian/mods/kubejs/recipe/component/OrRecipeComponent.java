package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.recipe.InputReplacement;
import dev.latvian.mods.kubejs.recipe.OutputReplacement;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.Optional;

public record OrRecipeComponent<H, L>(RecipeComponent<H> high, RecipeComponent<L> low) implements RecipeComponent<Either<H, L>> {

    @Override
    public String componentType() {
        return "or";
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return this.high.constructorDescription(ctx).or(this.low.constructorDescription(ctx));
    }

    @Override
    public ComponentRole role() {
        return this.high.role().isOther() ? this.low.role() : this.high.role();
    }

    @Override
    public Class<?> componentClass() {
        return Either.class;
    }

    public JsonElement write(RecipeJS recipe, Either<H, L> value) {
        return value.left().isPresent() ? this.high.write(recipe, (H) value.left().get()) : this.low.write(recipe, (L) value.right().orElseThrow());
    }

    public Either<H, L> read(RecipeJS recipe, Object from) {
        if (this.high.hasPriority(recipe, from)) {
            return Either.left(this.high.read(recipe, from));
        } else if (this.low.hasPriority(recipe, from)) {
            return Either.right(this.low.read(recipe, from));
        } else {
            try {
                return Either.left(this.high.read(recipe, from));
            } catch (Exception var6) {
                try {
                    return Either.right(this.low.read(recipe, from));
                } catch (Exception var5) {
                    ConsoleJS.SERVER.error("Failed to read %s as high priority (%s)!".formatted(from, this.high), var6);
                    ConsoleJS.SERVER.error("Failed to read %s as low priority (%s)!".formatted(from, this.low), var5);
                    throw new RecipeExceptionJS("Failed to read %s as either %s or %s!".formatted(from, this.high, this.low));
                }
            }
        }
    }

    public boolean isInput(RecipeJS recipe, Either<H, L> value, ReplacementMatch match) {
        Optional<H> l = value.left();
        return l.isPresent() ? this.high.isInput(recipe, (H) l.get(), match) : this.low.isInput(recipe, (L) value.right().get(), match);
    }

    public Either<H, L> replaceInput(RecipeJS recipe, Either<H, L> original, ReplacementMatch match, InputReplacement with) {
        Optional<H> l = original.left();
        if (l.isPresent()) {
            H r = this.high.replaceInput(recipe, (H) l.get(), match, with);
            return r == l.get() ? original : Either.left(r);
        } else {
            L r = this.low.replaceInput(recipe, (L) original.right().get(), match, with);
            return r == original.right().get() ? original : Either.right(r);
        }
    }

    public boolean isOutput(RecipeJS recipe, Either<H, L> value, ReplacementMatch match) {
        Optional<H> l = value.left();
        return l.isPresent() ? this.high.isOutput(recipe, (H) l.get(), match) : this.low.isOutput(recipe, (L) value.right().get(), match);
    }

    public Either<H, L> replaceOutput(RecipeJS recipe, Either<H, L> original, ReplacementMatch match, OutputReplacement with) {
        Optional<H> l = original.left();
        if (l.isPresent()) {
            H r = this.high.replaceOutput(recipe, (H) l.get(), match, with);
            return r == l.get() ? original : Either.left(r);
        } else {
            L r = this.low.replaceOutput(recipe, (L) original.right().get(), match, with);
            return r == original.right().get() ? original : Either.right(r);
        }
    }

    public boolean checkValueHasChanged(Either<H, L> oldValue, Either<H, L> newValue) {
        if (oldValue != null && newValue != null) {
            Optional<H> left = oldValue.left();
            if (left.isPresent()) {
                if (this.high.checkValueHasChanged((H) left.get(), (H) newValue.left().get())) {
                    return true;
                }
            } else if (this.low.checkValueHasChanged((L) oldValue.right().get(), (L) newValue.right().get())) {
                return true;
            }
        }
        return oldValue != newValue;
    }

    public String toString() {
        return "{" + this.high + "|" + this.low + "}";
    }
}