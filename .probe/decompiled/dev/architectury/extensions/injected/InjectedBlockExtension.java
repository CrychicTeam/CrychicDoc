package dev.architectury.extensions.injected;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

public interface InjectedBlockExtension extends InjectedRegistryEntryExtension<Block> {

    @Override
    default Holder<Block> arch$holder() {
        return ((Block) this).builtInRegistryHolder();
    }
}