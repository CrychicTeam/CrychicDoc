package snownee.kiwi;

import com.electronwill.nightconfig.core.utils.StringUtils;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.util.valueproviders.FloatProviderType;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.toposort.TopologicalSort;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import snownee.kiwi.block.def.BlockDefinition;
import snownee.kiwi.block.def.SimpleBlockDefinition;
import snownee.kiwi.client.model.RetextureModel;
import snownee.kiwi.command.KiwiCommand;
import snownee.kiwi.config.ConfigHandler;
import snownee.kiwi.config.KiwiConfig;
import snownee.kiwi.config.KiwiConfigManager;
import snownee.kiwi.customization.CustomizationHooks;
import snownee.kiwi.datagen.KiwiDataGen;
import snownee.kiwi.loader.AnnotatedTypeLoader;
import snownee.kiwi.loader.KiwiConfiguration;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.loader.event.ClientInitEvent;
import snownee.kiwi.loader.event.InitEvent;
import snownee.kiwi.loader.event.PostInitEvent;
import snownee.kiwi.loader.event.ServerInitEvent;
import snownee.kiwi.network.Networking;
import snownee.kiwi.schedule.Scheduler;
import snownee.kiwi.util.Util;

@Mod("kiwi")
public class Kiwi {

    public static final String ID = "kiwi";

    public static final RegistryLookup registryLookup = new RegistryLookup();

    static final Marker MARKER = MarkerFactory.getMarker("INIT");

    public static final Logger LOGGER = LogUtils.getLogger();

    public static Map<ResourceLocation, Boolean> defaultOptions = Maps.newHashMap();

    private static Multimap<String, KiwiAnnotationData> moduleData = ArrayListMultimap.create();

    private static Map<KiwiAnnotationData, String> conditions = Maps.newHashMap();

    private static Kiwi.LoadingStage stage = Kiwi.LoadingStage.UNINITED;

    private static Map<String, CreativeModeTab> GROUPS = Maps.newHashMap();

    private static boolean tagsUpdated;

    public static ResourceLocation id(String path) {
        return new ResourceLocation("kiwi", path);
    }

