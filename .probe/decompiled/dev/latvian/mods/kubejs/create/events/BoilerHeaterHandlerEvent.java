package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.content.fluids.tank.BoilerHeaters;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.platform.BoilerHeaterHelper;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.world.level.block.Block;

public class BoilerHeaterHandlerEvent extends EventJS {

    public void add(Block block, BoilerHeaterHandlerEvent.BoilerHeaterCallback onUpdate) {
        BoilerHeaterHelper.registerHeaterPlatform(block, onUpdate);
    }

    public void addAdvanced(BlockStatePredicate block, BoilerHeaterHandlerEvent.BoilerHeaterCallback onUpdate) {
        BoilerHeaters.registerHeaterProvider((level, blockPos, blockState) -> block.test(blockState) ? (l, b, bs) -> onUpdate.updateHeat(l.kjs$getBlock(b)) : null);
    }

    @FunctionalInterface
    public interface BoilerHeaterCallback {

        float updateHeat(BlockContainerJS var1);
    }
}