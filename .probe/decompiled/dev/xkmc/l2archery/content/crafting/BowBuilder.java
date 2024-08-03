package dev.xkmc.l2archery.content.crafting;

import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2library.serial.recipe.CustomShapedBuilder;
import net.minecraft.world.level.ItemLike;

public class BowBuilder extends CustomShapedBuilder<BowRecipe> {

    public BowBuilder(ItemLike result, int count) {
        super(ArcheryRegister.BOW_RECIPE, result, count);
    }
}