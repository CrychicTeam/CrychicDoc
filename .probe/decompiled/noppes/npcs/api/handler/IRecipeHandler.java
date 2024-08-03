package noppes.npcs.api.handler;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.api.handler.data.IRecipe;

public interface IRecipeHandler {

    List<IRecipe> getGlobalList();

    List<IRecipe> getCarpentryList();

    IRecipe addRecipe(String var1, boolean var2, ItemStack var3, Object... var4);

    IRecipe addRecipe(String var1, boolean var2, ItemStack var3, int var4, int var5, ItemStack... var6);

    IRecipe delete(int var1);
}