package team.lodestar.lodestone.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import team.lodestar.lodestone.registry.common.tag.LodestoneDamageTypeTags;

public class LodestoneDamageTypeDatagen extends DamageTypeTagsProvider {

    public LodestoneDamageTypeDatagen(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, "lodestone", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.m_206424_(LodestoneDamageTypeTags.IS_MAGIC).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC);
    }
}