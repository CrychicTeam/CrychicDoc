package net.minecraft.world.level.block.state.pattern;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class BlockPatternBuilder {

    private static final Joiner COMMA_JOINED = Joiner.on(",");

    private final List<String[]> pattern = Lists.newArrayList();

    private final Map<Character, Predicate<BlockInWorld>> lookup = Maps.newHashMap();

    private int height;

    private int width;

    private BlockPatternBuilder() {
        this.lookup.put(' ', (Predicate) p_187549_ -> true);
    }

    public BlockPatternBuilder aisle(String... string0) {
        if (!ArrayUtils.isEmpty(string0) && !StringUtils.isEmpty(string0[0])) {
            if (this.pattern.isEmpty()) {
                this.height = string0.length;
                this.width = string0[0].length();
            }
            if (string0.length != this.height) {
                throw new IllegalArgumentException("Expected aisle with height of " + this.height + ", but was given one with a height of " + string0.length + ")");
            } else {
                for (String $$1 : string0) {
                    if ($$1.length() != this.width) {
                        throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.width + ", found one with " + $$1.length() + ")");
                    }
                    for (char $$2 : $$1.toCharArray()) {
                        if (!this.lookup.containsKey($$2)) {
                            this.lookup.put($$2, null);
                        }
                    }
                }
                this.pattern.add(string0);
                return this;
            }
        } else {
            throw new IllegalArgumentException("Empty pattern for aisle");
        }
    }

    public static BlockPatternBuilder start() {
        return new BlockPatternBuilder();
    }

    public BlockPatternBuilder where(char char0, Predicate<BlockInWorld> predicateBlockInWorld1) {
        this.lookup.put(char0, predicateBlockInWorld1);
        return this;
    }

    public BlockPattern build() {
        return new BlockPattern(this.createPattern());
    }

    private Predicate<BlockInWorld>[][][] createPattern() {
        this.ensureAllCharactersMatched();
        Predicate<BlockInWorld>[][][] $$0 = (Predicate<BlockInWorld>[][][]) Array.newInstance(Predicate.class, new int[] { this.pattern.size(), this.height, this.width });
        for (int $$1 = 0; $$1 < this.pattern.size(); $$1++) {
            for (int $$2 = 0; $$2 < this.height; $$2++) {
                for (int $$3 = 0; $$3 < this.width; $$3++) {
                    $$0[$$1][$$2][$$3] = (Predicate<BlockInWorld>) this.lookup.get(((String[]) this.pattern.get($$1))[$$2].charAt($$3));
                }
            }
        }
        return $$0;
    }

    private void ensureAllCharactersMatched() {
        List<Character> $$0 = Lists.newArrayList();
        for (Entry<Character, Predicate<BlockInWorld>> $$1 : this.lookup.entrySet()) {
            if ($$1.getValue() == null) {
                $$0.add((Character) $$1.getKey());
            }
        }
        if (!$$0.isEmpty()) {
            throw new IllegalStateException("Predicates for character(s) " + COMMA_JOINED.join($$0) + " are missing");
        }
    }
}