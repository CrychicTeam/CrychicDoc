package net.minecraft.client.gui.narration;

import com.google.common.collect.Maps;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Consumer;

public class ScreenNarrationCollector {

    int generation;

    final Map<ScreenNarrationCollector.EntryKey, ScreenNarrationCollector.NarrationEntry> entries = Maps.newTreeMap(Comparator.comparing(p_169196_ -> p_169196_.type).thenComparing(p_169185_ -> p_169185_.depth));

    public void update(Consumer<NarrationElementOutput> consumerNarrationElementOutput0) {
        this.generation++;
        consumerNarrationElementOutput0.accept(new ScreenNarrationCollector.Output(0));
    }

    public String collectNarrationText(boolean boolean0) {
        final StringBuilder $$1 = new StringBuilder();
        Consumer<String> $$2 = new Consumer<String>() {

            private boolean firstEntry = true;

            public void accept(String p_169204_) {
                if (!this.firstEntry) {
                    $$1.append(". ");
                }
                this.firstEntry = false;
                $$1.append(p_169204_);
            }
        };
        this.entries.forEach((p_169193_, p_169194_) -> {
            if (p_169194_.generation == this.generation && (boolean0 || !p_169194_.alreadyNarrated)) {
                p_169194_.contents.getText($$2);
                p_169194_.alreadyNarrated = true;
            }
        });
        return $$1.toString();
    }

    static class EntryKey {

        final NarratedElementType type;

        final int depth;

        EntryKey(NarratedElementType narratedElementType0, int int1) {
            this.type = narratedElementType0;
            this.depth = int1;
        }
    }

    static class NarrationEntry {

        NarrationThunk<?> contents = NarrationThunk.EMPTY;

        int generation = -1;

        boolean alreadyNarrated;

        public ScreenNarrationCollector.NarrationEntry update(int int0, NarrationThunk<?> narrationThunk1) {
            if (!this.contents.equals(narrationThunk1)) {
                this.contents = narrationThunk1;
                this.alreadyNarrated = false;
            } else if (this.generation + 1 != int0) {
                this.alreadyNarrated = false;
            }
            this.generation = int0;
            return this;
        }
    }

    class Output implements NarrationElementOutput {

        private final int depth;

        Output(int int0) {
            this.depth = int0;
        }

        @Override
        public void add(NarratedElementType narratedElementType0, NarrationThunk<?> narrationThunk1) {
            ((ScreenNarrationCollector.NarrationEntry) ScreenNarrationCollector.this.entries.computeIfAbsent(new ScreenNarrationCollector.EntryKey(narratedElementType0, this.depth), p_169229_ -> new ScreenNarrationCollector.NarrationEntry())).update(ScreenNarrationCollector.this.generation, narrationThunk1);
        }

        @Override
        public NarrationElementOutput nest() {
            return ScreenNarrationCollector.this.new Output(this.depth + 1);
        }
    }
}