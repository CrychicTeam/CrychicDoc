package org.violetmoon.zetaimplforge.client.event.load;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import org.violetmoon.zeta.client.event.load.ZAddBlockColorHandlers;

public class ForgeZAddBlockColorHandlers implements ZAddBlockColorHandlers {

    protected final RegisterColorHandlersEvent.Block e;

    protected final Map<String, Function<Block, BlockColor>> namedBlockColors;

    public ForgeZAddBlockColorHandlers(RegisterColorHandlersEvent.Block e) {
        this(e, new HashMap());
    }

    protected ForgeZAddBlockColorHandlers(RegisterColorHandlersEvent.Block e, Map<String, Function<Block, BlockColor>> namedBlockColors) {
        this.e = e;
        this.namedBlockColors = namedBlockColors;
    }

    @Override
    public void register(BlockColor blockColor, Block... blocks) {
        this.e.register(blockColor, blocks);
    }

    @Override
    public void registerNamed(Function<Block, BlockColor> c, String... names) {
        for (String name : names) {
            this.namedBlockColors.put(name, c);
        }
    }

    @Override
    public BlockColors getBlockColors() {
        return this.e.getBlockColors();
    }

    @Override
    public ZAddBlockColorHandlers.Post makePostEvent() {
        return new ForgeZAddBlockColorHandlers.Post(this.e, this.namedBlockColors);
    }

    public static class Post extends ForgeZAddBlockColorHandlers implements ZAddBlockColorHandlers.Post {

        public Post(RegisterColorHandlersEvent.Block e, Map<String, Function<Block, BlockColor>> namedBlockColors) {
            super(e, namedBlockColors);
        }

        @Override
        public Map<String, Function<Block, BlockColor>> getNamedBlockColors() {
            return this.namedBlockColors;
        }
    }
}