    public Kiwi() throws Exception {
        if (stage == Kiwi.LoadingStage.UNINITED) {
            stage = Kiwi.LoadingStage.CONSTRUCTING;
            try {
                registerRegistries();
                registerTabs();
            } catch (Exception var15) {
                throw new RuntimeException(var15);
            }
            CustomizationHooks.init();
            Map<String, KiwiAnnotationData> classOptionalMap = Maps.newHashMap();
            String dist = Platform.isPhysicalClient() ? "client" : "server";
            for (String mod : ModList.get().getMods().stream().map(IModInfo::getModId).toList()) {
                if (!"minecraft".equals(mod) && !"forge".equals(mod)) {
                    AnnotatedTypeLoader loader = new AnnotatedTypeLoader(mod);
                    KiwiConfiguration configuration = loader.get();
                    if (configuration != null) {
                        for (KiwiAnnotationData module : configuration.modules) {
                            if (checkDist(module, dist)) {
                                moduleData.put(mod, module);
                            }
                        }
                        for (KiwiAnnotationData optional : configuration.optionals) {
                            if (checkDist(optional, dist)) {
                                classOptionalMap.put(optional.target(), optional);
                            }
                        }
                        for (KiwiAnnotationData condition : configuration.conditions) {
                            if (checkDist(condition, dist)) {
                                conditions.put(condition, mod);
                            }
                        }
                        for (KiwiAnnotationData config : configuration.configs) {
                            if (checkDist(config, dist)) {
                                KiwiConfig.ConfigType type = null;
                                try {
                                    type = KiwiConfig.ConfigType.valueOf((String) config.data().get("type"));
                                } catch (Throwable var14) {
                                }
                                type = type == null ? KiwiConfig.ConfigType.COMMON : type;
                                if (type != KiwiConfig.ConfigType.CLIENT || Platform.isPhysicalClient() || Platform.isDataGen()) {
                                    try {
                                        Class<?> clazz = Class.forName(config.target());
                                        String fileName = (String) config.data().get("value");
                                        boolean master = type == KiwiConfig.ConfigType.COMMON && Strings.isNullOrEmpty(fileName);
                                        if (Strings.isNullOrEmpty(fileName)) {
                                            fileName = String.format("%s-%s", mod, type.extension());
                                        }
                                        new ConfigHandler(mod, fileName, type, clazz, master);
                                    } catch (ClassNotFoundException var16) {
                                        LOGGER.error(MARKER, "Failed to load config class {}", config.target());
                                    }
                                }
                            }
                        }
                        for (KiwiAnnotationData packet : configuration.packets) {
                            if (checkDist(packet, dist)) {
                                Networking.processClass(packet.target(), mod);
                            }
                        }
                    }
                }
            }
            LOGGER.info(MARKER, "Processing " + moduleData.size() + " KiwiModule annotations");
            for (Entry<String, KiwiAnnotationData> entry : moduleData.entries()) {
                KiwiAnnotationData optionalx = (KiwiAnnotationData) classOptionalMap.get(((KiwiAnnotationData) entry.getValue()).target());
                if (optionalx != null) {
                    String modid = (String) entry.getKey();
                    if (Platform.isModLoaded(modid)) {
                        String name = (String) ((KiwiAnnotationData) entry.getValue()).data().get("value");
                        if (Strings.isNullOrEmpty(name)) {
                            name = "core";
                        }
                        Boolean defaultEnabled = (Boolean) optionalx.data().get("defaultEnabled");
                        if (defaultEnabled == null) {
                            defaultEnabled = Boolean.TRUE;
                        }
                        defaultOptions.put(new ResourceLocation(modid, name), defaultEnabled);
                    }
                }
            }
            KiwiConfigManager.init();
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            modEventBus.addListener(this::init);
            modEventBus.addListener(this::clientInit);
            MinecraftForge.EVENT_BUS.addListener(this::serverInit);
            modEventBus.addListener(this::postInit);
            modEventBus.addListener(this::loadComplete);
            if (Platform.isModLoaded("fabric_api")) {
                modEventBus.addListener(this::gatherData);
            }
            modEventBus.register(KiwiModules.class);
            if (Platform.isPhysicalClient()) {
                modEventBus.addListener(this::registerModelLoader);
                KiwiModule.RenderLayer.Layer.CUTOUT.value = RenderType.cutout();
                KiwiModule.RenderLayer.Layer.CUTOUT_MIPPED.value = RenderType.cutoutMipped();
                KiwiModule.RenderLayer.Layer.TRANSLUCENT.value = RenderType.translucent();
            }
            MinecraftForge.EVENT_BUS.addListener(this::onCommandsRegister);
            MinecraftForge.EVENT_BUS.addListener(this::onAttachEntity);
            stage = Kiwi.LoadingStage.CONSTRUCTED;
        }
    }

