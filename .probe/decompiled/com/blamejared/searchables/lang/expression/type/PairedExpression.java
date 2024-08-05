package com.blamejared.searchables.lang.expression.type;

import com.blamejared.searchables.lang.expression.Expression;
import com.blamejared.searchables.lang.expression.visitor.ContextAwareVisitor;
import com.blamejared.searchables.lang.expression.visitor.Visitor;

public class PairedExpression extends Expression {

    private final Expression first;

    private final Expression second;

    public PairedExpression(Expression first, Expression second) {
        this.first = first;
        this.second = second;
    }

    public Expression first() {
        return this.first;
    }

    public Expression second() {
        return this.second;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitPaired(this);
    }

    @Override
    public <R, C> R accept(ContextAwareVisitor<R, C> visitor, C context) {
        return visitor.visitPaired(this, context);
    }
}