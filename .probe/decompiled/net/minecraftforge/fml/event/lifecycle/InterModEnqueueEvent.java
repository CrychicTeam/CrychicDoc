package net.minecraftforge.fml.event.lifecycle;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingStage;

public class InterModEnqueueEvent extends ParallelDispatchEvent {

    public InterModEnqueueEvent(ModContainer container, ModLoadingStage stage) {
        super(container, stage);
    }
}