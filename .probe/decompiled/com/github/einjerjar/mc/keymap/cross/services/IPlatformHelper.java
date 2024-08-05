package com.github.einjerjar.mc.keymap.cross.services;

import java.io.File;

public interface IPlatformHelper {

    String loader();

    boolean modLoaded(String var1);

    boolean dev();

    File config(String var1);

    IKeybindHelper keybindHelper();

    ITickHelper tickHelper();
}