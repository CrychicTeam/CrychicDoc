package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import vectorwing.farmersdelight.common.tag.ModTags;

public interface HeatableBlockEntity {

    default boolean isHeated(Level level, BlockPos pos) {
        BlockState stateBelow = level.getBlockState(pos.below());
        if (stateBelow.m_204336_(ModTags.HEAT_SOURCES)) {
            return stateBelow.m_61138_(BlockStateProperties.LIT) ? (Boolean) stateBelow.m_61143_(BlockStateProperties.LIT) : true;
        } else {
            if (!this.requiresDirectHeat() && stateBelow.m_204336_(ModTags.HEAT_CONDUCTORS)) {
                BlockState stateFurtherBelow = level.getBlockState(pos.below(2));
                if (stateFurtherBelow.m_204336_(ModTags.HEAT_SOURCES)) {
                    if (stateFurtherBelow.m_61138_(BlockStateProperties.LIT)) {
                        return (Boolean) stateFurtherBelow.m_61143_(BlockStateProperties.LIT);
                    }
                    return true;
                }
            }
            return false;
        }
    }

    default boolean requiresDirectHeat() {
        return false;
    }
}