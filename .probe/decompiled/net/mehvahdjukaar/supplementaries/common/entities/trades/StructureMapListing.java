package net.mehvahdjukaar.supplementaries.common.entities.trades;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.mehvahdjukaar.moonlight.api.misc.StrOpt;
import net.mehvahdjukaar.moonlight.api.trades.ModItemListing;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

public record StructureMapListing(Item cost, int minPrice, int maxPrice, ItemStack cost2, HolderSet<Structure> structure, int maxTrades, float priceMult, int level, String mapName, int mapColor, ResourceLocation mapMarker) implements ModItemListing {

    private static final Codec<HolderSet<Structure>> TARGET_CODEC = Codec.either(RegistryCodecs.homogeneousList(Registries.STRUCTURE, true), Structure.CODEC).xmap(either -> (HolderSet) either.map(Function.identity(), xva$0 -> HolderSet.direct(xva$0)), Either::left);

    public static final Codec<StructureMapListing> CODEC = RecordCodecBuilder.create(i -> i.group(BuiltInRegistries.ITEM.m_194605_().fieldOf("item").forGetter(StructureMapListing::cost), StrOpt.of(ExtraCodecs.POSITIVE_INT, "price_min", 7).forGetter(StructureMapListing::minPrice), StrOpt.of(ExtraCodecs.POSITIVE_INT, "price_max", 13).forGetter(StructureMapListing::maxPrice), StrOpt.of(ItemStack.CODEC, "price_secondary", ItemStack.EMPTY).forGetter(StructureMapListing::cost2), TARGET_CODEC.fieldOf("structure").forGetter(p -> p.structure), StrOpt.of(ExtraCodecs.POSITIVE_INT, "max_trades", 16).forGetter(StructureMapListing::maxTrades), StrOpt.of(ExtraCodecs.POSITIVE_FLOAT, "price_multiplier", 0.05F).forGetter(StructureMapListing::priceMult), StrOpt.of(Codec.intRange(1, 5), "level", 1).forGetter(StructureMapListing::level), StrOpt.of(Codec.STRING, "map_name", "").forGetter(p -> p.mapName), StrOpt.of(ColorUtils.CODEC, "map_color", 16777215).forGetter(p -> p.mapColor), StrOpt.of(ResourceLocation.CODEC, "map_marker", new ResourceLocation("")).forGetter(p -> p.mapMarker)).apply(i, StructureMapListing::new));

    @Override
    public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource random) {
        ItemStack itemstack = AdventurerMapsHandler.createCustomMapForTrade(entity.level(), entity.blockPosition(), this.structure, this.mapName.isEmpty() ? null : this.mapName, this.mapColor, this.mapMarker.getPath().isEmpty() ? null : this.mapMarker);
        if (itemstack.isEmpty()) {
            return null;
        } else {
            int i = Math.max(1, random.nextInt(Math.max(1, this.maxPrice - this.minPrice)) + this.minPrice);
            return new MerchantOffer(new ItemStack(this.cost, i), this.cost2, itemstack, this.maxTrades, ModItemListing.defaultXp(false, this.level), this.priceMult);
        }
    }

    @Override
    public Codec<? extends ModItemListing> getCodec() {
        return CODEC;
    }

    @Override
    public int getLevel() {
        return this.level;
    }
}