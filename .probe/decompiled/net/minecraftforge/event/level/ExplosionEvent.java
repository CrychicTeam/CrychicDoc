package net.minecraftforge.event.level;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class ExplosionEvent extends Event {

    private final Level level;

    private final Explosion explosion;

    public ExplosionEvent(Level level, Explosion explosion) {
        this.level = level;
        this.explosion = explosion;
    }

    public Level getLevel() {
        return this.level;
    }

    public Explosion getExplosion() {
        return this.explosion;
    }

    public static class Detonate extends ExplosionEvent {

        private final List<Entity> entityList;

        public Detonate(Level level, Explosion explosion, List<Entity> entityList) {
            super(level, explosion);
            this.entityList = entityList;
        }

        public List<BlockPos> getAffectedBlocks() {
            return this.getExplosion().getToBlow();
        }

        public List<Entity> getAffectedEntities() {
            return this.entityList;
        }
    }

    @Cancelable
    public static class Start extends ExplosionEvent {

        public Start(Level level, Explosion explosion) {
            super(level, explosion);
        }
    }
}