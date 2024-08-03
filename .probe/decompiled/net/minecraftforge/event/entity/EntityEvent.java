package net.minecraftforge.event.entity;

import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.eventbus.api.Event;

public class EntityEvent extends Event {

    private final Entity entity;

    public EntityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public static class EnteringSection extends EntityEvent {

        private final long packedOldPos;

        private final long packedNewPos;

        public EnteringSection(Entity entity, long packedOldPos, long packedNewPos) {
            super(entity);
            this.packedOldPos = packedOldPos;
            this.packedNewPos = packedNewPos;
        }

        public long getPackedOldPos() {
            return this.packedOldPos;
        }

        public long getPackedNewPos() {
            return this.packedNewPos;
        }

        public SectionPos getOldPos() {
            return SectionPos.of(this.packedOldPos);
        }

        public SectionPos getNewPos() {
            return SectionPos.of(this.packedNewPos);
        }

        public boolean didChunkChange() {
            return SectionPos.x(this.packedOldPos) != SectionPos.x(this.packedNewPos) || SectionPos.z(this.packedOldPos) != SectionPos.z(this.packedNewPos);
        }
    }

    public static class EntityConstructing extends EntityEvent {

        public EntityConstructing(Entity entity) {
            super(entity);
        }
    }

    @Deprecated(forRemoval = true, since = "1.20.1")
    public static class EyeHeight extends EntityEvent {

        private final Pose pose;

        private final EntityDimensions size;

        private final float originalEyeHeight;

        private float newEyeHeight;

        public EyeHeight(Entity entity, Pose pose, EntityDimensions size, float eyeHeight) {
            super(entity);
            this.pose = pose;
            this.size = size;
            this.originalEyeHeight = eyeHeight;
            this.newEyeHeight = eyeHeight;
        }

        public Pose getPose() {
            return this.pose;
        }

        public EntityDimensions getSize() {
            return this.size;
        }

        public float getOriginalEyeHeight() {
            return this.originalEyeHeight;
        }

        public float getNewEyeHeight() {
            return this.newEyeHeight;
        }

        public void setNewEyeHeight(float newEyeHeight) {
            this.newEyeHeight = newEyeHeight;
        }
    }

    @Deprecated(forRemoval = true, since = "1.20.1")
    public static class Size extends EntityEvent {

        private final Pose pose;

        private final EntityDimensions originalSize;

        private EntityDimensions newSize;

        private final float oldEyeHeight;

        private float newEyeHeight;

        public Size(Entity entity, Pose pose, EntityDimensions size) {
            this(entity, pose, size, 1.0F);
        }

        public Size(Entity entity, Pose pose, EntityDimensions size, float defaultEyeHeight) {
            this(entity, pose, size, size, defaultEyeHeight, defaultEyeHeight);
        }

        public Size(Entity entity, Pose pose, EntityDimensions oldSize, EntityDimensions newSize, float oldEyeHeight, float newEyeHeight) {
            super(entity);
            this.pose = pose;
            this.originalSize = oldSize;
            this.newSize = newSize;
            this.oldEyeHeight = oldEyeHeight;
            this.newEyeHeight = newEyeHeight;
        }

        public Pose getPose() {
            return this.pose;
        }

        public EntityDimensions getOldSize() {
            return this.getOriginalSize();
        }

        public EntityDimensions getOriginalSize() {
            return this.originalSize;
        }

        public EntityDimensions getNewSize() {
            return this.newSize;
        }

        public void setNewSize(EntityDimensions size) {
            this.newSize = size;
        }

        public void setNewSize(EntityDimensions size, boolean updateEyeHeight) {
            this.setNewSize(size);
            if (updateEyeHeight) {
                this.newEyeHeight = this.getEntity().getEyeHeightAccess(this.getPose(), this.newSize);
            }
        }

        public float getOldEyeHeight() {
            return this.oldEyeHeight;
        }

        public float getNewEyeHeight() {
            return this.newEyeHeight;
        }

        public void setNewEyeHeight(float eyeHeight) {
            this.newEyeHeight = eyeHeight;
        }
    }
}