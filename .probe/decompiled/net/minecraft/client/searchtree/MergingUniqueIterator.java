package net.minecraft.client.searchtree;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import java.util.Comparator;
import java.util.Iterator;

public class MergingUniqueIterator<T> extends AbstractIterator<T> {

    private final PeekingIterator<T> firstIterator;

    private final PeekingIterator<T> secondIterator;

    private final Comparator<T> comparator;

    public MergingUniqueIterator(Iterator<T> iteratorT0, Iterator<T> iteratorT1, Comparator<T> comparatorT2) {
        this.firstIterator = Iterators.peekingIterator(iteratorT0);
        this.secondIterator = Iterators.peekingIterator(iteratorT1);
        this.comparator = comparatorT2;
    }

    protected T computeNext() {
        boolean $$0 = !this.firstIterator.hasNext();
        boolean $$1 = !this.secondIterator.hasNext();
        if ($$0 && $$1) {
            return (T) this.endOfData();
        } else if ($$0) {
            return (T) this.secondIterator.next();
        } else if ($$1) {
            return (T) this.firstIterator.next();
        } else {
            int $$2 = this.comparator.compare(this.firstIterator.peek(), this.secondIterator.peek());
            if ($$2 == 0) {
                this.secondIterator.next();
            }
            return (T) ($$2 <= 0 ? this.firstIterator.next() : this.secondIterator.next());
        }
    }
}