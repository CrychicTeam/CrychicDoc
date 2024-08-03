package me.jellysquid.mods.lithium.mixin.entity.collisions.intersection;

import me.jellysquid.mods.lithium.common.entity.LithiumEntityCollisions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Level.class })
public abstract class WorldMixin implements LevelAccessor {

    @Override
    public boolean noCollision(@Nullable Entity entity, AABB box) {
        boolean ret = !LithiumEntityCollisions.doesBoxCollideWithBlocks((Level) this, entity, box);
        if (ret && this instanceof EntityGetter) {
            ret = !LithiumEntityCollisions.doesBoxCollideWithHardEntities(this, entity, box);
        }
        if (ret && entity != null) {
            ret = !LithiumEntityCollisions.doesEntityCollideWithWorldBorder(this, entity);
        }
        return ret;
    }
}