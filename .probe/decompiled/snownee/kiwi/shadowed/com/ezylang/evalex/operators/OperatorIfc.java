package snownee.kiwi.shadowed.com.ezylang.evalex.operators;

import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

public interface OperatorIfc {

    int OPERATOR_PRECEDENCE_OR = 2;

    int OPERATOR_PRECEDENCE_AND = 4;

    int OPERATOR_PRECEDENCE_EQUALITY = 7;

    int OPERATOR_PRECEDENCE_COMPARISON = 10;

    int OPERATOR_PRECEDENCE_ADDITIVE = 20;

    int OPERATOR_PRECEDENCE_MULTIPLICATIVE = 30;

    int OPERATOR_PRECEDENCE_POWER = 40;

    int OPERATOR_PRECEDENCE_UNARY = 60;

    int OPERATOR_PRECEDENCE_POWER_HIGHER = 80;

    int getPrecedence();

    boolean isLeftAssociative();

    boolean isPrefix();

    boolean isPostfix();

    boolean isInfix();

    int getPrecedence(ExpressionConfiguration var1);

    boolean isOperandLazy();

    EvaluationValue evaluate(Expression var1, Token var2, EvaluationValue... var3) throws EvaluationException;

    public static enum OperatorType {

        PREFIX_OPERATOR, POSTFIX_OPERATOR, INFIX_OPERATOR
    }
}