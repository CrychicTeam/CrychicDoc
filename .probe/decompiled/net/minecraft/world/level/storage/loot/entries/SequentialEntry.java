package net.minecraft.world.level.storage.loot.entries;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SequentialEntry extends CompositeEntryBase {

    SequentialEntry(LootPoolEntryContainer[] lootPoolEntryContainer0, LootItemCondition[] lootItemCondition1) {
        super(lootPoolEntryContainer0, lootItemCondition1);
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.SEQUENCE;
    }

    @Override
    protected ComposableEntryContainer compose(ComposableEntryContainer[] composableEntryContainer0) {
        switch(composableEntryContainer0.length) {
            case 0:
                return f_79406_;
            case 1:
                return composableEntryContainer0[0];
            case 2:
                return composableEntryContainer0[0].and(composableEntryContainer0[1]);
            default:
                return (p_79819_, p_79820_) -> {
                    for (ComposableEntryContainer $$3 : composableEntryContainer0) {
                        if (!$$3.expand(p_79819_, p_79820_)) {
                            return false;
                        }
                    }
                    return true;
                };
        }
    }

    public static SequentialEntry.Builder sequential(LootPoolEntryContainer.Builder<?>... lootPoolEntryContainerBuilder0) {
        return new SequentialEntry.Builder(lootPoolEntryContainerBuilder0);
    }

    public static class Builder extends LootPoolEntryContainer.Builder<SequentialEntry.Builder> {

        private final List<LootPoolEntryContainer> entries = Lists.newArrayList();

        public Builder(LootPoolEntryContainer.Builder<?>... lootPoolEntryContainerBuilder0) {
            for (LootPoolEntryContainer.Builder<?> $$1 : lootPoolEntryContainerBuilder0) {
                this.entries.add($$1.build());
            }
        }

        protected SequentialEntry.Builder getThis() {
            return this;
        }

        @Override
        public SequentialEntry.Builder then(LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder0) {
            this.entries.add(lootPoolEntryContainerBuilder0.build());
            return this;
        }

        @Override
        public LootPoolEntryContainer build() {
            return new SequentialEntry((LootPoolEntryContainer[]) this.entries.toArray(new LootPoolEntryContainer[0]), this.m_79651_());
        }
    }
}