package icyllis.arc3d.compiler.spirv;

import java.util.Arrays;

sealed class Instruction permits InstructionBuilder {

    static final int kWord = 0;

    static final int kNoResult = 1;

    static final int kDefaultPrecisionResult = 2;

    static final int kRelaxedPrecisionResult = 3;

    static final int kUniqueResult = 4;

    static final int kKeyedResult = 5;

    int mOpcode;

    int mResultKind;

    int[] mWords;

    transient int mHash;

    Instruction() {
    }

    Instruction(int opcode, int resultKind, int[] words, int hash) {
        this.mOpcode = opcode;
        this.mResultKind = resultKind;
        this.mWords = words;
        this.mHash = hash;
    }

    static boolean isResult(int kind) {
        return kind >= 2;
    }

    public int hashCode() {
        int h = this.mHash;
        if (h == 0) {
            h = this.mOpcode;
            h = 31 * h + this.mResultKind;
            for (int j : this.mWords) {
                h = 31 * h + j;
            }
            this.mHash = h;
        }
        return h;
    }

    public boolean equals(Object o) {
        if (o.getClass() != Instruction.class) {
            return false;
        } else {
            Instruction key = (Instruction) o;
            return this.mOpcode == key.mOpcode && this.mResultKind == key.mResultKind && Arrays.equals(this.mWords, key.mWords);
        }
    }
}