package dev.latvian.mods.kubejs.block;

import dev.latvian.mods.kubejs.entity.EntityEventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Info("Invoked when an entity attempts to trample farmland.\n")
public class FarmlandTrampledEventJS extends EntityEventJS {

    private final Level level;

    private final BlockContainerJS block;

    private final float distance;

    private final Entity entity;

    public FarmlandTrampledEventJS(Level l, BlockPos pos, BlockState state, float d, Entity e) {
        this.level = l;
        this.block = this.level.kjs$getBlock(pos);
        this.block.cachedState = state;
        this.distance = d;
        this.entity = e;
    }

    @Info("The distance of the entity from the block.")
    public float getDistance() {
        return this.distance;
    }

    @Info("The entity that is attempting to trample the farmland.")
    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Info("The level that the farmland and the entity are in.")
    @Override
    public Level getLevel() {
        return this.level;
    }

    @Info("The farmland block.")
    public BlockContainerJS getBlock() {
        return this.block;
    }
}