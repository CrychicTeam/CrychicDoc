package com.almostreliable.ponderjs.util;

import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaObject;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockStateFunction extends Function<BlockIDPredicate, BlockState> {

    static BlockStateFunction of(Context ctx, @Nullable Object o) {
        if (o instanceof BaseFunction function) {
            Function f = (Function) NativeJavaObject.createInterfaceAdapter(ctx, Function.class, function);
            return blockIDPredicate -> {
                Object result = f.apply(blockIDPredicate);
                return (BlockState) of(ctx, result).apply(blockIDPredicate);
            };
        } else {
            BlockState blockState = Util.blockStateOf(o);
            return $ -> blockState;
        }
    }

    static UnaryOperator<BlockState> from(BlockStateFunction function) {
        return blockState -> {
            BlockIDPredicate predicate = Util.createBlockID(blockState);
            return (BlockState) function.apply(predicate);
        };
    }
}