package io.github.lightman314.lightmanscurrency.datagen.common.tags;

import io.github.lightman314.lightmanscurrency.common.villager_merchant.CustomPointsOfInterest;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class LCPoiTagProvider extends PoiTypeTagsProvider {

    public LCPoiTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "lightmanscurrency", existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider lookup) {
        this.m_206424_(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(CustomPointsOfInterest.BANKER_KEY).add(CustomPointsOfInterest.CASHIER_KEY);
    }
}