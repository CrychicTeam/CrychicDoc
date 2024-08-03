package com.blamejared.searchables.api.formatter;

import com.blamejared.searchables.api.SearchableType;
import com.blamejared.searchables.api.TokenRange;
import com.blamejared.searchables.lang.StringSearcher;
import com.blamejared.searchables.lang.expression.type.ComponentExpression;
import com.blamejared.searchables.lang.expression.type.GroupingExpression;
import com.blamejared.searchables.lang.expression.type.LiteralExpression;
import com.blamejared.searchables.lang.expression.type.PairedExpression;
import com.blamejared.searchables.lang.expression.visitor.ContextAwareVisitor;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

public class FormattingVisitor implements ContextAwareVisitor<TokenRange, FormattingContext>, Consumer<String>, BiFunction<String, Integer, FormattedCharSequence> {

    private final SearchableType<?> type;

    private final List<Pair<TokenRange, Style>> tokens = new ArrayList();

    private TokenRange lastRange = TokenRange.at(0);

    public FormattingVisitor(SearchableType<?> type) {
        this.type = type;
    }

    public void reset() {
        this.tokens.clear();
        this.lastRange = TokenRange.at(0);
    }

    public List<Pair<TokenRange, Style>> tokens() {
        return this.tokens;
    }

    public Optional<Pair<TokenRange, Style>> tokenAt(int position) {
        return this.tokens.stream().filter(range -> ((TokenRange) range.getFirst()).contains(position)).findFirst();
    }

    public TokenRange visitGrouping(GroupingExpression expr, FormattingContext context) {
        TokenRange leftRange = expr.left().accept(this, context);
        this.tokens.add(Pair.of(this.getAndPushRange(), context.style()));
        TokenRange rightRange = expr.right().accept(this, context);
        return TokenRange.encompassing(leftRange, rightRange);
    }

    public TokenRange visitComponent(ComponentExpression expr, FormattingContext context) {
        boolean valid = context.valid() && expr.left() instanceof LiteralExpression && expr.right() instanceof LiteralExpression;
        TokenRange leftRange = expr.left().accept(this, FormattingContext.key(FormattingConstants.KEY, valid));
        this.tokens.add(Pair.of(this.getAndPushRange(), context.style(valid)));
        TokenRange rightRange = expr.right().accept(this, FormattingContext.literal(FormattingConstants.TERM, valid));
        return TokenRange.encompassing(leftRange, rightRange);
    }

    public TokenRange visitLiteral(LiteralExpression expr, FormattingContext context) {
        Style style = context.style();
        if (!context.valid() || context.isKey() && !this.type.components().containsKey(expr.value())) {
            style = FormattingConstants.INVALID;
        }
        TokenRange range = this.getAndPushRange(expr.displayValue().length());
        this.tokens.add(Pair.of(range, style));
        return range;
    }

    public TokenRange visitPaired(PairedExpression expr, FormattingContext context) {
        TokenRange leftRange = expr.first().accept(this, context);
        TokenRange rightRange = expr.second().accept(this, context);
        return TokenRange.encompassing(leftRange, rightRange);
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
        StringSearcher.search(search, this, FormattingContext.empty());
    }

    public FormattedCharSequence apply(String currentString, Integer offset) {
        List<FormattedCharSequence> sequences = new ArrayList();
        int index = 0;
        for (Pair<TokenRange, Style> token : this.tokens) {
            TokenRange range = (TokenRange) token.getFirst();
            int subEnd = Math.max(range.start() - offset, 0);
            if (subEnd >= currentString.length()) {
                break;
            }
            int subStart = Math.min(range.end() - offset, currentString.length());
            if (subStart > 0) {
                sequences.add(FormattedCharSequence.forward(currentString.substring(index, subEnd), (Style) token.getSecond()));
                sequences.add(FormattedCharSequence.forward(currentString.substring(subEnd, subStart), (Style) token.getSecond()));
                index = subStart;
            }
        }
        sequences.add(FormattedCharSequence.forward(currentString.substring(index), Style.EMPTY));
        return FormattedCharSequence.composite(sequences);
    }
}