package de.keksuccino.konkrete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PostLoadingHandler {

    private static Map<String, List<Runnable>> events = new HashMap();

    protected static void runPostLoadingEvents() {
        for (Entry<String, List<Runnable>> m : events.entrySet()) {
            System.out.println("[KONKRETE] Running PostLoadingEvents for mod: " + (String) m.getKey());
            for (Runnable r : (List) m.getValue()) {
                r.run();
            }
            System.out.println("[KONKRETE] PostLoadingEvents completed for mod: " + (String) m.getKey());
        }
    }

    protected static void addEvent(String modid, Runnable event) {
        if (!events.containsKey(modid)) {
            events.put(modid, new ArrayList());
        }
        ((List) events.get(modid)).add(event);
    }
}