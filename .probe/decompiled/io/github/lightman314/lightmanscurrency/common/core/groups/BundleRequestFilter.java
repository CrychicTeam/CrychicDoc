package io.github.lightman314.lightmanscurrency.common.core.groups;

import io.github.lightman314.lightmanscurrency.common.core.variants.IOptionalKey;
import java.util.function.Function;

public enum BundleRequestFilter {

    ALL(o -> true), VANILLA(o -> o instanceof IOptionalKey ok ? ok.isVanilla() : true), MODDED(o -> o instanceof IOptionalKey ok ? ok.isModded() : true);

    private final Function<Object, Boolean> filter;

    private BundleRequestFilter(Function<Object, Boolean> filter) {
        this.filter = filter;
    }

    public final boolean filterKey(Object key) {
        return (Boolean) this.filter.apply(key);
    }
}