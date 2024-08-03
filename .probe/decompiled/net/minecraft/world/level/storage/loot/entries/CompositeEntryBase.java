package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.function.Consumer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class CompositeEntryBase extends LootPoolEntryContainer {

    protected final LootPoolEntryContainer[] children;

    private final ComposableEntryContainer composedChildren;

    protected CompositeEntryBase(LootPoolEntryContainer[] lootPoolEntryContainer0, LootItemCondition[] lootItemCondition1) {
        super(lootItemCondition1);
        this.children = lootPoolEntryContainer0;
        this.composedChildren = this.compose(lootPoolEntryContainer0);
    }

    @Override
    public void validate(ValidationContext validationContext0) {
        super.validate(validationContext0);
        if (this.children.length == 0) {
            validationContext0.reportProblem("Empty children list");
        }
        for (int $$1 = 0; $$1 < this.children.length; $$1++) {
            this.children[$$1].validate(validationContext0.forChild(".entry[" + $$1 + "]"));
        }
    }

    protected abstract ComposableEntryContainer compose(ComposableEntryContainer[] var1);

    @Override
    public final boolean expand(LootContext lootContext0, Consumer<LootPoolEntry> consumerLootPoolEntry1) {
        return !this.m_79639_(lootContext0) ? false : this.composedChildren.expand(lootContext0, consumerLootPoolEntry1);
    }

    public static <T extends CompositeEntryBase> LootPoolEntryContainer.Serializer<T> createSerializer(final CompositeEntryBase.CompositeEntryConstructor<T> compositeEntryBaseCompositeEntryConstructorT0) {
        return new LootPoolEntryContainer.Serializer<T>() {

            public void serializeCustom(JsonObject p_79449_, T p_79450_, JsonSerializationContext p_79451_) {
                p_79449_.add("children", p_79451_.serialize(p_79450_.children));
            }

            public final T deserializeCustom(JsonObject p_79445_, JsonDeserializationContext p_79446_, LootItemCondition[] p_79447_) {
                LootPoolEntryContainer[] $$3 = GsonHelper.getAsObject(p_79445_, "children", p_79446_, LootPoolEntryContainer[].class);
                return compositeEntryBaseCompositeEntryConstructorT0.create($$3, p_79447_);
            }
        };
    }

    @FunctionalInterface
    public interface CompositeEntryConstructor<T extends CompositeEntryBase> {

        T create(LootPoolEntryContainer[] var1, LootItemCondition[] var2);
    }
}