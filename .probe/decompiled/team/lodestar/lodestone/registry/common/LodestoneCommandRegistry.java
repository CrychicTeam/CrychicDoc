package team.lodestar.lodestone.registry.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import team.lodestar.lodestone.command.DevWorldSetupCommand;
import team.lodestar.lodestone.command.FreezeActiveWorldEventsCommand;
import team.lodestar.lodestone.command.GetDataWorldEventCommand;
import team.lodestar.lodestone.command.ListActiveWorldEventsCommand;
import team.lodestar.lodestone.command.RemoveActiveWorldEventsCommand;
import team.lodestar.lodestone.command.UnfreezeActiveWorldEventsCommand;

@EventBusSubscriber(bus = Bus.FORGE)
public class LodestoneCommandRegistry {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("lode").then(DevWorldSetupCommand.register())).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("worldevent").then(RemoveActiveWorldEventsCommand.register())).then(ListActiveWorldEventsCommand.register())).then(GetDataWorldEventCommand.register())).then(FreezeActiveWorldEventsCommand.register())).then(UnfreezeActiveWorldEventsCommand.register())));
        dispatcher.register((LiteralArgumentBuilder) Commands.literal("lodestone").redirect(cmd));
    }
}