package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;

public class ZetaBlockWrapper implements IZetaBlock, ItemLike {

    private final Block parent;

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier condition;

    public ZetaBlockWrapper(Block parent, @Nullable ZetaModule module) {
        this.parent = parent;
        this.module = module;
    }

    @Override
    public Block getBlock() {
        return this.parent;
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    public ZetaBlockWrapper setCondition(BooleanSupplier condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.condition == null || this.condition.getAsBoolean();
    }

    @Override
    public Item asItem() {
        return this.parent.asItem();
    }
}