package mezz.jei.api.helpers;

import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IStackHelper {

    boolean isEquivalent(@Nullable ItemStack var1, @Nullable ItemStack var2, UidContext var3);

    String getUniqueIdentifierForStack(ItemStack var1, UidContext var2);
}