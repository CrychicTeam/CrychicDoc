package org.violetmoon.zetaimplforge.mixin.delegate;

import java.util.Optional;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.zeta.block.ext.CustomWeatheringCopper;

public class WeatheringCopperDelegate {

    public static Optional<BlockState> customWeatheringPrevious(Optional<BlockState> original, BlockState state) {
        return state.m_60734_() instanceof CustomWeatheringCopper copper ? copper.getPrevious(state) : original;
    }

    public static BlockState customWeatheringFirst(BlockState original, BlockState state) {
        return state.m_60734_() instanceof CustomWeatheringCopper copper ? copper.getFirst(state) : original;
    }
}