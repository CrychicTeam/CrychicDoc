package net.minecraftforge.registries;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

class ObjectHolderRef implements Consumer<Predicate<ResourceLocation>> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Field field;

    private final ResourceLocation injectedObject;

    private final ForgeRegistry<?> registry;

    @Nullable
    static ObjectHolderRef create(ResourceLocation registryName, Field field, String injectedObject, boolean extractFromExistingValues) {
        ForgeRegistry<?> registry = RegistryManager.ACTIVE.getRegistry(registryName);
        if (registry == null) {
            return null;
        } else {
            ResourceLocation injectedObjectName;
            if (extractFromExistingValues) {
                try {
                    Object existing = field.get(null);
                    if (existing == null || existing == registry.getDefault()) {
                        return null;
                    }
                    injectedObjectName = ((ForgeRegistry<Object>) registry).getKey(existing);
                } catch (IllegalAccessException var8) {
                    throw new RuntimeException(var8);
                }
            } else {
                try {
                    injectedObjectName = new ResourceLocation(injectedObject);
                } catch (ResourceLocationException var7) {
                    throw new IllegalArgumentException("Invalid @ObjectHolder annotation on \"" + field.toString() + "\"", var7);
                }
            }
            if (injectedObjectName == null) {
                throw new IllegalStateException(String.format(Locale.ENGLISH, "The ObjectHolder annotation cannot apply to a field that does not map to a registry. Ensure the registry was created during NewRegistryEvent. (found : %s at %s.%s)", field.getType().getName(), field.getDeclaringClass().getName(), field.getName()));
            } else {
                field.setAccessible(true);
                if (Modifier.isFinal(field.getModifiers())) {
                    throw new RuntimeException("@ObjectHolder on final field, our transformer did not run? " + field.getDeclaringClass().getName() + "/" + field.getName());
                } else {
                    return new ObjectHolderRef(registry, field, injectedObjectName);
                }
            }
        }
    }

    private ObjectHolderRef(ForgeRegistry<?> registry, Field field, ResourceLocation injectedObject) {
        this.registry = registry;
        this.field = field;
        this.injectedObject = injectedObject;
    }

    public void accept(Predicate<ResourceLocation> filter) {
        if (this.registry != null && filter.test(this.registry.getRegistryName())) {
            Object thing;
            if (this.registry.containsKey(this.injectedObject)) {
                thing = this.registry.getValue(this.injectedObject);
            } else {
                thing = null;
            }
            if (thing == null) {
                LOGGER.debug("Unable to lookup {} for {}. This means the object wasn't registered. It's likely just mod options.", this.injectedObject, this.field);
            } else {
                try {
                    this.field.set(null, thing);
                } catch (ReflectiveOperationException | IllegalArgumentException var4) {
                    LOGGER.warn("Unable to set {} with value {} ({})", this.field, thing, this.injectedObject, var4);
                }
            }
        }
    }

    public int hashCode() {
        return this.field.hashCode();
    }

    public boolean equals(Object other) {
        return !(other instanceof ObjectHolderRef o) ? false : this.field.equals(o.field);
    }
}