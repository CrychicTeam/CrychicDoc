package dev.xkmc.l2backpack.content.recipe;

import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2backpack.init.registrate.BackpackItems;
import dev.xkmc.l2backpack.init.registrate.BackpackMisc;
import dev.xkmc.l2library.serial.recipe.AbstractShapedRecipe;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class MultiSwitchCraftRecipe extends AbstractShapedRecipe<MultiSwitchCraftRecipe> {

    public MultiSwitchCraftRecipe(ResourceLocation rl, String group, int w, int h, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(rl, group, w, h, ingredients, result);
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        List<ItemStack> q = null;
        List<ItemStack> s = null;
        List<ItemStack> a = null;
        List<ItemStack> m = null;
        for (int i = 0; i < container.m_6643_(); i++) {
            ItemStack stack = container.m_8020_(i);
            if (stack.is((Item) BackpackItems.QUIVER.get())) {
                List<ItemStack> list = BaseBagItem.getItems(stack);
                if (list.size() == 9) {
                    q = list;
                }
            }
            if (stack.is((Item) BackpackItems.SCABBARD.get())) {
                List<ItemStack> list = BaseBagItem.getItems(stack);
                if (list.size() == 9) {
                    s = list;
                }
            }
            if (stack.is((Item) BackpackItems.ARMOR_SWAP.get())) {
                List<ItemStack> list = BaseBagItem.getItems(stack);
                if (list.size() == 9) {
                    a = list;
                }
            }
            if (stack.is((Item) BackpackItems.MULTI_SWITCH.get())) {
                List<ItemStack> list = BaseBagItem.getItems(stack);
                if (list.size() == 27) {
                    m = list;
                }
            }
        }
        if (m == null) {
            m = new ArrayList(27);
            for (int i = 0; i < 27; i++) {
                m.add(i, ItemStack.EMPTY);
            }
            if (q != null) {
                for (int i = 0; i < 9; i++) {
                    m.set(i, (ItemStack) q.get(i));
                }
            }
            if (s != null) {
                for (int i = 0; i < 9; i++) {
                    m.set(i + 9, (ItemStack) s.get(i));
                }
            }
            if (a != null) {
                for (int i = 0; i < 9; i++) {
                    m.set(i + 18, (ItemStack) a.get(i));
                }
            }
        }
        ItemStack ans = super.m_5874_(container, access);
        BaseBagItem.setItems(ans, m);
        return ans;
    }

    @Override
    public AbstractShapedRecipe.Serializer<MultiSwitchCraftRecipe> getSerializer() {
        return (AbstractShapedRecipe.Serializer<MultiSwitchCraftRecipe>) BackpackMisc.RSC_BAG_CRAFT.get();
    }
}