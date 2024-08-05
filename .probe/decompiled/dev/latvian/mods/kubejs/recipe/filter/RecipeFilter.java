package dev.latvian.mods.kubejs.recipe.filter;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.latvian.mods.kubejs.core.RecipeKJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.regexp.NativeRegExp;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface RecipeFilter extends Predicate<RecipeKJS> {

    Event<RecipeFilterParseEvent> PARSE = EventFactory.createLoop();

    boolean test(RecipeKJS var1);

    static RecipeFilter of(Context cx, @Nullable Object o) {
        if (o == null || o == ConstantFilter.TRUE) {
            return ConstantFilter.TRUE;
        } else if (o == ConstantFilter.FALSE) {
            return ConstantFilter.FALSE;
        } else if (!(o instanceof CharSequence) && !(o instanceof NativeRegExp) && !(o instanceof Pattern)) {
            List<?> list = ListJS.orSelf(o);
            if (list.isEmpty()) {
                return ConstantFilter.FALSE;
            } else if (list.size() > 1) {
                OrFilter predicate = new OrFilter();
                for (Object o1 : list) {
                    RecipeFilter p = of(cx, o1);
                    if (p == ConstantFilter.TRUE) {
                        return ConstantFilter.TRUE;
                    }
                    if (p != ConstantFilter.FALSE) {
                        predicate.list.add(p);
                    }
                }
                return (RecipeFilter) (predicate.list.isEmpty() ? ConstantFilter.FALSE : (predicate.list.size() == 1 ? (RecipeFilter) predicate.list.get(0) : predicate));
            } else {
                Map<?, ?> map = MapJS.of(list.get(0));
                if (map != null && !map.isEmpty()) {
                    AndFilter predicate = new AndFilter();
                    if (map.get("or") != null) {
                        predicate.list.add(of(cx, map.get("or")));
                    }
                    if (map.get("not") != null) {
                        predicate.list.add(new NotFilter(of(cx, map.get("not"))));
                    }
                    try {
                        Object id = map.get("id");
                        if (id != null) {
                            Pattern pattern = UtilsJS.parseRegex(id);
                            predicate.list.add(pattern == null ? new IDFilter(UtilsJS.getMCID(cx, id)) : RegexIDFilter.of(pattern));
                        }
                        Object type = map.get("type");
                        if (type != null) {
                            predicate.list.add(new TypeFilter(UtilsJS.getMCID(cx, type)));
                        }
                        Object group = map.get("group");
                        if (group != null) {
                            predicate.list.add(new GroupFilter(group.toString()));
                        }
                        Object mod = map.get("mod");
                        if (mod != null) {
                            predicate.list.add(new ModFilter(mod.toString()));
                        }
                        Object input = map.get("input");
                        if (input != null) {
                            predicate.list.add(new InputFilter(ReplacementMatch.of(input)));
                        }
                        Object output = map.get("output");
                        if (output != null) {
                            predicate.list.add(new OutputFilter(ReplacementMatch.of(output)));
                        }
                        PARSE.invoker().parse(cx, predicate.list, map);
                        return (RecipeFilter) (predicate.list.isEmpty() ? ConstantFilter.TRUE : (predicate.list.size() == 1 ? (RecipeFilter) predicate.list.get(0) : predicate));
                    } catch (RecipeExceptionJS var11) {
                        if (var11.error) {
                            ConsoleJS.getCurrent(cx).error(var11.getMessage());
                        } else {
                            ConsoleJS.getCurrent(cx).warn(var11.getMessage());
                        }
                        return ConstantFilter.FALSE;
                    }
                } else {
                    return ConstantFilter.TRUE;
                }
            }
        } else {
            String s = o.toString();
            if (s.equals("*")) {
                return ConstantFilter.TRUE;
            } else if (s.equals("-")) {
                return ConstantFilter.FALSE;
            } else {
                Pattern r = UtilsJS.parseRegex(s);
                return (RecipeFilter) (r == null ? new IDFilter(UtilsJS.getMCID(cx, s)) : RegexIDFilter.of(r));
            }
        }
    }
}