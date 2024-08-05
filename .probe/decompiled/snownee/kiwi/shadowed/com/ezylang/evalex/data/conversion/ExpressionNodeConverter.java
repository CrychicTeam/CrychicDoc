package snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion;

import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.ASTNode;

public class ExpressionNodeConverter implements ConverterIfc {

    @Override
    public EvaluationValue convert(Object object, ExpressionConfiguration configuration) {
        return EvaluationValue.expressionNodeValue((ASTNode) object);
    }

    @Override
    public boolean canConvert(Object object) {
        return object instanceof ASTNode;
    }
}