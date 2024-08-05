package net.blay09.mods.balm.api;

import java.util.function.Consumer;
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
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

public interface BalmRuntime {

    BalmConfig getConfig();

    BalmEvents getEvents();

    BalmWorldGen getWorldGen();

    BalmBlocks getBlocks();

    BalmBlockEntities getBlockEntities();

    BalmItems getItems();

    BalmMenus getMenus();

    BalmNetworking getNetworking();

    BalmHooks getHooks();

    BalmRegistries getRegistries();

    BalmSounds getSounds();

    BalmEntities getEntities();

    BalmProviders getProviders();

    BalmCommands getCommands();

    BalmLootTables getLootTables();

    BalmStats getStats();

    BalmRecipes getRecipes();

    boolean isModLoaded(String var1);

    String getModName(String var1);

    <T> SidedProxy<T> sidedProxy(String var1, String var2);

    void initialize(String var1, Runnable var2);

    void initializeIfLoaded(String var1, String var2);

    void addServerReloadListener(ResourceLocation var1, PreparableReloadListener var2);

    void addServerReloadListener(ResourceLocation var1, Consumer<ResourceManager> var2);
}