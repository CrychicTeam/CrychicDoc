package com.mna.blocks.tileentities.init;

import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.BookshelfTile;
import com.mna.blocks.tileentities.BrazierTile;
import com.mna.blocks.tileentities.ChalkRuneTile;
import com.mna.blocks.tileentities.CoffinTile;
import com.mna.blocks.tileentities.ConstructWorkbenchTile;
import com.mna.blocks.tileentities.EldrinAltarTile;
import com.mna.blocks.tileentities.EldrinConduitTile;
import com.mna.blocks.tileentities.ElementalSentryTile;
import com.mna.blocks.tileentities.FluidJugTile;
import com.mna.blocks.tileentities.IceSpikeTile;
import com.mna.blocks.tileentities.IllusionBlockTile;
import com.mna.blocks.tileentities.ImpaleSpikeTile;
import com.mna.blocks.tileentities.LodestarTile;
import com.mna.blocks.tileentities.MagelightTile;
import com.mna.blocks.tileentities.ManaCrystalTile;
import com.mna.blocks.tileentities.ManaResevoirTile;
import com.mna.blocks.tileentities.ManaweaveCacheTile;
import com.mna.blocks.tileentities.ManaweaveProjectorTile;
import com.mna.blocks.tileentities.ManaweavingAltarTile;
import com.mna.blocks.tileentities.OcculusTile;
import com.mna.blocks.tileentities.OffsetBlockTile;
import com.mna.blocks.tileentities.ParticleEmitterTile;
import com.mna.blocks.tileentities.PedestalTile;
import com.mna.blocks.tileentities.RefractionLensTile;
import com.mna.blocks.tileentities.RuneForgeTile;
import com.mna.blocks.tileentities.RunicAnvilTile;
import com.mna.blocks.tileentities.SanctumTile;
import com.mna.blocks.tileentities.ScrollShelfTile;
import com.mna.blocks.tileentities.SeerStoneTile;
import com.mna.blocks.tileentities.SimpleSpectralTile;
import com.mna.blocks.tileentities.SlipstreamGeneratorTile;
import com.mna.blocks.tileentities.SpectralTile;
import com.mna.blocks.tileentities.TransitoryTile;
import com.mna.blocks.tileentities.TransitoryTunnelTile;
import com.mna.blocks.tileentities.WellspringPillarTile;
import com.mna.blocks.tileentities.wizard_lab.AffinityTinkerTile;
import com.mna.blocks.tileentities.wizard_lab.ArcanaAltarTile;
import com.mna.blocks.tileentities.wizard_lab.BookStandTile;
import com.mna.blocks.tileentities.wizard_lab.DisenchanterTile;
import com.mna.blocks.tileentities.wizard_lab.EldrinFumeTile;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.blocks.tileentities.wizard_lab.MagiciansWorkbenchTile;
import com.mna.blocks.tileentities.wizard_lab.RunescribingTableTile;
import com.mna.blocks.tileentities.wizard_lab.SpellSpecializationTile;
import com.mna.blocks.tileentities.wizard_lab.StudyDeskTile;
import com.mna.blocks.tileentities.wizard_lab.ThesisDeskTile;
import com.mna.blocks.tileentities.wizard_lab.TranscriptionTableTile;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "mna");

    public static final RegistryObject<BlockEntityType<ChalkRuneTile>> CHALK_RUNE = TILE_ENTITY_TYPES.register("chalk_rune_tile_entity", () -> BlockEntityType.Builder.of(ChalkRuneTile::new, BlockInit.CHALK_RUNE.get()).build(null));

    public static final RegistryObject<BlockEntityType<PedestalTile>> PEDESTAL = TILE_ENTITY_TYPES.register("pedestal_tile_entity", () -> BlockEntityType.Builder.of(PedestalTile::new, BlockInit.PEDESTAL.get(), BlockInit.PEDESTAL_WITH_SIGN.get()).build(null));

    public static final RegistryObject<BlockEntityType<RuneForgeTile>> RUNEFORGE = TILE_ENTITY_TYPES.register("runeforge_tile_entity", () -> BlockEntityType.Builder.of(RuneForgeTile::new, BlockInit.RUNEFORGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<ManaweavingAltarTile>> MANAWEAVING_ALTAR = TILE_ENTITY_TYPES.register("manaweaving_altar_tile_entity", () -> BlockEntityType.Builder.of(ManaweavingAltarTile::new, BlockInit.MANAWEAVING_ALTAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ManaweaveCacheTile>> MANAWEAVE_CACHE = TILE_ENTITY_TYPES.register("manaweave_cache_tile", () -> BlockEntityType.Builder.of(ManaweaveCacheTile::new, BlockInit.MANAWEAVE_CACHE.get()).build(null));

    public static final RegistryObject<BlockEntityType<InscriptionTableTile>> INSCRIPTION_TABLE = TILE_ENTITY_TYPES.register("inscription_table_tile_entity", () -> BlockEntityType.Builder.of(InscriptionTableTile::new, BlockInit.INSCRIPTION_TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<RunescribingTableTile>> RUNESCRIBING_TABLE = TILE_ENTITY_TYPES.register("runescribing_table_tile_entity", () -> BlockEntityType.Builder.of(RunescribingTableTile::new, BlockInit.RUNESCRIBING_TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<RunicAnvilTile>> RUNIC_ANVIL = TILE_ENTITY_TYPES.register("runic_anvil_tile_entity", () -> BlockEntityType.Builder.of(RunicAnvilTile::new, BlockInit.RUNIC_ANVIL.get()).build(null));

    public static final RegistryObject<BlockEntityType<ParticleEmitterTile>> PARTICLE_EMITTER = TILE_ENTITY_TYPES.register("particle_emitter_tile_entity", () -> BlockEntityType.Builder.of(ParticleEmitterTile::new, BlockInit.PARTICLE_EMITTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<ManaweaveProjectorTile>> MANAWEAVE_PROJECTOR = TILE_ENTITY_TYPES.register("manaweave_projector_tile_entity", () -> BlockEntityType.Builder.of(ManaweaveProjectorTile::new, BlockInit.MANAWEAVE_PROJECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ManaResevoirTile>> MANA_RESEVOIR = TILE_ENTITY_TYPES.register("mana_resevoir_tile_entity", () -> BlockEntityType.Builder.of(ManaResevoirTile::new, BlockInit.MANA_RESEVOIR.get()).build(null));

    public static final RegistryObject<BlockEntityType<TransitoryTunnelTile>> TRANSITORY_TUNNEL = TILE_ENTITY_TYPES.register("transitory_tunnel_tile_entity", () -> BlockEntityType.Builder.of(TransitoryTunnelTile::new, BlockInit.TRANSITORY_TUNNEL.get()).build(null));

    public static final RegistryObject<BlockEntityType<TransitoryTile>> TRANSITORY_TILE = TILE_ENTITY_TYPES.register("transitory_tile_tile_entity", () -> BlockEntityType.Builder.of(TransitoryTile::new, BlockInit.TRANSITORY_TILE.get()).build(null));

    public static final RegistryObject<BlockEntityType<ManaCrystalTile>> MANA_CRYSTAL = TILE_ENTITY_TYPES.register("mana_crystal_tile_entity", () -> BlockEntityType.Builder.of(ManaCrystalTile::new, BlockInit.MANA_CRYSTAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<OcculusTile>> OCCULUS = TILE_ENTITY_TYPES.register("occulus_tile_entity", () -> BlockEntityType.Builder.of(OcculusTile::new, BlockInit.OCCULUS.get()).build(null));

    public static final RegistryObject<BlockEntityType<IceSpikeTile>> ICE_SPIKE = TILE_ENTITY_TYPES.register("ice_spike_tile", () -> BlockEntityType.Builder.of(IceSpikeTile::new, BlockInit.ICE_SPIKE.get()).build(null));

    public static final RegistryObject<BlockEntityType<ImpaleSpikeTile>> IMPALE_SPIKE = TILE_ENTITY_TYPES.register("impale_spike_tile", () -> BlockEntityType.Builder.of(ImpaleSpikeTile::new, BlockInit.IMPALE_SPIKE.get()).build(null));

    public static final RegistryObject<BlockEntityType<ElementalSentryTile>> ELEMENTAL_SENTRY = TILE_ENTITY_TYPES.register("arcane_sentry_tile_entity", () -> BlockEntityType.Builder.of(ElementalSentryTile::new, BlockInit.ARCANE_SENTRY.get(), BlockInit.ENDER_SENTRY.get(), BlockInit.FIRE_SENTRY.get(), BlockInit.WATER_SENTRY.get(), BlockInit.EARTH_SENTRY.get(), BlockInit.WIND_SENTRY.get()).build(null));

    public static final RegistryObject<BlockEntityType<SeerStoneTile>> SEER_STONE = TILE_ENTITY_TYPES.register("seer_stone_tile_entity", () -> BlockEntityType.Builder.of(SeerStoneTile::new, BlockInit.SEER_STONE.get()).build(null));

    public static final RegistryObject<BlockEntityType<ConstructWorkbenchTile>> CONSTRUCT_WORKBENCH = TILE_ENTITY_TYPES.register("construct_workbench", () -> BlockEntityType.Builder.of(ConstructWorkbenchTile::new, BlockInit.CONSTRUCT_WORKBENCH.get()).build(null));

    public static final RegistryObject<BlockEntityType<LodestarTile>> LODESTAR = TILE_ENTITY_TYPES.register("lodestar", () -> BlockEntityType.Builder.of(LodestarTile::new, BlockInit.LODESTAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<MagelightTile>> MAGE_LIGHT = TILE_ENTITY_TYPES.register("magelight", () -> BlockEntityType.Builder.of(MagelightTile::new, BlockInit.MAGE_LIGHT.get()).build(null));

    public static final RegistryObject<BlockEntityType<BrazierTile>> BRAZIER = TILE_ENTITY_TYPES.register("brazier", () -> BlockEntityType.Builder.of(BrazierTile::new, BlockInit.BRAZIER.get()).build(null));

    public static final RegistryObject<BlockEntityType<SlipstreamGeneratorTile>> SLIPSTREAM_GENERATOR = TILE_ENTITY_TYPES.register("slipstream_generator", () -> BlockEntityType.Builder.of(SlipstreamGeneratorTile::new, BlockInit.SLIPSTREAM_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<SimpleSpectralTile>> SIMPLE_SPECTRAL_TILE = TILE_ENTITY_TYPES.register("simple_spectral_tile", () -> BlockEntityType.Builder.of(SimpleSpectralTile::new, BlockInit.SPECTRAL_LADDER.get(), BlockInit.SPECTRAL_WEB.get()).build(null));

    public static final RegistryObject<BlockEntityType<SpectralTile>> SPECTRAL_TILE = TILE_ENTITY_TYPES.register("spectral_tile", () -> BlockEntityType.Builder.of(SpectralTile::new, BlockInit.SPECTRAL_CRAFTING_TABLE.get(), BlockInit.SPECTRAL_ANVIL.get(), BlockInit.SPECTRAL_STONECUTTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<SanctumTile>> CIRCLE_OF_POWER = TILE_ENTITY_TYPES.register("circle_of_power", () -> BlockEntityType.Builder.of(SanctumTile::new, BlockInit.CIRCLE_OF_POWER.get()).build(null));

    public static final RegistryObject<BlockEntityType<CoffinTile>> COFFIN = TILE_ENTITY_TYPES.register("coffin", () -> BlockEntityType.Builder.of(CoffinTile::new, BlockInit.COFFIN.get()).build(null));

    public static final RegistryObject<BlockEntityType<OffsetBlockTile>> FILLER_TILE = TILE_ENTITY_TYPES.register("filler_tile", () -> BlockEntityType.Builder.of(OffsetBlockTile::new, BlockInit.EMPTY_FILLER_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<EldrinConduitTile>> ELDRIN_CONDUIT_TILE = TILE_ENTITY_TYPES.register("eldrin_conduit_tile", () -> BlockEntityType.Builder.of(EldrinConduitTile::new, BlockInit.ELDRIN_CONDUIT_FIRE.get(), BlockInit.ELDRIN_CONDUIT_WATER.get(), BlockInit.ELDRIN_CONDUIT_EARTH.get(), BlockInit.ELDRIN_CONDUIT_AIR.get(), BlockInit.ELDRIN_CONDUIT_ENDER.get(), BlockInit.ELDRIN_CONDUIT_ARCANE.get(), BlockInit.LESSER_ELDRIN_CONDUIT_FIRE.get(), BlockInit.LESSER_ELDRIN_CONDUIT_WATER.get(), BlockInit.LESSER_ELDRIN_CONDUIT_EARTH.get(), BlockInit.LESSER_ELDRIN_CONDUIT_AIR.get(), BlockInit.LESSER_ELDRIN_CONDUIT_ENDER.get(), BlockInit.LESSER_ELDRIN_CONDUIT_ARCANE.get()).build(null));

    public static final RegistryObject<BlockEntityType<EldrinAltarTile>> ELDRIN_ALTAR_TILE = TILE_ENTITY_TYPES.register("runic_altar_tile", () -> BlockEntityType.Builder.of(EldrinAltarTile::new, BlockInit.ELDRIN_ALTAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<WellspringPillarTile>> WELLSPRING_PILLAR = TILE_ENTITY_TYPES.register("wellspring_pillar", () -> BlockEntityType.Builder.of(WellspringPillarTile::new, BlockInit.WELLSPRING_PILLAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<MagiciansWorkbenchTile>> MAGICIANS_WORKBENCH = TILE_ENTITY_TYPES.register("magicians_workbench", () -> BlockEntityType.Builder.of(MagiciansWorkbenchTile::new, BlockInit.MAGICIANS_WORKBENCH.get()).build(null));

    public static final RegistryObject<BlockEntityType<FluidJugTile>> FLUID_JUG = TILE_ENTITY_TYPES.register("fluid_jug", () -> BlockEntityType.Builder.of(FluidJugTile::new, BlockInit.FLUID_JUG.get(), BlockInit.FLUID_JUG_INFINITE_LAVA.get(), BlockInit.FLUID_JUG_INFINITE_WATER.get()).build(null));

    public static final RegistryObject<BlockEntityType<IllusionBlockTile>> ILLUSION_BLOCK = TILE_ENTITY_TYPES.register("illusion_block", () -> BlockEntityType.Builder.of(IllusionBlockTile::new, BlockInit.ILLUSION_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<ThesisDeskTile>> THESIS_DESK = TILE_ENTITY_TYPES.register("thesis_desk", () -> BlockEntityType.Builder.of(ThesisDeskTile::new, BlockInit.THESIS_DESK.get()).build(null));

    public static final RegistryObject<BlockEntityType<TranscriptionTableTile>> TRANSCRIPTION_TABLE = TILE_ENTITY_TYPES.register("transcription_table", () -> BlockEntityType.Builder.of(TranscriptionTableTile::new, BlockInit.TRANSCRIPTION_TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<StudyDeskTile>> STUDY_DESK = TILE_ENTITY_TYPES.register("study_desk", () -> BlockEntityType.Builder.of(StudyDeskTile::new, BlockInit.STUDY_DESK.get()).build(null));

    public static final RegistryObject<BlockEntityType<SpellSpecializationTile>> SPELL_SPECIALIZAITON = TILE_ENTITY_TYPES.register("spell_specialization", () -> BlockEntityType.Builder.of(SpellSpecializationTile::new, BlockInit.SPELL_SPECIALIZATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<ArcanaAltarTile>> ALTAR_OF_ARCANA = TILE_ENTITY_TYPES.register("altar_of_arcana", () -> BlockEntityType.Builder.of(ArcanaAltarTile::new, BlockInit.ALTAR_OF_ARCANA.get()).build(null));

    public static final RegistryObject<BlockEntityType<DisenchanterTile>> DISENCHANTER = TILE_ENTITY_TYPES.register("disenchanter", () -> BlockEntityType.Builder.of(DisenchanterTile::new, BlockInit.DISENCHANTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<AffinityTinkerTile>> AFFINITY_TINKER = TILE_ENTITY_TYPES.register("affinity_tinker", () -> BlockEntityType.Builder.of(AffinityTinkerTile::new, BlockInit.AFFINITY_TINKER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BookshelfTile>> BOOKSHELF = TILE_ENTITY_TYPES.register("bookshelf", () -> BlockEntityType.Builder.of(BookshelfTile::new, BlockInit.BOOKSHELF.get()).build(null));

    public static final RegistryObject<BlockEntityType<BookStandTile>> BOOK_STAND = TILE_ENTITY_TYPES.register("book_stand", () -> BlockEntityType.Builder.of(BookStandTile::new, BlockInit.BOOK_STAND.get()).build(null));

    public static final RegistryObject<BlockEntityType<ScrollShelfTile>> SCROLLSHELF = TILE_ENTITY_TYPES.register("scrollshelf", () -> BlockEntityType.Builder.of(ScrollShelfTile::new, BlockInit.SCROLLSHELF.get()).build(null));

    public static final RegistryObject<BlockEntityType<EldrinFumeTile>> ELDRIN_FUME = TILE_ENTITY_TYPES.register("eldrin_fume", () -> BlockEntityType.Builder.of(EldrinFumeTile::new, BlockInit.ELDRIN_FUME.get()).build(null));

    public static final RegistryObject<BlockEntityType<RefractionLensTile>> REFRACTION_LENS = TILE_ENTITY_TYPES.register("refraction_lens", () -> BlockEntityType.Builder.of(RefractionLensTile::new, BlockInit.REFRACTION_LENS_ARCANE.get(), BlockInit.REFRACTION_LENS_ENDER.get(), BlockInit.REFRACTION_LENS_FIRE.get(), BlockInit.REFRACTION_LENS_WATER.get(), BlockInit.REFRACTION_LENS_WIND.get(), BlockInit.REFRACTION_LENS_EARTH.get()).build(null));
}