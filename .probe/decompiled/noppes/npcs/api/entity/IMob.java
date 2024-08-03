package noppes.npcs.api.entity;

import net.minecraft.world.entity.Mob;
import noppes.npcs.api.IPos;

public interface IMob<T extends Mob> extends IEntityLiving<T> {

    boolean isNavigating();

    void clearNavigation();

    void navigateTo(double var1, double var3, double var5, double var7);

    void jump();

    T getMCEntity();

    IPos getNavigationPath();
}