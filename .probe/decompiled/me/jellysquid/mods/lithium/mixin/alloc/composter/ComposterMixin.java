package me.jellysquid.mods.lithium.mixin.alloc.composter;

import me.jellysquid.mods.lithium.common.util.ArrayConstants;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

public class ComposterMixin {

    @Mixin(targets = { "net.minecraft.block.ComposterBlock$ComposterInventory" })
    abstract static class ComposterBlockComposterInventoryMixin implements WorldlyContainer {

        @Overwrite
        @Override
        public int[] getSlotsForFace(Direction side) {
            return side == Direction.UP ? ArrayConstants.ZERO : ArrayConstants.EMPTY;
        }
    }

    @Mixin(targets = { "net.minecraft.block.ComposterBlock$DummyInventory" })
    abstract static class ComposterBlockDummyInventoryMixin implements WorldlyContainer {

        @Overwrite
        @Override
        public int[] getSlotsForFace(Direction side) {
            return ArrayConstants.EMPTY;
        }
    }

    @Mixin(targets = { "net.minecraft.block.ComposterBlock$FullComposterInventory" })
    abstract static class ComposterBlockFullComposterInventoryMixin implements WorldlyContainer {

        @Overwrite
        @Override
        public int[] getSlotsForFace(Direction side) {
            return side == Direction.DOWN ? ArrayConstants.ZERO : ArrayConstants.EMPTY;
        }
    }
}