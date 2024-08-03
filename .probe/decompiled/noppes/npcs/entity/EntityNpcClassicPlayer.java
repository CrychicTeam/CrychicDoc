package noppes.npcs.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class EntityNpcClassicPlayer extends EntityCustomNpc {

    public EntityNpcClassicPlayer(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
        this.display.setSkinTexture("customnpcs:textures/entity/humanmale/steve.png");
    }
}