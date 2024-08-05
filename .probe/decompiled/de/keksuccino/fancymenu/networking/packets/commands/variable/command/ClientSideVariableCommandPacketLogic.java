package de.keksuccino.fancymenu.networking.packets.commands.variable.command;

import de.keksuccino.fancymenu.customization.variables.Variable;
import de.keksuccino.fancymenu.customization.variables.VariableHandler;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ClientSideVariableCommandPacketLogic {

    private static final Logger LOGGER = LogManager.getLogger();

    protected static boolean handle(@NotNull VariableCommandPacket packet) {
        return packet.set ? setVariable(packet) : getVariable(packet);
    }

    protected static boolean getVariable(@NotNull VariableCommandPacket packet) {
        try {
            String s = VariableHandler.variableExists((String) Objects.requireNonNull(packet.variable_name)) ? ((Variable) Objects.requireNonNull(VariableHandler.getVariable(packet.variable_name))).getValue() : null;
            if (s != null) {
                packet.sendChatFeedback(Component.translatable("fancymenu.commands.variable.get.success", s), false);
                return true;
            }
            packet.sendChatFeedback(Component.translatable("fancymenu.commands.variable.not_found"), true);
        } catch (Exception var2) {
            packet.sendChatFeedback(Component.translatable("fancymenu.commands.variable.get.error"), true);
            LOGGER.error("[FANCYMENU] Failed to get variable via /fmvariable command!", var2);
        }
        return false;
    }

    protected static boolean setVariable(@NotNull VariableCommandPacket packet) {
        try {
            VariableHandler.setVariable((String) Objects.requireNonNull(packet.variable_name), (String) Objects.requireNonNull(packet.set_to_value));
            if (packet.feedback) {
                packet.sendChatFeedback(Component.translatable("fancymenu.commands.variable.set.success", packet.set_to_value), false);
            }
            return true;
        } catch (Exception var2) {
            packet.sendChatFeedback(Component.translatable("fancymenu.commands.variable.set.error"), true);
            LOGGER.error("[FANCYMENU] Failed to set variable via /fmvariable command!", var2);
            return false;
        }
    }
}