package de.keksuccino.konkrete.objecthunter.exp4j.tokenizer;

import de.keksuccino.konkrete.objecthunter.exp4j.function.Function;
import de.keksuccino.konkrete.objecthunter.exp4j.function.Functions;
import de.keksuccino.konkrete.objecthunter.exp4j.operator.Operator;
import de.keksuccino.konkrete.objecthunter.exp4j.operator.Operators;
import java.util.Map;
import java.util.Set;

public class Tokenizer {

    private final char[] expression;

    private final int expressionLength;

    private final Map<String, Function> userFunctions;

    private final Map<String, Operator> userOperators;

    private final Set<String> variableNames;

    private final boolean implicitMultiplication;

    private int pos = 0;

    private Token lastToken;

    public Tokenizer(String expression, Map<String, Function> userFunctions, Map<String, Operator> userOperators, Set<String> variableNames, boolean implicitMultiplication) {
        this.expression = expression.trim().toCharArray();
        this.expressionLength = this.expression.length;
        this.userFunctions = userFunctions;
        this.userOperators = userOperators;
        this.variableNames = variableNames;
        this.implicitMultiplication = implicitMultiplication;
    }

    public Tokenizer(String expression, Map<String, Function> userFunctions, Map<String, Operator> userOperators, Set<String> variableNames) {
        this.expression = expression.trim().toCharArray();
        this.expressionLength = this.expression.length;
        this.userFunctions = userFunctions;
        this.userOperators = userOperators;
        this.variableNames = variableNames;
        this.implicitMultiplication = true;
    }

    public boolean hasNext() {
        return this.expression.length > this.pos;
    }

    public Token nextToken() {
        char ch = this.expression[this.pos];
        while (Character.isWhitespace(ch)) {
            ch = this.expression[++this.pos];
        }
        if (!Character.isDigit(ch) && ch != '.') {
            if (this.isArgumentSeparator(ch)) {
                return this.parseArgumentSeparatorToken();
            } else if (this.isOpenParentheses(ch)) {
                if (this.lastToken != null && this.implicitMultiplication && this.lastToken.getType() != 2 && this.lastToken.getType() != 4 && this.lastToken.getType() != 3 && this.lastToken.getType() != 7) {
                    this.lastToken = new OperatorToken(Operators.getBuiltinOperator('*', 2));
                    return this.lastToken;
                } else {
                    return this.parseParentheses(true);
                }
            } else if (this.isCloseParentheses(ch)) {
                return this.parseParentheses(false);
            } else if (Operator.isAllowedOperatorChar(ch)) {
                return this.parseOperatorToken(ch);
            } else if (!isAlphabetic(ch) && ch != '_') {
                throw new IllegalArgumentException("Unable to parse char '" + ch + "' (Code:" + ch + ") at [" + this.pos + "]");
            } else if (this.lastToken != null && this.implicitMultiplication && this.lastToken.getType() != 2 && this.lastToken.getType() != 4 && this.lastToken.getType() != 3 && this.lastToken.getType() != 7) {
                this.lastToken = new OperatorToken(Operators.getBuiltinOperator('*', 2));
                return this.lastToken;
            } else {
                return this.parseFunctionOrVariable();
            }
        } else {
            if (this.lastToken != null) {
                if (this.lastToken.getType() == 1) {
                    throw new IllegalArgumentException("Unable to parse char '" + ch + "' (Code:" + ch + ") at [" + this.pos + "]");
                }
                if (this.implicitMultiplication && this.lastToken.getType() != 2 && this.lastToken.getType() != 4 && this.lastToken.getType() != 3 && this.lastToken.getType() != 7) {
                    this.lastToken = new OperatorToken(Operators.getBuiltinOperator('*', 2));
                    return this.lastToken;
                }
            }
            return this.parseNumberToken(ch);
        }
    }

    private Token parseArgumentSeparatorToken() {
        this.pos++;
        this.lastToken = new ArgumentSeparatorToken();
        return this.lastToken;
    }

    private boolean isArgumentSeparator(char ch) {
        return ch == ',';
    }

    private Token parseParentheses(boolean open) {
        if (open) {
            this.lastToken = new OpenParenthesesToken();
        } else {
            this.lastToken = new CloseParenthesesToken();
        }
        this.pos++;
        return this.lastToken;
    }

    private boolean isOpenParentheses(char ch) {
        return ch == '(' || ch == '{' || ch == '[';
    }

