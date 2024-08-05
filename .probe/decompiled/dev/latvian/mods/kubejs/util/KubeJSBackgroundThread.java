package dev.latvian.mods.kubejs.util;

import dev.latvian.mods.kubejs.script.ScriptType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KubeJSBackgroundThread extends Thread {

    public static boolean running = true;

    public KubeJSBackgroundThread() {
        super("KubeJS Background Thread");
    }

    public void run() {
        ScriptType[] types = ScriptType.values();
        for (ScriptType type : types) {
            type.executor = Executors.newSingleThreadExecutor();
        }
        while (running) {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException var9) {
                var9.printStackTrace();
            }
            for (ScriptType type : types) {
                type.console.flush(false);
            }
        }
        for (ScriptType type : types) {
            type.console.flush(false);
            ((ExecutorService) type.executor).shutdown();
            boolean b;
            try {
                b = ((ExecutorService) type.executor).awaitTermination(3L, TimeUnit.SECONDS);
            } catch (InterruptedException var8) {
                b = false;
            }
            if (!b) {
                ((ExecutorService) type.executor).shutdownNow();
            }
        }
    }
}