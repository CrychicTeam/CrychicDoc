package org.violetmoon.quark.api;

import java.util.function.Supplier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandler;

public interface ITransferManager {

    boolean acceptsTransfer(Player var1);

    default IItemHandler getTransferItemHandler(Supplier<IItemHandler> defaultSupplier) {
        return (IItemHandler) defaultSupplier.get();
    }
}