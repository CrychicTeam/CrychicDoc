package net.minecraftforge.registries;

import com.google.common.collect.Maps;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

public class ObjectHolderRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Set<Consumer<Predicate<ResourceLocation>>> objectHolders = new HashSet();

    private static final Type OBJECT_HOLDER = Type.getType(ObjectHolder.class);

    private static final Type MOD = Type.getType(Mod.class);

    private static final List<ObjectHolderRegistry.VanillaObjectHolderData> VANILLA_OBJECT_HOLDERS = List.of(new ObjectHolderRegistry.VanillaObjectHolderData("net.minecraft.world.level.block.Blocks", "block", "net.minecraft.world.level.block.Block"), new ObjectHolderRegistry.VanillaObjectHolderData("net.minecraft.world.item.Items", "item", "net.minecraft.world.item.Item"), new ObjectHolderRegistry.VanillaObjectHolderData("net.minecraft.world.item.enchantment.Enchantments", "enchantment", "net.minecraft.world.item.enchantment.Enchantment"), new ObjectHolderRegistry.VanillaObjectHolderData("net.minecraft.world.effect.MobEffects", "mob_effect", "net.minecraft.world.effect.MobEffect"), new ObjectHolderRegistry.VanillaObjectHolderData("net.minecraft.core.particles.ParticleTypes", "particle_type", "net.minecraft.core.particles.ParticleType"), new ObjectHolderRegistry.VanillaObjectHolderData("net.minecraft.sounds.SoundEvents", "sound_event", "net.minecraft.sounds.SoundEvent"));

    public static synchronized void addHandler(Consumer<Predicate<ResourceLocation>> ref) {
        objectHolders.add(ref);
    }

    public static synchronized boolean removeHandler(Consumer<Predicate<ResourceLocation>> ref) {
        return objectHolders.remove(ref);
    }

    public static void findObjectHolders() {
        LOGGER.debug(ForgeRegistry.REGISTRIES, "Processing ObjectHolder annotations");
        List<AnnotationData> annotations = ModList.get().getAllScanData().stream().map(ModFileScanData::getAnnotations).flatMap(Collection::stream).filter(a -> OBJECT_HOLDER.equals(a.annotationType()) || MOD.equals(a.annotationType())).toList();
        Map<Type, String> classModIds = Maps.newHashMap();
        Map<Type, Class<?>> classCache = Maps.newHashMap();
        annotations.stream().filter(a -> MOD.equals(a.annotationType())).forEach(data -> classModIds.put(data.clazz(), (String) data.annotationData().get("value")));
        VANILLA_OBJECT_HOLDERS.forEach(data -> {
            try {
                Class<?> holderClass = Class.forName(data.holderClass(), true, ObjectHolderRegistry.class.getClassLoader());
                Class<?> registryClass = Class.forName(data.registryType(), true, ObjectHolderRegistry.class.getClassLoader());
                Type holderType = Type.getType(holderClass);
                classCache.put(holderType, holderClass);
                scanTarget(classModIds, classCache, holderType, null, registryClass, data.registryName(), "minecraft", true, true);
            } catch (ClassNotFoundException var6) {
                throw new RuntimeException("Vanilla class not found, should not be possible", var6);
            }
        });
        annotations.stream().filter(a -> OBJECT_HOLDER.equals(a.annotationType())).filter(a -> a.targetType() == ElementType.FIELD).forEach(data -> scanTarget(classModIds, classCache, data.clazz(), data.memberName(), null, (String) data.annotationData().get("registryName"), (String) data.annotationData().get("value"), false, false));
        LOGGER.debug(ForgeRegistry.REGISTRIES, "Found {} ObjectHolder annotations", objectHolders.size());
    }

    private static void scanTarget(Map<Type, String> classModIds, Map<Type, Class<?>> classCache, Type type, @Nullable String annotationTarget, @Nullable Class<?> registryClass, String registryName, String value, boolean isClass, boolean extractFromValue) {
        Class<?> clazz;
        if (classCache.containsKey(type)) {
            clazz = (Class<?>) classCache.get(type);
        } else {
            try {
                clazz = Class.forName(type.getClassName(), extractFromValue, ObjectHolderRegistry.class.getClassLoader());
                classCache.put(type, clazz);
            } catch (ClassNotFoundException var13) {
                throw new RuntimeException(var13);
            }
        }
        if (isClass) {
            scanClassForFields(classModIds, type, new ResourceLocation(registryName), registryClass, value, clazz, extractFromValue);
        } else {
            if (value.indexOf(58) == -1) {
                String prefix = (String) classModIds.get(type);
                if (prefix == null) {
                    LOGGER.warn(ForgeRegistry.REGISTRIES, "Found an unqualified ObjectHolder annotation ({}) without a modid context at {}.{}, ignoring", value, type, annotationTarget);
                    throw new IllegalStateException("Unqualified reference to ObjectHolder");
                }
                value = prefix + ":" + value;
            }
            try {
                Field f = clazz.getDeclaredField(annotationTarget);
                ObjectHolderRef ref = ObjectHolderRef.create(new ResourceLocation(registryName), f, value, extractFromValue);
                if (ref != null) {
                    addHandler(ref);
                }
            } catch (NoSuchFieldException var12) {
                throw new RuntimeException(var12);
            }
        }
    }

    private static void scanClassForFields(Map<Type, String> classModIds, Type targetClass, ResourceLocation registryName, Class<?> registryClass, String value, Class<?> clazz, boolean extractFromExistingValues) {
        classModIds.put(targetClass, value);
        int flags = 4105;
        for (Field f : clazz.getFields()) {
            if ((f.getModifiers() & 4105) == 4105 && !f.isAnnotationPresent(ObjectHolder.class) && registryClass.isAssignableFrom(f.getType())) {
                ObjectHolderRef ref = ObjectHolderRef.create(registryName, f, value + ":" + f.getName().toLowerCase(Locale.ENGLISH), extractFromExistingValues);
                if (ref != null) {
                    addHandler(ref);
                }
            }
        }
    }

    private static ResourceLocation getRegistryName(Map<Type, ResourceLocation> classRegistryNames, @Nullable String registryName, Type targetClass, Object declaration) {
        if (registryName != null) {
            return new ResourceLocation(registryName);
        } else if (classRegistryNames.containsKey(targetClass)) {
            return (ResourceLocation) classRegistryNames.get(targetClass);
        } else {
            throw new IllegalStateException("No registry name was declared for " + declaration);
        }
    }

    public static void applyObjectHolders() {
        try {
            LOGGER.debug(ForgeRegistry.REGISTRIES, "Applying holder lookups");
            applyObjectHolders(key -> true);
            LOGGER.debug(ForgeRegistry.REGISTRIES, "Holder lookups applied");
        } catch (RuntimeException var1) {
            LOGGER.error("", var1);
        }
    }

    public static void applyObjectHolders(Predicate<ResourceLocation> filter) {
        RuntimeException aggregate = new RuntimeException("Failed to apply some object holders, see suppressed exceptions for details");
        objectHolders.forEach(objectHolder -> {
            try {
                objectHolder.accept(filter);
            } catch (Exception var4) {
                aggregate.addSuppressed(var4);
            }
        });
        if (aggregate.getSuppressed().length > 0) {
            throw aggregate;
        }
    }

    private static record VanillaObjectHolderData(String holderClass, String registryName, String registryType) {
    }
}