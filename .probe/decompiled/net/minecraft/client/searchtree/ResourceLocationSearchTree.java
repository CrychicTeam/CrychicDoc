package net.minecraft.client.searchtree;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;

public interface ResourceLocationSearchTree<T> {

    static <T> ResourceLocationSearchTree<T> empty() {
        return new ResourceLocationSearchTree<T>() {

            @Override
            public List<T> searchNamespace(String p_235218_) {
                return List.of();
            }

            @Override
            public List<T> searchPath(String p_235220_) {
                return List.of();
            }
        };
    }

    static <T> ResourceLocationSearchTree<T> create(List<T> listT0, Function<T, Stream<ResourceLocation>> functionTStreamResourceLocation1) {
        if (listT0.isEmpty()) {
            return empty();
        } else {
            final SuffixArray<T> $$2 = new SuffixArray<>();
            final SuffixArray<T> $$3 = new SuffixArray<>();
            for (T $$4 : listT0) {
                ((Stream) functionTStreamResourceLocation1.apply($$4)).forEach(p_235210_ -> {
                    $$2.add($$4, p_235210_.getNamespace().toLowerCase(Locale.ROOT));
                    $$3.add($$4, p_235210_.getPath().toLowerCase(Locale.ROOT));
                });
            }
            $$2.generate();
            $$3.generate();
            return new ResourceLocationSearchTree<T>() {

                @Override
                public List<T> searchNamespace(String p_235227_) {
                    return $$2.search(p_235227_);
                }

                @Override
                public List<T> searchPath(String p_235229_) {
                    return $$3.search(p_235229_);
                }
            };
        }
    }

    List<T> searchNamespace(String var1);

    List<T> searchPath(String var1);
}