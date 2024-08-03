package dev.architectury.hooks.level.forge;

import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

public class ExplosionHooksImpl {

    public static Vec3 getPosition(Explosion explosion) {
        return explosion.getPosition();
    }
}