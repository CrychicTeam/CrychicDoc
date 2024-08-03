package icyllis.arc3d.compiler.parser;

import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.Unmodifiable;

public class DFAState {

    public final int mIndex;

    public final IntList mStates;

    boolean mScanned = false;

    public DFAState(int index, @Unmodifiable IntList states) {
        this.mIndex = index;
        this.mStates = states;
    }
}