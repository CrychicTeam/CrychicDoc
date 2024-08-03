package net.minecraft.server;

import net.minecraft.commands.CommandSourceStack;

public class ConsoleInput {

    public final String msg;

    public final CommandSourceStack source;

    public ConsoleInput(String string0, CommandSourceStack commandSourceStack1) {
        this.msg = string0;
        this.source = commandSourceStack1;
    }
}