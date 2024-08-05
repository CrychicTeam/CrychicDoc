package dev.latvian.mods.rhino.classfile;

final class ExceptionTableEntry {

    int itsStartLabel;

    int itsEndLabel;

    int itsHandlerLabel;

    short itsCatchType;

    ExceptionTableEntry(int startLabel, int endLabel, int handlerLabel, short catchType) {
        this.itsStartLabel = startLabel;
        this.itsEndLabel = endLabel;
        this.itsHandlerLabel = handlerLabel;
        this.itsCatchType = catchType;
    }
}