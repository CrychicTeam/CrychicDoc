package com.blamejared.searchables.lang.expression.visitor;

import com.blamejared.searchables.lang.expression.type.ComponentExpression;
import com.blamejared.searchables.lang.expression.type.GroupingExpression;
import com.blamejared.searchables.lang.expression.type.LiteralExpression;
import com.blamejared.searchables.lang.expression.type.PairedExpression;

public interface ContextAwareVisitor<R, C> {

    R visitGrouping(GroupingExpression var1, C var2);

    R visitComponent(ComponentExpression var1, C var2);

    R visitLiteral(LiteralExpression var1, C var2);

    R visitPaired(PairedExpression var1, C var2);

    default R postVisit(R obj, C context) {
        return obj;
    }
}