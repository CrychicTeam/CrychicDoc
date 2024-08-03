package com.blamejared.searchables.api.autcomplete;

import com.blamejared.searchables.api.TokenRange;
import com.blamejared.searchables.lang.StringSearcher;
import com.blamejared.searchables.lang.expression.type.ComponentExpression;
import com.blamejared.searchables.lang.expression.type.GroupingExpression;
import com.blamejared.searchables.lang.expression.type.LiteralExpression;
import com.blamejared.searchables.lang.expression.type.PairedExpression;
import com.blamejared.searchables.lang.expression.visitor.Visitor;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Consumer;

public class CompletionVisitor implements Visitor<TokenRange>, Consumer<String> {

    private final List<TokenRange> tokens = new ArrayList();

    private TokenRange lastRange = TokenRange.EMPTY;

    public void reset() {
        this.tokens.clear();
        this.lastRange = TokenRange.EMPTY;
    }

    protected void reduceTokens() {
        ListIterator<TokenRange> iterator = this.tokens.listIterator(this.tokens.size());
        TokenRange last = null;
        while (iterator.hasPrevious()) {
            TokenRange previous = (TokenRange) iterator.previous();
            if (last != null && last.covers(previous)) {
                last.addRange(previous);
                iterator.remove();
            } else {
                last = previous;
            }
        }
    }

    public List<TokenRange> tokens() {
        return this.tokens;
    }

    public Optional<TokenRange> tokenAt(int position) {
        return this.tokens.stream().filter(range -> range.contains(position)).findFirst();
    }

    public TokenRange rangeAt(int position) {
        return (TokenRange) this.tokenAt(position).orElse(TokenRange.EMPTY);
    }

    public TokenRange visitGrouping(GroupingExpression expr) {
        TokenRange leftRange = expr.left().accept(this);
        this.getAndPushRange();
        TokenRange rightRange = expr.right().accept(this);
        return TokenRange.encompassing(leftRange, rightRange);
    }

    public TokenRange visitComponent(ComponentExpression expr) {
        TokenRange leftRange = expr.left().accept(this);
        this.addToken(this.getAndPushRange());
        TokenRange rightRange = expr.right().accept(this);
        return this.addToken(TokenRange.encompassing(leftRange, rightRange));
    }

    public TokenRange visitLiteral(LiteralExpression expr) {
        return this.addToken(this.getAndPushRange(expr.displayValue().length()));
    }

    public TokenRange visitPaired(PairedExpression expr) {
        TokenRange leftRange = this.addToken(expr.first().accept(this));
        TokenRange rightRange = this.addToken(expr.second().accept(this));
        return this.addToken(TokenRange.encompassing(leftRange, rightRange));
    }

    private TokenRange addToken(TokenRange range) {
        this.tokens.add(range.recalculate());
        return range;
    }

    private TokenRange getAndPushRange() {
        return this.getAndPushRange(1);
    }

    private TokenRange getAndPushRange(int end) {
        TokenRange oldRange = this.lastRange;
        this.lastRange = TokenRange.between(this.lastRange.end(), this.lastRange.end() + end);
        return TokenRange.between(oldRange.end(), oldRange.end() + end);
    }

    public void accept(String search) {
        this.reset();
        StringSearcher.search(search, this);
    }

    public TokenRange postVisit(TokenRange obj) {
        this.reduceTokens();
        return Visitor.super.postVisit(obj);
    }
}