package team.lodestar.lodestone.systems.multiblock;

import net.minecraft.core.BlockPos;

public interface IModularMultiBlockCore extends IMultiBlockCore {

    @Override
    default boolean isModular() {
        return true;
    }

    void separate(BlockPos var1);

    void connect(BlockPos var1);

    void reset();
}