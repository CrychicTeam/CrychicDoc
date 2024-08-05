package dev.xkmc.modulargolems.content.recipe;

import dev.xkmc.l2library.serial.recipe.AbstractShapedRecipe;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.init.registrate.GolemMiscs;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class GolemAssembleRecipe extends AbstractShapedRecipe<GolemAssembleRecipe> {

    public GolemAssembleRecipe(ResourceLocation id, String group, int w, int h, NonNullList<Ingredient> ings, ItemStack result) {
        super(id, group, w, h, ings, result);
    }

    @Override
    public boolean matches(CraftingContainer cont, Level level) {
        if (!super.m_5818_(cont, level)) {
            return false;
        } else {
            for (int i = 0; i < cont.m_6643_(); i++) {
                ItemStack input = cont.m_8020_(i);
                if (!input.isEmpty() && input.getItem() instanceof GolemPart part && GolemPart.getMaterial(input).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public ItemStack assemble(CraftingContainer cont, RegistryAccess access) {
        ItemStack stack = super.m_5874_(cont, access);
        for (int i = 0; i < cont.m_6643_(); i++) {
            ItemStack input = cont.m_8020_(i);
            if (!input.isEmpty() && input.getItem() instanceof GolemPart part) {
                GolemPart.getMaterial(input).ifPresent(mat -> GolemHolder.addMaterial(stack, part, mat));
            }
        }
        return stack;
    }

    @Override
    public AbstractShapedRecipe.Serializer<GolemAssembleRecipe> getSerializer() {
        return (AbstractShapedRecipe.Serializer<GolemAssembleRecipe>) GolemMiscs.ASSEMBLE.get();
    }
}