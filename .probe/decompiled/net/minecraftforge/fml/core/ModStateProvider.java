package net.minecraftforge.fml.core;

import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IModLoadingState;
import net.minecraftforge.fml.IModStateProvider;
import net.minecraftforge.fml.ModLoadingPhase;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingState;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.loading.FMLPaths;

public class ModStateProvider implements IModStateProvider {

    final ModLoadingState ERROR = ModLoadingState.empty("ERROR", "", ModLoadingPhase.ERROR);

    private final ModLoadingState VALIDATE = ModLoadingState.empty("VALIDATE", "", ModLoadingPhase.GATHER);

    final ModLoadingState CONSTRUCT = ModLoadingState.withTransition("CONSTRUCT", "VALIDATE", ml -> "Constructing %d mods".formatted(ml.size()), ModLoadingPhase.GATHER, new ParallelTransition(ModLoadingStage.CONSTRUCT, FMLConstructModEvent.class));

    private final ModLoadingState CONFIG_LOAD = ModLoadingState.withInline("CONFIG_LOAD", "", ModLoadingPhase.LOAD, ml -> {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ConfigTracker.INSTANCE.loadConfigs(Type.CLIENT, FMLPaths.CONFIGDIR.get()));
        ConfigTracker.INSTANCE.loadConfigs(Type.COMMON, FMLPaths.CONFIGDIR.get());
    });

    private final ModLoadingState COMMON_SETUP = ModLoadingState.withTransition("COMMON_SETUP", "CONFIG_LOAD", ModLoadingPhase.LOAD, new ParallelTransition(ModLoadingStage.COMMON_SETUP, FMLCommonSetupEvent.class));

    private final ModLoadingState SIDED_SETUP = ModLoadingState.withTransition("SIDED_SETUP", "COMMON_SETUP", ModLoadingPhase.LOAD, new ParallelTransition(ModLoadingStage.SIDED_SETUP, (Class<? extends ParallelDispatchEvent>) DistExecutor.unsafeRunForDist(() -> () -> FMLClientSetupEvent.class, () -> () -> FMLDedicatedServerSetupEvent.class)));

    private final ModLoadingState ENQUEUE_IMC = ModLoadingState.withTransition("ENQUEUE_IMC", "", ModLoadingPhase.COMPLETE, new ParallelTransition(ModLoadingStage.ENQUEUE_IMC, InterModEnqueueEvent.class));

    private final ModLoadingState PROCESS_IMC = ModLoadingState.withTransition("PROCESS_IMC", "ENQUEUE_IMC", ModLoadingPhase.COMPLETE, new ParallelTransition(ModLoadingStage.PROCESS_IMC, InterModProcessEvent.class));

    private final ModLoadingState COMPLETE = ModLoadingState.withTransition("COMPLETE", "PROCESS_IMC", ml -> "completing load of %d mods".formatted(ml.size()), ModLoadingPhase.COMPLETE, new ParallelTransition(ModLoadingStage.COMPLETE, FMLLoadCompleteEvent.class));

    private final ModLoadingState DONE = ModLoadingState.empty("DONE", "", ModLoadingPhase.DONE);

    public List<IModLoadingState> getAllStates() {
        return List.of(this.ERROR, this.VALIDATE, this.CONSTRUCT, this.CONFIG_LOAD, this.COMMON_SETUP, this.SIDED_SETUP, this.ENQUEUE_IMC, this.PROCESS_IMC, this.COMPLETE, this.DONE);
    }
}