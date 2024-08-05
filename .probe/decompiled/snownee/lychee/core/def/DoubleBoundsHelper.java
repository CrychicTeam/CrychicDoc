package snownee.lychee.core.def;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import snownee.lychee.mixin.DoublesAccess;

public class DoubleBoundsHelper {

    public static MinMaxBounds.Doubles fromNetwork(FriendlyByteBuf pBuffer) {
        double min = pBuffer.readDouble();
        double max = pBuffer.readDouble();
        return Double.isNaN(min) && Double.isNaN(max) ? MinMaxBounds.Doubles.ANY : DoublesAccess.create(Double.isNaN(min) ? null : min, Double.isNaN(max) ? null : max);
    }

    public static void toNetwork(MinMaxBounds.Doubles doubles, FriendlyByteBuf pBuffer) {
        Double min = (Double) doubles.m_55305_();
        if (min == null) {
            min = Double.NaN;
        }
        pBuffer.writeDouble(min);
        Double max = (Double) doubles.m_55326_();
        if (max == null) {
            max = Double.NaN;
        }
        pBuffer.writeDouble(max);
    }

    public static float random(MinMaxBounds.Doubles doubles, RandomSource random) {
        float min = doubles.m_55305_() == null ? Float.MIN_VALUE : ((Double) doubles.m_55305_()).floatValue();
        float max = doubles.m_55326_() == null ? Float.MAX_VALUE : ((Double) doubles.m_55326_()).floatValue();
        return min == max ? min : Mth.randomBetween(random, min, max);
    }
}