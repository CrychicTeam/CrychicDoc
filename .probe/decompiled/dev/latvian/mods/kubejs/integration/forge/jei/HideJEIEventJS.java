package dev.latvian.mods.kubejs.integration.forge.jei;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.runtime.IJeiRuntime;

public class HideJEIEventJS<T> extends EventJS {

    private final IJeiRuntime runtime;

    private final IIngredientType<T> type;

    private final Function<Object, Predicate<T>> function;

    private final HashSet<T> hidden;

    private final Predicate<T> isValid;

    private final Collection<T> allIngredients;

    public HideJEIEventJS(IJeiRuntime r, IIngredientType<T> t, Function<Object, Predicate<T>> f, Predicate<T> i) {
        this.runtime = r;
        this.type = t;
        this.function = f;
        this.hidden = new HashSet();
        this.isValid = i;
        this.allIngredients = this.runtime.getIngredientManager().getAllIngredients(this.type);
    }

    public Collection<T> getAllIngredients() {
        return this.allIngredients;
    }

    public void hide(Object o) {
        Predicate<T> p = (Predicate<T>) this.function.apply(o);
        for (T value : this.allIngredients) {
            if (p.test(value)) {
                this.hidden.add(value);
            }
        }
    }

    public void hideAll() {
        this.hidden.addAll(this.allIngredients);
    }

    @Override
    protected void afterPosted(EventResult result) {
        if (!this.hidden.isEmpty()) {
            this.runtime.getIngredientManager().removeIngredientsAtRuntime(this.type, this.hidden.stream().filter(this.isValid).toList());
        }
    }
}