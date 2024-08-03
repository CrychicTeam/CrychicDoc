package dev.latvian.mods.kubejs.recipe.special;

import dev.architectury.utils.GameInstance;
import dev.latvian.mods.kubejs.core.CraftingContainerKJS;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeCraftingGrid;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.jetbrains.annotations.Nullable;

public interface KubeJSCraftingRecipe extends CraftingRecipe {

    List<IngredientAction> kjs$getIngredientActions();

    @Nullable
    ModifyRecipeResultCallback kjs$getModifyResult();

    String kjs$getStage();

    default NonNullList<ItemStack> kjs$getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.m_6643_(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); i++) {
            list.set(i, IngredientAction.getRemaining(container, i, this.kjs$getIngredientActions()));
        }
        return list;
    }

    default ItemStack kjs$assemble(CraftingContainer container, RegistryAccess registryAccess) {
        if (!this.kjs$getStage().isEmpty()) {
            Player player = getPlayer(((CraftingContainerKJS) container).kjs$getMenu());
            if (player == null || !player.kjs$getStages().has(this.kjs$getStage())) {
                return ItemStack.EMPTY;
            }
        }
        ModifyRecipeResultCallback modifyResult = this.kjs$getModifyResult();
        ItemStack result = this.m_8043_(registryAccess);
        result = result != null && !result.isEmpty() ? result.copy() : ItemStack.EMPTY;
        return modifyResult != null ? modifyResult.modify(new ModifyRecipeCraftingGrid(container), result) : result;
    }

    @Nullable
    private static Player getPlayer(AbstractContainerMenu menu) {
        if (menu instanceof CraftingMenu craft) {
            return craft.player;
        } else if (menu instanceof InventoryMenu inv) {
            return inv.owner;
        } else {
            MinecraftServer server = GameInstance.getServer();
            if (server != null) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    if (player.f_36096_ == menu && menu.stillValid(player)) {
                        return player;
                    }
                }
            }
            return null;
        }
    }
}