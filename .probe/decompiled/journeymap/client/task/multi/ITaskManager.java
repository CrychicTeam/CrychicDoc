package journeymap.client.task.multi;

import net.minecraft.client.Minecraft;

public interface ITaskManager {

    Class<? extends ITask> getTaskClass();

    boolean enableTask(Minecraft var1, Object var2);

    boolean isEnabled(Minecraft var1);

    ITask getTask(Minecraft var1);

    void taskAccepted(ITask var1, boolean var2);

    void disableTask(Minecraft var1);
}