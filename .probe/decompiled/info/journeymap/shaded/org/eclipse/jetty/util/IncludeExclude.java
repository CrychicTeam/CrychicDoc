package info.journeymap.shaded.org.eclipse.jetty.util;

import java.util.Set;
import java.util.function.Predicate;

public class IncludeExclude<ITEM> extends IncludeExcludeSet<ITEM, ITEM> {

    public IncludeExclude() {
    }

    public <SET extends Set<ITEM>> IncludeExclude(Class<SET> setClass) {
        super(setClass);
    }

    public <SET extends Set<ITEM>> IncludeExclude(Set<ITEM> includeSet, Predicate<ITEM> includePredicate, Set<ITEM> excludeSet, Predicate<ITEM> excludePredicate) {
        super(includeSet, includePredicate, excludeSet, excludePredicate);
    }
}