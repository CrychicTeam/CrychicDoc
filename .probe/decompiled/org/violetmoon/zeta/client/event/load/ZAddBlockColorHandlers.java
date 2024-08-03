package org.violetmoon.zeta.client.event.load;

import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.block.Block;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;

public interface ZAddBlockColorHandlers extends IZetaLoadEvent {

    void register(BlockColor var1, Block... var2);

    void registerNamed(Function<Block, BlockColor> var1, String... var2);

    BlockColors getBlockColors();

    ZAddBlockColorHandlers.Post makePostEvent();

    public interface Post extends ZAddBlockColorHandlers {

        Map<String, Function<Block, BlockColor>> getNamedBlockColors();
    }
}