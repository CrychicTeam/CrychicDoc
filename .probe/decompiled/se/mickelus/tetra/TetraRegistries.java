package se.mickelus.tetra;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.tetra.advancements.BlockInteractionCriterion;
import se.mickelus.tetra.advancements.BlockUseCriterion;
import se.mickelus.tetra.advancements.ImprovementCraftCriterion;
import se.mickelus.tetra.advancements.ModuleCraftCriterion;
import se.mickelus.tetra.blocks.InitializableBlock;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.blocks.forged.ForgedCrateBlock;
import se.mickelus.tetra.blocks.forged.ForgedPillarBlock;
import se.mickelus.tetra.blocks.forged.ForgedPlatformBlock;
import se.mickelus.tetra.blocks.forged.ForgedPlatformSlabBlock;
import se.mickelus.tetra.blocks.forged.ForgedVentBlock;
import se.mickelus.tetra.blocks.forged.ForgedWallBlock;
import se.mickelus.tetra.blocks.forged.ForgedWorkbenchBlock;
import se.mickelus.tetra.blocks.forged.chthonic.ChthonicExtractorBlock;
import se.mickelus.tetra.blocks.forged.chthonic.ChthonicExtractorTile;
import se.mickelus.tetra.blocks.forged.chthonic.DepletedBedrockBlock;
import se.mickelus.tetra.blocks.forged.chthonic.ExtractorProjectileEntity;
import se.mickelus.tetra.blocks.forged.chthonic.FracturedBedrockBlock;
import se.mickelus.tetra.blocks.forged.chthonic.FracturedBedrockTile;
import se.mickelus.tetra.blocks.forged.container.ForgedContainerBlock;
import se.mickelus.tetra.blocks.forged.container.ForgedContainerBlockEntity;
import se.mickelus.tetra.blocks.forged.container.ForgedContainerMenu;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorBaseBlock;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorBaseBlockEntity;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorPipeBlock;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorPistonBlock;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorPistonBlockEntity;
import se.mickelus.tetra.blocks.forged.extractor.SeepingBedrockBlock;
import se.mickelus.tetra.blocks.forged.hammer.HammerBaseBlock;
import se.mickelus.tetra.blocks.forged.hammer.HammerBaseBlockEntity;
import se.mickelus.tetra.blocks.forged.hammer.HammerHeadBlock;
import se.mickelus.tetra.blocks.forged.hammer.HammerHeadBlockEntity;
import se.mickelus.tetra.blocks.forged.transfer.TransferUnitBlock;
import se.mickelus.tetra.blocks.forged.transfer.TransferUnitBlockEntity;
import se.mickelus.tetra.blocks.geode.GeodeBlock;
import se.mickelus.tetra.blocks.geode.GeodeItem;
import se.mickelus.tetra.blocks.geode.PristineAmethystItem;
import se.mickelus.tetra.blocks.geode.PristineDiamondItem;
import se.mickelus.tetra.blocks.geode.PristineEmeraldItem;
import se.mickelus.tetra.blocks.geode.PristineLapisItem;
import se.mickelus.tetra.blocks.geode.PristineQuartzItem;
import se.mickelus.tetra.blocks.holo.HolosphereBlock;
import se.mickelus.tetra.blocks.holo.HolosphereBlockEntity;
import se.mickelus.tetra.blocks.multischematic.MultiblockSchematicBlock;
import se.mickelus.tetra.blocks.rack.RackBlock;
import se.mickelus.tetra.blocks.rack.RackTile;
import se.mickelus.tetra.blocks.salvage.InteractiveBlockOverlay;
import se.mickelus.tetra.blocks.scroll.OpenScrollBlock;
import se.mickelus.tetra.blocks.scroll.RolledScrollBlock;
import se.mickelus.tetra.blocks.scroll.ScrollItem;
import se.mickelus.tetra.blocks.scroll.ScrollTile;
import se.mickelus.tetra.blocks.scroll.WallScrollBlock;
import se.mickelus.tetra.blocks.workbench.BasicWorkbenchBlock;
import se.mickelus.tetra.blocks.workbench.WorkbenchContainer;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.client.particle.SweepingStrikeParticleType;
import se.mickelus.tetra.crafting.ScrollIngredient;
import se.mickelus.tetra.effect.howling.HowlingPotionEffect;
import se.mickelus.tetra.effect.potion.BleedingPotionEffect;
import se.mickelus.tetra.effect.potion.EarthboundPotionEffect;
import se.mickelus.tetra.effect.potion.ExhaustedPotionEffect;
import se.mickelus.tetra.effect.potion.MiningSpeedPotionEffect;
import se.mickelus.tetra.effect.potion.PriedPotionEffect;
import se.mickelus.tetra.effect.potion.PuncturedPotionEffect;
import se.mickelus.tetra.effect.potion.SeveredPotionEffect;
import se.mickelus.tetra.effect.potion.SmallAbsorbPotionEffect;
import se.mickelus.tetra.effect.potion.SmallHealthPotionEffect;
import se.mickelus.tetra.effect.potion.SmallStrengthPotionEffect;
import se.mickelus.tetra.effect.potion.SteeledPotionEffect;
import se.mickelus.tetra.effect.potion.StunPotionEffect;
import se.mickelus.tetra.effect.potion.UnwaveringPotionEffect;
import se.mickelus.tetra.items.InitializableItem;
import se.mickelus.tetra.items.cell.ThermalCellItem;
import se.mickelus.tetra.items.forged.BeamItem;
import se.mickelus.tetra.items.forged.BoltItem;
import se.mickelus.tetra.items.forged.CombustionChamberItem;
import se.mickelus.tetra.items.forged.EarthpiercerItem;
import se.mickelus.tetra.items.forged.InsulatedPlateItem;
import se.mickelus.tetra.items.forged.LubricantDispenserItem;
import se.mickelus.tetra.items.forged.MeshItem;
import se.mickelus.tetra.items.forged.MetalScrapItem;
import se.mickelus.tetra.items.forged.PlanarStabilizerItem;
import se.mickelus.tetra.items.forged.QuickLatchItem;
import se.mickelus.tetra.items.forged.StonecutterItem;
import se.mickelus.tetra.items.loot.DragonSinewItem;
import se.mickelus.tetra.items.modular.EffectItemPredicate;
import se.mickelus.tetra.items.modular.ItemPredicateModular;
import se.mickelus.tetra.items.modular.MaterialItemPredicate;
import se.mickelus.tetra.items.modular.ThrownModularItemEntity;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ShootableDummyItem;
import se.mickelus.tetra.items.modular.impl.dynamic.DynamicModularItem;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.ModularToolbeltItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltContainer;
import se.mickelus.tetra.items.modular.impl.toolbelt.suspend.SuspendPotionEffect;
import se.mickelus.tetra.levelgen.ForgedContainerProcessor;
import se.mickelus.tetra.levelgen.ForgedCrateProcessor;
import se.mickelus.tetra.levelgen.ForgedHammerProcessor;
import se.mickelus.tetra.levelgen.MultiblockSchematicProcessor;
import se.mickelus.tetra.levelgen.TransferUnitProcessor;
import se.mickelus.tetra.loot.FortuneBonusCondition;
import se.mickelus.tetra.loot.ReplaceTableModifier;
import se.mickelus.tetra.loot.ScrollDataFunction;

