package dev.latvian.mods.kubejs.level;

import dev.architectury.hooks.level.ExplosionHooks;
import dev.latvian.mods.kubejs.player.EntityArrayList;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class ExplosionEventJS extends LevelEventJS {

    protected final Level level;

    protected final Explosion explosion;

    public ExplosionEventJS(Level level, Explosion explosion) {
        this.level = level;
        this.explosion = explosion;
    }

    @Override
    public Level getLevel() {
        return this.level;
    }

    public Vec3 getPosition() {
        return ExplosionHooks.getPosition(this.explosion);
    }

    public double getX() {
        return this.getPosition().x;
    }

    public double getY() {
        return this.getPosition().y;
    }

    public double getZ() {
        return this.getPosition().z;
    }

    public BlockContainerJS getBlock() {
        return new BlockContainerJS(this.level, BlockPos.containing(this.getPosition()));
    }

    @Nullable
    public LivingEntity getExploder() {
        return this.explosion.getIndirectSourceEntity();
    }

    @Info("Invoked right after an explosion happens.\n")
    public static class After extends ExplosionEventJS {

        private final List<Entity> affectedEntities;

        public After(Level level, Explosion explosion, List<Entity> affectedEntities) {
            super(level, explosion);
            this.affectedEntities = affectedEntities;
        }

        @Info("Gets a list of all entities affected by the explosion.")
        public EntityArrayList getAffectedEntities() {
            return new EntityArrayList(this.level, this.affectedEntities);
        }

        @Info("Remove an entity from the list of affected entities.")
        public void removeAffectedEntity(Entity entity) {
            this.affectedEntities.remove(entity);
        }

        @Info("Remove all entities from the list of affected entities.")
        public void removeAllAffectedEntities() {
            this.affectedEntities.clear();
        }

        @Info("Gets a list of all blocks affected by the explosion.")
        public List<BlockContainerJS> getAffectedBlocks() {
            List<BlockContainerJS> list = new ArrayList(this.explosion.getToBlow().size());
            for (BlockPos pos : this.explosion.getToBlow()) {
                list.add(new BlockContainerJS(this.level, pos));
            }
            return list;
        }

        @Info("Remove a block from the list of affected blocks.")
        public void removeAffectedBlock(BlockContainerJS block) {
            this.explosion.getToBlow().remove(block.getPos());
        }

        @Info("Remove all blocks from the list of affected blocks.")
        public void removeAllAffectedBlocks() {
            this.explosion.getToBlow().clear();
        }

        @Info("Remove all knockback from all affected *players*.")
        public void removeKnockback() {
            this.explosion.getHitPlayers().clear();
        }
    }

    @Info("Invoked right before an explosion happens.\n")
    public static class Before extends ExplosionEventJS {

        public Before(Level level, Explosion explosion) {
            super(level, explosion);
        }

        @Info("Returns the size of the explosion.")
        public float getSize() {
            return this.explosion.radius;
        }

        @Info("Sets the size of the explosion.")
        public void setSize(float s) {
            this.explosion.radius = s;
        }
    }
}