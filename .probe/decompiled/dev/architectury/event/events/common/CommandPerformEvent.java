package dev.architectury.event.events.common;

import com.mojang.brigadier.ParseResults;
import dev.architectury.event.Event;
import dev.architectury.event.EventActor;
import dev.architectury.event.EventFactory;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

public class CommandPerformEvent {

    public static final Event<EventActor<CommandPerformEvent>> EVENT = EventFactory.createEventActorLoop();

    private ParseResults<CommandSourceStack> results;

    @Nullable
    private Throwable throwable;

    public CommandPerformEvent(ParseResults<CommandSourceStack> results, @Nullable Throwable throwable) {
        this.results = results;
        this.throwable = throwable;
    }

    public ParseResults<CommandSourceStack> getResults() {
        return this.results;
    }

    public void setResults(ParseResults<CommandSourceStack> results) {
        this.results = results;
    }

    @Nullable
    public Throwable getThrowable() {
        return this.throwable;
    }

    public void setThrowable(@Nullable Throwable throwable) {
        this.throwable = throwable;
    }
}