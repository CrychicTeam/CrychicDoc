package com.almostreliable.ponderjs;

import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;

public class PonderStoriesManager {

    private final Map<ResourceLocation, List<PonderStoryBoardEntry>> stories = Collections.synchronizedMap(new HashMap());

    public void add(PonderStoryBoardEntry entry) {
        List<PonderStoryBoardEntry> list = (List<PonderStoryBoardEntry>) this.stories.computeIfAbsent(entry.getComponent(), $ -> new ArrayList());
        list.add(entry);
    }

    public void clear() {
        synchronized (PonderRegistry.ALL) {
            Set<ResourceLocation> toRemove = new HashSet();
            for (Entry<ResourceLocation, List<PonderStoryBoardEntry>> thisStoryEntry : this.stories.entrySet()) {
                List<PonderStoryBoardEntry> existingStories = (List<PonderStoryBoardEntry>) PonderRegistry.ALL.get(thisStoryEntry.getKey());
                existingStories.removeIf(story -> ((List) thisStoryEntry.getValue()).contains(story));
                if (existingStories.isEmpty()) {
                    toRemove.add((ResourceLocation) thisStoryEntry.getKey());
                }
            }
            toRemove.forEach(PonderRegistry.ALL::remove);
        }
        this.stories.clear();
    }

    public void compileLang() {
        this.stories.values().stream().flatMap(Collection::stream).forEach(entry -> PonderRegistry.compileScene(0, entry, null));
    }
}