package me.lucko.spark.common.command.tabcomplete;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabCompleter {

    private final Map<Integer, CompletionSupplier> suppliers = new HashMap();

    private int from = Integer.MAX_VALUE;

    public static TabCompleter create() {
        return new TabCompleter();
    }

    public static List<String> completeForOpts(List<String> args, String... options) {
        List<String> opts = new ArrayList(Arrays.asList(options));
        opts.removeAll(args);
        return create().from(0, CompletionSupplier.startsWith(opts)).complete(args);
    }

    private TabCompleter() {
    }

    public TabCompleter at(int position, CompletionSupplier supplier) {
        Preconditions.checkState(position < this.from);
        this.suppliers.put(position, supplier);
        return this;
    }

    public TabCompleter from(int position, CompletionSupplier supplier) {
        Preconditions.checkState(this.from == Integer.MAX_VALUE);
        this.suppliers.put(position, supplier);
        this.from = position;
        return this;
    }

    public List<String> complete(List<String> args) {
        int lastIndex = 0;
        String partial;
        return !args.isEmpty() && !(partial = (String) args.get(lastIndex = args.size() - 1)).trim().isEmpty() ? this.getCompletions(lastIndex, partial) : this.getCompletions(lastIndex, "");
    }

    private List<String> getCompletions(int position, String partial) {
        return position >= this.from ? ((CompletionSupplier) this.suppliers.get(this.from)).supplyCompletions(partial) : ((CompletionSupplier) this.suppliers.getOrDefault(position, CompletionSupplier.EMPTY)).supplyCompletions(partial);
    }
}