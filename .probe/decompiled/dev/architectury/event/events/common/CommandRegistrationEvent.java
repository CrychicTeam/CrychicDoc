package dev.architectury.event.events.common;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public interface CommandRegistrationEvent {

    Event<CommandRegistrationEvent> EVENT = EventFactory.createLoop();

    void register(CommandDispatcher<CommandSourceStack> var1, CommandBuildContext var2, Commands.CommandSelection var3);
}