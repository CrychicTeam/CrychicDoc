package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaSignItem extends SignItem implements IZetaItem {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaSignItem(@Nullable ZetaModule module, Block sign, Block wallSign) {
        super(new Item.Properties().stacksTo(16), sign, wallSign);
        this.module = module;
        if (module != null) {
            String resloc = module.zeta.registryUtil.inherit(sign, "%s");
            module.zeta.registry.registerItem(this, resloc);
            this.setCreativeTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, Blocks.CHEST, true);
        }
    }

    public ZetaSignItem setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }
}