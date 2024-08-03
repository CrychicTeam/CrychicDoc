package sereneseasons.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.FORGE)
public class SeasonCommands {

    @SubscribeEvent
    public static void onCommandsRegistered(RegisterCommandsEvent event) {
        event.getDispatcher().register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) LiteralArgumentBuilder.literal("season").requires(cs -> cs.hasPermission(2))).then(CommandSetSeason.register())).then(CommandGetSeason.register()));
    }
}