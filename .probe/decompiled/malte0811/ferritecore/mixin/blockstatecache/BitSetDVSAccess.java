package malte0811.ferritecore.mixin.blockstatecache;

import java.util.BitSet;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ BitSetDiscreteVoxelShape.class })
public interface BitSetDVSAccess extends DiscreteVSAccess {

    @Accessor
    BitSet getStorage();

    @Accessor
    int getXMin();

    @Accessor
    int getYMin();

    @Accessor
    int getZMin();

    @Accessor
    int getXMax();

    @Accessor
    int getYMax();

    @Accessor
    int getZMax();
}