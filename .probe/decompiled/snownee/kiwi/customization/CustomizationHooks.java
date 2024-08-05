package snownee.kiwi.customization;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.SharedConstants;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.loading.ClientModLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.resource.DelegatingPackResources;
import net.minecraftforge.resource.PathPackResources;
import net.minecraftforge.resource.ResourcePackLoader;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.Kiwi;
import snownee.kiwi.customization.block.BlockFundamentals;
import snownee.kiwi.customization.block.GlassType;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.block.behavior.BlockBehaviorRegistry;
import snownee.kiwi.customization.block.behavior.SitManager;
import snownee.kiwi.customization.block.component.KBlockComponent;
import snownee.kiwi.customization.block.family.BlockFamilies;
import snownee.kiwi.customization.block.loader.KBlockTemplate;
import snownee.kiwi.customization.builder.BuilderRules;
import snownee.kiwi.customization.item.ItemFundamentals;
import snownee.kiwi.customization.item.loader.KCreativeTab;
import snownee.kiwi.customization.item.loader.KItemTemplate;
import snownee.kiwi.customization.placement.PlacementSystem;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.util.ClientProxy;
import snownee.kiwi.util.resource.OneTimeLoader;
import snownee.kiwi.util.resource.RequiredFolderRepositorySource;

public final class CustomizationHooks {

    private static final Set<String> blockNamespaces = Sets.newLinkedHashSet();

    private static boolean enabled = true;

    public static boolean kswitch = Platform.isModLoaded("kswitch") || !Platform.isProduction();

