package se.mickelus.tetra.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import se.mickelus.mutil.util.JsonOptional;
import se.mickelus.tetra.blocks.PropertyMatcher;

public class BlockUseCriterion extends AbstractCriterionTriggerInstance {

    public static final GenericTrigger<BlockUseCriterion> trigger = new GenericTrigger<>("tetra:block_use", BlockUseCriterion::deserialize);

    private final PropertyMatcher before;

    private final PropertyMatcher after;

    private final ItemPredicate item;

    private final Map<String, String> data;

    public BlockUseCriterion(ContextAwarePredicate playerCondition, PropertyMatcher before, PropertyMatcher after, ItemPredicate item, Map<String, String> data) {
        super(trigger.getId(), playerCondition);
        this.before = before;
        this.after = after;
        this.item = item;
        this.data = data;
    }

    public static void trigger(ServerPlayer player, BlockState state, ItemStack usedItem, Map<String, String> data) {
        trigger.fulfillCriterion(player, criterion -> criterion.test(state, usedItem, data));
    }

    public static void trigger(ServerPlayer player, BlockState state, ItemStack usedItem) {
        trigger(player, state, usedItem, Collections.emptyMap());
    }

    private static BlockUseCriterion deserialize(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        return new BlockUseCriterion(entityPredicate, (PropertyMatcher) JsonOptional.field(json, "before").map(PropertyMatcher::deserialize).orElse(null), (PropertyMatcher) JsonOptional.field(json, "after").map(PropertyMatcher::deserialize).orElse(null), (ItemPredicate) JsonOptional.field(json, "item").map(ItemPredicate::m_45051_).orElse(null), (Map<String, String>) ((Stream) JsonOptional.field(json, "data").map(JsonElement::getAsJsonObject).map(JsonObject::entrySet).map(Collection::stream).orElseGet(Stream::empty)).collect(Collectors.toMap(Entry::getKey, entry -> ((JsonElement) entry.getValue()).getAsString())));
    }

    public boolean test(BlockState state, ItemStack usedItem, Map<String, String> data) {
        if (this.before != null && !this.before.test(state)) {
            return false;
        } else if (this.after != null && !this.after.test(state)) {
            return false;
        } else if (this.item != null && !this.item.matches(usedItem)) {
            return false;
        } else if (this.data != null) {
            boolean hasUnmatched = this.data.entrySet().stream().anyMatch(entry -> !data.containsKey(entry.getKey()) || !((String) entry.getValue()).equals(data.get(entry.getKey())));
            return !hasUnmatched;
        } else {
            return true;
        }
    }
}