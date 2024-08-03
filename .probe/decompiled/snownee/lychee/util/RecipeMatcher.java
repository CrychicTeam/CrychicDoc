package snownee.lychee.util;

import java.util.BitSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class RecipeMatcher<T> {

    public List<T> inputs;

    public List<? extends Predicate<T>> tests;

    public int[] inputCapacity;

    public int[] inputUsed;

    public int[][] use;

    private BitSet data;

    private BitSet mask;

    public RecipeMatcher(List<T> inputs, List<? extends Predicate<T>> tests, int[] inputCapacity) {
        this.inputs = inputs;
        this.tests = tests;
        this.inputCapacity = inputCapacity;
        this.inputUsed = new int[inputs.size()];
        this.use = new int[inputs.size()][tests.size()];
        this.data = new BitSet(inputs.size() * tests.size());
        this.mask = new BitSet(inputs.size());
        for (int i = 0; i < tests.size(); i++) {
            Predicate<T> test = (Predicate<T>) tests.get(i);
            int offset = i * inputs.size();
            for (int j = 0; j < inputs.size(); j++) {
                if (test.test(inputs.get(j))) {
                    this.data.set(offset + j);
                }
            }
        }
        for (int i = 0; i < tests.size(); i++) {
            this.mask.clear();
            if (!this.match(i)) {
                this.inputUsed = null;
                return;
            }
        }
    }

    private boolean match(int test) {
        int offset = test * this.inputs.size();
        for (int i = 0; i < this.inputs.size(); i++) {
            if (this.data.get(offset + i) && !this.mask.get(i)) {
                this.mask.set(i);
                if (this.inputUsed[i] < this.inputCapacity[i]) {
                    this.use[i][this.inputUsed[i]] = test;
                    this.inputUsed[i]++;
                    return true;
                }
                for (int j = 0; j < this.inputUsed[i]; j++) {
                    if (this.match(this.use[i][j])) {
                        this.use[i][j] = test;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static <T> Optional<RecipeMatcher<T>> findMatches(List<T> inputs, List<? extends Predicate<T>> tests, int[] amount) {
        int sum = 0;
        for (int i = 0; i < amount.length; i++) {
            sum += amount[i];
        }
        int testSize = tests.size();
        if (sum < testSize) {
            return Optional.empty();
        } else {
            RecipeMatcher<T> matcher = new RecipeMatcher<>(inputs, tests, amount);
            return matcher.inputUsed == null ? Optional.empty() : Optional.of(matcher);
        }
    }
}