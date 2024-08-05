package vectorwing.farmersdelight.common.utility;

import java.util.List;
import java.util.function.Function;

public class ListUtils {

    public static <F, T, L extends List<T>> L mapArrayIndexSet(F[] array, Function<F, T> mapper, L list) {
        int i = 0;
        for (F f : array) {
            list.set(i++, mapper.apply(f));
        }
        return list;
    }
}