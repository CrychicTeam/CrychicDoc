package dev.latvian.mods.rhino;

import java.util.Arrays;

final class InterpreterData {

    static final int INITIAL_MAX_ICODE_LENGTH = 1024;

    static final int INITIAL_STRINGTABLE_SIZE = 64;

    static final int INITIAL_NUMBERTABLE_SIZE = 64;

    String itsName;

    String itsSourceFile;

    boolean itsNeedsActivation;

    int itsFunctionType;

    String[] itsStringTable;

    double[] itsDoubleTable;

    InterpreterData[] itsNestedFunctions;

    Object[] itsRegExpLiterals;

    Object[] itsTemplateLiterals;

    byte[] itsICode;

    int[] itsExceptionTable;

    int itsMaxVars;

    int itsMaxLocals;

    int itsMaxStack;

    int itsMaxFrameArray;

    String[] argNames;

    boolean[] argIsConst;

    int argCount;

    int itsMaxCalleeArgs;

    boolean isStrict;

    boolean topLevel;

    boolean isES6Generator;

    Object[] literalIds;

    UintMap longJumps;

    int firstLinePC = -1;

    InterpreterData parentData;

    boolean evalScriptFlag;

    boolean declaredAsVar;

    boolean declaredAsFunctionExpression;

    private int icodeHashCode = 0;

    InterpreterData(String sourceFile, boolean isStrict) {
        this.itsSourceFile = sourceFile;
        this.isStrict = isStrict;
        this.init();
    }

    InterpreterData(InterpreterData parent) {
        this.parentData = parent;
        this.itsSourceFile = parent.itsSourceFile;
        this.isStrict = parent.isStrict;
        this.init();
    }

    private void init() {
        this.itsICode = new byte[1024];
        this.itsStringTable = new String[64];
    }

    public String getFunctionName() {
        return this.itsName;
    }

    public int getParamAndVarCount() {
        return this.argNames.length;
    }

    public boolean getParamOrVarConst(int index) {
        return this.argIsConst[index];
    }

    public int getFunctionCount() {
        return this.itsNestedFunctions == null ? 0 : this.itsNestedFunctions.length;
    }

    public InterpreterData getFunction(int index) {
        return this.itsNestedFunctions[index];
    }

    public InterpreterData getParent() {
        return this.parentData;
    }

    public int icodeHashCode() {
        int h = this.icodeHashCode;
        if (h == 0) {
            this.icodeHashCode = h = Arrays.hashCode(this.itsICode);
        }
        return h;
    }
}