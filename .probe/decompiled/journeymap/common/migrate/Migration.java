package journeymap.common.migrate;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.util.ArrayList;
import java.util.List;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;

public class Migration {

    private final String targetPackage;

    public Migration(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public boolean performTasks() {
        boolean success = true;
        List<MigrationTask> tasks = new ArrayList();
        try {
            for (ClassInfo classInfo : ClassPath.from(Journeymap.class.getClassLoader()).getTopLevelClassesRecursive(this.targetPackage)) {
                Class<?> clazz = classInfo.load();
                if (MigrationTask.class.isAssignableFrom(clazz)) {
                    try {
                        MigrationTask task = (MigrationTask) clazz.newInstance();
                        if (task.isActive(Journeymap.JM_VERSION)) {
                            tasks.add(task);
                        }
                    } catch (Throwable var9) {
                        Journeymap.getLogger().error("Couldn't instantiate MigrationTask " + clazz, LogFormatter.toPartialString(var9));
                        success = false;
                    }
                }
            }
        } catch (Throwable var10) {
            Journeymap.getLogger().error("Couldn't find MigrationTasks: " + var10, LogFormatter.toPartialString(var10));
            success = false;
        }
        for (MigrationTask task : tasks) {
            try {
                if (!(Boolean) task.call()) {
                    success = false;
                }
            } catch (Throwable var8) {
                Journeymap.getLogger().fatal(LogFormatter.toString(var8));
                success = false;
            }
        }
        if (!success) {
            Journeymap.getLogger().fatal("Some or all of JourneyMap migration failed! You may experience significant errors.");
        }
        return success;
    }
}