    private static final int CACHE_SIZE = 512;

    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>> OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> object2bytelinkedopenhashmap = new Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>(512, 0.25F) {

            protected void rehash(int needed) {
            }
        };
        object2bytelinkedopenhashmap.defaultReturnValue((byte) 127);
        return object2bytelinkedopenhashmap;
    });

    private CustomizationHooks() {
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static boolean skipGlassRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pDirection) {
        if (KBlockSettings.of(pState.m_60734_()) == null && KBlockSettings.of(pAdjacentBlockState.m_60734_()) == null) {
            return false;
        } else {
            GlassType glassType = getGlassType(pState);
            if (glassType == null || !glassType.skipRendering()) {
                return false;
            } else if (!pState.m_60713_(pAdjacentBlockState.m_60734_()) && glassType != getGlassType(pAdjacentBlockState)) {
                return false;
            } else {
                Block.BlockStatePairKey key = new Block.BlockStatePairKey(pState, pAdjacentBlockState, pDirection);
                Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> map = (Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>) OCCLUSION_CACHE.get();
                byte b0 = map.getAndMoveToFirst(key);
                if (b0 != 127) {
                    return b0 == 0;
                } else {
                    VoxelShape shape1 = KBlockSettings.getGlassFaceShape(pState, pDirection);
                    if (shape1.isEmpty()) {
                        return true;
                    } else {
                        VoxelShape shape2 = KBlockSettings.getGlassFaceShape(pAdjacentBlockState, pDirection.getOpposite());
                        boolean flag = Shapes.joinIsNotEmpty(shape1, shape2, BooleanOp.ONLY_FIRST);
                        if (map.size() == 512) {
                            map.removeLastByte();
                        }
                        map.putAndMoveToFirst(key, (byte) (flag ? 1 : 0));
                        return !flag;
                    }
                }
            }
        }
    }

    @Nullable
    public static GlassType getGlassType(BlockState blockState) {
        KBlockSettings settings = KBlockSettings.of(blockState.m_60734_());
        if (settings != null && settings.glassType != null) {
            return settings.glassType;
        } else {
            return isColorlessGlass(blockState) ? GlassType.CLEAR : null;
        }
    }

    public static void init() {
        enabled = CustomizationServiceFinder.shouldEnable(ModList.get().getMods());
        if (isEnabled()) {
            Kiwi.LOGGER.info("Kiwi Customization is enabled");
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
            modEventBus.addListener(EventPriority.LOWEST, event -> {
                if (Registries.BLOCK.equals(event.getRegistryKey())) {
                    initLoader(modEventBus);
                }
            });
            modEventBus.addListener(event -> {
                event.create(new RegistryBuilder().setName(CustomizationRegistries.BLOCK_COMPONENT_KEY.location()).disableOverrides().disableSaving().hasTags(), $ -> {
                    CustomizationRegistries.BLOCK_COMPONENT = (Registry<KBlockComponent.Type<?>>) BuiltInRegistries.REGISTRY.get(CustomizationRegistries.BLOCK_COMPONENT_KEY.location());
                    Kiwi.registerRegistry($, KBlockComponent.Type.class);
                });
                event.create(new RegistryBuilder().setName(CustomizationRegistries.BLOCK_TEMPLATE_KEY.location()).disableOverrides().disableSaving().hasTags(), $ -> {
                    CustomizationRegistries.BLOCK_TEMPLATE = (Registry<KBlockTemplate.Type<?>>) BuiltInRegistries.REGISTRY.get(CustomizationRegistries.BLOCK_TEMPLATE_KEY.location());
                    Kiwi.registerRegistry($, KBlockTemplate.Type.class);
                });
                event.create(new RegistryBuilder().setName(CustomizationRegistries.ITEM_TEMPLATE_KEY.location()).disableOverrides().disableSaving().hasTags(), $ -> {
                    CustomizationRegistries.ITEM_TEMPLATE = (Registry<KItemTemplate.Type<?>>) BuiltInRegistries.REGISTRY.get(CustomizationRegistries.ITEM_TEMPLATE_KEY.location());
                    Kiwi.registerRegistry($, KItemTemplate.Type.class);
                });
            });
            modEventBus.addListener(event -> event.addRepositorySource(new RequiredFolderRepositorySource(CustomizationServiceFinder.PACK_DIRECTORY, event.getPackType(), PackSource.BUILT_IN)));
            forgeEventBus.addListener(event -> {
                if (PlacementSystem.isDebugEnabled(event.getPlayer())) {
                    PlacementSystem.removeDebugBlocks(event.getPlayer().m_9236_(), event.getPos());
                }
            });
            forgeEventBus.addListener(event -> {
                InteractionResult result = BlockBehaviorRegistry.getInstance().onUseBlock(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
                if (result.consumesAction()) {
                    event.setCanceled(true);
                    event.setCancellationResult(result);
                }
            });
            forgeEventBus.addListener(event -> {
                if (event.getHand() == InteractionHand.MAIN_HAND && SitManager.sit(event.getEntity(), event.getHitVec())) {
                    event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
                    event.setCanceled(true);
                }
            });
            forgeEventBus.addListener(event -> BlockFamilies.reloadTags());
            if (Platform.isPhysicalClient()) {
                CustomizationClient.init();
            }
        }
    }

    public static void initLoader(IEventBus modEventBus) {
        ResourceManager resourceManager = collectKiwiPacks();
        OneTimeLoader.Context context = new OneTimeLoader.Context();
        Map<String, CustomizationMetadata> metadataMap = CustomizationMetadata.loadMap(resourceManager, context);
        BlockFundamentals blockFundamentals = BlockFundamentals.reload(resourceManager, context, true);
        blockNamespaces.clear();
        blockFundamentals.blocks().keySet().stream().map(ResourceLocation::m_135827_).forEach(blockNamespaces::add);
        List<ResourceLocation> blockIds = Lists.newArrayList();
        CustomizationMetadata.sortedForEach(metadataMap, "block", blockFundamentals.blocks(), (id, definition) -> {
            try {
                Block block = definition.createBlock(id, blockFundamentals.shapes());
                if (block == null) {
                    return;
                }
                ForgeRegistries.BLOCKS.register(id, block);
                blockFundamentals.slotProviders().attachSlotsA(block, definition);
                blockFundamentals.placeChoices().attachChoicesA(block, definition);
                blockIds.add(id);
            } catch (Exception var5x) {
                Kiwi.LOGGER.error("Failed to create block %s".formatted(id), var5x);
            }
        });
        ItemFundamentals itemFundamentals = ItemFundamentals.reload(resourceManager, context, true);
        for (ResourceLocation blockId : blockIds) {
            if (!itemFundamentals.items().containsKey(blockId)) {
                itemFundamentals.addDefaultBlockItem(blockId);
            }
        }
        KItemTemplate none = (KItemTemplate) itemFundamentals.templates().get(new ResourceLocation("none"));
        Preconditions.checkNotNull(none, "Missing 'none' item definition");
        CustomizationMetadata.sortedForEach(metadataMap, "item", itemFundamentals.items(), (id, definition) -> {
            try {
                if (definition.template().template() == none) {
                    return;
                }
                Item item = definition.createItem(id);
                if (item == null) {
                    return;
                }
                ForgeRegistries.ITEMS.register(id, item);
            } catch (Exception var4x) {
                Kiwi.LOGGER.error("Failed to create item %s".formatted(id), var4x);
            }
        });
        blockFundamentals.slotProviders().attachSlotsB();
        blockFundamentals.placeChoices().attachChoicesB();
        blockFundamentals.slotLinks().finish();
        if (Platform.isPhysicalClient()) {
            CustomizationClient.afterRegister(itemFundamentals.items(), blockFundamentals.blocks(), new ClientProxy.Context(ClientModLoader.isLoading(), modEventBus));
        }
        Map<ResourceLocation, KCreativeTab> tabs = OneTimeLoader.load(resourceManager, "kiwi/creative_tab", KCreativeTab.CODEC, context);
        List<Entry<ResourceLocation, KCreativeTab>> newTabs = tabs.entrySet().stream().sorted(Comparator.comparingInt($ -> ((KCreativeTab) $.getValue()).order())).filter(entryx -> {
            KCreativeTab valuex = (KCreativeTab) entryx.getValue();
            if (valuex.insert().isPresent()) {
                insertToTab(modEventBus, valuex);
                return false;
            } else {
                return true;
            }
        }).toList();
        for (int i = 0; i < newTabs.size(); i++) {
            Entry<ResourceLocation, KCreativeTab> entry = (Entry<ResourceLocation, KCreativeTab>) newTabs.get(i);
            ResourceLocation key = (ResourceLocation) entry.getKey();
            KCreativeTab value = (KCreativeTab) entry.getValue();
            CreativeModeTab.Builder tab = AbstractModule.itemCategory(key.getNamespace(), key.getPath(), () -> ((Item) BuiltInRegistries.ITEM.m_123009_(value.icon()).orElse(Items.BARRIER)).getDefaultInstance()).displayItems((params, output) -> output.acceptAll(value.contents().stream().map(BuiltInRegistries.ITEM::m_6246_).filter(Objects::nonNull).map(Item::m_7968_).toList()));
            if (i > 0) {
                tab.withTabsBefore(new ResourceLocation[] { (ResourceLocation) ((Entry) newTabs.get(i - 1)).getKey() });
            }
            if (i < newTabs.size() - 1) {
                tab.withTabsAfter(new ResourceLocation[] { (ResourceLocation) ((Entry) newTabs.get(i + 1)).getKey() });
            }
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab.build());
        }
        BlockFamilies.reloadResources(resourceManager, context);
        if (!Platform.isDataGen()) {
            BuilderRules.reload(resourceManager, context);
        }
    }

    private static void insertToTab(IEventBus modEventBus, KCreativeTab kCreativeTab) {
        if (Platform.isPhysicalClient()) {
            modEventBus.addListener(event -> {
                if (event.getTabKey() == kCreativeTab.insert().orElseThrow()) {
                    for (ResourceKey<Item> content : kCreativeTab.contents()) {
                        Item item = (Item) BuiltInRegistries.ITEM.m_6246_(content);
                        if (item == null) {
                            return;
                        }
                        event.m_246326_(item);
                    }
                }
            });
        }
    }

    public static ResourceManager collectKiwiPacks() {
        CustomizationServiceFinder.PACK_DIRECTORY.toFile().mkdirs();
        RequiredFolderRepositorySource folderRepositorySource = new RequiredFolderRepositorySource(CustomizationServiceFinder.PACK_DIRECTORY, PackType.CLIENT_RESOURCES, PackSource.BUILT_IN);
        PackRepository packRepository = new PackRepository(folderRepositorySource);
        ResourcePackLoader.loadResourcePacks(packRepository, CustomizationHooks::buildPackFinder);
        packRepository.reload();
        List<String> selected = Lists.newArrayList(packRepository.getAvailableIds());
        selected.remove("mod_resources");
        selected.add(0, "mod_resources");
        packRepository.setSelected(selected);
        return new KiwiPackResourceManager(packRepository.openAllSelected());
    }

    private static RepositorySource buildPackFinder(Map<IModFile, ? extends PathPackResources> modResourcePacks) {
        return packAcceptor -> clientPackFinder(modResourcePacks, packAcceptor);
    }

    private static void clientPackFinder(Map<IModFile, ? extends PathPackResources> modResourcePacks, Consumer<Pack> packAcceptor) {
        ArrayList<PathPackResources> hiddenPacks = new ArrayList();
        for (Entry<IModFile, ? extends PathPackResources> e : modResourcePacks.entrySet()) {
            IModInfo mod = (IModInfo) ((IModFile) e.getKey()).getModInfos().get(0);
            String name = "mod:" + mod.getModId();
            Pack modPack = Pack.readMetaAndCreate(name, Component.literal(((PathPackResources) e.getValue()).m_5542_()), false, id -> (PackResources) e.getValue(), PackType.CLIENT_RESOURCES, Pack.Position.BOTTOM, PackSource.DEFAULT);
            if (modPack == null) {
                ModLoader.get().addWarning(new ModLoadingWarning(mod, ModLoadingStage.ERROR, "fml.modloading.brokenresources", new Object[] { e.getKey() }));
            } else {
                Kiwi.LOGGER.debug("Generating PackInfo named {} for mod file {}", name, ((IModFile) e.getKey()).getFilePath());
                if (mod.getOwningFile().showAsResourcePack()) {
                    packAcceptor.accept(modPack);
                } else {
                    hiddenPacks.add((PathPackResources) e.getValue());
                }
            }
        }
        Pack modResourcesPack = Pack.readMetaAndCreate("mod_resources", Component.literal("Mod Resources"), true, id -> new DelegatingPackResources(id, false, new PackMetadataSection(Component.translatable("fml.resources.modresources", hiddenPacks.size()), SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES)), hiddenPacks), PackType.CLIENT_RESOURCES, Pack.Position.BOTTOM, PackSource.DEFAULT);
        packAcceptor.accept(modResourcesPack);
    }

    public static Set<String> getBlockNamespaces() {
        return blockNamespaces;
    }

    public static boolean isColorlessGlass(BlockState blockState) {
        return blockState.m_204336_(Tags.Blocks.GLASS_COLORLESS);
    }
}