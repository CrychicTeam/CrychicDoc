package org.embeddedt.embeddium.util.sodium;

import java.util.ServiceLoader;
import java.util.Set;
import java.util.ServiceLoader.Provider;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import net.fabricmc.loader.api.FabricLoader;
import org.embeddedt.embeddium.api.service.FlawlessFramesService;

public class FlawlessFrames {

    private static final Set<Object> ACTIVE = ConcurrentHashMap.newKeySet();

    public static void onClientInitialization() {
        Function<String, Consumer<Boolean>> provider = name -> {
            Object token = new Object();
            return active -> {
                if (active) {
                    ACTIVE.add(token);
                } else {
                    ACTIVE.remove(token);
                }
            };
        };
        try {
            FabricLoader.getInstance().getEntrypoints("frex_flawless_frames", Consumer.class).forEach(api -> api.accept(provider));
        } catch (NoClassDefFoundError var2) {
        }
        ServiceLoader.load(FlawlessFramesService.class, FlawlessFrames.class.getClassLoader()).stream().map(Provider::get).forEach(s -> s.acceptController(provider));
    }

    public static boolean isActive() {
        return !ACTIVE.isEmpty();
    }
}