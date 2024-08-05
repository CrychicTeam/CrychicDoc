package com.mna.blocks;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.items.MACreativeTabs;
import com.mna.blocks.artifice.AffinityTinkerBlock;
import com.mna.blocks.artifice.ArcanaAltarBlock;
import com.mna.blocks.artifice.BookStandBlock;
import com.mna.blocks.artifice.CoffinBlock;
import com.mna.blocks.artifice.ConstructWorkbenchBlock;
import com.mna.blocks.artifice.DisenchanterBlock;
import com.mna.blocks.artifice.EldrinAltarBlock;
import com.mna.blocks.artifice.EldrinConduitBlock;
import com.mna.blocks.artifice.EldrinFumeBlock;
import com.mna.blocks.artifice.ElementalSentryBlock;
import com.mna.blocks.artifice.FluidJugBlock;
import com.mna.blocks.artifice.LodestarBlock;
import com.mna.blocks.artifice.MagiciansWorkbenchBlock;
import com.mna.blocks.artifice.OcculusBlock;
import com.mna.blocks.artifice.RedstoneSpellTriggerBlock;
import com.mna.blocks.artifice.RefractionLensBlock;
import com.mna.blocks.artifice.RunicLightBlock;
import com.mna.blocks.artifice.RunicTorchBlock;
import com.mna.blocks.artifice.SeerStoneBlock;
import com.mna.blocks.artifice.SlipstreamGeneratorBlock;
import com.mna.blocks.artifice.SpellSpecializationBlock;
import com.mna.blocks.artifice.StudyDeskBlock;
import com.mna.blocks.artifice.ThesisDeskBlock;
import com.mna.blocks.artifice.TranscriptionTableBlock;
import com.mna.blocks.artifice.TransitoryTunnelBlock;
import com.mna.blocks.artifice.WardingCandleBlock;
import com.mna.blocks.artifice.WellspringPillarBlock;
import com.mna.blocks.decoration.BookshelfBlock;
import com.mna.blocks.decoration.BrazierBlock;
import com.mna.blocks.decoration.ClayMugBlock;
import com.mna.blocks.decoration.ParticleEmitterBlock;
import com.mna.blocks.decoration.ScrollshelfBlock;
import com.mna.blocks.decoration.SimpleRotationalBlock;
import com.mna.blocks.manaweaving.ManaResevoirBlock;
import com.mna.blocks.manaweaving.ManaweaveProjectorBlock;
import com.mna.blocks.manaweaving.ManaweavingAltarBlock;
import com.mna.blocks.ritual.ChalkRuneBlock;
import com.mna.blocks.ritual.ChimeriteCrystalBlock;
import com.mna.blocks.ritual.CircleOfPowerBlock;
import com.mna.blocks.ritual.RitualTeleportLocationBlock;
import com.mna.blocks.runeforging.PedestalBlock;
import com.mna.blocks.runeforging.RuneforgeBlock;
import com.mna.blocks.runeforging.RunescribingTableBlock;
import com.mna.blocks.runeforging.RunicAnvilBlock;
import com.mna.blocks.sorcery.HellfireBlock;
import com.mna.blocks.sorcery.IceSpikeBlock;
import com.mna.blocks.sorcery.IllusionBlock;
import com.mna.blocks.sorcery.ImpaleBlock;
import com.mna.blocks.sorcery.InscriptionTableBlock;
import com.mna.blocks.sorcery.MagelightBlock;
import com.mna.blocks.sorcery.ManaCrystalBlock;
import com.mna.blocks.sorcery.PulseBlock;
import com.mna.blocks.sorcery.SpectralAnvilBlock;
import com.mna.blocks.sorcery.SpectralCraftingTableBlock;
import com.mna.blocks.sorcery.SpectralLadderBlock;
import com.mna.blocks.sorcery.SpectralStonecutterBlock;
import com.mna.blocks.sorcery.SpectralWebBlock;
import com.mna.blocks.sorcery.TransitoryTileBlock;
import com.mna.blocks.utility.CloudBlock;
import com.mna.blocks.utility.FillerBlock;
import com.mna.blocks.worldgen.MAFlowerBlock;
import com.mna.blocks.worldgen.MASandFlowerBlock;
import com.mna.blocks.worldgen.MATarmaRootBlock;
import com.mna.blocks.worldgen.MAWaterFlowerBlock;
import com.mna.blocks.worldgen.ManaweaveCacheBlock;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class BlockInit {

    public static final BlockBehaviour.StatePredicate ALWAYS = (bs, wr, bp) -> true;

    public static final BlockBehaviour.StatePredicate NEVER = (bs, wr, bp) -> false;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "mna");

    public static final RegistryObject<Block> VINTEUM_ORE = BLOCKS.register("vinteum_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.5F, 3.5F)));

    public static final RegistryObject<MAFlowerBlock> AUM = BLOCKS.register("aum", () -> new MAFlowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).strength(0.0F, 0.0F)));

    public static final RegistryObject<MAWaterFlowerBlock> WAKEBLOOM = BLOCKS.register("wakebloom", () -> new MAWaterFlowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).randomTicks().strength(0.0F)));

    public static final RegistryObject<MAFlowerBlock> CERUBLOSSOM = BLOCKS.register("cerublossom", () -> new MAFlowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).randomTicks().strength(0.0F)));

    public static final RegistryObject<MATarmaRootBlock> TARMA_ROOT = BLOCKS.register("tarma_root", () -> new MATarmaRootBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).randomTicks().strength(0.0F)));

    public static final RegistryObject<MASandFlowerBlock> DESERT_NOVA = BLOCKS.register("desert_nova", () -> new MASandFlowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).randomTicks().strength(0.0F)));

    public static final RegistryObject<FlowerPotBlock> POTTED_AUM = BLOCKS.register("potted_aum", () -> new FlowerPotBlock(() -> (FlowerPotBlock) ForgeRegistries.BLOCKS.getDelegateOrThrow(Blocks.FLOWER_POT).get(), AUM, BlockBehaviour.Properties.of().strength(0.0F).noOcclusion()));

    public static final RegistryObject<FlowerPotBlock> POTTED_CERUBLOSSOM = BLOCKS.register("potted_cerublossom", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CERUBLOSSOM, BlockBehaviour.Properties.of().strength(0.0F).noOcclusion()));

    public static final RegistryObject<FlowerPotBlock> POTTED_WAKEBLOOM = BLOCKS.register("potted_wakebloom", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, WAKEBLOOM, BlockBehaviour.Properties.of().strength(0.0F).noOcclusion()));

    public static final RegistryObject<FlowerPotBlock> POTTED_TARMA_ROOT = BLOCKS.register("potted_tarma_root", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, TARMA_ROOT, BlockBehaviour.Properties.of().strength(0.0F).noOcclusion()));

    public static final RegistryObject<FlowerPotBlock> POTTED_DESERT_NOVA = BLOCKS.register("potted_desert_nova", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DESERT_NOVA, BlockBehaviour.Properties.of().strength(0.0F).noOcclusion()));

    public static final RegistryObject<Block> VINTEUM_BLOCK = BLOCKS.register("vinteum_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5F, 3.5F)));

    public static final RegistryObject<Block> VINTEUM_DUST_BLOCK = BLOCKS.register("vinteum_dust_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(1.0F, 1.0F)));

    public static final RegistryObject<Block> CHIMERITE_BLOCK = BLOCKS.register("chimerite_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5F, 3.5F)));

    public static final RegistryObject<Block> CHALK_RUNE = BLOCKS.register("chalk_rune", ChalkRuneBlock::new);

    public static final RegistryObject<Block> INSCRIPTION_TABLE = BLOCKS.register("inscription_table", InscriptionTableBlock::new);

    public static final RegistryObject<Block> ALTAR_OF_ARCANA = BLOCKS.register("altar_of_arcana", ArcanaAltarBlock::new);

    public static final RegistryObject<Block> MANAWEAVING_ALTAR = BLOCKS.register("manaweaving_altar", ManaweavingAltarBlock::new);

    public static final RegistryObject<Block> MANAWEAVE_PROJECTOR = BLOCKS.register("manaweave_projector", ManaweaveProjectorBlock::new);

    public static final RegistryObject<Block> MANA_RESEVOIR = BLOCKS.register("mana_resevoir", ManaResevoirBlock::new);

    public static final RegistryObject<Block> PEDESTAL = BLOCKS.register("pedestal", () -> new PedestalBlock(false));

    public static final RegistryObject<Block> PEDESTAL_WITH_SIGN = BLOCKS.register("pedestal_with_sign", () -> new PedestalBlock(true));

    public static final RegistryObject<Block> PARTICLE_EMITTER = BLOCKS.register("particle_emitter", ParticleEmitterBlock::new);

    public static final RegistryObject<CloudBlock> STORM_CLOUD = BLOCKS.register("storm_cloud", CloudBlock::new);

    public static final RegistryObject<RedstoneSpellTriggerBlock> REDSTONE_TRIGGER = BLOCKS.register("redstone_trigger", RedstoneSpellTriggerBlock::new);

    public static final RegistryObject<PulseBlock> REDSTONE_PULSE = BLOCKS.register("redstone_pulse", PulseBlock::new);

    public static final RegistryObject<Block> RUNESCRIBING_TABLE = BLOCKS.register("runescribing_table", RunescribingTableBlock::new);

    public static final RegistryObject<Block> RUNEFORGE = BLOCKS.register("runeforge", RuneforgeBlock::new);

    public static final RegistryObject<Block> RUNIC_ANVIL = BLOCKS.register("runic_anvil", RunicAnvilBlock::new);

    public static final RegistryObject<Block> CLAY_MUG = BLOCKS.register("clay_mug", ClayMugBlock::new);

    public static final RegistryObject<Block> RITUAL_TELEPORT_DESTINATION = BLOCKS.register("ritual_teleport_location", RitualTeleportLocationBlock::new);

    public static final RegistryObject<MagelightBlock> MAGE_LIGHT = BLOCKS.register("mage_light", MagelightBlock::new);

    public static final RegistryObject<IllusionBlock> ILLUSION_BLOCK = BLOCKS.register("illusion_block", IllusionBlock::new);

    public static final RegistryObject<IceSpikeBlock> ICE_SPIKE = BLOCKS.register("ice_spike", IceSpikeBlock::new);

    public static final RegistryObject<ImpaleBlock> IMPALE_SPIKE = BLOCKS.register("impale_block", ImpaleBlock::new);

    public static final RegistryObject<TransitoryTunnelBlock> TRANSITORY_TUNNEL = BLOCKS.register("transitory_tunnel", TransitoryTunnelBlock::new);

    public static final RegistryObject<BrazierBlock> BRAZIER = BLOCKS.register("brazier", BrazierBlock::new);

    public static final RegistryObject<Block> HELLFIRE = BLOCKS.register("hellfire", HellfireBlock::new);

    public static final RegistryObject<ManaCrystalBlock> MANA_CRYSTAL = BLOCKS.register("mana_crystal", ManaCrystalBlock::new);

    public static final RegistryObject<ManaweaveCacheBlock> MANAWEAVE_CACHE = BLOCKS.register("manaweave_cache", ManaweaveCacheBlock::new);

    public static final RegistryObject<OcculusBlock> OCCULUS = BLOCKS.register("occulus", OcculusBlock::new);

    public static final RegistryObject<TransitoryTileBlock> TRANSITORY_TILE = BLOCKS.register("transitory_tile", TransitoryTileBlock::new);

    public static final RegistryObject<ElementalSentryBlock> ARCANE_SENTRY = BLOCKS.register("arcane_sentry", () -> new ElementalSentryBlock(Affinity.ARCANE));

    public static final RegistryObject<ElementalSentryBlock> EARTH_SENTRY = BLOCKS.register("earth_sentry", () -> new ElementalSentryBlock(Affinity.EARTH));

    public static final RegistryObject<ElementalSentryBlock> WATER_SENTRY = BLOCKS.register("water_sentry", () -> new ElementalSentryBlock(Affinity.WATER));

    public static final RegistryObject<ElementalSentryBlock> FIRE_SENTRY = BLOCKS.register("fire_sentry", () -> new ElementalSentryBlock(Affinity.FIRE));

    public static final RegistryObject<ElementalSentryBlock> ENDER_SENTRY = BLOCKS.register("ender_sentry", () -> new ElementalSentryBlock(Affinity.ENDER));

    public static final RegistryObject<ElementalSentryBlock> WIND_SENTRY = BLOCKS.register("wind_sentry", () -> new ElementalSentryBlock(Affinity.WIND));

    public static final RegistryObject<SeerStoneBlock> SEER_STONE = BLOCKS.register("seer_stone", SeerStoneBlock::new);

    public static final RegistryObject<ConstructWorkbenchBlock> CONSTRUCT_WORKBENCH = BLOCKS.register("construct_workbench", ConstructWorkbenchBlock::new);

    public static final RegistryObject<LodestarBlock> LODESTAR = BLOCKS.register("lodestar", () -> new LodestarBlock(false));

    public static final RegistryObject<LodestarBlock> DELEGATION_STATION = BLOCKS.register("delegation_station", () -> new LodestarBlock(true));

    public static final RegistryObject<SlipstreamGeneratorBlock> SLIPSTREAM_GENERATOR = BLOCKS.register("slipstream_generator", SlipstreamGeneratorBlock::new);

    public static final RegistryObject<WardingCandleBlock> WARDING_CANDLE = BLOCKS.register("warding_candle", WardingCandleBlock::new);

    public static final RegistryObject<SpectralCraftingTableBlock> SPECTRAL_CRAFTING_TABLE = BLOCKS.register("spectral_crafting_table", () -> new SpectralCraftingTableBlock(BlockBehaviour.Properties.of().noOcclusion().strength(2.5F).sound(SoundType.WOOD)));

    public static final RegistryObject<SpectralAnvilBlock> SPECTRAL_ANVIL = BLOCKS.register("spectral_anvil", () -> new SpectralAnvilBlock(BlockBehaviour.Properties.of().noOcclusion().strength(2.5F).sound(SoundType.ANVIL)));

    public static final RegistryObject<SpectralStonecutterBlock> SPECTRAL_STONECUTTER = BLOCKS.register("spectral_stonecutter", () -> new SpectralStonecutterBlock(BlockBehaviour.Properties.of().noOcclusion().strength(2.5F).sound(SoundType.ANVIL)));

    public static final RegistryObject<SpectralLadderBlock> SPECTRAL_LADDER = BLOCKS.register("spectral_ladder", () -> new SpectralLadderBlock(BlockBehaviour.Properties.of().noOcclusion().strength(2.5F).sound(SoundType.WOOD)));

    public static final RegistryObject<SpectralWebBlock> SPECTRAL_WEB = BLOCKS.register("spectral_web", () -> new SpectralWebBlock(BlockBehaviour.Properties.of().noCollission().requiresCorrectToolForDrops().strength(4.0F)));

    public static final RegistryObject<CircleOfPowerBlock> CIRCLE_OF_POWER = BLOCKS.register("circle_of_power", () -> new CircleOfPowerBlock());

    public static final RegistryObject<Block> BASIC_TABLE = BLOCKS.register("basic_table", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).noOcclusion().strength(1.0F)));

    public static final RegistryObject<Block> ORNATE_TABLE = BLOCKS.register("ornate_table", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).noOcclusion().strength(1.0F)));

    public static final RegistryObject<CoffinBlock> COFFIN = BLOCKS.register("coffin", () -> new CoffinBlock());

    public static final RegistryObject<MagiciansWorkbenchBlock> MAGICIANS_WORKBENCH = BLOCKS.register("magicians_workbench", () -> new MagiciansWorkbenchBlock());

    public static final RegistryObject<BookshelfBlock> BOOKSHELF = BLOCKS.register("book_shelf", () -> new BookshelfBlock());

    public static final RegistryObject<ScrollshelfBlock> SCROLLSHELF = BLOCKS.register("scroll_shelf", () -> new ScrollshelfBlock());

    public static final RegistryObject<FluidJugBlock> FLUID_JUG = BLOCKS.register("fluid_jug", () -> new FluidJugBlock());

    public static final RegistryObject<FluidJugBlock> FLUID_JUG_INFINITE_WATER = BLOCKS.register("fluid_jug_infinite_water", () -> new FluidJugBlock(true, new ResourceLocation("water")));

    public static final RegistryObject<FluidJugBlock> FLUID_JUG_INFINITE_LAVA = BLOCKS.register("fluid_jug_infinite_lava", () -> new FluidJugBlock(true, new ResourceLocation("lava")));

    public static final RegistryObject<RunicLightBlock> RUNIC_LIGHT = BLOCKS.register("runic_light", () -> new RunicLightBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).noOcclusion().hasPostProcess(BlockInit::needsPostProcessing).emissiveRendering(BlockInit::needsPostProcessing).lightLevel(state -> 15)));

    public static final RegistryObject<RunicTorchBlock> RUNIC_TORCH = BLOCKS.register("runic_torch", () -> new RunicTorchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).noOcclusion().lightLevel(state -> 15)));

    public static final RegistryObject<ThesisDeskBlock> THESIS_DESK = BLOCKS.register("thesis_desk", ThesisDeskBlock::new);

    public static final RegistryObject<AffinityTinkerBlock> AFFINITY_TINKER = BLOCKS.register("affinity_tinker", AffinityTinkerBlock::new);

    public static final RegistryObject<SpellSpecializationBlock> SPELL_SPECIALIZATION = BLOCKS.register("spell_specialization", SpellSpecializationBlock::new);

    public static final RegistryObject<DisenchanterBlock> DISENCHANTER = BLOCKS.register("disenchanter", DisenchanterBlock::new);

    public static final RegistryObject<StudyDeskBlock> STUDY_DESK = BLOCKS.register("study_desk", StudyDeskBlock::new);

    public static final RegistryObject<TranscriptionTableBlock> TRANSCRIPTION_TABLE = BLOCKS.register("spell_infusion", TranscriptionTableBlock::new);

    public static final RegistryObject<EldrinFumeBlock> ELDRIN_FUME = BLOCKS.register("eldrin_fume", EldrinFumeBlock::new);

    public static final RegistryObject<BookStandBlock> BOOK_STAND = BLOCKS.register("book_stand", BookStandBlock::new);

    public static final RegistryObject<FillerBlock> EMPTY_FILLER_BLOCK = BLOCKS.register("filler_block", FillerBlock::new);

    public static final RegistryObject<WellspringPillarBlock> WELLSPRING_PILLAR = BLOCKS.register("wellspring_pillar", WellspringPillarBlock::new);

    public static final RegistryObject<RefractionLensBlock> REFRACTION_LENS_ARCANE = BLOCKS.register("refraction_lens_arcane", () -> new RefractionLensBlock(Affinity.ARCANE));

    public static final RegistryObject<RefractionLensBlock> REFRACTION_LENS_ENDER = BLOCKS.register("refraction_lens_ender", () -> new RefractionLensBlock(Affinity.ENDER));

    public static final RegistryObject<RefractionLensBlock> REFRACTION_LENS_WIND = BLOCKS.register("refraction_lens_wind", () -> new RefractionLensBlock(Affinity.WIND));

    public static final RegistryObject<RefractionLensBlock> REFRACTION_LENS_EARTH = BLOCKS.register("refraction_lens_earth", () -> new RefractionLensBlock(Affinity.EARTH));

    public static final RegistryObject<RefractionLensBlock> REFRACTION_LENS_WATER = BLOCKS.register("refraction_lens_water", () -> new RefractionLensBlock(Affinity.WATER));

    public static final RegistryObject<RefractionLensBlock> REFRACTION_LENS_FIRE = BLOCKS.register("refraction_lens_fire", () -> new RefractionLensBlock(Affinity.FIRE));

    public static final RegistryObject<EldrinAltarBlock> ELDRIN_ALTAR = BLOCKS.register("eldrin_altar", EldrinAltarBlock::new);

    public static final RegistryObject<EldrinConduitBlock> ELDRIN_CONDUIT_FIRE = BLOCKS.register("eldrin_conduit_fire", () -> new EldrinConduitBlock(Affinity.FIRE, false));

    public static final RegistryObject<EldrinConduitBlock> ELDRIN_CONDUIT_WATER = BLOCKS.register("eldrin_conduit_water", () -> new EldrinConduitBlock(Affinity.WATER, false));

    public static final RegistryObject<EldrinConduitBlock> ELDRIN_CONDUIT_EARTH = BLOCKS.register("eldrin_conduit_earth", () -> new EldrinConduitBlock(Affinity.EARTH, false));

    public static final RegistryObject<EldrinConduitBlock> ELDRIN_CONDUIT_AIR = BLOCKS.register("eldrin_conduit_air", () -> new EldrinConduitBlock(Affinity.WIND, false));

    public static final RegistryObject<EldrinConduitBlock> ELDRIN_CONDUIT_ENDER = BLOCKS.register("eldrin_conduit_ender", () -> new EldrinConduitBlock(Affinity.ENDER, false));

    public static final RegistryObject<EldrinConduitBlock> ELDRIN_CONDUIT_ARCANE = BLOCKS.register("eldrin_conduit_arcane", () -> new EldrinConduitBlock(Affinity.ARCANE, false));

    public static final RegistryObject<EldrinConduitBlock> LESSER_ELDRIN_CONDUIT_FIRE = BLOCKS.register("lesser_eldrin_conduit_fire", () -> new EldrinConduitBlock(Affinity.FIRE, true));

    public static final RegistryObject<EldrinConduitBlock> LESSER_ELDRIN_CONDUIT_WATER = BLOCKS.register("lesser_eldrin_conduit_water", () -> new EldrinConduitBlock(Affinity.WATER, true));

    public static final RegistryObject<EldrinConduitBlock> LESSER_ELDRIN_CONDUIT_EARTH = BLOCKS.register("lesser_eldrin_conduit_earth", () -> new EldrinConduitBlock(Affinity.EARTH, true));

    public static final RegistryObject<EldrinConduitBlock> LESSER_ELDRIN_CONDUIT_AIR = BLOCKS.register("lesser_eldrin_conduit_air", () -> new EldrinConduitBlock(Affinity.WIND, true));

    public static final RegistryObject<EldrinConduitBlock> LESSER_ELDRIN_CONDUIT_ENDER = BLOCKS.register("lesser_eldrin_conduit_ender", () -> new EldrinConduitBlock(Affinity.ENDER, true));

    public static final RegistryObject<EldrinConduitBlock> LESSER_ELDRIN_CONDUIT_ARCANE = BLOCKS.register("lesser_eldrin_conduit_arcane", () -> new EldrinConduitBlock(Affinity.ARCANE, true));

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_BLACK = BLOCKS.register("chimerite_crystal_black", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_BLUE = BLOCKS.register("chimerite_crystal_blue", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_BROWN = BLOCKS.register("chimerite_crystal_brown", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_CYAN = BLOCKS.register("chimerite_crystal_cyan", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_GRAY = BLOCKS.register("chimerite_crystal_gray", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_GREEN = BLOCKS.register("chimerite_crystal_green", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_LIGHT_BLUE = BLOCKS.register("chimerite_crystal_light_blue", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_LIGHT_GRAY = BLOCKS.register("chimerite_crystal_light_gray", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_LIME = BLOCKS.register("chimerite_crystal_lime", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_MAGENTA = BLOCKS.register("chimerite_crystal_magenta", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_ORANGE = BLOCKS.register("chimerite_crystal_orange", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_PINK = BLOCKS.register("chimerite_crystal_pink", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_PURPLE = BLOCKS.register("chimerite_crystal_purple", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_RED = BLOCKS.register("chimerite_crystal_red", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_WHITE = BLOCKS.register("chimerite_crystal_white", ChimeriteCrystalBlock::new);

    public static final RegistryObject<ChimeriteCrystalBlock> CHIMERITE_CRYSTAL_YELLOW = BLOCKS.register("chimerite_crystal_yellow", ChimeriteCrystalBlock::new);

    public static final RegistryObject<Block> TRANSMUTED_SILVER_BLOCK = BLOCKS.register("decoration/transmuted_silver_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> TRANSMUTED_SILVER_SLAB = BLOCKS.register("decoration/transmuted_silver_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> TRANSMUTED_SILVER_STAIRS = BLOCKS.register("decoration/transmuted_silver_stairs", () -> new StairBlock(() -> TRANSMUTED_SILVER_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> ARCANE_STONE = BLOCKS.register("decoration/arcane_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> ARCANE_STONE_SLAB = BLOCKS.register("decoration/arcane_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> ARCANE_STONE_WALL = BLOCKS.register("decoration/arcane_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> ARCANE_STONE_STAIRS = BLOCKS.register("decoration/arcane_stone_stairs", () -> new StairBlock(() -> ARCANE_STONE.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> ARCANE_STONE_PILLAR = BLOCKS.register("decoration/arcane_stone_pillar", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_STONE = BLOCKS.register("decoration/vinteum_arcane_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_STONE_STRAIGHT = BLOCKS.register("decoration/vinteum_arcane_stone_straight", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_STONE_CORNER = BLOCKS.register("decoration/vinteum_arcane_stone_corner", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_STONE_T = BLOCKS.register("decoration/vinteum_arcane_stone_t", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_STONE_QUAD = BLOCKS.register("decoration/vinteum_arcane_stone_quad", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_STONE_CHISELED = BLOCKS.register("decoration/vinteum_arcane_stone_chiseled", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_STONE_SLAB = BLOCKS.register("decoration/vinteum_arcane_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_STONE_WALL = BLOCKS.register("decoration/vinteum_arcane_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_STONE_STAIRS = BLOCKS.register("decoration/vinteum_arcane_stone_stairs", () -> new StairBlock(() -> ARCANE_STONE.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_STONE_PILLAR = BLOCKS.register("decoration/vinteum_arcane_stone_pillar", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_STONE = BLOCKS.register("decoration/redstone_arcane_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_STONE_STRAIGHT = BLOCKS.register("decoration/redstone_arcane_stone_straight", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_STONE_CORNER = BLOCKS.register("decoration/redstone_arcane_stone_corner", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_STONE_T = BLOCKS.register("decoration/redstone_arcane_stone_t", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_STONE_QUAD = BLOCKS.register("decoration/redstone_arcane_stone_quad", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_STONE_CHISELED = BLOCKS.register("decoration/redstone_arcane_stone_chiseled", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_STONE_SLAB = BLOCKS.register("decoration/redstone_arcane_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_STONE_WALL = BLOCKS.register("decoration/redstone_arcane_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_STONE_STAIRS = BLOCKS.register("decoration/redstone_arcane_stone_stairs", () -> new StairBlock(() -> ARCANE_STONE.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_STONE_PILLAR = BLOCKS.register("decoration/redstone_arcane_stone_pillar", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_STONE = BLOCKS.register("decoration/chimerite_arcane_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_STONE_STRAIGHT = BLOCKS.register("decoration/chimerite_arcane_stone_straight", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_STONE_CORNER = BLOCKS.register("decoration/chimerite_arcane_stone_corner", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_STONE_T = BLOCKS.register("decoration/chimerite_arcane_stone_t", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_STONE_QUAD = BLOCKS.register("decoration/chimerite_arcane_stone_quad", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_STONE_CHISELED = BLOCKS.register("decoration/chimerite_arcane_stone_chiseled", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_STONE_SLAB = BLOCKS.register("decoration/chimerite_arcane_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_STONE_WALL = BLOCKS.register("decoration/chimerite_arcane_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_STONE_STAIRS = BLOCKS.register("decoration/chimerite_arcane_stone_stairs", () -> new StairBlock(() -> ARCANE_STONE.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_STONE_PILLAR = BLOCKS.register("decoration/chimerite_arcane_stone_pillar", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> ARCANE_SANDSTONE = BLOCKS.register("decoration/arcane_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> ARCANE_SANDSTONE_SLAB = BLOCKS.register("decoration/arcane_sandstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> ARCANE_SANDSTONE_WALL = BLOCKS.register("decoration/arcane_sandstone_wall", () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> ARCANE_SANDSTONE_STAIRS = BLOCKS.register("decoration/arcane_sandstone_stairs", () -> new StairBlock(() -> ARCANE_SANDSTONE.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> ARCANE_SANDSTONE_PILLAR = BLOCKS.register("decoration/arcane_sandstone_pillar", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_SANDSTONE = BLOCKS.register("decoration/vinteum_arcane_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_SANDSTONE_STRAIGHT = BLOCKS.register("decoration/vinteum_arcane_sandstone_straight", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_SANDSTONE_CORNER = BLOCKS.register("decoration/vinteum_arcane_sandstone_corner", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_SANDSTONE_T = BLOCKS.register("decoration/vinteum_arcane_sandstone_t", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_SANDSTONE_QUAD = BLOCKS.register("decoration/vinteum_arcane_sandstone_quad", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_SANDSTONE_CHISELED = BLOCKS.register("decoration/vinteum_arcane_sandstone_chiseled", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_SANDSTONE_SLAB = BLOCKS.register("decoration/vinteum_arcane_sandstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_SANDSTONE_WALL = BLOCKS.register("decoration/vinteum_arcane_sandstone_wall", () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_SANDSTONE_STAIRS = BLOCKS.register("decoration/vinteum_arcane_sandstone_stairs", () -> new StairBlock(() -> ARCANE_SANDSTONE.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> VINTEUM_ARCANE_SANDSTONE_PILLAR = BLOCKS.register("decoration/vinteum_arcane_sandstone_pillar", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_SANDSTONE = BLOCKS.register("decoration/redstone_arcane_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_SANDSTONE_STRAIGHT = BLOCKS.register("decoration/redstone_arcane_sandstone_straight", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_SANDSTONE_CORNER = BLOCKS.register("decoration/redstone_arcane_sandstone_corner", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_SANDSTONE_T = BLOCKS.register("decoration/redstone_arcane_sandstone_t", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_SANDSTONE_QUAD = BLOCKS.register("decoration/redstone_arcane_sandstone_quad", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_SANDSTONE_CHISELED = BLOCKS.register("decoration/redstone_arcane_sandstone_chiseled", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_SANDSTONE_SLAB = BLOCKS.register("decoration/redstone_arcane_sandstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_SANDSTONE_WALL = BLOCKS.register("decoration/redstone_arcane_sandstone_wall", () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_SANDSTONE_STAIRS = BLOCKS.register("decoration/redstone_arcane_sandstone_stairs", () -> new StairBlock(() -> ARCANE_SANDSTONE.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> REDSTONE_ARCANE_SANDSTONE_PILLAR = BLOCKS.register("decoration/redstone_arcane_sandstone_pillar", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_SANDSTONE = BLOCKS.register("decoration/chimerite_arcane_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_SANDSTONE_STRAIGHT = BLOCKS.register("decoration/chimerite_arcane_sandstone_straight", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_SANDSTONE_CORNER = BLOCKS.register("decoration/chimerite_arcane_sandstone_corner", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_SANDSTONE_T = BLOCKS.register("decoration/chimerite_arcane_sandstone_t", () -> new SimpleRotationalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_SANDSTONE_QUAD = BLOCKS.register("decoration/chimerite_arcane_sandstone_quad", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_SANDSTONE_CHISELED = BLOCKS.register("decoration/chimerite_arcane_sandstone_chiseled", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_SANDSTONE_SLAB = BLOCKS.register("decoration/chimerite_arcane_sandstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_SANDSTONE_WALL = BLOCKS.register("decoration/chimerite_arcane_sandstone_wall", () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_SANDSTONE_STAIRS = BLOCKS.register("decoration/chimerite_arcane_sandstone_stairs", () -> new StairBlock(() -> ARCANE_SANDSTONE.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static final RegistryObject<Block> CHIMERITE_ARCANE_SANDSTONE_PILLAR = BLOCKS.register("decoration/chimerite_arcane_sandstone_pillar", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 10.0F).hasPostProcess(ALWAYS).emissiveRendering(ALWAYS)));

    public static boolean needsPostProcessing(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @SubscribeEvent
    public static void FillCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == MACreativeTabs.GENERAL) {
            List<Block> _excluded = Arrays.asList(CIRCLE_OF_POWER.get(), EMPTY_FILLER_BLOCK.get(), HELLFIRE.get(), TRANSITORY_TILE.get(), SPECTRAL_ANVIL.get(), SPECTRAL_CRAFTING_TABLE.get(), SPECTRAL_LADDER.get(), SPECTRAL_STONECUTTER.get(), SPECTRAL_WEB.get(), MAGE_LIGHT.get(), ICE_SPIKE.get(), IMPALE_SPIKE.get(), STORM_CLOUD.get(), RITUAL_TELEPORT_DESTINATION.get(), POTTED_AUM.get(), POTTED_CERUBLOSSOM.get(), POTTED_DESERT_NOVA.get(), POTTED_TARMA_ROOT.get(), POTTED_WAKEBLOOM.get(), CHALK_RUNE.get(), FLUID_JUG.get(), FLUID_JUG_INFINITE_LAVA.get(), FLUID_JUG_INFINITE_WATER.get(), MANAWEAVE_CACHE.get(), WAKEBLOOM.get(), ARCANE_SENTRY.get(), EARTH_SENTRY.get(), ENDER_SENTRY.get(), WATER_SENTRY.get(), FIRE_SENTRY.get(), WIND_SENTRY.get(), REDSTONE_PULSE.get(), TRANSITORY_TUNNEL.get(), CLAY_MUG.get());
            BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> !_excluded.contains(block)).forEach(block -> {
                ItemStack stack = new ItemStack(block);
                if (!stack.isEmpty() && stack.getCount() == 1) {
                    event.m_246342_(stack);
                } else {
                    ManaAndArtifice.LOGGER.warn("unable to put " + block.toString() + " in the creative tab");
                }
            });
        }
    }
}