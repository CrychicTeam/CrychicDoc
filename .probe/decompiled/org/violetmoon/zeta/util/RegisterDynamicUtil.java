package org.violetmoon.zeta.util;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.violetmoon.zeta.Zeta;

public class RegisterDynamicUtil {

    private static final Set<Zeta> interestedParties = new HashSet(2);

    public static void signup(Zeta z) {
        interestedParties.add(z);
    }

    public static <E> void onRegisterDynamic(RegistryOps.RegistryInfoLookup lookup, ResourceKey<? extends Registry<E>> registryId, WritableRegistry<E> registry) {
        interestedParties.forEach(z -> z.registry.performDynamicRegistration(lookup, registryId, registry));
    }
}