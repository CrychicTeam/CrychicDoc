package dev.latvian.mods.kubejs.integration.forge.jei;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.BaseFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

public class HideCustomJEIEventJS extends EventJS {

    private final IJeiRuntime runtime;

    private final HashMap<IIngredientType<?>, HideJEIEventJS<?>> events;

    public HideCustomJEIEventJS(IJeiRuntime r) {
        this.runtime = r;
        this.events = new HashMap();
    }

    public HideJEIEventJS get(IIngredientType s) {
        return (HideJEIEventJS) this.events.computeIfAbsent(s, type -> new HideJEIEventJS(this.runtime, type, o -> {
            Function<Object, String> idFn = it -> this.runtime.getIngredientManager().getIngredientHelper(UtilsJS.cast(type)).getUniqueId(it, UidContext.Ingredient);
            List<Predicate> predicates = new ArrayList();
            for (Object o1 : ListJS.orSelf(o)) {
                Pattern regex = UtilsJS.parseRegex(o1);
                if (regex != null) {
                    predicates.add((Predicate) it -> regex.asPredicate().test((String) idFn.apply(it)));
                } else if (o1 instanceof Predicate p) {
                    predicates.add(p);
                } else if (o instanceof BaseFunction f) {
                    predicates.add((Predicate) UtilsJS.makeFunctionProxy(ScriptType.CLIENT, Predicate.class, f));
                } else if (!(o1 instanceof CharSequence) && !(o1 instanceof ResourceLocation)) {
                    predicates.add(Predicate.isEqual(o1));
                } else {
                    predicates.add((Predicate) it -> Objects.equals(idFn.apply(it), o1.toString()));
                }
            }
            return (Predicate) it -> {
                for (Predicate px : predicates) {
                    if (px.test(it)) {
                        return true;
                    }
                }
                return false;
            };
        }, o -> true));
    }

    @Override
    protected void afterPosted(EventResult result) {
        for (HideJEIEventJS<?> eventJS : this.events.values()) {
            eventJS.afterPosted(result);
        }
    }
}