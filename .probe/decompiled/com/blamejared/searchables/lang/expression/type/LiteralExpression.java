package com.blamejared.searchables.lang.expression.type;

import com.blamejared.searchables.lang.expression.Expression;
import com.blamejared.searchables.lang.expression.visitor.ContextAwareVisitor;
import com.blamejared.searchables.lang.expression.visitor.Visitor;

public class LiteralExpression extends Expression {

    private final String value;

    private final String displayValue;

    public LiteralExpression(String value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitLiteral(this);
    }

    @Override
    public <R, C> R accept(ContextAwareVisitor<R, C> visitor, C context) {
        return visitor.visitLiteral(this, context);
    }

    public String value() {
        return this.value;
    }

    public String displayValue() {
        return this.displayValue;
    }

    public String toString() {
        return this.value;
    }
}