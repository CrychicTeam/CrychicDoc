package malte0811.ferritecore.hash;

import it.unimi.dsi.fastutil.Hash.Strategy;
import java.util.Objects;
import malte0811.ferritecore.mixin.blockstatecache.SliceShapeAccess;

public class SliceShapeHash implements Strategy<SliceShapeAccess> {

    public static final SliceShapeHash INSTANCE = new SliceShapeHash();

    public int hashCode(SliceShapeAccess o) {
        int result = Objects.hashCode(o.getAxis());
        result = 31 * result + DiscreteVSHash.INSTANCE.hashCode(o.getShape());
        return 31 * result + VoxelShapeHash.INSTANCE.hashCode(o.getDelegate());
    }

    public boolean equals(SliceShapeAccess a, SliceShapeAccess b) {
        if (a == b) {
            return true;
        } else {
            return a != null && b != null ? Objects.equals(a.getAxis(), b.getAxis()) && VoxelShapeHash.INSTANCE.equals(a.getDelegate(), b.getDelegate()) && DiscreteVSHash.INSTANCE.equals(a.getShape(), b.getShape()) : false;
        }
    }
}