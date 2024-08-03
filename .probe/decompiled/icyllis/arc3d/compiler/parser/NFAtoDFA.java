package icyllis.arc3d.compiler.parser;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Unmodifiable;

public class NFAtoDFA {

    public static final char START_CHAR = '\t';

    public static final char END_CHAR = '~';

    private final NFA mNFA;

    private final Map<IntList, DFAState> mStates = new HashMap();

    private final List<IntList> mTransitions = new ArrayList();

    private final IntList mCharMappings = new IntArrayList();

    private final IntList mAccepts = new IntArrayList();

    public NFAtoDFA(NFA NFA) {
        this.mNFA = NFA;
    }

    @Nonnull
    public DFA convert() {
        this.getOrCreate(IntList.of());
        IntArrayList n = new IntArrayList(this.mNFA.mStartStates);
        n.sort(null);
        DFAState start = this.getOrCreate(n);
        this.traverse(start);
        this.computeMappings();
        int[][] transitions = new int[this.mTransitions.size()][];
        Arrays.setAll(transitions, i -> ((IntList) this.mTransitions.get(i)).toIntArray());
        return new DFA(this.mCharMappings.toIntArray(), transitions, this.mAccepts.toIntArray());
    }

    @Nonnull
    private DFAState getOrCreate(@Unmodifiable IntList states) {
        DFAState result = (DFAState) this.mStates.get(states);
        if (result == null) {
            int index = this.mStates.size();
            result = new DFAState(index, states);
            this.mStates.put(states, result);
        }
        return result;
    }

    private void add(int index, IntList states) {
        if (this.mNFA.get(index) instanceof NFAState.Replace replace) {
            IntListIterator var5 = replace.mShadow.iterator();
            while (var5.hasNext()) {
                int i = (Integer) var5.next();
                this.add(i, states);
            }
        } else if (!states.contains(index)) {
            states.add(index);
        }
    }

    private void addTransition(char c, int curr, int next) {
        while (this.mTransitions.size() <= c) {
            this.mTransitions.add(new IntArrayList());
        }
        IntList row = (IntList) this.mTransitions.get(c);
        while (row.size() <= curr) {
            row.add(-1);
        }
        row.set(curr, next);
    }

    private void traverse(@Nonnull DFAState curr) {
        curr.mScanned = true;
        for (char c = '\t'; c <= '~'; c++) {
            IntArrayList n = new IntArrayList();
            int best = Integer.MAX_VALUE;
            IntListIterator next = curr.mStates.iterator();
            while (next.hasNext()) {
                int index = (Integer) next.next();
                NFAState state = this.mNFA.get(index);
                if (state.accept(c)) {
                    IntListIterator var8 = state.next().iterator();
                    while (var8.hasNext()) {
                        int i = (Integer) var8.next();
                        if (this.mNFA.get(i) instanceof NFAState.Accept e) {
                            best = Math.min(best, e.mToken);
                        }
                        this.add(i, n);
                    }
                }
            }
            n.sort(null);
            DFAState nextx = this.getOrCreate(n);
            this.addTransition(c, curr.mIndex, nextx.mIndex);
            if (best != Integer.MAX_VALUE) {
                while (this.mAccepts.size() <= nextx.mIndex) {
                    this.mAccepts.add(-1);
                }
                this.mAccepts.set(nextx.mIndex, best);
            }
            if (!nextx.mScanned) {
                this.traverse(nextx);
            }
        }
    }

    private void computeMappings() {
        List<IntList> uniques = new ArrayList();
        for (IntList transition : this.mTransitions) {
            int found = uniques.indexOf(transition);
            if (found == -1) {
                found = uniques.size();
                uniques.add(transition);
            }
            this.mCharMappings.add(found);
        }
        this.mTransitions.clear();
        this.mTransitions.addAll(uniques);
    }
}