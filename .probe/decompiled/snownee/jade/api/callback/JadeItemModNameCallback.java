package snownee.jade.api.callback;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface JadeItemModNameCallback {

    @Nullable
    String gatherItemModName(ItemStack var1);
}