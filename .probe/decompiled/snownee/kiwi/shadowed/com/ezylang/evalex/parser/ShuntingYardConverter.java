package snownee.kiwi.shadowed.com.ezylang.evalex.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.OperatorIfc;

public class ShuntingYardConverter {

    private final List<Token> expressionTokens;

    private final String originalExpression;

    private final ExpressionConfiguration configuration;

    private final Deque<Token> operatorStack = new ArrayDeque();

    private final Deque<ASTNode> operandStack = new ArrayDeque();

    public ShuntingYardConverter(String originalExpression, List<Token> expressionTokens, ExpressionConfiguration configuration) {
        this.originalExpression = originalExpression;
        this.expressionTokens = expressionTokens;
        this.configuration = configuration;
    }

    public ASTNode toAbstractSyntaxTree() throws ParseException {
        Token previousToken = null;
        for (Token currentToken : this.expressionTokens) {
            switch(currentToken.getType()) {
                case VARIABLE_OR_CONSTANT:
                case NUMBER_LITERAL:
                case STRING_LITERAL:
                    this.operandStack.push(new ASTNode(currentToken));
                    break;
                case FUNCTION:
                    this.operatorStack.push(currentToken);
                    break;
                case COMMA:
                    this.processOperatorsFromStackUntilTokenType(Token.TokenType.BRACE_OPEN);
                    break;
                case INFIX_OPERATOR:
                case PREFIX_OPERATOR:
                case POSTFIX_OPERATOR:
                    this.processOperator(currentToken);
                    break;
                case BRACE_OPEN:
                    this.processBraceOpen(previousToken, currentToken);
                    break;
                case BRACE_CLOSE:
                    this.processBraceClose();
                    break;
                case ARRAY_OPEN:
                    this.processArrayOpen(currentToken);
                    break;
                case ARRAY_CLOSE:
                    this.processArrayClose();
                    break;
                case STRUCTURE_SEPARATOR:
                    this.processStructureSeparator(currentToken);
                    break;
                default:
                    throw new ParseException(currentToken, "Unexpected token of type '" + currentToken.getType() + "'");
            }
            previousToken = currentToken;
        }
        while (!this.operatorStack.isEmpty()) {
            Token token = (Token) this.operatorStack.pop();
            this.createOperatorNode(token);
        }
        if (this.operandStack.isEmpty()) {
            throw new ParseException(this.originalExpression, "Empty expression");
        } else if (this.operandStack.size() > 1) {
            throw new ParseException(this.originalExpression, "Too many operands");
        } else {
            return (ASTNode) this.operandStack.pop();
        }
    }

    private void processStructureSeparator(Token currentToken) throws ParseException {
        for (Token nextToken = this.operatorStack.isEmpty() ? null : (Token) this.operatorStack.peek(); nextToken != null && nextToken.getType() == Token.TokenType.STRUCTURE_SEPARATOR; nextToken = (Token) this.operatorStack.peek()) {
            Token token = (Token) this.operatorStack.pop();
            this.createOperatorNode(token);
        }
        this.operatorStack.push(currentToken);
    }

    private void processBraceOpen(Token previousToken, Token currentToken) {
        if (previousToken != null && previousToken.getType() == Token.TokenType.FUNCTION) {
            Token paramStart = new Token(currentToken.getStartPosition(), currentToken.getValue(), Token.TokenType.FUNCTION_PARAM_START);
            this.operandStack.push(new ASTNode(paramStart));
        }
        this.operatorStack.push(currentToken);
    }

    private void processBraceClose() throws ParseException {
        this.processOperatorsFromStackUntilTokenType(Token.TokenType.BRACE_OPEN);
        this.operatorStack.pop();
        if (!this.operatorStack.isEmpty() && ((Token) this.operatorStack.peek()).getType() == Token.TokenType.FUNCTION) {
            Token functionToken = (Token) this.operatorStack.pop();
            ArrayList<ASTNode> parameters = new ArrayList();
            while (true) {
                ASTNode node = (ASTNode) this.operandStack.pop();
                if (node.getToken().getType() == Token.TokenType.FUNCTION_PARAM_START) {
                    this.validateFunctionParameters(functionToken, parameters);
                    this.operandStack.push(new ASTNode(functionToken, (ASTNode[]) parameters.toArray(new ASTNode[0])));
                    break;
                }
                parameters.add(0, node);
            }
        }
    }

