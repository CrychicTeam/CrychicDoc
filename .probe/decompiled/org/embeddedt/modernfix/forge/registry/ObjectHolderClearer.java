package org.embeddedt.modernfix.forge.registry;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ObjectHolderRegistry;
import org.embeddedt.modernfix.ModernFix;

public class ObjectHolderClearer {

    public static void clearThrowables() {
        Set<Consumer<Predicate<ResourceLocation>>> holders = (Set<Consumer<Predicate<ResourceLocation>>>) ObfuscationReflectionHelper.getPrivateValue(ObjectHolderRegistry.class, null, "objectHolders");
        if (holders != null) {
            int numCleared = 0;
            HashMap<Class<?>, Field> throwableField = new HashMap();
            Throwable singletonThrowable = new Throwable("[This stacktrace was cleared to save memory]");
            try {
                for (Consumer<Predicate<ResourceLocation>> holder : holders) {
                    Field target = (Field) throwableField.computeIfAbsent(holder.getClass(), clz -> {
                        Field[] clzFields = clz.getDeclaredFields();
                        for (Field f : clzFields) {
                            if (Throwable.class.isAssignableFrom(f.getType())) {
                                f.setAccessible(true);
                                return f;
                            }
                        }
                        return null;
                    });
                    if (target != null) {
                        target.set(holder, singletonThrowable);
                        numCleared++;
                    }
                }
            } catch (ReflectiveOperationException | NoClassDefFoundError | RuntimeException var7) {
            }
            ModernFix.LOGGER.debug("Cleared " + numCleared + " object holder stacktrace references");
        }
    }
}