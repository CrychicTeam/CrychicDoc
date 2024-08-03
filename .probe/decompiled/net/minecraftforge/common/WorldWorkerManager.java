package net.minecraftforge.common;

import java.util.ArrayList;
import java.util.List;

public class WorldWorkerManager {

    private static List<WorldWorkerManager.IWorker> workers = new ArrayList();

    private static long startTime = -1L;

    private static int index = 0;

    public static void tick(boolean start) {
        if (start) {
            startTime = System.currentTimeMillis();
        } else {
            index = 0;
            WorldWorkerManager.IWorker task = getNext();
            if (task != null) {
                long time = 50L - (System.currentTimeMillis() - startTime);
                if (time < 10L) {
                    time = 10L;
                }
                time += System.currentTimeMillis();
                while (System.currentTimeMillis() < time && task != null) {
                    boolean again = task.doWork();
                    if (!task.hasWork()) {
                        remove(task);
                        task = getNext();
                    } else if (!again) {
                        task = getNext();
                    }
                }
            }
        }
    }

    public static synchronized void addWorker(WorldWorkerManager.IWorker worker) {
        workers.add(worker);
    }

    private static synchronized WorldWorkerManager.IWorker getNext() {
        return workers.size() > index ? (WorldWorkerManager.IWorker) workers.get(index++) : null;
    }

    private static synchronized void remove(WorldWorkerManager.IWorker worker) {
        workers.remove(worker);
        index--;
    }

    public static synchronized void clear() {
        workers.clear();
    }

    public interface IWorker {

        boolean hasWork();

        boolean doWork();
    }
}