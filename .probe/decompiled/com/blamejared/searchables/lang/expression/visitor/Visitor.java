package com.blamejared.searchables.lang.expression.visitor;

import com.blamejared.searchables.lang.expression.type.ComponentExpression;
import com.blamejared.searchables.lang.expression.type.GroupingExpression;
import com.blamejared.searchables.lang.expression.type.LiteralExpression;
import com.blamejared.searchables.lang.expression.type.PairedExpression;

public interface Visitor<R> {

    R visitGrouping(GroupingExpression var1);

    R visitComponent(ComponentExpression var1);

    R visitLiteral(LiteralExpression var1);

    R visitPaired(PairedExpression var1);

    default R postVisit(R obj) {
        return obj;
    }
}