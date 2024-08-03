package dev.latvian.mods.unit.token;

import dev.latvian.mods.unit.Unit;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public record PostfixUnitToken(List<UnitToken> infix) implements UnitToken {

    @Override
    public Unit interpret(UnitTokenStream stream) {
        Stack<UnitSymbol> operatorsStack = new Stack();
        LinkedList<UnitToken> postfix = new LinkedList();
        for (UnitToken next : this.infix) {
            if (stream.context.isDebug()) {
                stream.context.debugInfo("> " + next);
            }
            if (!(next instanceof UnitSymbol nextOperator)) {
                postfix.add(next);
                if (stream.context.isDebug()) {
                    stream.context.debugInfo("Operator Stack", operatorsStack);
                    stream.context.debugInfo("Operand Stack", postfix);
                }
            } else {
                boolean pushedCurrent = false;
                while (true) {
                    if (!operatorsStack.isEmpty()) {
                        UnitSymbol o = (UnitSymbol) operatorsStack.peek();
                        if (o != null) {
                            if (o.hasHigherPrecedenceThan(nextOperator)) {
                                postfix.add((UnitToken) operatorsStack.pop());
                                if (stream.context.isDebug()) {
                                    stream.context.debugInfo("Operator Stack", operatorsStack);
                                    stream.context.debugInfo("Operand Stack", postfix);
                                }
                                continue;
                            }
                            pushedCurrent = true;
                            operatorsStack.push(nextOperator);
                            if (stream.context.isDebug()) {
                                stream.context.debugInfo("Operator Stack", operatorsStack);
                                stream.context.debugInfo("Operand Stack", postfix);
                            }
                        }
                    }
                    if (!pushedCurrent) {
                        operatorsStack.push(nextOperator);
                        if (stream.context.isDebug()) {
                            stream.context.debugInfo("Operator Stack", operatorsStack);
                            stream.context.debugInfo("Operand Stack", postfix);
                        }
                    }
                    break;
                }
            }
        }
        while (!operatorsStack.isEmpty()) {
            UnitSymbol last = (UnitSymbol) operatorsStack.pop();
            postfix.add(last);
        }
        if (stream.context.isDebug()) {
            stream.context.debugInfo("Postfix", postfix);
        }
        Stack<UnitToken> resultStack = new Stack();
        for (UnitToken token : postfix) {
            token.unstack(resultStack);
            if (stream.context.isDebug()) {
                stream.context.debugInfo("Result Stack", resultStack);
            }
        }
        UnitToken lastUnit = (UnitToken) resultStack.pop();
        return lastUnit.interpret(stream);
    }

    public UnitToken normalize() {
        if (this.infix.size() == 1) {
            return (UnitToken) this.infix.get(0);
        } else {
            return (UnitToken) (this.infix.size() == 3 && this.infix.get(1) instanceof UnitSymbol symbol && symbol.op != null ? new OpResultUnitToken(symbol, (UnitToken) this.infix.get(0), (UnitToken) this.infix.get(2)) : this);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int i = 0; i < this.infix.size(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(this.infix.get(i));
        }
        sb.append(')');
        return sb.toString();
    }
}