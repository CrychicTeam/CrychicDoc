package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;

public class EntityPredicate {

    public static final EntityPredicate ANY = new EntityPredicate(EntityTypePredicate.ANY, DistancePredicate.ANY, LocationPredicate.ANY, LocationPredicate.ANY, MobEffectsPredicate.ANY, NbtPredicate.ANY, EntityFlagsPredicate.ANY, EntityEquipmentPredicate.ANY, EntitySubPredicate.ANY, null);

    private final EntityTypePredicate entityType;

    private final DistancePredicate distanceToPlayer;

    private final LocationPredicate location;

    private final LocationPredicate steppingOnLocation;

    private final MobEffectsPredicate effects;

    private final NbtPredicate nbt;

    private final EntityFlagsPredicate flags;

    private final EntityEquipmentPredicate equipment;

    private final EntitySubPredicate subPredicate;

    private final EntityPredicate vehicle;

    private final EntityPredicate passenger;

    private final EntityPredicate targetedEntity;

    @Nullable
    private final String team;

    private EntityPredicate(EntityTypePredicate entityTypePredicate0, DistancePredicate distancePredicate1, LocationPredicate locationPredicate2, LocationPredicate locationPredicate3, MobEffectsPredicate mobEffectsPredicate4, NbtPredicate nbtPredicate5, EntityFlagsPredicate entityFlagsPredicate6, EntityEquipmentPredicate entityEquipmentPredicate7, EntitySubPredicate entitySubPredicate8, @Nullable String string9) {
        this.entityType = entityTypePredicate0;
        this.distanceToPlayer = distancePredicate1;
        this.location = locationPredicate2;
        this.steppingOnLocation = locationPredicate3;
        this.effects = mobEffectsPredicate4;
        this.nbt = nbtPredicate5;
        this.flags = entityFlagsPredicate6;
        this.equipment = entityEquipmentPredicate7;
        this.subPredicate = entitySubPredicate8;
        this.passenger = this;
        this.vehicle = this;
        this.targetedEntity = this;
        this.team = string9;
    }

    EntityPredicate(EntityTypePredicate entityTypePredicate0, DistancePredicate distancePredicate1, LocationPredicate locationPredicate2, LocationPredicate locationPredicate3, MobEffectsPredicate mobEffectsPredicate4, NbtPredicate nbtPredicate5, EntityFlagsPredicate entityFlagsPredicate6, EntityEquipmentPredicate entityEquipmentPredicate7, EntitySubPredicate entitySubPredicate8, EntityPredicate entityPredicate9, EntityPredicate entityPredicate10, EntityPredicate entityPredicate11, @Nullable String string12) {
        this.entityType = entityTypePredicate0;
        this.distanceToPlayer = distancePredicate1;
        this.location = locationPredicate2;
        this.steppingOnLocation = locationPredicate3;
        this.effects = mobEffectsPredicate4;
        this.nbt = nbtPredicate5;
        this.flags = entityFlagsPredicate6;
        this.equipment = entityEquipmentPredicate7;
        this.subPredicate = entitySubPredicate8;
        this.vehicle = entityPredicate9;
        this.passenger = entityPredicate10;
        this.targetedEntity = entityPredicate11;
        this.team = string12;
    }

    public static ContextAwarePredicate fromJson(JsonObject jsonObject0, String string1, DeserializationContext deserializationContext2) {
        JsonElement $$3 = jsonObject0.get(string1);
        return fromElement(string1, deserializationContext2, $$3);
    }

    public static ContextAwarePredicate[] fromJsonArray(JsonObject jsonObject0, String string1, DeserializationContext deserializationContext2) {
        JsonElement $$3 = jsonObject0.get(string1);
        if ($$3 != null && !$$3.isJsonNull()) {
            JsonArray $$4 = GsonHelper.convertToJsonArray($$3, string1);
            ContextAwarePredicate[] $$5 = new ContextAwarePredicate[$$4.size()];
            for (int $$6 = 0; $$6 < $$4.size(); $$6++) {
                $$5[$$6] = fromElement(string1 + "[" + $$6 + "]", deserializationContext2, $$4.get($$6));
            }
            return $$5;
        } else {
            return new ContextAwarePredicate[0];
        }
    }

    private static ContextAwarePredicate fromElement(String string0, DeserializationContext deserializationContext1, @Nullable JsonElement jsonElement2) {
        ContextAwarePredicate $$3 = ContextAwarePredicate.fromElement(string0, deserializationContext1, jsonElement2, LootContextParamSets.ADVANCEMENT_ENTITY);
        if ($$3 != null) {
            return $$3;
        } else {
            EntityPredicate $$4 = fromJson(jsonElement2);
            return wrap($$4);
        }
    }

