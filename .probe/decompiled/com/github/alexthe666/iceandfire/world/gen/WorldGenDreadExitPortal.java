package com.github.alexthe666.iceandfire.world.gen;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class WorldGenDreadExitPortal {

    private static final ResourceLocation STRUCTURE = new ResourceLocation("iceandfire", "dread_exit_portal");

    public boolean generate(Level worldIn, Random rand, BlockPos position) {
        return true;
    }
}