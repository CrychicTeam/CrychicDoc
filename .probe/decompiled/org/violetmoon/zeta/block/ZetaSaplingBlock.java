package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaSaplingBlock extends SaplingBlock implements IZetaBlock {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaSaplingBlock(String name, @Nullable ZetaModule module, AbstractTreeGrower tree) {
        super(tree, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerBlock(this, name + "_sapling", true);
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
            this.setCreativeTab(CreativeModeTabs.NATURAL_BLOCKS, Blocks.AZALEA, true);
        }
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    public ZetaSaplingBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }
}