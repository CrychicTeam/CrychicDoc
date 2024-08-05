package icyllis.arc3d.compiler.parser;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public class NFA {

    private final List<NFAState> mStates = new ArrayList();

    private int mTokenIndex = 0;

    final IntList mStartStates = new IntArrayList();

    public void add(@Nonnull RegexNode node) {
        int token = ++this.mTokenIndex;
        int state = this.add(NFAState.Accept(token));
        this.mStartStates.addAll(node.transition(this, IntList.of(state)));
    }

    public int add(NFAState state) {
        int index = this.mStates.size();
        this.mStates.add(state);
        return index;
    }

    public NFAState get(int index) {
        return (NFAState) this.mStates.get(index);
    }

    public IntList replace(int index, IntList shadow) {
        assert this.mStates.get(index) == null;
        this.mStates.set(index, NFAState.Replace(shadow));
        return shadow;
    }

    public int match(@Nonnull String s) {
        IntList states = this.mStartStates;
        for (int p = 0; p < s.length(); p++) {
            IntArrayList n = new IntArrayList();
            IntListIterator index = states.iterator();
            while (index.hasNext()) {
                int indexx = (Integer) index.next();
                NFAState state = this.get(indexx);
                if (state.accept(s.charAt(p))) {
                    IntListIterator var8 = state.next().iterator();
                    while (var8.hasNext()) {
                        int i = (Integer) var8.next();
                        if (this.get(i) instanceof NFAState.Replace replace) {
                            n.addAll(replace.mShadow);
                        } else {
                            n.add(i);
                        }
                    }
                }
            }
            if (n.isEmpty()) {
                return -1;
            }
            states = n;
        }
        int accept = -1;
        IntListIterator var13 = states.iterator();
        while (var13.hasNext()) {
            int index = (Integer) var13.next();
            NFAState var16 = this.get(index);
            if (var16 instanceof NFAState.Accept) {
                NFAState.Accept e = (NFAState.Accept) var16;
                if (accept == -1 || e.mToken < accept) {
                    accept = e.mToken;
                }
            }
        }
        return accept;
    }
}