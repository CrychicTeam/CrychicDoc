package net.blay09.mods.balm.forge;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.proxy.ProxyResolutionException;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.CommonBalmLootTables;
import net.blay09.mods.balm.forge.block.ForgeBalmBlocks;
import net.blay09.mods.balm.forge.block.entity.ForgeBalmBlockEntities;
import net.blay09.mods.balm.forge.command.ForgeBalmCommands;
import net.blay09.mods.balm.forge.config.ForgeBalmConfig;
import net.blay09.mods.balm.forge.entity.ForgeBalmEntities;
import net.blay09.mods.balm.forge.event.ForgeBalmCommonEvents;
import net.blay09.mods.balm.forge.event.ForgeBalmEvents;
import net.blay09.mods.balm.forge.item.ForgeBalmItems;
import net.blay09.mods.balm.forge.menu.ForgeBalmMenus;
import net.blay09.mods.balm.forge.network.ForgeBalmNetworking;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.blay09.mods.balm.forge.recipe.ForgeBalmRecipes;
import net.blay09.mods.balm.forge.sound.ForgeBalmSounds;
import net.blay09.mods.balm.forge.stats.ForgeBalmStats;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ForgeBalmRuntime implements BalmRuntime {

    private final BalmWorldGen worldGen = new ForgeBalmWorldGen();

    private final BalmBlocks blocks = new ForgeBalmBlocks();

    private final BalmBlockEntities blockEntities = new ForgeBalmBlockEntities();

    private final ForgeBalmEvents events = new ForgeBalmEvents();

    private final BalmItems items = new ForgeBalmItems();

    private final BalmMenus menus = new ForgeBalmMenus();

    private final BalmNetworking networking = new ForgeBalmNetworking();

    private final BalmConfig config = new ForgeBalmConfig();

    private final BalmHooks hooks = new ForgeBalmHooks();

    private final BalmRegistries registries = new ForgeBalmRegistries();

    private final BalmSounds sounds = new ForgeBalmSounds();

    private final BalmEntities entities = new ForgeBalmEntities();

    private final BalmProviders providers = new ForgeBalmProviders();

    private final BalmCommands commands = new ForgeBalmCommands();

    private final BalmLootTables lootTables = new CommonBalmLootTables();

    private final BalmStats stats = new ForgeBalmStats();

    private final BalmRecipes recipes = new ForgeBalmRecipes();

    private final List<String> addonClasses = new ArrayList();

    public ForgeBalmRuntime() {
        ForgeBalmCommonEvents.registerEvents(this.events);
    }

    @Override
    public BalmConfig getConfig() {
        return this.config;
    }

    @Override
    public BalmEvents getEvents() {
        return this.events;
    }

    @Override
    public BalmWorldGen getWorldGen() {
        return this.worldGen;
    }

    @Override
    public BalmBlocks getBlocks() {
        return this.blocks;
    }

    @Override
    public BalmBlockEntities getBlockEntities() {
        return this.blockEntities;
    }

    @Override
    public BalmItems getItems() {
        return this.items;
    }

    @Override
    public BalmMenus getMenus() {
        return this.menus;
    }

    @Override
    public BalmNetworking getNetworking() {
        return this.networking;
    }

    @Override
    public BalmHooks getHooks() {
        return this.hooks;
    }

    @Override
    public BalmRegistries getRegistries() {
        return this.registries;
    }

    @Override
    public BalmSounds getSounds() {
        return this.sounds;
    }

    @Override
    public BalmEntities getEntities() {
        return this.entities;
    }

    @Override
    public BalmProviders getProviders() {
        return this.providers;
    }

    @Override
    public BalmCommands getCommands() {
        return this.commands;
    }

    @Override
    public BalmLootTables getLootTables() {
        return this.lootTables;
    }

    @Override
    public BalmStats getStats() {
        return this.stats;
    }

    @Override
    public BalmRecipes getRecipes() {
        return this.recipes;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return (String) ModList.get().getModContainerById(modId).map(it -> it.getModInfo().getDisplayName()).orElse(modId);
    }

    @Override
    public void initialize(String modId, Runnable initializer) {
        ((ForgeBalmItems) this.items).register();
        ((ForgeBalmEntities) this.entities).register();
        ((ForgeBalmWorldGen) this.worldGen).register();
        ((ForgeBalmStats) this.stats).register();
        initializer.run();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(event -> this.initializeAddons());
        DeferredRegisters.register(modId, modEventBus);
    }

    @Override
    public void initializeIfLoaded(String modId, String className) {
        if (this.isModLoaded(modId)) {
            this.addonClasses.add(className);
        }
    }

    @Override
    public <T> SidedProxy<T> sidedProxy(String commonName, String clientName) {
        SidedProxy<T> proxy = new SidedProxy<>(commonName, clientName);
        try {
            if (FMLEnvironment.dist.isClient()) {
                proxy.resolveClient();
            } else {
                proxy.resolveCommon();
            }
            return proxy;
        } catch (ProxyResolutionException var5) {
            throw new RuntimeException(var5);
        }
    }

    private void initializeAddons() {
        for (String addonClass : this.addonClasses) {
            try {
                Class.forName(addonClass).getConstructor().newInstance();
            } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException var4) {
                var4.printStackTrace();
            }
        }
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        MinecraftForge.EVENT_BUS.addListener(event -> event.addListener(reloadListener));
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        MinecraftForge.EVENT_BUS.addListener(event -> event.addListener(reloadListener::accept));
    }
}