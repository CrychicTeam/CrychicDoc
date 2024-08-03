package org.violetmoon.zeta.module;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public interface IDisableable<SELF> {

    @Nullable
    ZetaModule getModule();

    SELF setCondition(BooleanSupplier var1);

    boolean doesConditionApply();

    default boolean isEnabled() {
        ZetaModule module = this.getModule();
        return module != null && module.enabled && this.doesConditionApply();
    }

    static boolean isEnabled(Item i) {
        if (i instanceof IDisableable<?> dis) {
            return dis.isEnabled();
        } else {
            return i instanceof BlockItem bi ? isEnabled(bi.getBlock()) : true;
        }
    }

    static boolean isEnabled(Block b) {
        return b instanceof IDisableable<?> dis ? dis.isEnabled() : true;
    }
}