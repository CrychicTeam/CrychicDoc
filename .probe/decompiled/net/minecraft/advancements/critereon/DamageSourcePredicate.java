package net.minecraft.advancements.critereon;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.phys.Vec3;

public class DamageSourcePredicate {

    public static final DamageSourcePredicate ANY = DamageSourcePredicate.Builder.damageType().build();

    private final List<TagPredicate<DamageType>> tags;

    private final EntityPredicate directEntity;

    private final EntityPredicate sourceEntity;

    public DamageSourcePredicate(List<TagPredicate<DamageType>> listTagPredicateDamageType0, EntityPredicate entityPredicate1, EntityPredicate entityPredicate2) {
        this.tags = listTagPredicateDamageType0;
        this.directEntity = entityPredicate1;
        this.sourceEntity = entityPredicate2;
    }

    public boolean matches(ServerPlayer serverPlayer0, DamageSource damageSource1) {
        return this.matches(serverPlayer0.serverLevel(), serverPlayer0.m_20182_(), damageSource1);
    }

    public boolean matches(ServerLevel serverLevel0, Vec3 vec1, DamageSource damageSource2) {
        if (this == ANY) {
            return true;
        } else {
            for (TagPredicate<DamageType> $$3 : this.tags) {
                if (!$$3.matches(damageSource2.typeHolder())) {
                    return false;
                }
            }
            return !this.directEntity.matches(serverLevel0, vec1, damageSource2.getDirectEntity()) ? false : this.sourceEntity.matches(serverLevel0, vec1, damageSource2.getEntity());
        }
    }

    public static DamageSourcePredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "damage type");
            JsonArray $$2 = GsonHelper.getAsJsonArray($$1, "tags", null);
            List<TagPredicate<DamageType>> $$3;
            if ($$2 != null) {
                $$3 = new ArrayList($$2.size());
                for (JsonElement $$4 : $$2) {
                    $$3.add(TagPredicate.fromJson($$4, Registries.DAMAGE_TYPE));
                }
            } else {
                $$3 = List.of();
            }
            EntityPredicate $$6 = EntityPredicate.fromJson($$1.get("direct_entity"));
            EntityPredicate $$7 = EntityPredicate.fromJson($$1.get("source_entity"));
            return new DamageSourcePredicate($$3, $$6, $$7);
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            if (!this.tags.isEmpty()) {
                JsonArray $$1 = new JsonArray(this.tags.size());
                for (int $$2 = 0; $$2 < this.tags.size(); $$2++) {
                    $$1.add(((TagPredicate) this.tags.get($$2)).serializeToJson());
                }
                $$0.add("tags", $$1);
            }
            $$0.add("direct_entity", this.directEntity.serializeToJson());
            $$0.add("source_entity", this.sourceEntity.serializeToJson());
            return $$0;
        }
    }

    public static class Builder {

        private final com.google.common.collect.ImmutableList.Builder<TagPredicate<DamageType>> tags = ImmutableList.builder();

        private EntityPredicate directEntity = EntityPredicate.ANY;

        private EntityPredicate sourceEntity = EntityPredicate.ANY;

        public static DamageSourcePredicate.Builder damageType() {
            return new DamageSourcePredicate.Builder();
        }

        public DamageSourcePredicate.Builder tag(TagPredicate<DamageType> tagPredicateDamageType0) {
            this.tags.add(tagPredicateDamageType0);
            return this;
        }

        public DamageSourcePredicate.Builder direct(EntityPredicate entityPredicate0) {
            this.directEntity = entityPredicate0;
            return this;
        }

        public DamageSourcePredicate.Builder direct(EntityPredicate.Builder entityPredicateBuilder0) {
            this.directEntity = entityPredicateBuilder0.build();
            return this;
        }

        public DamageSourcePredicate.Builder source(EntityPredicate entityPredicate0) {
            this.sourceEntity = entityPredicate0;
            return this;
        }

        public DamageSourcePredicate.Builder source(EntityPredicate.Builder entityPredicateBuilder0) {
            this.sourceEntity = entityPredicateBuilder0.build();
            return this;
        }

        public DamageSourcePredicate build() {
            return new DamageSourcePredicate(this.tags.build(), this.directEntity, this.sourceEntity);
        }
    }
}