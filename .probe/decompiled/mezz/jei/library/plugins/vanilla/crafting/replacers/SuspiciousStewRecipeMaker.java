package mezz.jei.library.plugins.vanilla.crafting.replacers;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;

public final class SuspiciousStewRecipeMaker {

    public static List<CraftingRecipe> createRecipes() {
        String group = "jei.suspicious.stew";
        Ingredient brownMushroom = Ingredient.of(Blocks.BROWN_MUSHROOM.asItem());
        Ingredient redMushroom = Ingredient.of(Blocks.RED_MUSHROOM.asItem());
        Ingredient bowl = Ingredient.of(Items.BOWL);
        return BuiltInRegistries.ITEM.m_203431_(ItemTags.SMALL_FLOWERS).stream().flatMap(HolderSet.ListBacked::m_203614_).map(Holder::m_203334_).filter(BlockItem.class::isInstance).map(item -> ((BlockItem) item).getBlock()).filter(FlowerBlock.class::isInstance).map(FlowerBlock.class::cast).map(flowerBlock -> {
            Ingredient flower = Ingredient.of(flowerBlock.m_5456_());
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, brownMushroom, redMushroom, bowl, flower);
            ItemStack output = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            MobEffect mobeffect = flowerBlock.getSuspiciousEffect();
            SuspiciousStewItem.saveMobEffect(output, mobeffect, flowerBlock.getEffectDuration());
            ResourceLocation id = new ResourceLocation("minecraft", "jei.suspicious.stew." + flowerBlock.m_7705_());
            return new ShapelessRecipe(id, group, CraftingBookCategory.MISC, output, inputs);
        }).toList();
    }

    private SuspiciousStewRecipeMaker() {
    }
}