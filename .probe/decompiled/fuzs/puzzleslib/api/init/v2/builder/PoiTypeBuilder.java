package fuzs.puzzleslib.api.init.v2.builder;

import com.google.common.collect.ImmutableSet;
import java.util.stream.Stream;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@Deprecated(forRemoval = true)
public record PoiTypeBuilder(int ticketCount, int searchDistance, Iterable<BlockState> blocks) {

    private PoiTypeBuilder(int ticketCount, int searchDistance, Block... blocks) {
        this(ticketCount, searchDistance, (Iterable<BlockState>) Stream.of(blocks).mapMulti((block, mapper) -> block.getStateDefinition().getPossibleStates().forEach(mapper)).collect(ImmutableSet.toImmutableSet()));
    }

    public static PoiTypeBuilder of(int ticketCount, int searchDistance, Iterable<BlockState> blocks) {
        return new PoiTypeBuilder(ticketCount, searchDistance, blocks);
    }

    public static PoiTypeBuilder of(int ticketCount, int searchDistance, Block... blocks) {
        return new PoiTypeBuilder(ticketCount, searchDistance, blocks);
    }
}