package com.blamejared.searchables.api;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.jetbrains.annotations.NotNull;

public final class TokenRange implements Comparable<TokenRange>, Iterable<TokenRange> {

    public static final TokenRange EMPTY = at(0);

    private final int start;

    private final int end;

    private final SortedSet<TokenRange> subRanges;

    public static TokenRange at(int position) {
        return new TokenRange(position, position);
    }

    public static TokenRange between(int start, int end) {
        return new TokenRange(start, end);
    }

    public static TokenRange encompassing(TokenRange first, TokenRange second) {
        return new TokenRange(Math.min(first.start(), second.start()), Math.max(first.end(), second.end()));
    }

    private TokenRange(int start, int end) {
        this.start = start;
        this.end = end;
        this.subRanges = new TreeSet();
    }

    public void addRange(TokenRange range) {
        this.subRanges.add(range);
    }

    public void addRanges(Collection<TokenRange> ranges) {
        this.subRanges.addAll(ranges);
    }

    public Set<TokenRange> subRanges() {
        return this.subRanges;
    }

    public TokenRange range(int index) {
        return (TokenRange) this.subRanges().stream().skip((long) index).findFirst().orElseThrow(IndexOutOfBoundsException::new);
    }

    public int rangeIndexAtPosition(int position) {
        if (!this.contains(position)) {
            throw new IndexOutOfBoundsException();
        } else {
            int i = 0;
            for (TokenRange subRange : this.subRanges()) {
                if (subRange.contains(position)) {
                    break;
                }
                i++;
            }
            return i;
        }
    }

    public TokenRange rangeAtPosition(int position) {
        return (TokenRange) this.subRanges.stream().filter(tokenRange -> tokenRange.contains(position)).findFirst().orElse(this);
    }

    public TokenRange simplify() {
        return between(this.start(), this.end());
    }

    public boolean covers(TokenRange other) {
        return this.start() <= other.start() && other.end() <= this.end();
    }

    public boolean contains(int position) {
        return this.start() <= position && position <= this.end();
    }

    public String substring(String of) {
        return of.substring(this.start(), this.end());
    }

    public String substring(String of, int end) {
        return of.substring(this.start(), end);
    }

    public String delete(String from) {
        return new StringBuilder(from).delete(this.start(), this.end()).toString();
    }

    public String insert(String to, String toInsert) {
        return new StringBuilder(to).insert(this.start(), toInsert).toString();
    }

    public String replace(String into, String toInsert) {
        return this.insert(this.delete(into), toInsert);
    }

    public boolean isEmpty() {
        return this.start() == this.end();
    }

    public int length() {
        return this.end() - this.start();
    }

    public int start() {
        return this.start;
    }

    public int end() {
        return this.end;
    }

    public TokenRange recalculate() {
        if (this.subRanges().isEmpty()) {
            return this;
        } else {
            int rangeStart = (Integer) this.subRanges().stream().min(Comparator.comparing(TokenRange::end)).map(TokenRange::start).orElse(this.start());
            int rangeEnd = (Integer) this.subRanges().stream().max(Comparator.comparing(TokenRange::end)).map(TokenRange::end).orElse(this.end());
            if (rangeStart == this.start() && rangeEnd == this.end()) {
                return this;
            } else {
                TokenRange newRange = between(rangeStart, rangeEnd);
                newRange.subRanges().addAll(this.subRanges());
                return newRange;
            }
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            TokenRange that = (TokenRange) o;
            if (this.start != that.start) {
                return false;
            } else {
                return this.end != that.end ? false : Objects.equals(this.subRanges, that.subRanges);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.start;
        result = 31 * result + this.end;
        return 31 * result + this.subRanges.hashCode();
    }

    public String toString() {
        return "TokenRange{start=" + this.start + ", end=" + this.end + ", subRanges=" + this.subRanges + "}";
    }

    public int compareTo(@NotNull TokenRange o) {
        return Integer.compare(this.start(), o.start());
    }

    @NotNull
    public Iterator<TokenRange> iterator() {
        return this.subRanges().iterator();
    }
}