    public static ContextAwarePredicate wrap(EntityPredicate entityPredicate0) {
        if (entityPredicate0 == ANY) {
            return ContextAwarePredicate.ANY;
        } else {
            LootItemCondition $$1 = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, entityPredicate0).build();
            return new ContextAwarePredicate(new LootItemCondition[] { $$1 });
        }
    }

    public boolean matches(ServerPlayer serverPlayer0, @Nullable Entity entity1) {
        return this.matches(serverPlayer0.serverLevel(), serverPlayer0.m_20182_(), entity1);
    }

    public boolean matches(ServerLevel serverLevel0, @Nullable Vec3 vec1, @Nullable Entity entity2) {
        if (this == ANY) {
            return true;
        } else if (entity2 == null) {
            return false;
        } else if (!this.entityType.matches(entity2.getType())) {
            return false;
        } else {
            if (vec1 == null) {
                if (this.distanceToPlayer != DistancePredicate.ANY) {
                    return false;
                }
            } else if (!this.distanceToPlayer.matches(vec1.x, vec1.y, vec1.z, entity2.getX(), entity2.getY(), entity2.getZ())) {
                return false;
            }
            if (!this.location.matches(serverLevel0, entity2.getX(), entity2.getY(), entity2.getZ())) {
                return false;
            } else {
                if (this.steppingOnLocation != LocationPredicate.ANY) {
                    Vec3 $$3 = Vec3.atCenterOf(entity2.getOnPos());
                    if (!this.steppingOnLocation.matches(serverLevel0, $$3.x(), $$3.y(), $$3.z())) {
                        return false;
                    }
                }
                if (!this.effects.matches(entity2)) {
                    return false;
                } else if (!this.nbt.matches(entity2)) {
                    return false;
                } else if (!this.flags.matches(entity2)) {
                    return false;
                } else if (!this.equipment.matches(entity2)) {
                    return false;
                } else if (!this.subPredicate.matches(entity2, serverLevel0, vec1)) {
                    return false;
                } else if (!this.vehicle.matches(serverLevel0, vec1, entity2.getVehicle())) {
                    return false;
                } else if (this.passenger != ANY && entity2.getPassengers().stream().noneMatch(p_150322_ -> this.passenger.matches(serverLevel0, vec1, p_150322_))) {
                    return false;
                } else if (!this.targetedEntity.matches(serverLevel0, vec1, entity2 instanceof Mob ? ((Mob) entity2).getTarget() : null)) {
                    return false;
                } else {
                    if (this.team != null) {
                        Team $$4 = entity2.getTeam();
                        if ($$4 == null || !this.team.equals($$4.getName())) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
    }

    public static EntityPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "entity");
            EntityTypePredicate $$2 = EntityTypePredicate.fromJson($$1.get("type"));
            DistancePredicate $$3 = DistancePredicate.fromJson($$1.get("distance"));
            LocationPredicate $$4 = LocationPredicate.fromJson($$1.get("location"));
            LocationPredicate $$5 = LocationPredicate.fromJson($$1.get("stepping_on"));
            MobEffectsPredicate $$6 = MobEffectsPredicate.fromJson($$1.get("effects"));
            NbtPredicate $$7 = NbtPredicate.fromJson($$1.get("nbt"));
            EntityFlagsPredicate $$8 = EntityFlagsPredicate.fromJson($$1.get("flags"));
            EntityEquipmentPredicate $$9 = EntityEquipmentPredicate.fromJson($$1.get("equipment"));
            EntitySubPredicate $$10 = EntitySubPredicate.fromJson($$1.get("type_specific"));
            EntityPredicate $$11 = fromJson($$1.get("vehicle"));
            EntityPredicate $$12 = fromJson($$1.get("passenger"));
            EntityPredicate $$13 = fromJson($$1.get("targeted_entity"));
            String $$14 = GsonHelper.getAsString($$1, "team", null);
            return new EntityPredicate.Builder().entityType($$2).distance($$3).located($$4).steppingOn($$5).effects($$6).nbt($$7).flags($$8).equipment($$9).subPredicate($$10).team($$14).vehicle($$11).passenger($$12).targetedEntity($$13).build();
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            $$0.add("type", this.entityType.serializeToJson());
            $$0.add("distance", this.distanceToPlayer.serializeToJson());
            $$0.add("location", this.location.serializeToJson());
            $$0.add("stepping_on", this.steppingOnLocation.serializeToJson());
            $$0.add("effects", this.effects.serializeToJson());
            $$0.add("nbt", this.nbt.serializeToJson());
            $$0.add("flags", this.flags.serializeToJson());
            $$0.add("equipment", this.equipment.serializeToJson());
            $$0.add("type_specific", this.subPredicate.serialize());
            $$0.add("vehicle", this.vehicle.serializeToJson());
            $$0.add("passenger", this.passenger.serializeToJson());
            $$0.add("targeted_entity", this.targetedEntity.serializeToJson());
            $$0.addProperty("team", this.team);
            return $$0;
        }
    }

    public static LootContext createContext(ServerPlayer serverPlayer0, Entity entity1) {
        LootParams $$2 = new LootParams.Builder(serverPlayer0.serverLevel()).withParameter(LootContextParams.THIS_ENTITY, entity1).withParameter(LootContextParams.ORIGIN, serverPlayer0.m_20182_()).create(LootContextParamSets.ADVANCEMENT_ENTITY);
        return new LootContext.Builder($$2).create(null);
    }

    public static class Builder {

        private EntityTypePredicate entityType = EntityTypePredicate.ANY;

        private DistancePredicate distanceToPlayer = DistancePredicate.ANY;

        private LocationPredicate location = LocationPredicate.ANY;

        private LocationPredicate steppingOnLocation = LocationPredicate.ANY;

        private MobEffectsPredicate effects = MobEffectsPredicate.ANY;

        private NbtPredicate nbt = NbtPredicate.ANY;

        private EntityFlagsPredicate flags = EntityFlagsPredicate.ANY;

        private EntityEquipmentPredicate equipment = EntityEquipmentPredicate.ANY;

        private EntitySubPredicate subPredicate = EntitySubPredicate.ANY;

        private EntityPredicate vehicle = EntityPredicate.ANY;

        private EntityPredicate passenger = EntityPredicate.ANY;

        private EntityPredicate targetedEntity = EntityPredicate.ANY;

        @Nullable
        private String team;

        public static EntityPredicate.Builder entity() {
            return new EntityPredicate.Builder();
        }

        public EntityPredicate.Builder of(EntityType<?> entityType0) {
            this.entityType = EntityTypePredicate.of(entityType0);
            return this;
        }

        public EntityPredicate.Builder of(TagKey<EntityType<?>> tagKeyEntityType0) {
            this.entityType = EntityTypePredicate.of(tagKeyEntityType0);
            return this;
        }

        public EntityPredicate.Builder entityType(EntityTypePredicate entityTypePredicate0) {
            this.entityType = entityTypePredicate0;
            return this;
        }

        public EntityPredicate.Builder distance(DistancePredicate distancePredicate0) {
            this.distanceToPlayer = distancePredicate0;
            return this;
        }

        public EntityPredicate.Builder located(LocationPredicate locationPredicate0) {
            this.location = locationPredicate0;
            return this;
        }

        public EntityPredicate.Builder steppingOn(LocationPredicate locationPredicate0) {
            this.steppingOnLocation = locationPredicate0;
            return this;
        }

        public EntityPredicate.Builder effects(MobEffectsPredicate mobEffectsPredicate0) {
            this.effects = mobEffectsPredicate0;
            return this;
        }

        public EntityPredicate.Builder nbt(NbtPredicate nbtPredicate0) {
            this.nbt = nbtPredicate0;
            return this;
        }

        public EntityPredicate.Builder flags(EntityFlagsPredicate entityFlagsPredicate0) {
            this.flags = entityFlagsPredicate0;
            return this;
        }

        public EntityPredicate.Builder equipment(EntityEquipmentPredicate entityEquipmentPredicate0) {
            this.equipment = entityEquipmentPredicate0;
            return this;
        }

        public EntityPredicate.Builder subPredicate(EntitySubPredicate entitySubPredicate0) {
            this.subPredicate = entitySubPredicate0;
            return this;
        }

        public EntityPredicate.Builder vehicle(EntityPredicate entityPredicate0) {
            this.vehicle = entityPredicate0;
            return this;
        }

        public EntityPredicate.Builder passenger(EntityPredicate entityPredicate0) {
            this.passenger = entityPredicate0;
            return this;
        }

        public EntityPredicate.Builder targetedEntity(EntityPredicate entityPredicate0) {
            this.targetedEntity = entityPredicate0;
            return this;
        }

        public EntityPredicate.Builder team(@Nullable String string0) {
            this.team = string0;
            return this;
        }

        public EntityPredicate build() {
            return new EntityPredicate(this.entityType, this.distanceToPlayer, this.location, this.steppingOnLocation, this.effects, this.nbt, this.flags, this.equipment, this.subPredicate, this.vehicle, this.passenger, this.targetedEntity, this.team);
        }
    }
}