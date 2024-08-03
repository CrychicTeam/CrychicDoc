package fr.lucreeper74.createmetallurgy.data.recipes;

import com.google.common.collect.BiMap;
import fr.lucreeper74.createmetallurgy.registries.CMRecipeTypes;
import java.util.HashSet;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraftforge.registries.ForgeRegistries;

public class GrindingRecipeGen extends CMProcessingRecipesGen {

    CMRecipeProvider.GeneratedRecipe ALL_COPPER_BLOCKS = this.deoxidized();

    CMRecipeProvider.GeneratedRecipe ALL_WAXED_COPPER_BLOCKS = this.unwaxed();

    CMRecipeProvider.GeneratedRecipe deoxidized() {
        for (Block current : ((BiMap) WeatheringCopper.NEXT_BY_BLOCK.get()).values()) {
            Block previous = (Block) WeatheringCopper.getPrevious(current).get();
            this.create(ForgeRegistries.BLOCKS.getKey(current).getPath(), b -> b.duration(50).require(current).output(previous));
        }
        return null;
    }

    CMRecipeProvider.GeneratedRecipe unwaxed() {
        for (Block normal : new HashSet<Block>() {

            {
                this.addAll(((BiMap) WeatheringCopper.NEXT_BY_BLOCK.get()).values());
                this.addAll(((BiMap) WeatheringCopper.PREVIOUS_BY_BLOCK.get()).values());
            }
        }) {
            String waxedName = "waxed_" + ForgeRegistries.BLOCKS.getKey(normal).getPath();
            String modId = normal.toString().substring(6, normal.toString().indexOf(":"));
            Block waxed = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(modId, waxedName));
            this.create(waxedName, b -> b.duration(50).require(waxed).output(normal));
        }
        return null;
    }

    public GrindingRecipeGen(PackOutput output) {
        super(output);
    }

    protected CMRecipeTypes getRecipeType() {
        return CMRecipeTypes.GRINDING;
    }
}