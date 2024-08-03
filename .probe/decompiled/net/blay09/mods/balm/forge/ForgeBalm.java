package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.AbstractBalmConfig;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.config.ExampleConfig;
import net.blay09.mods.balm.forge.client.ForgeBalmClient;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.minecraft.world.Container;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;

@Mod("balm")
public class ForgeBalm {

    public ForgeBalm() {
        ((AbstractBalmConfig) Balm.getConfig()).initialize();
        ExampleConfig.initialize();
        ForgeBalmWorldGen.initializeBalmBiomeModifiers();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeBalmClient::onInitializeClient);
        ForgeBalmProviders providers = (ForgeBalmProviders) Balm.getProviders();
        providers.register(IItemHandler.class, new CapabilityToken<IItemHandler>() {
        });
        providers.register(IFluidHandler.class, new CapabilityToken<IFluidHandler>() {
        });
        providers.register(IFluidHandlerItem.class, new CapabilityToken<IFluidHandlerItem>() {
        });
        providers.register(IEnergyStorage.class, new CapabilityToken<IEnergyStorage>() {
        });
        providers.register(Container.class, new CapabilityToken<Container>() {
        });
        providers.register(FluidTank.class, new CapabilityToken<FluidTank>() {
        });
        providers.register(EnergyStorage.class, new CapabilityToken<EnergyStorage>() {
        });
    }
}