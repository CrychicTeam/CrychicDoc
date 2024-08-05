package snownee.kiwi.util;

import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;

public class BlockPredicateHelper {

    public static boolean fastMatch(BlockPredicate predicate, BlockState blockstate) {
        if (predicate == BlockPredicate.ANY) {
            return true;
        } else if (predicate.tag != null && !blockstate.m_204336_(predicate.tag)) {
            return false;
        } else if (predicate.blocks != null && !predicate.blocks.contains(blockstate.m_60734_())) {
            return false;
        } else if (!predicate.properties.matches(blockstate)) {
            return false;
        } else if (predicate.nbt != NbtPredicate.ANY) {
            throw new NotImplementedException();
        } else {
            return true;
        }
    }
}