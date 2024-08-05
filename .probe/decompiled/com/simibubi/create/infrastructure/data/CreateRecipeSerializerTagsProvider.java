package com.simibubi.create.infrastructure.data;

import com.simibubi.create.AllTags;
import com.simibubi.create.compat.Mods;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class CreateRecipeSerializerTagsProvider extends TagsProvider<RecipeSerializer<?>> {

    public CreateRecipeSerializerTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.RECIPE_SERIALIZER, lookupProvider, "create", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.m_206424_(AllTags.AllRecipeSerializerTags.AUTOMATION_IGNORE.tag).addOptional(Mods.OCCULTISM.rl("spirit_trade")).addOptional(Mods.OCCULTISM.rl("ritual"));
        for (AllTags.AllRecipeSerializerTags tag : AllTags.AllRecipeSerializerTags.values()) {
            if (tag.alwaysDatagen) {
                this.m_236451_(tag.tag);
            }
        }
    }

    @Override
    public String getName() {
        return "Create's Recipe Serializer Tags";
    }
}