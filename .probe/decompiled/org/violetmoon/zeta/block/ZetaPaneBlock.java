package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaPaneBlock extends IronBarsBlock implements IZetaBlock {

    @Nullable
    public final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaPaneBlock(String name, @Nullable ZetaModule module, BlockBehaviour.Properties properties, RenderLayerRegistry.Layer renderLayer) {
        super(properties);
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerBlock(this, name, true);
            if (renderLayer != null) {
                module.zeta.renderLayerRegistry.put(this, renderLayer);
            }
        }
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    public ZetaPaneBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }
}