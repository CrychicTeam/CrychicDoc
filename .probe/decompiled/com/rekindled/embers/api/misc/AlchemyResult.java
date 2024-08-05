package com.rekindled.embers.api.misc;

import com.rekindled.embers.recipe.IAlchemyRecipe;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class AlchemyResult {

    public List<IAlchemyRecipe.PedestalContents> contents;

    public ItemStack result;

    public int blackPins;

    public int whitePins;

    public AlchemyResult(List<IAlchemyRecipe.PedestalContents> contents, ItemStack result, int blackPins, int whitePins) {
        this.contents = contents;
        this.result = result;
        this.blackPins = blackPins;
        this.whitePins = whitePins;
    }

    public ItemStack createResultStack(ItemStack stack) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("blackPins", this.blackPins);
        nbt.putInt("whitePins", this.whitePins);
        nbt.put("result", this.result.serializeNBT());
        ListTag aspectNBT = new ListTag();
        ListTag inputNBT = new ListTag();
        for (IAlchemyRecipe.PedestalContents contents : this.contents) {
            aspectNBT.add(contents.aspect.serializeNBT());
            inputNBT.add(contents.input.serializeNBT());
        }
        nbt.put("aspects", aspectNBT);
        nbt.put("inputs", inputNBT);
        stack.setTag(nbt);
        return stack;
    }
}