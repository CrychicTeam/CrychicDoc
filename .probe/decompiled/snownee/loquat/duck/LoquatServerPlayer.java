package snownee.loquat.duck;

import java.util.Set;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import snownee.loquat.core.RestrictInstance;
import snownee.loquat.core.area.Area;

public interface LoquatServerPlayer {

    Set<Area> loquat$getAreasIn();

    RestrictInstance loquat$getRestrictionInstance();

    void loquat$setLastGoodPos(Vec3 var1);

    @Nullable
    Vec3 loquat$getLastGoodPos();

    void loquat$reset();
}