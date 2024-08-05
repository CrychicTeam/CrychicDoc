package com.simibubi.create.foundation.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import java.util.Arrays;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public class PonderRegistrationHelper {

    protected String namespace;

    public PonderRegistrationHelper(String namespace) {
        this.namespace = namespace;
    }

    public PonderStoryBoardEntry addStoryBoard(ResourceLocation component, ResourceLocation schematicLocation, PonderStoryBoardEntry.PonderStoryBoard storyBoard, PonderTag... tags) {
        PonderStoryBoardEntry entry = this.createStoryBoardEntry(storyBoard, schematicLocation, component);
        entry.highlightTags(tags);
        PonderRegistry.addStoryBoard(entry);
        return entry;
    }

    public PonderStoryBoardEntry addStoryBoard(ResourceLocation component, String schematicPath, PonderStoryBoardEntry.PonderStoryBoard storyBoard, PonderTag... tags) {
        return this.addStoryBoard(component, this.asLocation(schematicPath), storyBoard, tags);
    }

    public PonderStoryBoardEntry addStoryBoard(ItemProviderEntry<?> component, ResourceLocation schematicLocation, PonderStoryBoardEntry.PonderStoryBoard storyBoard, PonderTag... tags) {
        return this.addStoryBoard(component.getId(), schematicLocation, storyBoard, tags);
    }

    public PonderStoryBoardEntry addStoryBoard(ItemProviderEntry<?> component, String schematicPath, PonderStoryBoardEntry.PonderStoryBoard storyBoard, PonderTag... tags) {
        return this.addStoryBoard(component, this.asLocation(schematicPath), storyBoard, tags);
    }

    public PonderRegistrationHelper.MultiSceneBuilder forComponents(ItemProviderEntry<?>... components) {
        return new PonderRegistrationHelper.MultiSceneBuilder(Arrays.asList(components));
    }

    public PonderRegistrationHelper.MultiSceneBuilder forComponents(Iterable<? extends ItemProviderEntry<?>> components) {
        return new PonderRegistrationHelper.MultiSceneBuilder(components);
    }

    public PonderStoryBoardEntry createStoryBoardEntry(PonderStoryBoardEntry.PonderStoryBoard storyBoard, ResourceLocation schematicLocation, ResourceLocation component) {
        return new PonderStoryBoardEntry(storyBoard, this.namespace, schematicLocation, component);
    }

    public PonderStoryBoardEntry createStoryBoardEntry(PonderStoryBoardEntry.PonderStoryBoard storyBoard, String schematicPath, ResourceLocation component) {
        return this.createStoryBoardEntry(storyBoard, this.asLocation(schematicPath), component);
    }

    public PonderTag createTag(String name) {
        return new PonderTag(this.asLocation(name));
    }

    public PonderChapter getOrCreateChapter(String name) {
        return PonderChapter.of(this.asLocation(name));
    }

    public ResourceLocation asLocation(String path) {
        return new ResourceLocation(this.namespace, path);
    }

    public class MultiSceneBuilder {

        protected Iterable<? extends ItemProviderEntry<?>> components;

        protected MultiSceneBuilder(Iterable<? extends ItemProviderEntry<?>> components) {
            this.components = components;
        }

        public PonderRegistrationHelper.MultiSceneBuilder addStoryBoard(ResourceLocation schematicLocation, PonderStoryBoardEntry.PonderStoryBoard storyBoard) {
            return this.addStoryBoard(schematicLocation, storyBoard, $ -> {
            });
        }

        public PonderRegistrationHelper.MultiSceneBuilder addStoryBoard(ResourceLocation schematicLocation, PonderStoryBoardEntry.PonderStoryBoard storyBoard, PonderTag... tags) {
            return this.addStoryBoard(schematicLocation, storyBoard, sb -> sb.highlightTags(tags));
        }

        public PonderRegistrationHelper.MultiSceneBuilder addStoryBoard(ResourceLocation schematicLocation, PonderStoryBoardEntry.PonderStoryBoard storyBoard, Consumer<PonderStoryBoardEntry> extras) {
            this.components.forEach(c -> extras.accept(PonderRegistrationHelper.this.addStoryBoard(c, schematicLocation, storyBoard)));
            return this;
        }

        public PonderRegistrationHelper.MultiSceneBuilder addStoryBoard(String schematicPath, PonderStoryBoardEntry.PonderStoryBoard storyBoard) {
            return this.addStoryBoard(PonderRegistrationHelper.this.asLocation(schematicPath), storyBoard);
        }

        public PonderRegistrationHelper.MultiSceneBuilder addStoryBoard(String schematicPath, PonderStoryBoardEntry.PonderStoryBoard storyBoard, PonderTag... tags) {
            return this.addStoryBoard(PonderRegistrationHelper.this.asLocation(schematicPath), storyBoard, tags);
        }

        public PonderRegistrationHelper.MultiSceneBuilder addStoryBoard(String schematicPath, PonderStoryBoardEntry.PonderStoryBoard storyBoard, Consumer<PonderStoryBoardEntry> extras) {
            return this.addStoryBoard(PonderRegistrationHelper.this.asLocation(schematicPath), storyBoard, extras);
        }
    }
}