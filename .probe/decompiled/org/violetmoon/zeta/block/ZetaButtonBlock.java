package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.BooleanSuppliers;

public abstract class ZetaButtonBlock extends ButtonBlock implements IZetaBlock {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaButtonBlock(BlockSetType setType, int ticksToStayPressed, boolean arrowsCanPress, String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(properties, setType, ticksToStayPressed, arrowsCanPress);
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerBlock(this, regname, true);
            CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.REDSTONE_BLOCKS, this, Blocks.STONE_BUTTON, false);
        }
    }

    @NotNull
    @Override
    protected abstract SoundEvent getSound(boolean var1);

    public ZetaButtonBlock setCondition(BooleanSupplier enabledSupplier) {
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