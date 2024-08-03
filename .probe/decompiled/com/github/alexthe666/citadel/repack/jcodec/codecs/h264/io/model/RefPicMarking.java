package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;

public class RefPicMarking {

    private RefPicMarking.Instruction[] instructions;

    public RefPicMarking(RefPicMarking.Instruction[] instructions) {
        this.instructions = instructions;
    }

    public RefPicMarking.Instruction[] getInstructions() {
        return this.instructions;
    }

    public String toString() {
        return Platform.toJSON(this);
    }

    public static enum InstrType {

        REMOVE_SHORT,
        REMOVE_LONG,
        CONVERT_INTO_LONG,
        TRUNK_LONG,
        CLEAR,
        MARK_LONG
    }

    public static class Instruction {

        private RefPicMarking.InstrType type;

        private int arg1;

        private int arg2;

        public Instruction(RefPicMarking.InstrType type, int arg1, int arg2) {
            this.type = type;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }

        public RefPicMarking.InstrType getType() {
            return this.type;
        }

        public int getArg1() {
            return this.arg1;
        }

        public int getArg2() {
            return this.arg2;
        }
    }
}