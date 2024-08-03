package journeymap.client.task.main;

import journeymap.client.JourneymapClient;
import net.minecraft.client.Minecraft;

public interface IMainThreadTask {

    IMainThreadTask perform(Minecraft var1, JourneymapClient var2);

    String getName();
}