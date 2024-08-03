package snownee.lychee.core.def;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.storage.loot.IntRange;
import snownee.lychee.mixin.IntRangeAccess;
import snownee.lychee.mixin.IntsAccess;

public class IntBoundsHelper {

    public static final MinMaxBounds.Ints ONE = MinMaxBounds.Ints.exactly(1);

    public static MinMaxBounds.Ints fromNetwork(FriendlyByteBuf pBuffer) {
        int min = pBuffer.readInt();
        int max = pBuffer.readInt();
        if (min == Integer.MAX_VALUE && max == Integer.MIN_VALUE) {
            return MinMaxBounds.Ints.ANY;
        } else {
            return min == 1 && max == 1 ? ONE : IntsAccess.create(min == Integer.MAX_VALUE ? null : min, max == Integer.MIN_VALUE ? null : max);
        }
    }

    public static void toNetwork(MinMaxBounds.Ints ints, FriendlyByteBuf pBuffer) {
        Integer min = (Integer) ints.m_55305_();
        if (min == null) {
            min = Integer.MAX_VALUE;
        }
        pBuffer.writeInt(min);
        Integer max = (Integer) ints.m_55326_();
        if (max == null) {
            max = Integer.MIN_VALUE;
        }
        pBuffer.writeInt(max);
    }

    public static MinMaxBounds.Ints fromIntRange(IntRangeAccess range) {
        return IntsAccess.create(NumberProviderHelper.toConstant(range.getMin()), NumberProviderHelper.toConstant(range.getMax()));
    }

    public static IntRange toIntRange(MinMaxBounds.Ints ints) {
        return IntRangeAccess.create(NumberProviderHelper.fromConstant((Integer) ints.m_55305_()), NumberProviderHelper.fromConstant((Integer) ints.m_55326_()));
    }

    public static int random(MinMaxBounds.Ints ints, RandomSource random) {
        int min = ints.m_55305_() == null ? Integer.MIN_VALUE : (Integer) ints.m_55305_();
        int max = ints.m_55326_() == null ? Integer.MAX_VALUE : (Integer) ints.m_55326_();
        return min == max ? min : Mth.randomBetweenInclusive(random, min, max);
    }
}