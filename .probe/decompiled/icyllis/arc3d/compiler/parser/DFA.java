package icyllis.arc3d.compiler.parser;

import java.util.Arrays;

public class DFA {

    public static final int INVALID = -1;

    public final int[] mCharMappings;

    public final int[][] mTransitions;

    public final int[] mAccepts;

    public DFA(int[] charMappings, int[][] transitions, int[] accepts) {
        this.mCharMappings = charMappings;
        this.mTransitions = transitions;
        this.mAccepts = accepts;
    }

    public String toString() {
        return "DFA{mCharMappings=" + Arrays.toString(this.mCharMappings) + ", mTransitions=" + Arrays.deepToString(this.mTransitions) + ", mAccepts=" + Arrays.toString(this.mAccepts) + "}";
    }
}