    private boolean isCloseParentheses(char ch) {
        return ch == ')' || ch == '}' || ch == ']';
    }

    private Token parseFunctionOrVariable() {
        int offset = this.pos;
        int lastValidLen = 1;
        Token lastValidToken = null;
        int len = 1;
        if (this.isEndOfExpression(offset)) {
            this.pos++;
        }
        for (int testPos = offset + len - 1; !this.isEndOfExpression(testPos) && isVariableOrFunctionCharacter(this.expression[testPos]); testPos = offset + len - 1) {
            String name = new String(this.expression, offset, len);
            if (this.variableNames != null && this.variableNames.contains(name)) {
                lastValidLen = len;
                lastValidToken = new VariableToken(name);
            } else {
                Function f = this.getFunction(name);
                if (f != null) {
                    lastValidLen = len;
                    lastValidToken = new FunctionToken(f);
                }
            }
            len++;
        }
        if (lastValidToken == null) {
            throw new UnknownFunctionOrVariableException(new String(this.expression), this.pos, len);
        } else {
            this.pos += lastValidLen;
            this.lastToken = lastValidToken;
            return this.lastToken;
        }
    }

    private Function getFunction(String name) {
        Function f = null;
        if (this.userFunctions != null) {
            f = (Function) this.userFunctions.get(name);
        }
        if (f == null) {
            f = Functions.getBuiltinFunction(name);
        }
        return f;
    }

    private Token parseOperatorToken(char firstChar) {
        int offset = this.pos;
        int len = 1;
        StringBuilder symbol = new StringBuilder();
        Operator lastValid = null;
        symbol.append(firstChar);
        while (!this.isEndOfExpression(offset + len) && Operator.isAllowedOperatorChar(this.expression[offset + len])) {
            symbol.append(this.expression[offset + len++]);
        }
        while (symbol.length() > 0) {
            Operator op = this.getOperator(symbol.toString());
            if (op != null) {
                lastValid = op;
                break;
            }
            symbol.setLength(symbol.length() - 1);
        }
        this.pos = this.pos + symbol.length();
        this.lastToken = new OperatorToken(lastValid);
        return this.lastToken;
    }

    private Operator getOperator(String symbol) {
        Operator op = null;
        if (this.userOperators != null) {
            op = (Operator) this.userOperators.get(symbol);
        }
        if (op == null && symbol.length() == 1) {
            int argc = 2;
            if (this.lastToken == null) {
                argc = 1;
            } else {
                int lastTokenType = this.lastToken.getType();
                if (lastTokenType != 4 && lastTokenType != 7) {
                    if (lastTokenType == 2) {
                        Operator lastOp = ((OperatorToken) this.lastToken).getOperator();
                        if (lastOp.getNumOperands() == 2 || lastOp.getNumOperands() == 1 && !lastOp.isLeftAssociative()) {
                            argc = 1;
                        }
                    }
                } else {
                    argc = 1;
                }
            }
            op = Operators.getBuiltinOperator(symbol.charAt(0), argc);
        }
        return op;
    }

    private Token parseNumberToken(char firstChar) {
        int offset = this.pos;
        int len = 1;
        this.pos++;
        if (this.isEndOfExpression(offset + len)) {
            this.lastToken = new NumberToken(Double.parseDouble(String.valueOf(firstChar)));
            return this.lastToken;
        } else {
            while (!this.isEndOfExpression(offset + len) && isNumeric(this.expression[offset + len], this.expression[offset + len - 1] == 'e' || this.expression[offset + len - 1] == 'E')) {
                len++;
                this.pos++;
            }
            if (this.expression[offset + len - 1] == 'e' || this.expression[offset + len - 1] == 'E') {
                len--;
                this.pos--;
            }
            this.lastToken = new NumberToken(this.expression, offset, len);
            return this.lastToken;
        }
    }

    private static boolean isNumeric(char ch, boolean lastCharE) {
        return Character.isDigit(ch) || ch == '.' || ch == 'e' || ch == 'E' || lastCharE && (ch == '-' || ch == '+');
    }

    public static boolean isAlphabetic(int codePoint) {
        return Character.isLetter(codePoint);
    }

    public static boolean isVariableOrFunctionCharacter(int codePoint) {
        return isAlphabetic(codePoint) || Character.isDigit(codePoint) || codePoint == 95 || codePoint == 46;
    }

    private boolean isEndOfExpression(int offset) {
        return this.expressionLength <= offset;
    }
}