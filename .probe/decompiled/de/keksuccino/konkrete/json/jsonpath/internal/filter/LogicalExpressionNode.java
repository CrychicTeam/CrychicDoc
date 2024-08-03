package de.keksuccino.konkrete.json.jsonpath.internal.filter;

import de.keksuccino.konkrete.json.jsonpath.Predicate;
import de.keksuccino.konkrete.json.jsonpath.internal.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LogicalExpressionNode extends ExpressionNode {

    protected List<ExpressionNode> chain = new ArrayList();

    private final LogicalOperator operator;

    public static ExpressionNode createLogicalNot(ExpressionNode op) {
        return new LogicalExpressionNode(op, LogicalOperator.NOT, null);
    }

    public static LogicalExpressionNode createLogicalOr(ExpressionNode left, ExpressionNode right) {
        return new LogicalExpressionNode(left, LogicalOperator.OR, right);
    }

    public static LogicalExpressionNode createLogicalOr(Collection<ExpressionNode> operands) {
        return new LogicalExpressionNode(LogicalOperator.OR, operands);
    }

    public static LogicalExpressionNode createLogicalAnd(ExpressionNode left, ExpressionNode right) {
        return new LogicalExpressionNode(left, LogicalOperator.AND, right);
    }

    public static LogicalExpressionNode createLogicalAnd(Collection<ExpressionNode> operands) {
        return new LogicalExpressionNode(LogicalOperator.AND, operands);
    }

    private LogicalExpressionNode(ExpressionNode left, LogicalOperator operator, ExpressionNode right) {
        this.chain.add(left);
        this.chain.add(right);
        this.operator = operator;
    }

    private LogicalExpressionNode(LogicalOperator operator, Collection<ExpressionNode> operands) {
        this.chain.addAll(operands);
        this.operator = operator;
    }

    public LogicalExpressionNode and(LogicalExpressionNode other) {
        return createLogicalAnd(this, other);
    }

    public LogicalExpressionNode or(LogicalExpressionNode other) {
        return createLogicalOr(this, other);
    }

    public LogicalOperator getOperator() {
        return this.operator;
    }

    public LogicalExpressionNode append(ExpressionNode expressionNode) {
        this.chain.add(0, expressionNode);
        return this;
    }

    public String toString() {
        return "(" + Utils.join(" " + this.operator.getOperatorString() + " ", this.chain) + ")";
    }

    @Override
    public boolean apply(Predicate.PredicateContext ctx) {
        if (this.operator == LogicalOperator.OR) {
            for (ExpressionNode expression : this.chain) {
                if (expression.apply(ctx)) {
                    return true;
                }
            }
            return false;
        } else if (this.operator == LogicalOperator.AND) {
            for (ExpressionNode expressionx : this.chain) {
                if (!expressionx.apply(ctx)) {
                    return false;
                }
            }
            return true;
        } else {
            ExpressionNode expressionxx = (ExpressionNode) this.chain.get(0);
            return !expressionxx.apply(ctx);
        }
    }
}