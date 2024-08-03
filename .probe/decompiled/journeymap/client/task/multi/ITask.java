package journeymap.client.task.multi;

import java.io.File;
import journeymap.client.JourneymapClient;
import net.minecraft.client.Minecraft;

public interface ITask {

    int getMaxRuntime();

    void performTask(Minecraft var1, JourneymapClient var2, File var3, boolean var4) throws InterruptedException;
}