package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.damagesource.DamageSource;

public class DamagePredicate {

    public static final DamagePredicate ANY = DamagePredicate.Builder.damageInstance().build();

    private final MinMaxBounds.Doubles dealtDamage;

    private final MinMaxBounds.Doubles takenDamage;

    private final EntityPredicate sourceEntity;

    @Nullable
    private final Boolean blocked;

    private final DamageSourcePredicate type;

    public DamagePredicate() {
        this.dealtDamage = MinMaxBounds.Doubles.ANY;
        this.takenDamage = MinMaxBounds.Doubles.ANY;
        this.sourceEntity = EntityPredicate.ANY;
        this.blocked = null;
        this.type = DamageSourcePredicate.ANY;
    }

    public DamagePredicate(MinMaxBounds.Doubles minMaxBoundsDoubles0, MinMaxBounds.Doubles minMaxBoundsDoubles1, EntityPredicate entityPredicate2, @Nullable Boolean boolean3, DamageSourcePredicate damageSourcePredicate4) {
        this.dealtDamage = minMaxBoundsDoubles0;
        this.takenDamage = minMaxBoundsDoubles1;
        this.sourceEntity = entityPredicate2;
        this.blocked = boolean3;
        this.type = damageSourcePredicate4;
    }

    public boolean matches(ServerPlayer serverPlayer0, DamageSource damageSource1, float float2, float float3, boolean boolean4) {
        if (this == ANY) {
            return true;
        } else if (!this.dealtDamage.matches((double) float2)) {
            return false;
        } else if (!this.takenDamage.matches((double) float3)) {
            return false;
        } else if (!this.sourceEntity.matches(serverPlayer0, damageSource1.getEntity())) {
            return false;
        } else {
            return this.blocked != null && this.blocked != boolean4 ? false : this.type.matches(serverPlayer0, damageSource1);
        }
    }

    public static DamagePredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "damage");
            MinMaxBounds.Doubles $$2 = MinMaxBounds.Doubles.fromJson($$1.get("dealt"));
            MinMaxBounds.Doubles $$3 = MinMaxBounds.Doubles.fromJson($$1.get("taken"));
            Boolean $$4 = $$1.has("blocked") ? GsonHelper.getAsBoolean($$1, "blocked") : null;
            EntityPredicate $$5 = EntityPredicate.fromJson($$1.get("source_entity"));
            DamageSourcePredicate $$6 = DamageSourcePredicate.fromJson($$1.get("type"));
            return new DamagePredicate($$2, $$3, $$5, $$4, $$6);
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            $$0.add("dealt", this.dealtDamage.m_55328_());
            $$0.add("taken", this.takenDamage.m_55328_());
            $$0.add("source_entity", this.sourceEntity.serializeToJson());
            $$0.add("type", this.type.serializeToJson());
            if (this.blocked != null) {
                $$0.addProperty("blocked", this.blocked);
            }
            return $$0;
        }
    }

    public static class Builder {

        private MinMaxBounds.Doubles dealtDamage = MinMaxBounds.Doubles.ANY;

        private MinMaxBounds.Doubles takenDamage = MinMaxBounds.Doubles.ANY;

        private EntityPredicate sourceEntity = EntityPredicate.ANY;

        @Nullable
        private Boolean blocked;

        private DamageSourcePredicate type = DamageSourcePredicate.ANY;

        public static DamagePredicate.Builder damageInstance() {
            return new DamagePredicate.Builder();
        }

        public DamagePredicate.Builder dealtDamage(MinMaxBounds.Doubles minMaxBoundsDoubles0) {
            this.dealtDamage = minMaxBoundsDoubles0;
            return this;
        }

        public DamagePredicate.Builder takenDamage(MinMaxBounds.Doubles minMaxBoundsDoubles0) {
            this.takenDamage = minMaxBoundsDoubles0;
            return this;
        }

        public DamagePredicate.Builder sourceEntity(EntityPredicate entityPredicate0) {
            this.sourceEntity = entityPredicate0;
            return this;
        }

        public DamagePredicate.Builder blocked(Boolean boolean0) {
            this.blocked = boolean0;
            return this;
        }

        public DamagePredicate.Builder type(DamageSourcePredicate damageSourcePredicate0) {
            this.type = damageSourcePredicate0;
            return this;
        }

        public DamagePredicate.Builder type(DamageSourcePredicate.Builder damageSourcePredicateBuilder0) {
            this.type = damageSourcePredicateBuilder0.build();
            return this;
        }

        public DamagePredicate build() {
            return new DamagePredicate(this.dealtDamage, this.takenDamage, this.sourceEntity, this.blocked, this.type);
        }
    }
}