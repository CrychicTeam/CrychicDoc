package me.lucko.spark.forge;

import cpw.mods.modlauncher.TransformingClassLoader;
import me.lucko.spark.common.sampler.source.ClassSourceLookup;

public class ForgeClassSourceLookup implements ClassSourceLookup {

    @Override
    public String identify(Class<?> clazz) {
        if (!(clazz.getClassLoader() instanceof TransformingClassLoader)) {
            return null;
        } else {
            String name = clazz.getModule().getName();
            return !name.equals("forge") && !name.equals("minecraft") ? name : null;
        }
    }
}