package net.minecraft.world.level.storage.loot;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class ValidationContext {

    private final Multimap<String, String> problems;

    private final Supplier<String> context;

    private final LootContextParamSet params;

    private final LootDataResolver resolver;

    private final Set<LootDataId<?>> visitedElements;

    @Nullable
    private String contextCache;

    public ValidationContext(LootContextParamSet lootContextParamSet0, LootDataResolver lootDataResolver1) {
        this(HashMultimap.create(), () -> "", lootContextParamSet0, lootDataResolver1, ImmutableSet.of());
    }

    public ValidationContext(Multimap<String, String> multimapStringString0, Supplier<String> supplierString1, LootContextParamSet lootContextParamSet2, LootDataResolver lootDataResolver3, Set<LootDataId<?>> setLootDataId4) {
        this.problems = multimapStringString0;
        this.context = supplierString1;
        this.params = lootContextParamSet2;
        this.resolver = lootDataResolver3;
        this.visitedElements = setLootDataId4;
    }

    private String getContext() {
        if (this.contextCache == null) {
            this.contextCache = (String) this.context.get();
        }
        return this.contextCache;
    }

    public void reportProblem(String string0) {
        this.problems.put(this.getContext(), string0);
    }

    public ValidationContext forChild(String string0) {
        return new ValidationContext(this.problems, () -> this.getContext() + string0, this.params, this.resolver, this.visitedElements);
    }

    public ValidationContext enterElement(String string0, LootDataId<?> lootDataId1) {
        ImmutableSet<LootDataId<?>> $$2 = ImmutableSet.builder().addAll(this.visitedElements).add(lootDataId1).build();
        return new ValidationContext(this.problems, () -> this.getContext() + string0, this.params, this.resolver, $$2);
    }

    public boolean hasVisitedElement(LootDataId<?> lootDataId0) {
        return this.visitedElements.contains(lootDataId0);
    }

    public Multimap<String, String> getProblems() {
        return ImmutableMultimap.copyOf(this.problems);
    }

    public void validateUser(LootContextUser lootContextUser0) {
        this.params.validateUser(this, lootContextUser0);
    }

    public LootDataResolver resolver() {
        return this.resolver;
    }

    public ValidationContext setParams(LootContextParamSet lootContextParamSet0) {
        return new ValidationContext(this.problems, this.context, lootContextParamSet0, this.resolver, this.visitedElements);
    }
}