    private void validateFunctionParameters(Token functionToken, ArrayList<ASTNode> parameters) throws ParseException {
        FunctionIfc function = functionToken.getFunctionDefinition();
        if (parameters.size() < function.getCountOfNonVarArgParameters()) {
            throw new ParseException(functionToken, "Not enough parameters for function");
        } else if (!function.hasVarArgs() && parameters.size() > function.getFunctionParameterDefinitions().size()) {
            throw new ParseException(functionToken, "Too many parameters for function");
        }
    }

    private void processArrayOpen(Token currentToken) throws ParseException {
        for (Token nextToken = this.operatorStack.isEmpty() ? null : (Token) this.operatorStack.peek(); nextToken != null && nextToken.getType() == Token.TokenType.STRUCTURE_SEPARATOR; nextToken = this.operatorStack.isEmpty() ? null : (Token) this.operatorStack.peek()) {
            Token token = (Token) this.operatorStack.pop();
            this.createOperatorNode(token);
        }
        Token arrayIndex = new Token(currentToken.getStartPosition(), currentToken.getValue(), Token.TokenType.ARRAY_INDEX);
        this.operatorStack.push(arrayIndex);
        this.operatorStack.push(currentToken);
    }

    private void processArrayClose() throws ParseException {
        this.processOperatorsFromStackUntilTokenType(Token.TokenType.ARRAY_OPEN);
        this.operatorStack.pop();
        Token arrayToken = (Token) this.operatorStack.pop();
        ArrayList<ASTNode> operands = new ArrayList();
        ASTNode index = (ASTNode) this.operandStack.pop();
        operands.add(0, index);
        ASTNode array = (ASTNode) this.operandStack.pop();
        operands.add(0, array);
        this.operandStack.push(new ASTNode(arrayToken, (ASTNode[]) operands.toArray(new ASTNode[0])));
    }

    private void processOperatorsFromStackUntilTokenType(Token.TokenType untilTokenType) throws ParseException {
        while (!this.operatorStack.isEmpty() && ((Token) this.operatorStack.peek()).getType() != untilTokenType) {
            Token token = (Token) this.operatorStack.pop();
            this.createOperatorNode(token);
        }
    }

    private void createOperatorNode(Token token) throws ParseException {
        if (this.operandStack.isEmpty()) {
            throw new ParseException(token, "Missing operand for operator");
        } else {
            ASTNode operand1 = (ASTNode) this.operandStack.pop();
            if (token.getType() != Token.TokenType.PREFIX_OPERATOR && token.getType() != Token.TokenType.POSTFIX_OPERATOR) {
                if (this.operandStack.isEmpty()) {
                    throw new ParseException(token, "Missing second operand for operator");
                }
                ASTNode operand2 = (ASTNode) this.operandStack.pop();
                this.operandStack.push(new ASTNode(token, operand2, operand1));
            } else {
                this.operandStack.push(new ASTNode(token, operand1));
            }
        }
    }

    private void processOperator(Token currentToken) throws ParseException {
        for (Token nextToken = this.operatorStack.isEmpty() ? null : (Token) this.operatorStack.peek(); this.isOperator(nextToken) && this.isNextOperatorOfHigherPrecedence(currentToken.getOperatorDefinition(), nextToken.getOperatorDefinition()); nextToken = this.operatorStack.isEmpty() ? null : (Token) this.operatorStack.peek()) {
            Token token = (Token) this.operatorStack.pop();
            this.createOperatorNode(token);
        }
        this.operatorStack.push(currentToken);
    }

    private boolean isNextOperatorOfHigherPrecedence(OperatorIfc currentOperator, OperatorIfc nextOperator) {
        if (nextOperator == null) {
            return true;
        } else {
            return currentOperator.isLeftAssociative() ? currentOperator.getPrecedence(this.configuration) <= nextOperator.getPrecedence(this.configuration) : currentOperator.getPrecedence(this.configuration) < nextOperator.getPrecedence(this.configuration);
        }
    }

    private boolean isOperator(Token token) {
        if (token == null) {
            return false;
        } else {
            Token.TokenType tokenType = token.getType();
            switch(tokenType) {
                case INFIX_OPERATOR:
                case PREFIX_OPERATOR:
                case POSTFIX_OPERATOR:
                case STRUCTURE_SEPARATOR:
                    return true;
                case BRACE_OPEN:
                case BRACE_CLOSE:
                case ARRAY_OPEN:
                case ARRAY_CLOSE:
                default:
                    return false;
            }
        }
    }
}