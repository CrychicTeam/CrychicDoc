package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.datagen.IafPOITypes;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.common.data.ExistingFileHelper;

public class POITagGenerator extends PoiTypeTagsProvider {

    public POITagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, "iceandfire", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.m_206424_(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(IafPOITypes.SCRIBE_POI);
    }

    private static TagKey<PoiType> create(String name) {
        return TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation("iceandfire", name));
    }

    @Override
    public String getName() {
        return "Ice and Fire POI Type Tags";
    }
}