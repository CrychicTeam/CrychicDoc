package fuzs.puzzleslib.api.core.v1.context;

import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface FlammableBlocksContext {

    void registerFlammable(int var1, int var2, Block... var3);
}