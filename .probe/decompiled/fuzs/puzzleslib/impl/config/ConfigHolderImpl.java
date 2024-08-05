package fuzs.puzzleslib.impl.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ConfigDataHolder;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraftforge.fml.config.ModConfig.Type;

public abstract class ConfigHolderImpl implements ConfigHolder.Builder {

    private final String modId;

    private Map<Class<?>, ConfigDataHolderImpl<?>> configsByClass = Maps.newIdentityHashMap();

    protected ConfigHolderImpl(String modId) {
        this.modId = modId;
    }

    private static <T extends ConfigCore> Supplier<T> construct(Class<T> clazz) {
        return () -> {
            try {
                return (ConfigCore) MethodHandles.publicLookup().findConstructor(clazz, MethodType.methodType(void.class)).invoke();
            } catch (Throwable var2) {
                throw new RuntimeException(var2);
            }
        };
    }

    @Override
    public <T extends ConfigCore> ConfigDataHolder<T> getHolder(Class<T> clazz) {
        ConfigDataHolderImpl<?> holder = (ConfigDataHolderImpl<?>) this.configsByClass.get(clazz);
        Objects.requireNonNull(holder, "No config holder available for type " + clazz);
        return (ConfigDataHolder<T>) holder;
    }

    @Override
    public <T extends ConfigCore> ConfigHolder.Builder client(Class<T> clazz) {
        Supplier<T> supplier = ModLoaderEnvironment.INSTANCE.isClient() ? construct(clazz) : () -> null;
        if (this.configsByClass.put(clazz, new ConfigDataHolderImpl(Type.CLIENT, supplier)) != null) {
            throw new IllegalStateException("Duplicate registration for client config of type " + clazz);
        } else {
            return this;
        }
    }

    @Override
    public <T extends ConfigCore> ConfigHolder.Builder common(Class<T> clazz) {
        if (this.configsByClass.put(clazz, new ConfigDataHolderImpl(Type.COMMON, construct(clazz))) != null) {
            throw new IllegalStateException("Duplicate registration for common config of type " + clazz);
        } else {
            return this;
        }
    }

    @Override
    public <T extends ConfigCore> ConfigHolder.Builder server(Class<T> clazz) {
        if (this.configsByClass.put(clazz, new ConfigDataHolderImpl(Type.SERVER, construct(clazz))) != null) {
            throw new IllegalStateException("Duplicate registration for server config of type " + clazz);
        } else {
            return this;
        }
    }

    @Override
    public <T extends ConfigCore> ConfigHolder.Builder setFileName(Class<T> clazz, UnaryOperator<String> fileName) {
        ConfigDataHolderImpl<T> holder = (ConfigDataHolderImpl<T>) this.<T>getHolder(clazz);
        holder.fileName = fileName;
        return this;
    }

    @Override
    public void build() {
        this.configsByClass = ImmutableMap.copyOf(this.configsByClass);
        for (ConfigDataHolderImpl<?> holder : this.configsByClass.values()) {
            if (holder.config != null) {
                this.bake(holder, this.modId);
            }
        }
    }

    abstract void bake(ConfigDataHolderImpl<?> var1, String var2);
}