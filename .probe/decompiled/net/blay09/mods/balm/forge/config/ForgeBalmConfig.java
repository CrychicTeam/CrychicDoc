package net.blay09.mods.balm.forge.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.AbstractBalmConfig;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.config.ExpectedType;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.api.network.ConfigReflection;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForgeBalmConfig extends AbstractBalmConfig {

    private final Logger logger = LogManager.getLogger();

    private final Map<Class<?>, ModConfig> configs = new HashMap();

    private final Map<Class<?>, BalmConfigData> configData = new HashMap();

    private <T extends BalmConfigData> IConfigSpec<?> createConfigSpec(Class<T> clazz) {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        try {
            this.buildConfigSpec("", builder, clazz);
        } catch (IllegalAccessException var4) {
            throw new IllegalArgumentException("Config spec generation unexpectedly failed.", var4);
        }
        return builder.build();
    }

    private void buildConfigSpec(String parentPath, ForgeConfigSpec.Builder builder, Class<?> clazz) throws IllegalAccessException {
        List<Field> fields = ConfigReflection.getAllFields(clazz);
        Object defaults = this.createConfigDataInstance(clazz);
        for (Field field : fields) {
            Class<?> type = field.getType();
            Object defaultValue = field.get(defaults);
            String path = parentPath + field.getName();
            Comment comment = (Comment) field.getAnnotation(Comment.class);
            if (comment != null) {
                builder.comment(comment.value());
            }
            if (String.class.isAssignableFrom(type)) {
                builder.define(path, (String) defaultValue);
            } else if (List.class.isAssignableFrom(type)) {
                ExpectedType expectedType = (ExpectedType) field.getAnnotation(ExpectedType.class);
                if (expectedType == null) {
                    this.logger.warn("Config field without expected type, will not validate list content ({} in {})", field.getName(), clazz.getName());
                }
                builder.defineListAllowEmpty(Arrays.asList(path.split("\\.")), (Supplier) (() -> (List) defaultValue), it -> expectedType == null || expectedType.value().isAssignableFrom(it.getClass()) || expectedType.value().isEnum() && Arrays.stream(expectedType.value().getEnumConstants()).anyMatch(constant -> constant.toString().equals(it)));
            } else if (Enum.class.isAssignableFrom(type)) {
                builder.defineEnum(path, (Enum) defaultValue);
            } else if (int.class.isAssignableFrom(type)) {
                builder.defineInRange(path, (Integer) defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
            } else if (float.class.isAssignableFrom(type)) {
                builder.defineInRange(path, (double) ((Float) defaultValue).floatValue(), -Float.MAX_VALUE, Float.MAX_VALUE);
            } else if (double.class.isAssignableFrom(type)) {
                builder.defineInRange(path, (Double) defaultValue, -Double.MAX_VALUE, Double.MAX_VALUE);
            } else if (boolean.class.isAssignableFrom(type)) {
                builder.define(path, ((Boolean) defaultValue).booleanValue());
            } else if (long.class.isAssignableFrom(type)) {
                builder.defineInRange(path, (Long) defaultValue, Long.MIN_VALUE, Long.MAX_VALUE);
            } else {
                this.buildConfigSpec(path + ".", builder, type);
            }
        }
    }

    private <T extends BalmConfigData> T readConfigValues(Class<T> clazz, ModConfig config) {
        T instance = (T) this.createConfigDataInstance(clazz);
        try {
            this.readConfigValues("", instance, config);
        } catch (IllegalAccessException var5) {
            var5.printStackTrace();
        }
        return instance;
    }

    private <T> void readConfigValues(String parentPath, T instance, ModConfig config) throws IllegalAccessException {
        for (Field field : ConfigReflection.getAllFields(instance.getClass())) {
            String path = parentPath + field.getName();
            boolean hasValue = config.getConfigData().contains(path);
            Class<?> type = field.getType();
            try {
                if (hasValue && int.class.isAssignableFrom(type)) {
                    field.set(instance, config.getConfigData().getInt(path));
                } else if (hasValue && long.class.isAssignableFrom(type)) {
                    field.set(instance, config.getConfigData().getLong(path));
                } else if (hasValue && float.class.isAssignableFrom(type)) {
                    Object value = config.getConfigData().get(path);
                    if (value instanceof Double doubleValue) {
                        field.set(instance, doubleValue.floatValue());
                    } else if (value instanceof Float floatValue) {
                        field.set(instance, floatValue);
                    } else if (value instanceof Integer integerValue) {
                        field.set(instance, integerValue.floatValue());
                    } else {
                        this.logger.error("Invalid config value for " + path + ", expected " + type.getName() + " but got " + value.getClass());
                    }
                } else if (hasValue && double.class.isAssignableFrom(type)) {
                    Object value = config.getConfigData().get(path);
                    if (value instanceof Double doubleValue) {
                        field.set(instance, doubleValue);
                    } else if (value instanceof Float floatValue) {
                        field.set(instance, floatValue.doubleValue());
                    } else if (value instanceof Integer integerValue) {
                        field.set(instance, integerValue.doubleValue());
                    } else {
                        this.logger.error("Invalid config value for " + path + ", expected " + type.getName() + " but got " + value.getClass());
                    }
                } else if (!hasValue || !type.isPrimitive() && !String.class.isAssignableFrom(type) && !List.class.isAssignableFrom(type)) {
                    if (hasValue && type.isEnum()) {
                        Enum<?> value = config.getConfigData().getEnum(path, type);
                        field.set(instance, value);
                    } else {
                        this.readConfigValues(path + ".", field.get(instance), config);
                    }
                } else {
                    Object raw = config.getConfigData().getRaw(path);
                    if (raw != null) {
                        try {
                            field.set(instance, raw);
                        } catch (IllegalArgumentException var14) {
                            this.logger.error("Invalid config value for " + path + ", expected " + type.getName() + " but got " + raw.getClass());
                        }
                    } else {
                        this.logger.error("Null config value for " + path + ", falling back to default");
                    }
                }
            } catch (Exception var15) {
                this.logger.error("Unexpected error loading config value for " + path + ", falling back to default", var15);
            }
        }
    }

    private <T extends BalmConfigData> void writeConfigValues(ModConfig config, T configData) {
        try {
            this.writeConfigValues("", config, configData);
        } catch (IllegalAccessException var4) {
            var4.printStackTrace();
        }
    }

    private <T> void writeConfigValues(String parentPath, ModConfig config, T instance) throws IllegalAccessException {
        for (Field field : ConfigReflection.getAllFields(instance.getClass())) {
            String path = parentPath + field.getName();
            Class<?> type = field.getType();
            Object value = field.get(instance);
            if (!type.isPrimitive() && !Enum.class.isAssignableFrom(type) && !String.class.isAssignableFrom(type) && !List.class.isAssignableFrom(type)) {
                this.writeConfigValues(path + ".", config, field.get(instance));
            } else {
                config.getConfigData().set(path, value);
            }
        }
    }

    @Override
    public <T extends BalmConfigData> T initializeBackingConfig(Class<T> clazz) {
        IConfigSpec<?> configSpec = this.createConfigSpec(clazz);
        ModLoadingContext.get().registerConfig(Type.COMMON, configSpec);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(event -> {
            this.configs.put(clazz, event.getConfig());
            T newConfigData = this.readConfigValues(clazz, event.getConfig());
            this.configData.put(clazz, newConfigData);
            this.setActiveConfig(clazz, newConfigData);
        });
        FMLJavaModLoadingContext.get().getModEventBus().addListener(event -> {
            this.configs.put(clazz, event.getConfig());
            T newConfigData = this.readConfigValues(clazz, event.getConfig());
            this.configData.put(clazz, newConfigData);
            boolean hasSyncMessage = this.getConfigSyncMessageFactory(clazz) != null;
            boolean isHostingServer = ServerLifecycleHooks.getCurrentServer() != null;
            boolean isIngame = (Boolean) DistExecutor.runForDist(() -> () -> Minecraft.getInstance().gameMode != null, () -> () -> false);
            if (!hasSyncMessage || isHostingServer || !isIngame) {
                this.setActiveConfig(clazz, newConfigData);
            }
            Balm.getEvents().fireEvent(new ConfigReloadedEvent());
        });
        T initialData = (T) this.createConfigDataInstance(clazz);
        this.configData.put(clazz, initialData);
        return initialData;
    }

    @Override
    public <T extends BalmConfigData> T getBackingConfig(Class<T> clazz) {
        return (T) this.configData.get(clazz);
    }

    @Override
    public <T extends BalmConfigData> void saveBackingConfig(Class<T> clazz) {
        ModConfig modConfig = (ModConfig) this.configs.get(clazz);
        if (modConfig != null) {
            this.writeConfigValues(modConfig, (T) this.configData.get(clazz));
            modConfig.save();
        }
    }

    @Override
    public File getConfigDir() {
        return FMLPaths.CONFIGDIR.get().toFile();
    }
}