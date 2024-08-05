package com.rekindled.embers.datagen;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.compat.curios.CuriosCompat;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class EmbersBlockTags extends BlockTagsProvider {

    public static final TagKey<Block> EMITTER_CONNECTION = BlockTags.create(new ResourceLocation("embers", "emitter_connection"));

    public static final TagKey<Block> EMITTER_CONNECTION_FLOOR = BlockTags.create(new ResourceLocation("embers", "emitter_connection/floor"));

    public static final TagKey<Block> EMITTER_CONNECTION_CEILING = BlockTags.create(new ResourceLocation("embers", "emitter_connection/ceiling"));

    public static final TagKey<Block> ITEM_PIPE_CONNECTION = BlockTags.create(new ResourceLocation("embers", "item_pipe_connection"));

    public static final TagKey<Block> ITEM_PIPE_CONNECTION_TOGGLEABLE = BlockTags.create(new ResourceLocation("embers", "item_pipe_connection/toggleable"));

    public static final TagKey<Block> FLUID_PIPE_CONNECTION = BlockTags.create(new ResourceLocation("embers", "fluid_pipe_connection"));

    public static final TagKey<Block> FLUID_PIPE_CONNECTION_TOGGLEABLE = BlockTags.create(new ResourceLocation("embers", "fluid_pipe_connection/toggleable"));

    public static final TagKey<Block> RELOCATION_NOT_SUPPORTED = BlockTags.create(new ResourceLocation("forge", "relocation_not_supported"));

    public static final TagKey<Block> MECH_CORE_PROXYABLE = BlockTags.create(new ResourceLocation("embers", "mech_core_proxyable"));

    public static final TagKey<Block> MECH_CORE_PROXYABLE_BOTTOM = BlockTags.create(new ResourceLocation("embers", "mech_core_proxyable/bottom"));

    public static final TagKey<Block> MECH_CORE_PROXYABLE_TOP = BlockTags.create(new ResourceLocation("embers", "mech_core_proxyable/top"));

    public static final TagKey<Block> HEAT_SOURCES = BlockTags.create(new ResourceLocation("embers", "heat_sources"));

    public static final TagKey<Block> RESERVOIR_EXPANSION = BlockTags.create(new ResourceLocation("embers", "reservoir_expansion"));

    public static final TagKey<Block> CHAMBER_CONNECTION = BlockTags.create(new ResourceLocation("embers", "chamber_connection"));

    public static final TagKey<Block> DIAL = BlockTags.create(new ResourceLocation("embers", "dial"));

    public static final TagKey<Block> MINABLE_WITH_PICKAXE_SHOVEL = BlockTags.create(new ResourceLocation("embers", "mineable/pickaxe_shovel"));

    public static final TagKey<Block> MINABLE_WITH_HAMMER = BlockTags.create(new ResourceLocation("embers", "mineable/hammer"));

    public static final TagKey<Block> NEEDS_LEAD_TOOL = BlockTags.create(new ResourceLocation("embers", "needs_lead_tool"));

    public static final TagKey<Block> NEEDS_TYRFING = BlockTags.create(new ResourceLocation("embers", "needs_tyrfing"));

    public static final TagKey<Block> NEEDS_SILVER_TOOL = BlockTags.create(new ResourceLocation("embers", "needs_silver_tool"));

    public static final TagKey<Block> NEEDS_DAWNSTONE_TOOL = BlockTags.create(new ResourceLocation("embers", "needs_dawnstone_tool"));

    public static final TagKey<Block> NEEDS_CLOCKWORK_TOOL = BlockTags.create(new ResourceLocation("embers", "needs_clockwork_tool"));

    public static final TagKey<Block> NEEDS_CLOCKWORK_HAMMER = BlockTags.create(new ResourceLocation("embers", "needs_clockwork_hammer"));

    public static final TagKey<Block> WORLD_BOTTOM = BlockTags.create(new ResourceLocation("embers", "world_bottom"));

    public static final TagKey<Block> SNOW = BlockTags.create(new ResourceLocation("embers", "snow"));

    public static final TagKey<Block> PRISTINE_COPPER = BlockTags.create(new ResourceLocation("embers", "pristine_copper"));

    public static final TagKey<Block> EXPOSED_COPPER = BlockTags.create(new ResourceLocation("embers", "exposed_copper"));

    public static final TagKey<Block> WEATHERED_COPPER = BlockTags.create(new ResourceLocation("embers", "weathered_copper"));

    public static final TagKey<Block> OXIDIZED_COPPER = BlockTags.create(new ResourceLocation("embers", "oxidized_copper"));

    public static final TagKey<Block> LEAD_ORE = BlockTags.create(new ResourceLocation("forge", "ores/lead"));

    public static final TagKey<Block> RAW_LEAD_BLOCK = BlockTags.create(new ResourceLocation("forge", "storage_blocks/raw_lead"));

    public static final TagKey<Block> SILVER_ORE = BlockTags.create(new ResourceLocation("forge", "ores/silver"));

    public static final TagKey<Block> RAW_SILVER_BLOCK = BlockTags.create(new ResourceLocation("forge", "storage_blocks/raw_silver"));

    public static final TagKey<Block> LEAD_BLOCK = BlockTags.create(new ResourceLocation("forge", "storage_blocks/lead"));

    public static final TagKey<Block> SILVER_BLOCK = BlockTags.create(new ResourceLocation("forge", "storage_blocks/silver"));

    public static final TagKey<Block> DAWNSTONE_BLOCK = BlockTags.create(new ResourceLocation("forge", "storage_blocks/dawnstone"));

    public static final TagKey<Block> CRYSTAL_SEEDS = BlockTags.create(new ResourceLocation("embers", "crystal_seeds"));

    public static final TagKey<Block> COPPER_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/copper"));

    public static final TagKey<Block> IRON_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/iron"));

    public static final TagKey<Block> GOLD_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/gold"));

    public static final TagKey<Block> LEAD_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/lead"));

    public static final TagKey<Block> SILVER_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/silver"));

    public static final TagKey<Block> NICKEL_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/nickel"));

    public static final TagKey<Block> TIN_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/tin"));

    public static final TagKey<Block> ALUMINUM_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/aluminum"));

    public static final TagKey<Block> ZINC_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/zinc"));

    public static final TagKey<Block> PLATINUM_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/platinum"));

    public static final TagKey<Block> URANIUM_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/uranium"));

    public static final TagKey<Block> DAWNSTONE_SEED = BlockTags.create(new ResourceLocation("embers", "crystal_seeds/dawnstone"));

    public static final TagKey<Block> BRONZE_BLOCK = BlockTags.create(new ResourceLocation("forge", "storage_blocks/bronze"));

    public static final TagKey<Block> NICKEL_BLOCK = BlockTags.create(new ResourceLocation("forge", "storage_blocks/nickel"));

    public static final TagKey<Block> TIN_BLOCK = BlockTags.create(new ResourceLocation("forge", "storage_blocks/tin"));

    public static final TagKey<Block> ALUMINUM_BLOCK = BlockTags.create(new ResourceLocation("forge", "storage_blocks/aluminium"));

    public static final TagKey<Block> ELECTRUM_BLOCK = BlockTags.create(new ResourceLocation("forge", "storage_blocks/electrum"));

    public EmbersBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "embers", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.m_206424_(EMITTER_CONNECTION).add(Blocks.LEVER, Blocks.LADDER, Blocks.IRON_BARS, Blocks.TRIPWIRE_HOOK, Blocks.WALL_TORCH, Blocks.SOUL_WALL_TORCH, Blocks.REDSTONE_WALL_TORCH, Blocks.BELL, Blocks.LANTERN, Blocks.SOUL_LANTERN, Blocks.END_ROD, Blocks.LIGHTNING_ROD, Blocks.CHAIN).add(RegistryManager.EMBER_DIAL.get(), RegistryManager.CAMINITE_LEVER.get(), RegistryManager.EMBER_EMITTER.get(), RegistryManager.EMBER_RECEIVER.get(), RegistryManager.EMBER_EJECTOR.get(), RegistryManager.EMBER_FUNNEL.get(), RegistryManager.EMBER_RELAY.get(), RegistryManager.MIRROR_RELAY.get(), RegistryManager.BEAM_SPLITTER.get()).addTags(new TagKey[] { Tags.Blocks.GLASS_PANES, BlockTags.BUTTONS, Tags.Blocks.FENCES, BlockTags.WALLS, BlockTags.WALL_SIGNS }).addTags(new TagKey[] { EMITTER_CONNECTION_FLOOR, EMITTER_CONNECTION_CEILING, DIAL });
        this.m_206424_(EMITTER_CONNECTION_FLOOR).add(Blocks.TORCH, Blocks.SOUL_TORCH, Blocks.REDSTONE_TORCH, Blocks.POINTED_DRIPSTONE).addTags(new TagKey[] { BlockTags.STANDING_SIGNS });
        this.m_206424_(EMITTER_CONNECTION_CEILING).add(Blocks.POINTED_DRIPSTONE).addTag(BlockTags.CEILING_HANGING_SIGNS);
        this.m_206424_(ITEM_PIPE_CONNECTION).addTag(ITEM_PIPE_CONNECTION_TOGGLEABLE);
        this.m_206424_(ITEM_PIPE_CONNECTION_TOGGLEABLE).add(RegistryManager.ITEM_PIPE.get(), RegistryManager.ITEM_EXTRACTOR.get());
        this.m_206424_(BlockTags.BUTTONS).add(RegistryManager.CAMINITE_BUTTON.get());
        this.decoTags(RegistryManager.CAMINITE_BRICKS_DECO);
        this.decoTags(RegistryManager.CAMINITE_LARGE_BRICKS_DECO);
        this.decoTags(RegistryManager.CAMINITE_LARGE_TILE_DECO);
        this.decoTags(RegistryManager.CAMINITE_TILES_DECO);
        this.decoTags(RegistryManager.ARCHAIC_BRICKS_DECO);
        this.decoTags(RegistryManager.ARCHAIC_TILE_DECO);
        this.decoTags(RegistryManager.ARCHAIC_LARGE_BRICKS_DECO);
        this.decoTags(RegistryManager.ASHEN_STONE_DECO);
        this.decoTags(RegistryManager.ASHEN_BRICK_DECO);
        this.decoTags(RegistryManager.ASHEN_TILE_DECO);
        this.woodDecoTags(RegistryManager.SEALED_PLANKS_DECO);
        this.woodDecoTags(RegistryManager.SEALED_WOOD_TILE_DECO);
        this.decoTags(RegistryManager.METAL_PLATFORM_DECO);
        this.m_206424_(MINABLE_WITH_PICKAXE_SHOVEL).addTags(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL });
        this.m_206424_(MINABLE_WITH_HAMMER).addTags(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_AXE });
        this.m_206424_(BlockTags.MINEABLE_WITH_PICKAXE).add(RegistryManager.LEAD_ORE.get(), RegistryManager.DEEPSLATE_LEAD_ORE.get(), RegistryManager.RAW_LEAD_BLOCK.get(), RegistryManager.LEAD_BLOCK.get(), RegistryManager.SILVER_ORE.get(), RegistryManager.DEEPSLATE_SILVER_ORE.get(), RegistryManager.RAW_SILVER_BLOCK.get(), RegistryManager.SILVER_BLOCK.get(), RegistryManager.DAWNSTONE_BLOCK.get(), RegistryManager.MITHRIL_BLOCK.get(), RegistryManager.CAMINITE_BRICKS.get(), RegistryManager.CAMINITE_LARGE_BRICKS.get(), RegistryManager.CAMINITE_LARGE_TILE.get(), RegistryManager.CAMINITE_TILES.get(), RegistryManager.ARCHAIC_BRICKS.get(), RegistryManager.ARCHAIC_EDGE.get(), RegistryManager.ARCHAIC_TILE.get(), RegistryManager.ARCHAIC_LARGE_BRICKS.get(), RegistryManager.ARCHAIC_LIGHT.get(), RegistryManager.ASHEN_STONE.get(), RegistryManager.ASHEN_BRICK.get(), RegistryManager.ASHEN_TILE.get(), RegistryManager.SOLIDIFIED_METAL.get(), RegistryManager.METAL_PLATFORM.get(), RegistryManager.EMBER_LANTERN.get(), RegistryManager.COPPER_CELL.get(), RegistryManager.CREATIVE_EMBER.get(), RegistryManager.EMBER_DIAL.get(), RegistryManager.ITEM_DIAL.get(), RegistryManager.FLUID_DIAL.get(), RegistryManager.ATMOSPHERIC_GAUGE.get(), RegistryManager.EMBER_EMITTER.get(), RegistryManager.EMBER_RECEIVER.get(), RegistryManager.ITEM_PIPE.get(), RegistryManager.ITEM_EXTRACTOR.get(), RegistryManager.EMBER_BORE.get(), RegistryManager.EMBER_BORE_EDGE.get(), RegistryManager.MECHANICAL_CORE.get(), RegistryManager.EMBER_ACTIVATOR.get(), RegistryManager.MELTER.get(), RegistryManager.FLUID_PIPE.get(), RegistryManager.FLUID_EXTRACTOR.get(), RegistryManager.FLUID_VESSEL.get(), RegistryManager.STAMPER.get(), RegistryManager.STAMP_BASE.get(), RegistryManager.BIN.get(), RegistryManager.MIXER_CENTRIFUGE.get(), RegistryManager.ITEM_DROPPER.get(), RegistryManager.PRESSURE_REFINERY.get(), RegistryManager.EMBER_EJECTOR.get(), RegistryManager.EMBER_FUNNEL.get(), RegistryManager.EMBER_RELAY.get(), RegistryManager.MIRROR_RELAY.get(), RegistryManager.BEAM_SPLITTER.get(), RegistryManager.ITEM_VACUUM.get(), RegistryManager.HEARTH_COIL.get(), RegistryManager.HEARTH_COIL_EDGE.get(), RegistryManager.RESERVOIR.get(), RegistryManager.RESERVOIR_EDGE.get(), RegistryManager.CAMINITE_RING.get(), RegistryManager.CAMINITE_RING_EDGE.get(), RegistryManager.CAMINITE_GAUGE.get(), RegistryManager.CAMINITE_GAUGE_EDGE.get(), RegistryManager.CAMINITE_VALVE.get(), RegistryManager.CAMINITE_VALVE_EDGE.get(), RegistryManager.CRYSTAL_CELL.get(), RegistryManager.CRYSTAL_CELL_EDGE.get(), RegistryManager.CLOCKWORK_ATTENUATOR.get(), RegistryManager.GEOLOGIC_SEPARATOR.get(), RegistryManager.COPPER_CHARGER.get(), RegistryManager.EMBER_SIPHON.get(), RegistryManager.ITEM_TRANSFER.get(), RegistryManager.FLUID_TRANSFER.get(), RegistryManager.ALCHEMY_PEDESTAL.get(), RegistryManager.ALCHEMY_TABLET.get(), RegistryManager.BEAM_CANNON.get(), RegistryManager.MECHANICAL_PUMP.get(), RegistryManager.MINI_BOILER.get(), RegistryManager.CATALYTIC_PLUG.get(), RegistryManager.WILDFIRE_STIRLING.get(), RegistryManager.EMBER_INJECTOR.get(), RegistryManager.COPPER_CRYSTAL_SEED.BLOCK.get(), RegistryManager.IRON_CRYSTAL_SEED.BLOCK.get(), RegistryManager.GOLD_CRYSTAL_SEED.BLOCK.get(), RegistryManager.LEAD_CRYSTAL_SEED.BLOCK.get(), RegistryManager.SILVER_CRYSTAL_SEED.BLOCK.get(), RegistryManager.NICKEL_CRYSTAL_SEED.BLOCK.get(), RegistryManager.TIN_CRYSTAL_SEED.BLOCK.get(), RegistryManager.ALUMINUM_CRYSTAL_SEED.BLOCK.get(), RegistryManager.ZINC_CRYSTAL_SEED.BLOCK.get(), RegistryManager.PLATINUM_CRYSTAL_SEED.BLOCK.get(), RegistryManager.URANIUM_CRYSTAL_SEED.BLOCK.get(), RegistryManager.DAWNSTONE_CRYSTAL_SEED.BLOCK.get(), RegistryManager.FIELD_CHART.get(), RegistryManager.FIELD_CHART_EDGE.get(), RegistryManager.IGNEM_REACTOR.get(), RegistryManager.CATALYSIS_CHAMBER.get(), RegistryManager.COMBUSTION_CHAMBER.get(), RegistryManager.CINDER_PLINTH.get(), RegistryManager.DAWNSTONE_ANVIL.get(), RegistryManager.AUTOMATIC_HAMMER.get(), RegistryManager.INFERNO_FORGE.get(), RegistryManager.INFERNO_FORGE_EDGE.get(), RegistryManager.MNEMONIC_INSCRIBER.get(), RegistryManager.ENTROPIC_ENUMERATOR.get(), RegistryManager.HEAT_EXCHANGER.get(), RegistryManager.HEAT_INSULATION.get(), RegistryManager.EXCAVATION_BUCKETS.get()).m_176839_(CuriosCompat.EXPLOSION_PEDESTAL.getId());
        this.m_206424_(BlockTags.MINEABLE_WITH_AXE).add(RegistryManager.SEALED_PLANKS.get(), RegistryManager.REINFORCED_SEALED_PLANKS.get(), RegistryManager.SEALED_WOOD_PILLAR.get(), RegistryManager.SEALED_WOOD_KEG.get(), RegistryManager.CHAR_INSTILLER.get(), RegistryManager.ATMOSPHERIC_BELLOWS.get());
        this.m_206424_(BlockTags.MINEABLE_WITH_SHOVEL).add(RegistryManager.RAW_CAMINITE_BLOCK.get());
        this.m_206424_(BlockTags.NEEDS_IRON_TOOL).add(RegistryManager.LEAD_ORE.get(), RegistryManager.DEEPSLATE_LEAD_ORE.get(), RegistryManager.RAW_LEAD_BLOCK.get(), RegistryManager.LEAD_BLOCK.get(), RegistryManager.SILVER_ORE.get(), RegistryManager.DEEPSLATE_SILVER_ORE.get(), RegistryManager.RAW_SILVER_BLOCK.get(), RegistryManager.SILVER_BLOCK.get(), RegistryManager.DAWNSTONE_BLOCK.get(), RegistryManager.MITHRIL_BLOCK.get());
        this.m_206424_(RELOCATION_NOT_SUPPORTED).add(RegistryManager.EMBER_BORE.get(), RegistryManager.EMBER_BORE_EDGE.get(), RegistryManager.EMBER_ACTIVATOR.get(), RegistryManager.MELTER.get(), RegistryManager.MIXER_CENTRIFUGE.get(), RegistryManager.PRESSURE_REFINERY.get(), RegistryManager.HEARTH_COIL.get(), RegistryManager.HEARTH_COIL_EDGE.get(), RegistryManager.RESERVOIR.get(), RegistryManager.RESERVOIR_EDGE.get(), RegistryManager.CAMINITE_RING.get(), RegistryManager.CAMINITE_RING_EDGE.get(), RegistryManager.CAMINITE_GAUGE.get(), RegistryManager.CAMINITE_GAUGE_EDGE.get(), RegistryManager.CAMINITE_VALVE.get(), RegistryManager.CAMINITE_VALVE_EDGE.get(), RegistryManager.CRYSTAL_CELL.get(), RegistryManager.CRYSTAL_CELL_EDGE.get(), RegistryManager.ALCHEMY_PEDESTAL.get(), RegistryManager.MECHANICAL_PUMP.get(), RegistryManager.FIELD_CHART.get(), RegistryManager.FIELD_CHART_EDGE.get(), RegistryManager.COMBUSTION_CHAMBER.get(), RegistryManager.CATALYSIS_CHAMBER.get(), RegistryManager.INFERNO_FORGE.get()).addTag(CRYSTAL_SEEDS).m_176839_(CuriosCompat.EXPLOSION_PEDESTAL.getId());
        this.m_206424_(MECH_CORE_PROXYABLE).add(RegistryManager.EMBER_ACTIVATOR.get(), RegistryManager.MIXER_CENTRIFUGE.get(), RegistryManager.PRESSURE_REFINERY.get(), RegistryManager.ALCHEMY_TABLET.get(), RegistryManager.IGNEM_REACTOR.get());
        this.m_206424_(MECH_CORE_PROXYABLE_BOTTOM).add(RegistryManager.HEARTH_COIL.get(), RegistryManager.RESERVOIR.get(), RegistryManager.CRYSTAL_CELL.get(), RegistryManager.INFERNO_FORGE.get());
        this.m_206424_(MECH_CORE_PROXYABLE_TOP).add(RegistryManager.EMBER_BORE.get());
        this.m_206424_(RESERVOIR_EXPANSION).add(RegistryManager.CAMINITE_RING.get(), RegistryManager.CAMINITE_GAUGE.get(), RegistryManager.CAMINITE_VALVE.get());
        this.m_206424_(CHAMBER_CONNECTION).add(RegistryManager.IGNEM_REACTOR.get());
        this.m_206424_(DIAL).add(RegistryManager.EMBER_DIAL.get(), RegistryManager.ITEM_DIAL.get(), RegistryManager.FLUID_DIAL.get(), RegistryManager.CLOCKWORK_ATTENUATOR.get(), RegistryManager.ATMOSPHERIC_GAUGE.get());
        this.m_206424_(FLUID_PIPE_CONNECTION).addTag(FLUID_PIPE_CONNECTION_TOGGLEABLE);
        this.m_206424_(FLUID_PIPE_CONNECTION_TOGGLEABLE).add(RegistryManager.FLUID_PIPE.get(), RegistryManager.FLUID_EXTRACTOR.get());
        this.m_206424_(HEAT_SOURCES).add(Blocks.LAVA, Blocks.FIRE);
        this.m_206424_(WORLD_BOTTOM).add(Blocks.BEDROCK);
        this.m_206424_(SNOW).add(Blocks.SNOW_BLOCK);
        this.m_206424_(PRISTINE_COPPER).addTag(Tags.Blocks.STORAGE_BLOCKS_COPPER).add(Blocks.CUT_COPPER);
        this.m_206424_(EXPOSED_COPPER).add(Blocks.EXPOSED_COPPER).add(Blocks.EXPOSED_CUT_COPPER);
        this.m_206424_(WEATHERED_COPPER).add(Blocks.WEATHERED_COPPER).add(Blocks.WEATHERED_CUT_COPPER);
        this.m_206424_(OXIDIZED_COPPER).add(Blocks.OXIDIZED_COPPER).add(Blocks.OXIDIZED_CUT_COPPER);
        this.m_206424_(Tags.Blocks.ORES).addTags(new TagKey[] { LEAD_ORE });
        this.m_206424_(LEAD_ORE).add(RegistryManager.LEAD_ORE.get()).add(RegistryManager.DEEPSLATE_LEAD_ORE.get());
        this.m_206424_(Tags.Blocks.ORES_IN_GROUND_STONE).add(RegistryManager.LEAD_ORE.get());
        this.m_206424_(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(RegistryManager.DEEPSLATE_LEAD_ORE.get());
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS).addTags(new TagKey[] { RAW_LEAD_BLOCK });
        this.m_206424_(RAW_LEAD_BLOCK).add(RegistryManager.RAW_LEAD_BLOCK.get());
        this.m_206424_(Tags.Blocks.ORES).addTags(new TagKey[] { SILVER_ORE });
        this.m_206424_(SILVER_ORE).add(RegistryManager.SILVER_ORE.get()).add(RegistryManager.DEEPSLATE_SILVER_ORE.get());
        this.m_206424_(Tags.Blocks.ORES_IN_GROUND_STONE).add(RegistryManager.SILVER_ORE.get());
        this.m_206424_(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(RegistryManager.DEEPSLATE_SILVER_ORE.get());
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS).addTags(new TagKey[] { RAW_SILVER_BLOCK });
        this.m_206424_(RAW_SILVER_BLOCK).add(RegistryManager.RAW_SILVER_BLOCK.get());
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS).addTags(new TagKey[] { LEAD_BLOCK }).addTags(new TagKey[] { SILVER_BLOCK }).addTags(new TagKey[] { DAWNSTONE_BLOCK });
        this.m_206424_(LEAD_BLOCK).add(RegistryManager.LEAD_BLOCK.get());
        this.m_206424_(SILVER_BLOCK).add(RegistryManager.SILVER_BLOCK.get());
        this.m_206424_(DAWNSTONE_BLOCK).add(RegistryManager.DAWNSTONE_BLOCK.get());
        this.m_206424_(CRYSTAL_SEEDS).addTags(new TagKey[] { COPPER_SEED, IRON_SEED, GOLD_SEED, LEAD_SEED, SILVER_SEED, NICKEL_SEED, TIN_SEED, ALUMINUM_SEED, ZINC_SEED, PLATINUM_SEED, URANIUM_SEED, DAWNSTONE_SEED });
        this.m_206424_(COPPER_SEED).add(RegistryManager.COPPER_CRYSTAL_SEED.BLOCK.get());
        this.m_206424_(IRON_SEED).add(RegistryManager.IRON_CRYSTAL_SEED.BLOCK.get());
        this.m_206424_(GOLD_SEED).add(RegistryManager.GOLD_CRYSTAL_SEED.BLOCK.get());
        this.m_206424_(LEAD_SEED).add(RegistryManager.LEAD_CRYSTAL_SEED.BLOCK.get());
        this.m_206424_(SILVER_SEED).add(RegistryManager.SILVER_CRYSTAL_SEED.BLOCK.get());
        this.m_206424_(NICKEL_SEED).add(RegistryManager.NICKEL_CRYSTAL_SEED.BLOCK.get());
        this.m_206424_(TIN_SEED).add(RegistryManager.TIN_CRYSTAL_SEED.BLOCK.get());
        this.m_206424_(ALUMINUM_SEED).add(RegistryManager.ALUMINUM_CRYSTAL_SEED.BLOCK.get());
        this.m_206424_(ZINC_SEED).add(RegistryManager.ZINC_CRYSTAL_SEED.BLOCK.get());
        this.m_206424_(PLATINUM_SEED).add(RegistryManager.PLATINUM_CRYSTAL_SEED.BLOCK.get());
        this.m_206424_(URANIUM_SEED).add(RegistryManager.URANIUM_CRYSTAL_SEED.BLOCK.get());
        this.m_206424_(DAWNSTONE_SEED).add(RegistryManager.DAWNSTONE_CRYSTAL_SEED.BLOCK.get());
    }

    public void decoTags(RegistryManager.StoneDecoBlocks deco) {
        if (deco.stairs != null) {
            this.m_206424_(BlockTags.STAIRS).add(deco.stairs.get());
            this.m_206424_(BlockTags.MINEABLE_WITH_PICKAXE).add(deco.stairs.get());
        }
        if (deco.slab != null) {
            this.m_206424_(BlockTags.SLABS).add(deco.slab.get());
            this.m_206424_(BlockTags.MINEABLE_WITH_PICKAXE).add(deco.slab.get());
        }
        if (deco.wall != null) {
            this.m_206424_(BlockTags.WALLS).add(deco.wall.get());
            this.m_206424_(BlockTags.MINEABLE_WITH_PICKAXE).add(deco.wall.get());
        }
    }

    public void woodDecoTags(RegistryManager.StoneDecoBlocks deco) {
        if (deco.stairs != null) {
            this.m_206424_(BlockTags.STAIRS).add(deco.stairs.get());
            this.m_206424_(BlockTags.MINEABLE_WITH_AXE).add(deco.stairs.get());
        }
        if (deco.slab != null) {
            this.m_206424_(BlockTags.SLABS).add(deco.slab.get());
            this.m_206424_(BlockTags.MINEABLE_WITH_AXE).add(deco.slab.get());
        }
        if (deco.wall != null) {
            this.m_206424_(BlockTags.WALLS).add(deco.wall.get());
            this.m_206424_(BlockTags.MINEABLE_WITH_AXE).add(deco.wall.get());
        }
    }
}