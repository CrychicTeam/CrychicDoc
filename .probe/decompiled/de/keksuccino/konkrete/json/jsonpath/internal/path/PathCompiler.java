package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.InvalidPathException;
import de.keksuccino.konkrete.json.jsonpath.Predicate;
import de.keksuccino.konkrete.json.jsonpath.internal.CharacterIndex;
import de.keksuccino.konkrete.json.jsonpath.internal.Path;
import de.keksuccino.konkrete.json.jsonpath.internal.Utils;
import de.keksuccino.konkrete.json.jsonpath.internal.filter.FilterCompiler;
import de.keksuccino.konkrete.json.jsonpath.internal.function.ParamType;
import de.keksuccino.konkrete.json.jsonpath.internal.function.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PathCompiler {

    private static final char DOC_CONTEXT = '$';

    private static final char EVAL_CONTEXT = '@';

    private static final char OPEN_SQUARE_BRACKET = '[';

    private static final char CLOSE_SQUARE_BRACKET = ']';

    private static final char OPEN_PARENTHESIS = '(';

    private static final char CLOSE_PARENTHESIS = ')';

    private static final char OPEN_BRACE = '{';

    private static final char CLOSE_BRACE = '}';

    private static final char WILDCARD = '*';

    private static final char PERIOD = '.';

    private static final char SPACE = ' ';

    private static final char TAB = '\t';

    private static final char CR = '\r';

    private static final char LF = '\n';

    private static final char BEGIN_FILTER = '?';

    private static final char COMMA = ',';

    private static final char SPLIT = ':';

    private static final char MINUS = '-';

    private static final char SINGLE_QUOTE = '\'';

    private static final char DOUBLE_QUOTE = '"';

    private final LinkedList<Predicate> filterStack;

    private final CharacterIndex path;

    private PathCompiler(String path, LinkedList<Predicate> filterStack) {
        this(new CharacterIndex(path), filterStack);
    }

    private PathCompiler(CharacterIndex path, LinkedList<Predicate> filterStack) {
        this.filterStack = filterStack;
        this.path = path;
    }

    private Path compile() {
        RootPathToken root = this.readContextToken();
        return new CompiledPath(root, root.getPathFragment().equals("$"));
    }

    public static Path compile(String path, Predicate... filters) {
        try {
            CharacterIndex ci = new CharacterIndex(path);
            ci.trim();
            if (ci.charAt(0) != '$' && ci.charAt(0) != '@') {
                ci = new CharacterIndex("$." + path);
                ci.trim();
            }
            if (ci.lastCharIs('.')) {
                fail("Path must not end with a '.' or '..'");
            }
            LinkedList<Predicate> filterStack = new LinkedList(Arrays.asList(filters));
            return new PathCompiler(ci, filterStack).compile();
        } catch (Exception var4) {
            if (!(var4 instanceof InvalidPathException ipe)) {
                ipe = new InvalidPathException(var4);
            }
            throw ipe;
        }
    }

    private void readWhitespace() {
        while (this.path.inBounds()) {
            char c = this.path.currentChar();
            if (this.isWhitespace(c)) {
                this.path.incrementPosition(1);
                continue;
            }
            break;
        }
    }

    private Boolean isPathContext(char c) {
        return c == '$' || c == '@';
    }

    private RootPathToken readContextToken() {
        this.readWhitespace();
        if (!this.isPathContext(this.path.currentChar())) {
            throw new InvalidPathException("Path must start with '$' or '@'");
        } else {
            RootPathToken pathToken = PathTokenFactory.createRootPathToken(this.path.currentChar());
            if (this.path.currentIsTail()) {
                return pathToken;
            } else {
                this.path.incrementPosition(1);
                if (this.path.currentChar() != '.' && this.path.currentChar() != '[') {
                    fail("Illegal character at position " + this.path.position() + " expected '.' or '['");
                }
                PathTokenAppender appender = pathToken.getPathTokenAppender();
                this.readNextToken(appender);
                return pathToken;
            }
        }
    }

    private boolean readNextToken(PathTokenAppender appender) {
        char c = this.path.currentChar();
        switch(c) {
            case '*':
                if (!this.readWildCardToken(appender)) {
                    fail("Could not parse token starting at position " + this.path.position());
                }
                return true;
            case '.':
                if (!this.readDotToken(appender)) {
                    fail("Could not parse token starting at position " + this.path.position());
                }
                return true;
            case '[':
                if (!this.readBracketPropertyToken(appender) && !this.readArrayToken(appender) && !this.readWildCardToken(appender) && !this.readFilterToken(appender) && !this.readPlaceholderToken(appender)) {
                    fail("Could not parse token starting at position " + this.path.position() + ". Expected ?, ', 0-9, * ");
                }
                return true;
            default:
                if (!this.readPropertyOrFunctionToken(appender)) {
                    fail("Could not parse token starting at position " + this.path.position());
                }
                return true;
        }
    }

    private boolean readDotToken(PathTokenAppender appender) {
        if (this.path.currentCharIs('.') && this.path.nextCharIs('.')) {
            appender.appendPathToken(PathTokenFactory.crateScanToken());
            this.path.incrementPosition(2);
        } else {
            if (!this.path.hasMoreCharacters()) {
                throw new InvalidPathException("Path must not end with a '.");
            }
            this.path.incrementPosition(1);
        }
        if (this.path.currentCharIs('.')) {
            throw new InvalidPathException("Character '.' on position " + this.path.position() + " is not valid.");
        } else {
            return this.readNextToken(appender);
        }
    }

    private boolean readPropertyOrFunctionToken(PathTokenAppender appender) {
        if (!this.path.currentCharIs('[') && !this.path.currentCharIs('*') && !this.path.currentCharIs('.') && !this.path.currentCharIs(' ')) {
            int startPosition = this.path.position();
            int readPosition = startPosition;
            int endPosition = 0;
            boolean isFunction;
            for (isFunction = false; this.path.inBounds(readPosition); readPosition++) {
                char c = this.path.charAt(readPosition);
                if (c == ' ') {
                    throw new InvalidPathException("Use bracket notion ['my prop'] if your property contains blank characters. position: " + this.path.position());
                }
                if (c == '.' || c == '[') {
                    endPosition = readPosition;
                    break;
                }
                if (c == '(') {
                    isFunction = true;
                    endPosition = readPosition;
                    break;
                }
            }
            if (endPosition == 0) {
                endPosition = this.path.length();
            }
            List<Parameter> functionParameters = null;
            if (isFunction) {
                int parenthesis_count = 1;
                for (int i = readPosition + 1; i < this.path.length(); i++) {
                    if (this.path.charAt(i) == ')') {
                        parenthesis_count--;
                    } else if (this.path.charAt(i) == '(') {
                        parenthesis_count++;
                    }
                    if (parenthesis_count == 0) {
                        break;
                    }
                }
                if (parenthesis_count != 0) {
                    String functionName = this.path.subSequence(startPosition, endPosition).toString();
                    throw new InvalidPathException("Arguments to function: '" + functionName + "' are not closed properly.");
                }
                if (this.path.inBounds(readPosition + 1)) {
                    char cx = this.path.charAt(readPosition + 1);
                    if (cx != ')') {
                        this.path.setPosition(endPosition + 1);
                        String functionName = this.path.subSequence(startPosition, endPosition).toString();
                        functionParameters = this.parseFunctionParameters(functionName);
                    } else {
                        this.path.setPosition(readPosition + 1);
                    }
                } else {
                    this.path.setPosition(readPosition);
                }
            } else {
                this.path.setPosition(endPosition);
            }
            String property = this.path.subSequence(startPosition, endPosition).toString();
            if (isFunction) {
                appender.appendPathToken(PathTokenFactory.createFunctionPathToken(property, functionParameters));
            } else {
                appender.appendPathToken(PathTokenFactory.createSinglePropertyPathToken(property, '\''));
            }
            return this.path.currentIsTail() || this.readNextToken(appender);
        } else {
            return false;
        }
    }

    private List<Parameter> parseFunctionParameters(String funcName) {
        ParamType type = null;
        int groupParen = 1;
        int groupBracket = 0;
        int groupBrace = 0;
        int groupQuote = 0;
        boolean endOfStream = false;
        char priorChar = 0;
        List<Parameter> parameters = new ArrayList();
        StringBuilder parameter = new StringBuilder();
        while (this.path.inBounds() && !endOfStream) {
            char c = this.path.currentChar();
            this.path.incrementPosition(1);
            if (type == null) {
                if (this.isWhitespace(c)) {
                    continue;
                }
                if (c == '{' || Character.isDigit(c) || '"' == c) {
                    type = ParamType.JSON;
                } else if (this.isPathContext(c)) {
                    type = ParamType.PATH;
                }
            }
            switch(c) {
                case '"':
                    if (priorChar != '\\' && groupQuote > 0) {
                        groupQuote--;
                    } else {
                        groupQuote++;
                    }
                    break;
                case '(':
                    groupParen++;
                    break;
                case ')':
                    if (0 > --groupParen || priorChar == '(') {
                        parameter.append(c);
                    }
                case ',':
                    if (0 == groupQuote && 0 == groupBrace && 0 == groupBracket && (0 == groupParen && ')' == c || 1 == groupParen)) {
                        endOfStream = 0 == groupParen;
                        if (null != type) {
                            Parameter param = null;
                            switch(type) {
                                case JSON:
                                    param = new Parameter(parameter.toString());
                                    break;
                                case PATH:
                                    LinkedList<Predicate> predicates = new LinkedList();
                                    PathCompiler compiler = new PathCompiler(parameter.toString(), predicates);
                                    param = new Parameter(compiler.compile());
                            }
                            if (null != param) {
                                parameters.add(param);
                            }
                            parameter.delete(0, parameter.length());
                            type = null;
                        }
                    }
                    break;
                case '[':
                    groupBracket++;
                    break;
                case ']':
                    if (0 == groupBracket) {
                        throw new InvalidPathException("Unexpected close bracket ']' at character position: " + this.path.position());
                    }
                    groupBracket--;
                    break;
                case '{':
                    groupBrace++;
                    break;
                case '}':
                    if (0 == groupBrace) {
                        throw new InvalidPathException("Unexpected close brace '}' at character position: " + this.path.position());
                    }
                    groupBrace--;
            }
            if (type != null && (c != ',' || 0 != groupBrace || 0 != groupBracket || 1 != groupParen)) {
                parameter.append(c);
            }
            priorChar = c;
        }
        if (0 == groupBrace && 0 == groupParen && 0 == groupBracket) {
            return parameters;
        } else {
            throw new InvalidPathException("Arguments to function: '" + funcName + "' are not closed properly.");
        }
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private boolean readPlaceholderToken(PathTokenAppender appender) {
        if (!this.path.currentCharIs('[')) {
            return false;
        } else {
            int questionmarkIndex = this.path.indexOfNextSignificantChar('?');
            if (questionmarkIndex == -1) {
                return false;
            } else {
                char nextSignificantChar = this.path.nextSignificantChar(questionmarkIndex);
                if (nextSignificantChar != ']' && nextSignificantChar != ',') {
                    return false;
                } else {
                    int expressionBeginIndex = this.path.position() + 1;
                    int expressionEndIndex = this.path.nextIndexOf(expressionBeginIndex, ']');
                    if (expressionEndIndex == -1) {
                        return false;
                    } else {
                        String expression = this.path.subSequence(expressionBeginIndex, expressionEndIndex).toString();
                        String[] tokens = expression.split(",");
                        if (this.filterStack.size() < tokens.length) {
                            throw new InvalidPathException("Not enough predicates supplied for filter [" + expression + "] at position " + this.path.position());
                        } else {
                            Collection<Predicate> predicates = new ArrayList();
                            for (String token : tokens) {
                                token = token != null ? token.trim() : null;
                                if (!"?".equals(token == null ? "" : token)) {
                                    throw new InvalidPathException("Expected '?' but found " + token);
                                }
                                predicates.add((Predicate) this.filterStack.pop());
                            }
                            appender.appendPathToken(PathTokenFactory.createPredicatePathToken(predicates));
                            this.path.setPosition(expressionEndIndex + 1);
                            return this.path.currentIsTail() || this.readNextToken(appender);
                        }
                    }
                }
            }
        }
    }

    private boolean readFilterToken(PathTokenAppender appender) {
        if (!this.path.currentCharIs('[') && !this.path.nextSignificantCharIs('?')) {
            return false;
        } else {
            int openStatementBracketIndex = this.path.position();
            int questionMarkIndex = this.path.indexOfNextSignificantChar('?');
            if (questionMarkIndex == -1) {
                return false;
            } else {
                int openBracketIndex = this.path.indexOfNextSignificantChar(questionMarkIndex, '(');
                if (openBracketIndex == -1) {
                    return false;
                } else {
                    int closeBracketIndex = this.path.indexOfClosingBracket(openBracketIndex, true, true);
                    if (closeBracketIndex == -1) {
                        return false;
                    } else if (!this.path.nextSignificantCharIs(closeBracketIndex, ']')) {
                        return false;
                    } else {
                        int closeStatementBracketIndex = this.path.indexOfNextSignificantChar(closeBracketIndex, ']');
                        String criteria = this.path.subSequence(openStatementBracketIndex, closeStatementBracketIndex + 1).toString();
                        Predicate predicate = FilterCompiler.compile(criteria);
                        appender.appendPathToken(PathTokenFactory.createPredicatePathToken(predicate));
                        this.path.setPosition(closeStatementBracketIndex + 1);
                        return this.path.currentIsTail() || this.readNextToken(appender);
                    }
                }
            }
        }
    }

    private boolean readWildCardToken(PathTokenAppender appender) {
        boolean inBracket = this.path.currentCharIs('[');
        if (inBracket && !this.path.nextSignificantCharIs('*')) {
            return false;
        } else if (!this.path.currentCharIs('*') && this.path.isOutOfBounds(this.path.position() + 1)) {
            return false;
        } else {
            if (inBracket) {
                int wildCardIndex = this.path.indexOfNextSignificantChar('*');
                if (!this.path.nextSignificantCharIs(wildCardIndex, ']')) {
                    int offset = wildCardIndex + 1;
                    throw new InvalidPathException("Expected wildcard token to end with ']' on position " + offset);
                }
                int bracketCloseIndex = this.path.indexOfNextSignificantChar(wildCardIndex, ']');
                this.path.setPosition(bracketCloseIndex + 1);
            } else {
                this.path.incrementPosition(1);
            }
            appender.appendPathToken(PathTokenFactory.createWildCardPathToken());
            return this.path.currentIsTail() || this.readNextToken(appender);
        }
    }

    private boolean readArrayToken(PathTokenAppender appender) {
        if (!this.path.currentCharIs('[')) {
            return false;
        } else {
            char nextSignificantChar = this.path.nextSignificantChar();
            if (!Character.isDigit(nextSignificantChar) && nextSignificantChar != '-' && nextSignificantChar != ':') {
                return false;
            } else {
                int expressionBeginIndex = this.path.position() + 1;
                int expressionEndIndex = this.path.nextIndexOf(expressionBeginIndex, ']');
                if (expressionEndIndex == -1) {
                    return false;
                } else {
                    String expression = this.path.subSequence(expressionBeginIndex, expressionEndIndex).toString().trim();
                    if ("*".equals(expression)) {
                        return false;
                    } else {
                        for (int i = 0; i < expression.length(); i++) {
                            char c = expression.charAt(i);
                            if (!Character.isDigit(c) && c != ',' && c != '-' && c != ':' && c != ' ') {
                                return false;
                            }
                        }
                        boolean isSliceOperation = expression.contains(":");
                        if (isSliceOperation) {
                            ArraySliceOperation arraySliceOperation = ArraySliceOperation.parse(expression);
                            appender.appendPathToken(PathTokenFactory.createSliceArrayPathToken(arraySliceOperation));
                        } else {
                            ArrayIndexOperation arrayIndexOperation = ArrayIndexOperation.parse(expression);
                            appender.appendPathToken(PathTokenFactory.createIndexArrayPathToken(arrayIndexOperation));
                        }
                        this.path.setPosition(expressionEndIndex + 1);
                        return this.path.currentIsTail() || this.readNextToken(appender);
                    }
                }
            }
        }
    }

    private boolean readBracketPropertyToken(PathTokenAppender appender) {
        if (!this.path.currentCharIs('[')) {
            return false;
        } else {
            char potentialStringDelimiter = this.path.nextSignificantChar();
            if (potentialStringDelimiter != '\'' && potentialStringDelimiter != '"') {
                return false;
            } else {
                List<String> properties = new ArrayList();
                int startPosition = this.path.position() + 1;
                int readPosition = startPosition;
                int endPosition = 0;
                boolean inProperty = false;
                boolean inEscape = false;
                for (boolean lastSignificantWasComma = false; this.path.inBounds(readPosition); readPosition++) {
                    char c = this.path.charAt(readPosition);
                    if (inEscape) {
                        inEscape = false;
                    } else if ('\\' == c) {
                        inEscape = true;
                    } else {
                        if (c == ']' && !inProperty) {
                            if (lastSignificantWasComma) {
                                fail("Found empty property at index " + readPosition);
                            }
                            break;
                        }
                        if (c == potentialStringDelimiter) {
                            if (inProperty) {
                                char nextSignificantChar = this.path.nextSignificantChar(readPosition);
                                if (nextSignificantChar != ']' && nextSignificantChar != ',') {
                                    fail("Property must be separated by comma or Property must be terminated close square bracket at index " + readPosition);
                                }
                                endPosition = readPosition;
                                String prop = this.path.subSequence(startPosition, readPosition).toString();
                                properties.add(Utils.unescape(prop));
                                inProperty = false;
                            } else {
                                startPosition = readPosition + 1;
                                inProperty = true;
                                lastSignificantWasComma = false;
                            }
                        } else if (c == ',' && !inProperty) {
                            if (lastSignificantWasComma) {
                                fail("Found empty property at index " + readPosition);
                            }
                            lastSignificantWasComma = true;
                        }
                    }
                }
                if (inProperty) {
                    fail("Property has not been closed - missing closing " + potentialStringDelimiter);
                }
                int endBracketIndex = this.path.indexOfNextSignificantChar(endPosition, ']') + 1;
                this.path.setPosition(endBracketIndex);
                appender.appendPathToken(PathTokenFactory.createPropertyPathToken(properties, potentialStringDelimiter));
                return this.path.currentIsTail() || this.readNextToken(appender);
            }
        }
    }

    public static boolean fail(String message) {
        throw new InvalidPathException(message);
    }
}