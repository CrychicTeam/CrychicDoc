package com.simibubi.create.foundation.ponder;

import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public class PonderChapterRegistry {

    private final Map<ResourceLocation, Pair<PonderChapter, List<PonderStoryBoardEntry>>> chapters = new HashMap();

    PonderChapter addChapter(@Nonnull PonderChapter chapter) {
        synchronized (this.chapters) {
            this.chapters.put(chapter.getId(), Pair.of(chapter, new ArrayList()));
            return chapter;
        }
    }

    @Nullable
    PonderChapter getChapter(ResourceLocation id) {
        Pair<PonderChapter, List<PonderStoryBoardEntry>> pair = (Pair<PonderChapter, List<PonderStoryBoardEntry>>) this.chapters.get(id);
        return pair == null ? null : pair.getFirst();
    }

    public void addStoriesToChapter(@Nonnull PonderChapter chapter, PonderStoryBoardEntry... entries) {
        List<PonderStoryBoardEntry> entryList = (List<PonderStoryBoardEntry>) ((Pair) this.chapters.get(chapter.getId())).getSecond();
        synchronized (entryList) {
            Collections.addAll(entryList, entries);
        }
    }

    public List<PonderChapter> getAllChapters() {
        return (List<PonderChapter>) this.chapters.values().stream().map(Pair::getFirst).collect(Collectors.toList());
    }

    public List<PonderStoryBoardEntry> getStories(PonderChapter chapter) {
        return (List<PonderStoryBoardEntry>) ((Pair) this.chapters.get(chapter.getId())).getSecond();
    }
}