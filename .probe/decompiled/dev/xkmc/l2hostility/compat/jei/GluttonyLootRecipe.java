package dev.xkmc.l2hostility.compat.jei;

import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record GluttonyLootRecipe() implements ITraitLootRecipe {

    @Override
    public List<ItemStack> getResults() {
        return List.of(LHItems.BOTTLE_CURSE.asStack());
    }

    @Override
    public List<ItemStack> getCurioRequired() {
        return List.of(LHItems.CURSE_GLUTTONY.asStack());
    }

    @Override
    public List<ItemStack> getInputs() {
        return List.of();
    }

    @Override
    public void addTooltip(List<Component> l) {
        l.add(LangData.TOOLTIP_JEI_GLUTTONY.get().withStyle(ChatFormatting.YELLOW));
    }
}