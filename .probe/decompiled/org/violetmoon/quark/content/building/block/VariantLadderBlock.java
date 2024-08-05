package org.violetmoon.quark.content.building.block;

import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class VariantLadderBlock extends LadderBlock implements IZetaBlock {

    @Nullable
    private final ZetaModule module;

    private final boolean flammable;

    private BooleanSupplier condition = BooleanSuppliers.TRUE;

    public VariantLadderBlock(String type, @Nullable ZetaModule module, BlockBehaviour.Properties props, boolean flammable) {
        super(props);
        this.module = module;
        this.flammable = flammable;
        if (module != null) {
            module.zeta.registry.registerBlock(this, type + "_ladder", true);
            CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.FUNCTIONAL_BLOCKS, this, Blocks.LADDER, false);
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
        }
    }

    public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return this.flammable;
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    public VariantLadderBlock setCondition(BooleanSupplier condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.condition.getAsBoolean();
    }
}