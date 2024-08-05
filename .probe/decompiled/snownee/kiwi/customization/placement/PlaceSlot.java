package snownee.kiwi.customization.placement;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public record PlaceSlot(Direction side, ImmutableSortedMap<String, String> tags) {

    public static final Comparator<String> TAG_COMPARATOR = (a, b) -> {
        boolean aStar = a.charAt(0) == '*';
        boolean bStar = b.charAt(0) == '*';
        if (aStar != bStar) {
            return aStar ? -1 : 1;
        } else {
            return a.compareTo(b);
        }
    };

    private static ImmutableListMultimap<Pair<BlockState, Direction>, PlaceSlot> BLOCK_STATE_LOOKUP = ImmutableListMultimap.of();

    private static ImmutableSet<Block> BLOCK_HAS_SLOTS = ImmutableSet.of();

    public static Collection<PlaceSlot> find(BlockState blockState, Direction side) {
        if (blockState.m_61138_(BlockStateProperties.WATERLOGGED)) {
            blockState = (BlockState) blockState.m_61124_(BlockStateProperties.WATERLOGGED, false);
        }
        return BLOCK_STATE_LOOKUP.get(Pair.of(blockState, side));
    }

    public static Optional<PlaceSlot> find(BlockState blockState, Direction side, String primaryTag) {
        if (hasNoSlots(blockState.m_60734_())) {
            return Optional.empty();
        } else {
            Collection<PlaceSlot> slots = find(blockState, side);
            return slots.isEmpty() ? Optional.empty() : slots.stream().filter(slot -> slot.primaryTag().equals(primaryTag)).findAny();
        }
    }

    public static boolean hasNoSlots(Block block) {
        return !BLOCK_HAS_SLOTS.contains(block);
    }

    public static void renewData(PlaceSlotProvider.Preparation preparation) {
        BLOCK_STATE_LOOKUP = ImmutableListMultimap.copyOf(preparation.slots());
        BLOCK_HAS_SLOTS = (ImmutableSet<Block>) preparation.slots().keySet().stream().map(Pair::getFirst).map(BlockBehaviour.BlockStateBase::m_60734_).collect(ImmutableSet.toImmutableSet());
    }

    public static int blockCount() {
        return BLOCK_HAS_SLOTS.size();
    }

    public String primaryTag() {
        return (String) this.tags.firstKey();
    }

    public List<String> tagList() {
        List<String> list = Lists.newArrayListWithCapacity(this.tags.size());
        this.tags.forEach((k, v) -> {
            if (v.isEmpty()) {
                list.add(k);
            } else {
                list.add(k + ":" + v);
            }
        });
        return list;
    }

    public boolean hasTag(ParsedProtoTag resolvedTag) {
        Preconditions.checkArgument(resolvedTag.isResolved(), "Tag must be resolved");
        return resolvedTag.prefix().equals("*") ? this.tags.containsKey(resolvedTag.toString()) : Objects.equals(this.tags.get(resolvedTag.key()), resolvedTag.value());
    }
}