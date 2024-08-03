package net.minecraft.world.level.storage.loot.entries;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class EntryGroup extends CompositeEntryBase {

    EntryGroup(LootPoolEntryContainer[] lootPoolEntryContainer0, LootItemCondition[] lootItemCondition1) {
        super(lootPoolEntryContainer0, lootItemCondition1);
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.GROUP;
    }

    @Override
    protected ComposableEntryContainer compose(ComposableEntryContainer[] composableEntryContainer0) {
        switch(composableEntryContainer0.length) {
            case 0:
                return f_79406_;
            case 1:
                return composableEntryContainer0[0];
            case 2:
                ComposableEntryContainer $$1 = composableEntryContainer0[0];
                ComposableEntryContainer $$2 = composableEntryContainer0[1];
                return (p_79556_, p_79557_) -> {
                    $$1.expand(p_79556_, p_79557_);
                    $$2.expand(p_79556_, p_79557_);
                    return true;
                };
            default:
                return (p_79562_, p_79563_) -> {
                    for (ComposableEntryContainer $$3 : composableEntryContainer0) {
                        $$3.expand(p_79562_, p_79563_);
                    }
                    return true;
                };
        }
    }

    public static EntryGroup.Builder list(LootPoolEntryContainer.Builder<?>... lootPoolEntryContainerBuilder0) {
        return new EntryGroup.Builder(lootPoolEntryContainerBuilder0);
    }

    public static class Builder extends LootPoolEntryContainer.Builder<EntryGroup.Builder> {

        private final List<LootPoolEntryContainer> entries = Lists.newArrayList();

        public Builder(LootPoolEntryContainer.Builder<?>... lootPoolEntryContainerBuilder0) {
            for (LootPoolEntryContainer.Builder<?> $$1 : lootPoolEntryContainerBuilder0) {
                this.entries.add($$1.build());
            }
        }

        protected EntryGroup.Builder getThis() {
            return this;
        }

        @Override
        public EntryGroup.Builder append(LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder0) {
            this.entries.add(lootPoolEntryContainerBuilder0.build());
            return this;
        }

        @Override
        public LootPoolEntryContainer build() {
            return new EntryGroup((LootPoolEntryContainer[]) this.entries.toArray(new LootPoolEntryContainer[0]), this.m_79651_());
        }
    }
}