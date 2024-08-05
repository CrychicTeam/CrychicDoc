package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaBushBlock extends BushBlock implements IZetaBlock {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaBushBlock(String regname, @Nullable ZetaModule module, ResourceKey<CreativeModeTab> tab, BlockBehaviour.Properties properties) {
        super(properties);
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerBlock(this, regname, true);
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
            if (tab != null) {
                this.setCreativeTab(tab);
            }
        }
    }

    public ZetaBushBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }
}