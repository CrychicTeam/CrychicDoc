package noppes.npcs.api.event;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.IEntity;

@Cancelable
public class ForgeEvent extends CustomNPCsEvent {

    public final Event event;

    public ForgeEvent(Event event) {
        this.event = event;
    }

    @Cancelable
    public static class EntityEvent extends ForgeEvent {

        public final IEntity entity;

        public EntityEvent(net.minecraftforge.event.entity.EntityEvent event, IEntity entity) {
            super(event);
            this.entity = entity;
        }
    }

    public static class InitEvent extends ForgeEvent {

        public InitEvent() {
            super(null);
        }
    }

    @Cancelable
    public static class LevelEvent extends ForgeEvent {

        public final IWorld world;

        public LevelEvent(net.minecraftforge.event.level.LevelEvent event, IWorld world) {
            super(event);
            this.world = world;
        }
    }
}