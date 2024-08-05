package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

public class KeyValueCondition implements Condition {

    private static final Splitter PIPE_SPLITTER = Splitter.on('|').omitEmptyStrings();

    private final String key;

    private final String value;

    public KeyValueCondition(String string0, String string1) {
        this.key = string0;
        this.value = string1;
    }

    @Override
    public Predicate<BlockState> getPredicate(StateDefinition<Block, BlockState> stateDefinitionBlockBlockState0) {
        Property<?> $$1 = stateDefinitionBlockBlockState0.getProperty(this.key);
        if ($$1 == null) {
            throw new RuntimeException(String.format(Locale.ROOT, "Unknown property '%s' on '%s'", this.key, stateDefinitionBlockBlockState0.getOwner()));
        } else {
            String $$2 = this.value;
            boolean $$3 = !$$2.isEmpty() && $$2.charAt(0) == '!';
            if ($$3) {
                $$2 = $$2.substring(1);
            }
            List<String> $$4 = PIPE_SPLITTER.splitToList($$2);
            if ($$4.isEmpty()) {
                throw new RuntimeException(String.format(Locale.ROOT, "Empty value '%s' for property '%s' on '%s'", this.value, this.key, stateDefinitionBlockBlockState0.getOwner()));
            } else {
                Predicate<BlockState> $$5;
                if ($$4.size() == 1) {
                    $$5 = this.getBlockStatePredicate(stateDefinitionBlockBlockState0, $$1, $$2);
                } else {
                    List<Predicate<BlockState>> $$6 = (List<Predicate<BlockState>>) $$4.stream().map(p_111958_ -> this.getBlockStatePredicate(stateDefinitionBlockBlockState0, $$1, p_111958_)).collect(Collectors.toList());
                    $$5 = p_111954_ -> $$6.stream().anyMatch(p_173509_ -> p_173509_.test(p_111954_));
                }
                return $$3 ? $$5.negate() : $$5;
            }
        }
    }

    private Predicate<BlockState> getBlockStatePredicate(StateDefinition<Block, BlockState> stateDefinitionBlockBlockState0, Property<?> property1, String string2) {
        Optional<?> $$3 = property1.getValue(string2);
        if (!$$3.isPresent()) {
            throw new RuntimeException(String.format(Locale.ROOT, "Unknown value '%s' for property '%s' on '%s' in '%s'", string2, this.key, stateDefinitionBlockBlockState0.getOwner(), this.value));
        } else {
            return p_111951_ -> p_111951_.m_61143_(property1).equals($$3.get());
        }
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("key", this.key).add("value", this.value).toString();
    }
}