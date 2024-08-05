package dev.latvian.mods.kubejs.integration.forge.jei;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.util.ListJS;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.runtime.IJeiRuntime;

public class AddJEIEventJS<T> extends EventJS {

    private final IJeiRuntime runtime;

    private final IIngredientType<T> type;

    private final Function<Object, T> function;

    private final Collection<T> added;

    private final Predicate<T> isValid;

    public AddJEIEventJS(IJeiRuntime r, IIngredientType<T> t, Function<Object, T> f, Predicate<T> i) {
        this.runtime = r;
        this.type = t;
        this.function = f;
        this.added = new ArrayList();
        this.isValid = i;
    }

    public void add(Object o) {
        for (Object o1 : ListJS.orSelf(o)) {
            T t = (T) this.function.apply(o1);
            if (t != null) {
                this.added.add(t);
            }
        }
    }

    @Override
    protected void afterPosted(EventResult result) {
        if (!this.added.isEmpty()) {
            List<T> items = (List<T>) this.added.stream().filter(this.isValid).collect(Collectors.toList());
            this.runtime.getIngredientManager().addIngredientsAtRuntime(this.type, items);
        }
    }
}