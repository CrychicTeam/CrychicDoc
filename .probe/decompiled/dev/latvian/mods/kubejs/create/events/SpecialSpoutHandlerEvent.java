package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.resources.ResourceLocation;

public class SpecialSpoutHandlerEvent extends EventJS {

    public void add(ResourceLocation path, BlockStatePredicate block, SpecialSpoutHandlerEvent.SpoutHandler handler) {
        BlockSpoutingBehaviour.addCustomSpoutInteraction(path, FluidIngredientHelper.createSpoutingHandler(block, handler));
    }

    @FunctionalInterface
    public interface SpoutHandler {

        long fillBlock(BlockContainerJS var1, FluidStackJS var2, boolean var3);
    }
}