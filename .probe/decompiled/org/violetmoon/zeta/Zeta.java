package org.violetmoon.zeta;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.advancement.AdvancementModifierRegistry;
import org.violetmoon.zeta.block.ext.BlockExtensionFactory;
import org.violetmoon.zeta.capability.ZetaCapabilityManager;
import org.violetmoon.zeta.config.ConfigManager;
import org.violetmoon.zeta.config.IZetaConfigInternals;
import org.violetmoon.zeta.config.SectionDefinition;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.bus.ZetaEventBus;
import org.violetmoon.zeta.item.ext.ItemExtensionFactory;
import org.violetmoon.zeta.module.ModuleFinder;
import org.violetmoon.zeta.module.ZetaCategory;
import org.violetmoon.zeta.module.ZetaModuleManager;
import org.violetmoon.zeta.network.ZetaNetworkHandler;
import org.violetmoon.zeta.registry.BrewingRegistry;
import org.violetmoon.zeta.registry.CraftingExtensionsRegistry;
import org.violetmoon.zeta.registry.DyeablesRegistry;
import org.violetmoon.zeta.registry.PottedPlantRegistry;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.registry.VariantRegistry;
import org.violetmoon.zeta.registry.ZetaRegistry;
import org.violetmoon.zeta.util.NameChanger;
import org.violetmoon.zeta.util.RaytracingUtil;
import org.violetmoon.zeta.util.RegistryUtil;
import org.violetmoon.zeta.util.ZetaSide;
import org.violetmoon.zeta.util.handler.FuelHandler;
import org.violetmoon.zeta.util.handler.RequiredModTooltipHandler;
import org.violetmoon.zeta.util.zetalist.IZeta;
import org.violetmoon.zeta.util.zetalist.ZetaList;
import org.violetmoon.zeta.world.EntitySpawnHandler;

public abstract class Zeta implements IZeta {

    public static final String ZETA_ID = "zeta";

    public static final Logger GLOBAL_LOG = LogManager.getLogger("zeta");

    public final Logger log;

    public final String modid;

    public final ZetaSide side;

    public final ZetaEventBus<IZetaLoadEvent> loadBus;

    public final ZetaEventBus<IZetaPlayEvent> playBus;

    public final ZetaModuleManager modules;

    public final ZetaRegistry registry;

    public final RegistryUtil registryUtil = new RegistryUtil(this);

    public final RenderLayerRegistry renderLayerRegistry;

    public final DyeablesRegistry dyeables;

    public final CraftingExtensionsRegistry craftingExtensions;

    public final BrewingRegistry brewingRegistry;

    public final AdvancementModifierRegistry advancementModifierRegistry;

    public final PottedPlantRegistry pottedPlantRegistry;

    public final RequiredModTooltipHandler requiredModTooltipHandler = new RequiredModTooltipHandler();

    public final VariantRegistry variantRegistry = new VariantRegistry(this);

    public final ZetaCapabilityManager capabilityManager;

    public final BlockExtensionFactory blockExtensions;

    public final ItemExtensionFactory itemExtensions;

    public final RaytracingUtil raytracingUtil;

    public final NameChanger nameChanger;

    public final FuelHandler fuel;

    public ConfigManager configManager;

    public IZetaConfigInternals configInternals;

    public ZetaNetworkHandler network;

    public EntitySpawnHandler entitySpawn;

    public Zeta(String modid, Logger log, ZetaSide side) {
        this.log = log;
        this.modid = modid;
        this.side = side;
        this.loadBus = new ZetaEventBus<>(this, LoadEvent.class, IZetaLoadEvent.class, log);
        this.playBus = new ZetaEventBus<>(this, PlayEvent.class, IZetaPlayEvent.class, null);
        this.modules = this.createModuleManager();
        this.registry = this.createRegistry();
        this.renderLayerRegistry = this.createRenderLayerRegistry();
        this.dyeables = this.createDyeablesRegistry();
        this.craftingExtensions = this.createCraftingExtensionsRegistry();
        this.brewingRegistry = this.createBrewingRegistry();
        this.advancementModifierRegistry = this.createAdvancementModifierRegistry();
        this.pottedPlantRegistry = this.createPottedPlantRegistry();
        this.blockExtensions = this.createBlockExtensionFactory();
        this.itemExtensions = this.createItemExtensionFactory();
        this.capabilityManager = this.createCapabilityManager();
        this.raytracingUtil = this.createRaytracingUtil();
        this.nameChanger = this.createNameChanger();
        this.fuel = this.createFuelHandler();
        this.entitySpawn = this.createEntitySpawnHandler();
        this.loadBus.subscribe(this.craftingExtensions).subscribe(this.dyeables).subscribe(this.brewingRegistry).subscribe(this.advancementModifierRegistry).subscribe(this.fuel).subscribe(this.entitySpawn);
        this.playBus.subscribe(this.fuel);
        ZetaList.INSTANCE.register(this);
    }

    public void loadModules(@Nullable Iterable<ZetaCategory> categories, @Nullable ModuleFinder finder, Object rootPojo) {
        if (categories != null && finder != null) {
            this.modules.initCategories(categories);
            this.modules.load(finder);
        }
        this.configManager = new ConfigManager(this, rootPojo);
        this.configInternals = this.makeConfigInternals(this.configManager.getRootConfig());
        this.configManager.onReload();
    }

    public abstract boolean isModLoaded(String var1);

    @Nullable
    public abstract String getModDisplayName(String var1);

    public <T> T modIntegration(String compatWith, Supplier<Supplier<T>> yes, Supplier<Supplier<T>> no) {
        try {
            return (T) ((Supplier) (this.isModLoaded(compatWith) ? yes : no).get()).get();
        } catch (Exception var5) {
            throw new RuntimeException("Zeta: " + this.modid + " threw exception initializing compat with " + compatWith, var5);
        }
    }

    public abstract IZetaConfigInternals makeConfigInternals(SectionDefinition var1);

    public ZetaModuleManager createModuleManager() {
        return new ZetaModuleManager(this);
    }

    public abstract ZetaRegistry createRegistry();

    public RenderLayerRegistry createRenderLayerRegistry() {
        return new RenderLayerRegistry();
    }

    public abstract CraftingExtensionsRegistry createCraftingExtensionsRegistry();

    public DyeablesRegistry createDyeablesRegistry() {
        return new DyeablesRegistry();
    }

    public abstract BrewingRegistry createBrewingRegistry();

    public AdvancementModifierRegistry createAdvancementModifierRegistry() {
        return new AdvancementModifierRegistry(this);
    }

    public abstract PottedPlantRegistry createPottedPlantRegistry();

    public abstract ZetaCapabilityManager createCapabilityManager();

    public BlockExtensionFactory createBlockExtensionFactory() {
        return BlockExtensionFactory.DEFAULT;
    }

    public abstract ItemExtensionFactory createItemExtensionFactory();

    public abstract RaytracingUtil createRaytracingUtil();

    public NameChanger createNameChanger() {
        return new NameChanger();
    }

    public FuelHandler createFuelHandler() {
        return new FuelHandler(this);
    }

    public EntitySpawnHandler createEntitySpawnHandler() {
        return new EntitySpawnHandler(this);
    }

    public abstract ZetaNetworkHandler createNetworkHandler(int var1);

    public abstract <E, T extends E> T fireExternalEvent(T var1);

    public abstract boolean fireRightClickBlock(Player var1, InteractionHand var2, BlockPos var3, BlockHitResult var4);

    public abstract void start();

    @Override
    public Zeta asZeta() {
        return this;
    }
}