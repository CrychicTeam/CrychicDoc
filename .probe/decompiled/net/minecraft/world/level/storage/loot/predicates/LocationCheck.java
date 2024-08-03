package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class LocationCheck implements LootItemCondition {

    final LocationPredicate predicate;

    final BlockPos offset;

    LocationCheck(LocationPredicate locationPredicate0, BlockPos blockPos1) {
        this.predicate = locationPredicate0;
        this.offset = blockPos1;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.LOCATION_CHECK;
    }

    public boolean test(LootContext lootContext0) {
        Vec3 $$1 = lootContext0.getParamOrNull(LootContextParams.ORIGIN);
        return $$1 != null && this.predicate.matches(lootContext0.getLevel(), $$1.x() + (double) this.offset.m_123341_(), $$1.y() + (double) this.offset.m_123342_(), $$1.z() + (double) this.offset.m_123343_());
    }

    public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder locationPredicateBuilder0) {
        return () -> new LocationCheck(locationPredicateBuilder0.build(), BlockPos.ZERO);
    }

    public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder locationPredicateBuilder0, BlockPos blockPos1) {
        return () -> new LocationCheck(locationPredicateBuilder0.build(), blockPos1);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LocationCheck> {

        public void serialize(JsonObject jsonObject0, LocationCheck locationCheck1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.add("predicate", locationCheck1.predicate.serializeToJson());
            if (locationCheck1.offset.m_123341_() != 0) {
                jsonObject0.addProperty("offsetX", locationCheck1.offset.m_123341_());
            }
            if (locationCheck1.offset.m_123342_() != 0) {
                jsonObject0.addProperty("offsetY", locationCheck1.offset.m_123342_());
            }
            if (locationCheck1.offset.m_123343_() != 0) {
                jsonObject0.addProperty("offsetZ", locationCheck1.offset.m_123343_());
            }
        }

        public LocationCheck deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            LocationPredicate $$2 = LocationPredicate.fromJson(jsonObject0.get("predicate"));
            int $$3 = GsonHelper.getAsInt(jsonObject0, "offsetX", 0);
            int $$4 = GsonHelper.getAsInt(jsonObject0, "offsetY", 0);
            int $$5 = GsonHelper.getAsInt(jsonObject0, "offsetZ", 0);
            return new LocationCheck($$2, new BlockPos($$3, $$4, $$5));
        }
    }
}