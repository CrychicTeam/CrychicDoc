package com.mna.apibridge;

import com.mna.api.items.IItemHelper;
import com.mna.items.sorcery.ItemSpellBook;
import com.mna.items.sorcery.ItemSpellGrimoire;
import com.mna.items.sorcery.ItemStaff;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemHelper implements IItemHelper {

    @Override
    public Item CreateStaff(Item.Properties properties, float attackDamage, float attackSpeed) {
        return new ItemStaff(properties, attackDamage, attackSpeed);
    }

    @Override
    public Item CreateGrimoire(Item.Properties properties, @Nullable ResourceLocation faction, @Nullable ResourceLocation openModel, @Nullable ResourceLocation closedModel, boolean renderBookInFirstPerson) {
        return new ItemSpellGrimoire(properties, faction, openModel, closedModel, renderBookInFirstPerson);
    }

    @Override
    public Item CreateSpellBook(Item.Properties properties, @Nullable ResourceLocation openModel, @Nullable ResourceLocation closedModel, boolean renderBookInFirstPerson) {
        return new ItemSpellBook(properties, openModel, closedModel, renderBookInFirstPerson);
    }

    @Override
    public boolean AreTagsEqual(ItemStack a, ItemStack b) {
        CompoundTag ta = a.getTag();
        CompoundTag tb = b.getTag();
        return ta != null && !ta.isEmpty() || tb != null && !tb.isEmpty() ? Objects.equals(ta, tb) : true;
    }
}