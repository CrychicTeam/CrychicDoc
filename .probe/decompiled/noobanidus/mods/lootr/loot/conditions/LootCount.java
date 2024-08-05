package noobanidus.mods.lootr.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.init.ModLoot;

public class LootCount implements LootItemCondition {

    private final List<LootCount.Operation> operations;

    public LootCount(List<LootCount.Operation> operations) {
        this.operations = operations;
    }

    @Override
    public LootItemConditionType getType() {
        return ModLoot.LOOT_COUNT.get();
    }

    public boolean test(LootContext lootContext) {
        Vec3 incomingPos = lootContext.getParamOrNull(LootContextParams.ORIGIN);
        if (incomingPos == null) {
            return false;
        } else {
            BlockPos position = new BlockPos((int) incomingPos.x, (int) incomingPos.y, (int) incomingPos.z);
            BlockEntity tileentity = lootContext.getLevel().m_7702_(position);
            if (tileentity instanceof ILootBlockEntity) {
                int count = ((ILootBlockEntity) tileentity).getOpeners().size() + 1;
                for (LootCount.Operation op : this.operations) {
                    if (!op.test(count)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ORIGIN);
    }

    public static enum Operand implements BiPredicate<Integer, Integer> {

        EQUALS(Integer::equals, 0),
        NOT_EQUALS((a, b) -> !a.equals(b), 0),
        LESS_THAN((a, b) -> a < b, 1),
        GREATER_THAN((a, b) -> a > b, 1),
        LESS_THAN_EQUALS((a, b) -> a <= b, 1),
        GREATER_THAN_EQUALS((a, b) -> a >= b, 1);

        private final BiPredicate<Integer, Integer> predicate;

        private final int precedence;

        private Operand(BiPredicate<Integer, Integer> predicate, int precedence) {
            this.predicate = predicate;
            this.precedence = precedence;
        }

        @Nullable
        public static LootCount.Operand fromString(String name) {
            name = name.toUpperCase(Locale.ROOT);
            for (LootCount.Operand o : values()) {
                if (name.equals(o.name())) {
                    return o;
                }
            }
            return null;
        }

        public boolean test(Integer integer, Integer integer2) {
            return this.predicate.test(integer, integer2);
        }

        public int getPrecedence() {
            return this.precedence;
        }
    }

    public static class Operation implements Predicate<Integer> {

        private final LootCount.Operand operand;

        private final int value;

        public Operation(LootCount.Operand operand, int value) {
            this.operand = operand;
            this.value = value;
        }

        public static LootCount.Operation deserialize(JsonObject object) {
            String operand = object.get("type").getAsString();
            LootCount.Operand op = LootCount.Operand.fromString(operand);
            if (op == null) {
                throw new IllegalArgumentException("invalid operand for operation: " + operand);
            } else {
                return new LootCount.Operation(op, object.get("value").getAsInt());
            }
        }

        public int getPrecedence() {
            return this.operand.getPrecedence();
        }

        public boolean test(Integer integer) {
            return this.operand.test(integer, this.value);
        }

        public JsonObject serialize() {
            JsonObject result = new JsonObject();
            result.addProperty("type", this.operand.name().toLowerCase(Locale.ROOT));
            result.addProperty("value", this.value);
            return result;
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootCount> {

        public void serialize(JsonObject object, LootCount count, JsonSerializationContext context) {
            JsonArray operations = new JsonArray();
            for (LootCount.Operation op : count.operations) {
                operations.add(op.serialize());
            }
            object.add("operations", operations);
        }

        public LootCount deserialize(JsonObject object, JsonDeserializationContext context) {
            JsonArray objects = object.get("operations").getAsJsonArray();
            List<LootCount.Operation> operations = new ArrayList();
            for (JsonElement element : objects) {
                if (!element.isJsonObject()) {
                    throw new IllegalArgumentException("invalid operand for LootCount: " + element.toString());
                }
                operations.add(LootCount.Operation.deserialize(element.getAsJsonObject()));
            }
            operations.sort(Comparator.comparingInt(LootCount.Operation::getPrecedence));
            return new LootCount(operations);
        }
    }
}