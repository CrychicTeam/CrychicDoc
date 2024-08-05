package mezz.jei.api.runtime.config;

public interface IJeiConfigValue<T> {

    String getName();

    String getDescription();

    T getValue();

    T getDefaultValue();

    boolean set(T var1);

    IJeiConfigValueSerializer<T> getSerializer();
}