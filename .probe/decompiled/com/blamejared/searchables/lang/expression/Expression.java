package com.blamejared.searchables.lang.expression;

import com.blamejared.searchables.lang.expression.visitor.ContextAwareVisitor;
import com.blamejared.searchables.lang.expression.visitor.Visitor;

public abstract class Expression {

    public abstract <R> R accept(Visitor<R> var1);

    public abstract <R, C> R accept(ContextAwareVisitor<R, C> var1, C var2);
}