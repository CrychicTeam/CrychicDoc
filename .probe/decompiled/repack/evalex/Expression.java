package repack.evalex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Expression {

    public static final BigDecimal PI = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");

    private MathContext mc = MathContext.DECIMAL32;

    private String expression = null;

    private List<String> rpn = null;

    private Map<String, Expression.Operator> operators = new HashMap();

    private Map<String, Expression.Function> functions = new HashMap();

    private Map<String, BigDecimal> variables = new HashMap();

    private final char decimalSeparator = '.';

    private final char minusSign = '-';

    public Expression(String expression) {
        this.expression = expression;
        this.addOperator(new Expression.Operator("+", 20, true) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return v1.add(v2, Expression.this.mc);
            }
        });
        this.addOperator(new Expression.Operator("-", 20, true) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return v1.subtract(v2, Expression.this.mc);
            }
        });
        this.addOperator(new Expression.Operator("*", 30, true) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return v1.multiply(v2, Expression.this.mc);
            }
        });
        this.addOperator(new Expression.Operator("/", 30, true) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return v1.divide(v2, Expression.this.mc);
            }
        });
        this.addOperator(new Expression.Operator("%", 30, true) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return v1.remainder(v2, Expression.this.mc);
            }
        });
        this.addOperator(new Expression.Operator("^", 40, false) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                int signOf2 = v2.signum();
                double dn1 = v1.doubleValue();
                v2 = v2.multiply(new BigDecimal(signOf2));
                BigDecimal remainderOf2 = v2.remainder(BigDecimal.ONE);
                BigDecimal n2IntPart = v2.subtract(remainderOf2);
                BigDecimal intPow = v1.pow(n2IntPart.intValueExact(), Expression.this.mc);
                BigDecimal doublePow = new BigDecimal(Math.pow(dn1, remainderOf2.doubleValue()));
                BigDecimal result = intPow.multiply(doublePow, Expression.this.mc);
                if (signOf2 == -1) {
                    result = BigDecimal.ONE.divide(result, Expression.this.mc.getPrecision(), RoundingMode.HALF_UP);
                }
                return result;
            }
        });
        this.addOperator(new Expression.Operator("&&", 4, false) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                boolean b1 = !v1.equals(BigDecimal.ZERO);
                boolean b2 = !v2.equals(BigDecimal.ZERO);
                return b1 && b2 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Expression.Operator("||", 2, false) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                boolean b1 = !v1.equals(BigDecimal.ZERO);
                boolean b2 = !v2.equals(BigDecimal.ZERO);
                return !b1 && !b2 ? BigDecimal.ZERO : BigDecimal.ONE;
            }
        });
        this.addOperator(new Expression.Operator(">", 10, false) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return v1.compareTo(v2) == 1 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Expression.Operator(">=", 10, false) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return v1.compareTo(v2) >= 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Expression.Operator("<", 10, false) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return v1.compareTo(v2) == -1 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Expression.Operator("<=", 10, false) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return v1.compareTo(v2) <= 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Expression.Operator("=", 7, false) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return v1.compareTo(v2) == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Expression.Operator("==", 7, false) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return ((Expression.Operator) Expression.this.operators.get("=")).eval(v1, v2);
            }
        });
        this.addOperator(new Expression.Operator("!=", 7, false) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return v1.compareTo(v2) != 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Expression.Operator("<>", 7, false) {

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return ((Expression.Operator) Expression.this.operators.get("!=")).eval(v1, v2);
            }
        });
        this.addFunction(new Expression.Function("NOT", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                boolean zero = ((BigDecimal) parameters.get(0)).compareTo(BigDecimal.ZERO) == 0;
                return zero ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addFunction(new Expression.Function("IF", 3) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                boolean isTrue = !((BigDecimal) parameters.get(0)).equals(BigDecimal.ZERO);
                return isTrue ? (BigDecimal) parameters.get(1) : (BigDecimal) parameters.get(2);
            }
        });
        this.addFunction(new Expression.Function("RANDOM", 0) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                double d = Math.random();
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("SIN", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                double d = Math.sin(Math.toRadians(((BigDecimal) parameters.get(0)).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("COS", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                double d = Math.cos(Math.toRadians(((BigDecimal) parameters.get(0)).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("TAN", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                double d = Math.tan(Math.toRadians(((BigDecimal) parameters.get(0)).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("SINH", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                double d = Math.sinh(((BigDecimal) parameters.get(0)).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("COSH", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                double d = Math.cosh(((BigDecimal) parameters.get(0)).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("TANH", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                double d = Math.tanh(((BigDecimal) parameters.get(0)).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("RAD", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                double d = Math.toRadians(((BigDecimal) parameters.get(0)).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("DEG", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                double d = Math.toDegrees(((BigDecimal) parameters.get(0)).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("MAX", 2) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                BigDecimal v1 = (BigDecimal) parameters.get(0);
                BigDecimal v2 = (BigDecimal) parameters.get(1);
                return v1.compareTo(v2) > 0 ? v1 : v2;
            }
        });
        this.addFunction(new Expression.Function("MIN", 2) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                BigDecimal v1 = (BigDecimal) parameters.get(0);
                BigDecimal v2 = (BigDecimal) parameters.get(1);
                return v1.compareTo(v2) < 0 ? v1 : v2;
            }
        });
        this.addFunction(new Expression.Function("ABS", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                return ((BigDecimal) parameters.get(0)).abs(Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("LOG", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                double d = Math.log(((BigDecimal) parameters.get(0)).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("LOG10", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                double d = Math.log10(((BigDecimal) parameters.get(0)).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Expression.Function("ROUND", 2) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                BigDecimal toRound = (BigDecimal) parameters.get(0);
                int precision = ((BigDecimal) parameters.get(1)).intValue();
                return toRound.setScale(precision, Expression.this.mc.getRoundingMode());
            }
        });
        this.addFunction(new Expression.Function("FLOOR", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                BigDecimal toRound = (BigDecimal) parameters.get(0);
                return toRound.setScale(0, RoundingMode.FLOOR);
            }
        });
        this.addFunction(new Expression.Function("CEILING", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                BigDecimal toRound = (BigDecimal) parameters.get(0);
                return toRound.setScale(0, RoundingMode.CEILING);
            }
        });
        this.addFunction(new Expression.Function("SQRT", 1) {

            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                BigDecimal x = (BigDecimal) parameters.get(0);
                if (x.compareTo(BigDecimal.ZERO) == 0) {
                    return new BigDecimal(0);
                } else if (x.signum() < 0) {
                    throw Expression.this.new ExpressionException("Argument to SQRT() function must not be negative");
                } else {
                    BigInteger n = x.movePointRight(Expression.this.mc.getPrecision() << 1).toBigInteger();
                    int bits = n.bitLength() + 1 >> 1;
                    BigInteger ix = n.shiftRight(bits);
                    BigInteger ixPrev;
                    do {
                        ixPrev = ix;
                        ix = ix.add(n.divide(ix)).shiftRight(1);
                        Thread.yield();
                    } while (ix.compareTo(ixPrev) != 0);
                    return new BigDecimal(ix, Expression.this.mc.getPrecision());
                }
            }
        });
        this.variables.put("PI", PI);
        this.variables.put("TRUE", BigDecimal.ONE);
        this.variables.put("FALSE", BigDecimal.ZERO);
    }

    private boolean isNumber(String st) {
        if (st.charAt(0) == '-' && st.length() == 1) {
            return false;
        } else {
            for (char ch : st.toCharArray()) {
                if (!Character.isDigit(ch) && ch != '-' && ch != '.') {
                    return false;
                }
            }
            return true;
        }
    }

    private List<String> shuntingYard(String expression) {
        List<String> outputQueue = new ArrayList();
        Stack<String> stack = new Stack();
        Expression.Tokenizer tokenizer = new Expression.Tokenizer(expression);
        String lastFunction = null;
        String previousToken = null;
        while (tokenizer.hasNext()) {
            String token = tokenizer.next();
            if (this.isNumber(token)) {
                outputQueue.add(token);
            } else if (this.variables.containsKey(token)) {
                outputQueue.add(token);
            } else if (this.functions.containsKey(token.toUpperCase())) {
                stack.push(token);
                lastFunction = token;
            } else if (Character.isLetter(token.charAt(0))) {
                stack.push(token);
            } else if (",".equals(token)) {
                while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                    outputQueue.add((String) stack.pop());
                }
                if (stack.isEmpty()) {
                    throw new Expression.ExpressionException("Parse error for function '" + lastFunction + "'");
                }
            } else if (this.operators.containsKey(token)) {
                Expression.Operator o1 = (Expression.Operator) this.operators.get(token);
                for (String token2 = stack.isEmpty() ? null : (String) stack.peek(); this.operators.containsKey(token2) && (o1.isLeftAssoc() && o1.getPrecedence() <= ((Expression.Operator) this.operators.get(token2)).getPrecedence() || o1.getPrecedence() < ((Expression.Operator) this.operators.get(token2)).getPrecedence()); token2 = stack.isEmpty() ? null : (String) stack.peek()) {
                    outputQueue.add((String) stack.pop());
                }
                stack.push(token);
            } else if ("(".equals(token)) {
                if (previousToken != null && this.isNumber(previousToken)) {
                    throw new Expression.ExpressionException("Missing operator at character position " + tokenizer.getPos());
                }
                stack.push(token);
            } else if (")".equals(token)) {
                while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                    outputQueue.add((String) stack.pop());
                }
                if (stack.isEmpty()) {
                    throw new RuntimeException("Mismatched parentheses");
                }
                stack.pop();
                if (!stack.isEmpty() && this.functions.containsKey(((String) stack.peek()).toUpperCase())) {
                    outputQueue.add((String) stack.pop());
                }
            }
            previousToken = token;
        }
        while (!stack.isEmpty()) {
            String element = (String) stack.pop();
            if ("(".equals(element) || ")".equals(element)) {
                throw new RuntimeException("Mismatched parentheses");
            }
            if (!this.operators.containsKey(element)) {
                throw new RuntimeException("Unknown operator or function: " + element);
            }
            outputQueue.add(element);
        }
        return outputQueue;
    }

    public BigDecimal eval() {
        Stack<BigDecimal> stack = new Stack();
        for (String token : this.getRPN()) {
            if (this.operators.containsKey(token)) {
                BigDecimal v1 = (BigDecimal) stack.pop();
                BigDecimal v2 = (BigDecimal) stack.pop();
                stack.push(((Expression.Operator) this.operators.get(token)).eval(v2, v1));
            } else if (this.variables.containsKey(token)) {
                stack.push(((BigDecimal) this.variables.get(token)).round(this.mc));
            } else if (!this.functions.containsKey(token.toUpperCase())) {
                stack.push(new BigDecimal(token, this.mc));
            } else {
                Expression.Function f = (Expression.Function) this.functions.get(token.toUpperCase());
                ArrayList<BigDecimal> p = new ArrayList(f.getNumParams());
                for (int i = 0; i < f.numParams; i++) {
                    p.add(0, (BigDecimal) stack.pop());
                }
                BigDecimal fResult = f.eval(p);
                stack.push(fResult);
            }
        }
        return ((BigDecimal) stack.pop()).stripTrailingZeros();
    }

    public Expression setPrecision(int precision) {
        this.mc = new MathContext(precision);
        return this;
    }

    public Expression setRoundingMode(RoundingMode roundingMode) {
        this.mc = new MathContext(this.mc.getPrecision(), roundingMode);
        return this;
    }

    public Expression.Operator addOperator(Expression.Operator operator) {
        return (Expression.Operator) this.operators.put(operator.getOper(), operator);
    }

    public Expression.Function addFunction(Expression.Function function) {
        return (Expression.Function) this.functions.put(function.getName(), function);
    }

    public Expression setVariable(String variable, BigDecimal value) {
        this.variables.put(variable, value);
        return this;
    }

    public Expression setVariable(String variable, String value) {
        if (this.isNumber(value)) {
            this.variables.put(variable, new BigDecimal(value));
        } else {
            this.expression = this.expression.replaceAll("\\b" + variable + "\\b", "(" + value + ")");
            this.rpn = null;
        }
        return this;
    }

    public Expression with(String variable, BigDecimal value) {
        return this.setVariable(variable, value);
    }

    public Expression and(String variable, String value) {
        return this.setVariable(variable, value);
    }

    public Expression and(String variable, BigDecimal value) {
        return this.setVariable(variable, value);
    }

    public Expression with(String variable, String value) {
        return this.setVariable(variable, value);
    }

    public Iterator<String> getExpressionTokenizer() {
        return new Expression.Tokenizer(this.expression);
    }

    private List<String> getRPN() {
        if (this.rpn == null) {
            this.rpn = this.shuntingYard(this.expression);
        }
        return this.rpn;
    }

    public String toRPN() {
        String result = new String();
        for (String st : this.getRPN()) {
            result = result.isEmpty() ? result : result + " ";
            result = result + st;
        }
        return result;
    }

    public class ExpressionException extends RuntimeException {

        private static final long serialVersionUID = 1118142866870779047L;

        public ExpressionException(String message) {
            super(message);
        }
    }

    public abstract class Function {

        private String name;

        private int numParams;

        public Function(String name, int numParams) {
            this.name = name.toUpperCase();
            this.numParams = numParams;
        }

        public String getName() {
            return this.name;
        }

        public int getNumParams() {
            return this.numParams;
        }

        public abstract BigDecimal eval(List<BigDecimal> var1);
    }

    public abstract class Operator {

        private String oper;

        private int precedence;

        private boolean leftAssoc;

        public Operator(String oper, int precedence, boolean leftAssoc) {
            this.oper = oper;
            this.precedence = precedence;
            this.leftAssoc = leftAssoc;
        }

        public String getOper() {
            return this.oper;
        }

        public int getPrecedence() {
            return this.precedence;
        }

        public boolean isLeftAssoc() {
            return this.leftAssoc;
        }

        public abstract BigDecimal eval(BigDecimal var1, BigDecimal var2);
    }

    private class Tokenizer implements Iterator<String> {

        private int pos = 0;

        private String input;

        private String previousToken;

        public Tokenizer(String input) {
            this.input = input.trim();
        }

        public boolean hasNext() {
            return this.pos < this.input.length();
        }

        private char peekNextChar() {
            return this.pos < this.input.length() - 1 ? this.input.charAt(this.pos + 1) : '\u0000';
        }

        public String next() {
            StringBuilder token = new StringBuilder();
            if (this.pos >= this.input.length()) {
                return this.previousToken = null;
            } else {
                char ch = this.input.charAt(this.pos);
                while (Character.isWhitespace(ch) && this.pos < this.input.length()) {
                    ch = this.input.charAt(++this.pos);
                }
                if (Character.isDigit(ch)) {
                    while ((Character.isDigit(ch) || ch == '.') && this.pos < this.input.length()) {
                        token.append(this.input.charAt(this.pos++));
                        ch = this.pos == this.input.length() ? 0 : this.input.charAt(this.pos);
                    }
                } else if (ch != '-' || !Character.isDigit(this.peekNextChar()) || !"(".equals(this.previousToken) && !",".equals(this.previousToken) && this.previousToken != null && !Expression.this.operators.containsKey(this.previousToken)) {
                    if (!Character.isLetter(ch) && ch != '_') {
                        if (ch != '(' && ch != ')' && ch != ',') {
                            while (!Character.isLetter(ch) && !Character.isDigit(ch) && ch != '_' && !Character.isWhitespace(ch) && ch != '(' && ch != ')' && ch != ',' && this.pos < this.input.length()) {
                                token.append(this.input.charAt(this.pos));
                                this.pos++;
                                ch = this.pos == this.input.length() ? 0 : this.input.charAt(this.pos);
                                if (ch == '-') {
                                    break;
                                }
                            }
                            if (!Expression.this.operators.containsKey(token.toString())) {
                                throw Expression.this.new ExpressionException("Unknown operator '" + token + "' at position " + (this.pos - token.length() + 1));
                            }
                        } else {
                            token.append(ch);
                            this.pos++;
                        }
                    } else {
                        while ((Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') && this.pos < this.input.length()) {
                            token.append(this.input.charAt(this.pos++));
                            ch = this.pos == this.input.length() ? 0 : this.input.charAt(this.pos);
                        }
                    }
                } else {
                    token.append('-');
                    this.pos++;
                    token.append(this.next());
                }
                return this.previousToken = token.toString();
            }
        }

        public void remove() {
            throw Expression.this.new ExpressionException("remove() not supported");
        }

        public int getPos() {
            return this.pos;
        }
    }
}