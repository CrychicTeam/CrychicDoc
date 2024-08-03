package lio.playeranimatorapi.forge;

import lio.playeranimatorapi.commands.PlayPlayerAnimationCommand;
import lio.playeranimatorapi.commands.StopPlayerAnimationCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class ModEvents {

    @EventBusSubscriber(modid = "liosplayeranimatorapi", bus = Bus.FORGE)
    public class ModEventListener {

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            PlayPlayerAnimationCommand.register(event.getDispatcher());
            StopPlayerAnimationCommand.register(event.getDispatcher());
        }
    }
}