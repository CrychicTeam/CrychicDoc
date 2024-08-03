package journeymap.client.command;

import com.google.common.base.Joiner;
import com.mojang.blaze3d.platform.InputConstants;
import journeymap.client.JourneymapClient;
import journeymap.client.log.ChatLog;
import journeymap.client.task.main.IMainThreadTask;
import journeymap.client.ui.UIManager;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointParser;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSource;

public class CmdEditWaypoint implements JMCommand {

    @Override
    public String getName() {
        return "wpedit";
    }

    @Override
    public int execute(CommandSource sender, String[] args) throws CommandRuntimeException {
        String text = Joiner.on(" ").skipNulls().join(args);
        final Waypoint waypoint = WaypointParser.parse(text);
        if (waypoint != null) {
            final boolean controlDown = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 341) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 345);
            JourneymapClient.getInstance().queueMainThreadTask(new IMainThreadTask() {

                @Override
                public IMainThreadTask perform(Minecraft mc, JourneymapClient jm) {
                    if (controlDown) {
                        if (waypoint.isInPlayerDimension()) {
                            waypoint.setPersistent(false);
                            UIManager.INSTANCE.openFullscreenMap(waypoint);
                        } else {
                            ChatLog.announceError("Location is not in your dimension");
                        }
                    } else {
                        UIManager.INSTANCE.openWaypointEditor(waypoint, true, null);
                    }
                    return null;
                }

                @Override
                public String getName() {
                    return "Edit Waypoint";
                }
            });
        } else {
            ChatLog.announceError("Not a valid waypoint. Use: 'x:3, z:70', etc. : " + text);
        }
        return 0;
    }

    @Override
    public String getUsage(CommandSource sender) {
        return null;
    }
}