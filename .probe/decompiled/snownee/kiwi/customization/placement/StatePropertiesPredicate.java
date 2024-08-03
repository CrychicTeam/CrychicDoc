package snownee.kiwi.customization.placement;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import snownee.kiwi.customization.block.KBlockUtils;
import snownee.kiwi.util.codec.CustomizationCodecs;

public record StatePropertiesPredicate(List<StatePropertiesPredicate.PropertyMatcher> properties) implements Predicate<BlockState> {

    public static final Codec<StatePropertiesPredicate> CODEC = Codec.compoundList(Codec.STRING, Codec.either(CustomizationCodecs.compactList(Codec.STRING), CustomizationCodecs.INT_BOUNDS)).xmap($ -> new StatePropertiesPredicate($.stream().map(pair -> {
        Optional<List<String>> strValues = ((Either) pair.getSecond()).left();
        return (StatePropertiesPredicate.PropertyMatcher) strValues.map(strings -> new StatePropertiesPredicate.PropertyMatcher((String) pair.getFirst(), Either.left(Set.copyOf(strings)))).orElseGet(() -> new StatePropertiesPredicate.PropertyMatcher((String) pair.getFirst(), Either.right((MinMaxBounds.Ints) ((Either) pair.getSecond()).right().orElseThrow())));
    }).toList()), $ -> $.properties.stream().map(matcher -> Pair.of(matcher.key, matcher.value.mapLeft(List::copyOf))).toList());

    public boolean test(BlockState blockState) {
        for (StatePropertiesPredicate.PropertyMatcher matcher : this.properties) {
            if (!matcher.test(blockState)) {
                return false;
            }
        }
        return true;
    }

    public boolean smartTest(BlockState baseState, BlockState targetState) {
        for (StatePropertiesPredicate.PropertyMatcher matcher : this.properties) {
            if (!matcher.smartTest(baseState, targetState)) {
                return false;
            }
        }
        return true;
    }

    public static record PropertyMatcher(String key, Either<Set<String>, MinMaxBounds.Ints> value) {

        public boolean test(BlockState blockState) {
            Property<?> property = KBlockUtils.getProperty(blockState, this.key);
            Optional<Set<String>> strValues = this.value.left();
            boolean isInteger = property.getValueClass() == Integer.class;
            if (strValues.isEmpty() && !isInteger) {
                throw new IllegalStateException("Property value type mismatch");
            } else {
                return strValues.isEmpty() ? ((MinMaxBounds.Ints) this.value.right().orElseThrow()).matches((Integer) blockState.m_61143_(property)) : ((Set) strValues.get()).contains(KBlockUtils.getValueString(blockState, this.key));
            }
        }

        public boolean smartTest(BlockState baseState, BlockState targetState) {
            Property<?> property = KBlockUtils.getProperty(targetState, this.key);
            Optional<Set<String>> strValues = this.value.left();
            boolean isInteger = property.getValueClass() == Integer.class;
            if (strValues.isEmpty() && !isInteger) {
                throw new IllegalStateException("Property value type mismatch");
            } else if (strValues.isEmpty()) {
                return ((MinMaxBounds.Ints) this.value.right().orElseThrow()).matches((Integer) targetState.m_61143_(property));
            } else {
                String targetValue = KBlockUtils.getValueString(targetState, this.key);
                for (String s : (Set) strValues.get()) {
                    if (s.startsWith("@")) {
                        String baseValue = KBlockUtils.getValueString(baseState, s.substring(1));
                        if (baseValue.equals(targetValue)) {
                            return true;
                        }
                    } else if (s.equals(targetValue)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }
}