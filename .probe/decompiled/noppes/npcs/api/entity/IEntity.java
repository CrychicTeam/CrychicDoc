package noppes.npcs.api.entity;

import net.minecraft.world.entity.Entity;
import noppes.npcs.api.INbt;
import noppes.npcs.api.IPos;
import noppes.npcs.api.IRayTrace;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.item.IItemStack;

public interface IEntity<T extends Entity> {

    double getX();

    void setX(double var1);

    double getY();

    void setY(double var1);

    double getZ();

    void setZ(double var1);

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    IPos getPos();

    void setPos(IPos var1);

    void setPosition(double var1, double var3, double var5);

    void setRotation(float var1);

    float getRotation();

    float getHeight();

    float getEyeHeight();

    float getWidth();

    void setPitch(float var1);

    float getPitch();

    IEntity getMount();

    void setMount(IEntity var1);

    IEntity[] getRiders();

    IEntity[] getAllRiders();

    void addRider(IEntity var1);

    void clearRiders();

    void knockback(int var1, float var2);

    boolean isSneaking();

    boolean isSprinting();

    IEntityItem dropItem(IItemStack var1);

    boolean inWater();

    boolean inFire();

    boolean inLava();

    IData getTempdata();

    IData getStoreddata();

    INbt getNbt();

    boolean isAlive();

    long getAge();

    void despawn();

    void spawn();

    void kill();

    boolean isBurning();

    void setBurning(int var1);

    void extinguish();

    IWorld getWorld();

    String getTypeName();

    int getType();

    boolean typeOf(int var1);

    T getMCEntity();

    String getUUID();

    String generateNewUUID();

    void storeAsClone(int var1, String var2);

    INbt getEntityNbt();

    void setEntityNbt(INbt var1);

    IRayTrace rayTraceBlock(double var1, boolean var3, boolean var4);

    IEntity[] rayTraceEntities(double var1, boolean var3, boolean var4);

    String[] getTags();

    void addTag(String var1);

    boolean hasTag(String var1);

    void removeTag(String var1);

    void playAnimation(int var1);

    void damage(float var1);

    double getMotionX();

    double getMotionY();

    double getMotionZ();

    void setMotionX(double var1);

    void setMotionY(double var1);

    void setMotionZ(double var1);

    String getName();

    void setName(String var1);

    boolean hasCustomName();

    String getEntityName();
}