package net.mehvahdjukaar.moonlight.core;

import net.mehvahdjukaar.moonlight.api.MoonlightRegistry;
import net.mehvahdjukaar.moonlight.api.events.IDropItemOnDeathEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.integration.CompatWoodTypes;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.AdditionalItemPlacementsAPI;
import net.mehvahdjukaar.moonlight.api.map.MapDataRegistry;
import net.mehvahdjukaar.moonlight.api.misc.DataObjectReference;
import net.mehvahdjukaar.moonlight.api.misc.RegistryAccessJsonReloadListener;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicResourcePack;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesTypeRegistry;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.trades.ItemListingRegistry;
import net.mehvahdjukaar.moonlight.core.fluid.SoftFluidInternal;
import net.mehvahdjukaar.moonlight.core.map.MapDataInternal;
import net.mehvahdjukaar.moonlight.core.misc.VillagerAIInternal;
import net.mehvahdjukaar.moonlight.core.network.ModMessages;
import net.mehvahdjukaar.moonlight.core.set.BlockSetInternal;
import net.mehvahdjukaar.moonlight.core.set.BlocksColorInternal;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class Moonlight {

    public static final String MOD_ID = "moonlight";

    public static final Logger LOGGER = LogManager.getLogger("Moonlight");

    public static final boolean HAS_BEEN_INIT = true;

    public static final ThreadLocal<Boolean> CAN_EARLY_RELOAD_HACK = ThreadLocal.withInitial(() -> true);

    public static ResourceLocation res(String name) {
        return new ResourceLocation("moonlight", name);
    }

    public static void commonInit() {
        BlockSetInternal.registerBlockSetDefinition(WoodTypeRegistry.INSTANCE);
        BlockSetInternal.registerBlockSetDefinition(LeavesTypeRegistry.INSTANCE);
        CompatWoodTypes.init();
        MoonlightRegistry.init();
        ModMessages.init();
        VillagerAIInternal.init();
        MapDataInternal.init();
        SoftFluidInternal.init();
        PlatHelper.addCommonSetup(Moonlight::commonSetup);
        PlatHelper.addServerReloadListener(new ItemListingRegistry(), res("villager_trades"));
        BlockSetAPI.addDynamicRegistration((reg, wood) -> AdditionalItemPlacementsAPI.afterItemReg(), WoodType.class, BuiltInRegistries.BLOCK_ENTITY_TYPE);
        if (PlatHelper.getPhysicalSide().isClient()) {
            MoonlightClient.initClient();
        }
    }

    private static void commonSetup() {
        BlocksColorInternal.setup();
    }

    public static void onPlayerCloned(Player oldPlayer, Player newPlayer, boolean wasDeath) {
        if (wasDeath && !oldPlayer.m_9236_().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            Inventory inv = oldPlayer.getInventory();
            int i = 0;
            for (ItemStack v : inv.items) {
                if (v != ItemStack.EMPTY) {
                    IDropItemOnDeathEvent e = IDropItemOnDeathEvent.create(v, oldPlayer, false);
                    MoonlightEventsHelper.postEvent(e, IDropItemOnDeathEvent.class);
                    if (e.isCanceled()) {
                        newPlayer.getInventory().setItem(i, e.getReturnItemStack());
                    }
                }
                i++;
            }
        }
    }

    public static void afterDataReload(RegistryAccess registryAccess) {
        RegistryAccessJsonReloadListener.runReloads(registryAccess);
        DynamicResourcePack.clearAfterReload(PackType.SERVER_DATA);
        DataObjectReference.onDataReload();
    }

    public static void beforeServerStart() {
        SoftFluidInternal.doPostInitServer();
        SoftFluidStack.invalidateEmptyInstance();
    }

    public static void assertInitPhase() {
        if (!PlatHelper.isInitializing() && PlatHelper.isDev() && PlatHelper.getPlatform().isForge()) {
            throw new AssertionError("Method has to be called during main mod initialization phase. Client and Server initializer are not valid, you must call in the main one");
        }
    }

    public static MapItemSavedData getMapDataFromKnownKeys(ServerLevel level, int mapId) {
        MapItemSavedData d = level.getMapData(MapItem.makeKey(mapId));
        if (d == null) {
            d = level.getMapData("magicmap_" + mapId);
            if (d == null) {
                d = level.getMapData("mazemap_" + mapId);
            }
        }
        return d;
    }

    public static void checkDatapackRegistry() {
        try {
            SoftFluidRegistry.getEmpty();
            MapDataRegistry.getDefaultType();
        } catch (Exception var1) {
            throw new RuntimeException("Not all required entries were found in datapack registry. How did this happen?\nThis MUST be some OTHER mod messing up datapack registries (currently Cyanide is known to cause this).\nNote that this could be caused by Paper or similar servers. Know that those are NOT meant to be used with mods", var1);
        }
    }
}