package vazkii.patchouli.common.multiblock;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.TriPredicate;

public class StringStateMatcher {

    public static IStateMatcher fromString(String s) throws CommandSyntaxException {
        s = s.trim();
        if (s.equals("ANY")) {
            return StateMatcher.ANY;
        } else {
            return (IStateMatcher) (s.equals("AIR") ? StateMatcher.AIR : (IStateMatcher) BlockStateParser.parseForTesting(BuiltInRegistries.BLOCK.m_255303_(), s, true).map(blockResult -> new StringStateMatcher.ExactMatcher(blockResult.blockState(), blockResult.properties()), tagResult -> new StringStateMatcher.TagMatcher(tagResult.tag(), tagResult.vagueProperties())));
        }
    }

    private static class ExactMatcher implements IStateMatcher {

        private final BlockState state;

        private final Map<Property<?>, Comparable<?>> props;

        private ExactMatcher(BlockState state, Map<Property<?>, Comparable<?>> props) {
            this.state = state;
            this.props = props;
        }

        @Override
        public BlockState getDisplayedState(long ticks) {
            return this.state;
        }

        @Override
        public TriPredicate<BlockGetter, BlockPos, BlockState> getStatePredicate() {
            return (w, p, s) -> this.state.m_60734_() == s.m_60734_() && this.checkProps(s);
        }

        private boolean checkProps(BlockState state) {
            for (Entry<Property<?>, Comparable<?>> e : this.props.entrySet()) {
                if (!state.m_61143_((Property) e.getKey()).equals(e.getValue())) {
                    return false;
                }
            }
            return true;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                StringStateMatcher.ExactMatcher that = (StringStateMatcher.ExactMatcher) o;
                return Objects.equals(this.state, that.state) && Objects.equals(this.props, that.props);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.state, this.props });
        }
    }

    private static class TagMatcher implements IStateMatcher {

        private final HolderSet<Block> tag;

        private final Map<String, String> props;

        private TagMatcher(HolderSet<Block> tag, Map<String, String> props) {
            this.tag = tag;
            this.props = props;
        }

        @Override
        public BlockState getDisplayedState(long ticks) {
            if (this.tag.size() == 0) {
                return Blocks.BEDROCK.defaultBlockState();
            } else {
                int idx = (int) (ticks / 20L % (long) this.tag.size());
                return this.tag.get(idx).value().defaultBlockState();
            }
        }

        @Override
        public TriPredicate<BlockGetter, BlockPos, BlockState> getStatePredicate() {
            return (w, p, s) -> s.m_204341_(this.tag) && this.checkProps(s);
        }

        private boolean checkProps(BlockState state) {
            for (Entry<String, String> entry : this.props.entrySet()) {
                Property<?> prop = state.m_60734_().getStateDefinition().getProperty((String) entry.getKey());
                if (prop == null) {
                    return false;
                }
                Comparable<?> value = (Comparable<?>) prop.getValue((String) entry.getValue()).orElse(null);
                if (value == null) {
                    return false;
                }
                if (!state.m_61143_(prop).equals(value)) {
                    return false;
                }
            }
            return true;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                StringStateMatcher.TagMatcher that = (StringStateMatcher.TagMatcher) o;
                return Objects.equals(this.tag, that.tag) && Objects.equals(this.props, that.props);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.tag, this.props });
        }
    }
}