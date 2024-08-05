package com.simibubi.create.content.decoration.copycat;

import com.simibubi.create.content.decoration.palettes.GlassPaneBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatSpecialCases {

    public static boolean isBarsMaterial(BlockState material) {
        return material.m_60734_() instanceof IronBarsBlock && !(material.m_60734_() instanceof GlassPaneBlock) && !(material.m_60734_() instanceof StainedGlassPaneBlock);
    }

    public static boolean isTrapdoorMaterial(BlockState material) {
        return material.m_60734_() instanceof TrapDoorBlock && material.m_61138_(TrapDoorBlock.HALF) && material.m_61138_(TrapDoorBlock.OPEN) && material.m_61138_(TrapDoorBlock.f_54117_);
    }
}