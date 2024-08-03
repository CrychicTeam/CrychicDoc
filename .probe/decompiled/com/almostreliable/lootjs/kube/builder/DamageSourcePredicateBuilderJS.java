package com.almostreliable.lootjs.kube.builder;

import com.almostreliable.lootjs.loot.condition.WrappedDamageSourceCondition;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.function.Consumer;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class DamageSourcePredicateBuilderJS implements LootItemCondition.Builder {

    private final DamageSourcePredicate.Builder vanillaBuilder = new DamageSourcePredicate.Builder();

    private String[] sourceNames;

    public DamageSourcePredicateBuilderJS anyType(String... names) {
        this.sourceNames = names;
        return this;
    }

    public DamageSourcePredicateBuilderJS is(ResourceLocation tag) {
        this.vanillaBuilder.tag(TagPredicate.is(TagKey.create(Registries.DAMAGE_TYPE, tag)));
        return this;
    }

    public DamageSourcePredicateBuilderJS isNot(ResourceLocation tag) {
        this.vanillaBuilder.tag(TagPredicate.isNot(TagKey.create(Registries.DAMAGE_TYPE, tag)));
        return this;
    }

    public DamageSourcePredicateBuilderJS matchDirectEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        this.vanillaBuilder.direct(builder.build());
        return this;
    }

    public DamageSourcePredicateBuilderJS matchSourceEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        this.vanillaBuilder.source(builder.build());
        return this;
    }

    @HideFromJS
    public WrappedDamageSourceCondition build() {
        return new WrappedDamageSourceCondition(this.vanillaBuilder.build(), this.sourceNames);
    }
}