public class TetraRegistries {

    public static final DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, "tetra");

    public static final DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, "tetra");

    public static final DeferredRegister<BlockEntityType<?>> blockEntities = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "tetra");

    public static final DeferredRegister<MenuType<?>> containers = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "tetra");

    public static final DeferredRegister<EntityType<?>> entities = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "tetra");

    public static final DeferredRegister<ParticleType<?>> particles = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "tetra");

    public static final DeferredRegister<MobEffect> effects = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "tetra");

    public static final DeferredRegister<SoundEvent> sounds = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "tetra");

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> lootModifiers = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, "tetra");

    public static final DeferredRegister<LootItemConditionType> lootConditions = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, "tetra");

    public static final DeferredRegister<LootItemFunctionType> lootFunctions = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, "tetra");

    public static final DeferredRegister<StructureProcessorType<?>> structureProcessors = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, "tetra");

    public static final DeferredRegister<CreativeModeTab> creativeTabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "tetra");

    public static final TagKey<Block> forgeHammerBreakTag = BlockTags.create(new ResourceLocation("tetra:needs_forge_hammer_tool"));

    public static final Tier forgeHammerTier = TierSortingRegistry.registerTier(new ForgeTier(Tiers.NETHERITE.getLevel() + 1, 0, 0.0F, 0.0F, 0, forgeHammerBreakTag, () -> Ingredient.EMPTY), new ResourceLocation("tetra:maxed_forge_hammer"), List.of(Tiers.NETHERITE), List.of());

    private static Item.Properties itemProperties;

    private static RegistryObject<CreativeModeTab> defaultCreativeTabs;

    private static RegistryObject<BasicWorkbenchBlock> basicWorkbench;

    private static RegistryObject<SeepingBedrockBlock> seepingBedrock;

    private static RegistryObject<RackBlock> rack;

    private static RegistryObject<BlockItem> chthonicExtractorItem;

    private static RegistryObject<FracturedBedrockBlock> fracturedBedrock;

    private static RegistryObject<ForgedWallBlock> forgedWall;

    private static RegistryObject<ForgedPillarBlock> forgedPillar;

    private static RegistryObject<ForgedPlatformBlock> forgedPlatform;

    private static RegistryObject<ForgedPlatformSlabBlock> forgedPlatformSlab;

    private static RegistryObject<ForgedVentBlock> forgedVent;

    private static RegistryObject<HammerBaseBlock> forgeHammer;

    private static RegistryObject<ForgedWorkbenchBlock> forgedWorkbench;

    private static RegistryObject<ForgedCrateBlock> forgedCrate;

    private static RegistryObject<TransferUnitBlock> transferUnit;

    private static RegistryObject<BoltItem> bolt;

    private static RegistryObject<DragonSinewItem> dragonSinew;

    private static RegistryObject<StonecutterItem> stonecutter;

    private static RegistryObject<EarthpiercerItem> earthpiercer;

    private static RegistryObject<ModularHolosphereItem> modularHolosphere;

    private static RegistryObject<PlanarStabilizerItem> planarStabilizer;

    private static RegistryObject<InsulatedPlateItem> insulatedPlate;

    private static RegistryObject<QuickLatchItem> quickLatch;

    private static RegistryObject<MeshItem> mesh;

    private static RegistryObject<BeamItem> beam;

    private static RegistryObject<PristineDiamondItem> pristineDiamond;

    private static RegistryObject<PristineEmeraldItem> pristineEmerald;

    private static RegistryObject<PristineLapisItem> pristineLapis;

    private static RegistryObject<PristineAmethystItem> pristineAmethyst;

    private static RegistryObject<PristineQuartzItem> pristineQuartz;

    private static RegistryObject<GeodeItem> geode;

    public static void init(IEventBus bus) {
        bus.register(TetraRegistries.class);
        blocks.register(bus);
        items.register(bus);
        blockEntities.register(bus);
        entities.register(bus);
        particles.register(bus);
        containers.register(bus);
        effects.register(bus);
        sounds.register(bus);
        lootConditions.register(bus);
        lootFunctions.register(bus);
        lootModifiers.register(bus);
        structureProcessors.register(bus);
        creativeTabs.register(bus);
        itemProperties = new Item.Properties();
        defaultCreativeTabs = creativeTabs.register("default", () -> CreativeModeTab.builder().icon(() -> new ItemStack(GeodeItem.instance)).title(Component.translatable("itemGroup.tetra")).build());
        basicWorkbench = blocks.register("basic_workbench", BasicWorkbenchBlock::new);
        registerBlockItem(basicWorkbench);
        HolosphereBlock.instance = blocks.register("holosphere", HolosphereBlock::new);
        rack = blocks.register("rack", RackBlock::new);
        registerBlockItem(rack);
        RegistryObject<RolledScrollBlock> rolledScroll = blocks.register("scroll_rolled", RolledScrollBlock::new);
        RegistryObject<WallScrollBlock> wallScroll = blocks.register("scroll_wall", WallScrollBlock::new);
        RegistryObject<OpenScrollBlock> openScroll = blocks.register("scroll_open", OpenScrollBlock::new);
        forgedWall = blocks.register("forged_wall", ForgedWallBlock::new);
        registerBlockItem(forgedWall);
        forgedPillar = blocks.register("forged_pillar", ForgedPillarBlock::new);
        registerBlockItem(forgedPillar);
        forgedPlatform = blocks.register("forged_platform", ForgedPlatformBlock::new);
        registerBlockItem(forgedPlatform);
        forgedPlatformSlab = blocks.register("forged_platform_slab", ForgedPlatformSlabBlock::new);
        registerBlockItem(forgedPlatformSlab);
        forgedVent = blocks.register("forged_vent", ForgedVentBlock::new);
        registerBlockItem(forgedVent);
        blocks.register("hammer_head", HammerHeadBlock::new);
        forgeHammer = blocks.register("hammer_base", HammerBaseBlock::new);
        registerBlockItem(forgeHammer);
        forgedWorkbench = blocks.register("forged_workbench", ForgedWorkbenchBlock::new);
        registerBlockItem(forgedWorkbench);
        ForgedContainerBlock.instance = blocks.register("forged_container", ForgedContainerBlock::new);
        registerBlockItem(ForgedContainerBlock.instance);
        forgedCrate = blocks.register("forged_crate", ForgedCrateBlock::new);
        registerBlockItem(forgedCrate);
        transferUnit = blocks.register("transfer_unit", TransferUnitBlock::new);
        registerBlockItem(transferUnit);
        RegistryObject<ChthonicExtractorBlock> chthonicExtractor = blocks.register("chthonic_extractor", ChthonicExtractorBlock::new);
        chthonicExtractorItem = ChthonicExtractorBlock.registerItems(items);
        fracturedBedrock = blocks.register("fractured_bedrock", FracturedBedrockBlock::new);
        blocks.register("depleted_bedrock", DepletedBedrockBlock::new);
        CoreExtractorBaseBlock.instance = blocks.register("core_extractor", CoreExtractorBaseBlock::new);
        registerBlockItem(CoreExtractorBaseBlock.instance);
        CoreExtractorPistonBlock.instance = blocks.register("extractor_piston", CoreExtractorPistonBlock::new);
        registerBlockItem(blocks.register("extractor_pipe", CoreExtractorPipeBlock::new));
        seepingBedrock = blocks.register("seeping_bedrock", SeepingBedrockBlock::new);
        registerBlockItem(seepingBedrock);
        new MultiblockSchematicBlock.Builder("stonecutter", 3, 2, ForgedBlockCommon.propertiesSolid).build(blocks, items);
        new MultiblockSchematicBlock.Builder("earthpiercer", 2, 2, ForgedBlockCommon.propertiesSolid).build(blocks, items);
        new MultiblockSchematicBlock.Builder("extractor", 3, 3, ForgedBlockCommon.propertiesSolid).build(blocks, items);
        blocks.register("block_geode", GeodeBlock::new);
        items.register("modular_sword", ModularBladedItem::new);
        items.register("modular_double", ModularDoubleHeadedItem::new);
        items.register("modular_bow", ModularBowItem::new);
        RegistryObject<Item> shootableDummy = items.register("shootable_dummy", ShootableDummyItem::new);
        items.register("modular_crossbow", () -> new ModularCrossbowItem(shootableDummy.get()));
        items.register("modular_single", ModularSingleHeadedItem::new);
        items.register("modular_shield", ModularShieldItem::new);
        ModularToolbeltItem.instance = items.register("modular_toolbelt", ModularToolbeltItem::new);
        modularHolosphere = items.register("holo", ModularHolosphereItem::new);
        items.register("dynamic_handheld", DynamicModularItem::new);
        geode = items.register("geode", GeodeItem::new);
        pristineLapis = items.register("pristine_lapis", PristineLapisItem::new);
        pristineEmerald = items.register("pristine_emerald", PristineEmeraldItem::new);
        pristineDiamond = items.register("pristine_diamond", PristineDiamondItem::new);
        pristineAmethyst = items.register("pristine_amethyst", PristineAmethystItem::new);
        pristineQuartz = items.register("pristine_quartz", PristineQuartzItem::new);
        dragonSinew = items.register("dragon_sinew", DragonSinewItem::new);
        bolt = items.register("forged_bolt", BoltItem::new);
        beam = items.register("forged_beam", BeamItem::new);
        mesh = items.register("forged_mesh", MeshItem::new);
        quickLatch = items.register("quick_latch", QuickLatchItem::new);
        MetalScrapItem.instance = items.register("metal_scrap", MetalScrapItem::new);
        insulatedPlate = items.register("vent_plate", InsulatedPlateItem::new);
        planarStabilizer = items.register("planar_stabilizer", PlanarStabilizerItem::new);
        ThermalCellItem.instance = items.register("thermal_cell", ThermalCellItem::new);
        CombustionChamberItem.instance = items.register("combustion_chamber", CombustionChamberItem::new);
        LubricantDispenserItem.instance = items.register("lubricant_dispenser", LubricantDispenserItem::new);
        earthpiercer = items.register("earthpiercer", EarthpiercerItem::new);
        stonecutter = items.register("stonecutter", StonecutterItem::new);
        items.register("scroll_rolled", () -> new ScrollItem(rolledScroll.get()));
        WorkbenchTile.type = blockEntities.register("workbench", () -> BlockEntityType.Builder.of(WorkbenchTile::new, basicWorkbench.get(), forgedWorkbench.get()).build(null));
        ChthonicExtractorTile.type = blockEntities.register("chthonic_extractor", () -> BlockEntityType.Builder.of(ChthonicExtractorTile::new, chthonicExtractor.get()).build(null));
        blockEntities.register("fractured_bedrock", () -> BlockEntityType.Builder.of(FracturedBedrockTile::new, fracturedBedrock.get()).build(null));
        blockEntities.register("rack", () -> BlockEntityType.Builder.of(RackTile::new, rack.get()).build(null));
        blockEntities.register("scroll", () -> BlockEntityType.Builder.of(ScrollTile::new, openScroll.get(), wallScroll.get(), rolledScroll.get()).build(null));
        HammerBaseBlockEntity.type = blockEntities.register("hammer_base", () -> BlockEntityType.Builder.of(HammerBaseBlockEntity::new, HammerBaseBlock.instance).build(null));
        HammerHeadBlockEntity.type = blockEntities.register("hammer_head", () -> BlockEntityType.Builder.of(HammerHeadBlockEntity::new, HammerHeadBlock.instance).build(null));
        TransferUnitBlockEntity.type = blockEntities.register("transfer_unit", () -> BlockEntityType.Builder.of(TransferUnitBlockEntity::new, transferUnit.get()).build(null));
        blockEntities.register("core_extractor", () -> BlockEntityType.Builder.of(CoreExtractorBaseBlockEntity::new, CoreExtractorBaseBlock.instance.get()).build(null));
        CoreExtractorPistonBlockEntity.type = blockEntities.register("extractor_piston", () -> BlockEntityType.Builder.of(CoreExtractorPistonBlockEntity::new, CoreExtractorPistonBlock.instance.get()).build(null));
        ForgedContainerBlockEntity.type = blockEntities.register("forged_container", () -> BlockEntityType.Builder.of(ForgedContainerBlockEntity::new, ForgedContainerBlock.instance.get()).build(null));
        HolosphereBlockEntity.type = blockEntities.register("holosphere", () -> BlockEntityType.Builder.of(HolosphereBlockEntity::new, HolosphereBlock.instance.get()).build(null));
        entities.register("thrown_modular_item", () -> EntityType.Builder.of(ThrownModularItemEntity::new, MobCategory.MISC).setCustomClientFactory(ThrownModularItemEntity::new).sized(0.5F, 0.5F).build("thrown_modular_item"));
        entities.register("extractor_projectile", () -> EntityType.Builder.of(ExtractorProjectileEntity::new, MobCategory.MISC).setCustomClientFactory(ExtractorProjectileEntity::new).sized(0.5F, 0.5F).build("extractor_projectile"));
        particles.register("sparkle", () -> new SimpleParticleType(false));
        particles.register("sweeping_strike", SweepingStrikeParticleType::new);
        ToolbeltContainer.type = containers.register("modular_toolbelt", () -> IForgeMenuType.create((windowId, inv, data) -> ToolbeltContainer.create(windowId, inv)));
        WorkbenchContainer.containerType = containers.register("workbench", () -> IForgeMenuType.create((windowId, inv, data) -> WorkbenchContainer.create(windowId, data.readBlockPos(), inv)));
        ForgedContainerMenu.type = containers.register("forged_container", () -> IForgeMenuType.create((windowId, inv, data) -> ForgedContainerMenu.create(windowId, data.readBlockPos(), inv)));
        effects.register("bleeding", BleedingPotionEffect::new);
        effects.register("earthbound", EarthboundPotionEffect::new);
        effects.register("stun", StunPotionEffect::new);
        effects.register("howling", HowlingPotionEffect::new);
        effects.register("severed", SeveredPotionEffect::new);
        effects.register("punctured", PuncturedPotionEffect::new);
        effects.register("pried", PriedPotionEffect::new);
        effects.register("exhausted", ExhaustedPotionEffect::new);
        effects.register("steeled", SteeledPotionEffect::new);
        effects.register("small_strength", SmallStrengthPotionEffect::new);
        effects.register("unwavering", UnwaveringPotionEffect::new);
        effects.register("small_health", SmallHealthPotionEffect::new);
        effects.register("small_absorb", SmallAbsorbPotionEffect::new);
        effects.register("suspended", SuspendPotionEffect::new);
        effects.register("mining_speed", MiningSpeedPotionEffect::new);
        sounds.register(TetraSounds.scanHit.getLocation().getPath(), () -> TetraSounds.scanHit);
        sounds.register(TetraSounds.scanMiss.getLocation().getPath(), () -> TetraSounds.scanMiss);
        FortuneBonusCondition.type = lootConditions.register("random_chance_with_fortune", () -> new LootItemConditionType(new FortuneBonusCondition.ConditionSerializer()));
        ScrollDataFunction.type = lootFunctions.register("scroll", () -> new LootItemFunctionType(new ScrollDataFunction.Serializer()));
        lootModifiers.register("replace_table", ReplaceTableModifier.codec);
        ForgedHammerProcessor.type = registerStructureProcessor("hammer", () -> ForgedHammerProcessor.codec);
        ForgedCrateProcessor.type = registerStructureProcessor("crate", () -> ForgedCrateProcessor.codec);
        ForgedContainerProcessor.type = registerStructureProcessor("container", () -> ForgedContainerProcessor.codec);
        TransferUnitProcessor.type = registerStructureProcessor("transfer_unit", () -> TransferUnitProcessor.codec);
        MultiblockSchematicProcessor.type = registerStructureProcessor("multiblock_schematic", () -> MultiblockSchematicProcessor.codec);
        CraftingHelper.register(new ResourceLocation("tetra", "scroll"), ScrollIngredient.Serializer.instance);
    }

    public static <B extends Block> RegistryObject<Item> registerBlockItem(RegistryObject<B> block) {
        return items.register(block.getId().getPath(), () -> new BlockItem(block.get(), itemProperties));
    }

    public static <P extends StructureProcessor> RegistryObject<StructureProcessorType<?>> registerStructureProcessor(String id, StructureProcessorType<P> type) {
        return structureProcessors.register(id, () -> type);
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            try {
                CriteriaTriggers.register(BlockUseCriterion.trigger);
                CriteriaTriggers.register(BlockInteractionCriterion.trigger);
                CriteriaTriggers.register(ModuleCraftCriterion.trigger);
                CriteriaTriggers.register(ImprovementCraftCriterion.trigger);
                ItemPredicate.register(new ResourceLocation("tetra:modular_item"), ItemPredicateModular::new);
                ItemPredicate.register(new ResourceLocation("tetra:item_effect"), EffectItemPredicate::new);
                ItemPredicate.register(new ResourceLocation("tetra:material"), MaterialItemPredicate::new);
                ItemPredicate.register(new ResourceLocation("tetra:loose"), LooseItemPredicate::new);
            } catch (Exception var1) {
                var1.printStackTrace();
            }
        });
        blocks.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof InitializableBlock).map(block -> (InitializableBlock) block).forEach(block -> block.commonInit(TetraMod.packetHandler));
        items.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof InitializableItem).map(item -> (InitializableItem) item).forEach(item -> item.commonInit(TetraMod.packetHandler));
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == defaultCreativeTabs.getKey()) {
            event.accept(basicWorkbench);
            event.m_246342_(ModularHolosphereItem.getCreativeItemStack());
            event.accept(rack);
            event.m_246601_(ModularDoubleHeadedItem.getCreativeTabItemStacks());
            event.m_246601_(ModularBladedItem.getCreativeTabItemStacks());
            event.m_246601_(ModularToolbeltItem.getCreativeTabItemStacks());
            event.accept(geode);
            event.accept(pristineLapis);
            event.accept(pristineEmerald);
            event.accept(pristineDiamond);
            event.accept(pristineAmethyst);
            event.accept(dragonSinew);
            event.m_246601_(ScrollItem.instance.getCreativeTabItems());
            event.accept(bolt);
            event.accept(beam);
            event.accept(mesh);
            event.accept(quickLatch);
            event.accept(MetalScrapItem.instance);
            event.accept(insulatedPlate);
            event.accept(planarStabilizer);
            event.accept(CombustionChamberItem.instance);
            event.accept(LubricantDispenserItem.instance);
            event.accept(ThermalCellItem.instance);
            event.accept(earthpiercer);
            event.accept(stonecutter);
            event.accept(chthonicExtractorItem);
            event.accept(forgedWall);
            event.accept(forgedPillar);
            event.accept(forgedPlatform);
            event.accept(forgedPlatformSlab);
            event.accept(forgedVent);
            event.accept(forgeHammer);
            event.accept(forgedWorkbench);
            event.accept(ForgedContainerBlock.instance);
            event.accept(forgedCrate);
            event.accept(transferUnit);
            event.accept(CoreExtractorBaseBlock.instance);
            event.m_246326_(CoreExtractorPipeBlock.instance);
            event.accept(seepingBedrock);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            try {
                blocks.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof InitializableBlock).map(block -> (InitializableBlock) block).forEach(InitializableBlock::clientInit);
                items.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof InitializableItem).map(item -> (InitializableItem) item).forEach(InitializableItem::clientInit);
                MinecraftForge.EVENT_BUS.register(new InteractiveBlockOverlay());
            } catch (Exception var1) {
                var1.printStackTrace();
            }
        });
    }
}