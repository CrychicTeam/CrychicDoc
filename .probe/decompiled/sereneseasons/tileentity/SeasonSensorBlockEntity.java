package sereneseasons.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import sereneseasons.api.SSBlockEntities;

public class SeasonSensorBlockEntity extends BlockEntity {

    public SeasonSensorBlockEntity(BlockPos pos, BlockState state) {
        super(SSBlockEntities.SEASON_SENSOR.get(), pos, state);
    }
}