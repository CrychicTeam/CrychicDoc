package com.simibubi.create.foundation.data;

import com.simibubi.create.AllDamageTypes;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class DamageTypeTagGen extends TagsProvider<DamageType> {

    public DamageTypeTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, "create", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.m_206424_(DamageTypeTags.BYPASSES_ARMOR).add(AllDamageTypes.CRUSH, AllDamageTypes.FAN_FIRE, AllDamageTypes.FAN_LAVA, AllDamageTypes.DRILL, AllDamageTypes.SAW);
        this.m_206424_(DamageTypeTags.IS_FIRE).add(AllDamageTypes.FAN_FIRE, AllDamageTypes.FAN_LAVA);
        this.m_206424_(DamageTypeTags.IS_EXPLOSION).add(AllDamageTypes.CUCKOO_SURPRISE);
    }

    @Override
    public String getName() {
        return "Create's Damage Type Tags";
    }
}