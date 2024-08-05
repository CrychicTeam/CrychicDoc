package net.minecraftforge.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

@Cancelable
public class VanillaGameEvent extends Event {

    private final Level level;

    private final GameEvent vanillaEvent;

    private final Vec3 position;

    private final GameEvent.Context context;

    public VanillaGameEvent(Level level, GameEvent vanillaEvent, Vec3 position, GameEvent.Context context) {
        this.level = level;
        this.vanillaEvent = vanillaEvent;
        this.position = position;
        this.context = context;
    }

    public Level getLevel() {
        return this.level;
    }

    @Nullable
    public Entity getCause() {
        return this.context.sourceEntity();
    }

    public GameEvent getVanillaEvent() {
        return this.vanillaEvent;
    }

    public Vec3 getEventPosition() {
        return this.position;
    }

    public GameEvent.Context getContext() {
        return this.context;
    }
}