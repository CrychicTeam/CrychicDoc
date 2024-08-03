package pie.ilikepiefoo.events;

import dev.latvian.mods.kubejs.entity.EntityEventJS;
import net.minecraft.world.entity.Entity;

public class EntityEnterChunkEventJS extends EntityEventJS {

    private final Entity entity;

    private final int chunkX;

    private final int chunkY;

    private final int chunkZ;

    private final int prevX;

    private final int prevY;

    private final int prevZ;

    public EntityEnterChunkEventJS(Entity entity, int chunkX, int chunkY, int chunkZ, int prevX, int prevY, int prevZ) {
        this.entity = entity;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
        this.prevX = prevX;
        this.prevY = prevY;
        this.prevZ = prevZ;
    }

    public static EntityEnterChunkEventJS of(Entity entity, int chunkX, int chunkY, int chunkZ, int prevX, int prevY, int prevZ) {
        return new EntityEnterChunkEventJS(entity, chunkX, chunkY, chunkZ, prevX, prevY, prevZ);
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkY() {
        return this.chunkY;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }

    public int getPrevX() {
        return this.prevX;
    }

    public int getPrevY() {
        return this.prevY;
    }

    public int getPrevZ() {
        return this.prevZ;
    }
}