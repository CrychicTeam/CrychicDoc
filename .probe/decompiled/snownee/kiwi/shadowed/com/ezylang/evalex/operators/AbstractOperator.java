package snownee.kiwi.shadowed.com.ezylang.evalex.operators;

import lombok.Generated;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;

public abstract class AbstractOperator implements OperatorIfc {

    private final int precedence;

    private final boolean leftAssociative;

    private final boolean operandsLazy;

    OperatorIfc.OperatorType type;

    protected AbstractOperator() {
        InfixOperator infixAnnotation = (InfixOperator) this.getClass().getAnnotation(InfixOperator.class);
        PrefixOperator prefixAnnotation = (PrefixOperator) this.getClass().getAnnotation(PrefixOperator.class);
        PostfixOperator postfixAnnotation = (PostfixOperator) this.getClass().getAnnotation(PostfixOperator.class);
        if (infixAnnotation != null) {
            this.type = OperatorIfc.OperatorType.INFIX_OPERATOR;
            this.precedence = infixAnnotation.precedence();
            this.leftAssociative = infixAnnotation.leftAssociative();
            this.operandsLazy = infixAnnotation.operandsLazy();
        } else if (prefixAnnotation != null) {
            this.type = OperatorIfc.OperatorType.PREFIX_OPERATOR;
            this.precedence = prefixAnnotation.precedence();
            this.leftAssociative = prefixAnnotation.leftAssociative();
            this.operandsLazy = false;
        } else {
            if (postfixAnnotation == null) {
                throw new OperatorAnnotationNotFoundException(this.getClass().getName());
            }
            this.type = OperatorIfc.OperatorType.POSTFIX_OPERATOR;
            this.precedence = postfixAnnotation.precedence();
            this.leftAssociative = postfixAnnotation.leftAssociative();
            this.operandsLazy = false;
        }
    }

    @Override
    public int getPrecedence(ExpressionConfiguration configuration) {
        return this.getPrecedence();
    }

    @Override
    public boolean isLeftAssociative() {
        return this.leftAssociative;
    }

    @Override
    public boolean isOperandLazy() {
        return this.operandsLazy;
    }

    @Override
    public boolean isPrefix() {
        return this.type == OperatorIfc.OperatorType.PREFIX_OPERATOR;
    }

    @Override
    public boolean isPostfix() {
        return this.type == OperatorIfc.OperatorType.POSTFIX_OPERATOR;
    }

    @Override
    public boolean isInfix() {
        return this.type == OperatorIfc.OperatorType.INFIX_OPERATOR;
    }

    @Generated
    @Override
    public int getPrecedence() {
        return this.precedence;
    }
}