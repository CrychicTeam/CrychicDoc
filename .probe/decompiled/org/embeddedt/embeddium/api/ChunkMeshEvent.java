package org.embeddedt.embeddium.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import org.embeddedt.embeddium.api.eventbus.EmbeddiumEvent;
import org.embeddedt.embeddium.api.eventbus.EventHandlerRegistrar;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ChunkMeshEvent extends EmbeddiumEvent {

    public static final EventHandlerRegistrar<ChunkMeshEvent> BUS = new EventHandlerRegistrar<>();

    private final Level world;

    private final SectionPos sectionOrigin;

    private List<MeshAppender> meshAppenders = null;

    ChunkMeshEvent(Level world, SectionPos sectionOrigin) {
        this.world = world;
        this.sectionOrigin = sectionOrigin;
    }

    public Level getWorld() {
        return this.world;
    }

    public SectionPos getSectionOrigin() {
        return this.sectionOrigin;
    }

    public void addMeshAppender(MeshAppender appender) {
        if (this.meshAppenders == null) {
            this.meshAppenders = new ArrayList();
        }
        this.meshAppenders.add(appender);
    }

    @Internal
    public static List<MeshAppender> post(Level world, SectionPos origin) {
        ChunkMeshEvent event = new ChunkMeshEvent(world, origin);
        BUS.post(event);
        return (List<MeshAppender>) Objects.requireNonNullElse(event.meshAppenders, Collections.emptyList());
    }
}