package org.violetmoon.quark.content.building.block;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.block.ZetaSlabBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class QuarkVerticalSlabBlock extends VerticalSlabBlock implements IZetaBlock {

    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public QuarkVerticalSlabBlock(Block parent, ZetaModule module) {
        super(() -> parent, BlockBehaviour.Properties.copy(parent));
        String resloc = Quark.ZETA.registryUtil.inherit(parent, (Function<String, String>) (s -> s.replace("_slab", "_vertical_slab")));
        Quark.ZETA.registry.registerBlock(this, resloc, true);
        this.module = module;
        if (module.category.isAddon()) {
            module.zeta.requiredModTooltipHandler.map(this, module.category.requiredMod);
        }
        if (!(parent instanceof SlabBlock)) {
            throw new IllegalArgumentException("Can't rotate a non-slab block into a vertical slab.");
        } else {
            if (parent instanceof ZetaSlabBlock quarkSlab) {
                this.setCondition(quarkSlab.parent::isEnabled);
            }
            CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.BUILDING_BLOCKS, this, parent, false);
        }
    }

    public QuarkVerticalSlabBlock setCondition(BooleanSupplier enabledSupplier) {
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