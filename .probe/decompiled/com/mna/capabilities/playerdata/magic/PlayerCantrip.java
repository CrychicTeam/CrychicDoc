package com.mna.capabilities.playerdata.magic;

import com.mna.api.cantrips.ICantrip;
import com.mna.api.capabilities.IPlayerCantrip;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PlayerCantrip implements IPlayerCantrip {

    private ResourceLocation cantripID;

    private ItemStack stack;

    private boolean itemLocked = false;

    private ArrayList<ResourceLocation> combination;

    public PlayerCantrip(ICantrip cantrip) {
        this.cantripID = cantrip.getId();
        this.combination = new ArrayList();
        this.stack = cantrip.getDefaultStack();
        this.itemLocked = cantrip.isStackLocked();
        cantrip.getDefaultCombination().forEach(r -> this.combination.add(r));
    }

    @Override
    public ResourceLocation getCantripID() {
        return this.cantripID;
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public CompoundTag writeToNBT() {
        CompoundTag nbt = new CompoundTag();
        if (!this.stack.isEmpty()) {
            CompoundTag item = new CompoundTag();
            this.stack.save(item);
            nbt.put("item", item);
        }
        nbt.putInt("count", this.combination.size());
        for (int i = 0; i < this.combination.size(); i++) {
            nbt.putString(i + "", ((ResourceLocation) this.combination.get(i)).toString());
        }
        return nbt;
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        if (nbt.contains("item") && !this.itemLocked) {
            this.stack = ItemStack.of(nbt.getCompound("item"));
        }
        this.combination.clear();
        if (nbt.contains("count")) {
            int count = nbt.getInt("count");
            for (int i = 0; i < count; i++) {
                ResourceLocation rLoc = new ResourceLocation(nbt.getString(i + ""));
                this.combination.add(rLoc);
            }
        }
    }

    @Override
    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ResourceLocation getPattern(int index) {
        return index >= 0 && index < this.combination.size() ? (ResourceLocation) this.combination.get(index) : null;
    }

    @Override
    public void setPatterns(List<ResourceLocation> manaweavingPatterns) {
        this.combination.clear();
        this.combination.addAll(manaweavingPatterns);
    }

    @Override
    public boolean matches(List<ManaweavingPattern> patterns) {
        if (patterns.size() != this.combination.size()) {
            return false;
        } else {
            for (int i = 0; i < this.combination.size(); i++) {
                if (!((ManaweavingPattern) patterns.get(i)).m_6423_().equals(this.combination.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }
}