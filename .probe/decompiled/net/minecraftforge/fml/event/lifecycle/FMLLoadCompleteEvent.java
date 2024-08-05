package net.minecraftforge.fml.event.lifecycle;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingStage;

public class FMLLoadCompleteEvent extends ParallelDispatchEvent {

    public FMLLoadCompleteEvent(ModContainer container, ModLoadingStage stage) {
        super(container, stage);
    }
}