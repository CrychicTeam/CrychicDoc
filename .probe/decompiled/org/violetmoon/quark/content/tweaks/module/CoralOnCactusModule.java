package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CoralFanBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "tweaks")
public class CoralOnCactusModule extends ZetaModule {

    private static boolean staticEnabled;

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        staticEnabled = this.enabled;
    }

    public static boolean scanForWater(BlockState state, BlockGetter world, BlockPos pos, boolean prevValue) {
        if (prevValue || !staticEnabled) {
            return prevValue;
        } else {
            return state.m_60734_() instanceof CoralFanBlock ? world.getBlockState(pos.below()).m_60734_() == Blocks.CACTUS : false;
        }
    }
}