package com.almostreliable.ponderjs.particles;

import com.almostreliable.ponderjs.util.PonderErrorHelper;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface ParticleTransformation {

    static ParticleTransformation onlyPosition(ParticleTransformation.Simple transformation) {
        return (partialTick, position, motion) -> new ParticleTransformation.Data(transformation.apply(partialTick, position), motion);
    }

    static ParticleTransformation onlyMotion(ParticleTransformation.Simple transformation) {
        return (partialTick, position, motion) -> new ParticleTransformation.Data(position, transformation.apply(partialTick, motion));
    }

    ParticleTransformation.Data apply(float var1, Vec3 var2, Vec3 var3);

    public static record Data(Vec3 position, Vec3 motion) {

        public static ParticleTransformation.Data of(@Nullable Object o) {
            if (o instanceof List<?> list && list.size() >= 2) {
                Vec3 pos = UtilsJS.vec3Of(list.get(0));
                Vec3 motion = UtilsJS.vec3Of(list.get(1));
                return new ParticleTransformation.Data(pos, motion);
            }
            IllegalArgumentException e = new IllegalArgumentException("Invalid format for particle transformation data. Please use [position, motion] or [[x, y, z], [mx, my, mz]]");
            PonderErrorHelper.yeet(e);
            return new ParticleTransformation.Data(Vec3.ZERO, Vec3.ZERO);
        }
    }

    @FunctionalInterface
    public interface Simple {

        Vec3 apply(float var1, Vec3 var2);
    }
}