package net.minecraftforge.fml.event.lifecycle;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingStage;

public class FMLClientSetupEvent extends ParallelDispatchEvent {

    public FMLClientSetupEvent(ModContainer container, ModLoadingStage stage) {
        super(container, stage);
    }
}