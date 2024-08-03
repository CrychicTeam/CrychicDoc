package dev.latvian.mods.kubejs.misc;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class VillagerProfessionBuilder extends BuilderBase<VillagerProfession> {

    public transient Either<ResourceKey<PoiType>, TagKey<PoiType>> poiType = Either.right(PoiTypeTags.ACQUIRABLE_JOB_SITE);

    public transient ImmutableSet<Item> requestedItems = ImmutableSet.of();

    public transient ImmutableSet<Block> secondaryPoi = ImmutableSet.of();

    @Nullable
    public transient SoundEvent workSound = null;

    public VillagerProfessionBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public final RegistryInfo getRegistryType() {
        return RegistryInfo.VILLAGER_PROFESSION;
    }

    public VillagerProfession createObject() {
        Predicate<Holder<PoiType>> validPois = holder -> (Boolean) this.poiType.map(holder::m_203565_, holder::m_203656_);
        return new VillagerProfession(this.id.getPath(), validPois, validPois, this.requestedItems, this.secondaryPoi, this.workSound);
    }

    public VillagerProfessionBuilder poiType(ResourceLocation t) {
        this.poiType = Either.left(ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, t));
        return this;
    }

    public VillagerProfessionBuilder poiTypeTag(ResourceLocation t) {
        this.poiType = Either.right(TagKey.create(Registries.POINT_OF_INTEREST_TYPE, t));
        return this;
    }

    public VillagerProfessionBuilder requestedItems(Item[] t) {
        this.requestedItems = ImmutableSet.copyOf(t);
        return this;
    }

    public VillagerProfessionBuilder secondaryPoi(Block[] t) {
        this.secondaryPoi = ImmutableSet.copyOf(t);
        return this;
    }

    public VillagerProfessionBuilder workSound(@Nullable SoundEvent t) {
        this.workSound = t;
        return this;
    }
}