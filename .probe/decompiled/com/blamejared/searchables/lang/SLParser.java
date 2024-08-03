package com.blamejared.searchables.lang;

import com.blamejared.searchables.lang.expression.Expression;
import com.blamejared.searchables.lang.expression.type.ComponentExpression;
import com.blamejared.searchables.lang.expression.type.GroupingExpression;
import com.blamejared.searchables.lang.expression.type.LiteralExpression;
import com.blamejared.searchables.lang.expression.type.PairedExpression;
import java.util.List;
import java.util.Optional;

public class SLParser {

    private final List<Token> tokens;

    private int current = 0;

    public SLParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Optional<Expression> parse() {
        return this.tokens.size() == 1 && this.check(TokenType.EOL) ? Optional.empty() : Optional.of(this.expression());
    }

    private Expression expression() {
        return this.grouping();
    }

    private Expression grouping() {
        Expression expr = this.literal();
        while (this.match(TokenType.SPACE)) {
            Token operator = this.previous();
            Expression right = this.literal();
            expr = (Expression) (operator.type() == TokenType.COLON ? new ComponentExpression(expr, operator, right) : new GroupingExpression(expr, operator, right));
        }
        return expr;
    }

    private Expression literal() {
        if (this.match(TokenType.COLON)) {
            Token prevColon = this.previous();
            if (this.match(TokenType.IDENTIFIER)) {
                Token prevIdent = this.previous();
                LiteralExpression first = new LiteralExpression(prevColon.literal(), prevColon.lexeme());
                LiteralExpression second = new LiteralExpression(prevIdent.literal(), prevIdent.lexeme());
                return new PairedExpression(first, second);
            }
        }
        if (this.match(TokenType.IDENTIFIER)) {
            Token previous = this.previous();
            return (Expression) (this.check(TokenType.COLON) ? new ComponentExpression(new LiteralExpression(previous.literal(), previous.lexeme()), this.advance(), this.literal()) : new LiteralExpression(previous.literal(), previous.lexeme()));
        } else if (this.match(TokenType.STRING)) {
            Token previous = this.previous();
            LiteralExpression literalExpression = new LiteralExpression(previous.literal(), previous.lexeme());
            return (Expression) (!this.check(TokenType.SPACE) && !this.isAtEnd() ? new PairedExpression(literalExpression, this.expression()) : literalExpression);
        } else {
            return new LiteralExpression("", "");
        }
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (this.check(type)) {
                this.advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        return this.isAtEnd() ? false : this.peek().type() == type;
    }

    private Token advance() {
        if (!this.isAtEnd()) {
            this.current++;
        }
        return this.previous();
    }

    private boolean isAtEnd() {
        return this.peek().type() == TokenType.EOL;
    }

    private Token peek() {
        return (Token) this.tokens.get(this.current);
    }

    private Token previous() {
        return (Token) this.tokens.get(this.current - 1);
    }
}