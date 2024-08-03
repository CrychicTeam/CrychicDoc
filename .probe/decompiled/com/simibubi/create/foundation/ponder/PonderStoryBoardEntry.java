package com.simibubi.create.foundation.ponder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public class PonderStoryBoardEntry {

    private final PonderStoryBoardEntry.PonderStoryBoard board;

    private final String namespace;

    private final ResourceLocation schematicLocation;

    private final ResourceLocation component;

    private final List<PonderTag> tags;

    public PonderStoryBoardEntry(PonderStoryBoardEntry.PonderStoryBoard board, String namespace, ResourceLocation schematicLocation, ResourceLocation component) {
        this.board = board;
        this.namespace = namespace;
        this.schematicLocation = schematicLocation;
        this.component = component;
        this.tags = new ArrayList();
    }

    public PonderStoryBoardEntry(PonderStoryBoardEntry.PonderStoryBoard board, String namespace, String schematicPath, ResourceLocation component) {
        this(board, namespace, new ResourceLocation(namespace, schematicPath), component);
    }

    public PonderStoryBoardEntry.PonderStoryBoard getBoard() {
        return this.board;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public ResourceLocation getSchematicLocation() {
        return this.schematicLocation;
    }

    public ResourceLocation getComponent() {
        return this.component;
    }

    public List<PonderTag> getTags() {
        return this.tags;
    }

    public PonderStoryBoardEntry highlightTag(PonderTag tag) {
        this.tags.add(tag);
        return this;
    }

    public PonderStoryBoardEntry highlightTags(PonderTag... tags) {
        Collections.addAll(this.tags, tags);
        return this;
    }

    public PonderStoryBoardEntry highlightAllTags() {
        this.tags.add(PonderTag.HIGHLIGHT_ALL);
        return this;
    }

    public PonderStoryBoardEntry chapter(PonderChapter chapter) {
        PonderRegistry.CHAPTERS.addStoriesToChapter(chapter, this);
        return this;
    }

    public PonderStoryBoardEntry chapters(PonderChapter... chapters) {
        for (PonderChapter c : chapters) {
            this.chapter(c);
        }
        return this;
    }

    @FunctionalInterface
    public interface PonderStoryBoard {

        void program(SceneBuilder var1, SceneBuildingUtil var2);
    }
}