    private static boolean checkDist(KiwiAnnotationData annotationData, String dist) throws IOException {
        try {
            ClassNode clazz = new ClassNode(458752);
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(annotationData.target().replace('.', '/') + ".class");
            ClassReader classReader = new ClassReader(is);
            classReader.accept(clazz, 0);
            if (clazz.visibleAnnotations != null) {
                String ONLYIN = Type.getDescriptor(OnlyIn.class);
                for (AnnotationNode node : clazz.visibleAnnotations) {
                    if (node.values != null && ONLYIN.equals(node.desc)) {
                        int i = node.values.indexOf("value");
                        if (i != -1 && !node.values.get(i + 1).equals(dist)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (Throwable var9) {
            return false;
        }
    }

    public static void preInit() {
        if (stage == Kiwi.LoadingStage.CONSTRUCTED) {
            Set<ResourceLocation> disabledModules = Sets.newHashSet();
            conditions.forEach((k, v) -> {
                try {
                    Class<?> clazz = Class.forName(k.target());
                    String methodName = (String) k.data().get("method");
                    List<String> values = (List<String>) k.data().get("value");
                    if (values == null) {
                        values = List.of(v);
                    }
                    for (ResourceLocation idx : (List) values.stream().map(s -> checkPrefix(s, v)).collect(Collectors.toList())) {
                        LoadingContext context = new LoadingContext(idx);
                        try {
                            Boolean bl = (Boolean) MethodUtils.invokeExactStaticMethod(clazz, methodName, new Object[] { context });
                            if (!bl) {
                                disabledModules.add(idx);
                            }
                        } catch (Exception var11) {
                            disabledModules.add(idx);
                            throw var11;
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException var12) {
                    LOGGER.error(MARKER, "Failed to access to LoadingCondition: %s".formatted(k), var12);
                }
            });
            Map<ResourceLocation, Kiwi.Info> infos = Maps.newHashMap();
            boolean checkDep = false;
            label264: for (Entry<String, KiwiAnnotationData> entry : moduleData.entries()) {
                KiwiAnnotationData module = (KiwiAnnotationData) entry.getValue();
                String modid = (String) entry.getKey();
                if (Platform.isModLoaded(modid)) {
                    String name = (String) module.data().get("value");
                    if (Strings.isNullOrEmpty(name)) {
                        name = "core";
                    }
                    ResourceLocation rl = new ResourceLocation(modid, name);
                    if (disabledModules.contains(rl)) {
                        if (!KiwiConfigManager.modules.containsKey(rl)) {
                            throw new RuntimeException("Cannot load mandatory module: " + rl);
                        }
                    } else if (!KiwiConfigManager.modules.containsKey(rl) || (Boolean) ((ConfigHandler.Value) KiwiConfigManager.modules.get(rl)).get()) {
                        Kiwi.Info info = new Kiwi.Info(rl, module.target());
                        String dependencies = (String) module.data().get("dependencies");
                        for (String rule : (List) StringUtils.split(Strings.nullToEmpty(dependencies), ';').stream().filter(s -> !Strings.isNullOrEmpty(s)).collect(Collectors.toList())) {
                            if (rule.startsWith("@")) {
                                info.moduleRules.add(Util.RL(rule.substring(1), modid));
                                checkDep = true;
                            } else if (!Platform.isModLoaded(rule)) {
                                continue label264;
                            }
                        }
                        infos.put(rl, info);
                    }
                }
            }
            List<ResourceLocation> moduleLoadingQueue = null;
            if (checkDep) {
                List<Kiwi.Info> errorList = Lists.newLinkedList();
                for (Kiwi.Info i : infos.values()) {
                    for (ResourceLocation id : i.moduleRules) {
                        if (!infos.containsKey(id)) {
                            errorList.add(i);
                            break;
                        }
                    }
                }
                for (Kiwi.Info i : errorList) {
                    IModInfo modInfo = ((ModContainer) ModList.get().getModContainerById(i.id.getNamespace()).get()).getModInfo();
                    String dependencies = org.apache.commons.lang3.StringUtils.join(i.moduleRules, ", ");
                    ModLoader.get().addWarning(new ModLoadingWarning(modInfo, ModLoadingStage.ERROR, "msg.kiwi.no_dependencies", new Object[] { i.id, dependencies }));
                }
                if (!errorList.isEmpty()) {
                    return;
                }
                MutableGraph<ResourceLocation> graph = GraphBuilder.directed().allowsSelfLoops(false).expectedNodeCount(infos.size()).build();
                infos.keySet().forEach(graph::addNode);
                infos.values().forEach($ -> $.moduleRules.forEach(r -> graph.putEdge(r, $.id)));
                moduleLoadingQueue = TopologicalSort.topologicalSort(graph, null);
            } else {
                moduleLoadingQueue = ImmutableList.copyOf(infos.keySet());
            }
            for (ResourceLocation idx : moduleLoadingQueue) {
                Kiwi.Info info = (Kiwi.Info) infos.get(idx);
                ModContext context = ModContext.get(idx.getNamespace());
                context.setActiveContainer();
                try {
                    Class<?> clazz = Class.forName(info.className);
                    AbstractModule instance = (AbstractModule) clazz.getDeclaredConstructor().newInstance();
                    KiwiModules.add(idx, instance, context);
                } catch (Exception var28) {
                    LOGGER.error(MARKER, "Kiwi failed to initialize module class: %s".formatted(info.className), var28);
                    continue;
                }
                ModLoadingContext.get().setActiveContainer(null);
            }
            moduleData.clear();
            moduleData = null;
            defaultOptions.clear();
            defaultOptions = null;
            conditions.clear();
            conditions = null;
            Object2IntMap<ResourceKey<?>> counter = new Object2IntArrayMap();
            for (ModuleInfo info : KiwiModules.get()) {
                counter.clear();
                info.context.setActiveContainer();
                KiwiModule.Subscriber subscriber = (KiwiModule.Subscriber) info.module.getClass().getDeclaredAnnotation(KiwiModule.Subscriber.class);
                if (subscriber != null && (!subscriber.clientOnly() || FMLEnvironment.dist.isClient())) {
                    IEventBus eventBus;
                    if (subscriber.modBus()) {
                        eventBus = FMLJavaModLoadingContext.get().getModEventBus();
                    } else {
                        eventBus = MinecraftForge.EVENT_BUS;
                    }
                    eventBus.register(info.module);
                }
                boolean useOwnGroup = info.groupSetting == null;
                if (useOwnGroup) {
                    KiwiModule.Category group = (KiwiModule.Category) info.module.getClass().getDeclaredAnnotation(KiwiModule.Category.class);
                    if (group != null && group.value().length > 0) {
                        useOwnGroup = false;
                        info.groupSetting = GroupSetting.of(group, null);
                    }
                }
                String modid = info.module.uid.getNamespace();
                String namex = info.module.uid.getPath();
                Item.Properties tmpBuilder = null;
                Field tmpBuilderField = null;
                for (Field field : info.module.getClass().getFields()) {
                    if (field.getAnnotation(KiwiModule.Skip.class) == null) {
                        int mods = field.getModifiers();
                        if (Modifier.isPublic(mods) && Modifier.isStatic(mods)) {
                            KiwiModule.Name nameAnnotation = (KiwiModule.Name) field.getAnnotation(KiwiModule.Name.class);
                            ResourceLocation regName;
                            if (nameAnnotation != null) {
                                regName = checkPrefix(nameAnnotation.value(), modid);
                            } else {
                                regName = checkPrefix(field.getName().toLowerCase(Locale.ENGLISH), modid);
                            }
                            if (field.getType() == info.module.getClass() && "instance".equals(regName.getPath()) && regName.getNamespace().equals(modid)) {
                                try {
                                    field.set(null, info.module);
                                } catch (IllegalAccessException | IllegalArgumentException var27) {
                                    LOGGER.error(MARKER, "Kiwi failed to inject module instance to module class: %s".formatted(info.module.uid), var27);
                                }
                            } else {
                                Object o = null;
                                try {
                                    o = field.get(null);
                                } catch (IllegalAccessException | IllegalArgumentException var26) {
                                    LOGGER.error(MARKER, "Kiwi failed to catch game object: %s".formatted(field), var26);
                                }
                                if (o != null) {
                                    if (o instanceof Item.Properties) {
                                        tmpBuilder = (Item.Properties) o;
                                        tmpBuilderField = field;
                                    } else {
                                        Object registry;
                                        if (o instanceof KiwiGO<?> kiwiGO) {
                                            kiwiGO.setKey(regName);
                                            o = kiwiGO.getOrCreate();
                                            registry = kiwiGO.registry();
                                        } else {
                                            registry = registryLookup.findRegistry(o);
                                        }
                                        if (registry != null) {
                                            if (o instanceof Block) {
                                                if (field.getAnnotation(KiwiModule.NoItem.class) != null) {
                                                    info.noItems.add((Block) o);
                                                }
                                                checkNoGroup(info, field, o);
                                                if (tmpBuilder != null) {
                                                    info.blockItemBuilders.put((Block) o, tmpBuilder);
                                                    try {
                                                        tmpBuilderField.set(info.module, null);
                                                    } catch (Exception var25) {
                                                        LOGGER.error(MARKER, "Kiwi failed to clean used item builder: %s".formatted(tmpBuilderField), var25);
                                                    }
                                                }
                                            } else if (o instanceof Item) {
                                                checkNoGroup(info, field, o);
                                            } else if (useOwnGroup && info.groupSetting == null && o instanceof CreativeModeTab tab) {
                                                info.groupSetting = new GroupSetting(new String[] { regName.toString() }, new String[0]);
                                            }
                                            ResourceKey<?> superType = null;
                                            if (registry instanceof Registry<?> reg) {
                                                superType = reg.key();
                                            } else if (registry instanceof IForgeRegistry<?> reg) {
                                                superType = reg.getRegistryKey();
                                            }
                                            if (superType != null) {
                                                int i = counter.getOrDefault(superType, 0);
                                                counter.put(superType, i + 1);
                                                info.register(o, regName, registry, field);
                                            }
                                        }
                                        tmpBuilder = null;
                                        tmpBuilderField = null;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!"kiwi".equals(modid) || !namex.startsWith("contributors")) {
                    LOGGER.info(MARKER, "Module [{}:{}] initialized", modid, namex);
                    List<String> entries = Lists.newArrayList();
                    ObjectIterator var60 = counter.keySet().iterator();
                    while (var60.hasNext()) {
                        ResourceKey<?> key = (ResourceKey<?>) var60.next();
                        entries.add("%s: %s".formatted(Util.trimRL(key.location()), counter.getInt(key)));
                    }
                    if (!entries.isEmpty()) {
                        LOGGER.info(MARKER, "\t\t" + String.join(", ", entries));
                    }
                }
            }
            KiwiModules.ALL_USED_REGISTRIES.add(BuiltInRegistries.CREATIVE_MODE_TAB);
            KiwiModules.ALL_USED_REGISTRIES.add(ForgeRegistries.ITEMS);
            KiwiModules.fire(ModuleInfo::preInit);
            ModLoadingContext.get().setActiveContainer(null);
            stage = Kiwi.LoadingStage.INITED;
        }
    }

    public static void registerRegistry(Registry<?> registry, Class<?> baseClass) {
        registryLookup.registries.put(baseClass, registry);
    }

    public static void registerRegistry(IForgeRegistry<?> registry, Class<?> baseClass) {
        registryLookup.registries.put(baseClass, registry);
    }

    private static void registerRegistries() throws Exception {
        registerRegistry(BuiltInRegistries.GAME_EVENT, GameEvent.class);
        registerRegistry(ForgeRegistries.SOUND_EVENTS, SoundEvent.class);
        registerRegistry(ForgeRegistries.FLUIDS, Fluid.class);
        registerRegistry(ForgeRegistries.MOB_EFFECTS, MobEffect.class);
        registerRegistry(ForgeRegistries.BLOCKS, Block.class);
        registerRegistry(ForgeRegistries.ENCHANTMENTS, Enchantment.class);
        registerRegistry(ForgeRegistries.ENTITY_TYPES, EntityType.class);
        registerRegistry(ForgeRegistries.ITEMS, Item.class);
        registerRegistry(ForgeRegistries.POTIONS, Potion.class);
        registerRegistry(ForgeRegistries.PARTICLE_TYPES, ParticleType.class);
        registerRegistry(ForgeRegistries.BLOCK_ENTITY_TYPES, BlockEntityType.class);
        registerRegistry(ForgeRegistries.PAINTING_VARIANTS, PaintingVariant.class);
        registerRegistry(ForgeRegistries.CHUNK_STATUS, ChunkStatus.class);
        registerRegistry(BuiltInRegistries.RULE_TEST, RuleTestType.class);
        registerRegistry(BuiltInRegistries.POS_RULE_TEST, PosRuleTestType.class);
        registerRegistry(ForgeRegistries.MENU_TYPES, MenuType.class);
        registerRegistry(ForgeRegistries.RECIPE_TYPES, RecipeType.class);
        registerRegistry(ForgeRegistries.RECIPE_SERIALIZERS, RecipeSerializer.class);
        registerRegistry(ForgeRegistries.ATTRIBUTES, Attribute.class);
        registerRegistry(BuiltInRegistries.POSITION_SOURCE_TYPE, PositionSourceType.class);
        registerRegistry(ForgeRegistries.COMMAND_ARGUMENT_TYPES, ArgumentTypeInfo.class);
        registerRegistry(ForgeRegistries.STAT_TYPES, StatType.class);
        registerRegistry(BuiltInRegistries.VILLAGER_TYPE, VillagerType.class);
        registerRegistry(ForgeRegistries.VILLAGER_PROFESSIONS, VillagerProfession.class);
        registerRegistry(ForgeRegistries.POI_TYPES, PoiType.class);
        registerRegistry(ForgeRegistries.MEMORY_MODULE_TYPES, MemoryModuleType.class);
        registerRegistry(ForgeRegistries.SENSOR_TYPES, SensorType.class);
        registerRegistry(ForgeRegistries.SCHEDULES, Schedule.class);
        registerRegistry(ForgeRegistries.ACTIVITIES, Activity.class);
        registerRegistry(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, LootPoolEntryType.class);
        registerRegistry(BuiltInRegistries.LOOT_FUNCTION_TYPE, LootItemFunctionType.class);
        registerRegistry(BuiltInRegistries.LOOT_CONDITION_TYPE, LootItemConditionType.class);
        registerRegistry(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE, LootNumberProviderType.class);
        registerRegistry(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE, LootNbtProviderType.class);
        registerRegistry(BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE, LootScoreProviderType.class);
        registerRegistry(BuiltInRegistries.FLOAT_PROVIDER_TYPE, FloatProviderType.class);
        registerRegistry(BuiltInRegistries.INT_PROVIDER_TYPE, IntProviderType.class);
        registerRegistry(BuiltInRegistries.HEIGHT_PROVIDER_TYPE, HeightProviderType.class);
        registerRegistry(BuiltInRegistries.BLOCK_PREDICATE_TYPE, BlockPredicateType.class);
        registerRegistry(ForgeRegistries.WORLD_CARVERS, WorldCarver.class);
        registerRegistry(ForgeRegistries.FEATURES, Feature.class);
        registerRegistry(BuiltInRegistries.STRUCTURE_PLACEMENT, StructurePlacementType.class);
        registerRegistry(BuiltInRegistries.STRUCTURE_PIECE, StructurePieceType.class);
        registerRegistry(BuiltInRegistries.STRUCTURE_TYPE, StructureType.class);
        registerRegistry(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, PlacementModifierType.class);
        registerRegistry(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, BlockStateProviderType.class);
        registerRegistry(ForgeRegistries.FOLIAGE_PLACER_TYPES, FoliagePlacerType.class);
        registerRegistry(BuiltInRegistries.TRUNK_PLACER_TYPE, TrunkPlacerType.class);
        registerRegistry(BuiltInRegistries.ROOT_PLACER_TYPE, RootPlacerType.class);
        registerRegistry(ForgeRegistries.TREE_DECORATOR_TYPES, TreeDecoratorType.class);
        registerRegistry(BuiltInRegistries.FEATURE_SIZE_TYPE, FeatureSizeType.class);
        registerRegistry(BuiltInRegistries.BIOME_SOURCE, Codec.class);
        registerRegistry(BuiltInRegistries.CHUNK_GENERATOR, Codec.class);
        registerRegistry(BuiltInRegistries.MATERIAL_CONDITION, Codec.class);
        registerRegistry(BuiltInRegistries.MATERIAL_RULE, Codec.class);
        registerRegistry(BuiltInRegistries.DENSITY_FUNCTION_TYPE, Codec.class);
        registerRegistry(BuiltInRegistries.STRUCTURE_PROCESSOR, StructureProcessorType.class);
        registerRegistry(BuiltInRegistries.STRUCTURE_POOL_ELEMENT, StructurePoolElementType.class);
        registerRegistry(BuiltInRegistries.CAT_VARIANT, CatVariant.class);
        registerRegistry(BuiltInRegistries.FROG_VARIANT, FrogVariant.class);
        registerRegistry(BuiltInRegistries.BANNER_PATTERN, BannerPattern.class);
        registerRegistry(BuiltInRegistries.INSTRUMENT, Instrument.class);
        registerRegistry(BuiltInRegistries.CREATIVE_MODE_TAB, CreativeModeTab.class);
    }

    public static void registerTab(String id, ResourceKey<CreativeModeTab> tab) {
        Validate.isTrue(!GROUPS.containsKey(id), "Already exists: %s", new Object[] { id });
        GROUPS.put(id, BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(tab));
    }

    private static void registerTabs() {
        registerTab("building", CreativeModeTabs.BUILDING_BLOCKS);
        registerTab("colored", CreativeModeTabs.COLORED_BLOCKS);
        registerTab("combat", CreativeModeTabs.COMBAT);
        registerTab("food", CreativeModeTabs.FOOD_AND_DRINKS);
        registerTab("functional", CreativeModeTabs.FUNCTIONAL_BLOCKS);
        registerTab("ingredients", CreativeModeTabs.INGREDIENTS);
        registerTab("natural", CreativeModeTabs.NATURAL_BLOCKS);
        registerTab("op", CreativeModeTabs.OP_BLOCKS);
        registerTab("redstone", CreativeModeTabs.REDSTONE_BLOCKS);
        registerTab("spawnEggs", CreativeModeTabs.SPAWN_EGGS);
        registerTab("tools", CreativeModeTabs.TOOLS_AND_UTILITIES);
    }

    static CreativeModeTab getGroup(String path) {
        return (CreativeModeTab) GROUPS.computeIfAbsent(path, $ -> BuiltInRegistries.CREATIVE_MODE_TAB.get(ResourceLocation.tryParse(path)));
    }

    private static void checkNoGroup(ModuleInfo info, Field field, Object o) {
        if (field.getAnnotation(KiwiModule.NoCategory.class) != null) {
            info.noCategories.add(o);
        }
    }

    public static boolean isLoaded(ResourceLocation module) {
        return KiwiModules.isLoaded(module);
    }

    private static ResourceLocation checkPrefix(String name, String defaultModid) {
        return name.contains(":") ? new ResourceLocation(name) : new ResourceLocation(defaultModid, name);
    }

    public static void onTagsUpdated() {
        tagsUpdated = true;
    }

    public static boolean areTagsUpdated() {
        return tagsUpdated;
    }

    private void gatherData(GatherDataEvent event) {
        FabricDataGenerator dataGenerator = FabricDataGenerator.create("kiwi", event);
        new KiwiDataGen().onInitializeDataGenerator(dataGenerator);
    }

    private void init(FMLCommonSetupEvent event) {
        KiwiConfigManager.refresh();
        InitEvent e = new InitEvent(event);
        KiwiModules.fire(m -> m.init(e));
        ModLoadingContext.get().setActiveContainer(null);
        BlockDefinition.registerFactory(SimpleBlockDefinition.Factory.INSTANCE);
    }

    private void clientInit(FMLClientSetupEvent event) {
        ClientInitEvent e = new ClientInitEvent(event);
        KiwiModules.fire(m -> m.clientInit(e));
        ModLoadingContext.get().setActiveContainer(null);
    }

    private void serverInit(ServerStartingEvent event) {
        ServerInitEvent e = new ServerInitEvent();
        KiwiModules.fire(m -> m.serverInit(e));
        event.getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(Scheduler::load, () -> Scheduler.INSTANCE, "kiwi-schedule");
        ModLoadingContext.get().setActiveContainer(null);
    }

    private void onCommandsRegister(RegisterCommandsEvent event) {
        KiwiCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    private void postInit(InterModProcessEvent event) {
        PostInitEvent e = new PostInitEvent(event);
        KiwiModules.fire(m -> m.postInit(e));
        ModLoadingContext.get().setActiveContainer(null);
        KiwiModules.clear();
    }

    private void loadComplete(FMLLoadCompleteEvent event) {
        registryLookup.cache.invalidateAll();
    }

    @OnlyIn(Dist.CLIENT)
    private void registerModelLoader(ModelEvent.RegisterGeometryLoaders event) {
        event.register("retexture", RetextureModel.Loader.INSTANCE);
    }

    private void onAttachEntity(AttackEntityEvent event) {
        Util.onAttackEntity(event.getEntity(), event.getEntity().m_9236_(), InteractionHand.MAIN_HAND, event.getTarget(), null);
    }

    private static final class Info {

        final ResourceLocation id;

        final String className;

        final List<ResourceLocation> moduleRules = Lists.newLinkedList();

        public Info(ResourceLocation id, String className) {
            this.id = id;
            this.className = className;
        }
    }

    private static enum LoadingStage {

        UNINITED, CONSTRUCTING, CONSTRUCTED, INITED
    }
}