package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.base.CrockPotDamageTypes;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrockPotDamageTypeTagsProvider extends DamageTypeTagsProvider {

    public CrockPotDamageTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, "crockpot", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.m_206424_(DamageTypeTags.BYPASSES_ARMOR).add(CrockPotDamageTypes.CANDY, CrockPotDamageTypes.MONSTER_FOOD, CrockPotDamageTypes.POW_CAKE, CrockPotDamageTypes.SPICY, CrockPotDamageTypes.TAFFY);
        this.m_206424_(DamageTypeTags.BYPASSES_EFFECTS).add(CrockPotDamageTypes.CANDY, CrockPotDamageTypes.MONSTER_FOOD, CrockPotDamageTypes.POW_CAKE, CrockPotDamageTypes.SPICY, CrockPotDamageTypes.TAFFY);
    }
}