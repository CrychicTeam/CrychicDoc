package snownee.kiwi.shadowed.com.ezylang.evalex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import lombok.Generated;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.DataAccessorIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.OperatorIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.ASTNode;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.ParseException;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.ShuntingYardConverter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Tokenizer;

public class Expression {

    private final ExpressionConfiguration configuration;

    private final String expressionString;

    private final DataAccessorIfc dataAccessor;

    private final Map<String, EvaluationValue> constants = new TreeMap(String.CASE_INSENSITIVE_ORDER);

    private ASTNode abstractSyntaxTree;

    public Expression(String expressionString) {
        this(expressionString, ExpressionConfiguration.defaultConfiguration());
    }

    public Expression(String expressionString, ExpressionConfiguration configuration) {
        this.expressionString = expressionString;
        this.configuration = configuration;
        this.dataAccessor = (DataAccessorIfc) configuration.getDataAccessorSupplier().get();
        this.constants.putAll(configuration.getDefaultConstants());
    }

    public Expression(Expression expression) throws ParseException {
        this(expression.getExpressionString(), expression.getConfiguration());
        this.abstractSyntaxTree = expression.getAbstractSyntaxTree();
    }

    public EvaluationValue evaluate() throws EvaluationException, ParseException {
        EvaluationValue result = this.evaluateSubtree(this.getAbstractSyntaxTree());
        if (result.isNumberValue()) {
            BigDecimal bigDecimal = result.getNumberValue();
            if (this.configuration.getDecimalPlacesResult() != -1) {
                bigDecimal = this.roundValue(bigDecimal, this.configuration.getDecimalPlacesResult());
            }
            if (this.configuration.isStripTrailingZeros()) {
                bigDecimal = bigDecimal.stripTrailingZeros();
            }
            result = EvaluationValue.numberValue(bigDecimal);
        }
        return result;
    }

    public EvaluationValue evaluateSubtree(ASTNode startNode) throws EvaluationException {
        Token token = startNode.getToken();
        EvaluationValue result;
        switch(token.getType()) {
            case NUMBER_LITERAL:
                result = EvaluationValue.numberOfString(token.getValue(), this.configuration.getMathContext());
                break;
            case STRING_LITERAL:
                result = EvaluationValue.stringValue(token.getValue());
                break;
            case VARIABLE_OR_CONSTANT:
                result = this.getVariableOrConstant(token);
                if (result.isExpressionNode()) {
                    result = this.evaluateSubtree(result.getExpressionNode());
                }
                break;
            case PREFIX_OPERATOR:
            case POSTFIX_OPERATOR:
                result = token.getOperatorDefinition().evaluate(this, token, this.evaluateSubtree((ASTNode) startNode.getParameters().get(0)));
                break;
            case INFIX_OPERATOR:
                result = this.evaluateInfixOperator(startNode, token);
                break;
            case ARRAY_INDEX:
                result = this.evaluateArrayIndex(startNode);
                break;
            case STRUCTURE_SEPARATOR:
                result = this.evaluateStructureSeparator(startNode);
                break;
            case FUNCTION:
                result = this.evaluateFunction(startNode, token);
                break;
            default:
                throw new EvaluationException(token, "Unexpected evaluation token: " + token);
        }
        return result.isNumberValue() && this.configuration.getDecimalPlacesRounding() != -1 ? EvaluationValue.numberValue(this.roundValue(result.getNumberValue(), this.configuration.getDecimalPlacesRounding())) : result;
    }

    private EvaluationValue getVariableOrConstant(Token token) throws EvaluationException {
        EvaluationValue result = (EvaluationValue) this.constants.get(token.getValue());
        if (result == null) {
            result = this.getDataAccessor().getData(token.getValue());
        }
        if (result == null) {
            throw new EvaluationException(token, String.format("Variable or constant value for '%s' not found", token.getValue()));
        } else {
            return result;
        }
    }

    private EvaluationValue evaluateFunction(ASTNode startNode, Token token) throws EvaluationException {
        List<EvaluationValue> parameterResults = new ArrayList();
        for (int i = 0; i < startNode.getParameters().size(); i++) {
            if (token.getFunctionDefinition().isParameterLazy(i)) {
                parameterResults.add(this.convertValue(startNode.getParameters().get(i)));
            } else {
                parameterResults.add(this.evaluateSubtree((ASTNode) startNode.getParameters().get(i)));
            }
        }
        EvaluationValue[] parameters = (EvaluationValue[]) parameterResults.toArray(new EvaluationValue[0]);
        FunctionIfc function = token.getFunctionDefinition();
        function.validatePreEvaluation(token, parameters);
        return function.evaluate(this, token, parameters);
    }

    private EvaluationValue evaluateArrayIndex(ASTNode startNode) throws EvaluationException {
        EvaluationValue array = this.evaluateSubtree((ASTNode) startNode.getParameters().get(0));
        EvaluationValue index = this.evaluateSubtree((ASTNode) startNode.getParameters().get(1));
        if (array.isArrayValue() && index.isNumberValue()) {
            return (EvaluationValue) array.getArrayValue().get(index.getNumberValue().intValue());
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(startNode.getToken());
        }
    }

