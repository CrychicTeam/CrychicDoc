package net.mehvahdjukaar.amendments.common;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.amendments.common.forge.FlowerPotHandlerImpl;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.jetbrains.annotations.Contract;

public class FlowerPotHandler {

    @Contract
    @ExpectPlatform
    @Transformed
    public static Block getEmptyPot(FlowerPotBlock fullPot) {
        return FlowerPotHandlerImpl.getEmptyPot(fullPot);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static Block getFullPot(FlowerPotBlock emptyPot, Block flowerBlock) {
        return FlowerPotHandlerImpl.getFullPot(emptyPot, flowerBlock);
    }

    @ExpectPlatform
    @Transformed
    public static boolean isEmptyPot(Block b) {
        return FlowerPotHandlerImpl.isEmptyPot(b);
    }

    @ExpectPlatform
    @Transformed
    public static void setup() {
        FlowerPotHandlerImpl.setup();
    }
}