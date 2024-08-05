package net.mehvahdjukaar.moonlight.core.fluid;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.IdentityHashMap;
import java.util.Map;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidColors;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.core.fluid.forge.SoftFluidInternalImpl;
import net.mehvahdjukaar.moonlight.core.network.ClientBoundFinalizeFluidsMessage;
import net.mehvahdjukaar.moonlight.core.network.ModMessages;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class SoftFluidInternal {

    public static final Map<Fluid, Holder<SoftFluid>> FLUID_MAP = new IdentityHashMap();

    public static final Map<Item, Holder<SoftFluid>> ITEM_MAP = new IdentityHashMap();

    private static void populateSlaveMaps() {
        FLUID_MAP.clear();
        ITEM_MAP.clear();
        for (Holder.Reference<SoftFluid> h : SoftFluidRegistry.getHolders()) {
            SoftFluid s = h.value();
            if (s.isEnabled()) {
                s.getEquivalentFluids().forEach(f -> FLUID_MAP.put(f, h));
                s.getContainerList().getPossibleFilled().forEach(i -> {
                    if (i != Items.POTION || s != BuiltInSoftFluids.WATER.get()) {
                        ITEM_MAP.put(i, h);
                    }
                });
            }
        }
    }

    @ExpectPlatform
    @Transformed
    public static void init() {
        SoftFluidInternalImpl.init();
    }

    public static void postInitClient() {
        populateSlaveMaps();
        SoftFluidColors.refreshParticleColors();
    }

    public static void onDataSyncToPlayer(ServerPlayer player, boolean isJoined) {
        if (isJoined) {
            ModMessages.CHANNEL.sendToClientPlayer(player, new ClientBoundFinalizeFluidsMessage());
        }
    }

    public static void doPostInitServer() {
        populateSlaveMaps();
        registerExistingVanillaFluids(FLUID_MAP, ITEM_MAP);
    }

    @ExpectPlatform
    @Transformed
    private static void registerExistingVanillaFluids(Map<Fluid, Holder<SoftFluid>> fluidMap, Map<Item, Holder<SoftFluid>> itemMap) {
        SoftFluidInternalImpl.registerExistingVanillaFluids(fluidMap, itemMap);
    }
}