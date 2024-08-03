package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaFenceGateBlock extends FenceGateBlock implements IZetaBlock {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    @Deprecated
    public ZetaFenceGateBlock(String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        this(regname, module, WoodType.OAK, properties);
    }

    public ZetaFenceGateBlock(String regname, @Nullable ZetaModule module, WoodType woodType, BlockBehaviour.Properties properties) {
        this(regname, module, woodType.fenceGateOpen(), woodType.fenceGateClose(), properties);
    }

    public ZetaFenceGateBlock(String regname, @Nullable ZetaModule module, SoundEvent open, SoundEvent close, BlockBehaviour.Properties properties) {
        super(properties, open, close);
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerBlock(this, regname, true);
        }
    }

    public ZetaFenceGateBlock setCondition(BooleanSupplier enabledSupplier) {
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