package dev.xkmc.l2weaponry.content.capability;

public interface IShieldData {

    double getShieldDefense();

    void setShieldDefense(double var1);

    int getReflectTimer();

    boolean canReflect();

    double popRetain();
}