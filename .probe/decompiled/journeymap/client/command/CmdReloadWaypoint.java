package journeymap.client.command;

import journeymap.client.Constants;
import journeymap.client.waypoint.WaypointStore;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;

public class CmdReloadWaypoint implements JMCommand {

    @Override
    public String getName() {
        return "wpreload";
    }

    @Override
    public int execute(CommandSource sender, String[] args) throws CommandRuntimeException {
        WaypointStore.INSTANCE.reset();
        String feedBack = Constants.getString("jm.common.chat_announcement", Constants.getString("jm.waypoint.command.reloaded"));
        sender.sendSystemMessage(Component.translatable(feedBack));
        return 0;
    }

    @Override
    public String getUsage(CommandSource sender) {
        return null;
    }
}