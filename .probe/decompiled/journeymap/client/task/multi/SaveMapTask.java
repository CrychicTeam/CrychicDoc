package journeymap.client.task.multi;

import java.io.File;
import journeymap.client.JourneymapClient;
import journeymap.client.io.MapSaver;
import journeymap.client.model.MapType;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class SaveMapTask implements ITask {

    public static MapType MAP_TYPE;

    private static final Logger logger = Journeymap.getLogger();

    MapSaver mapSaver;

    private SaveMapTask(MapSaver mapSaver) {
        this.mapSaver = mapSaver;
    }

    @Override
    public int getMaxRuntime() {
        return 120000;
    }

    @Override
    public void performTask(Minecraft mc, JourneymapClient jm, File jmWorldDir, boolean threadLogging) {
        this.mapSaver.saveMap();
    }

    public static class Manager implements ITaskManager {

        MapSaver mapSaver;

        @Override
        public Class<? extends ITask> getTaskClass() {
            return SaveMapTask.class;
        }

        @Override
        public boolean enableTask(Minecraft minecraft, Object params) {
            if (params != null && params instanceof MapSaver) {
                this.mapSaver = (MapSaver) params;
            }
            return this.isEnabled(minecraft);
        }

        @Override
        public boolean isEnabled(Minecraft minecraft) {
            return this.mapSaver != null;
        }

        @Override
        public void disableTask(Minecraft minecraft) {
            this.mapSaver = null;
        }

        public SaveMapTask getTask(Minecraft minecraft) {
            return this.mapSaver == null ? null : new SaveMapTask(this.mapSaver);
        }

        @Override
        public void taskAccepted(ITask task, boolean accepted) {
            this.mapSaver = null;
        }
    }
}