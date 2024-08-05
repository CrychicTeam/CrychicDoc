package com.blamejared.searchables.lang.expression.type;

import com.blamejared.searchables.lang.Token;
import com.blamejared.searchables.lang.expression.Expression;
import com.blamejared.searchables.lang.expression.visitor.ContextAwareVisitor;
import com.blamejared.searchables.lang.expression.visitor.Visitor;

public class ComponentExpression extends Expression {

    private final Expression left;

    private final Token operator;

    private final Expression right;

    public ComponentExpression(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitComponent(this);
    }

    @Override
    public <R, C> R accept(ContextAwareVisitor<R, C> visitor, C context) {
        return visitor.visitComponent(this, context);
    }

    public Expression left() {
        return this.left;
    }

    public Token operator() {
        return this.operator;
    }

    public Expression right() {
        return this.right;
    }

    public String toString() {
        return "[%s%s%s]".formatted(this.left, this.operator.literal(), this.right);
    }
}