package malte0811.ferritecore.impl;

import com.google.common.base.Splitter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import malte0811.ferritecore.util.PredicateHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.tuple.Pair;

public class KeyValueConditionImpl {

    private static final Map<Pair<Property<?>, Comparable<?>>, Predicate<BlockState>> STATE_HAS_PROPERTY_CACHE = new ConcurrentHashMap();

    public static Predicate<BlockState> getPredicate(StateDefinition<Block, BlockState> stateContainer, String key, String value, Splitter splitter) {
        Property<?> property = stateContainer.getProperty(key);
        if (property == null) {
            throw new RuntimeException(String.format("Unknown property '%s' on '%s'", key, stateContainer.getOwner().toString()));
        } else {
            String valueNoInvert = value;
            boolean invert = !value.isEmpty() && value.charAt(0) == '!';
            if (invert) {
                valueNoInvert = value.substring(1);
            }
            List<String> matchedStates = splitter.splitToList(valueNoInvert);
            if (matchedStates.isEmpty()) {
                throw new RuntimeException(String.format("Empty value '%s' for property '%s' on '%s'", value, key, stateContainer.getOwner().toString()));
            } else {
                Predicate<BlockState> isMatchedState;
                if (matchedStates.size() == 1) {
                    isMatchedState = getBlockStatePredicate(stateContainer, property, valueNoInvert, key, value);
                } else {
                    List<Predicate<BlockState>> subPredicates = (List<Predicate<BlockState>>) matchedStates.stream().map(subValue -> getBlockStatePredicate(stateContainer, property, subValue, key, value)).collect(Collectors.toCollection(ArrayList::new));
                    PredicateHelper.canonize(subPredicates);
                    isMatchedState = Deduplicator.or(subPredicates);
                }
                return invert ? isMatchedState.negate() : isMatchedState;
            }
        }
    }

    private static <T extends Comparable<T>> Predicate<BlockState> getBlockStatePredicate(StateDefinition<Block, BlockState> container, Property<T> property, String subValue, String key, String value) {
        Optional<T> optional = property.getValue(subValue);
        if (optional.isEmpty()) {
            throw new RuntimeException(String.format("Unknown value '%s' for property '%s' on '%s' in '%s'", subValue, key, container.getOwner().toString(), value));
        } else {
            T unwrapped = (T) optional.get();
            return (Predicate<BlockState>) STATE_HAS_PROPERTY_CACHE.computeIfAbsent(Pair.of(property, unwrapped), pair -> {
                Comparable<?> valueInt = (Comparable<?>) pair.getRight();
                Property<?> propInt = (Property<?>) pair.getLeft();
                return state -> state.m_61143_(propInt).equals(valueInt);
            });
        }
    }
}