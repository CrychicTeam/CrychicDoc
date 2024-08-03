package de.keksuccino.fancymenu.networking.packets.commands.variable.suggestions;

import de.keksuccino.fancymenu.networking.Packet;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class VariableCommandSuggestionsPacket extends Packet {

    public List<String> variable_suggestions;

    @Override
    public boolean processPacket(@Nullable ServerPlayer sender) {
        return sender == null ? false : ServerSideVariableCommandSuggestionsPacketLogic.handle(sender, this);
    }
}