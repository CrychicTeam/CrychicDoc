package noppes.npcs.api.entity;

import net.minecraft.world.entity.projectile.ThrowableProjectile;
import noppes.npcs.api.item.IItemStack;

public interface IProjectile<T extends ThrowableProjectile> extends IThrowable<T> {

    IItemStack getItem();

    void setItem(IItemStack var1);

    boolean getHasGravity();

    void setHasGravity(boolean var1);

    int getAccuracy();

    void setAccuracy(int var1);

    void setHeading(IEntity var1);

    void setHeading(double var1, double var3, double var5);

    void setHeading(float var1, float var2);

    void enableEvents();
}