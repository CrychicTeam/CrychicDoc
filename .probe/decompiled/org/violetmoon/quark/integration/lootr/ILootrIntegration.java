package org.violetmoon.quark.integration.lootr;

import java.util.function.BooleanSupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;

public interface ILootrIntegration {

    default BlockEntityType<? extends ChestBlockEntity> chestTE() {
        return null;
    }

    default BlockEntityType<? extends ChestBlockEntity> trappedChestTE() {
        return null;
    }

    default void makeChestBlocks(ZetaModule module, String name, Block base, BooleanSupplier condition, Block superRegularChest, Block superTrappedChest) {
    }

    @Nullable
    default Block lootrVariant(Block base) {
        return null;
    }

    default void postRegister() {
    }

    public static class Dummy implements ILootrIntegration {
    }
}