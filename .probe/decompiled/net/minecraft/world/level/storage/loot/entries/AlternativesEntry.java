package net.minecraft.world.level.storage.loot.entries;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.apache.commons.lang3.ArrayUtils;

public class AlternativesEntry extends CompositeEntryBase {

    AlternativesEntry(LootPoolEntryContainer[] lootPoolEntryContainer0, LootItemCondition[] lootItemCondition1) {
        super(lootPoolEntryContainer0, lootItemCondition1);
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.ALTERNATIVES;
    }

    @Override
    protected ComposableEntryContainer compose(ComposableEntryContainer[] composableEntryContainer0) {
        switch(composableEntryContainer0.length) {
            case 0:
                return f_79405_;
            case 1:
                return composableEntryContainer0[0];
            case 2:
                return composableEntryContainer0[0].or(composableEntryContainer0[1]);
            default:
                return (p_79393_, p_79394_) -> {
                    for (ComposableEntryContainer $$3 : composableEntryContainer0) {
                        if ($$3.expand(p_79393_, p_79394_)) {
                            return true;
                        }
                    }
                    return false;
                };
        }
    }

    @Override
    public void validate(ValidationContext validationContext0) {
        super.validate(validationContext0);
        for (int $$1 = 0; $$1 < this.f_79428_.length - 1; $$1++) {
            if (ArrayUtils.isEmpty(this.f_79428_[$$1].conditions)) {
                validationContext0.reportProblem("Unreachable entry!");
            }
        }
    }

    public static AlternativesEntry.Builder alternatives(LootPoolEntryContainer.Builder<?>... lootPoolEntryContainerBuilder0) {
        return new AlternativesEntry.Builder(lootPoolEntryContainerBuilder0);
    }

    public static <E> AlternativesEntry.Builder alternatives(Collection<E> collectionE0, Function<E, LootPoolEntryContainer.Builder<?>> functionELootPoolEntryContainerBuilder1) {
        return new AlternativesEntry.Builder((LootPoolEntryContainer.Builder<?>[]) collectionE0.stream().map(functionELootPoolEntryContainerBuilder1::apply).toArray(LootPoolEntryContainer.Builder[]::new));
    }

    public static class Builder extends LootPoolEntryContainer.Builder<AlternativesEntry.Builder> {

        private final List<LootPoolEntryContainer> entries = Lists.newArrayList();

        public Builder(LootPoolEntryContainer.Builder<?>... lootPoolEntryContainerBuilder0) {
            for (LootPoolEntryContainer.Builder<?> $$1 : lootPoolEntryContainerBuilder0) {
                this.entries.add($$1.build());
            }
        }

        protected AlternativesEntry.Builder getThis() {
            return this;
        }

        @Override
        public AlternativesEntry.Builder otherwise(LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder0) {
            this.entries.add(lootPoolEntryContainerBuilder0.build());
            return this;
        }

        @Override
        public LootPoolEntryContainer build() {
            return new AlternativesEntry((LootPoolEntryContainer[]) this.entries.toArray(new LootPoolEntryContainer[0]), this.m_79651_());
        }
    }
}