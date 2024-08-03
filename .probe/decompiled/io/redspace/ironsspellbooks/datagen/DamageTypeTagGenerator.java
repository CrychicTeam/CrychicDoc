package io.redspace.ironsspellbooks.datagen;

import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class DamageTypeTagGenerator extends TagsProvider<DamageType> {

    public static final TagKey<DamageType> BYPASS_EVASION = create("bypass_evasion");

    public static final TagKey<DamageType> LONG_CAST_IGNORE = create("long_cast_ignore");

    public static final TagKey<DamageType> FIRE_MAGIC = create("fire_magic");

    public static final TagKey<DamageType> ICE_MAGIC = create("ice_magic");

    public static final TagKey<DamageType> LIGHTNING_MAGIC = create("lightning_magic");

    public static final TagKey<DamageType> HOLY_MAGIC = create("holy_magic");

    public static final TagKey<DamageType> ENDER_MAGIC = create("ender_magic");

    public static final TagKey<DamageType> BLOOD_MAGIC = create("blood_magic");

    public static final TagKey<DamageType> EVOCATION_MAGIC = create("evocation_magic");

    public static final TagKey<DamageType> ELDRITCH_MAGIC = create("eldritch_magic");

    public static final TagKey<DamageType> NATURE_MAGIC = create("nature_magic");

    public DamageTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, "irons_spellbooks", existingFileHelper);
    }

    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("irons_spellbooks", name));
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        this.m_206424_(FIRE_MAGIC).add(ISSDamageTypes.FIRE_MAGIC);
        this.m_206424_(ICE_MAGIC).add(ISSDamageTypes.ICE_MAGIC);
        this.m_206424_(LIGHTNING_MAGIC).add(ISSDamageTypes.LIGHTNING_MAGIC);
        this.m_206424_(HOLY_MAGIC).add(ISSDamageTypes.HOLY_MAGIC);
        this.m_206424_(ENDER_MAGIC).add(ISSDamageTypes.ENDER_MAGIC);
        this.m_206424_(BLOOD_MAGIC).add(ISSDamageTypes.BLOOD_MAGIC);
        this.m_206424_(EVOCATION_MAGIC).add(ISSDamageTypes.EVOCATION_MAGIC);
        this.m_206424_(ELDRITCH_MAGIC).add(ISSDamageTypes.ELDRITCH_MAGIC);
        this.m_206424_(NATURE_MAGIC).add(ISSDamageTypes.NATURE_MAGIC);
        this.m_206424_(BYPASS_EVASION).add(DamageTypes.ON_FIRE, DamageTypes.WITHER, DamageTypes.FREEZE, DamageTypes.STARVE, DamageTypes.DROWN, DamageTypes.STALAGMITE, DamageTypes.OUTSIDE_BORDER, DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.DRY_OUT, ISSDamageTypes.CAULDRON);
        this.m_206424_(LONG_CAST_IGNORE).add(DamageTypes.FREEZE, DamageTypes.STARVE, DamageTypes.ON_FIRE, DamageTypes.WITHER);
    }
}