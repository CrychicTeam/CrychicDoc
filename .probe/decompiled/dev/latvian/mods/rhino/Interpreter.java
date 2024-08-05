package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.ast.ScriptNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Interpreter extends Icode implements Evaluator {

    static final int EXCEPTION_TRY_START_SLOT = 0;

    static final int EXCEPTION_TRY_END_SLOT = 1;

    static final int EXCEPTION_HANDLER_SLOT = 2;

    static final int EXCEPTION_TYPE_SLOT = 3;

    static final int EXCEPTION_LOCAL_SLOT = 4;

    static final int EXCEPTION_SCOPE_SLOT = 5;

    static final int EXCEPTION_SLOT_SIZE = 6;

    InterpreterData itsData;

    private static boolean compareIdata(InterpreterData i1, InterpreterData i2) {
        return i1 == i2;
    }

    private static Interpreter.CallFrame captureFrameForGenerator(Interpreter.CallFrame frame) {
        frame.frozen = true;
        Interpreter.CallFrame result = frame.cloneFrozen();
        frame.frozen = false;
        result.parentFrame = null;
        result.frameIndex = 0;
        return result;
    }

    private static int getShort(byte[] iCode, int pc) {
        return iCode[pc] << 8 | iCode[pc + 1] & 0xFF;
    }

    private static int getIndex(byte[] iCode, int pc) {
        return (iCode[pc] & 0xFF) << 8 | iCode[pc + 1] & 0xFF;
    }

    private static int getInt(byte[] iCode, int pc) {
        return iCode[pc] << 24 | (iCode[pc + 1] & 0xFF) << 16 | (iCode[pc + 2] & 0xFF) << 8 | iCode[pc + 3] & 0xFF;
    }

    private static int getExceptionHandler(Interpreter.CallFrame frame, boolean onlyFinally) {
        int[] exceptionTable = frame.idata.itsExceptionTable;
        if (exceptionTable == null) {
            return -1;
        } else {
            int pc = frame.pc - 1;
            int best = -1;
            int bestStart = 0;
            int bestEnd = 0;
            for (int i = 0; i != exceptionTable.length; i += 6) {
                int start = exceptionTable[i + 0];
                int end = exceptionTable[i + 1];
                if (start <= pc && pc < end && (!onlyFinally || exceptionTable[i + 3] == 1)) {
                    if (best >= 0) {
                        if (bestEnd < end) {
                            continue;
                        }
                        if (bestStart > start) {
                            Kit.codeBug();
                        }
                        if (bestEnd == end) {
                            Kit.codeBug();
                        }
                    }
                    best = i;
                    bestStart = start;
                    bestEnd = end;
                }
            }
            return best;
        }
    }

    private static void initFunction(Context cx, Scriptable scope, InterpretedFunction parent, int index) {
        InterpretedFunction fn = InterpretedFunction.createFunction(cx, scope, parent, index);
        ScriptRuntime.initFunction(cx, scope, fn, fn.idata.itsFunctionType, parent.idata.evalScriptFlag);
    }

    static Object interpret(InterpretedFunction ifun, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!cx.hasTopCallScope()) {
            Kit.codeBug();
        }
        Interpreter.CallFrame frame = initFrame(cx, scope, thisObj, args, null, 0, args.length, ifun, null);
        frame.isContinuationsTopFrame = cx.isContinuationsTopCall;
        cx.isContinuationsTopCall = false;
        return interpretLoop(cx, frame, null);
    }

    public static Object resumeGenerator(Context cx, Scriptable scope, int operation, Object savedState, Object value) {
        Interpreter.CallFrame frame = (Interpreter.CallFrame) savedState;
        GeneratorState generatorState = new GeneratorState(operation, value);
        if (operation == 2) {
            try {
                return interpretLoop(cx, frame, generatorState);
            } catch (RuntimeException var8) {
                if (var8 != value) {
                    throw var8;
                } else {
                    return Undefined.instance;
                }
            }
        } else {
            Object result = interpretLoop(cx, frame, generatorState);
            if (generatorState.returnedException != null) {
                throw generatorState.returnedException;
            } else {
                return result;
            }
        }
    }

    private static Object interpretLoop(Context cx, Interpreter.CallFrame frame, Object throwable) {
        Object DBL_MRK = UniqueTag.DOUBLE_MARK;
        Object undefined = Undefined.instance;
        boolean instructionCounting = cx.instructionThreshold != 0;
        int INVOCATION_COST = 100;
        int EXCEPTION_COST = 100;
        String stringReg = null;
        int indexReg = -1;
        if (cx.lastInterpreterFrame != null) {
            if (cx.previousInterpreterInvocations == null) {
                cx.previousInterpreterInvocations = new ObjArray();
            }
            cx.previousInterpreterInvocations.push(cx.lastInterpreterFrame);
        }
        GeneratorState generatorState = null;
        if (throwable != null) {
            if (throwable instanceof GeneratorState) {
                generatorState = (GeneratorState) throwable;
                enterFrame(cx, frame, ScriptRuntime.EMPTY_OBJECTS, true);
                throwable = null;
            } else {
                Kit.codeBug();
            }
        }
        Object interpreterResult = null;
        double interpreterResultDbl = 0.0;
        label959: while (true) {
            label1012: {
                try {
                    if (throwable != null) {
                        frame = processThrowable(cx, throwable, frame, indexReg, instructionCounting);
                        throwable = frame.throwable;
                        frame.throwable = null;
                    } else if (generatorState == null && frame.frozen) {
                        Kit.codeBug();
                    }
                    Object[] stack = frame.stack;
                    double[] sDbl = frame.sDbl;
                    Object[] vars = frame.varSource.stack;
                    double[] varDbls = frame.varSource.sDbl;
                    int[] varAttributes = frame.varSource.stackAttributes;
                    byte[] iCode = frame.idata.itsICode;
                    String[] strings = frame.idata.itsStringTable;
                    int stackTop = frame.savedStackTop;
                    cx.lastInterpreterFrame = frame;
                    label942: while (true) {
                        label878: while (true) {
                            label876: while (true) {
                                label874: while (true) {
                                    label801: while (true) {
                                        int op = iCode[frame.pc++];
                                        switch(op) {
                                            case -67:
                                                Object[] templateLiterals = frame.idata.itsTemplateLiterals;
                                                stackTop++;
                                                stack[stackTop] = ScriptRuntime.getTemplateLiteralCallSite(cx, frame.scope, templateLiterals, indexReg);
                                                continue;
                                            case -65:
                                                {
                                                    frame.frozen = true;
                                                    frame.result = stack[stackTop];
                                                    frame.resultDbl = sDbl[stackTop];
                                                    stackTop--;
                                                    NativeIterator.StopIteration si = new NativeIterator.StopIteration(cx, frame.result == UniqueTag.DOUBLE_MARK ? frame.resultDbl : frame.result);
                                                    int sourceLine = getIndex(iCode, frame.pc);
                                                    generatorState.returnedException = new JavaScriptException(cx, si, frame.idata.itsSourceFile, sourceLine);
                                                    break;
                                                }
                                            case -64:
                                            case -60:
                                            case 1:
                                            case 77:
                                            case 79:
                                            case 80:
                                            case 81:
                                            case 82:
                                            case 83:
                                            case 84:
                                            case 85:
                                            case 86:
                                            case 87:
                                            case 88:
                                            case 89:
                                            case 90:
                                            case 91:
                                            case 92:
                                            case 93:
                                            case 94:
                                            case 95:
                                            case 96:
                                            case 97:
                                            case 98:
                                            case 99:
                                            case 100:
                                            case 101:
                                            case 102:
                                            case 103:
                                            case 104:
                                            case 105:
                                            case 106:
                                            case 107:
                                            case 108:
                                            case 109:
                                            case 110:
                                            case 111:
                                            case 112:
                                            case 113:
                                            case 114:
                                            case 115:
                                            case 116:
                                            case 117:
                                            case 118:
                                            case 119:
                                            case 120:
                                            case 121:
                                            case 122:
                                            case 123:
                                            case 124:
                                            case 125:
                                            case 126:
                                            case 127:
                                            case 128:
                                            case 129:
                                            case 130:
                                            case 131:
                                            case 132:
                                            case 133:
                                            case 134:
                                            case 135:
                                            case 136:
                                            case 137:
                                            case 138:
                                            case 139:
                                            case 140:
                                            case 141:
                                            case 142:
                                            case 143:
                                            case 144:
                                            case 145:
                                            case 146:
                                            case 147:
                                            case 148:
                                            case 149:
                                            case 150:
                                            case 151:
                                            case 152:
                                            case 153:
                                            case 154:
                                            case 155:
                                            case 156:
                                            default:
                                                throw new RuntimeException("Unknown icode : " + op + " @ pc : " + (frame.pc - 1));
                                            case -63:
                                                {
                                                    frame.frozen = true;
                                                    int sourceLine = getIndex(iCode, frame.pc);
                                                    generatorState.returnedException = new JavaScriptException(cx, NativeIterator.getStopIterationObject(frame.scope, cx), frame.idata.itsSourceFile, sourceLine);
                                                    break;
                                                }
                                            case -62:
                                                if (!frame.frozen) {
                                                    frame.pc--;
                                                    Interpreter.CallFrame generatorFrame = captureFrameForGenerator(frame);
                                                    generatorFrame.frozen = true;
                                                    frame.result = new ES6Generator(frame.scope, generatorFrame.fnOrScript, generatorFrame, cx);
                                                    break;
                                                }
                                            case -66:
                                            case 73:
                                                if (!frame.frozen) {
                                                    return freezeGenerator(cx, frame, stackTop, generatorState, op == -66);
                                                }
                                                Object obj = thawGenerator(cx, frame, stackTop, generatorState, op);
                                                if (obj != Scriptable.NOT_FOUND) {
                                                    throwable = obj;
                                                    break label942;
                                                }
                                                continue;
                                            case -61:
                                                indexReg = iCode[frame.pc++];
                                                break label878;
                                            case -59:
                                                Object rhs = stack[stackTop];
                                                if (rhs == DBL_MRK) {
                                                    rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                Scriptable lhs = (Scriptable) stack[--stackTop];
                                                stack[stackTop] = ScriptRuntime.setConst(cx, lhs, rhs, stringReg);
                                                continue;
                                            case -58:
                                                {
                                                    Object valuex = stack[stackTop];
                                                    int i = (int) sDbl[--stackTop];
                                                    ((Object[]) stack[stackTop])[i] = valuex;
                                                    ((int[]) stack[stackTop - 1])[i] = 1;
                                                    sDbl[stackTop] = (double) (i + 1);
                                                    continue;
                                                }
                                            case -57:
                                                {
                                                    Object valuexx = stack[stackTop];
                                                    int i = (int) sDbl[--stackTop];
                                                    ((Object[]) stack[stackTop])[i] = valuexx;
                                                    ((int[]) stack[stackTop - 1])[i] = -1;
                                                    sDbl[stackTop] = (double) (i + 1);
                                                    continue;
                                                }
                                            case -56:
                                                indexReg += frame.localShift;
                                                stack[indexReg] = null;
                                                continue;
                                            case -55:
                                            case 38:
                                            case 71:
                                                if (instructionCounting) {
                                                    cx.instructionCount += 100;
                                                }
                                                stackTop -= 1 + indexReg;
                                                Callable fun = (Callable) stack[stackTop];
                                                Scriptable funThisObj = (Scriptable) stack[stackTop + 1];
                                                if (op == 71) {
                                                    Object[] outArgs = getArgsArray(stack, sDbl, stackTop + 2, indexReg);
                                                    stack[stackTop] = ScriptRuntime.callRef(cx, funThisObj, fun, outArgs);
                                                } else {
                                                    Scriptable calleeScope = frame.scope;
                                                    if (frame.useActivation) {
                                                        calleeScope = ScriptableObject.getTopLevelScope(frame.scope);
                                                    }
                                                    if (fun instanceof InterpretedFunction ifun) {
                                                        Interpreter.CallFrame callParentFrame = frame;
                                                        if (op == -55) {
                                                            callParentFrame = frame.parentFrame;
                                                            exitFrame(cx, frame, null);
                                                        }
                                                        Interpreter.CallFrame calleeFrame = initFrame(cx, calleeScope, funThisObj, stack, sDbl, stackTop + 2, indexReg, ifun, callParentFrame);
                                                        if (op != -55) {
                                                            frame.savedStackTop = stackTop;
                                                            frame.savedCallOp = op;
                                                        }
                                                        frame = calleeFrame;
                                                        continue label959;
                                                    }
                                                    if (fun instanceof IdFunctionObject ifun && BaseFunction.isApplyOrCall(ifun) && ScriptRuntime.getCallable(cx, funThisObj) instanceof InterpretedFunction iApplyCallable) {
                                                        frame = initFrameForApplyOrCall(cx, frame, indexReg, stack, sDbl, stackTop, op, calleeScope, ifun, iApplyCallable);
                                                        continue label959;
                                                    }
                                                    if (fun instanceof ScriptRuntime.NoSuchMethodShim noSuchMethodShim && noSuchMethodShim.noSuchMethodMethod instanceof InterpretedFunction ifun) {
                                                        frame = initFrameForNoSuchMethod(cx, frame, indexReg, stack, sDbl, stackTop, op, funThisObj, calleeScope, noSuchMethodShim, ifun);
                                                        continue label959;
                                                    }
                                                    cx.lastInterpreterFrame = frame;
                                                    frame.savedCallOp = op;
                                                    frame.savedStackTop = stackTop;
                                                    stack[stackTop] = fun.call(cx, calleeScope, funThisObj, getArgsArray(stack, sDbl, stackTop + 2, indexReg));
                                                }
                                                continue;
                                            case -54:
                                                boolean valBln = stack_boolean(frame, stackTop, cx);
                                                Object x = ScriptRuntime.updateDotQuery(valBln, frame.scope);
                                                if (x != null) {
                                                    stack[stackTop] = x;
                                                    frame.scope = ScriptRuntime.leaveDotQuery(frame.scope);
                                                    frame.pc += 2;
                                                    continue;
                                                } else {
                                                    stackTop--;
                                                    break label801;
                                                }
                                            case -53:
                                                Object lhs = stack[stackTop];
                                                if (lhs == DBL_MRK) {
                                                    lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stackTop--;
                                                frame.scope = ScriptRuntime.enterDotQuery(lhs, frame.scope, cx);
                                                continue;
                                            case -52:
                                                stackTop++;
                                                stack[stackTop] = DBL_MRK;
                                                sDbl[stackTop] = 1.0;
                                                continue;
                                            case -51:
                                                stackTop++;
                                                stack[stackTop] = DBL_MRK;
                                                sDbl[stackTop] = 0.0;
                                                continue;
                                            case -50:
                                                stackTop++;
                                                stack[stackTop] = undefined;
                                                continue;
                                            case -49:
                                                indexReg = iCode[frame.pc++];
                                                break label876;
                                            case -48:
                                                indexReg = iCode[frame.pc++];
                                                break label874;
                                            case -47:
                                                stringReg = strings[getInt(iCode, frame.pc)];
                                                frame.pc += 4;
                                                continue;
                                            case -46:
                                                stringReg = strings[getIndex(iCode, frame.pc)];
                                                frame.pc += 2;
                                                continue;
                                            case -45:
                                                stringReg = strings[255 & iCode[frame.pc]];
                                                frame.pc++;
                                                continue;
                                            case -44:
                                                stringReg = strings[3];
                                                continue;
                                            case -43:
                                                stringReg = strings[2];
                                                continue;
                                            case -42:
                                                stringReg = strings[1];
                                                continue;
                                            case -41:
                                                stringReg = strings[0];
                                                continue;
                                            case -40:
                                                indexReg = getInt(iCode, frame.pc);
                                                frame.pc += 4;
                                                continue;
                                            case -39:
                                                indexReg = getIndex(iCode, frame.pc);
                                                frame.pc += 2;
                                                continue;
                                            case -38:
                                                indexReg = 255 & iCode[frame.pc];
                                                frame.pc++;
                                                continue;
                                            case -37:
                                                indexReg = 5;
                                                continue;
                                            case -36:
                                                indexReg = 4;
                                                continue;
                                            case -35:
                                                indexReg = 3;
                                                continue;
                                            case -34:
                                                indexReg = 2;
                                                continue;
                                            case -33:
                                                indexReg = 1;
                                                continue;
                                            case -32:
                                                indexReg = 0;
                                                continue;
                                            case -31:
                                            case 66:
                                            case 67:
                                                Object[] data = (Object[]) stack[stackTop];
                                                int[] getterSetters = (int[]) stack[--stackTop];
                                                Object val;
                                                if (op == 67) {
                                                    Object[] ids = (Object[]) frame.idata.literalIds[indexReg];
                                                    val = ScriptRuntime.newObjectLiteral(cx, frame.scope, ids, data, getterSetters);
                                                } else {
                                                    int[] skipIndexces = null;
                                                    if (op == -31) {
                                                        skipIndexces = (int[]) frame.idata.literalIds[indexReg];
                                                    }
                                                    val = ScriptRuntime.newArrayLiteral(cx, frame.scope, data, skipIndexces);
                                                }
                                                stack[stackTop] = val;
                                                continue;
                                            case -30:
                                                {
                                                    Object valuexxx = stack[stackTop];
                                                    if (valuexxx == DBL_MRK) {
                                                        valuexxx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                    }
                                                    int i = (int) sDbl[--stackTop];
                                                    ((Object[]) stack[stackTop])[i] = valuexxx;
                                                    sDbl[stackTop] = (double) (i + 1);
                                                    continue;
                                                }
                                            case -29:
                                                stackTop++;
                                                stack[stackTop] = new int[indexReg];
                                                stackTop++;
                                                stack[stackTop] = new Object[indexReg];
                                                sDbl[stackTop] = 0.0;
                                                continue;
                                            case -28:
                                                stackTop++;
                                                stack[stackTop] = DBL_MRK;
                                                sDbl[stackTop] = (double) getInt(iCode, frame.pc);
                                                frame.pc += 4;
                                                continue;
                                            case -27:
                                                stackTop++;
                                                stack[stackTop] = DBL_MRK;
                                                sDbl[stackTop] = (double) getShort(iCode, frame.pc);
                                                frame.pc += 2;
                                                continue;
                                            case -26:
                                                frame.pcSourceLineStart = frame.pc;
                                                frame.pc += 2;
                                                continue;
                                            case -25:
                                                if (instructionCounting) {
                                                    addInstructionCount(cx, frame, 0);
                                                }
                                                indexReg += frame.localShift;
                                                Object value = stack[indexReg];
                                                if (value != DBL_MRK) {
                                                    throwable = value;
                                                    break label942;
                                                }
                                                frame.pc = (int) sDbl[indexReg];
                                                if (instructionCounting) {
                                                    frame.pcPrevBranch = frame.pc;
                                                }
                                                continue;
                                            case -24:
                                                if (stackTop == frame.emptyStackTop + 1) {
                                                    indexReg += frame.localShift;
                                                    stack[indexReg] = stack[stackTop];
                                                    sDbl[indexReg] = sDbl[stackTop];
                                                    stackTop--;
                                                } else if (stackTop != frame.emptyStackTop) {
                                                    Kit.codeBug();
                                                }
                                                continue;
                                            case -23:
                                                stackTop++;
                                                stack[stackTop] = DBL_MRK;
                                                sDbl[stackTop] = (double) (frame.pc + 2);
                                                break label801;
                                            case -22:
                                                frame.result = undefined;
                                                break;
                                            case -21:
                                                if (instructionCounting) {
                                                    cx.instructionCount += 100;
                                                }
                                                stackTop = doCallSpecial(cx, frame, stack, sDbl, stackTop, iCode, indexReg);
                                                continue;
                                            case -20:
                                                initFunction(cx, frame.scope, frame.fnOrScript, indexReg);
                                                continue;
                                            case -19:
                                                InterpretedFunction fn = InterpretedFunction.createFunction(cx, frame.scope, frame.fnOrScript, indexReg);
                                                if (fn.idata.itsFunctionType == 4) {
                                                    stackTop++;
                                                    stack[stackTop] = new ArrowFunction(cx, frame.scope, fn, frame.thisObj);
                                                } else {
                                                    stackTop++;
                                                    stack[stackTop] = fn;
                                                }
                                                continue;
                                            case -18:
                                                Object valuexxx = stack[stackTop];
                                                if (valuexxx == DBL_MRK) {
                                                    valuexxx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stack[stackTop] = ScriptRuntime.getValueFunctionAndThis(cx, valuexxx);
                                                stackTop++;
                                                stack[stackTop] = cx.lastStoredScriptable();
                                                continue;
                                            case -17:
                                                Object obj = stack[stackTop - 1];
                                                if (obj == DBL_MRK) {
                                                    obj = ScriptRuntime.wrapNumber(sDbl[stackTop - 1]);
                                                }
                                                Object id = stack[stackTop];
                                                if (id == DBL_MRK) {
                                                    id = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stack[stackTop - 1] = ScriptRuntime.getElemFunctionAndThis(cx, frame.scope, obj, id);
                                                stack[stackTop] = cx.lastStoredScriptable();
                                                continue;
                                            case -16:
                                                Object objx = stack[stackTop];
                                                if (objx == DBL_MRK) {
                                                    objx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stack[stackTop] = ScriptRuntime.getPropFunctionAndThis(cx, frame.scope, objx, stringReg);
                                                stackTop++;
                                                stack[stackTop] = cx.lastStoredScriptable();
                                                continue;
                                            case -15:
                                                stackTop++;
                                                stack[stackTop] = ScriptRuntime.getNameFunctionAndThis(cx, frame.scope, stringReg);
                                                stackTop++;
                                                stack[stackTop] = cx.lastStoredScriptable();
                                                continue;
                                            case -14:
                                                stackTop++;
                                                stack[stackTop] = ScriptRuntime.typeofName(cx, frame.scope, stringReg).toString();
                                                continue;
                                            case -13:
                                                indexReg += frame.localShift;
                                                stack[indexReg] = frame.scope;
                                                continue;
                                            case -12:
                                                indexReg += frame.localShift;
                                                frame.scope = (Scriptable) stack[indexReg];
                                                continue;
                                            case -11:
                                                {
                                                    Ref ref = (Ref) stack[stackTop];
                                                    stack[stackTop] = ScriptRuntime.refIncrDecr(cx, frame.scope, ref, iCode[frame.pc]);
                                                    frame.pc++;
                                                    continue;
                                                }
                                            case -10:
                                                stackTop = doElemIncDec(cx, frame, iCode, stack, sDbl, stackTop);
                                                continue;
                                            case -9:
                                                Object lhs = stack[stackTop];
                                                if (lhs == DBL_MRK) {
                                                    lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stack[stackTop] = ScriptRuntime.propIncrDecr(cx, frame.scope, lhs, stringReg, iCode[frame.pc]);
                                                frame.pc++;
                                                continue;
                                            case -8:
                                                stackTop++;
                                                stack[stackTop] = ScriptRuntime.nameIncrDecr(cx, frame.scope, stringReg, iCode[frame.pc]);
                                                frame.pc++;
                                                continue;
                                            case -7:
                                                stackTop = doVarIncDec(cx, frame, stack, sDbl, stackTop, vars, varDbls, varAttributes, indexReg);
                                                continue;
                                            case -6:
                                                if (!stack_boolean(frame, stackTop--, cx)) {
                                                    frame.pc += 2;
                                                    continue;
                                                } else {
                                                    stack[stackTop--] = null;
                                                    break label801;
                                                }
                                            case -5:
                                                frame.result = stack[stackTop];
                                                frame.resultDbl = sDbl[stackTop];
                                                stack[stackTop] = null;
                                                stackTop--;
                                                continue;
                                            case -4:
                                                stack[stackTop] = null;
                                                stackTop--;
                                                continue;
                                            case -3:
                                                Object o = stack[stackTop];
                                                stack[stackTop] = stack[stackTop - 1];
                                                stack[stackTop - 1] = o;
                                                double d = sDbl[stackTop];
                                                sDbl[stackTop] = sDbl[stackTop - 1];
                                                sDbl[stackTop - 1] = d;
                                                continue;
                                            case -2:
                                                stack[stackTop + 1] = stack[stackTop - 1];
                                                sDbl[stackTop + 1] = sDbl[stackTop - 1];
                                                stack[stackTop + 2] = stack[stackTop];
                                                sDbl[stackTop + 2] = sDbl[stackTop];
                                                stackTop += 2;
                                                continue;
                                            case -1:
                                                stack[stackTop + 1] = stack[stackTop];
                                                sDbl[stackTop + 1] = sDbl[stackTop];
                                                stackTop++;
                                                continue;
                                            case 0:
                                            case 31:
                                                stackTop = doDelName(cx, frame, op, stack, sDbl, stackTop);
                                                continue;
                                            case 2:
                                                Object lhs = stack[stackTop];
                                                if (lhs == DBL_MRK) {
                                                    lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stackTop--;
                                                frame.scope = ScriptRuntime.enterWith(cx, frame.scope, lhs);
                                                continue;
                                            case 3:
                                                frame.scope = ScriptRuntime.leaveWith(frame.scope);
                                                continue;
                                            case 4:
                                                frame.result = stack[stackTop];
                                                frame.resultDbl = sDbl[stackTop];
                                                stackTop--;
                                                break;
                                            case 5:
                                                break label801;
                                            case 6:
                                                if (!stack_boolean(frame, stackTop--, cx)) {
                                                    frame.pc += 2;
                                                    continue;
                                                }
                                                break label801;
                                            case 7:
                                                if (stack_boolean(frame, stackTop--, cx)) {
                                                    frame.pc += 2;
                                                    continue;
                                                }
                                                break label801;
                                            case 8:
                                            case 74:
                                                Object rhs = stack[stackTop];
                                                if (rhs == DBL_MRK) {
                                                    rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                Scriptable lhs = (Scriptable) stack[--stackTop];
                                                stack[stackTop] = op == 8 ? ScriptRuntime.setName(cx, frame.scope, lhs, rhs, stringReg) : ScriptRuntime.strictSetName(cx, frame.scope, lhs, rhs, stringReg);
                                                continue;
                                            case 9:
                                            case 10:
                                            case 11:
                                            case 18:
                                            case 19:
                                                stackTop = doBitOp(frame, op, stack, sDbl, stackTop, cx);
                                                continue;
                                            case 12:
                                            case 13:
                                                boolean valBlnx = doEquals(stack, sDbl, --stackTop, cx);
                                                valBlnx ^= op == 13;
                                                stack[stackTop] = valBlnx;
                                                continue;
                                            case 14:
                                            case 15:
                                            case 16:
                                            case 17:
                                                stackTop = doCompare(frame, op, stack, sDbl, stackTop, cx);
                                                continue;
                                            case 20:
                                                {
                                                    double lDbl = stack_double(frame, stackTop - 1, cx);
                                                    int rIntValue = stack_int32(frame, stackTop, cx) & 31;
                                                    stackTop--;
                                                    stack[stackTop] = DBL_MRK;
                                                    sDbl[stackTop] = (double) (ScriptRuntime.toUint32(lDbl) >>> rIntValue);
                                                    continue;
                                                }
                                            case 21:
                                                doAdd(stack, sDbl, --stackTop, cx);
                                                continue;
                                            case 22:
                                            case 23:
                                            case 24:
                                            case 25:
                                            case 76:
                                                stackTop = doArithmetic(cx, frame, op, stack, sDbl, stackTop);
                                                continue;
                                            case 26:
                                                stack[stackTop] = !stack_boolean(frame, stackTop, cx);
                                                continue;
                                            case 27:
                                                {
                                                    int rIntValue = stack_int32(frame, stackTop, cx);
                                                    stack[stackTop] = DBL_MRK;
                                                    sDbl[stackTop] = (double) (~rIntValue);
                                                    continue;
                                                }
                                            case 28:
                                            case 29:
                                                double rDbl = stack_double(frame, stackTop, cx);
                                                stack[stackTop] = DBL_MRK;
                                                if (op == 29) {
                                                    rDbl = -rDbl;
                                                }
                                                sDbl[stackTop] = rDbl;
                                                continue;
                                            case 30:
                                                if (instructionCounting) {
                                                    cx.instructionCount += 100;
                                                }
                                                stackTop -= indexReg;
                                                Object lhs = stack[stackTop];
                                                if (lhs instanceof InterpretedFunction f) {
                                                    Scriptable newInstance = f.createObject(cx, frame.scope);
                                                    Interpreter.CallFrame calleeFrame = initFrame(cx, frame.scope, newInstance, stack, sDbl, stackTop + 1, indexReg, f, frame);
                                                    stack[stackTop] = newInstance;
                                                    frame.savedStackTop = stackTop;
                                                    frame.savedCallOp = op;
                                                    frame = calleeFrame;
                                                    continue label959;
                                                }
                                                if (!(lhs instanceof Function fun)) {
                                                    if (lhs == DBL_MRK) {
                                                        lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                    }
                                                    throw ScriptRuntime.notFunctionError(cx, lhs);
                                                }
                                                Object[] var94 = getArgsArray(stack, sDbl, stackTop + 1, indexReg);
                                                stack[stackTop] = fun.construct(cx, frame.scope, var94);
                                                continue;
                                            case 32:
                                                Object lhsx = stack[stackTop];
                                                if (lhsx == DBL_MRK) {
                                                    lhsx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stack[stackTop] = ScriptRuntime.typeof(cx, lhsx).toString();
                                                continue;
                                            case 33:
                                                Object lhsx = stack[stackTop];
                                                if (lhsx == DBL_MRK) {
                                                    lhsx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stack[stackTop] = ScriptRuntime.getObjectProp(cx, frame.scope, lhsx, stringReg);
                                                continue;
                                            case 34:
                                                Object lhsx = stack[stackTop];
                                                if (lhsx == DBL_MRK) {
                                                    lhsx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stack[stackTop] = ScriptRuntime.getObjectPropNoWarn(cx, frame.scope, lhsx, stringReg);
                                                continue;
                                            case 35:
                                                Object rhs = stack[stackTop];
                                                if (rhs == DBL_MRK) {
                                                    rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                Object lhsx = stack[--stackTop];
                                                if (lhsx == DBL_MRK) {
                                                    lhsx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stack[stackTop] = ScriptRuntime.setObjectProp(cx, frame.scope, lhsx, stringReg, rhs);
                                                continue;
                                            case 36:
                                                stackTop = doGetElem(cx, frame, stack, sDbl, stackTop);
                                                continue;
                                            case 37:
                                                stackTop = doSetElem(cx, frame, stack, sDbl, stackTop);
                                                continue;
                                            case 39:
                                                stackTop++;
                                                stack[stackTop] = ScriptRuntime.name(cx, frame.scope, stringReg);
                                                continue;
                                            case 40:
                                                stackTop++;
                                                stack[stackTop] = DBL_MRK;
                                                sDbl[stackTop] = frame.idata.itsDoubleTable[indexReg];
                                                continue;
                                            case 41:
                                                stackTop++;
                                                stack[stackTop] = stringReg;
                                                continue;
                                            case 42:
                                                stackTop++;
                                                stack[stackTop] = null;
                                                continue;
                                            case 43:
                                                stackTop++;
                                                stack[stackTop] = frame.thisObj;
                                                continue;
                                            case 44:
                                                stackTop++;
                                                stack[stackTop] = Boolean.FALSE;
                                                continue;
                                            case 45:
                                                stackTop++;
                                                stack[stackTop] = Boolean.TRUE;
                                                continue;
                                            case 46:
                                            case 47:
                                                boolean valBlnxx = doShallowEquals(stack, sDbl, --stackTop, cx);
                                                valBlnxx ^= op == 47;
                                                stack[stackTop] = valBlnxx;
                                                continue;
                                            case 48:
                                                Object re = frame.idata.itsRegExpLiterals[indexReg];
                                                stackTop++;
                                                stack[stackTop] = ScriptRuntime.wrapRegExp(cx, frame.scope, re);
                                                continue;
                                            case 49:
                                                stackTop++;
                                                stack[stackTop] = ScriptRuntime.bind(cx, frame.scope, stringReg);
                                                continue;
                                            case 50:
                                                {
                                                    Object valuexxx = stack[stackTop];
                                                    if (valuexxx == DBL_MRK) {
                                                        valuexxx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                    }
                                                    stackTop--;
                                                    int sourceLine = getIndex(iCode, frame.pc);
                                                    throwable = new JavaScriptException(cx, valuexxx, frame.idata.itsSourceFile, sourceLine);
                                                    break label942;
                                                }
                                            case 51:
                                                indexReg += frame.localShift;
                                                throwable = stack[indexReg];
                                                break label942;
                                            case 52:
                                            case 53:
                                                stackTop = doInOrInstanceof(cx, op, stack, sDbl, stackTop);
                                                continue;
                                            case 54:
                                                stackTop++;
                                                indexReg += frame.localShift;
                                                stack[stackTop] = stack[indexReg];
                                                sDbl[stackTop] = sDbl[indexReg];
                                                continue;
                                            case 55:
                                                break label874;
                                            case 56:
                                                break label876;
                                            case 57:
                                                stackTop--;
                                                indexReg += frame.localShift;
                                                boolean afterFirstScope = frame.idata.itsICode[frame.pc] != 0;
                                                Throwable caughtException = (Throwable) stack[stackTop + 1];
                                                Scriptable lastCatchScope;
                                                if (!afterFirstScope) {
                                                    lastCatchScope = null;
                                                } else {
                                                    lastCatchScope = (Scriptable) stack[indexReg];
                                                }
                                                stack[indexReg] = ScriptRuntime.newCatchScope(cx, frame.scope, caughtException, lastCatchScope, stringReg);
                                                frame.pc++;
                                                continue;
                                            case 58:
                                            case 59:
                                            case 60:
                                            case 61:
                                                Object lhsx = stack[stackTop];
                                                if (lhsx == DBL_MRK) {
                                                    lhsx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stackTop--;
                                                indexReg += frame.localShift;
                                                int enumType = op == 58 ? 0 : (op == 59 ? 1 : (op == 61 ? 6 : 2));
                                                stack[indexReg] = ScriptRuntime.enumInit(cx, frame.scope, lhsx, enumType);
                                                continue;
                                            case 62:
                                            case 63:
                                                indexReg += frame.localShift;
                                                IdEnumeration val = (IdEnumeration) stack[indexReg];
                                                stackTop++;
                                                stack[stackTop] = op == 62 ? val.next(cx) : val.getId(cx);
                                                continue;
                                            case 64:
                                                stackTop++;
                                                stack[stackTop] = frame.fnOrScript;
                                                continue;
                                            case 65:
                                                break;
                                            case 68:
                                                {
                                                    Ref ref = (Ref) stack[stackTop];
                                                    stack[stackTop] = ScriptRuntime.refGet(cx, ref);
                                                    continue;
                                                }
                                            case 69:
                                                {
                                                    Object valuexxx = stack[stackTop];
                                                    if (valuexxx == DBL_MRK) {
                                                        valuexxx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                    }
                                                    Ref ref = (Ref) stack[--stackTop];
                                                    stack[stackTop] = ScriptRuntime.refSet(cx, frame.scope, ref, valuexxx);
                                                    continue;
                                                }
                                            case 70:
                                                {
                                                    Ref ref = (Ref) stack[stackTop];
                                                    stack[stackTop] = ScriptRuntime.refDel(cx, ref);
                                                    continue;
                                                }
                                            case 72:
                                                Object objx = stack[stackTop];
                                                if (objx == DBL_MRK) {
                                                    objx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stack[stackTop] = ScriptRuntime.specialRef(cx, frame.scope, objx, stringReg);
                                                continue;
                                            case 75:
                                                stackTop = doNullishCoalescing(frame, stack, sDbl, stackTop);
                                                continue;
                                            case 78:
                                                Object lhsx = stack[stackTop];
                                                if (lhsx == DBL_MRK) {
                                                    lhsx = ScriptRuntime.wrapNumber(sDbl[stackTop]);
                                                }
                                                stack[stackTop] = ScriptRuntime.getObjectPropOptional(cx, frame.scope, lhsx, stringReg);
                                                continue;
                                            case 157:
                                                break label878;
                                        }
                                        exitFrame(cx, frame, null);
                                        interpreterResult = frame.result;
                                        interpreterResultDbl = frame.resultDbl;
                                        if (frame.parentFrame != null) {
                                            frame = frame.parentFrame;
                                            if (frame.frozen) {
                                                frame = frame.cloneFrozen();
                                            }
                                            setCallResult(frame, interpreterResult, interpreterResultDbl);
                                            interpreterResult = null;
                                            continue label959;
                                        }
                                        break label1012;
                                    }
                                    if (instructionCounting) {
                                        addInstructionCount(cx, frame, 2);
                                    }
                                    int offset = getShort(iCode, frame.pc);
                                    if (offset != 0) {
                                        frame.pc += offset - 1;
                                    } else {
                                        frame.pc = frame.idata.longJumps.getExistingInt(frame.pc);
                                    }
                                    if (instructionCounting) {
                                        frame.pcPrevBranch = frame.pc;
                                    }
                                }
                                stackTop = doGetVar(frame, stack, sDbl, stackTop, vars, varDbls, indexReg, cx);
                            }
                            stackTop = doSetVar(cx, frame, stack, sDbl, stackTop, vars, varDbls, varAttributes, indexReg);
                        }
                        stackTop = doSetConstVar(frame, stack, sDbl, stackTop, vars, varDbls, varAttributes, indexReg, cx);
                    }
                } catch (Throwable var32) {
                    if (throwable != null) {
                        var32.printStackTrace(System.err);
                        throw new IllegalStateException();
                    }
                    throwable = var32;
                }
                if (throwable == null) {
                    Kit.codeBug();
                }
                int EX_CATCH_STATE = 2;
                int EX_FINALLY_STATE = 1;
                int EX_NO_JS_STATE = 0;
                int exState;
                if (generatorState != null && generatorState.operation == 2 && throwable == generatorState.value) {
                    exState = 1;
                } else if (throwable instanceof JavaScriptException) {
                    exState = 2;
                } else if (throwable instanceof EcmaError) {
                    exState = 2;
                } else if (throwable instanceof EvaluatorException) {
                    exState = 2;
                } else if (throwable instanceof RuntimeException) {
                    exState = 1;
                } else if (throwable instanceof Error) {
                    exState = 0;
                } else {
                    exState = 1;
                }
                if (instructionCounting) {
                    try {
                        addInstructionCount(cx, frame, 100);
                    } catch (RuntimeException var30) {
                        throwable = var30;
                        exState = 1;
                    } catch (Error var31) {
                        throwable = var31;
                        exState = 0;
                    }
                }
                do {
                    if (exState != 0) {
                        boolean onlyFinally = exState != 2;
                        indexReg = getExceptionHandler(frame, onlyFinally);
                        if (indexReg >= 0) {
                            continue label959;
                        }
                    }
                    exitFrame(cx, frame, throwable);
                    frame = frame.parentFrame;
                } while (frame != null);
            }
            if (cx.previousInterpreterInvocations != null && cx.previousInterpreterInvocations.size() != 0) {
                cx.lastInterpreterFrame = cx.previousInterpreterInvocations.pop();
            } else {
                cx.lastInterpreterFrame = null;
                cx.previousInterpreterInvocations = null;
            }
            if (throwable != null) {
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                }
                throw (Error) throwable;
            }
            return interpreterResult != DBL_MRK ? interpreterResult : ScriptRuntime.wrapNumber(interpreterResultDbl);
        }
    }

    private static int doInOrInstanceof(Context cx, int op, Object[] stack, double[] sDbl, int stackTop) {
        Object rhs = stack[stackTop];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        Object lhs = stack[--stackTop];
        if (lhs == UniqueTag.DOUBLE_MARK) {
            lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        boolean valBln;
        if (op == 52) {
            valBln = ScriptRuntime.in(cx, lhs, rhs);
        } else {
            valBln = ScriptRuntime.instanceOf(cx, lhs, rhs);
        }
        stack[stackTop] = valBln;
        return stackTop;
    }

    private static int doCompare(Interpreter.CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop, Context cx) {
        boolean valBln;
        label49: {
            Object rhs = stack[--stackTop + 1];
            Object lhs = stack[stackTop];
            double rDbl;
            double lDbl;
            if (rhs == UniqueTag.DOUBLE_MARK) {
                rDbl = sDbl[stackTop + 1];
                lDbl = stack_double(frame, stackTop, cx);
            } else {
                if (lhs != UniqueTag.DOUBLE_MARK) {
                    valBln = switch(op) {
                        case 14 ->
                            ScriptRuntime.cmp_LT(cx, lhs, rhs);
                        case 15 ->
                            ScriptRuntime.cmp_LE(cx, lhs, rhs);
                        case 16 ->
                            ScriptRuntime.cmp_LT(cx, rhs, lhs);
                        case 17 ->
                            ScriptRuntime.cmp_LE(cx, rhs, lhs);
                        default ->
                            throw Kit.codeBug();
                    };
                    break label49;
                }
                rDbl = ScriptRuntime.toNumber(cx, rhs);
                lDbl = sDbl[stackTop];
            }
            switch(op) {
                case 14:
                    valBln = lDbl < rDbl;
                    break;
                case 15:
                    valBln = lDbl <= rDbl;
                    break;
                case 16:
                    valBln = lDbl > rDbl;
                    break;
                case 17:
                    valBln = lDbl >= rDbl;
                    break;
                default:
                    throw Kit.codeBug();
            }
        }
        stack[stackTop] = valBln;
        return stackTop;
    }

    private static int doBitOp(Interpreter.CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop, Context cx) {
        int lIntValue = stack_int32(frame, stackTop - 1, cx);
        int rIntValue = stack_int32(frame, stackTop, cx);
        stackTop--;
        stack[stackTop] = UniqueTag.DOUBLE_MARK;
        sDbl[stackTop] = switch(op) {
            case 9 ->
                (double) (lIntValue | rIntValue);
            case 10 ->
                (double) (lIntValue ^ rIntValue);
            case 11 ->
                (double) (lIntValue & rIntValue);
            default ->
                (double) lIntValue;
            case 18 ->
                (double) (lIntValue << rIntValue);
            case 19 ->
                (double) (lIntValue >> rIntValue);
        };
        return stackTop;
    }

    private static int doNullishCoalescing(Interpreter.CallFrame frame, Object[] stack, double[] sDbl, int stackTop) {
        Object a = frame.stack[stackTop - 1];
        Object b = frame.stack[stackTop];
        stackTop--;
        stack[stackTop] = a != null && !Undefined.isUndefined(a) ? a : b;
        return stackTop;
    }

    private static int doDelName(Context cx, Interpreter.CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop) {
        Object rhs = stack[stackTop];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        Object lhs = stack[--stackTop];
        if (lhs == UniqueTag.DOUBLE_MARK) {
            lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stack[stackTop] = ScriptRuntime.delete(cx, frame.scope, lhs, rhs, op == 0);
        return stackTop;
    }

    private static int doGetElem(Context cx, Interpreter.CallFrame frame, Object[] stack, double[] sDbl, int stackTop) {
        Object lhs = stack[--stackTop];
        if (lhs == UniqueTag.DOUBLE_MARK) {
            lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        Object id = stack[stackTop + 1];
        Object value;
        if (id != UniqueTag.DOUBLE_MARK) {
            value = ScriptRuntime.getObjectElem(cx, frame.scope, lhs, id);
        } else {
            double d = sDbl[stackTop + 1];
            value = ScriptRuntime.getObjectIndex(cx, frame.scope, lhs, d);
        }
        stack[stackTop] = value;
        return stackTop;
    }

    private static int doSetElem(Context cx, Interpreter.CallFrame frame, Object[] stack, double[] sDbl, int stackTop) {
        stackTop -= 2;
        Object rhs = stack[stackTop + 2];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            rhs = ScriptRuntime.wrapNumber(sDbl[stackTop + 2]);
        }
        Object lhs = stack[stackTop];
        if (lhs == UniqueTag.DOUBLE_MARK) {
            lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        Object id = stack[stackTop + 1];
        Object value;
        if (id != UniqueTag.DOUBLE_MARK) {
            value = ScriptRuntime.setObjectElem(cx, frame.scope, lhs, id, rhs);
        } else {
            double d = sDbl[stackTop + 1];
            value = ScriptRuntime.setObjectIndex(cx, frame.scope, lhs, d, rhs);
        }
        stack[stackTop] = value;
        return stackTop;
    }

    private static int doElemIncDec(Context cx, Interpreter.CallFrame frame, byte[] iCode, Object[] stack, double[] sDbl, int stackTop) {
        Object rhs = stack[stackTop];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        Object lhs = stack[--stackTop];
        if (lhs == UniqueTag.DOUBLE_MARK) {
            lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stack[stackTop] = ScriptRuntime.elemIncrDecr(cx, lhs, rhs, frame.scope, iCode[frame.pc]);
        frame.pc++;
        return stackTop;
    }

    private static int doCallSpecial(Context cx, Interpreter.CallFrame frame, Object[] stack, double[] sDbl, int stackTop, byte[] iCode, int indexReg) {
        int callType = iCode[frame.pc] & 255;
        boolean isNew = iCode[frame.pc + 1] != 0;
        int sourceLine = getIndex(iCode, frame.pc + 2);
        if (isNew) {
            stackTop -= indexReg;
            Object function = stack[stackTop];
            if (function == UniqueTag.DOUBLE_MARK) {
                function = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            }
            Object[] outArgs = getArgsArray(stack, sDbl, stackTop + 1, indexReg);
            stack[stackTop] = ScriptRuntime.newSpecial(cx, frame.scope, function, outArgs, callType);
        } else {
            stackTop -= 1 + indexReg;
            Scriptable functionThis = (Scriptable) stack[stackTop + 1];
            Callable function = (Callable) stack[stackTop];
            Object[] outArgs = getArgsArray(stack, sDbl, stackTop + 2, indexReg);
            stack[stackTop] = ScriptRuntime.callSpecial(cx, frame.scope, function, functionThis, outArgs, frame.thisObj, callType, frame.idata.itsSourceFile, sourceLine);
        }
        frame.pc += 4;
        return stackTop;
    }

    private static int doSetConstVar(Interpreter.CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg, Context cx) {
        if (!frame.useActivation) {
            if ((varAttributes[indexReg] & 1) == 0) {
                throw Context.reportRuntimeError1("msg.var.redecl", frame.idata.argNames[indexReg], cx);
            }
            if ((varAttributes[indexReg] & 8) != 0) {
                vars[indexReg] = stack[stackTop];
                varAttributes[indexReg] &= -9;
                varDbls[indexReg] = sDbl[stackTop];
            }
        } else {
            Object val = stack[stackTop];
            if (val == UniqueTag.DOUBLE_MARK) {
                val = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            }
            String stringReg = frame.idata.argNames[indexReg];
            if (!(frame.scope instanceof ConstProperties cp)) {
                throw Kit.codeBug();
            }
            cp.putConst(cx, stringReg, frame.scope, val);
        }
        return stackTop;
    }

    private static int doSetVar(Context cx, Interpreter.CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg) {
        if (!frame.useActivation) {
            if ((varAttributes[indexReg] & 1) == 0) {
                vars[indexReg] = stack[stackTop];
                varDbls[indexReg] = sDbl[stackTop];
            }
        } else {
            Object val = stack[stackTop];
            if (val == UniqueTag.DOUBLE_MARK) {
                val = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            }
            String stringReg = frame.idata.argNames[indexReg];
            frame.scope.put(cx, stringReg, frame.scope, val);
        }
        return stackTop;
    }

    private static int doGetVar(Interpreter.CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int indexReg, Context cx) {
        stackTop++;
        if (!frame.useActivation) {
            stack[stackTop] = vars[indexReg];
            sDbl[stackTop] = varDbls[indexReg];
        } else {
            String stringReg = frame.idata.argNames[indexReg];
            stack[stackTop] = frame.scope.get(cx, stringReg, frame.scope);
        }
        return stackTop;
    }

    private static int doVarIncDec(Context cx, Interpreter.CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg) {
        stackTop++;
        int incrDecrMask = frame.idata.itsICode[frame.pc];
        if (!frame.useActivation) {
            Object varValue = vars[indexReg];
            double d;
            if (varValue == UniqueTag.DOUBLE_MARK) {
                d = varDbls[indexReg];
            } else {
                d = ScriptRuntime.toNumber(cx, varValue);
            }
            double d2 = (incrDecrMask & 1) == 0 ? d + 1.0 : d - 1.0;
            boolean post = (incrDecrMask & 2) != 0;
            if ((varAttributes[indexReg] & 1) == 0) {
                if (varValue != UniqueTag.DOUBLE_MARK) {
                    vars[indexReg] = UniqueTag.DOUBLE_MARK;
                }
                varDbls[indexReg] = d2;
                stack[stackTop] = UniqueTag.DOUBLE_MARK;
                sDbl[stackTop] = post ? d : d2;
            } else if (post && varValue != UniqueTag.DOUBLE_MARK) {
                stack[stackTop] = varValue;
            } else {
                stack[stackTop] = UniqueTag.DOUBLE_MARK;
                sDbl[stackTop] = post ? d : d2;
            }
        } else {
            String varName = frame.idata.argNames[indexReg];
            stack[stackTop] = ScriptRuntime.nameIncrDecr(cx, frame.scope, varName, incrDecrMask);
        }
        frame.pc++;
        return stackTop;
    }

    private static Interpreter.CallFrame initFrameForNoSuchMethod(Context cx, Interpreter.CallFrame frame, int indexReg, Object[] stack, double[] sDbl, int stackTop, int op, Scriptable funThisObj, Scriptable calleeScope, ScriptRuntime.NoSuchMethodShim noSuchMethodShim, InterpretedFunction ifun) {
        Object[] argsArray = null;
        int shift = stackTop + 2;
        Object[] elements = new Object[indexReg];
        for (int i = 0; i < indexReg; shift++) {
            Object val = stack[shift];
            if (val == UniqueTag.DOUBLE_MARK) {
                val = ScriptRuntime.wrapNumber(sDbl[shift]);
            }
            elements[i] = val;
            i++;
        }
        argsArray = new Object[] { noSuchMethodShim.methodName, cx.newArray(calleeScope, elements) };
        Interpreter.CallFrame callParentFrame = frame;
        if (op == -55) {
            callParentFrame = frame.parentFrame;
            exitFrame(cx, frame, null);
        }
        Interpreter.CallFrame calleeFrame = initFrame(cx, calleeScope, funThisObj, argsArray, null, 0, 2, ifun, callParentFrame);
        if (op != -55) {
            frame.savedStackTop = stackTop;
            frame.savedCallOp = op;
        }
        return calleeFrame;
    }

    private static boolean doEquals(Object[] stack, double[] sDbl, int stackTop, Context cx) {
        Object rhs = stack[stackTop + 1];
        Object lhs = stack[stackTop];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            return lhs == UniqueTag.DOUBLE_MARK ? sDbl[stackTop] == sDbl[stackTop + 1] : ScriptRuntime.eqNumber(cx, sDbl[stackTop + 1], lhs);
        } else {
            return lhs == UniqueTag.DOUBLE_MARK ? ScriptRuntime.eqNumber(cx, sDbl[stackTop], rhs) : ScriptRuntime.eq(cx, lhs, rhs);
        }
    }

    private static boolean doShallowEquals(Object[] stack, double[] sDbl, int stackTop, Context cx) {
        Object rhs = stack[stackTop + 1];
        Object lhs = stack[stackTop];
        Object DBL_MRK = UniqueTag.DOUBLE_MARK;
        double rdbl;
        double ldbl;
        if (rhs == DBL_MRK) {
            rdbl = sDbl[stackTop + 1];
            if (lhs == DBL_MRK) {
                ldbl = sDbl[stackTop];
            } else {
                if (!(lhs instanceof Number)) {
                    return false;
                }
                ldbl = ((Number) lhs).doubleValue();
            }
        } else {
            if (lhs != DBL_MRK) {
                return ScriptRuntime.shallowEq(cx, lhs, rhs);
            }
            ldbl = sDbl[stackTop];
            if (!(rhs instanceof Number)) {
                return false;
            }
            rdbl = ((Number) rhs).doubleValue();
        }
        return ldbl == rdbl;
    }

    private static Interpreter.CallFrame processThrowable(Context cx, Object throwable, Interpreter.CallFrame frame, int indexReg, boolean instructionCounting) {
        if (indexReg >= 0) {
            if (frame.frozen) {
                frame = frame.cloneFrozen();
            }
            int[] table = frame.idata.itsExceptionTable;
            frame.pc = table[indexReg + 2];
            if (instructionCounting) {
                frame.pcPrevBranch = frame.pc;
            }
            frame.savedStackTop = frame.emptyStackTop;
            int scopeLocal = frame.localShift + table[indexReg + 5];
            int exLocal = frame.localShift + table[indexReg + 4];
            frame.scope = (Scriptable) frame.stack[scopeLocal];
            frame.stack[exLocal] = throwable;
            throwable = null;
        }
        frame.throwable = throwable;
        return frame;
    }

    private static Object freezeGenerator(Context cx, Interpreter.CallFrame frame, int stackTop, GeneratorState generatorState, boolean yieldStar) {
        if (generatorState.operation == 2) {
            throw ScriptRuntime.typeError0(cx, "msg.yield.closing");
        } else {
            frame.frozen = true;
            frame.result = frame.stack[stackTop];
            frame.resultDbl = frame.sDbl[stackTop];
            frame.savedStackTop = stackTop;
            frame.pc--;
            ScriptRuntime.exitActivationFunction(cx);
            Object result = frame.result != UniqueTag.DOUBLE_MARK ? frame.result : ScriptRuntime.wrapNumber(frame.resultDbl);
            return yieldStar ? new ES6Generator.YieldStarResult(result) : result;
        }
    }

    private static Object thawGenerator(Context cx, Interpreter.CallFrame frame, int stackTop, GeneratorState generatorState, int op) {
        frame.frozen = false;
        int sourceLine = getIndex(frame.idata.itsICode, frame.pc);
        frame.pc += 2;
        if (generatorState.operation == 1) {
            return new JavaScriptException(cx, generatorState.value, frame.idata.itsSourceFile, sourceLine);
        } else if (generatorState.operation == 2) {
            return generatorState.value;
        } else if (generatorState.operation != 0) {
            throw Kit.codeBug();
        } else {
            if (op == 73 || op == -66) {
                frame.stack[stackTop] = generatorState.value;
            }
            return Scriptable.NOT_FOUND;
        }
    }

    private static Interpreter.CallFrame initFrameForApplyOrCall(Context cx, Interpreter.CallFrame frame, int indexReg, Object[] stack, double[] sDbl, int stackTop, int op, Scriptable calleeScope, IdFunctionObject ifun, InterpretedFunction iApplyCallable) {
        Scriptable applyThis;
        if (indexReg != 0) {
            Object obj = stack[stackTop + 2];
            if (obj == UniqueTag.DOUBLE_MARK) {
                obj = ScriptRuntime.wrapNumber(sDbl[stackTop + 2]);
            }
            applyThis = ScriptRuntime.toObjectOrNull(cx, obj, frame.scope);
        } else {
            applyThis = null;
        }
        if (applyThis == null) {
            applyThis = cx.getTopCallOrThrow();
        }
        if (op == -55) {
            exitFrame(cx, frame, null);
            frame = frame.parentFrame;
        } else {
            frame.savedStackTop = stackTop;
            frame.savedCallOp = op;
        }
        Interpreter.CallFrame calleeFrame;
        if (BaseFunction.isApply(ifun)) {
            Object[] callArgs = indexReg < 2 ? ScriptRuntime.EMPTY_OBJECTS : ScriptRuntime.getApplyArguments(cx, stack[stackTop + 3]);
            calleeFrame = initFrame(cx, calleeScope, applyThis, callArgs, null, 0, callArgs.length, iApplyCallable, frame);
        } else {
            for (int i = 1; i < indexReg; i++) {
                stack[stackTop + 1 + i] = stack[stackTop + 2 + i];
                sDbl[stackTop + 1 + i] = sDbl[stackTop + 2 + i];
            }
            int argCount = indexReg < 2 ? 0 : indexReg - 1;
            calleeFrame = initFrame(cx, calleeScope, applyThis, stack, sDbl, stackTop + 2, argCount, iApplyCallable, frame);
        }
        return calleeFrame;
    }

    private static Interpreter.CallFrame initFrame(Context cx, Scriptable callerScope, Scriptable thisObj, Object[] args, double[] argsDbl, int argShift, int argCount, InterpretedFunction fnOrScript, Interpreter.CallFrame parentFrame) {
        Interpreter.CallFrame frame = new Interpreter.CallFrame(cx, thisObj, fnOrScript, parentFrame);
        frame.initializeArgs(cx, callerScope, args, argsDbl, argShift, argCount);
        enterFrame(cx, frame, args, false);
        return frame;
    }

    private static void enterFrame(Context cx, Interpreter.CallFrame frame, Object[] args, boolean continuationRestart) {
        boolean usesActivation = frame.idata.itsNeedsActivation;
        if (usesActivation) {
            Scriptable scope = frame.scope;
            if (scope == null) {
                Kit.codeBug();
            } else if (continuationRestart) {
                while (scope instanceof NativeWith) {
                    scope = scope.getParentScope();
                    if (scope == null || frame.parentFrame != null && frame.parentFrame.scope == scope) {
                        Kit.codeBug();
                        break;
                    }
                }
            }
            if (usesActivation) {
                ScriptRuntime.enterActivationFunction(cx, scope);
            }
        }
    }

    private static void exitFrame(Context cx, Interpreter.CallFrame frame, Object throwable) {
        if (frame.idata.itsNeedsActivation) {
            ScriptRuntime.exitActivationFunction(cx);
        }
    }

    private static void setCallResult(Interpreter.CallFrame frame, Object callResult, double callResultDbl) {
        if (frame.savedCallOp == 38) {
            frame.stack[frame.savedStackTop] = callResult;
            frame.sDbl[frame.savedStackTop] = callResultDbl;
        } else if (frame.savedCallOp == 30) {
            if (callResult instanceof Scriptable) {
                frame.stack[frame.savedStackTop] = callResult;
            }
        } else {
            Kit.codeBug();
        }
        frame.savedCallOp = 0;
    }

    private static int stack_int32(Interpreter.CallFrame frame, int i, Context cx) {
        Object x = frame.stack[i];
        return x == UniqueTag.DOUBLE_MARK ? ScriptRuntime.toInt32(frame.sDbl[i]) : ScriptRuntime.toInt32(cx, x);
    }

    private static double stack_double(Interpreter.CallFrame frame, int i, Context cx) {
        Object x = frame.stack[i];
        return x != UniqueTag.DOUBLE_MARK ? ScriptRuntime.toNumber(cx, x) : frame.sDbl[i];
    }

    private static boolean stack_boolean(Interpreter.CallFrame frame, int i, Context cx) {
        Object x = Wrapper.unwrapped(frame.stack[i]);
        if (Boolean.TRUE.equals(x)) {
            return true;
        } else if (Boolean.FALSE.equals(x)) {
            return false;
        } else if (x == UniqueTag.DOUBLE_MARK) {
            double d = frame.sDbl[i];
            return !Double.isNaN(d) && d != 0.0;
        } else if (x == null || x == Undefined.instance) {
            return false;
        } else if (!(x instanceof Number)) {
            return ScriptRuntime.toBoolean(cx, x);
        } else {
            double d = ((Number) x).doubleValue();
            return !Double.isNaN(d) && d != 0.0;
        }
    }

    private static void doAdd(Object[] stack, double[] sDbl, int stackTop, Context cx) {
        Object rhs = stack[stackTop + 1];
        Object lhs = stack[stackTop];
        double d;
        boolean leftRightOrder;
        if (rhs == UniqueTag.DOUBLE_MARK) {
            d = sDbl[stackTop + 1];
            if (lhs == UniqueTag.DOUBLE_MARK) {
                sDbl[stackTop] += d;
                return;
            }
            leftRightOrder = true;
        } else {
            if (lhs != UniqueTag.DOUBLE_MARK) {
                if (lhs instanceof Scriptable || rhs instanceof Scriptable) {
                    stack[stackTop] = ScriptRuntime.add(cx, lhs, rhs);
                } else if (lhs instanceof CharSequence) {
                    if (rhs instanceof CharSequence) {
                        stack[stackTop] = new ConsString((CharSequence) lhs, (CharSequence) rhs);
                    } else {
                        stack[stackTop] = new ConsString((CharSequence) lhs, ScriptRuntime.toCharSequence(cx, rhs));
                    }
                } else if (rhs instanceof CharSequence) {
                    stack[stackTop] = new ConsString(ScriptRuntime.toCharSequence(cx, lhs), (CharSequence) rhs);
                } else {
                    double lDbl = lhs instanceof Number ? ((Number) lhs).doubleValue() : ScriptRuntime.toNumber(cx, lhs);
                    double rDbl = rhs instanceof Number ? ((Number) rhs).doubleValue() : ScriptRuntime.toNumber(cx, rhs);
                    stack[stackTop] = UniqueTag.DOUBLE_MARK;
                    sDbl[stackTop] = lDbl + rDbl;
                }
                return;
            }
            d = sDbl[stackTop];
            lhs = rhs;
            leftRightOrder = false;
        }
        if (lhs instanceof Scriptable) {
            rhs = ScriptRuntime.wrapNumber(d);
            if (!leftRightOrder) {
                Object tmp = lhs;
                lhs = rhs;
                rhs = tmp;
            }
            stack[stackTop] = ScriptRuntime.add(cx, lhs, rhs);
        } else if (lhs instanceof CharSequence) {
            CharSequence rstr = ScriptRuntime.numberToString(cx, d, 10);
            if (leftRightOrder) {
                stack[stackTop] = new ConsString((CharSequence) lhs, rstr);
            } else {
                stack[stackTop] = new ConsString(rstr, (CharSequence) lhs);
            }
        } else {
            double lDbl = lhs instanceof Number ? ((Number) lhs).doubleValue() : ScriptRuntime.toNumber(cx, lhs);
            stack[stackTop] = UniqueTag.DOUBLE_MARK;
            sDbl[stackTop] = lDbl + d;
        }
    }

    private static int doArithmetic(Context cx, Interpreter.CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop) {
        double rDbl = stack_double(frame, stackTop, cx);
        double lDbl = stack_double(frame, --stackTop, cx);
        stack[stackTop] = UniqueTag.DOUBLE_MARK;
        sDbl[stackTop] = switch(op) {
            case 22 ->
                lDbl - rDbl;
            case 23 ->
                lDbl * rDbl;
            case 24 ->
                lDbl / rDbl;
            case 25 ->
                lDbl % rDbl;
            case 76 ->
                Math.pow(lDbl, rDbl);
            default ->
                lDbl;
        };
        return stackTop;
    }

    private static Object[] getArgsArray(Object[] stack, double[] sDbl, int shift, int count) {
        if (count == 0) {
            return ScriptRuntime.EMPTY_OBJECTS;
        } else {
            Object[] args = new Object[count];
            int i = 0;
            while (i != count) {
                Object val = stack[shift];
                if (val == UniqueTag.DOUBLE_MARK) {
                    val = ScriptRuntime.wrapNumber(sDbl[shift]);
                }
                args[i] = val;
                i++;
                shift++;
            }
            return args;
        }
    }

    private static void addInstructionCount(Context cx, Interpreter.CallFrame frame, int extra) {
        cx.instructionCount = cx.instructionCount + frame.pc - frame.pcPrevBranch + extra;
        if (cx.instructionCount > cx.instructionThreshold) {
            cx.observeInstructionCount(cx.instructionCount);
            cx.instructionCount = 0;
        }
    }

    @Override
    public Object compile(CompilerEnvirons compilerEnv, ScriptNode tree, boolean returnFunction, Context cx) {
        CodeGenerator cgen = new CodeGenerator();
        this.itsData = cgen.compile(compilerEnv, tree, returnFunction, cx);
        return this.itsData;
    }

    @Override
    public Script createScriptObject(Object bytecode, Object staticSecurityDomain) {
        if (bytecode != this.itsData) {
            Kit.codeBug();
        }
        return InterpretedFunction.createScript(this.itsData, staticSecurityDomain);
    }

    @Override
    public void setEvalScriptFlag(Script script) {
        ((InterpretedFunction) script).idata.evalScriptFlag = true;
    }

    @Override
    public Function createFunctionObject(Context cx, Scriptable scope, Object bytecode, Object staticSecurityDomain) {
        if (bytecode != this.itsData) {
            Kit.codeBug();
        }
        return InterpretedFunction.createFunction(cx, scope, this.itsData, staticSecurityDomain);
    }

    @Override
    public void captureStackInfo(Context cx, RhinoException ex) {
        if (cx != null && cx.lastInterpreterFrame != null) {
            Interpreter.CallFrame[] array;
            if (cx.previousInterpreterInvocations != null && cx.previousInterpreterInvocations.size() != 0) {
                int previousCount = cx.previousInterpreterInvocations.size();
                if (cx.previousInterpreterInvocations.peek() == cx.lastInterpreterFrame) {
                    previousCount--;
                }
                array = new Interpreter.CallFrame[previousCount + 1];
                cx.previousInterpreterInvocations.toArray(array);
            } else {
                array = new Interpreter.CallFrame[1];
            }
            array[array.length - 1] = (Interpreter.CallFrame) cx.lastInterpreterFrame;
            int interpreterFrameCount = 0;
            for (int i = 0; i != array.length; i++) {
                interpreterFrameCount += 1 + array[i].frameIndex;
            }
            int[] linePC = new int[interpreterFrameCount];
            int linePCIndex = interpreterFrameCount;
            int i = array.length;
            while (i != 0) {
                for (Interpreter.CallFrame frame = array[--i]; frame != null; frame = frame.parentFrame) {
                    linePCIndex--;
                    linePC[linePCIndex] = frame.pcSourceLineStart;
                }
            }
            if (linePCIndex != 0) {
                Kit.codeBug();
            }
            ex.interpreterStackInfo = array;
            ex.interpreterLineData = linePC;
        } else {
            ex.interpreterStackInfo = null;
            ex.interpreterLineData = null;
        }
    }

    @Override
    public String getSourcePositionFromStack(Context cx, int[] linep) {
        Interpreter.CallFrame frame = (Interpreter.CallFrame) cx.lastInterpreterFrame;
        InterpreterData idata = frame.idata;
        if (frame.pcSourceLineStart >= 0) {
            linep[0] = getIndex(idata.itsICode, frame.pcSourceLineStart);
        } else {
            linep[0] = 0;
        }
        return idata.itsSourceFile;
    }

    @Override
    public String getPatchedStack(RhinoException ex, String nativeStackTrace) {
        String tag = "dev.latvian.mods.rhino.Interpreter.interpretLoop";
        StringBuilder sb = new StringBuilder(nativeStackTrace.length() + 1000);
        String lineSeparator = System.lineSeparator();
        Interpreter.CallFrame[] array = (Interpreter.CallFrame[]) ex.interpreterStackInfo;
        int[] linePC = ex.interpreterLineData;
        int arrayIndex = array.length;
        int linePCIndex = linePC.length;
        int offset = 0;
        while (arrayIndex != 0) {
            arrayIndex--;
            int pos = nativeStackTrace.indexOf(tag, offset);
            if (pos < 0) {
                break;
            }
            for (pos += tag.length(); pos != nativeStackTrace.length(); pos++) {
                char c = nativeStackTrace.charAt(pos);
                if (c == '\n' || c == '\r') {
                    break;
                }
            }
            sb.append(nativeStackTrace, offset, pos);
            offset = pos;
            for (Interpreter.CallFrame frame = array[arrayIndex]; frame != null; frame = frame.parentFrame) {
                if (linePCIndex == 0) {
                    Kit.codeBug();
                }
                linePCIndex--;
                InterpreterData idata = frame.idata;
                sb.append(lineSeparator);
                sb.append("\tat script");
                if (idata.itsName != null && idata.itsName.length() != 0) {
                    sb.append('.');
                    sb.append(idata.itsName);
                }
                sb.append('(');
                sb.append(idata.itsSourceFile);
                int pc = linePC[linePCIndex];
                if (pc >= 0) {
                    sb.append(':');
                    sb.append(getIndex(idata.itsICode, pc));
                }
                sb.append(')');
            }
        }
        sb.append(nativeStackTrace.substring(offset));
        return sb.toString();
    }

    @Override
    public List<String> getScriptStack(RhinoException ex) {
        ScriptStackElement[][] stack = this.getScriptStackElements(ex);
        List<String> list = new ArrayList(stack.length);
        String lineSeparator = System.lineSeparator();
        for (ScriptStackElement[] group : stack) {
            StringBuilder sb = new StringBuilder();
            for (ScriptStackElement elem : group) {
                elem.renderJavaStyle(sb);
                sb.append(lineSeparator);
            }
            list.add(sb.toString());
        }
        return list;
    }

    public ScriptStackElement[][] getScriptStackElements(RhinoException ex) {
        if (ex.interpreterStackInfo == null) {
            return null;
        } else {
            List<ScriptStackElement[]> list = new ArrayList();
            Interpreter.CallFrame[] array = (Interpreter.CallFrame[]) ex.interpreterStackInfo;
            int[] linePC = ex.interpreterLineData;
            int arrayIndex = array.length;
            int linePCIndex = linePC.length;
            while (arrayIndex != 0) {
                Interpreter.CallFrame frame = array[--arrayIndex];
                List<ScriptStackElement> group = new ArrayList();
                while (frame != null) {
                    if (linePCIndex == 0) {
                        Kit.codeBug();
                    }
                    linePCIndex--;
                    InterpreterData idata = frame.idata;
                    String fileName = idata.itsSourceFile;
                    String functionName = null;
                    int lineNumber = -1;
                    int pc = linePC[linePCIndex];
                    if (pc >= 0) {
                        lineNumber = getIndex(idata.itsICode, pc);
                    }
                    if (idata.itsName != null && idata.itsName.length() != 0) {
                        functionName = idata.itsName;
                    }
                    frame = frame.parentFrame;
                    group.add(new ScriptStackElement(fileName, functionName, lineNumber));
                }
                list.add((ScriptStackElement[]) group.toArray(new ScriptStackElement[0]));
            }
            return (ScriptStackElement[][]) list.toArray(new ScriptStackElement[list.size()][]);
        }
    }

    private static class CallFrame implements Cloneable {

        final Context localContext;

        final InterpretedFunction fnOrScript;

        final InterpreterData idata;

        final Interpreter.CallFrame varSource;

        final int localShift;

        final int emptyStackTop;

        final boolean useActivation;

        final Scriptable thisObj;

        Interpreter.CallFrame parentFrame;

        int frameIndex;

        boolean frozen;

        Object[] stack;

        int[] stackAttributes;

        double[] sDbl;

        boolean isContinuationsTopFrame;

        Object result;

        double resultDbl;

        int pc;

        int pcPrevBranch;

        int pcSourceLineStart;

        Scriptable scope;

        int savedStackTop;

        int savedCallOp;

        Object throwable;

        private static Boolean equals(Interpreter.CallFrame f1, Interpreter.CallFrame f2, EqualObjectGraphs equal, Context cx) {
            while (f1 != f2) {
                if (f1 == null || f2 == null) {
                    return Boolean.FALSE;
                }
                if (!f1.fieldsEqual(f2, equal, cx)) {
                    return Boolean.FALSE;
                }
                f1 = f1.parentFrame;
                f2 = f2.parentFrame;
            }
            return Boolean.TRUE;
        }

        CallFrame(Context cx, Scriptable thisObj, InterpretedFunction fnOrScript, Interpreter.CallFrame parentFrame) {
            this.localContext = cx;
            this.idata = fnOrScript.idata;
            this.useActivation = this.idata.itsNeedsActivation;
            this.emptyStackTop = this.idata.itsMaxVars + this.idata.itsMaxLocals - 1;
            this.fnOrScript = fnOrScript;
            this.varSource = this;
            this.localShift = this.idata.itsMaxVars;
            this.thisObj = thisObj;
            this.parentFrame = parentFrame;
            this.frameIndex = parentFrame == null ? 0 : parentFrame.frameIndex + 1;
            if (this.frameIndex > cx.getMaximumInterpreterStackDepth()) {
                throw Context.reportRuntimeError("Exceeded maximum stack depth", cx);
            } else {
                this.result = Undefined.instance;
                this.pcSourceLineStart = this.idata.firstLinePC;
                this.savedStackTop = this.emptyStackTop;
            }
        }

        void initializeArgs(Context cx, Scriptable callerScope, Object[] args, double[] argsDbl, int argShift, int argCount) {
            if (this.useActivation) {
                if (argsDbl != null) {
                    args = Interpreter.getArgsArray(args, argsDbl, argShift, argCount);
                }
                argShift = 0;
                argsDbl = null;
            }
            if (this.idata.itsFunctionType != 0) {
                this.scope = this.fnOrScript.getParentScope();
                if (this.useActivation) {
                    if (this.idata.itsFunctionType == 4) {
                        this.scope = ScriptRuntime.createArrowFunctionActivation(cx, this.scope, this.fnOrScript, args, this.idata.isStrict);
                    } else {
                        this.scope = ScriptRuntime.createFunctionActivation(cx, this.scope, this.fnOrScript, args, this.idata.isStrict);
                    }
                }
            } else {
                this.scope = callerScope;
                ScriptRuntime.initScript(cx, this.scope, this.fnOrScript, this.thisObj, this.fnOrScript.idata.evalScriptFlag);
            }
            if (this.idata.itsNestedFunctions != null) {
                if (this.idata.itsFunctionType != 0 && !this.idata.itsNeedsActivation) {
                    Kit.codeBug();
                }
                for (int i = 0; i < this.idata.itsNestedFunctions.length; i++) {
                    InterpreterData fdata = this.idata.itsNestedFunctions[i];
                    if (fdata.itsFunctionType == 1) {
                        Interpreter.initFunction(cx, this.scope, this.fnOrScript, i);
                    }
                }
            }
            int maxFrameArray = this.idata.itsMaxFrameArray;
            if (maxFrameArray != this.emptyStackTop + this.idata.itsMaxStack + 1) {
                Kit.codeBug();
            }
            this.stack = new Object[maxFrameArray];
            this.stackAttributes = new int[maxFrameArray];
            this.sDbl = new double[maxFrameArray];
            int varCount = this.idata.getParamAndVarCount();
            for (int ix = 0; ix < varCount; ix++) {
                if (this.idata.getParamOrVarConst(ix)) {
                    this.stackAttributes[ix] = 13;
                }
            }
            int definedArgs = this.idata.argCount;
            if (definedArgs > argCount) {
                definedArgs = argCount;
            }
            System.arraycopy(args, argShift, this.stack, 0, definedArgs);
            if (argsDbl != null) {
                System.arraycopy(argsDbl, argShift, this.sDbl, 0, definedArgs);
            }
            for (int ixx = definedArgs; ixx != this.idata.itsMaxVars; ixx++) {
                this.stack[ixx] = Undefined.instance;
            }
        }

        Interpreter.CallFrame cloneFrozen() {
            if (!this.frozen) {
                Kit.codeBug();
            }
            Interpreter.CallFrame copy;
            try {
                copy = (Interpreter.CallFrame) this.clone();
            } catch (CloneNotSupportedException var3) {
                throw new IllegalStateException();
            }
            copy.stack = (Object[]) this.stack.clone();
            copy.stackAttributes = (int[]) this.stackAttributes.clone();
            copy.sDbl = (double[]) this.sDbl.clone();
            copy.frozen = false;
            return copy;
        }

        public boolean equals(Object other) {
            if (other instanceof Interpreter.CallFrame otherCallFrame) {
                if (this.localContext.hasTopCallScope()) {
                    return this.equalsInTopScope(otherCallFrame);
                } else {
                    Scriptable top = ScriptableObject.getTopLevelScope(this.scope);
                    return (Boolean) this.localContext.doTopCall(top, (c, scope, thisObj, args) -> this.equalsInTopScope(otherCallFrame), top, ScriptRuntime.EMPTY_OBJECTS, this.isStrictTopFrame());
                }
            } else {
                return false;
            }
        }

        public int hashCode() {
            int depth = 0;
            Interpreter.CallFrame f = this;
            int h = 0;
            do {
                h = 31 * (31 * h + f.pc) + f.idata.icodeHashCode();
                f = f.parentFrame;
            } while (f != null && depth++ < 8);
            return h;
        }

        private Boolean equalsInTopScope(Interpreter.CallFrame other) {
            return EqualObjectGraphs.withThreadLocal(eq -> equals(this, other, eq, this.localContext));
        }

        private boolean isStrictTopFrame() {
            Interpreter.CallFrame f = this;
            while (true) {
                Interpreter.CallFrame p = f.parentFrame;
                if (p == null) {
                    return f.idata.isStrict;
                }
                f = p;
            }
        }

        private boolean fieldsEqual(Interpreter.CallFrame other, EqualObjectGraphs equal, Context cx) {
            return this.frameIndex == other.frameIndex && this.pc == other.pc && Interpreter.compareIdata(this.idata, other.idata) && equal.equalGraphs(cx, this.varSource.stack, other.varSource.stack) && Arrays.equals(this.varSource.sDbl, other.varSource.sDbl) && equal.equalGraphs(cx, this.thisObj, other.thisObj) && equal.equalGraphs(cx, this.fnOrScript, other.fnOrScript) && equal.equalGraphs(cx, this.scope, other.scope);
        }
    }
}