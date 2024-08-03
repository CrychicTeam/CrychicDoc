package net.minecraft.server.network;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TextFilter {

    TextFilter DUMMY = new TextFilter() {

        @Override
        public void join() {
        }

        @Override
        public void leave() {
        }

        @Override
        public CompletableFuture<FilteredText> processStreamMessage(String p_143708_) {
            return CompletableFuture.completedFuture(FilteredText.passThrough(p_143708_));
        }

        @Override
        public CompletableFuture<List<FilteredText>> processMessageBundle(List<String> p_143710_) {
            return CompletableFuture.completedFuture((List) p_143710_.stream().map(FilteredText::m_243054_).collect(ImmutableList.toImmutableList()));
        }
    };

    void join();

    void leave();

    CompletableFuture<FilteredText> processStreamMessage(String var1);

    CompletableFuture<List<FilteredText>> processMessageBundle(List<String> var1);
}