package com.github.einjerjar.mc.keymap.cross;

import com.github.einjerjar.mc.keymap.cross.services.IKeybindHelper;
import com.github.einjerjar.mc.keymap.cross.services.IPlatformHelper;
import com.github.einjerjar.mc.keymap.cross.services.ITickHelper;
import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static final IKeybindHelper KEYBIND = PLATFORM.keybindHelper();

    public static final ITickHelper TICK = PLATFORM.tickHelper();

    private Services() {
    }

    public static <T> T load(Class<T> c) {
        return (T) ServiceLoader.load(c).findFirst().orElseThrow(() -> new RuntimeException(String.format("Can't load service for %s", c.getName())));
    }
}