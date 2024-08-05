package extensions.net.minecraft.world.entity.Entity;

import java.util.function.Function;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Extension
public final class EntityExt {

    private EntityExt() {
    }

    public static void spawn(@This Entity thiz, Level level, double x, double y, double z) {
        thiz.setPos(x, y, z);
        level.m_7967_(thiz);
    }

    public static void spawn(@This Entity thiz, Level level, Vec3 pos, Function<Entity, Entity> dataSupplier) {
        spawn((Entity) dataSupplier.apply(thiz), level, pos.x, pos.y, pos.z);
    }
}