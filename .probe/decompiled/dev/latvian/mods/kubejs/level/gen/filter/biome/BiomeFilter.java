package dev.latvian.mods.kubejs.level.gen.filter.biome;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.regexp.NativeRegExp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface BiomeFilter extends Predicate<BiomeModifications.BiomeContext> {

    BiomeFilter ALWAYS_TRUE = ctx -> true;

    BiomeFilter ALWAYS_FALSE = ctx -> false;

    boolean test(BiomeModifications.BiomeContext var1);

    static BiomeFilter of(Context cx, @Nullable Object o) {
        if (o == null || o == ALWAYS_TRUE) {
            return ALWAYS_TRUE;
        } else if (o == ALWAYS_FALSE) {
            return ALWAYS_FALSE;
        } else if (!(o instanceof CharSequence) && !(o instanceof NativeRegExp) && !(o instanceof Pattern)) {
            List<?> list = ListJS.orSelf(o);
            if (list.isEmpty()) {
                return ALWAYS_FALSE;
            } else if (list.size() > 1) {
                ArrayList<BiomeFilter> filters = new ArrayList();
                for (Object o1 : list) {
                    BiomeFilter filter = of(cx, o1);
                    if (filter == ALWAYS_TRUE) {
                        return ALWAYS_TRUE;
                    }
                    if (filter != ALWAYS_FALSE) {
                        filters.add(filter);
                    }
                }
                return (BiomeFilter) (filters.isEmpty() ? ALWAYS_FALSE : (filters.size() == 1 ? (BiomeFilter) filters.get(0) : new OrFilter(filters)));
            } else {
                Map<?, ?> map = MapJS.of(list.get(0));
                if (map != null && !map.isEmpty()) {
                    ArrayList<BiomeFilter> filters = new ArrayList();
                    if (map.get("or") != null) {
                        filters.add(of(cx, map.get("or")));
                    }
                    if (map.get("not") != null) {
                        filters.add(new NotFilter(of(cx, map.get("not"))));
                    }
                    try {
                        if (map.get("id") != null) {
                            filters.add(idFilter(cx, map.get("id").toString()));
                        }
                        if (map.get("type") != null) {
                            filters.add(idFilter(cx, map.get("type").toString()));
                        }
                        if (map.get("tag") != null) {
                            filters.add(new TagFilter(map.get("tag").toString()));
                        }
                    } catch (Exception var7) {
                        ConsoleJS.getCurrent(cx).error("Error trying to create BiomeFilter: " + var7.getMessage());
                        return ALWAYS_FALSE;
                    }
                    return (BiomeFilter) (filters.isEmpty() ? ALWAYS_TRUE : (filters.size() == 1 ? (BiomeFilter) filters.get(0) : new AndFilter(filters)));
                } else {
                    return ALWAYS_TRUE;
                }
            }
        } else {
            return idFilter(cx, o.toString());
        }
    }

    static BiomeFilter idFilter(Context cx, String s) {
        if (s.equals("*")) {
            return ALWAYS_TRUE;
        } else if (s.equals("-")) {
            return ALWAYS_FALSE;
        } else {
            Pattern pattern = UtilsJS.parseRegex(s);
            if (pattern != null) {
                return new RegexIDFilter(pattern);
            } else {
                return (BiomeFilter) (s.charAt(0) == '#' ? new TagFilter(s.substring(1)) : new IDFilter(UtilsJS.getMCID(cx, s)));
            }
        }
    }
}