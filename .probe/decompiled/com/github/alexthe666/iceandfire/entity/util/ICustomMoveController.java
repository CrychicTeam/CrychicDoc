package com.github.alexthe666.iceandfire.entity.util;

public interface ICustomMoveController {

    void up(boolean var1);

    void down(boolean var1);

    void attack(boolean var1);

    void strike(boolean var1);

    void dismount(boolean var1);

    void setControlState(byte var1);

    byte getControlState();
}