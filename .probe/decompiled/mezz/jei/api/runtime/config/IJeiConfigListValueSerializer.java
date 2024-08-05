package mezz.jei.api.runtime.config;

import java.util.List;

public interface IJeiConfigListValueSerializer<T> extends IJeiConfigValueSerializer<List<T>> {

    IJeiConfigValueSerializer<T> getListValueSerializer();
}