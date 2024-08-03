package com.github.alexthe666.iceandfire.datagen.tags;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class IafEntityTags extends EntityTypeTagsProvider {

    public static TagKey<EntityType<?>> IMMUNE_TO_GORGON_STONE = createKey("immune_to_gorgon_stone");

    public IafEntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, "iceandfire", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.m_206424_(IMMUNE_TO_GORGON_STONE).addTag(Tags.EntityTypes.BOSSES).add(EntityType.WARDEN);
    }

    private static TagKey<EntityType<?>> createKey(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("iceandfire", name));
    }
}