package com.nameless.indestructible.api.animation.types;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.StunType;

public class AnimationEvent {

    private final Consumer<LivingEntityPatch<?>> event;

    private AnimationEvent(Consumer<LivingEntityPatch<?>> event) {
        this.event = event;
    }

    public void testAndExecute(LivingEntityPatch<?> entitypatch) {
        if (!entitypatch.isLogicalClient()) {
            this.event.accept(entitypatch);
        }
    }

    public static AnimationEvent create(Consumer<LivingEntityPatch<?>> event) {
        return new AnimationEvent(event);
    }

    public static class ConditionalEvent extends AnimationEvent {

        private final int condition;

        private ConditionalEvent(Consumer<LivingEntityPatch<?>> event, int condition) {
            super(event);
            this.condition = condition;
        }

        public static AnimationEvent.ConditionalEvent create(Consumer<LivingEntityPatch<?>> event, int condition) {
            return new AnimationEvent.ConditionalEvent(event, condition);
        }

        public static AnimationEvent.ConditionalEvent CreateStunCommandEvent(String command, StunType stunType) {
            Consumer<LivingEntityPatch<?>> event = entitypatch -> {
                Level server = ((LivingEntity) entitypatch.getOriginal()).m_9236_();
                CommandSourceStack css = ((LivingEntity) entitypatch.getOriginal()).m_20203_().withPermission(2).withSuppressedOutput();
                if (server.getServer() != null && entitypatch.getOriginal() != null) {
                    server.getServer().getCommands().performPrefixedCommand(css, command);
                }
            };
            return create(event, stunType.ordinal());
        }

        public void testAndExecute(LivingEntityPatch<?> entitypatch, int condition) {
            if (this.condition == condition) {
                super.testAndExecute(entitypatch);
            }
        }
    }

    public static class HitEvent {

        private final BiConsumer<LivingEntityPatch<?>, Entity> event;

        private HitEvent(BiConsumer<LivingEntityPatch<?>, Entity> event) {
            this.event = event;
        }

        public static AnimationEvent.HitEvent create(BiConsumer<LivingEntityPatch<?>, Entity> event) {
            return new AnimationEvent.HitEvent(event);
        }

        public static AnimationEvent.HitEvent CreateHitCommandEvent(String command, boolean isTarget) {
            BiConsumer<LivingEntityPatch<?>, Entity> event = (entitypatch, target) -> {
                Level server = ((LivingEntity) entitypatch.getOriginal()).m_9236_();
                CommandSourceStack css = ((LivingEntity) entitypatch.getOriginal()).m_20203_().withPermission(2).withSuppressedOutput();
                if (isTarget && target instanceof LivingEntity) {
                    css = css.withEntity(target);
                }
                if (server.getServer() != null && entitypatch.getOriginal() != null) {
                    server.getServer().getCommands().performPrefixedCommand(css, command);
                }
            };
            return create(event);
        }

        public void testAndExecute(LivingEntityPatch<?> entitypatch, Entity target) {
            if (!entitypatch.isLogicalClient()) {
                this.event.accept(entitypatch, target);
            }
        }
    }

    public static class TimeStampedEvent extends AnimationEvent implements Comparable<AnimationEvent.TimeStampedEvent> {

        private final float time;

        private TimeStampedEvent(float time, Consumer<LivingEntityPatch<?>> event) {
            super(event);
            this.time = time;
        }

        public void testAndExecute(LivingEntityPatch<?> entitypatch, float prevElapsed, float elapsed) {
            if (this.time >= prevElapsed && this.time < elapsed) {
                super.testAndExecute(entitypatch);
            }
        }

        public int compareTo(AnimationEvent.TimeStampedEvent event) {
            if (this.time == event.time) {
                return 0;
            } else {
                return this.time > event.time ? 1 : -1;
            }
        }

        public static AnimationEvent.TimeStampedEvent create(float time, Consumer<LivingEntityPatch<?>> event) {
            return new AnimationEvent.TimeStampedEvent(time, event);
        }

        public static AnimationEvent.TimeStampedEvent CreateTimeCommandEvent(float time, String command, boolean isTarget) {
            Consumer<LivingEntityPatch<?>> event = entitypatch -> {
                Level server = ((LivingEntity) entitypatch.getOriginal()).m_9236_();
                CommandSourceStack css = ((LivingEntity) entitypatch.getOriginal()).m_20203_().withPermission(2).withSuppressedOutput();
                if (isTarget && entitypatch.getTarget() != null) {
                    css = css.withEntity(entitypatch.getTarget());
                }
                if (server.getServer() != null && entitypatch.getOriginal() != null) {
                    server.getServer().getCommands().performPrefixedCommand(css, command);
                }
            };
            return create(time, event);
        }
    }
}