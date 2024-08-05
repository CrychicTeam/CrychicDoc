package dev.ftb.mods.ftbteams.api;

import java.util.UUID;
import net.minecraft.network.chat.Component;

public interface TeamMessage {

    UUID sender();

    long date();

    Component text();
}