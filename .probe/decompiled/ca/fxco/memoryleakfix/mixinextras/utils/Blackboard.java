package ca.fxco.memoryleakfix.mixinextras.utils;

import org.spongepowered.asm.service.IGlobalPropertyService;
import org.spongepowered.asm.service.MixinService;

public class Blackboard {

    private static final IGlobalPropertyService SERVICE = MixinService.getGlobalPropertyService();

    public static <T> T get(String key) {
        Object[] impl = (Object[]) SERVICE.getProperty(SERVICE.resolveKey(key));
        return (T) (impl == null ? null : impl[0]);
    }

    public static void put(String key, Object value) {
        SERVICE.setProperty(SERVICE.resolveKey(key), new Object[1]);
        ((Object[]) SERVICE.getProperty(SERVICE.resolveKey(key)))[0] = value;
    }
}