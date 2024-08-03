package dev.latvian.mods.unit.token;

import dev.latvian.mods.unit.FixedColorUnit;
import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitContext;
import dev.latvian.mods.unit.operator.GroupUnit;
import java.util.ArrayList;

public final class UnitTokenStream {

    public final UnitContext context;

    public final String input;

    public final CharStream charStream;

    public final ArrayList<UnitToken> infix;

    public final ArrayList<Integer> inputStringPos;

    public final Unit unit;

    private int infixPos;

    private static boolean isHex(char c) {
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
    }

    public UnitTokenStream(UnitContext context, String input) {
        this.context = context;
        this.input = input;
        this.charStream = new CharStream(input.toCharArray());
        this.infix = new ArrayList();
        this.inputStringPos = new ArrayList();
        this.infixPos = -1;
        StringBuilder current = new StringBuilder();
        while (true) {
            char c = this.charStream.next();
            if (c == 0) {
                if (current.length() > 0) {
                    this.inputStringPos.add(this.charStream.position - current.length());
                    this.infix.add(new StringUnitToken(current.toString()));
                    current.setLength(0);
                }
                ArrayList<Unit> interpretedUnits = new ArrayList();
                if (context.isDebug()) {
                    context.debugInfo("Infix", this.infix);
                }
                try {
                    do {
                        UnitToken unitToken = this.readFully();
                        interpretedUnits.add(unitToken.interpret(this));
                    } while (this.ifNextToken(UnitSymbol.SEMICOLON));
                } catch (UnitInterpretException var9) {
                    throw new RuntimeException("Error parsing '" + input + "' @ " + (this.infixPos >= 0 && this.infixPos < this.inputStringPos.size() ? (Integer) this.inputStringPos.get(this.infixPos) : -1), var9);
                }
                this.unit = (Unit) (interpretedUnits.size() == 1 ? (Unit) interpretedUnits.get(0) : new GroupUnit((Unit[]) interpretedUnits.toArray(Unit.EMPTY_ARRAY)));
                return;
            }
            int cpos = this.charStream.position;
            UnitSymbol symbol = UnitSymbol.read(c, this.charStream);
            if (symbol == UnitSymbol.HASH) {
                if (!isHex(this.charStream.peek(1)) || !isHex(this.charStream.peek(2)) || !isHex(this.charStream.peek(3)) || !isHex(this.charStream.peek(4)) || !isHex(this.charStream.peek(5)) || !isHex(this.charStream.peek(6))) {
                    throw new UnitParseException("Invalid color code @ " + this.charStream.position);
                }
                boolean alpha = isHex(this.charStream.peek(7)) && isHex(this.charStream.peek(8));
                current.append('#');
                for (int i = 0; i < (alpha ? 8 : 6); i++) {
                    current.append(this.charStream.next());
                }
                int color = Long.decode(current.toString()).intValue();
                current.setLength(0);
                this.inputStringPos.add(cpos);
                this.infix.add(FixedColorUnit.of(color, alpha));
            } else {
                if (symbol != null && current.length() > 0) {
                    this.inputStringPos.add(cpos);
                    this.infix.add(new StringUnitToken(current.toString()));
                    current.setLength(0);
                }
                UnitSymbol unary = symbol == null ? null : symbol.getUnarySymbol();
                if (unary == null || !this.infix.isEmpty() && !((UnitToken) this.infix.get(this.infix.size() - 1)).nextUnaryOperator()) {
                    if (symbol != null) {
                        this.inputStringPos.add(cpos);
                        this.infix.add(symbol);
                    } else {
                        current.append(c);
                    }
                } else {
                    this.inputStringPos.add(cpos);
                    this.infix.add(unary);
                }
            }
        }
    }

    public Unit getUnit() {
        return this.unit;
    }

    public String toString() {
        return this.infix.toString();
    }

    public UnitToken nextToken() {
        if (++this.infixPos >= this.infix.size()) {
            throw new UnitInterpretException("EOL!");
        } else {
            return (UnitToken) this.infix.get(this.infixPos);
        }
    }

    public UnitToken peekToken() {
        return this.infixPos + 1 >= this.infix.size() ? null : (UnitToken) this.infix.get(this.infixPos + 1);
    }

    public boolean ifNextToken(UnitToken token) {
        if (token.equals(this.peekToken())) {
            this.nextToken();
            return true;
        } else {
            return false;
        }
    }

    public UnitToken readFully() {
        PostfixUnitToken postfix = new PostfixUnitToken(new ArrayList());
        if (this.ifNextToken(UnitSymbol.LP)) {
            postfix.infix().add(this.readFully());
            if (!this.ifNextToken(UnitSymbol.RP)) {
                throw new UnitInterpretException("Expected ')', got '" + this.peekToken() + "'!");
            }
        } else {
            postfix.infix().add(this.readSingleToken());
        }
        while (this.peekToken() instanceof UnitSymbol symbol && symbol.op != null) {
            postfix.infix().add(this.nextToken());
            if (this.peekToken() == UnitSymbol.LP) {
                postfix.infix().add(this.readFully());
            } else {
                postfix.infix().add(this.readSingleToken());
            }
        }
        if (this.ifNextToken(UnitSymbol.HOOK)) {
            UnitToken left = this.readFully();
            if (!this.ifNextToken(UnitSymbol.COLON)) {
                throw new UnitInterpretException("Expected ':', got '" + this.peekToken() + "'!");
            } else {
                UnitToken right = this.readFully();
                return new TernaryUnitToken(postfix.normalize(), left, right);
            }
        } else {
            return postfix.normalize();
        }
    }

    public UnitToken readSingleToken() {
        UnitToken token = this.nextToken();
        if (token instanceof UnitSymbol symbol && symbol.unaryOp != null) {
            if (this.peekToken() == UnitSymbol.LP) {
                return new UnaryOpUnitToken(symbol, this.readFully());
            }
            return new UnaryOpUnitToken(symbol, this.readSingleToken());
        }
        if (token instanceof StringUnitToken str) {
            if (this.ifNextToken(UnitSymbol.LP)) {
                FunctionUnitToken func = new FunctionUnitToken(str.name(), new ArrayList());
                while (!this.ifNextToken(UnitSymbol.RP)) {
                    if (!this.ifNextToken(UnitSymbol.COMMA)) {
                        func.args().add(this.readFully());
                    }
                }
                return func;
            } else {
                return str;
            }
        } else if (token instanceof FixedColorUnit) {
            return token;
        } else {
            throw new UnitInterpretException("Unexpected token: " + token);
        }
    }
}