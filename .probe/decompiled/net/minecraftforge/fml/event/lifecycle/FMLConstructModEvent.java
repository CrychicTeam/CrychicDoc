package net.minecraftforge.fml.event.lifecycle;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingStage;

public class FMLConstructModEvent extends ParallelDispatchEvent {

    public FMLConstructModEvent(ModContainer container, ModLoadingStage stage) {
        super(container, stage);
    }
}