package icyllis.arc3d.compiler.parser;

import java.util.ArrayDeque;
import java.util.Deque;
import javax.annotation.Nonnull;

public final class RegexParser {

    private static final char EOF = '\u0000';

    private final Deque<RegexNode> mStack = new ArrayDeque();

    private String mSource;

    private int mOffset;

    @Nonnull
    public RegexNode parse(@Nonnull String source) {
        this.mSource = source;
        this.mOffset = 0;
        assert this.mStack.size() == 0;
        this.regex();
        assert this.mStack.size() == 1;
        assert this.mOffset == source.length();
        this.mSource = null;
        return this.pop();
    }

    private char peek() {
        return this.mOffset >= this.mSource.length() ? '\u0000' : this.mSource.charAt(this.mOffset);
    }

    private void expect(char c) {
        if (this.peek() != c) {
            throw new IllegalStateException(String.format("expected '%c' at index %d, but found '%c'", c, this.mOffset, this.peek()));
        } else {
            this.mOffset++;
        }
    }

    private void push(@Nonnull RegexNode node) {
        this.mStack.push(node);
    }

    @Nonnull
    private RegexNode pop() {
        return (RegexNode) this.mStack.pop();
    }

    private void atom() {
        switch(this.peek()) {
            case '(':
                this.group();
                break;
            case '.':
                this.dot();
                break;
            case '[':
                this.clazz();
                break;
            default:
                this.literal();
        }
    }

    private void factor() {
        this.atom();
        switch(this.peek()) {
            case '*':
                this.push(RegexNode.Star(this.pop()));
                this.mOffset++;
                break;
            case '+':
                this.push(RegexNode.Plus(this.pop()));
                this.mOffset++;
                break;
            case '?':
                this.push(RegexNode.Ques(this.pop()));
                this.mOffset++;
        }
    }

    private void sequence() {
        this.factor();
        while (true) {
            switch(this.peek()) {
                case '\u0000':
                case ')':
                case '|':
                    return;
                default:
                    this.sequence();
                    RegexNode y = this.pop();
                    RegexNode x = this.pop();
                    this.push(RegexNode.Concat(x, y));
            }
        }
    }

    private static RegexNode escape(char c) {
        return switch(c) {
            case 'd' ->
                RegexNode.Range('0', '9');
            default ->
                RegexNode.Char(c);
            case 'n' ->
                RegexNode.Char('\n');
            case 'r' ->
                RegexNode.Char('\r');
            case 's' ->
                RegexNode.CharClass(RegexNode.Char('\t'), RegexNode.Char('\n'), RegexNode.Char('\u000b'), RegexNode.Char('\f'), RegexNode.Char('\r'), RegexNode.Char(' '));
            case 't' ->
                RegexNode.Char('\t');
            case 'w' ->
                RegexNode.CharClass(RegexNode.Range('a', 'z'), RegexNode.Range('A', 'Z'), RegexNode.Range('0', '9'), RegexNode.Char('_'));
        };
    }

    private void literal() {
        char c = this.peek();
        if (c == '\\') {
            this.mOffset++;
            this.push(escape(this.peek()));
        } else {
            this.push(RegexNode.Char(c));
        }
        this.mOffset++;
    }

    private void dot() {
        this.expect('.');
        this.push(RegexNode.Dot());
    }

    private void group() {
        this.expect('(');
        this.regex();
        this.expect(')');
    }

    private void item() {
        this.literal();
        if (this.peek() == '-') {
            this.mOffset++;
            if (this.peek() == ']') {
                this.push(RegexNode.Char('-'));
            } else {
                this.literal();
                RegexNode en = this.pop();
                RegexNode st = this.pop();
                this.push(RegexNode.Range(st, en));
            }
        }
    }

    private void clazz() {
        this.expect('[');
        int depth = this.mStack.size();
        boolean exclusive;
        if (this.peek() == '^') {
            this.mOffset++;
            exclusive = true;
        } else {
            exclusive = false;
        }
        while (true) {
            switch(this.peek()) {
                case '\u0000':
                    throw new IllegalStateException("unterminated character class");
                case ']':
                    this.mOffset++;
                    int n = this.mStack.size() - depth;
                    RegexNode[] clazz = new RegexNode[n];
                    for (int i = 0; i < n; i++) {
                        clazz[i] = this.pop();
                    }
                    this.push(RegexNode.CharClass(clazz, exclusive));
                    return;
                default:
                    this.item();
            }
        }
    }

    private void regex() {
        this.sequence();
        switch(this.peek()) {
            case '|':
                this.mOffset++;
                this.regex();
                RegexNode y = this.pop();
                RegexNode x = this.pop();
                this.push(RegexNode.Union(x, y));
            case '\u0000':
            case ')':
                return;
            default:
                throw new IllegalStateException();
        }
    }
}