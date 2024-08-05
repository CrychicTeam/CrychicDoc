package net.minecraft.world.level.gameevent;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;

public class DynamicGameEventListener<T extends GameEventListener> {

    private final T listener;

    @Nullable
    private SectionPos lastSection;

    public DynamicGameEventListener(T t0) {
        this.listener = t0;
    }

    public void add(ServerLevel serverLevel0) {
        this.move(serverLevel0);
    }

    public T getListener() {
        return this.listener;
    }

    public void remove(ServerLevel serverLevel0) {
        ifChunkExists(serverLevel0, this.lastSection, p_248453_ -> p_248453_.unregister(this.listener));
    }

    public void move(ServerLevel serverLevel0) {
        this.listener.getListenerSource().getPosition(serverLevel0).map(SectionPos::m_235863_).ifPresent(p_223621_ -> {
            if (this.lastSection == null || !this.lastSection.equals(p_223621_)) {
                ifChunkExists(serverLevel0, this.lastSection, p_248452_ -> p_248452_.unregister(this.listener));
                this.lastSection = p_223621_;
                ifChunkExists(serverLevel0, this.lastSection, p_248451_ -> p_248451_.register(this.listener));
            }
        });
    }

    private static void ifChunkExists(LevelReader levelReader0, @Nullable SectionPos sectionPos1, Consumer<GameEventListenerRegistry> consumerGameEventListenerRegistry2) {
        if (sectionPos1 != null) {
            ChunkAccess $$3 = levelReader0.getChunk(sectionPos1.x(), sectionPos1.z(), ChunkStatus.FULL, false);
            if ($$3 != null) {
                consumerGameEventListenerRegistry2.accept($$3.getListenerRegistry(sectionPos1.y()));
            }
        }
    }
}