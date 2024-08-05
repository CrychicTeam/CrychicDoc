package net.blay09.mods.waystones.api;

import java.util.List;
import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public abstract class WaystoneTeleportEvent extends BalmEvent {

    public static class Post extends WaystoneTeleportEvent {

        private final IWaystoneTeleportContext context;

        private final List<Entity> teleportedEntities;

        public Post(IWaystoneTeleportContext context, List<Entity> teleportedEntities) {
            this.context = context;
            this.teleportedEntities = teleportedEntities;
        }

        public IWaystoneTeleportContext getContext() {
            return this.context;
        }

        public List<Entity> getTeleportedEntities() {
            return this.teleportedEntities;
        }
    }

    public static class Pre extends WaystoneTeleportEvent {

        private final IWaystoneTeleportContext context;

        private EventResult dimensionalTeleportResult = EventResult.DEFAULT;

        private EventResult consumeItemResult = EventResult.DEFAULT;

        public Pre(IWaystoneTeleportContext context) {
            this.context = context;
        }

        public IWaystoneTeleportContext getContext() {
            return this.context;
        }

        public int getXpCost() {
            return this.context.getXpCost();
        }

        public void setXpCost(int xpCost) {
            this.context.setXpCost(xpCost);
        }

        public int getCooldown() {
            return this.context.getCooldown();
        }

        public void setCooldown(int cooldown) {
            this.context.setCooldown(cooldown);
        }

        public TeleportDestination getDestination() {
            return this.context.getDestination();
        }

        public void setDestination(ServerLevel level, Vec3 location, Direction direction) {
            this.context.setDestination(new TeleportDestination(level, location, direction));
        }

        public void addAdditionalEntity(Entity additionalEntity) {
            this.context.addAdditionalEntity(additionalEntity);
        }

        public EventResult getDimensionalTeleportResult() {
            return this.dimensionalTeleportResult;
        }

        public void setDimensionalTeleportResult(EventResult dimensionalTeleportResult) {
            this.dimensionalTeleportResult = dimensionalTeleportResult;
        }

        public EventResult getConsumeItemResult() {
            return this.consumeItemResult;
        }

        public void setConsumeItemResult(EventResult consumeItemResult) {
            this.consumeItemResult = consumeItemResult;
        }
    }
}