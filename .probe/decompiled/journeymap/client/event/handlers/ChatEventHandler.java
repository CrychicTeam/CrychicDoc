package journeymap.client.event.handlers;

import com.google.common.base.Strings;
import journeymap.client.command.ClientCommandInvoker;
import journeymap.client.command.CmdChatPosition;
import journeymap.client.command.CmdEditWaypoint;
import journeymap.client.command.CmdReloadWaypoint;
import journeymap.client.waypoint.WaypointParser;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;

public class ChatEventHandler {

    private final ClientCommandInvoker clientCommandInvoker = new ClientCommandInvoker();

    public ChatEventHandler() {
        this.clientCommandInvoker.registerSub(new CmdChatPosition());
        this.clientCommandInvoker.registerSub(new CmdEditWaypoint());
        this.clientCommandInvoker.registerSub(new CmdReloadWaypoint());
    }

    public Component onClientChatEventReceived(Component message) {
        if (message != null) {
            try {
                String text = message.getString();
                if (!Strings.isNullOrEmpty(text)) {
                    return WaypointParser.parseChatForWaypoints(message, text);
                }
            } catch (Exception var3) {
                Journeymap.getLogger().warn("Unexpected exception on ClientChatReceivedEvent: " + LogFormatter.toString(var3));
            }
        }
        return null;
    }

    public boolean onChatEvent(String message) {
        if (message.regionMatches(0, "/jm", 0, 3)) {
            if (message.length() > 3) {
                CommandSource player = Minecraft.getInstance().player;
                message = message.substring(4);
                this.clientCommandInvoker.execute(player, message.split(" "));
            } else {
                String commands = this.clientCommandInvoker.getPossibleCommands();
                String text = "Available sub commands are: " + commands;
                Minecraft.getInstance().player.sendSystemMessage(Component.literal(text));
            }
            return true;
        } else {
            return false;
        }
    }
}