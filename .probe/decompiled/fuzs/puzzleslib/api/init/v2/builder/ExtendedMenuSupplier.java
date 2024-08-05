package fuzs.puzzleslib.api.init.v2.builder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

@FunctionalInterface
public interface ExtendedMenuSupplier<T extends AbstractContainerMenu> {

    T create(int var1, Inventory var2, FriendlyByteBuf var3);
}