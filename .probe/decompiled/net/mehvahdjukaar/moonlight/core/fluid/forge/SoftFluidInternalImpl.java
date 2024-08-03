package net.mehvahdjukaar.moonlight.core.fluid.forge;

import java.util.Map;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class SoftFluidInternalImpl {

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.register(SoftFluidInternalImpl.class);
    }

    public static void registerExistingVanillaFluids(Map<Fluid, Holder<SoftFluid>> fluidMap, Map<Item, Holder<SoftFluid>> itemMap) {
        MappedRegistry<SoftFluid> reg = (MappedRegistry<SoftFluid>) SoftFluidRegistry.hackyGetRegistry();
        reg.unfreeze();
        for (Fluid f : ForgeRegistries.FLUIDS) {
            try {
                if (f != null && (!(f instanceof FlowingFluid flowingFluid) || flowingFluid.getSource() == f) && !(f instanceof ForgeFlowingFluid.Flowing) && f != Fluids.EMPTY && !fluidMap.containsKey(f)) {
                    Utils.getID(f);
                    SoftFluid sf = new SoftFluid.Builder(f).build();
                    Registry.register(reg, Utils.getID(f), sf);
                    Holder.Reference<SoftFluid> holder = (Holder.Reference<SoftFluid>) reg.getHolder(reg.getId(sf)).orElseThrow();
                    fluidMap.put(f, holder);
                    Item bucket = f.getBucket();
                    if (bucket != Items.AIR) {
                        itemMap.put(bucket, holder);
                    }
                }
            } catch (Exception var8) {
            }
        }
        reg.freeze();
    }

    @SubscribeEvent
    public static void registerDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(SoftFluidRegistry.KEY, SoftFluid.CODEC, SoftFluid.CODEC);
    }
}