package net.minecraft.world.level.storage.loot.parameters;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.ValidationContext;

public class LootContextParamSet {

    private final Set<LootContextParam<?>> required;

    private final Set<LootContextParam<?>> all;

    LootContextParamSet(Set<LootContextParam<?>> setLootContextParam0, Set<LootContextParam<?>> setLootContextParam1) {
        this.required = ImmutableSet.copyOf(setLootContextParam0);
        this.all = ImmutableSet.copyOf(Sets.union(setLootContextParam0, setLootContextParam1));
    }

    public boolean isAllowed(LootContextParam<?> lootContextParam0) {
        return this.all.contains(lootContextParam0);
    }

    public Set<LootContextParam<?>> getRequired() {
        return this.required;
    }

    public Set<LootContextParam<?>> getAllowed() {
        return this.all;
    }

    public String toString() {
        return "[" + Joiner.on(", ").join(this.all.stream().map(p_81400_ -> (this.required.contains(p_81400_) ? "!" : "") + p_81400_.getName()).iterator()) + "]";
    }

    public void validateUser(ValidationContext validationContext0, LootContextUser lootContextUser1) {
        Set<LootContextParam<?>> $$2 = lootContextUser1.getReferencedContextParams();
        Set<LootContextParam<?>> $$3 = Sets.difference($$2, this.all);
        if (!$$3.isEmpty()) {
            validationContext0.reportProblem("Parameters " + $$3 + " are not provided in this context");
        }
    }

    public static LootContextParamSet.Builder builder() {
        return new LootContextParamSet.Builder();
    }

    public static class Builder {

        private final Set<LootContextParam<?>> required = Sets.newIdentityHashSet();

        private final Set<LootContextParam<?>> optional = Sets.newIdentityHashSet();

        public LootContextParamSet.Builder required(LootContextParam<?> lootContextParam0) {
            if (this.optional.contains(lootContextParam0)) {
                throw new IllegalArgumentException("Parameter " + lootContextParam0.getName() + " is already optional");
            } else {
                this.required.add(lootContextParam0);
                return this;
            }
        }

        public LootContextParamSet.Builder optional(LootContextParam<?> lootContextParam0) {
            if (this.required.contains(lootContextParam0)) {
                throw new IllegalArgumentException("Parameter " + lootContextParam0.getName() + " is already required");
            } else {
                this.optional.add(lootContextParam0);
                return this;
            }
        }

        public LootContextParamSet build() {
            return new LootContextParamSet(this.required, this.optional);
        }
    }
}