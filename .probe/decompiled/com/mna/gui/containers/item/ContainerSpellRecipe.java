package com.mna.gui.containers.item;

import com.mna.api.tools.MATags;
import com.mna.gui.containers.ContainerInit;
import com.mna.items.ItemInit;
import com.mna.spells.crafting.SpellRecipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ContainerSpellRecipe extends AbstractContainerMenu {

    private List<List<ItemStack>> _cachedSpellRecipeReagents = new ArrayList();

    protected ContainerSpellRecipe(MenuType<?> type, int id) {
        super(type, id);
    }

    public ContainerSpellRecipe(int i, Inventory playerInv, FriendlyByteBuf buffer) {
        super(ContainerInit.SPELL_RECIPE_LIST.get(), i);
        if (playerInv.player.m_21205_().getItem() == ItemInit.ENCHANTED_VELLUM.get()) {
            this.pullRecipeReagentsFromStack(playerInv.player.m_21205_());
        } else if (playerInv.player.m_21206_().getItem() == ItemInit.ENCHANTED_VELLUM.get()) {
            this.pullRecipeReagentsFromStack(playerInv.player.m_21206_());
        }
    }

    private void pullRecipeReagentsFromStack(ItemStack stack) {
        HashMap<ResourceLocation, Integer> reagentIDsMerged = new HashMap();
        SpellRecipe.getShapeReagents(stack).stream().forEach(r -> {
            if (reagentIDsMerged.containsKey(r)) {
                reagentIDsMerged.put(r, (Integer) reagentIDsMerged.get(r) + 1);
            } else {
                reagentIDsMerged.put(r, 1);
            }
        });
        SpellRecipe.getComponentReagents(stack).stream().forEach(r -> {
            if (reagentIDsMerged.containsKey(r)) {
                reagentIDsMerged.put(r, (Integer) reagentIDsMerged.get(r) + 1);
            } else {
                reagentIDsMerged.put(r, 1);
            }
        });
        for (int i = 0; i < 3; i++) {
            SpellRecipe.getModifierReagents(stack, i).stream().forEach(r -> {
                if (reagentIDsMerged.containsKey(r)) {
                    reagentIDsMerged.put(r, (Integer) reagentIDsMerged.get(r) + 1);
                } else {
                    reagentIDsMerged.put(r, 1);
                }
            });
        }
        this._cachedSpellRecipeReagents.addAll(this.resolveReagents(reagentIDsMerged));
    }

    private NonNullList<List<ItemStack>> resolveReagents(HashMap<ResourceLocation, Integer> reagentIDs) {
        NonNullList<List<ItemStack>> resolved = NonNullList.create();
        for (ResourceLocation rLoc : reagentIDs.keySet()) {
            resolved.add((List) MATags.smartLookupItem(rLoc).stream().map(i -> new ItemStack(i, (Integer) reagentIDs.get(rLoc))).collect(Collectors.toList()));
        }
        return resolved;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public List<List<ItemStack>> getReagents() {
        return this._cachedSpellRecipeReagents;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }
}