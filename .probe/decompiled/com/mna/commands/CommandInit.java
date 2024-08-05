package com.mna.commands;

import com.mna.ManaAndArtifice;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandInit {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        CommandMna.register(event.getDispatcher());
        if (ManaAndArtifice.instance.isDebug) {
            CommandStructureDiff.register(event.getDispatcher());
        }
    }
}