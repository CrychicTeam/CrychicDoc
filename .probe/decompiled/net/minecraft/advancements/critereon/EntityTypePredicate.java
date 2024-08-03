package net.minecraft.advancements.critereon;

import com.google.common.base.Joiner;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;

public abstract class EntityTypePredicate {

    public static final EntityTypePredicate ANY = new EntityTypePredicate() {

        @Override
        public boolean matches(EntityType<?> p_37652_) {
            return true;
        }

        @Override
        public JsonElement serializeToJson() {
            return JsonNull.INSTANCE;
        }
    };

    private static final Joiner COMMA_JOINER = Joiner.on(", ");

    public abstract boolean matches(EntityType<?> var1);

    public abstract JsonElement serializeToJson();

    public static EntityTypePredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            String $$1 = GsonHelper.convertToString(jsonElement0, "type");
            if ($$1.startsWith("#")) {
                ResourceLocation $$2 = new ResourceLocation($$1.substring(1));
                return new EntityTypePredicate.TagPredicate(TagKey.create(Registries.ENTITY_TYPE, $$2));
            } else {
                ResourceLocation $$3 = new ResourceLocation($$1);
                EntityType<?> $$4 = (EntityType<?>) BuiltInRegistries.ENTITY_TYPE.m_6612_($$3).orElseThrow(() -> new JsonSyntaxException("Unknown entity type '" + $$3 + "', valid types are: " + COMMA_JOINER.join(BuiltInRegistries.ENTITY_TYPE.m_6566_())));
                return new EntityTypePredicate.TypePredicate($$4);
            }
        } else {
            return ANY;
        }
    }

    public static EntityTypePredicate of(EntityType<?> entityType0) {
        return new EntityTypePredicate.TypePredicate(entityType0);
    }

    public static EntityTypePredicate of(TagKey<EntityType<?>> tagKeyEntityType0) {
        return new EntityTypePredicate.TagPredicate(tagKeyEntityType0);
    }

    static class TagPredicate extends EntityTypePredicate {

        private final TagKey<EntityType<?>> tag;

        public TagPredicate(TagKey<EntityType<?>> tagKeyEntityType0) {
            this.tag = tagKeyEntityType0;
        }

        @Override
        public boolean matches(EntityType<?> entityType0) {
            return entityType0.is(this.tag);
        }

        @Override
        public JsonElement serializeToJson() {
            return new JsonPrimitive("#" + this.tag.location());
        }
    }

    static class TypePredicate extends EntityTypePredicate {

        private final EntityType<?> type;

        public TypePredicate(EntityType<?> entityType0) {
            this.type = entityType0;
        }

        @Override
        public boolean matches(EntityType<?> entityType0) {
            return this.type == entityType0;
        }

        @Override
        public JsonElement serializeToJson() {
            return new JsonPrimitive(BuiltInRegistries.ENTITY_TYPE.getKey(this.type).toString());
        }
    }
}