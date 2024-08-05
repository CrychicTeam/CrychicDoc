package noppes.npcs.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class EntityNpcAlex extends EntityCustomNpc {

    public EntityNpcAlex(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
        this.display.setSkinTexture("customnpcs:textures/entity/alex_skins/alex_2.png");
    }
}