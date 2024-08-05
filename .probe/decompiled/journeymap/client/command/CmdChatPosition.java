package journeymap.client.command;

import com.google.common.base.Joiner;
import journeymap.client.JourneymapClient;
import journeymap.client.task.main.IMainThreadTask;
import journeymap.client.ui.waypoint.WaypointChat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.phys.Vec3;

public class CmdChatPosition implements JMCommand {

    @Override
    public String getName() {
        return "~";
    }

    @Override
    public int execute(CommandSource sender, String[] args) throws CommandRuntimeException {
        String text;
        if (args.length > 1) {
            text = Joiner.on("").skipNulls().join(args);
        } else {
            Vec3 pos = Minecraft.getInstance().player.m_20182_();
            text = String.format("[x:%s, y:%s, z:%s]", pos.x(), pos.y(), pos.z());
        }
        final String pos = text;
        JourneymapClient.getInstance().queueMainThreadTask(new IMainThreadTask() {

            @Override
            public IMainThreadTask perform(Minecraft mc, JourneymapClient jm) {
                Minecraft.getInstance().setScreen(new WaypointChat(pos));
                return null;
            }

            @Override
            public String getName() {
                return "Edit Waypoint";
            }
        });
        return 0;
    }

    @Override
    public String getUsage(CommandSource sender) {
        return ChatFormatting.AQUA + "~" + ChatFormatting.RESET + " : Copy your location into Text";
    }
}