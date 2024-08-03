package com.rekindled.embers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.logging.LogUtils;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.apiimpl.EmbersAPIImpl;
import com.rekindled.embers.augment.ShiftingScalesAugment;
import com.rekindled.embers.augment.WindingGearsAugment;
import com.rekindled.embers.blockentity.render.AlchemyPedestalBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.AlchemyPedestalTopBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.AlchemyTabletBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.AtmosphericBellowsBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.AutomaticHammerBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.BinBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.CatalyticPlugBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.CinderPlinthBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.CopperChargerBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.CrystalCellBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.CrystalSeedBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.DawnstoneAnvilBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.EmberBoreBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.EntropicEnumeratorBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.ExcavationBucketsBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.FieldChartBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.FluidTransferBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.FluidVesselBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.GeologicSeparatorBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.InfernoForgeTopBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.ItemTransferBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.MechanicalPumpBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.MelterTopBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.MithrilBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.MixerCentrifugeBottomBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.MixerCentrifugeTopBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.MnemonicInscriberBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.ReservoirBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.StampBaseBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.StamperBlockEntityRenderer;
import com.rekindled.embers.compat.curios.CuriosCompat;
import com.rekindled.embers.datagen.EmbersBiomeModifiers;
import com.rekindled.embers.datagen.EmbersBlockStates;
import com.rekindled.embers.datagen.EmbersBlockTags;
import com.rekindled.embers.datagen.EmbersConfiguredFeatures;
import com.rekindled.embers.datagen.EmbersDamageTypeTags;
import com.rekindled.embers.datagen.EmbersDamageTypes;
import com.rekindled.embers.datagen.EmbersFluidTags;
import com.rekindled.embers.datagen.EmbersItemModels;
import com.rekindled.embers.datagen.EmbersItemTags;
import com.rekindled.embers.datagen.EmbersLang;
import com.rekindled.embers.datagen.EmbersLootModifiers;
import com.rekindled.embers.datagen.EmbersLootTables;
import com.rekindled.embers.datagen.EmbersPlacedFeatures;
import com.rekindled.embers.datagen.EmbersRecipes;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.datagen.EmbersStructures;
import com.rekindled.embers.entity.AncientGolemEntity;
import com.rekindled.embers.entity.render.AncientGolemRenderer;
import com.rekindled.embers.entity.render.EmberPacketRenderer;
import com.rekindled.embers.entity.render.EmberProjectileRenderer;
import com.rekindled.embers.entity.render.GlimmerProjectileRenderer;
import com.rekindled.embers.gui.SlateScreen;
import com.rekindled.embers.item.EmberStorageItem;
import com.rekindled.embers.item.TyrfingItem;
import com.rekindled.embers.model.AncientGolemModel;
import com.rekindled.embers.model.AshenArmorModel;
import com.rekindled.embers.network.PacketHandler;
import com.rekindled.embers.particle.AlchemyCircleParticle;
import com.rekindled.embers.particle.GlowParticle;
import com.rekindled.embers.particle.SmokeParticle;
import com.rekindled.embers.particle.SparkParticle;
import com.rekindled.embers.particle.StarParticle;
import com.rekindled.embers.particle.TyrfingParticle;
import com.rekindled.embers.particle.VaporParticle;
import com.rekindled.embers.particle.XRayGlowParticle;
import com.rekindled.embers.recipe.AugmentIngredient;
import com.rekindled.embers.recipe.HeatIngredient;
import com.rekindled.embers.render.EmbersRenderTypes;
import com.rekindled.embers.render.PipeModel;
import com.rekindled.embers.research.ResearchManager;
import com.rekindled.embers.research.capability.IResearchCapability;
import com.rekindled.embers.util.AugmentPredicate;
import com.rekindled.embers.util.DecimalFormats;
import com.rekindled.embers.util.GlowingTextTooltip;
import com.rekindled.embers.util.HeatBarTooltip;
import com.rekindled.embers.util.MatchCurioLootCondition;
import com.rekindled.embers.util.Misc;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterNamedRenderTypesEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod("embers")
public class Embers {

    public static final String MODID_OLD = "embersrekindled";

