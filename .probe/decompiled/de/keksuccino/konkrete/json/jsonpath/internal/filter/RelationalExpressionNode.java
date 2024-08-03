package de.keksuccino.konkrete.json.jsonpath.internal.filter;

import de.keksuccino.konkrete.json.jsonpath.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelationalExpressionNode extends ExpressionNode {

    private static final Logger logger = LoggerFactory.getLogger(RelationalExpressionNode.class);

    private final ValueNode left;

    private final RelationalOperator relationalOperator;

    private final ValueNode right;

    public RelationalExpressionNode(ValueNode left, RelationalOperator relationalOperator, ValueNode right) {
        this.left = left;
        this.relationalOperator = relationalOperator;
        this.right = right;
        logger.trace("ExpressionNode {}", this.toString());
    }

    public String toString() {
        return this.relationalOperator == RelationalOperator.EXISTS ? this.left.toString() : this.left.toString() + " " + this.relationalOperator.toString() + " " + this.right.toString();
    }

    @Override
    public boolean apply(Predicate.PredicateContext ctx) {
        ValueNode l = this.left;
        ValueNode r = this.right;
        if (this.left.isPathNode()) {
            l = this.left.asPathNode().evaluate(ctx);
        }
        if (this.right.isPathNode()) {
            r = this.right.asPathNode().evaluate(ctx);
        }
        Evaluator evaluator = EvaluatorFactory.createEvaluator(this.relationalOperator);
        return evaluator != null ? evaluator.evaluate(l, r, ctx) : false;
    }
}