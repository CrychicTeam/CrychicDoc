package dev.architectury.hooks.level;

import dev.architectury.hooks.level.forge.ExplosionHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

public final class ExplosionHooks {

    private ExplosionHooks() {
    }

    @ExpectPlatform
    @Transformed
    public static Vec3 getPosition(Explosion explosion) {
        return ExplosionHooksImpl.getPosition(explosion);
    }
}