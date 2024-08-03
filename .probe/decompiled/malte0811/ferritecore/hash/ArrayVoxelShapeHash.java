package malte0811.ferritecore.hash;

import it.unimi.dsi.fastutil.Hash.Strategy;
import java.util.Objects;
import malte0811.ferritecore.mixin.blockstatecache.ArrayVSAccess;

public class ArrayVoxelShapeHash implements Strategy<ArrayVSAccess> {

    public static final ArrayVoxelShapeHash INSTANCE = new ArrayVoxelShapeHash();

    public int hashCode(ArrayVSAccess o) {
        return 31 * Objects.hash(new Object[] { o.getXPoints(), o.getYPoints(), o.getZPoints() }) + DiscreteVSHash.INSTANCE.hashCode(o.getShape());
    }

    public boolean equals(ArrayVSAccess a, ArrayVSAccess b) {
        if (a == b) {
            return true;
        } else {
            return a != null && b != null ? Objects.equals(a.getXPoints(), b.getXPoints()) && Objects.equals(a.getYPoints(), b.getYPoints()) && Objects.equals(a.getZPoints(), b.getZPoints()) && DiscreteVSHash.INSTANCE.equals(a.getShape(), b.getShape()) : false;
        }
    }
}