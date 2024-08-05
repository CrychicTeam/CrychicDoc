package net.minecraftforge.fml.event.lifecycle;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingStage;

public class FMLCommonSetupEvent extends ParallelDispatchEvent {

    public FMLCommonSetupEvent(ModContainer container, ModLoadingStage stage) {
        super(container, stage);
    }
}