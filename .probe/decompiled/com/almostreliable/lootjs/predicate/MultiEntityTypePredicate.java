package com.almostreliable.lootjs.predicate;

import com.google.gson.JsonElement;
import java.util.List;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class MultiEntityTypePredicate extends EntityTypePredicate {

    private final List<EntityType<?>> types;

    private final List<TagKey<EntityType<?>>> tags;

    public MultiEntityTypePredicate(List<TagKey<EntityType<?>>> tags, List<EntityType<?>> types) {
        this.tags = tags;
        this.types = types;
    }

    @Override
    public boolean matches(EntityType<?> typeToCheck) {
        for (EntityType<?> type : this.types) {
            if (type == typeToCheck) {
                return true;
            }
        }
        for (TagKey<EntityType<?>> tag : this.tags) {
            if (typeToCheck.is(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public JsonElement serializeToJson() {
        throw new UnsupportedOperationException("Not supported for custom predicates from LootJS mod");
    }
}