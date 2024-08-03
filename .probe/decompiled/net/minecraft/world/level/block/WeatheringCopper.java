package net.minecraft.world.level.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.world.level.block.state.BlockState;

public interface WeatheringCopper extends ChangeOverTimeBlock<WeatheringCopper.WeatherState> {

    Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.builder().put(Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER).put(Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER).put(Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER).put(Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER).put(Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER).put(Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER).put(Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB).put(Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB).put(Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB).put(Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS).put(Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS).put(Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_STAIRS).build());

    Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> ((BiMap) NEXT_BY_BLOCK.get()).inverse());

    static Optional<Block> getPrevious(Block block0) {
        return Optional.ofNullable((Block) ((BiMap) PREVIOUS_BY_BLOCK.get()).get(block0));
    }

    static Block getFirst(Block block0) {
        Block $$1 = block0;
        for (Block $$2 = (Block) ((BiMap) PREVIOUS_BY_BLOCK.get()).get(block0); $$2 != null; $$2 = (Block) ((BiMap) PREVIOUS_BY_BLOCK.get()).get($$2)) {
            $$1 = $$2;
        }
        return $$1;
    }

    static Optional<BlockState> getPrevious(BlockState blockState0) {
        return getPrevious(blockState0.m_60734_()).map(p_154903_ -> p_154903_.withPropertiesOf(blockState0));
    }

    static Optional<Block> getNext(Block block0) {
        return Optional.ofNullable((Block) ((BiMap) NEXT_BY_BLOCK.get()).get(block0));
    }

    static BlockState getFirst(BlockState blockState0) {
        return getFirst(blockState0.m_60734_()).withPropertiesOf(blockState0);
    }

    @Override
    default Optional<BlockState> getNext(BlockState blockState0) {
        return getNext(blockState0.m_60734_()).map(p_154896_ -> p_154896_.withPropertiesOf(blockState0));
    }

    @Override
    default float getChanceModifier() {
        return this.m_142297_() == WeatheringCopper.WeatherState.UNAFFECTED ? 0.75F : 1.0F;
    }

    public static enum WeatherState {

        UNAFFECTED, EXPOSED, WEATHERED, OXIDIZED
    }
}