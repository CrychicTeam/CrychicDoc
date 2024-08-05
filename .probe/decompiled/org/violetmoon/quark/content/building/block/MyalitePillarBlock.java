package org.violetmoon.quark.content.building.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.block.ZetaPillarBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockColorProvider;

public class MyalitePillarBlock extends ZetaPillarBlock implements IZetaBlockColorProvider {

    public MyalitePillarBlock(String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
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