package mezz.jei.api.runtime.config;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IJeiConfigValueSerializer<T> {

    String serialize(T var1);

    IJeiConfigValueSerializer.IDeserializeResult<T> deserialize(String var1);

    boolean isValid(T var1);

    Optional<Collection<T>> getAllValidValues();

    String getValidValuesDescription();

    public interface IDeserializeResult<T> {

        Optional<T> getResult();

        List<String> getErrors();
    }
}