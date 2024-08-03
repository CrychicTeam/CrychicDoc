package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.IafProcessorLists;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "iceandfire", bus = Bus.MOD)
public class IafVillagerRegistry {

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, "iceandfire");

    public static final RegistryObject<PoiType> SCRIBE_POI = POI_TYPES.register("scribe", () -> new PoiType(ImmutableSet.copyOf(IafBlockRegistry.LECTERN.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, "iceandfire");

    public static final RegistryObject<VillagerProfession> SCRIBE = PROFESSIONS.register("scribe", () -> new VillagerProfession("scribe", entry -> ((PoiType) entry.value()).equals(SCRIBE_POI.get()), entry -> ((PoiType) entry.value()).equals(SCRIBE_POI.get()), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_LIBRARIAN));

    public static void addScribeTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        float emeraldForItemsMultiplier = 0.05F;
        float itemForEmeraldMultiplier = 0.05F;
        float rareItemForEmeraldMultiplier = 0.2F;
        ((List) trades.get(1)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 1), new ItemStack(IafItemRegistry.MANUSCRIPT.get(), 4), 25, 2, 0.05F));
        ((List) trades.get(1)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.BOOKSHELF, 3), new ItemStack(Items.EMERALD, 1), 8, 3, 0.05F));
        ((List) trades.get(1)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.PAPER, 15), new ItemStack(Items.EMERALD, 2), 4, 4, 0.05F));
        ((List) trades.get(1)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(IafBlockRegistry.ASH.get(), 10), new ItemStack(Items.EMERALD, 1), 8, 4, 0.05F));
        ((List) trades.get(2)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(IafItemRegistry.SILVER_INGOT.get(), 5), new ItemStack(Items.EMERALD, 1), 3, 5, 0.05F));
        ((List) trades.get(2)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(IafBlockRegistry.FIRE_LILY.get(), 8), new ItemStack(Items.EMERALD, 1), 3, 5, 0.05F));
        ((List) trades.get(2)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(IafBlockRegistry.LIGHTNING_LILY.get(), 7), new ItemStack(Items.EMERALD, 3), 2, 5, 0.05F));
        ((List) trades.get(2)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(IafBlockRegistry.FROST_LILY.get(), 4), 3, 3, 0.05F));
        ((List) trades.get(2)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 2), new ItemStack(IafBlockRegistry.DRAGON_ICE_SPIKES.get(), 7), 2, 3, 0.05F));
        ((List) trades.get(2)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(IafItemRegistry.SAPPHIRE_GEM.get()), new ItemStack(Items.EMERALD, 2), 30, 3, 0.2F));
        ((List) trades.get(2)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 2), new ItemStack(IafBlockRegistry.JAR_EMPTY.get(), 1), 3, 4, 0.05F));
        ((List) trades.get(2)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 2), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN.get(), 1), 40, 2, 0.05F));
        ((List) trades.get(2)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 2), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN.get(), 1), 40, 2, 0.05F));
        ((List) trades.get(2)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.AMETHYST_SHARD), new ItemStack(Items.EMERALD, 3), 20, 3, 0.2F));
        ((List) trades.get(3)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(IafItemRegistry.DRAGON_BONE.get(), 6), new ItemStack(Items.EMERALD, 1), 7, 4, 0.05F));
        ((List) trades.get(3)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(IafItemRegistry.CHAIN.get(), 2), new ItemStack(Items.EMERALD, 3), 4, 2, 0.05F));
        ((List) trades.get(3)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 6), new ItemStack(IafItemRegistry.PIXIE_DUST.get(), 2), 8, 3, 0.05F));
        ((List) trades.get(3)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 6), new ItemStack(IafItemRegistry.FIRE_DRAGON_FLESH.get(), 2), 8, 3, 0.05F));
        ((List) trades.get(3)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 7), new ItemStack(IafItemRegistry.ICE_DRAGON_FLESH.get(), 1), 8, 3, 0.05F));
        ((List) trades.get(3)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 8), new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_FLESH.get(), 1), 8, 3, 0.05F));
        ((List) trades.get(4)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 10), new ItemStack(IafItemRegistry.DRAGON_BONE.get(), 2), 20, 5, 0.05F));
        ((List) trades.get(4)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 4), new ItemStack(IafItemRegistry.SHINY_SCALES.get(), 1), 5, 2, 0.05F));
        ((List) trades.get(4)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(IafItemRegistry.DREAD_SHARD.get(), 5), new ItemStack(Items.EMERALD, 1), 10, 4, 0.05F));
        ((List) trades.get(4)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 8), new ItemStack(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER.get(), 12), 3, 6, 0.05F));
        ((List) trades.get(4)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 4), new ItemStack(IafItemRegistry.TROLL_TUSK.get(), 12), 7, 3, 0.05F));
        ((List) trades.get(5)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 15), new ItemStack(IafItemRegistry.SERPENT_FANG.get(), 3), 20, 3, 0.05F));
        ((List) trades.get(5)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 12), new ItemStack(IafItemRegistry.HYDRA_FANG.get(), 1), 20, 3, 0.05F));
        ((List) trades.get(5)).add((VillagerTrades.ItemListing) (entity, random) -> new MerchantOffer(new ItemStack(IafItemRegistry.ECTOPLASM.get(), 6), new ItemStack(Items.EMERALD, 1), 7, 3, 0.05F));
    }

    public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
        Holder<StructureProcessorList> villageHouseProcessorList = processorListRegistry.getHolderOrThrow(IafProcessorLists.HOUSE_PROCESSOR);
        StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
        if (pool != null) {
            SinglePoolElement piece = (SinglePoolElement) SinglePoolElement.m_210512_(nbtPieceRL, villageHouseProcessorList).apply(StructureTemplatePool.Projection.RIGID);
            for (int i = 0; i < weight; i++) {
                pool.templates.add(piece);
            }
            List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList(pool.rawTemplates);
            listOfPieceEntries.add(new Pair(piece, weight));
            pool.rawTemplates = listOfPieceEntries;
        }
    }
}