    public static final String MODID = "embers";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Embers() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::registerCaps);
        modEventBus.addListener(this::entityAttributes);
        modEventBus.addListener(this::spawnPlacements);
        modEventBus.addListener(this::registerRecipeSerializers);
        EmbersAPIImpl.init();
        RegistryManager.BLOCKS.register(modEventBus);
        RegistryManager.ITEMS.register(modEventBus);
        RegistryManager.FLUIDTYPES.register(modEventBus);
        RegistryManager.FLUIDS.register(modEventBus);
        RegistryManager.ENTITY_TYPES.register(modEventBus);
        RegistryManager.BLOCK_ENTITY_TYPES_NEW.register(modEventBus);
        RegistryManager.BLOCK_ENTITY_TYPES_OLD.register(modEventBus);
        RegistryManager.CREATIVE_TABS.register(modEventBus);
        RegistryManager.PARTICLE_TYPES.register(modEventBus);
        RegistryManager.SOUND_EVENTS.register(modEventBus);
        RegistryManager.RECIPE_TYPES.register(modEventBus);
        RegistryManager.RECIPE_SERIALIZERS.register(modEventBus);
        RegistryManager.LOOT_MODIFIERS.register(modEventBus);
        RegistryManager.MENU_TYPES.register(modEventBus);
        RegistryManager.STRUCTURE_TYPES.register(modEventBus);
        RegistryManager.STRUCTURE_PROCESSOR_TYPES.register(modEventBus);
        EmbersSounds.init();
        ConfigManager.register();
        if (ModList.get().isLoaded("curios")) {
            CuriosCompat.init();
        }
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        PacketHandler.init();
        RegistryManager.init(event);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, ResearchManager::attachCapability);
        MinecraftForge.EVENT_BUS.addListener(ResearchManager::onClone);
        ResearchManager.initResearches();
        MinecraftForge.EVENT_BUS.addListener(EmbersEvents::fixMappings);
        MinecraftForge.EVENT_BUS.addListener(EmbersEvents::onJoin);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, EmbersEvents::onEntityDamaged);
        MinecraftForge.EVENT_BUS.addListener(EmbersEvents::onBlockBreak);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, EmbersEvents::onProjectileFired);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, EmbersEvents::onArrowLoose);
        MinecraftForge.EVENT_BUS.addListener(EmbersEvents::onAnvilUpdate);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, TagsUpdatedEvent.class, e -> Misc.tagItems.clear());
        MinecraftForge.EVENT_BUS.addListener(EmbersEvents::onLevelLoad);
        MinecraftForge.EVENT_BUS.addListener(EmbersEvents::onServerTick);
        MinecraftForge.EVENT_BUS.addListener(EmbersEvents::onExplosion);
    }

    public void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(IEmberCapability.class);
        event.register(IResearchCapability.class);
        event.register(ShiftingScalesAugment.IScalesCapability.class);
    }

    public void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(RegistryManager.ANCIENT_GOLEM.get(), AncientGolemEntity.createAttributes().build());
    }

    public void spawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(RegistryManager.ANCIENT_GOLEM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        if (event.includeClient()) {
            gen.addProvider(true, new EmbersLang(output));
            ItemModelProvider itemModels = new EmbersItemModels(output, existingFileHelper);
            gen.addProvider(true, itemModels);
            gen.addProvider(true, new EmbersBlockStates(output, existingFileHelper));
            gen.addProvider(true, new EmbersSounds(output, existingFileHelper));
        }
        if (event.includeServer()) {
            gen.addProvider(true, new EmbersLootTables(output));
            gen.addProvider(true, new EmbersRecipes(output));
            BlockTagsProvider blockTags = new EmbersBlockTags(output, lookupProvider, existingFileHelper);
            gen.addProvider(true, blockTags);
            gen.addProvider(true, new EmbersItemTags(output, lookupProvider, blockTags.m_274426_(), existingFileHelper));
            gen.addProvider(true, new EmbersFluidTags(output, lookupProvider, existingFileHelper));
            gen.addProvider(true, new DatapackBuiltinEntriesProvider(output, lookupProvider, new RegistrySetBuilder().add(Registries.CONFIGURED_FEATURE, bootstrap -> EmbersConfiguredFeatures.generate(bootstrap)).add(Registries.PLACED_FEATURE, EmbersPlacedFeatures::generate).add(ForgeRegistries.Keys.BIOME_MODIFIERS, EmbersBiomeModifiers::generate).add(Registries.DAMAGE_TYPE, EmbersDamageTypes::generate).add(Registries.PROCESSOR_LIST, EmbersStructures::generateProcessors).add(Registries.TEMPLATE_POOL, EmbersStructures::generatePools).add(Registries.STRUCTURE, EmbersStructures::generateStructures).add(Registries.STRUCTURE_SET, EmbersStructures::generateSets), Set.of("embers")));
            gen.addProvider(true, new EmbersDamageTypeTags(output, lookupProvider, existingFileHelper));
            gen.addProvider(true, new EmbersLootModifiers(output));
        }
    }

    public void registerRecipeSerializers(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            ItemPredicate.register(AugmentPredicate.ID, AugmentPredicate::deserialize);
            CraftingHelper.register(new ResourceLocation("embers", "has_heat"), HeatIngredient.Serializer.INSTANCE);
            CraftingHelper.register(new ResourceLocation("embers", "has_augment"), AugmentIngredient.Serializer.INSTANCE);
        }
        if (event.getRegistryKey().equals(Registries.LOOT_CONDITION_TYPE)) {
            event.register(Registries.LOOT_CONDITION_TYPE, new ResourceLocation("embers", "match_curio"), () -> MatchCurioLootCondition.LOOT_CONDITION_TYPE);
        }
    }

    @EventBusSubscriber(modid = "embers", bus = Bus.MOD)
    public static class ClientModEvents {

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            modEventBus.addListener(EmbersClientEvents::afterModelBake);
            MinecraftForge.EVENT_BUS.addListener(EmbersClientEvents::onClientTick);
            MinecraftForge.EVENT_BUS.addListener(EmbersClientEvents::onMovementInput);
            MinecraftForge.EVENT_BUS.addListener(EmbersClientEvents::onBlockHighlight);
            MinecraftForge.EVENT_BUS.addListener(EmbersClientEvents::onLevelRender);
            MinecraftForge.EVENT_BUS.addListener(EmbersClientEvents::onTooltip);
            event.enqueueWork(() -> MenuScreens.register(RegistryManager.SLATE_MENU.get(), SlateScreen::new));
            ItemBlockRenderTypes.setRenderLayer(RegistryManager.STEAM.FLUID.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(RegistryManager.STEAM.FLUID_FLOW.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(RegistryManager.DWARVEN_OIL.FLUID.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(RegistryManager.DWARVEN_OIL.FLUID_FLOW.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(RegistryManager.DWARVEN_GAS.FLUID.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(RegistryManager.DWARVEN_GAS.FLUID_FLOW.get(), RenderType.translucent());
            event.enqueueWork(() -> ItemProperties.register(RegistryManager.INFLICTOR_GEM.get(), new ResourceLocation("embers", "charged"), (stack, level, entity, seed) -> stack.getOrCreateTag().contains("type") ? 1.0F : 0.0F));
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void overlayRegister(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("embers_ingame_overlay", EmbersClientEvents.INGAME_OVERLAY);
            event.registerAboveAll("shifting_scales_particles", ShiftingScalesAugment::renderIngameOverlay);
            event.registerAbove(VanillaGuiOverlay.PLAYER_HEALTH.id(), "shifting_scales_hearts", ShiftingScalesAugment::renderHeartsOverlay);
            event.registerBelow(VanillaGuiOverlay.JUMP_BAR.id(), "winding_gears_spring_bottom", WindingGearsAugment::renderSpringUnderlay);
            event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "winding_gears_spring_top", WindingGearsAugment::renderSpringOverlay);
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        static void addResourceListener(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(new DecimalFormats());
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        static void addParticleProvider(RegisterParticleProvidersEvent event) {
            event.registerSprite(RegistryManager.GLOW_PARTICLE.get(), new GlowParticle.Provider());
            event.registerSprite(RegistryManager.STAR_PARTICLE.get(), new StarParticle.Provider());
            event.registerSprite(RegistryManager.SPARK_PARTICLE.get(), new SparkParticle.Provider());
            event.registerSprite(RegistryManager.SMOKE_PARTICLE.get(), new SmokeParticle.Provider());
            event.registerSprite(RegistryManager.VAPOR_PARTICLE.get(), new VaporParticle.Provider());
            event.registerSprite(RegistryManager.ALCHEMY_CIRCLE_PARTICLE.get(), new AlchemyCircleParticle.Provider());
            event.registerSprite(RegistryManager.TYRFING_PARTICLE.get(), new TyrfingParticle.Provider());
            event.registerSprite(RegistryManager.XRAY_GLOW_PARTICLE.get(), new XRayGlowParticle.Provider());
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(RegistryManager.EMBER_PACKET.get(), EmberPacketRenderer::new);
            event.registerEntityRenderer(RegistryManager.EMBER_PROJECTILE.get(), EmberProjectileRenderer::new);
            event.registerEntityRenderer(RegistryManager.GLIMMER_PROJECTILE.get(), GlimmerProjectileRenderer::new);
            event.registerEntityRenderer(RegistryManager.ANCIENT_GOLEM.get(), AncientGolemRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.EMBER_BORE_ENTITY.get(), EmberBoreBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.MELTER_TOP_ENTITY.get(), MelterTopBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.FLUID_VESSEL_ENTITY.get(), FluidVesselBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.STAMPER_ENTITY.get(), StamperBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.STAMP_BASE_ENTITY.get(), StampBaseBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.BIN_ENTITY.get(), BinBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.MIXER_CENTRIFUGE_BOTTOM_ENTITY.get(), MixerCentrifugeBottomBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.MIXER_CENTRIFUGE_TOP_ENTITY.get(), MixerCentrifugeTopBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.RESERVOIR_ENTITY.get(), ReservoirBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.CRYSTAL_CELL_ENTITY.get(), CrystalCellBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.GEOLOGIC_SEPARATOR_ENTITY.get(), GeologicSeparatorBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.COPPER_CHARGER_ENTITY.get(), CopperChargerBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.ITEM_TRANSFER_ENTITY.get(), ItemTransferBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.FLUID_TRANSFER_ENTITY.get(), FluidTransferBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.ALCHEMY_PEDESTAL_TOP_ENTITY.get(), AlchemyPedestalTopBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.ALCHEMY_PEDESTAL_ENTITY.get(), AlchemyPedestalBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.ALCHEMY_TABLET_ENTITY.get(), AlchemyTabletBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.MECHANICAL_PUMP_BOTTOM_ENTITY.get(), MechanicalPumpBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.CATALYTIC_PLUG_ENTITY.get(), CatalyticPlugBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.COPPER_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.IRON_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.GOLD_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.LEAD_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.SILVER_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.NICKEL_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.TIN_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.ALUMINUM_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.ZINC_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.PLATINUM_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.URANIUM_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.DAWNSTONE_CRYSTAL_SEED.BLOCKENTITY.get(), CrystalSeedBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.FIELD_CHART_ENTITY.get(), FieldChartBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.CINDER_PLINTH_ENTITY.get(), CinderPlinthBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.DAWNSTONE_ANVIL_ENTITY.get(), DawnstoneAnvilBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.AUTOMATIC_HAMMER_ENTITY.get(), AutomaticHammerBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.INFERNO_FORGE_TOP_ENTITY.get(), InfernoForgeTopBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.MNEMONIC_INSCRIBER_ENTITY.get(), MnemonicInscriberBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.ATMOSPHERIC_BELLOWS_ENTITY.get(), AtmosphericBellowsBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.ENTROPIC_ENUMERATOR_ENTITY.get(), EntropicEnumeratorBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.MITHRIL_BLOCK_ENTITY.get(), MithrilBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(RegistryManager.EXCAVATION_BUCKETS_ENTITY.get(), ExcavationBucketsBlockEntityRenderer::new);
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(AncientGolemRenderer.LAYER_LOCATION, AncientGolemModel::createLayer);
            event.registerLayerDefinition(AshenArmorModel.ASHEN_ARMOR_HEAD, () -> LayerDefinition.create(AshenArmorModel.createHeadMesh(), 64, 64));
            event.registerLayerDefinition(AshenArmorModel.ASHEN_ARMOR_CHEST, () -> LayerDefinition.create(AshenArmorModel.createChestMesh(), 64, 64));
            event.registerLayerDefinition(AshenArmorModel.ASHEN_ARMOR_LEGS, () -> LayerDefinition.create(AshenArmorModel.createLegsMesh(), 64, 64));
            event.registerLayerDefinition(AshenArmorModel.ASHEN_ARMOR_FEET, () -> LayerDefinition.create(AshenArmorModel.createFeetMesh(), 64, 64));
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
            ItemColor emberContainerColor = new EmberStorageItem.ColorHandler();
            if (ModList.get().isLoaded("curios")) {
                CuriosCompat.registerColorHandler(event, emberContainerColor);
            }
            event.register(emberContainerColor, RegistryManager.EMBER_JAR.get(), RegistryManager.EMBER_CARTRIDGE.get());
            event.register(new TyrfingItem.ColorHandler(), RegistryManager.TYRFING.get());
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
            event.register("pipe", PipeModel.Loader.INSTANCE);
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(GlowingTextTooltip.class, GlowingTextTooltip.GlowingTextClientTooltip::new);
            event.register(HeatBarTooltip.class, HeatBarTooltip.HeatBarClientTooltip::new);
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation("embers", "position_tex_color_additive"), DefaultVertexFormat.POSITION_TEX_COLOR), shaderInstance -> EmbersRenderTypes.additiveShader = shaderInstance);
            event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation("embers", "rendertype_entity_solid_mithril"), DefaultVertexFormat.NEW_ENTITY), shaderInstance -> EmbersRenderTypes.mithrilShader = shaderInstance);
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void renderTypeRegistry(RegisterNamedRenderTypesEvent event) {
            event.register("mithril", RenderType.solid(), EmbersRenderTypes.MITHRIL);
        }
    }
}