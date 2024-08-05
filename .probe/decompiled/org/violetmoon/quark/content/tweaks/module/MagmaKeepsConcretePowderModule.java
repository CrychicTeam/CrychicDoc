package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "tweaks")
public class MagmaKeepsConcretePowderModule extends ZetaModule {

    private static boolean staticEnabled;

    @LoadEvent
    public void configChanged(ZConfigChanged event) {
        staticEnabled = this.enabled;
    }

    public static boolean preventSolidify(BlockGetter pLevel, BlockPos pPos, BlockState state) {
        return !staticEnabled ? false : pLevel.getBlockState(pPos.below()).m_60713_(Blocks.MAGMA_BLOCK);
    }
}