    private EvaluationValue evaluateStructureSeparator(ASTNode startNode) throws EvaluationException {
        EvaluationValue structure = this.evaluateSubtree((ASTNode) startNode.getParameters().get(0));
        Token nameToken = ((ASTNode) startNode.getParameters().get(1)).getToken();
        String name = nameToken.getValue();
        if (structure.isStructureValue()) {
            if (!structure.getStructureValue().containsKey(name)) {
                throw new EvaluationException(nameToken, String.format("Field '%s' not found in structure", name));
            } else {
                return (EvaluationValue) structure.getStructureValue().get(name);
            }
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(startNode.getToken());
        }
    }

    private EvaluationValue evaluateInfixOperator(ASTNode startNode, Token token) throws EvaluationException {
        OperatorIfc op = token.getOperatorDefinition();
        EvaluationValue left;
        EvaluationValue right;
        if (op.isOperandLazy()) {
            left = this.convertValue(startNode.getParameters().get(0));
            right = this.convertValue(startNode.getParameters().get(1));
        } else {
            left = this.evaluateSubtree((ASTNode) startNode.getParameters().get(0));
            right = this.evaluateSubtree((ASTNode) startNode.getParameters().get(1));
        }
        return op.evaluate(this, token, left, right);
    }

    private BigDecimal roundValue(BigDecimal value, int decimalPlaces) {
        return value.setScale(decimalPlaces, this.configuration.getMathContext().getRoundingMode());
    }

    public ASTNode getAbstractSyntaxTree() throws ParseException {
        if (this.abstractSyntaxTree == null) {
            Tokenizer tokenizer = new Tokenizer(this.expressionString, this.configuration);
            ShuntingYardConverter converter = new ShuntingYardConverter(this.expressionString, tokenizer.parse(), this.configuration);
            this.abstractSyntaxTree = converter.toAbstractSyntaxTree();
        }
        return this.abstractSyntaxTree;
    }

    public void validate() throws ParseException {
        this.getAbstractSyntaxTree();
    }

    public Expression with(String variable, Object value) {
        if (this.constants.containsKey(variable)) {
            if (!this.configuration.isAllowOverwriteConstants()) {
                throw new UnsupportedOperationException(String.format("Can't set value for constant '%s'", variable));
            }
            this.constants.remove(variable);
        }
        this.getDataAccessor().setData(variable, this.convertValue(value));
        return this;
    }

    public Expression and(String variable, Object value) {
        return this.with(variable, value);
    }

    public Expression withValues(Map<String, ?> values) {
        for (Entry<String, ?> entry : values.entrySet()) {
            this.with((String) entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Expression copy() throws ParseException {
        return new Expression(this);
    }

    public ASTNode createExpressionNode(String expression) throws ParseException {
        Tokenizer tokenizer = new Tokenizer(expression, this.configuration);
        ShuntingYardConverter converter = new ShuntingYardConverter(expression, tokenizer.parse(), this.configuration);
        return converter.toAbstractSyntaxTree();
    }

    public EvaluationValue convertDoubleValue(double value) {
        return this.convertValue(value);
    }

    public EvaluationValue convertValue(Object value) {
        return new EvaluationValue(value, this.configuration);
    }

    public List<ASTNode> getAllASTNodes() throws ParseException {
        return this.getAllASTNodesForNode(this.getAbstractSyntaxTree());
    }

    private List<ASTNode> getAllASTNodesForNode(ASTNode node) {
        List<ASTNode> nodes = new ArrayList();
        nodes.add(node);
        for (ASTNode child : node.getParameters()) {
            nodes.addAll(this.getAllASTNodesForNode(child));
        }
        return nodes;
    }

    public Set<String> getUsedVariables() throws ParseException {
        Set<String> variables = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        for (ASTNode node : this.getAllASTNodes()) {
            if (node.getToken().getType() == Token.TokenType.VARIABLE_OR_CONSTANT && !this.constants.containsKey(node.getToken().getValue())) {
                variables.add(node.getToken().getValue());
            }
        }
        return variables;
    }

    public Set<String> getUndefinedVariables() throws ParseException {
        Set<String> variables = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        for (String variable : this.getUsedVariables()) {
            if (this.getDataAccessor().getData(variable) == null) {
                variables.add(variable);
            }
        }
        return variables;
    }

    @Generated
    public ExpressionConfiguration getConfiguration() {
        return this.configuration;
    }

    @Generated
    public String getExpressionString() {
        return this.expressionString;
    }

    @Generated
    public DataAccessorIfc getDataAccessor() {
        return this.dataAccessor;
    }

    @Generated
    public Map<String, EvaluationValue> getConstants() {
        return this.constants;
    }
}