package com.blamejared.searchables.lang;

import com.blamejared.searchables.lang.expression.Expression;
import com.blamejared.searchables.lang.expression.visitor.ContextAwareVisitor;
import com.blamejared.searchables.lang.expression.visitor.Visitor;
import java.util.Optional;

public class StringSearcher {

    public static <T> Optional<T> search(String search, Visitor<T> visitor) {
        SLParser slParser = new SLParser(new SLScanner(search).scanTokens());
        return slParser.parse().map(expression -> expression.accept(visitor)).map(visitor::postVisit);
    }

    public static <T, C> Optional<T> search(String search, ContextAwareVisitor<T, C> visitor, C context) {
        SLParser slParser = new SLParser(new SLScanner(search).scanTokens());
        return slParser.parse().map(expression -> expression.accept(visitor, context)).map(t -> visitor.postVisit((T) t, context));
    }

    public static Optional<Expression> expression(String search) {
        SLParser slParser = new SLParser(new SLScanner(search).scanTokens());
        return slParser.parse();
    }
}