package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaPressurePlateBlock extends PressurePlateBlock implements IZetaBlock {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaPressurePlateBlock(PressurePlateBlock.Sensitivity sensitivity, String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties, BlockSetType blockSetType) {
        super(sensitivity, properties, blockSetType);
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerBlock(this, regname, true);
            this.setCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, false);
        }
    }

    public ZetaPressurePlateBlock setCondition(BooleanSupplier enabledSupplier) {
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