package malte0811.ferritecore.ducks;

import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public interface BlockStateCacheAccess {

    VoxelShape getCollisionShape();

    void setCollisionShape(VoxelShape var1);

    VoxelShape[] getOcclusionShapes();

    void setOcclusionShapes(@Nullable VoxelShape[] var1);

    boolean[] getFaceSturdy();

    void setFaceSturdy(boolean[] var1);
}