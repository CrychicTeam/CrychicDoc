package com.blamejared.searchables.api.context;

import com.blamejared.searchables.lang.expression.type.ComponentExpression;
import com.blamejared.searchables.lang.expression.type.GroupingExpression;
import com.blamejared.searchables.lang.expression.type.LiteralExpression;
import com.blamejared.searchables.lang.expression.type.PairedExpression;
import com.blamejared.searchables.lang.expression.visitor.Visitor;

public final class ContextVisitor<T> implements Visitor<SearchContext<T>> {

    private final SearchContext<T> context = new SearchContext<>();

    public SearchContext<T> visitGrouping(GroupingExpression expr) {
        expr.left().accept(this);
        expr.right().accept(this);
        return this.context;
    }

    public SearchContext<T> visitComponent(ComponentExpression expr) {
        if (expr.left() instanceof LiteralExpression leftLit && expr.right() instanceof LiteralExpression rightLit) {
            this.context.add(new SearchComponent<>(leftLit.value(), rightLit.value()));
        }
        return this.context;
    }

    public SearchContext<T> visitLiteral(LiteralExpression expr) {
        this.context.add(new SearchLiteral<>(expr.value()));
        return this.context;
    }

    public SearchContext<T> visitPaired(PairedExpression expr) {
        expr.first().accept(this);
        expr.second().accept(this);
        return this.context;
    }
}