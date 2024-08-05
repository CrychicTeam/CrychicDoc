package io.github.steveplays28.noisium.util.forge;

import net.minecraftforge.fml.loading.LoadingModList;

public class ModUtilImpl {

    public static boolean isModPresent(String id) {
        return LoadingModList.get().getModFileById(id) != null;
    }
}