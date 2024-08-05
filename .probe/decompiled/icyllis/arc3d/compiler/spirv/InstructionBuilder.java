package icyllis.arc3d.compiler.spirv;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Arrays;
import javax.annotation.Nonnull;

final class InstructionBuilder extends Instruction {

    IntArrayList mValues = new IntArrayList();

    IntArrayList mKinds = new IntArrayList();

    InstructionBuilder(int opcode) {
        this.mOpcode = opcode;
        this.mResultKind = 1;
    }

    InstructionBuilder reset(int opcode) {
        this.mOpcode = opcode;
        this.mResultKind = 1;
        this.mValues.clear();
        this.mKinds.clear();
        this.mHash = 0;
        return this;
    }

    InstructionBuilder addWord(int word) {
        this.mValues.add(word);
        this.mKinds.add(0);
        return this;
    }

    InstructionBuilder addResult() {
        assert this.mResultKind == 1;
        this.mValues.add(-1);
        this.mKinds.add(2);
        this.mResultKind = 2;
        return this;
    }

    InstructionBuilder addRelaxedResult() {
        assert this.mResultKind == 1;
        this.mValues.add(-1);
        this.mKinds.add(3);
        this.mResultKind = 3;
        return this;
    }

    InstructionBuilder addUniqueResult() {
        assert this.mResultKind == 1;
        this.mValues.add(-1);
        this.mKinds.add(4);
        this.mResultKind = 4;
        return this;
    }

    InstructionBuilder addKeyedResult(int key) {
        assert key != -1;
        assert this.mResultKind == 1;
        this.mValues.add(key);
        this.mKinds.add(5);
        this.mResultKind = 5;
        return this;
    }

    @Nonnull
    Instruction copy() {
        return new Instruction(this.mOpcode, this.mResultKind, this.mValues.toIntArray(), this.mHash);
    }

    @Override
    public int hashCode() {
        int h = this.mHash;
        if (h == 0) {
            h = this.mOpcode;
            h = 31 * h + this.mResultKind;
            int[] a = this.mValues.elements();
            int s = this.mValues.size();
            for (int i = 0; i < s; i++) {
                h = 31 * h + a[i];
            }
            this.mHash = h;
        }
        return h;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Instruction.class) {
            return false;
        } else {
            Instruction key = (Instruction) o;
            return this.mOpcode == key.mOpcode && this.mResultKind == key.mResultKind && Arrays.equals(this.mValues.elements(), 0, this.mValues.size(), key.mWords, 0, key.mWords.length);
        }
    }
}