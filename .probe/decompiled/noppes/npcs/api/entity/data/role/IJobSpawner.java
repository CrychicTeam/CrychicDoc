package noppes.npcs.api.entity.data.role;

import noppes.npcs.api.entity.IEntityLiving;

public interface IJobSpawner {

    IEntityLiving spawnEntity(int var1);

    void removeAllSpawned();
}