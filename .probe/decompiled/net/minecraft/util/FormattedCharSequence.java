package net.minecraft.util;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import java.util.List;
import net.minecraft.network.chat.Style;

@FunctionalInterface
public interface FormattedCharSequence {

    FormattedCharSequence EMPTY = p_13704_ -> true;

    boolean accept(FormattedCharSink var1);

    static FormattedCharSequence codepoint(int int0, Style style1) {
        return p_13730_ -> p_13730_.accept(0, style1, int0);
    }

    static FormattedCharSequence forward(String string0, Style style1) {
        return string0.isEmpty() ? EMPTY : p_13739_ -> StringDecomposer.iterate(string0, style1, p_13739_);
    }

    static FormattedCharSequence forward(String string0, Style style1, Int2IntFunction intIntFunction2) {
        return string0.isEmpty() ? EMPTY : p_144730_ -> StringDecomposer.iterate(string0, style1, decorateOutput(p_144730_, intIntFunction2));
    }

    static FormattedCharSequence backward(String string0, Style style1) {
        return string0.isEmpty() ? EMPTY : p_144716_ -> StringDecomposer.iterateBackwards(string0, style1, p_144716_);
    }

    static FormattedCharSequence backward(String string0, Style style1, Int2IntFunction intIntFunction2) {
        return string0.isEmpty() ? EMPTY : p_13721_ -> StringDecomposer.iterateBackwards(string0, style1, decorateOutput(p_13721_, intIntFunction2));
    }

    static FormattedCharSink decorateOutput(FormattedCharSink formattedCharSink0, Int2IntFunction intIntFunction1) {
        return (p_13711_, p_13712_, p_13713_) -> formattedCharSink0.accept(p_13711_, p_13712_, (Integer) intIntFunction1.apply(p_13713_));
    }

    static FormattedCharSequence composite() {
        return EMPTY;
    }

    static FormattedCharSequence composite(FormattedCharSequence formattedCharSequence0) {
        return formattedCharSequence0;
    }

    static FormattedCharSequence composite(FormattedCharSequence formattedCharSequence0, FormattedCharSequence formattedCharSequence1) {
        return fromPair(formattedCharSequence0, formattedCharSequence1);
    }

    static FormattedCharSequence composite(FormattedCharSequence... formattedCharSequence0) {
        return fromList(ImmutableList.copyOf(formattedCharSequence0));
    }

    static FormattedCharSequence composite(List<FormattedCharSequence> listFormattedCharSequence0) {
        int $$1 = listFormattedCharSequence0.size();
        switch($$1) {
            case 0:
                return EMPTY;
            case 1:
                return (FormattedCharSequence) listFormattedCharSequence0.get(0);
            case 2:
                return fromPair((FormattedCharSequence) listFormattedCharSequence0.get(0), (FormattedCharSequence) listFormattedCharSequence0.get(1));
            default:
                return fromList(ImmutableList.copyOf(listFormattedCharSequence0));
        }
    }

    static FormattedCharSequence fromPair(FormattedCharSequence formattedCharSequence0, FormattedCharSequence formattedCharSequence1) {
        return p_13702_ -> formattedCharSequence0.accept(p_13702_) && formattedCharSequence1.accept(p_13702_);
    }

    static FormattedCharSequence fromList(List<FormattedCharSequence> listFormattedCharSequence0) {
        return p_13726_ -> {
            for (FormattedCharSequence $$2 : listFormattedCharSequence0) {
                if (!$$2.accept(p_13726_)) {
                    return false;
                }
            }
            return true;
        };
    }
}