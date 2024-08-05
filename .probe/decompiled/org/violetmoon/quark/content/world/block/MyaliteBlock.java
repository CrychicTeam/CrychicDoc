package org.violetmoon.quark.content.world.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockColorProvider;

public class MyaliteBlock extends ZetaBlock implements IZetaBlockColorProvider {

    public MyaliteBlock(String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(regname, module, properties);
    }

    @Nullable
    @Override
    public String getBlockColorProviderName() {
        return "myalite";
    }

    @Nullable
    @Override
    public String getItemColorProviderName() {
        return "myalite";
    }
}