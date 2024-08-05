package com.rekindled.embers.datagen;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.compat.curios.CuriosCompat;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class EmbersBlockLootTables extends BlockLootSubProvider {

    public EmbersBlockLootTables() {
        super(Set.of(), FeatureFlags.VANILLA_SET);
    }

    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return (Iterable<Block>) ForgeRegistries.BLOCKS.getValues().stream().filter(block -> "embers".equals(((ResourceLocation) Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block))).getNamespace())).collect(Collectors.toList());
    }

    @Override
    protected void generate() {
        this.m_246481_(RegistryManager.LEAD_ORE.get(), block -> this.m_246109_(block, RegistryManager.RAW_LEAD.get()));
        this.m_246481_(RegistryManager.DEEPSLATE_LEAD_ORE.get(), block -> this.m_246109_(block, RegistryManager.RAW_LEAD.get()));
        this.m_245724_(RegistryManager.RAW_LEAD_BLOCK.get());
        this.m_245724_(RegistryManager.LEAD_BLOCK.get());
        this.m_246481_(RegistryManager.SILVER_ORE.get(), block -> this.m_246109_(block, RegistryManager.RAW_SILVER.get()));
        this.m_246481_(RegistryManager.DEEPSLATE_SILVER_ORE.get(), block -> this.m_246109_(block, RegistryManager.RAW_SILVER.get()));
        this.m_245724_(RegistryManager.RAW_SILVER_BLOCK.get());
        this.m_245724_(RegistryManager.SILVER_BLOCK.get());
        this.m_245724_(RegistryManager.DAWNSTONE_BLOCK.get());
        this.m_245724_(RegistryManager.MITHRIL_BLOCK.get());
        this.m_245724_(RegistryManager.CAMINITE_BRICKS.get());
        this.decoDrops(RegistryManager.CAMINITE_BRICKS_DECO);
        this.m_245724_(RegistryManager.CAMINITE_LARGE_BRICKS.get());
        this.decoDrops(RegistryManager.CAMINITE_LARGE_BRICKS_DECO);
        this.m_245724_(RegistryManager.RAW_CAMINITE_BLOCK.get());
        this.m_245724_(RegistryManager.CAMINITE_LARGE_TILE.get());
        this.decoDrops(RegistryManager.CAMINITE_LARGE_TILE_DECO);
        this.m_245724_(RegistryManager.CAMINITE_TILES.get());
        this.decoDrops(RegistryManager.CAMINITE_TILES_DECO);
        this.m_245724_(RegistryManager.ARCHAIC_BRICKS.get());
        this.decoDrops(RegistryManager.ARCHAIC_BRICKS_DECO);
        this.m_245724_(RegistryManager.ARCHAIC_EDGE.get());
        this.m_245724_(RegistryManager.ARCHAIC_TILE.get());
        this.m_245724_(RegistryManager.ARCHAIC_LARGE_BRICKS.get());
        this.decoDrops(RegistryManager.ARCHAIC_LARGE_BRICKS_DECO);
        this.decoDrops(RegistryManager.ARCHAIC_TILE_DECO);
        this.m_245724_(RegistryManager.ARCHAIC_LIGHT.get());
        this.m_245724_(RegistryManager.ASHEN_STONE.get());
        this.decoDrops(RegistryManager.ASHEN_STONE_DECO);
        this.m_245724_(RegistryManager.ASHEN_BRICK.get());
        this.decoDrops(RegistryManager.ASHEN_BRICK_DECO);
        this.m_245724_(RegistryManager.ASHEN_TILE.get());
        this.decoDrops(RegistryManager.ASHEN_TILE_DECO);
        this.m_245724_(RegistryManager.SEALED_PLANKS.get());
        this.decoDrops(RegistryManager.SEALED_PLANKS_DECO);
        this.m_245724_(RegistryManager.REINFORCED_SEALED_PLANKS.get());
        this.m_245724_(RegistryManager.SEALED_WOOD_TILE.get());
        this.decoDrops(RegistryManager.SEALED_WOOD_TILE_DECO);
        this.m_245724_(RegistryManager.SEALED_WOOD_PILLAR.get());
        this.m_245724_(RegistryManager.SEALED_WOOD_KEG.get());
        this.m_245724_(RegistryManager.SOLIDIFIED_METAL.get());
        this.m_245724_(RegistryManager.METAL_PLATFORM.get());
        this.decoDrops(RegistryManager.METAL_PLATFORM_DECO);
        this.m_245724_(RegistryManager.EMBER_LANTERN.get());
        this.m_245724_(RegistryManager.COPPER_CELL.get());
        this.m_245724_(RegistryManager.CREATIVE_EMBER.get());
        this.m_245724_(RegistryManager.EMBER_DIAL.get());
        this.m_245724_(RegistryManager.ITEM_DIAL.get());
        this.m_245724_(RegistryManager.FLUID_DIAL.get());
        this.m_245724_(RegistryManager.ATMOSPHERIC_GAUGE.get());
        this.m_245724_(RegistryManager.EMBER_EMITTER.get());
        this.m_245724_(RegistryManager.EMBER_RECEIVER.get());
        this.m_245724_(RegistryManager.CAMINITE_LEVER.get());
        this.m_245724_(RegistryManager.CAMINITE_BUTTON.get());
        this.m_245724_(RegistryManager.ITEM_PIPE.get());
        this.m_245724_(RegistryManager.ITEM_EXTRACTOR.get());
        this.m_245724_(RegistryManager.EMBER_BORE.get());
        this.m_246125_(RegistryManager.EMBER_BORE_EDGE.get(), RegistryManager.EMBER_BORE.get());
        this.m_245724_(RegistryManager.MECHANICAL_CORE.get());
        this.m_245724_(RegistryManager.EMBER_ACTIVATOR.get());
        this.m_245724_(RegistryManager.MELTER.get());
        this.m_245724_(RegistryManager.FLUID_PIPE.get());
        this.m_245724_(RegistryManager.FLUID_EXTRACTOR.get());
        this.m_245724_(RegistryManager.FLUID_VESSEL.get());
        this.m_245724_(RegistryManager.STAMPER.get());
        this.m_245724_(RegistryManager.STAMP_BASE.get());
        this.m_245724_(RegistryManager.BIN.get());
        this.m_245724_(RegistryManager.MIXER_CENTRIFUGE.get());
        this.m_245724_(RegistryManager.ITEM_DROPPER.get());
        this.m_245724_(RegistryManager.PRESSURE_REFINERY.get());
        this.m_245724_(RegistryManager.EMBER_EJECTOR.get());
        this.m_245724_(RegistryManager.EMBER_FUNNEL.get());
        this.m_245724_(RegistryManager.EMBER_RELAY.get());
        this.m_245724_(RegistryManager.MIRROR_RELAY.get());
        this.m_245724_(RegistryManager.BEAM_SPLITTER.get());
        this.m_245724_(RegistryManager.ITEM_VACUUM.get());
        this.m_245724_(RegistryManager.HEARTH_COIL.get());
        this.m_246125_(RegistryManager.HEARTH_COIL_EDGE.get(), RegistryManager.HEARTH_COIL.get());
        this.m_245724_(RegistryManager.RESERVOIR.get());
        this.m_246125_(RegistryManager.RESERVOIR_EDGE.get(), RegistryManager.RESERVOIR.get());
        this.m_245724_(RegistryManager.CAMINITE_RING.get());
        this.m_246125_(RegistryManager.CAMINITE_RING_EDGE.get(), RegistryManager.CAMINITE_RING.get());
        this.m_245724_(RegistryManager.CAMINITE_GAUGE.get());
        this.m_246125_(RegistryManager.CAMINITE_GAUGE_EDGE.get(), RegistryManager.CAMINITE_GAUGE.get());
        this.m_245724_(RegistryManager.CAMINITE_VALVE.get());
        this.m_246125_(RegistryManager.CAMINITE_VALVE_EDGE.get(), RegistryManager.CAMINITE_VALVE.get());
        this.m_245724_(RegistryManager.CRYSTAL_CELL.get());
        this.m_246125_(RegistryManager.CRYSTAL_CELL_EDGE.get(), RegistryManager.CRYSTAL_CELL.get());
        this.m_245724_(RegistryManager.CLOCKWORK_ATTENUATOR.get());
        this.m_245724_(RegistryManager.GEOLOGIC_SEPARATOR.get());
        this.m_245724_(RegistryManager.COPPER_CHARGER.get());
        this.m_245724_(RegistryManager.EMBER_SIPHON.get());
        this.m_245724_(RegistryManager.ITEM_TRANSFER.get());
        this.m_245724_(RegistryManager.FLUID_TRANSFER.get());
        this.m_245724_(RegistryManager.ALCHEMY_PEDESTAL.get());
        this.m_245724_(CuriosCompat.EXPLOSION_PEDESTAL.get());
        this.m_245724_(RegistryManager.ALCHEMY_TABLET.get());
        this.m_245724_(RegistryManager.BEAM_CANNON.get());
        this.m_245724_(RegistryManager.MECHANICAL_PUMP.get());
        this.m_245724_(RegistryManager.MINI_BOILER.get());
        this.m_245724_(RegistryManager.CATALYTIC_PLUG.get());
        this.m_245724_(RegistryManager.WILDFIRE_STIRLING.get());
        this.m_245724_(RegistryManager.EMBER_INJECTOR.get());
        this.m_245724_(RegistryManager.COPPER_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.IRON_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.GOLD_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.LEAD_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.SILVER_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.NICKEL_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.TIN_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.ALUMINUM_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.ZINC_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.PLATINUM_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.URANIUM_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.DAWNSTONE_CRYSTAL_SEED.BLOCK.get());
        this.m_245724_(RegistryManager.FIELD_CHART.get());
        this.m_246125_(RegistryManager.FIELD_CHART_EDGE.get(), RegistryManager.FIELD_CHART.get());
        this.m_245724_(RegistryManager.IGNEM_REACTOR.get());
        this.m_245724_(RegistryManager.CATALYSIS_CHAMBER.get());
        this.m_245724_(RegistryManager.COMBUSTION_CHAMBER.get());
        this.m_247577_(RegistryManager.GLIMMER.get(), m_246386_());
        this.m_245724_(RegistryManager.CINDER_PLINTH.get());
        this.m_245724_(RegistryManager.DAWNSTONE_ANVIL.get());
        this.m_245724_(RegistryManager.AUTOMATIC_HAMMER.get());
        this.m_245724_(RegistryManager.INFERNO_FORGE.get());
        this.m_246125_(RegistryManager.INFERNO_FORGE_EDGE.get(), RegistryManager.INFERNO_FORGE.get());
        this.m_245724_(RegistryManager.MNEMONIC_INSCRIBER.get());
        this.m_245724_(RegistryManager.CHAR_INSTILLER.get());
        this.m_245724_(RegistryManager.ATMOSPHERIC_BELLOWS.get());
        this.m_245724_(RegistryManager.ENTROPIC_ENUMERATOR.get());
        this.m_245724_(RegistryManager.HEAT_EXCHANGER.get());
        this.m_245724_(RegistryManager.HEAT_INSULATION.get());
        this.m_245724_(RegistryManager.EXCAVATION_BUCKETS.get());
    }

    public void decoDrops(RegistryManager.StoneDecoBlocks deco) {
        if (deco.stairs != null) {
            this.m_245724_(deco.stairs.get());
        }
        if (deco.slab != null) {
            this.m_246481_(deco.slab.get(), x$0 -> this.m_247233_(x$0));
        }
        if (deco.wall != null) {
            this.m_245724_(deco.wall.get());
        }
    }
}