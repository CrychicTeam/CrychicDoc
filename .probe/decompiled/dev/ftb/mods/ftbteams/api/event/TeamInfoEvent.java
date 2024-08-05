package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class TeamInfoEvent extends TeamEvent {

    private final CommandSourceStack source;

    public TeamInfoEvent(Team t, CommandSourceStack p) {
        super(t);
        this.source = p;
    }

    public CommandSourceStack getSource() {
        return this.source;
    }

    public void add(Component component) {
        this.source.sendSuccess(() -> component, false);
    }
}