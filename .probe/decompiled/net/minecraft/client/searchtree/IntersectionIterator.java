package net.minecraft.client.searchtree;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import java.util.Comparator;
import java.util.Iterator;

public class IntersectionIterator<T> extends AbstractIterator<T> {

    private final PeekingIterator<T> firstIterator;

    private final PeekingIterator<T> secondIterator;

    private final Comparator<T> comparator;

    public IntersectionIterator(Iterator<T> iteratorT0, Iterator<T> iteratorT1, Comparator<T> comparatorT2) {
        this.firstIterator = Iterators.peekingIterator(iteratorT0);
        this.secondIterator = Iterators.peekingIterator(iteratorT1);
        this.comparator = comparatorT2;
    }

    protected T computeNext() {
        while (this.firstIterator.hasNext() && this.secondIterator.hasNext()) {
            int $$0 = this.comparator.compare(this.firstIterator.peek(), this.secondIterator.peek());
            if ($$0 == 0) {
                this.secondIterator.next();
                return (T) this.firstIterator.next();
            }
            if ($$0 < 0) {
                this.firstIterator.next();
            } else {
                this.secondIterator.next();
            }
        }
        return (T) this.endOfData();
    }
}