package net.minecraftforge.client.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

public class RegisterClientCommandsEvent extends Event {

    private final CommandDispatcher<CommandSourceStack> dispatcher;

    private final CommandBuildContext context;

    @Internal
    public RegisterClientCommandsEvent(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        this.dispatcher = dispatcher;
        this.context = context;
    }

    public CommandDispatcher<CommandSourceStack> getDispatcher() {
        return this.dispatcher;
    }

    public CommandBuildContext getBuildContext() {
        return this.context;
    }
}