package com.github.einjerjar.mc.keymap.cross.services;

import net.minecraft.client.Minecraft;

public interface ITickHelper {

    void registerEndClientTick(ITickHelper.EndTickListener var1);

    public interface EndTickListener {

        void execute(Minecraft var1);
    }
}