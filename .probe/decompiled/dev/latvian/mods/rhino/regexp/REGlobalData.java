package dev.latvian.mods.rhino.regexp;

class REGlobalData {

    boolean multiline;

    RECompiled regexp;

    int skipped;

    int cp;

    long[] parens;

    REProgState stateStackTop;

    REBackTrackData backTrackStackTop;

    int parensIndex(int i) {
        return (int) this.parens[i];
    }

    int parensLength(int i) {
        return (int) (this.parens[i] >>> 32);
    }

    void setParens(int i, int index, int length) {
        if (this.backTrackStackTop != null && this.backTrackStackTop.parens == this.parens) {
            this.parens = (long[]) this.parens.clone();
        }
        this.parens[i] = (long) index & 4294967295L | (long) length << 32;
    }
}