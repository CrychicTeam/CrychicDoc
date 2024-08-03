package net.minecraft.client.searchtree;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;

public interface PlainTextSearchTree<T> {

    static <T> PlainTextSearchTree<T> empty() {
        return p_235196_ -> List.of();
    }

    static <T> PlainTextSearchTree<T> create(List<T> listT0, Function<T, Stream<String>> functionTStreamString1) {
        if (listT0.isEmpty()) {
            return empty();
        } else {
            SuffixArray<T> $$2 = new SuffixArray<>();
            for (T $$3 : listT0) {
                ((Stream) functionTStreamString1.apply($$3)).forEach(p_235194_ -> $$2.add($$3, p_235194_.toLowerCase(Locale.ROOT)));
            }
            $$2.generate();
            return $$2::m_119973_;
        }
    }

    List<T> search(String var1);
}