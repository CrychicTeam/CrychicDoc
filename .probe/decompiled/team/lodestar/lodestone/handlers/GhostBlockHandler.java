package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import team.lodestar.lodestone.systems.rendering.ghost.GhostBlockOptions;
import team.lodestar.lodestone.systems.rendering.ghost.GhostBlockRenderer;

public class GhostBlockHandler {

    public static final Map<Object, GhostBlockHandler.GhostBlockEntry> GHOSTS = new HashMap();

    public static GhostBlockHandler.GhostBlockEntry addGhost(Object slot, GhostBlockRenderer renderer, GhostBlockOptions options, int timeLeft) {
        if (!GHOSTS.containsKey(slot)) {
            GHOSTS.put(slot, new GhostBlockHandler.GhostBlockEntry(renderer, options, timeLeft));
        }
        GhostBlockHandler.GhostBlockEntry ghostBlockEntry = (GhostBlockHandler.GhostBlockEntry) GHOSTS.get(slot);
        ghostBlockEntry.timeLeft = timeLeft;
        ghostBlockEntry.options = options;
        ghostBlockEntry.ghost = renderer;
        return ghostBlockEntry;
    }

    public static void renderGhosts(PoseStack poseStack) {
        GHOSTS.forEach((slot, ghostBlockEntry) -> {
            GhostBlockRenderer ghost = ghostBlockEntry.ghost;
            ghost.render(poseStack, ghostBlockEntry.options);
        });
    }

    public static void tickGhosts() {
        Iterator<Entry<Object, GhostBlockHandler.GhostBlockEntry>> iterator = GHOSTS.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Object, GhostBlockHandler.GhostBlockEntry> next = (Entry<Object, GhostBlockHandler.GhostBlockEntry>) iterator.next();
            GhostBlockHandler.GhostBlockEntry entry = (GhostBlockHandler.GhostBlockEntry) next.getValue();
            if (entry.timeLeft <= 0) {
                iterator.remove();
            } else {
                entry.timeLeft--;
            }
        }
    }

    static class GhostBlockEntry {

        private GhostBlockRenderer ghost;

        private GhostBlockOptions options;

        private int timeLeft;

        public GhostBlockEntry(GhostBlockRenderer ghost, GhostBlockOptions options, int timeLeft) {
            this.ghost = ghost;
            this.options = options;
            this.timeLeft = timeLeft;
        }
    }
}