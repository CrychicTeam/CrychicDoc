package de.keksuccino.konkrete.json.jsonpath.internal.filter;

import de.keksuccino.konkrete.json.jsonpath.Filter;
import de.keksuccino.konkrete.json.jsonpath.InvalidPathException;
import de.keksuccino.konkrete.json.jsonpath.Predicate;
import de.keksuccino.konkrete.json.jsonpath.internal.CharacterIndex;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterCompiler {

    private static final Logger logger = LoggerFactory.getLogger(FilterCompiler.class);

    private static final char DOC_CONTEXT = '$';

    private static final char EVAL_CONTEXT = '@';

    private static final char OPEN_SQUARE_BRACKET = '[';

    private static final char CLOSE_SQUARE_BRACKET = ']';

    private static final char OPEN_PARENTHESIS = '(';

    private static final char CLOSE_PARENTHESIS = ')';

    private static final char OPEN_OBJECT = '{';

    private static final char CLOSE_OBJECT = '}';

    private static final char OPEN_ARRAY = '[';

    private static final char CLOSE_ARRAY = ']';

    private static final char SINGLE_QUOTE = '\'';

    private static final char DOUBLE_QUOTE = '"';

    private static final char SPACE = ' ';

    private static final char PERIOD = '.';

    private static final char AND = '&';

    private static final char OR = '|';

    private static final char MINUS = '-';

    private static final char LT = '<';

    private static final char GT = '>';

    private static final char EQ = '=';

    private static final char TILDE = '~';

    private static final char TRUE = 't';

    private static final char FALSE = 'f';

    private static final char NULL = 'n';

    private static final char NOT = '!';

    private static final char PATTERN = '/';

    private static final char IGNORE_CASE = 'i';

    private CharacterIndex filter;

    public static Filter compile(String filterString) {
        FilterCompiler compiler = new FilterCompiler(filterString);
        return new FilterCompiler.CompiledFilter(compiler.compile());
    }

    private FilterCompiler(String filterString) {
        this.filter = new CharacterIndex(filterString);
        this.filter.trim();
        if (this.filter.currentCharIs('[') && this.filter.lastCharIs(']')) {
            this.filter.incrementPosition(1);
            this.filter.decrementEndPosition(1);
            this.filter.trim();
            if (!this.filter.currentCharIs('?')) {
                throw new InvalidPathException("Filter must start with '[?' and end with ']'. " + filterString);
            } else {
                this.filter.incrementPosition(1);
                this.filter.trim();
                if (!this.filter.currentCharIs('(') || !this.filter.lastCharIs(')')) {
                    throw new InvalidPathException("Filter must start with '[?(' and end with ')]'. " + filterString);
                }
            }
        } else {
            throw new InvalidPathException("Filter must start with '[' and end with ']'. " + filterString);
        }
    }

    public Predicate compile() {
        try {
            ExpressionNode result = this.readLogicalOR();
            this.filter.skipBlanks();
            if (this.filter.inBounds()) {
                throw new InvalidPathException(String.format("Expected end of filter expression instead of: %s", this.filter.subSequence(this.filter.position(), this.filter.length())));
            } else {
                return result;
            }
        } catch (InvalidPathException var2) {
            throw var2;
        } catch (Exception var3) {
            throw new InvalidPathException("Failed to parse filter: " + this.filter + ", error on position: " + this.filter.position() + ", char: " + this.filter.currentChar());
        }
    }

    private ValueNode readValueNode() {
        switch(this.filter.skipBlanks().currentChar()) {
            case '!':
                this.filter.incrementPosition(1);
                switch(this.filter.skipBlanks().currentChar()) {
                    case '$':
                        return this.readPath();
                    case '@':
                        return this.readPath();
                    default:
                        throw new InvalidPathException(String.format("Unexpected character: %c", '!'));
                }
            case '$':
                return this.readPath();
            case '@':
                return this.readPath();
            default:
                return this.readLiteral();
        }
    }

    private ValueNode readLiteral() {
        switch(this.filter.skipBlanks().currentChar()) {
            case '"':
                return this.readStringLiteral('"');
            case '\'':
                return this.readStringLiteral('\'');
            case '-':
                return this.readNumberLiteral();
            case '/':
                return this.readPattern();
            case '[':
                return this.readJsonLiteral();
            case 'f':
                return this.readBooleanLiteral();
            case 'n':
                return this.readNullLiteral();
            case 't':
                return this.readBooleanLiteral();
            case '{':
                return this.readJsonLiteral();
            default:
                return this.readNumberLiteral();
        }
    }

    private ExpressionNode readLogicalOR() {
        List<ExpressionNode> ops = new ArrayList();
        ops.add(this.readLogicalAND());
        while (true) {
            int savepoint = this.filter.position();
            if (!this.filter.hasSignificantSubSequence(LogicalOperator.OR.getOperatorString())) {
                this.filter.setPosition(savepoint);
                return (ExpressionNode) (1 == ops.size() ? (ExpressionNode) ops.get(0) : LogicalExpressionNode.createLogicalOr(ops));
            }
            ops.add(this.readLogicalAND());
        }
    }

    private ExpressionNode readLogicalAND() {
        List<ExpressionNode> ops = new ArrayList();
        ops.add(this.readLogicalANDOperand());
        while (true) {
            int savepoint = this.filter.position();
            if (!this.filter.hasSignificantSubSequence(LogicalOperator.AND.getOperatorString())) {
                this.filter.setPosition(savepoint);
                return (ExpressionNode) (1 == ops.size() ? (ExpressionNode) ops.get(0) : LogicalExpressionNode.createLogicalAnd(ops));
            }
            ops.add(this.readLogicalANDOperand());
        }
    }

    private ExpressionNode readLogicalANDOperand() {
        int savepoint = this.filter.skipBlanks().position();
        if (this.filter.skipBlanks().currentCharIs('!')) {
            this.filter.readSignificantChar('!');
            switch(this.filter.skipBlanks().currentChar()) {
                case '$':
                case '@':
                    this.filter.setPosition(savepoint);
                    break;
                default:
                    ExpressionNode op = this.readLogicalANDOperand();
                    return LogicalExpressionNode.createLogicalNot(op);
            }
        }
        if (this.filter.skipBlanks().currentCharIs('(')) {
            this.filter.readSignificantChar('(');
            ExpressionNode op = this.readLogicalOR();
            this.filter.readSignificantChar(')');
            return op;
        } else {
            return this.readExpression();
        }
    }

    private RelationalExpressionNode readExpression() {
        ValueNode left = this.readValueNode();
        int savepoint = this.filter.position();
        try {
            RelationalOperator operator = this.readRelationalOperator();
            ValueNode right = this.readValueNode();
            return new RelationalExpressionNode(left, operator, right);
        } catch (InvalidPathException var6) {
            this.filter.setPosition(savepoint);
            ValueNodes.PathNode pathNode = left.asPathNode();
            ValueNode var7 = pathNode.asExistsCheck(pathNode.shouldExists());
            RelationalOperator operatorx = RelationalOperator.EXISTS;
            ValueNode rightx = var7.asPathNode().shouldExists() ? ValueNodes.TRUE : ValueNodes.FALSE;
            return new RelationalExpressionNode(var7, operatorx, rightx);
        }
    }

    private LogicalOperator readLogicalOperator() {
        int begin = this.filter.skipBlanks().position();
        int end = begin + 1;
        if (!this.filter.inBounds(end)) {
            throw new InvalidPathException("Expected boolean literal");
        } else {
            CharSequence logicalOperator = this.filter.subSequence(begin, end + 1);
            if (!logicalOperator.equals("||") && !logicalOperator.equals("&&")) {
                throw new InvalidPathException("Expected logical operator");
            } else {
                this.filter.incrementPosition(logicalOperator.length());
                logger.trace("LogicalOperator from {} to {} -> [{}]", new Object[] { begin, end, logicalOperator });
                return LogicalOperator.fromString(logicalOperator.toString());
            }
        }
    }

    private RelationalOperator readRelationalOperator() {
        int begin = this.filter.skipBlanks().position();
        if (this.isRelationalOperatorChar(this.filter.currentChar())) {
            while (this.filter.inBounds() && this.isRelationalOperatorChar(this.filter.currentChar())) {
                this.filter.incrementPosition(1);
            }
        } else {
            while (this.filter.inBounds() && this.filter.currentChar() != ' ') {
                this.filter.incrementPosition(1);
            }
        }
        CharSequence operator = this.filter.subSequence(begin, this.filter.position());
        logger.trace("Operator from {} to {} -> [{}]", new Object[] { begin, this.filter.position() - 1, operator });
        return RelationalOperator.fromString(operator.toString());
    }

    private ValueNodes.NullNode readNullLiteral() {
        int begin = this.filter.position();
        if (this.filter.currentChar() == 'n' && this.filter.inBounds(this.filter.position() + 3)) {
            CharSequence nullValue = this.filter.subSequence(this.filter.position(), this.filter.position() + 4);
            if ("null".equals(nullValue.toString())) {
                logger.trace("NullLiteral from {} to {} -> [{}]", new Object[] { begin, this.filter.position() + 3, nullValue });
                this.filter.incrementPosition(nullValue.length());
                return ValueNode.createNullNode();
            }
        }
        throw new InvalidPathException("Expected <null> value");
    }

    private ValueNodes.JsonNode readJsonLiteral() {
        int begin = this.filter.position();
        char openChar = this.filter.currentChar();
        assert openChar == '[' || openChar == '{';
        char closeChar = (char) (openChar == '[' ? 93 : 125);
        int closingIndex = this.filter.indexOfMatchingCloseChar(this.filter.position(), openChar, closeChar, true, false);
        if (closingIndex == -1) {
            throw new InvalidPathException("String not closed. Expected ' in " + this.filter);
        } else {
            this.filter.setPosition(closingIndex + 1);
            CharSequence json = this.filter.subSequence(begin, this.filter.position());
            logger.trace("JsonLiteral from {} to {} -> [{}]", new Object[] { begin, this.filter.position(), json });
            return ValueNode.createJsonNode(json);
        }
    }

    private int endOfFlags(int position) {
        int endIndex = position;
        for (char[] currentChar = new char[1]; this.filter.inBounds(endIndex); endIndex++) {
            currentChar[0] = this.filter.charAt(endIndex);
            if (PatternFlag.parseFlags(currentChar) <= 0) {
                break;
            }
        }
        return endIndex;
    }

    private ValueNodes.PatternNode readPattern() {
        int begin = this.filter.position();
        int closingIndex = this.filter.nextIndexOfUnescaped('/');
        if (closingIndex == -1) {
            throw new InvalidPathException("Pattern not closed. Expected / in " + this.filter);
        } else {
            if (this.filter.inBounds(closingIndex + 1)) {
                int endFlagsIndex = this.endOfFlags(closingIndex + 1);
                if (endFlagsIndex > closingIndex) {
                    CharSequence flags = this.filter.subSequence(closingIndex + 1, endFlagsIndex);
                    closingIndex += flags.length();
                }
            }
            this.filter.setPosition(closingIndex + 1);
            CharSequence pattern = this.filter.subSequence(begin, this.filter.position());
            logger.trace("PatternNode from {} to {} -> [{}]", new Object[] { begin, this.filter.position(), pattern });
            return ValueNode.createPatternNode(pattern);
        }
    }

    private ValueNodes.StringNode readStringLiteral(char endChar) {
        int begin = this.filter.position();
        int closingSingleQuoteIndex = this.filter.nextIndexOfUnescaped(endChar);
        if (closingSingleQuoteIndex == -1) {
            throw new InvalidPathException("String literal does not have matching quotes. Expected " + endChar + " in " + this.filter);
        } else {
            this.filter.setPosition(closingSingleQuoteIndex + 1);
            CharSequence stringLiteral = this.filter.subSequence(begin, this.filter.position());
            logger.trace("StringLiteral from {} to {} -> [{}]", new Object[] { begin, this.filter.position(), stringLiteral });
            return ValueNode.createStringNode(stringLiteral, true);
        }
    }

    private ValueNodes.NumberNode readNumberLiteral() {
        int begin = this.filter.position();
        while (this.filter.inBounds() && this.filter.isNumberCharacter(this.filter.position())) {
            this.filter.incrementPosition(1);
        }
        CharSequence numberLiteral = this.filter.subSequence(begin, this.filter.position());
        logger.trace("NumberLiteral from {} to {} -> [{}]", new Object[] { begin, this.filter.position(), numberLiteral });
        return ValueNode.createNumberNode(numberLiteral);
    }

    private ValueNodes.BooleanNode readBooleanLiteral() {
        int begin = this.filter.position();
        int end = this.filter.currentChar() == 't' ? this.filter.position() + 3 : this.filter.position() + 4;
        if (!this.filter.inBounds(end)) {
            throw new InvalidPathException("Expected boolean literal");
        } else {
            CharSequence boolValue = this.filter.subSequence(begin, end + 1);
            if (!boolValue.equals("true") && !boolValue.equals("false")) {
                throw new InvalidPathException("Expected boolean literal");
            } else {
                this.filter.incrementPosition(boolValue.length());
                logger.trace("BooleanLiteral from {} to {} -> [{}]", new Object[] { begin, end, boolValue });
                return ValueNode.createBooleanNode(boolValue);
            }
        }
    }

    private ValueNodes.PathNode readPath() {
        char previousSignificantChar = this.filter.previousSignificantChar();
        int begin = this.filter.position();
        this.filter.incrementPosition(1);
        while (this.filter.inBounds()) {
            if (this.filter.currentChar() == '[') {
                int closingSquareBracketIndex = this.filter.indexOfMatchingCloseChar(this.filter.position(), '[', ']', true, false);
                if (closingSquareBracketIndex == -1) {
                    throw new InvalidPathException("Square brackets does not match in filter " + this.filter);
                }
                this.filter.setPosition(closingSquareBracketIndex + 1);
            }
            boolean closingFunctionBracket = this.filter.currentChar() == ')' && this.currentCharIsClosingFunctionBracket(begin);
            boolean closingLogicalBracket = this.filter.currentChar() == ')' && !closingFunctionBracket;
            if (!this.filter.inBounds() || this.isRelationalOperatorChar(this.filter.currentChar()) || this.filter.currentChar() == ' ' || closingLogicalBracket) {
                break;
            }
            this.filter.incrementPosition(1);
        }
        boolean shouldExists = previousSignificantChar != '!';
        CharSequence path = this.filter.subSequence(begin, this.filter.position());
        return ValueNode.createPathNode(path, false, shouldExists);
    }

    private boolean expressionIsTerminated() {
        char c = this.filter.currentChar();
        if (c != ')' && !this.isLogicalOperatorChar(c)) {
            c = this.filter.nextSignificantChar();
            return c == ')' || this.isLogicalOperatorChar(c);
        } else {
            return true;
        }
    }

    private boolean currentCharIsClosingFunctionBracket(int lowerBound) {
        if (this.filter.currentChar() != ')') {
            return false;
        } else {
            int idx = this.filter.indexOfPreviousSignificantChar();
            if (idx != -1 && this.filter.charAt(idx) == '(') {
                idx--;
                while (this.filter.inBounds(idx) && idx > lowerBound) {
                    if (this.filter.charAt(idx) == '.') {
                        return true;
                    }
                    idx--;
                }
                return false;
            } else {
                return false;
            }
        }
    }

    private boolean isLogicalOperatorChar(char c) {
        return c == '&' || c == '|';
    }

    private boolean isRelationalOperatorChar(char c) {
        return c == '<' || c == '>' || c == '=' || c == '~' || c == '!';
    }

    private static final class CompiledFilter extends Filter {

        private final Predicate predicate;

        private CompiledFilter(Predicate predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean apply(Predicate.PredicateContext ctx) {
            return this.predicate.apply(ctx);
        }

        public String toString() {
            String predicateString = this.predicate.toString();
            return predicateString.startsWith("(") ? "[?" + predicateString + "]" : "[?(" + predicateString + ")]";
        }
    }
}