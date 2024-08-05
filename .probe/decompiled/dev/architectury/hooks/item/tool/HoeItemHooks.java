package dev.architectury.hooks.item.tool;

import dev.architectury.hooks.item.tool.forge.HoeItemHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class HoeItemHooks {

    private HoeItemHooks() {
    }

    @ExpectPlatform
    @Transformed
    public static void addTillable(Block input, Predicate<UseOnContext> predicate, Consumer<UseOnContext> action, Function<UseOnContext, BlockState> newState) {
        HoeItemHooksImpl.addTillable(input, predicate, action, newState);
    }
}