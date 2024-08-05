package icyllis.arc3d.compiler.parser;

import it.unimi.dsi.fastutil.ints.IntList;
import java.util.function.IntPredicate;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Unmodifiable;

public interface NFAState {

    boolean accept(char var1);

    @Unmodifiable
    IntList next();

    @Nonnull
    static NFAState Accept(int token) {
        return new NFAState.Accept(token);
    }

    @Nonnull
    static NFAState Filter(IntPredicate filter, @Unmodifiable IntList next) {
        return new NFAState.Filter(filter, next);
    }

    @Nonnull
    static NFAState Replace(@Unmodifiable IntList shadow) {
        return new NFAState.Replace(shadow);
    }

    public static class Accept implements NFAState {

        public final int mToken;

        private Accept(int token) {
            this.mToken = token;
        }

        @Override
        public boolean accept(char c) {
            return false;
        }

        @Override
        public IntList next() {
            return IntList.of();
        }
    }

    public static class Filter implements NFAState {

        private final IntPredicate mFilter;

        @Unmodifiable
        private final IntList mNext;

        private Filter(IntPredicate filter, @Unmodifiable IntList next) {
            this.mFilter = filter;
            this.mNext = next;
        }

        @Override
        public boolean accept(char c) {
            return this.mFilter.test(c);
        }

        @Override
        public IntList next() {
            return this.mNext;
        }
    }

    public static class Replace implements NFAState {

        @Unmodifiable
        public final IntList mShadow;

        private Replace(@Unmodifiable IntList shadow) {
            this.mShadow = shadow;
        }

        @Override
        public boolean accept(char c) {
            throw new IllegalStateException();
        }

        @Override
        public IntList next() {
            throw new IllegalStateException();
        }
    }
}