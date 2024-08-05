package me.shedaniel.autoconfig.serializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.util.Utils;

public final class PartitioningSerializer<T extends PartitioningSerializer.GlobalData, M extends ConfigData> implements ConfigSerializer<T> {

    private Class<T> configClass;

    private Map<Field, ConfigSerializer<M>> serializers;

    private PartitioningSerializer(Config definition, Class<T> configClass, ConfigSerializer.Factory<M> factory) {
        this.configClass = configClass;
        this.serializers = (Map<Field, ConfigSerializer<M>>) getModuleFields(configClass).stream().collect(Utils.toLinkedMap(Function.identity(), field -> factory.create(createDefinition(String.format("%s/%s", definition.name(), ((Config) field.getType().getAnnotation(Config.class)).name())), field.getType())));
    }

    public static <T extends PartitioningSerializer.GlobalData, M extends ConfigData> ConfigSerializer.Factory<T> wrap(ConfigSerializer.Factory<M> inner) {
        return (definition, configClass) -> new PartitioningSerializer<>(definition, configClass, inner);
    }

    private static Config createDefinition(String name) {
        return new Config() {

            public Class<? extends Annotation> annotationType() {
                return Config.class;
            }

            @Override
            public String name() {
                return name;
            }

            public int hashCode() {
                return "name".hashCode() * 127 ^ this.name().hashCode();
            }

            public boolean equals(Object obj) {
                return obj instanceof Config && ((Config) obj).name().equals(this.name());
            }
        };
    }

    private static boolean isValidModule(Field field) {
        return ConfigData.class.isAssignableFrom(field.getType()) && field.getType().isAnnotationPresent(Config.class);
    }

    private static List<Field> getModuleFields(Class<?> configClass) {
        return (List<Field>) Arrays.stream(configClass.getDeclaredFields()).filter(PartitioningSerializer::isValidModule).collect(Collectors.toList());
    }

    public void serialize(T config) throws ConfigSerializer.SerializationException {
        for (Entry<Field, ConfigSerializer<M>> entry : this.serializers.entrySet()) {
            ((ConfigSerializer) entry.getValue()).serialize(Utils.getUnsafely((Field) entry.getKey(), config));
        }
    }

    public T deserialize() throws ConfigSerializer.SerializationException {
        T ret = this.createDefault();
        for (Entry<Field, ConfigSerializer<M>> entry : this.serializers.entrySet()) {
            Utils.setUnsafely((Field) entry.getKey(), ret, ((ConfigSerializer) entry.getValue()).deserialize());
        }
        return ret;
    }

    public T createDefault() {
        return Utils.constructUnsafely(this.configClass);
    }

    public abstract static class GlobalData implements ConfigData {

        public GlobalData() {
            Arrays.stream(this.getClass().getDeclaredFields()).filter(field -> !PartitioningSerializer.isValidModule(field)).forEach(field -> {
                throw new RuntimeException(String.format("Invalid module: %s", field));
            });
        }

        @Override
        public final void validatePostLoad() throws ConfigData.ValidationException {
            for (Field moduleField : PartitioningSerializer.getModuleFields(this.getClass())) {
                Utils.<ConfigData>getUnsafely(moduleField, this).validatePostLoad();
            }
        }
    }
}