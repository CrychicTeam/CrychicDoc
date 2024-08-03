package icyllis.arc3d.compiler.parser;

import it.unimi.dsi.fastutil.ints.IntList;
import java.util.BitSet;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

@FunctionalInterface
public interface RegexNode {

    @Unmodifiable
    IntList transition(NFA var1, @Unmodifiable IntList var2);

    @Nonnull
    static RegexNode Char(char c) {
        return new RegexNode.Char(c);
    }

    @Nonnull
    static RegexNode Range(char start, char end) {
        return new RegexNode.Range(start, end);
    }

    @Nonnull
    static RegexNode Range(RegexNode start, RegexNode end) {
        try {
            return new RegexNode.Range(((RegexNode.Char) start).mChar, ((RegexNode.Char) end).mChar);
        } catch (ClassCastException var3) {
            throw new IllegalStateException("character range contains non-literal characters", var3);
        }
    }

    @Nonnull
    static RegexNode CharClass(RegexNode... clazz) {
        return new RegexNode.CharClass(clazz, false);
    }

    @Nonnull
    static RegexNode CharClass(RegexNode[] clazz, boolean exclusive) {
        return new RegexNode.CharClass(clazz, exclusive);
    }

    @Nonnull
    @Contract(pure = true)
    static RegexNode Concat(RegexNode x, RegexNode y) {
        return (nfa, next) -> x.transition(nfa, y.transition(nfa, next));
    }

    @Nonnull
    @Contract(pure = true)
    static RegexNode Union(RegexNode x, RegexNode y) {
        return (nfa, next) -> {
            IntList xn = x.transition(nfa, next);
            IntList yn = y.transition(nfa, next);
            int[] result = new int[xn.size() + yn.size()];
            xn.getElements(0, result, 0, xn.size());
            yn.getElements(0, result, xn.size(), yn.size());
            return IntList.of(result);
        };
    }

    @Nonnull
    @Contract(pure = true)
    static RegexNode Dot() {
        return (nfa, next) -> {
            int state = nfa.add(NFAState.Filter(ch -> ch != 10 && ch != 13, next));
            return IntList.of(state);
        };
    }

    @Nonnull
    @Contract(pure = true)
    static RegexNode Star(RegexNode x) {
        return (nfa, next) -> {
            int[] loop = new int[next.size() + 1];
            next.getElements(0, loop, 0, next.size());
            int state = nfa.add((NFAState) null);
            loop[next.size()] = state;
            IntList left = x.transition(nfa, IntList.of(loop));
            int[] result = new int[left.size() + next.size()];
            left.getElements(0, result, 0, left.size());
            next.getElements(0, result, left.size(), next.size());
            return nfa.replace(state, IntList.of(result));
        };
    }

    @Nonnull
    @Contract(pure = true)
    static RegexNode Plus(RegexNode x) {
        return (nfa, next) -> {
            int[] loop = new int[next.size() + 1];
            next.getElements(0, loop, 0, next.size());
            int state = nfa.add((NFAState) null);
            loop[next.size()] = state;
            IntList result = x.transition(nfa, IntList.of(loop));
            return nfa.replace(state, result);
        };
    }

    @Nonnull
    @Contract(pure = true)
    static RegexNode Ques(RegexNode x) {
        return (nfa, next) -> {
            IntList left = x.transition(nfa, next);
            int[] result = new int[left.size() + next.size()];
            left.getElements(0, result, 0, left.size());
            next.getElements(0, result, left.size(), next.size());
            return IntList.of(result);
        };
    }

    public static class Char implements RegexNode {

        public final char mChar;

        private Char(char c) {
            this.mChar = c;
        }

        @Override
        public IntList transition(NFA nfa, @Unmodifiable IntList next) {
            int state = nfa.add(NFAState.Filter(ch -> ch == this.mChar, next));
            return IntList.of(state);
        }

        public String toString() {
            return "Char(0x" + Integer.toHexString(this.mChar) + ")";
        }
    }

    public static class CharClass extends BitSet implements RegexNode {

        public final boolean mExclusive;

        private CharClass(RegexNode[] clazz, boolean exclusive) {
            this.mExclusive = exclusive;
            for (RegexNode x : clazz) {
                if (x instanceof RegexNode.Char node) {
                    this.set(node.mChar);
                } else if (x instanceof RegexNode.Range node) {
                    this.set(node.mStart, node.mEnd + 1);
                } else {
                    if (!(x instanceof RegexNode.CharClass)) {
                        throw new AssertionError(x);
                    }
                    RegexNode.CharClass node = (RegexNode.CharClass) x;
                    if (node.mExclusive) {
                        assert false;
                        this.xor(node);
                    } else {
                        this.or(node);
                    }
                }
            }
        }

        @Override
        public IntList transition(NFA nfa, @Unmodifiable IntList next) {
            int state = nfa.add(NFAState.Filter(ch -> this.get(ch) ^ this.mExclusive, next));
            return IntList.of(state);
        }

        public String toString() {
            return "CharClass(" + (this.mExclusive ? "^" : "") + super.toString() + ")";
        }
    }

    public static class Range implements RegexNode {

        public final char mStart;

        public final char mEnd;

        private Range(char start, char end) {
            if (start > end) {
                throw new IllegalStateException(String.format("character range '%c'-'%c' is out of order", start, end));
            } else {
                this.mStart = start;
                this.mEnd = end;
            }
        }

        @Override
        public IntList transition(NFA nfa, @Unmodifiable IntList next) {
            int state = nfa.add(NFAState.Filter(ch -> ch >= this.mStart && ch <= this.mEnd, next));
            return IntList.of(state);
        }

        public String toString() {
            return "Range(0x" + Integer.toHexString(this.mStart) + ", 0x" + Integer.toHexString(this.mEnd) + ")";
        }
    }
}