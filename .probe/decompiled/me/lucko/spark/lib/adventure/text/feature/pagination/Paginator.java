package me.lucko.spark.lib.adventure.text.feature.pagination;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.function.ObjIntConsumer;
import org.jetbrains.annotations.NotNull;

interface Paginator {

    static <T> void forEachPageEntry(@NotNull final Collection<? extends T> content, final int pageSize, final int page, @NotNull final ObjIntConsumer<? super T> consumer) {
        int size = content.size();
        int start = pageSize * (page - 1);
        int end = pageSize * page;
        if (content instanceof List && content instanceof RandomAccess) {
            List<? extends T> list = (List<? extends T>) content;
            for (int i = start; i < end && i < size; i++) {
                consumer.accept(list.get(i), i);
            }
        } else {
            Iterator<? extends T> it = content.iterator();
            for (int i = 0; i < start; i++) {
                it.next();
            }
            for (int i = start; i < end && i < size; i++) {
                consumer.accept(it.next(), i);
            }
        }
    }
}