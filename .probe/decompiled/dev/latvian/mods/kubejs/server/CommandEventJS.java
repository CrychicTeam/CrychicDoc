package dev.latvian.mods.kubejs.server;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;
import dev.architectury.event.events.common.CommandPerformEvent;
import net.minecraft.commands.CommandSourceStack;

public class CommandEventJS extends ServerEventJS {

    private final CommandPerformEvent event;

    private final String commandName;

    public CommandEventJS(CommandPerformEvent e) {
        super(((CommandSourceStack) e.getResults().getContext().getSource()).getServer());
        this.event = e;
        this.commandName = this.event.getResults().getContext().getNodes().isEmpty() ? "" : ((ParsedCommandNode) this.event.getResults().getContext().getNodes().get(0)).getNode().getName();
    }

    public String getCommandName() {
        return this.commandName;
    }

    public String getInput() {
        return this.event.getResults().getReader().getString();
    }

    public ParseResults<CommandSourceStack> getParseResults() {
        return this.event.getResults();
    }

    public void setParseResults(ParseResults<CommandSourceStack> parse) {
        this.event.setResults(parse);
    }

    public Throwable getException() {
        return this.event.getThrowable();
    }

    public void setException(Throwable exception) {
        this.event.setThrowable(exception);
    }
}